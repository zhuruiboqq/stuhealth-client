package com.vastcm.stuhealth.client.report.ui;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.util.StringUtils;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.utils.BigDecimalUtil;
import com.vastcm.stuhealth.client.utils.biz.FixItem4UI;

public class FixItemRptPanel extends ReportBasePanel {

	private static final long serialVersionUID = 1L;
	private static int Start_Row_Index = 1;
	private static int Start_Column_Index = 1;

	public FixItemRptPanel() {
		initComponents();

		setFilterBasePanel(new FixItemFilterPanel());
		tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		rptParamInfo = new RptParamInfo();
		rptParamInfo.setColumnNames(new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z" });
	}

	@Override
	protected void initTable(boolean isRebuild) {
		super.initTable(isRebuild);
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		StringBuffer filterBuf = new StringBuffer(100);
		FilterCommonAction.processFilter4SchoolRange(filterParams, filterBuf, hqlParam, "result.schoolBH", "result.classBH", "result.classLongNo");
		FilterCommonAction.processFilter4SchoolTerm(filterParams, filterBuf, hqlParam, "result.tjnd", "result.term");

		Object[] fixItemArray = (Object[]) filterParams.get(FixItemFilterPanel.Param_Fix_Item_List);
		StringBuffer sql = new StringBuffer(1000);
		for (int i = 0; i < fixItemArray.length; i++) {
			appendFixItemSQL(sql, ((FixItem4UI) fixItemArray[i]).getCode(), filterBuf.toString());
			if (i != fixItemArray.length - 1)
				sql.append("union all \n");
		}

		rptParamInfo.setQuerySQL(sql.toString());
	}

	private void appendFixItemSQL(StringBuffer sql, String checkItemCode, String resultFilter) {
		sql.append(" select hw_checkitem.ItemName itemName, itemResult.ItemResult itemResult  \n");
		sql.append(" , sum(case when result.id is not null then 1 else 0 end) qty             \n");
		sql.append(" from itemResult                                                          \n");
		sql.append(" inner join hw_checkitem on itemResult.FieldMc = hw_checkitem.FieldName   \n");
		sql.append(" left join result on itemResult.itemCode = result.").append(checkItemCode).append(" \n");
		if (StringUtils.hasText(resultFilter)) {
			sql.append(resultFilter).append("\n");
		}
		sql.append(" where itemResult.FieldMc = '").append(checkItemCode).append("'           \n");
		sql.append(" group by hw_checkitem.ItemName,itemResult.ItemResult                     \n");
	}

	@Override
	public void loadReportData(List datas) throws Exception {
		int defaultSize = rptParamInfo.getColumnNames().length;

		Vector<Vector<Object>> allDataVector = new Vector<Vector<Object>>(20);
		Vector<Object> rowData1Vector = null, rowData2Vector = null;
		for (int i = 0; i < Start_Row_Index; i++) {//添加空白行
			rowData1Vector = new Vector<Object>();
			rowData1Vector.setSize(defaultSize);
			allDataVector.add(rowData1Vector);
		}
		String itemName = null;
		int index = -1;
		BigDecimal totalQty = BigDecimal.ZERO, qty;
		Iterator iterator = datas.iterator();
		// 肝                       总参检人数                 正常(人数)      正常(%)         异常(人数)          异常(%)
		//          10          9            90%            1                10%
		while (iterator.hasNext()) {
			Object[] rowDataArray = (Object[]) iterator.next();
			String itemNameTemp = String.valueOf(rowDataArray[0]);

			if (!itemNameTemp.equals(itemName)) {//新的定性项目
				if (index != -1) {//不是第一个，要添加到记录中
					addNewData(defaultSize, allDataVector, rowData1Vector, rowData2Vector, totalQty, index);
				}
				//初始化新的项目行
				index = 1;
				totalQty = BigDecimal.ZERO;
				rowData1Vector = new Vector<Object>();
				rowData1Vector.setSize(defaultSize);
				rowData2Vector = new Vector<Object>();
				rowData2Vector.setSize(defaultSize);

				rowData1Vector.set(Start_Column_Index, itemNameTemp);
				rowData1Vector.set(Start_Column_Index + 1, "总参检人数");
			}
			String itemResult = String.valueOf(rowDataArray[1]);
			qty = (BigDecimal) rowDataArray[2];
			totalQty = totalQty.add(qty);
			rowData1Vector.set(Start_Column_Index + index * 2, itemResult + "(人数)");
			rowData1Vector.set(Start_Column_Index + index * 2 + 1, itemResult + "(%)");
			rowData2Vector.set(Start_Column_Index + index * 2, qty);

			itemName = itemNameTemp;
			index++;
		}
		//处理最后一个
		if (index != -1) {
			addNewData(defaultSize, allDataVector, rowData1Vector, rowData2Vector, totalQty, index);
		}
		RptDefaultTableModel rptDefaultTableModel = new RptDefaultTableModel(allDataVector, convertToVector(rptParamInfo.getColumnNames()));
		tblMain.setModel(rptDefaultTableModel);

		initTable(true);
	}

	private void addNewData(int defaultSize, Vector<Vector<Object>> allDataVector, Vector<Object> rowData1Vector, Vector<Object> rowData2Vector,
			BigDecimal totalQty, int index) {
		//先添加一行空行
		Vector<Object> rowData3Vector = new Vector<Object>();
		rowData3Vector.setSize(defaultSize);
		allDataVector.add(rowData3Vector);
		//设置总人数
		rowData2Vector.set(Start_Column_Index + 1, totalQty);
		//计算比例
		if (!BigDecimalUtil.isZero(totalQty)) {
			for (int i = 1; i < index; i++) {
				BigDecimal qty = (BigDecimal) rowData2Vector.get(Start_Column_Index + i * 2);
				rowData2Vector.set(Start_Column_Index + i * 2 + 1, BigDecimalUtil.divide(BigDecimalUtil.HUNDRED.multiply(qty), totalQty));
			}
		}

		allDataVector.add(rowData1Vector);
		allDataVector.add(rowData2Vector);
	}

	/**
	 * Returns a vector that contains the same objects as the array.
	 * @param anArray the array to be converted
	 * @return the new vector; if <code>anArray</code> is <code>null</code>, returns <code>null</code>
	 */
	protected static Vector convertToVector(Object[] anArray) {
		if (anArray == null) {
			return null;
		}
		Vector v = new Vector(anArray.length);
		for (int i = 0; i < anArray.length; i++) {
			v.addElement(anArray[i]);
		}
		return v;
	}

	private void initComponents() {
	}
}