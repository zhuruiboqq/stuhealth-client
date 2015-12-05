package com.vastcm.stuhealth.client.report.ui;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Date;
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
import com.vastcm.stuhealth.client.framework.report.ui.ReportBase4MultiPanel;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.utils.BigDecimalUtil;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.JTableExportParam;
import com.vastcm.stuhealth.client.utils.MyTableUtils;
import com.vastcm.swing.print.table.ExtPageFormat;
import com.vastcm.swing.print.table.PageBorder;
import com.vastcm.swing.print.table.PrintTable;

/**
 * 学生健康体检统计汇总表（四个表：视力、牙齿、内科、其他）
 * @author bob
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StudentExamineStatSumRptPanel extends ReportBase4MultiPanel {
	private static final long serialVersionUID = 1L;

	private boolean isStatGrade = true;
	private static final int SightRptIndex = 0;
	private static final int ToolRptIndex = 1;
	private static final int InternalMedicineRptIndex = 2;
	private static final int OtherRptIndex = 3;
	private int scale = BigDecimalUtil.Default_Scale;
	private JLabel left1Lbl;
	private JLabel right1Lbl;
	private JLabel mid1Lbl;
	private JLabel left2Lbl;
	private JLabel mid2Lbl;
	private JLabel right2Lbl;

	public StudentExamineStatSumRptPanel() {
		initComponents();

		setFilterBasePanel(new StudentExamineStatSumFilterPanel());
	}

	@Override
	public String[] getReportTabName() {
		return new String[] { "视力健康汇总表", "牙齿健康汇总表", "内科健康汇总表", "其他健康汇总表" };
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		int count;
		int[] sumColumnIndexs;

		rptParamInfos[SightRptIndex] = new RptParamInfo();
		rptParamInfos[SightRptIndex].setColumnNames(new String[] { "年级", "排序", "性别", "受检人数", "人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%",
				"人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%" });
		count = 10;
		sumColumnIndexs = new int[1 + count];
		sumColumnIndexs[0] = 3;
		setSumColumnIndex(sumColumnIndexs, 1, 4, count, 2);
		rptParamInfos[SightRptIndex].setSumColumnIndexs(sumColumnIndexs);

		rptParamInfos[ToolRptIndex] = new RptParamInfo();
		rptParamInfos[ToolRptIndex].setColumnNames(new String[] { "年级", "排序", "性别", "受检人数", "人数", "%", "牙数", "龋均", "人数", "%", "牙数", "牙数", "牙数", "%",
				"龋均", "人数", "%", "牙数", "牙数", "牙数", "%", "龋均", "人数", "%" });
		count = 13;
		sumColumnIndexs = new int[1 + count];
		sumColumnIndexs[0] = 3;
		sumColumnIndexs[1] = 4;
		setSumColumnIndex(sumColumnIndexs, 2, 6, 3, 1);
		setSumColumnIndex(sumColumnIndexs, 5, 10, 3, 1);
		sumColumnIndexs[8] = 14;
		sumColumnIndexs[9] = 15;
		setSumColumnIndex(sumColumnIndexs, 10, 17, 3, 1);
		sumColumnIndexs[13] = 22;
		rptParamInfos[ToolRptIndex].setSumColumnIndexs(sumColumnIndexs);

		rptParamInfos[InternalMedicineRptIndex] = new RptParamInfo();
		rptParamInfos[InternalMedicineRptIndex].setColumnNames(new String[] { "年级", "排序", "性别", "受检人数", "人数", "%", "人数", "%", "人数", "%", "人数", "%",
				"人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%" });
		count = 10;
		sumColumnIndexs = new int[1 + count];
		sumColumnIndexs[0] = 3;
		setSumColumnIndex(sumColumnIndexs, 1, 4, count, 2);
		rptParamInfos[InternalMedicineRptIndex].setSumColumnIndexs(sumColumnIndexs);

		rptParamInfos[OtherRptIndex] = new RptParamInfo();
		rptParamInfos[OtherRptIndex].setColumnNames(new String[] { "年级", "排序", "性别", "受检人数", "人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%",
				"人数", "%", "人数", "%", "人数", "%", "人数", "%", "人数", "%" });
		count = 10;
		sumColumnIndexs = new int[1 + count];
		sumColumnIndexs[0] = 3;
		setSumColumnIndex(sumColumnIndexs, 1, 4, count, 2);
		rptParamInfos[OtherRptIndex].setSumColumnIndexs(sumColumnIndexs);

	}

	private void setSumColumnIndex(int[] sumColumnIndexs, int startArrayIndex, int columnStartIndex, int count, int columnStep) {
		for (int i = 0; i < count; i++) {
			sumColumnIndexs[startArrayIndex++] = columnStartIndex;
			columnStartIndex += columnStep;
		}
	}

	@Override
	protected void initTable(boolean isRebuild) {
		tblMains[SightRptIndex].setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
		tblMains[ToolRptIndex].setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
		tblMains[InternalMedicineRptIndex].setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
		tblMains[OtherRptIndex].setAutoCellMerge(CellSpanTable.AUTO_CELL_MERGE_ROWS_LIMITED);
		super.initTable(isRebuild);
		TableColumnGroup tempGroup;
		int defaultColumnGroupIndex = 4;
		int colIndex = defaultColumnGroupIndex;
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], "体检无异常", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], "视力正常", colIndex++, colIndex++);
		tempGroup = new TableColumnGroup("视力不良");
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], tempGroup, "4.9", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], tempGroup, "4.6-4.8", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], tempGroup, "4.5以下", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], tempGroup, "合计", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], tempGroup, "新发病", colIndex++, colIndex++);
		((NestedTableHeader) tblMains[SightRptIndex].getTableHeader()).addColumnGroup(tempGroup);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], "色盲色弱", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], "沙眼", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[SightRptIndex], "结膜炎", colIndex++, colIndex++);

		colIndex = defaultColumnGroupIndex;
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], "龋齿(含乳恒牙)", colIndex++, colIndex++, colIndex++, colIndex++);
		tempGroup = new TableColumnGroup("恒 牙 龋");
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "DMF", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "D", colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "M", colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "F", colIndex++, colIndex++);
		tempGroup.add(tblMains[ToolRptIndex].getColumnModel().getColumn(colIndex++));
		((NestedTableHeader) tblMains[ToolRptIndex].getTableHeader()).addColumnGroup(tempGroup);
		tempGroup = new TableColumnGroup("乳 牙 龋");
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "DMF", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "D", colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "M", colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], tempGroup, "F", colIndex++, colIndex++);
		tempGroup.add(tblMains[ToolRptIndex].getColumnModel().getColumn(colIndex++));
		((NestedTableHeader) tblMains[ToolRptIndex].getTableHeader()).addColumnGroup(tempGroup);
		MyTableUtils.addColumnGroup2Table(tblMains[ToolRptIndex], "牙周病", colIndex++, colIndex++);

		colIndex = defaultColumnGroupIndex;
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "超重", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "肥胖", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "低血红蛋白", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "结菌试验阳性", colIndex++, colIndex++);
		tempGroup = new TableColumnGroup("肝功能异常");
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], tempGroup, "谷丙转氨酶阳性", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], tempGroup, "胆红素阳性", colIndex++, colIndex++);
		((NestedTableHeader) tblMains[InternalMedicineRptIndex].getTableHeader()).addColumnGroup(tempGroup);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "高血压", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "甲状腺肿大", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "脊柱侧弯", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[InternalMedicineRptIndex], "肠蛔虫病", colIndex++, colIndex++);

		colIndex = defaultColumnGroupIndex;
		tempGroup = new TableColumnGroup("其它异常情况");
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "心", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "肝", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "脾", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "肺", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "四肢", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "皮肤", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "听力", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "耳", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "鼻", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMains[OtherRptIndex], tempGroup, "扁桃体", colIndex++, colIndex++);
		((NestedTableHeader) tblMains[OtherRptIndex].getTableHeader()).addColumnGroup(tempGroup);

		MyTableUtils.hiddenColumn2(tblMains[SightRptIndex], 1);
		MyTableUtils.hiddenColumn2(tblMains[ToolRptIndex], 1);
		MyTableUtils.hiddenColumn2(tblMains[InternalMedicineRptIndex], 1);
		MyTableUtils.hiddenColumn2(tblMains[OtherRptIndex], 1);
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);

		Map<String, Object> filterParams = rptParamInfos[DefaultReportIndex].getFilterParam();
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		StringBuffer filterSB = new StringBuffer(200);
		//不会显示班级，返回的schoolNode不会是班级
		Pair<SchoolTreeNode, DefaultMutableTreeNode> pair = FilterCommonAction.processFilter4SchoolRange(filterParams, filterSB, hqlParam);
		SchoolTreeNode schoolNode = pair.getKey();
		FilterCommonAction.processFilter4SchoolTerm(filterParams, filterSB, hqlParam);
		String schoolType = FilterCommonAction.processFilter4SchoolType(filterParams, filterSB, hqlParam);

		boolean tempBoolean = ((StudentExamineStatSumFilterPanel) getFilterBasePanel()).isStatGrade();
		if (isStatGrade != tempBoolean) {
			isStatGrade = tempBoolean;
			String firstName = isStatGrade ? "年级" : "年龄";
			rptParamInfos[SightRptIndex].getColumnNames()[0] = firstName;
			rptParamInfos[ToolRptIndex].getColumnNames()[0] = firstName;
			rptParamInfos[InternalMedicineRptIndex].getColumnNames()[0] = firstName;
			rptParamInfos[OtherRptIndex].getColumnNames()[0] = firstName;
		}
		String tableEndStr = isStatGrade ? "grade" : "age";
		String selectFieldStr = isStatGrade ? "(select Grade.ShortName from Grade where rpt.grade=Grade.GradeCode) grade, grade orderFiled"
				: "age, age orderFiled";
		String fieldStr = isStatGrade ? "grade" : "age";
		filterSB.append(" and ").append(fieldStr).append("<>'HJ' \n");

		StringBuffer sql = new StringBuffer(200);
		sql.append("select * from (");
		appendSightRptSQL(sql, selectFieldStr, fieldStr, tableEndStr, filterSB, false);
		//不需要SQL合计
		//		sql.append("\n union all");
		//		appendSightRptSQL(sql, fieldStr, tableEndStr, filterSB, true);
		sql.append("\n) tempResult");
		sql.append("\n order by orderFiled+0, XB desc");
		rptParamInfos[SightRptIndex].setHqlParam(hqlParam);
		rptParamInfos[SightRptIndex].setQuerySQL(sql.toString());

		sql.setLength(0);
		sql.append("select * from (");
		appendToolRptSQL(sql, selectFieldStr, fieldStr, tableEndStr, filterSB, false);
		//		sql.append("\n union all");
		//		appendToolRptSQL(sql, fieldStr, tableEndStr, filterSB, true);
		sql.append("\n) tempResult");
		sql.append("\n order by orderFiled+0, XB desc");
		rptParamInfos[ToolRptIndex].setHqlParam(hqlParam);
		rptParamInfos[ToolRptIndex].setQuerySQL(sql.toString());

		sql.setLength(0);
		sql.append("select * from (");
		appendInternalMedicineRptSQL(sql, selectFieldStr, fieldStr, tableEndStr, filterSB, false);
		//		sql.append("\n union all");
		//		appendInternalMedicineRptSQL(sql, fieldStr, tableEndStr, filterSB, true);
		sql.append("\n) tempResult");
		sql.append("\n order by orderFiled+0, XB desc");
		rptParamInfos[InternalMedicineRptIndex].setHqlParam(hqlParam);
		rptParamInfos[InternalMedicineRptIndex].setQuerySQL(sql.toString());

		sql.setLength(0);
		sql.append("select * from (");
		appendOtherRptSQL(sql, selectFieldStr, fieldStr, tableEndStr, filterSB, false);
		//		sql.append("\n union all");
		//		appendOtherRptSQL(sql, fieldStr, tableEndStr, filterSB, true);
		sql.append("\n) tempResult");
		sql.append("\n order by orderFiled+0, XB desc");
		rptParamInfos[OtherRptIndex].setHqlParam(hqlParam);
		rptParamInfos[OtherRptIndex].setQuerySQL(sql.toString());

		left1Lbl.setText(ReportBasePanel.EmptyString4Show + FilterCommonAction.getPrintMsg4SchoolCity(pair));
		left2Lbl.setText(ReportBasePanel.EmptyString4Show + "受检单位（学校）：" + FilterCommonAction.getPrintMsg4SchoolRange(schoolType, schoolNode));
		mid1Lbl.setText("学校类型：" + FilterCommonAction.getPrintMsg4SchoolType(schoolType, schoolNode));
	}

	private void appendSightRptSQL(StringBuffer sql, String selectFieldStr, String fieldStr, String tableEndStr, StringBuffer filterSB, boolean isSum) {
		sql.append("\n select ").append(isSum ? "'合计' " + fieldStr : selectFieldStr).append(", XB, sum(SJ) sj");
		appendCommonColumn(sql, 1, 6);
		sql.append("\n, sum(H7) H7, case when sum(H11)=0 then 0 else round(sum(h7)*100/sum(H11),").append(scale).append(") end cal_h7");
		appendCommonColumn(sql, 8, 10);
		sql.append("\n from Report_ZB_").append(tableEndStr).append(" rpt \n");
		sql.append("\n where rid='01' ");
		sql.append(filterSB);
		if (isSum) {
			sql.append("\n group by XB");
		} else {
			sql.append("\n group by ").append(fieldStr).append(", XB");
		}
	}

	private void appendToolRptSQL(StringBuffer sql, String selectFieldStr, String fieldStr, String tableEndStr, StringBuffer filterSB, boolean isSum) {
		sql.append(" select ").append(isSum ? "'合计' " + fieldStr : selectFieldStr).append(", XB, sum(SJ) sj");
		appendCommonColumn(sql, 1, 1);
		sql.append(", sum(H2) h2, case when sum(SJ)=0 then 0 else round(sum(h2)/sum(SJ),").append(scale).append(") end YHYB_CAL");
		appendCommonColumn(sql, 3, 3);
		sql.append(", sum(H4) h4, sum(H5) h5, sum(H6) h6");
		sql.append(", case when sum(H4)+sum(H5)+sum(H6)=0 then 0 else round(sum(H6)*100/(sum(H4)+sum(H5)+sum(H6)),").append(scale)
				.append(") end h6b");
		sql.append(", case when sum(h3)=0 then 0 else round((sum(H4)+sum(H5)+sum(H6))/sum(h3),").append(scale).append(") end HYB_CAL");
		appendCommonColumn(sql, 7, 7);
		sql.append(", sum(H8) h8, sum(H9) h9, sum(H10) h10");
		sql.append(", case when sum(H8)+sum(H9)+sum(H10)=0 then 0 else round(sum(H10)*100/(sum(H8)+sum(H9)+sum(H10)),").append(scale)
				.append(") end h10b");
		sql.append(", case when sum(h7)=0 then 0 else round((sum(H8)+sum(H9)+sum(H10))/sum(h7),").append(scale).append(") end YYB_CAL");
		appendCommonColumn(sql, 11, 11);

		sql.append(" from Report_ZB_").append(tableEndStr).append(" rpt \n");
		sql.append(" where rid='02' ");
		sql.append(filterSB);
		if (isSum) {
			sql.append("\n group by XB");
		} else {
			sql.append("\n group by ").append(fieldStr).append(", XB");
		}

	}

	private void appendInternalMedicineRptSQL(StringBuffer sql, String selectFieldStr, String fieldStr, String tableEndStr, StringBuffer filterSB,
			boolean isSum) {
		sql.append(" select ").append(isSum ? "'合计' " + fieldStr : selectFieldStr).append(", XB, sum(SJ) sj");
		appendCommonColumn(sql, 1, 10);
		sql.append(" from Report_ZB_").append(tableEndStr).append(" rpt \n");
		sql.append(" where rid='03' ");
		sql.append(filterSB);
		if (isSum) {
			sql.append("\n group by XB");
		} else {
			sql.append("\n group by ").append(fieldStr).append(", XB");
		}
	}

	private void appendOtherRptSQL(StringBuffer sql, String selectFieldStr, String fieldStr, String tableEndStr, StringBuffer filterSB, boolean isSum) {
		sql.append(" select ").append(isSum ? "'合计' " + fieldStr : selectFieldStr).append(", XB, sum(SJ) sj");
		appendCommonColumn(sql, 1, 10);
		sql.append(" from Report_ZB_").append(tableEndStr).append(" rpt \n");
		sql.append(" where rid='04' ");
		sql.append(filterSB);
		if (isSum) {
			sql.append("\n group by XB");
		} else {
			sql.append("\n group by ").append(fieldStr).append(", XB");
		}
	}

	private void appendCommonColumn(StringBuffer sql, int startIndex, int endIndex) {
		for (int i = startIndex; i <= endIndex; i++) {
			sql.append("\n, sum(H").append(i).append(") H").append(i).append(", case when sum(SJ)=0 then 0 else round(sum(h").append(i)
					.append(")*100/sum(SJ),").append(scale).append(") end cal_h").append(i);
		}
	}

	@Override
	public void loadReportData(List datas, int index) {
		RptDefaultTableModel tableModel = new RptDefaultTableModel((Object[][]) datas.toArray(new Object[][] {}),
				rptParamInfos[index].getColumnNames());
		int[] sumColumnIndexs = rptParamInfos[index].getSumColumnIndexs();
		tableModel.calculatGroupSum2(-1, 2, sumColumnIndexs);

		Map<Integer, Object> statRowIndexMap = tableModel.getStatRowIndexMap();
		Iterator<Integer> it = statRowIndexMap.keySet().iterator();
		Vector dataVector = tableModel.getDataVector();
		if (ToolRptIndex != index) {
			while (it.hasNext()) {
				Vector rowData = (Vector) dataVector.get(it.next());
				for (int i = 1; i < sumColumnIndexs.length; i++) {
					rowData.setElementAt(
							BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(sumColumnIndexs[i]), BigDecimalUtil.HUNDRED),
									rowData.get(sumColumnIndexs[0])), sumColumnIndexs[i] + 1);//当前单元格除以第一个合计数
				}
			}
		} else {//牙齿
			Object dmfSum;
			while (it.hasNext()) {
				Vector rowData = (Vector) dataVector.get(it.next());
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(4), BigDecimalUtil.HUNDRED), rowData.get(3)), 5);
				rowData.setElementAt(BigDecimalUtil.divide(rowData.get(6), rowData.get(3)), 7);
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(8), BigDecimalUtil.HUNDRED), rowData.get(3)), 9);
				dmfSum = BigDecimalUtil.add(BigDecimalUtil.add(rowData.get(10), rowData.get(11)), rowData.get(12));
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(12), BigDecimalUtil.HUNDRED), dmfSum), 13);
				rowData.setElementAt(BigDecimalUtil.divide(dmfSum, rowData.get(8)), 14);
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(15), BigDecimalUtil.HUNDRED), rowData.get(3)), 16);
				dmfSum = BigDecimalUtil.add(BigDecimalUtil.add(rowData.get(17), rowData.get(18)), rowData.get(19));
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(19), BigDecimalUtil.HUNDRED), dmfSum), 20);
				rowData.setElementAt(BigDecimalUtil.divide(dmfSum, rowData.get(15)), 21);
				rowData.setElementAt(BigDecimalUtil.divide(BigDecimalUtil.multiply(rowData.get(22), BigDecimalUtil.HUNDRED), rowData.get(3)), 23);
			}
		}
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(tableModel);
		tblMains[index].setModel(sortableTreeTableModel);
	}

	@Override
	protected void processTableExportParam(JTableExportParam param, int index) {
		int indexTemp = index + 1;
		String[] Temp = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
		param.setReportTitle("广东省中小学生健康检查统计表(" + Temp[indexTemp] + ")");
		param.setTabTitle("健康汇总表" + indexTemp);
		param.setTableStyle(JTableExportParam.TableStyle.Three_Line);
		String text = left1Lbl.getText().substring(ReportBasePanel.EmptyString4Show.length());
		param.addHeaderMessage(new MessageFormat(text), new MessageFormat(mid1Lbl.getText()), new MessageFormat(right1Lbl.getText()));
		text = left2Lbl.getText().substring(ReportBasePanel.EmptyString4Show.length());
		param.addHeaderMessage(new MessageFormat(text), new MessageFormat(mid2Lbl.getText()), new MessageFormat(right2Lbl.getText()));
	}

	@Override
	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {
		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		PrintTableUtil.addHeadSubTitle(pageFormat, 2);
		PageBorder headSubTitle = pageFormat.getHeadSubTitle(0);
		headSubTitle.setLeftContent(left1Lbl.getText());
		headSubTitle.setMidContent(mid1Lbl.getText());
		headSubTitle.setRightContent(right1Lbl.getText());
		headSubTitle = pageFormat.getHeadSubTitle(1);
		headSubTitle.setLeftContent(left2Lbl.getText());
		headSubTitle.setMidContent(mid2Lbl.getText());
		headSubTitle.setRightContent(right2Lbl.getText());
	}

	private void initComponents() {
		JPanel reportHead = new JPanel();
		reportHead.setLayout(new java.awt.GridLayout(2, 3, 10, 10));
		left1Lbl = new JLabel(ReportBasePanel.EmptyString4Show + "          市          县(市、区)");
		reportHead.add(left1Lbl);
		mid1Lbl = new JLabel("学校类型：小学( )初中( )高中( )");
		reportHead.add(mid1Lbl);
		right1Lbl = new JLabel("填报人：            ");
		reportHead.add(right1Lbl);
		left2Lbl = new JLabel(ReportBasePanel.EmptyString4Show + "受检单位（学校）：");
		reportHead.add(left2Lbl);
		mid2Lbl = new JLabel("体检单位：（盖章）");
		reportHead.add(mid2Lbl);
		right2Lbl = new JLabel("统计日期：" + DateUtil.getDateString(new Date(), "yyyy年MM月dd日"));
		reportHead.add(right2Lbl);
		tblPanel.add(reportHead, java.awt.BorderLayout.NORTH);
	}
}