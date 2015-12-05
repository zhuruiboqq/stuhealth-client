/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.report.ui;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jidesoft.grid.ExpressionCalculatedColumn;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SingleColumn;
import com.jidesoft.grid.SortableTreeTableModel;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.service.IJasperReportService;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.utils.MyTableUtils;

/**
 * 常见疾病查询
 * @author bob
 */
public class CommonDiseaseRptPanel extends ReportBasePanel {

	private IJasperReportService jasperReportService;

	public CommonDiseaseRptPanel() {
		initComponents();

		setFilterBasePanel(new StudentAppraiseFilterPanel());
		tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		rptParamInfo = new RptParamInfo();
		rptParamInfo.setColumnNames(new String[] { "id隐藏", "班级", "编号", "姓名", "性别", "年龄", "身高(cm)", "评价", "体重(kg)", "评价", "胸围(cm)", "评价", "肺活量(ml)",
				"评价", "脉搏", "评价", "收缩压(mmHg)", "评价", "舒张压(mmHg)", "评价", "左眼", "右眼", "评价", "左眼", "右眼", "评价", "左眼", "右眼", "弱视", "左眼", "右眼", "色觉",
				"听力左", "听力右", "牙齿", "牙周", "胸部", "脊柱", "四肢", "心", "肝", "脾", "肺", "蛔虫卵", "神经衰弱", "血红蛋白(g/L)", "评价", "眼病", "头部", "颈部", "皮肤", "淋巴结",
				"结核菌素试验", "谷丙转氨酶(参考\n值0-40 IU/L)", "总胆红素(参考\n值0-20 umol/L)", "直胆红素(参考\n值0-20 umol/L)", "肝功能", "表面抗原", "表面抗体", "结膜炎", "窝沟封闭", "牙龈",
				"男性外生殖器", "营养评价", "发育" });
		int[] columnWidths = new int[rptParamInfo.getColumnNames().length];
		for (int i = 0; i < columnWidths.length; i++)
			columnWidths[i] = 50;

		columnWidths[0] = 0;
		columnWidths[1] = 100;
		columnWidths[2] = 80;
		//		columnWidths[1] = 100;

		rptParamInfo.setColumnWidths(columnWidths);
	}

	@Override
	protected void initTable(boolean isRebuild) {
		super.initTable(isRebuild);
		//		int colIndex = 6;
		//		MyTableUtils.addColumnGroup2Table(tblMain, "身高", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "体重", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "胸围", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "肺活量", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "脉搏", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "收缩压", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "舒张压", colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "视力", colIndex++, colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "近视力", colIndex++, colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "屈光不正", colIndex++, colIndex++);
		//		colIndex++;
		//		MyTableUtils.addColumnGroup2Table(tblMain, "沙眼", colIndex++, colIndex++);
		//		colIndex = 45;
		//		MyTableUtils.addColumnGroup2Table(tblMain, "血红蛋白", colIndex++, colIndex++);
		//
		//		MyTableUtils.hiddenColumn(tblMain, 0);
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		String standardFlag = "D";
		standardFlag = (String) filterParams.get(StudentAppraiseFilterPanel.Param_Standard_Option);
		StringBuffer sql = new StringBuffer(200);
		sql.append("select CHECKID,CLASS_NAME,BH,XM,XB,NL,SG,SGDJ").append(standardFlag).append(",TZ,TZDJ").append(standardFlag);
		sql.append(",XW,XWDJ").append(standardFlag).append(",FHL,FHLDJ").append(standardFlag);
		sql.append(",MB,MBBJ,SSY,SSYBJ,SZY,SZYBJ,LSL,RSL,SLPJ,LJSL,RJSL,case when ljsl<5.0 or rjsl<5.0 then '视力低下' when ljsl>=5.0 and rjsl>=5.0 then '正常' else '' end JSLPJ,LQG,RQG,RS,LSY,RSY,BS,TL,RTL,case when qsbnum>0 then '龋齿' else '无龋齿' end YCPJ,YZB,XK,JZ,SZ,XZ,GP,PEI,FEI,FHCL,SJSR,XHDB,PXPJ,YB,TB,JB,PF,LBJ,JHJS,GBZAM,DHSA,DHSZ,GG,BMKY,BMKT,JMY,WGFB,YYB,WSZQ,YYPJ,FYPJ from Result");
		sql.append(" where 1=1 ");
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		FilterCommonAction.processFilter4SchoolRange(filterParams, sql, hqlParam);
		FilterCommonAction.processFilter4SchoolTerm(filterParams, sql, hqlParam);
		FilterCommonAction.processFilter4QueryPair(filterParams, sql, hqlParam);
		rptParamInfo.setQuerySQL(sql.toString());
	}

	@Override
	public void loadReportData(List datas) {
		RptDefaultTableModel tableModel = new RptDefaultTableModel((Object[][]) datas.toArray(new Object[][] {}), rptParamInfo.getColumnNames());
		com.jidesoft.grid.CalculatedTableModel calculatedTableModel = new com.jidesoft.grid.CalculatedTableModel(tableModel);
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			SingleColumn localSingleColumn = new SingleColumn(tableModel, i);
			//			localSingleColumn.setIdentifier(i);
			calculatedTableModel.addColumn(localSingleColumn);
			if (i == 6) {
				//				ExpressionCalculatedColumn column = new ExpressionCalculatedColumn(tableModel, "测试" + i, "[" + (i - 2) + "]/[" + (i - 1) + "]");
				ExpressionCalculatedColumn column = new ExpressionCalculatedColumn(tableModel, "测试" + i, "[年龄]/[身高(cm)]");
				column.setDependingColumns(new int[] { 5, 6 });
				calculatedTableModel.addColumn(column);
			}else if(i==14){
				ExpressionCalculatedColumn column = new ExpressionCalculatedColumn(tableModel, "测试" + i, "[年龄]/[脉搏]");
				column.setDependingColumns(new int[] { 5, 14 });
				calculatedTableModel.addColumn(column);
			}
		}
		//		calculatedTableModel.addAllColumns();
		System.out.println("getColumnCount:" + calculatedTableModel.getColumnCount());
		System.out.println("getRowCount:" + calculatedTableModel.getRowCount());
		//		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(calculatedTableModel);
		tblMain.setModel(calculatedTableModel);
		initTable(true);
	}

	private void initComponents() {
	}
}