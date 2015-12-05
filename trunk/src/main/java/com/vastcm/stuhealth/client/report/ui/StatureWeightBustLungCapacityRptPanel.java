package com.vastcm.stuhealth.client.report.ui;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.grid.CellSpanTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TableColumnGroup;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.framework.Pair;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.ui.PrintTableUtil;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.utils.BigDecimalUtil;
import com.vastcm.stuhealth.client.utils.JTableExportParam;
import com.vastcm.stuhealth.client.utils.MyTableUtils;
import com.vastcm.stuhealth.client.utils.biz.SchoolTermItem4UI;
import com.vastcm.swing.print.table.ExtPageFormat;
import com.vastcm.swing.print.table.PageBorder;
import com.vastcm.swing.print.table.PrintTable;

/**
 * 身高体重胸围肺活量
 * @author bob
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatureWeightBustLungCapacityRptPanel extends ReportBasePanel {
	private static final long serialVersionUID = 1L;

	private static final int StatureIndex = 0;
	private static final int WeightIndex = 1;
	private static final int BustIndex = 2;
	private static final int LungCapacityIndex = 3;

	private boolean isStatGrade = true;//起缓存作用
	private String[] itemNames = new String[] { "身高(cm)", "体重(kg)", "胸围(cm)", "肺活量(ml)" };
	private String[] eachItemNames = new String[] { "实检人数", "平均数", "标准差", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%" };
	private boolean[] isItemSelected = new boolean[] { true, true, true, true };
	private int totalStatSize;
	private SchoolTreeNode schoolNode;
	private SchoolTermItem4UI schoolTermItem;
	private int scale = BigDecimalUtil.Default_Scale;
	private JLabel rightLbl;
	private JLabel leftLbl;
	private String emptyStr = "        ";

	public StatureWeightBustLungCapacityRptPanel() {
		initComponents();

		setFilterBasePanel(new StatureWeightBustLungCapacityFilterPanel());
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		itemNames = new String[] { "身高(cm)", "体重(kg)", "胸围(cm)", "肺活量(ml)" };
		eachItemNames = new String[] { "实检人数", "平均数", "标准差", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%", "人数", "占受检%" };
		isItemSelected = new boolean[] { true, true, true, true };
		rptParamInfo = new RptParamInfo();
		recreateColumn();
	}

	private void recreateColumn() {
		totalStatSize = 0;
		for (int i = 0; i < isItemSelected.length; i++) {
			if (isItemSelected[i])
				totalStatSize++;
		}
		String[] columnNames = new String[3 + eachItemNames.length * totalStatSize];
		int[] sumColumnIndexs = new int[1 + totalStatSize * 6];
		int index = 0, sumIndex = 0;
		columnNames[index++] = isStatGrade ? "年级" : "年龄";
		columnNames[index++] = "性别";
		sumColumnIndexs[sumIndex++] = index;//合计列
		columnNames[index++] = "学生人数";
		for (int i = 0; i < totalStatSize; i++) {
			for (int j = 0; j < eachItemNames.length; j++) {
				if (j == 0 || j == 3 || j == 5 || j == 7 || j == 9 || j == 11) {
					sumColumnIndexs[sumIndex++] = index;//合计列
				}
				columnNames[index++] = eachItemNames[j];
			}
		}
		rptParamInfo.setSumColumnIndexs(sumColumnIndexs);
		rptParamInfo.setColumnNames(columnNames);
	}

	@Override
	protected void initTable(boolean isRebuild) {
		tblMain.setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
		super.initTable(isRebuild);
		TableColumnGroup tempGroup;
		int colIndex = 3;
		for (int i = 0; i < isItemSelected.length; i++) {
			if (!isItemSelected[i])
				continue;

			tempGroup = new TableColumnGroup(itemNames[i]);
			tempGroup.add(tblMain.getColumnModel().getColumn(colIndex++));
			tempGroup.add(tblMain.getColumnModel().getColumn(colIndex++));
			tempGroup.add(tblMain.getColumnModel().getColumn(colIndex++));
			MyTableUtils.addColumnGroup2Table(tblMain, tempGroup, "上等", colIndex++, colIndex++);
			MyTableUtils.addColumnGroup2Table(tblMain, tempGroup, "中上等", colIndex++, colIndex++);
			MyTableUtils.addColumnGroup2Table(tblMain, tempGroup, "中等", colIndex++, colIndex++);
			MyTableUtils.addColumnGroup2Table(tblMain, tempGroup, "中下等", colIndex++, colIndex++);
			MyTableUtils.addColumnGroup2Table(tblMain, tempGroup, "下等", colIndex++, colIndex++);
			((NestedTableHeader) tblMain.getTableHeader()).addColumnGroup(tempGroup);
		}
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		isItemSelected = (boolean[]) filterParams.get(StatureWeightBustLungCapacityFilterPanel.Param_Stat_Item);
		boolean tempBoolean = ((StatureWeightBustLungCapacityFilterPanel) getFilterBasePanel()).isStatGrade();
		if (isStatGrade != tempBoolean) {
			isStatGrade = tempBoolean;
		}
		String tableEndStr = isStatGrade ? "grade" : "age";
		String selectFieldStr = isStatGrade ? "(select Grade.ShortName from Grade where rpt.grade=Grade.GradeCode) grade, grade orderFiled"
				: "age, age orderFiled";
		String fieldStr = isStatGrade ? "grade" : "age";

		StringBuffer sql = new StringBuffer(1000);
		sql.append(" select ").append(selectFieldStr).append(", XB, sum(case when RID='01' then XS else 0 end) XS");//学生总人数只需要取其中一行记录即可
		for (int i = 0; i < isItemSelected.length; i++) {
			if (!isItemSelected[i])
				continue;
			appendInnerSelectField(sql, i);
		}
		sql.append(" from Report_TB_").append(tableEndStr).append(" rpt \n");
		sql.append(" where 1=1 ");

		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		//不会显示班级，返回的schoolNode不会是班级
		Pair<SchoolTreeNode, DefaultMutableTreeNode> pair = FilterCommonAction.processFilter4SchoolRange(filterParams, sql, hqlParam);
		schoolNode = pair.getKey();
		schoolTermItem = FilterCommonAction.processFilter4SchoolTerm(filterParams, sql, hqlParam);
		String schoolType = FilterCommonAction.processFilter4SchoolType(filterParams, sql, hqlParam);
		FilterCommonAction.processFilter4Gender(filterParams, sql, hqlParam);
		FilterCommonAction.processFilter4QueryPair(filterParams, sql, hqlParam);
		sql.append(" and ").append(fieldStr).append("<>'HJ' ");
		sql.append("\n group by ").append(fieldStr).append(", XB");

		StringBuffer outerSql = new StringBuffer(500);
		outerSql.append(" select ").append(fieldStr).append(", XB, XS");
		for (int i = 0; i < isItemSelected.length; i++) {
			if (!isItemSelected[i])
				continue;
			appendOuterSelectField(outerSql, i);
		}
		outerSql.append("\n from (").append(sql);
		outerSql.append("\n) tempResult\n order by orderFiled+0, XB desc");
		rptParamInfo.setQuerySQL(outerSql.toString());

		leftLbl.setText(emptyStr + "填报学校：" + FilterCommonAction.getPrintMsg4SchoolRange(schoolType, schoolNode));
		rightLbl.setText(schoolTermItem.toString());
	}

	private void appendInnerSelectField(StringBuffer sql, int index) {
		String rid;
		if (index == StatureIndex || index == WeightIndex) {//身高或体重
			rid = "01";
		} else {
			rid = "02";
		}
		StringBuffer tempSql = new StringBuffer();
		if (index == StatureIndex || index == BustIndex) {//身高或胸围
			tempSql.append("\n , sum(case when RID='%2$s' then H1 else 0 end) H1_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H2 else 0 end) H2_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H3 else 0 end) H3_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H4 else 0 end) H4_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H5 else 0 end) H5_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H6 else 0 end) H6_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H15B else 0 end) avg_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H16B else 0 end) standard_%1$s");
		} else {
			tempSql.append("\n , sum(case when RID='%2$s' then H7 else 0 end) H7_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H8 else 0 end) H8_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H9 else 0 end) H9_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H10 else 0 end) H10_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H11 else 0 end) H11_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H12 else 0 end) H12_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H17B else 0 end) avg_%1$s");
			tempSql.append("\n , sum(case when RID='%2$s' then H18B else 0 end) standard_%1$s");
		}
		sql.append(String.format(tempSql.toString(), index, rid));
	}

	private void appendOuterSelectField(StringBuffer sql, int index) {
		//参数1为index
		StringBuffer tempSql = new StringBuffer();
		if (index == StatureIndex || index == BustIndex) {//身高或胸围
			tempSql.append("\n , H1_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(avg_%1$s/H1_%1$s, %2$s) end avg_%1$s");
			//case when h1<2 or standard_=0 then 0 else round(sqrt((standard_-avg_*avg_/h1)/(h1-1)),2) end standard_rid
			tempSql.append("\n , case when H1_%1$s<2 or standard_%1$s=0 then 0 else round(sqrt((standard_%1$s-avg_%1$s*avg_%1$s/H1_%1$s)/(H1_%1$s-1)), %2$s) end standard_%1$s");
			tempSql.append("\n , H2_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(H2_%1$s*100/H1_%1$s, %2$s) end rate2_%1$s");
			tempSql.append("\n , H3_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(H3_%1$s*100/H1_%1$s, %2$s) end rate3_%1$s");
			tempSql.append("\n , H4_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(H4_%1$s*100/H1_%1$s, %2$s) end rate4_%1$s");
			tempSql.append("\n , H5_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(H5_%1$s*100/H1_%1$s, %2$s) end rate5_%1$s");
			tempSql.append("\n , H6_%1$s");
			tempSql.append("\n , case when H1_%1$s=0 then 0 else round(H6_%1$s*100/H1_%1$s, %2$s) end rate6_%1$s");
		} else {
			tempSql.append("\n , H7_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(avg_%1$s/H7_%1$s, %2$s) end avg_%1$s");
			//case when h1<2 or standard_=0 then 0 else round(sqrt((standard_-avg_*avg_/h1)/(h1-1)),2) end standard_rid
			tempSql.append("\n , case when H7_%1$s<2 or standard_%1$s=0 then 0 else round(sqrt((standard_%1$s-avg_%1$s*avg_%1$s/H7_%1$s)/(H7_%1$s-1)), %2$s) end standard_%1$s");
			tempSql.append("\n , H8_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(H8_%1$s*100/H7_%1$s, %2$s) end rate8_%1$s");
			tempSql.append("\n , H9_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(H9_%1$s*100/H7_%1$s, %2$s) end rate9_%1$s");
			tempSql.append("\n , H10_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(H10_%1$s*100/H7_%1$s, %2$s) end rate10_%1$s");
			tempSql.append("\n , H11_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(H11_%1$s*100/H7_%1$s, %2$s) end rate11_%1$s");
			tempSql.append("\n , H12_%1$s");
			tempSql.append("\n , case when H7_%1$s=0 then 0 else round(H12_%1$s*100/H7_%1$s, %2$s) end rate12_%1$s");
		}
		sql.append(String.format(tempSql.toString(), index, scale));
	}

	@Override
	public void loadReportData(List datas) {
		recreateColumn();
		RptDefaultTableModel tableModel = new RptDefaultTableModel((Object[][]) datas.toArray(new Object[][] {}), rptParamInfo.getColumnNames());
		tableModel.calculatGroupSum2(0, 1, rptParamInfo.getSumColumnIndexs());
		//		CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
		//		for (int i = 0; i < totalStatSize; i++) {
		//			int startIndex = 3 + eachItemNames.length * i;
		//			//TODO zrb 是否要转成使用CalculatedTableModel
		//		}

		Map<Integer, Object> statRowIndexMap = tableModel.getStatRowIndexMap();
		Iterator<Integer> it = statRowIndexMap.keySet().iterator();
		Vector dataVector = tableModel.getDataVector();
		while (it.hasNext()) {
			Vector rowData = (Vector) dataVector.get(it.next());
			for (int i = 0; i < totalStatSize; i++) {
				int startIndex = 3 + eachItemNames.length * i;
				rowData.setElementAt(
						BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(startIndex + 3), BigDecimalUtil.HUNDRED), rowData.get(startIndex)),
						startIndex + 4);//上等占比
				rowData.setElementAt(
						BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(startIndex + 5), BigDecimalUtil.HUNDRED), rowData.get(startIndex)),
						startIndex + 6);//中上等占比
				rowData.setElementAt(
						BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(startIndex + 7), BigDecimalUtil.HUNDRED), rowData.get(startIndex)),
						startIndex + 8);//中等占比
				rowData.setElementAt(
						BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(startIndex + 9), BigDecimalUtil.HUNDRED), rowData.get(startIndex)),
						startIndex + 10);//中下等占比
				rowData.setElementAt(
						BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(startIndex + 11), BigDecimalUtil.HUNDRED), rowData.get(startIndex)),
						startIndex + 12);//下等占比
			}
		}
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(tableModel);
		tblMain.setModel(sortableTreeTableModel);
		initTable(true);
	}

	protected void processTableExportParam(JTableExportParam param) {
		param.setTableStyle(JTableExportParam.TableStyle.Three_Line);
		MessageFormat left = new MessageFormat("填报学校：");
		if (schoolNode != null) {
			left = new MessageFormat("填报学校：" + schoolNode.getName());
		}
		MessageFormat right = new MessageFormat("");
		if (schoolTermItem != null) {
			right = new MessageFormat(schoolTermItem.toString());
		}
		param.addHeaderMessage(left, null, right);
	}

	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {
		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		PrintTableUtil.addHeadSubTitle(pageFormat, 1);
		PageBorder headSubTitle = pageFormat.getHeadSubTitle(0);
		headSubTitle.setLeftContent(leftLbl.getText());
		headSubTitle.setRightContent(rightLbl.getText());
	}

	private void initComponents() {
		JPanel reportHead = new JPanel();
		reportHead.setLayout(new java.awt.GridLayout(1, 2, 10, 10));
		leftLbl = new JLabel(emptyStr + "填报学校：");
		reportHead.add(leftLbl);
		rightLbl = new JLabel("");
		reportHead.add(rightLbl);
		tblPanel.add(reportHead, java.awt.BorderLayout.NORTH);
	}
}