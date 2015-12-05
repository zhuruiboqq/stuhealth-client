/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.report.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.util.StringUtils;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.SortableTreeTableModel;
import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.framework.Pair;
import com.vastcm.stuhealth.client.framework.report.DataBaseType;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.service.IJasperReportService;
import com.vastcm.stuhealth.client.framework.report.ui.PrintTableUtil;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.JTableExportParam;
import com.vastcm.stuhealth.client.utils.MyTableUtils;
import com.vastcm.stuhealth.client.utils.biz.AboutInfoUtils;
import com.vastcm.swing.print.table.ExtPageFormat;
import com.vastcm.swing.print.table.PageBorder;
import com.vastcm.swing.print.table.PrintTable;

/**
 * 个体评价报表
 * 
 * @author bob
 */
public class StudentAppraise2RptPanel extends ReportBasePanel {
	private IJasperReportService jasperReportService;
	private Map<String, String> field2SelectMap = new HashMap<String, String>();
	private Map<String, String> hasAdjustFieldMap = new HashMap<String, String>();

	protected javax.swing.JButton btnAppraiseReport;
	protected javax.swing.JButton btnChoolAllRecord;
	private JLabel left1Lbl;
	private JLabel mid1Lbl;
	private JLabel right1Lbl;

	public StudentAppraise2RptPanel() {
		initComponents();

		setFilterBasePanel(new StudentAppraiseFilterPanel());
		tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
		initData();
	}

	public void initData() {

		StringBuffer sql = new StringBuffer(200);
		field2SelectMap.put("SGDJQ", ", left(SGDJQ, CHAR_LENGTH(SGDJQ)-1) SGDJQ");//身高，评价
		field2SelectMap.put("TZDJQ", ", left(TZDJQ, CHAR_LENGTH(TZDJQ)-1) TZDJQ");//体重，评价
		field2SelectMap.put("FHLDJQ", ", left(FHLDJQ, CHAR_LENGTH(FHLDJQ)-1) FHLDJQ");//肺活量，评价
		field2SelectMap.put("MBPJ", ", left(MBPJ, CHAR_LENGTH(MBPJ)-1) MBPJ");//脉搏，评价
		field2SelectMap.put("SSYPJ", ", case SSYPJ when '无异常' then '-' when '正常' then '-' else SSYPJ end SSYPJ");//收缩压，评价
		field2SelectMap.put("SZYPJ", ", case SZYPJ when '无异常' then '-' when '正常' then '-' else SZYPJ end SZYPJ");//舒张压，评价
		field2SelectMap.put("LSLPJ", ", case when SLPJ is null then '' else (case SLPJ when '' then '' when '正常' then '-' else '●' end) end SLPJ");//视力，评价
		processItemResultSQLMap(sql, "LQG");//屈光不正左
		processItemResultSQLMap(sql, "RQG");//屈光不正右
		processItemResultSQLMap(sql, "RS");//弱视
		processItemResultSQLMap(sql, "JMY");//结膜炎
		processItemResultSQLMap(sql, "LSY");//左眼沙眼
		processItemResultSQLMap(sql, "RSY");//右眼沙眼
		processItemResultSQLMap(sql, "BS");//色觉
		processItemResultSQLMap(sql, "YB");//眼病
		processItemResultSQLMap(sql, "TL");//听力左
		processItemResultSQLMap(sql, "RTL");//听力右
		processItemResultSQLMap(sql, "EB");//耳病
		processItemResultSQLMap(sql, "BB");//鼻病
		processItemResultSQLMap(sql, "BTT");//扁桃体
		field2SelectMap.put("QSBNUM", ", case when qsbnum>0 then '●' else '-' end YCPJ");//龋齿
		processItemResultSQLMap(sql, "YZB");//牙周
		processItemResultSQLMap(sql, "WGFB");//窝沟封闭
		processItemResultSQLMap(sql, "YYB");//牙龈
		processItemResultSQLMap(sql, "XZ");//心
		processItemResultSQLMap(sql, "FEI");//肺
		processItemResultSQLMap(sql, "GP");//肝
		processItemResultSQLMap(sql, "PEI");//脾
		processItemResultSQLMap(sql, "TB");//头部
		processItemResultSQLMap(sql, "JB");//颈部
		processItemResultSQLMap(sql, "XK");//胸部
		processItemResultSQLMap(sql, "JZ");//脊柱
		processItemResultSQLMap(sql, "SZ");//四肢
		processItemResultSQLMap(sql, "PZ");//平足
		processItemResultSQLMap(sql, "PF");//皮肤
		processItemResultSQLMap(sql, "JZX");//甲状腺
		processItemResultSQLMap(sql, "LBJ");//淋巴结
		processItemResultSQLMap(sql, "XJ");//嗅觉
		processItemResultSQLMap(sql, "PFB");//皮肤病
		processItemResultSQLMap(sql, "WSZQ");//男性外生殖器
		processItemResultSQLMap(sql, "FHCL");//蠕虫卵
		processItemResultSQLMap(sql, "GG");//肝功能
		processItemResultSQLMap(sql, "JHJS");//结核菌素试验
		processItemResultSQLMap(sql, "XT");//胸透
		processItemResultSQLMap(sql, "YGHKT");//乙肝核心抗体
		processItemResultSQLMap(sql, "YGEKY");//乙肝e抗原
		processItemResultSQLMap(sql, "YGEKT");//乙肝e抗体
		processItemResultSQLNoTranMap(sql, "BLOOD");//血型
		processItemResultSQLMap(sql, "BMKY");//表面抗原
		processItemResultSQLMap(sql, "BMKT");//表面抗体
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		rptParamInfo = new RptParamInfo();
		rptParamInfo.setColumnNames(new String[] { "班级", "编号", "姓名", "性别", "年龄", "(cm)", "评价", "(kg)", "评价", "腰围", "臀围", "胸围(cm)", "(ml)", "评价",
				"脉搏", "评价", "(mmHg)", "评价", "(mmHg)", "评价", "左眼", "右眼", "评价", "左眼", "右眼", "弱视", "结膜炎", "左眼", "右眼", "色觉", "眼", "听力左", "听力右", "耳", "鼻",
				"扁桃体", "龋齿", "牙周", "窝沟封闭", "牙龈", "心", "肺", "肝", "脾", "头部", "颈部", "胸部", "脊柱", "四肢", "平足", "皮肤", "甲状腺", "淋巴结", "嗅觉", "皮肤病", "神经衰弱",
				"男性外生殖器", "血红蛋白", "蠕虫卵", "肝功能", "结核菌素试验", "营养评价", "BMI评价", "发育", "坐高", "肩宽", "骨盆宽", "握力", "胸透", "背肌力", "肱三头肌皮褶厚度", "肩胛下皮褶厚度", "红细胞",
				"乙肝核心抗体", "乙肝e抗原", "乙肝e抗体", "血型", "谷丙转氨酶\n参考值0-40 IU/L", "总胆红素\n参考值0-20 umol/L", "直胆红素\n参考值0-20 umol/L", "表面抗原", "表面抗体", "id隐藏" });
	}

	@Override
	protected void initTable(boolean isRebuild) {
		super.initTable(isRebuild);
		hasAdjustFieldMap.put("SG", "SGDJQ;(ml);评价");
		hasAdjustFieldMap.put("TZ", "TZDJQ;(kg);评价");
		hasAdjustFieldMap.put("FHL", "FHLDJQ;(ml);评价");
		hasAdjustFieldMap.put("MB", "MBPJ;脉搏;评价");
		hasAdjustFieldMap.put("SSY", "SSYPJ;(mmHg);评价");
		hasAdjustFieldMap.put("SZY", "SZYPJ;(mmHg);评价");
		hasAdjustFieldMap.put("LSL", "SLPJ;左眼;右眼;评价");//有3列
		//LSL 视力
		//LQG 屈光不正
		//LJSL 近视力(没有)
		//LSY 沙眼

		int colIndex = 5;
		MyTableUtils.addColumnGroup2Table(tblMain, "身高", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "体重", colIndex++, colIndex++);
		colIndex++;
		MyTableUtils.addColumnGroup2Table(tblMain, "肺活量", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "脉搏", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "收缩压", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "舒张压", colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "视力", colIndex++, colIndex++, colIndex++);
		//		MyTableUtils.addColumnGroup2Table(tblMain, "近视力", colIndex++, colIndex++, colIndex++);
		MyTableUtils.addColumnGroup2Table(tblMain, "屈光不正", colIndex++, colIndex++);
		colIndex++;
		colIndex++;
		MyTableUtils.addColumnGroup2Table(tblMain, "沙眼", colIndex++, colIndex++);

		int size = rptParamInfo.getColumnNames().length;
		//		MyTableUtils.hiddenColumn(tblMain, size - 2);
		MyTableUtils.hiddenColumn(tblMain, size - 1);
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);
		//		System.out.println(getWholeSQL());
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		String standardFlag = "Q";//默认使用全国标准，广东标准作为评价的版本
		//		standardFlag = (String) filterParams.get(StudentAppraiseFilterPanel.Param_Standard_Option);
		StringBuffer sql = new StringBuffer(4000);
		sql.append("select CLASS_NAME,BH,XM,XB,NL");//班级	编号	姓名	性别	年龄
		sql.append(",SG, left(SGDJ").append(standardFlag).append(",CHAR_LENGTH(SGDJ").append(standardFlag).append(")-1)");//身高
		sql.append(",TZ, left(TZDJ").append(standardFlag).append(",CHAR_LENGTH(TZDJ").append(standardFlag).append(")-1)");//体重
		sql.append(",YW,TW,XW");//腰围、臀围、胸围
		//		sql.append(",XW,XWDJ").append(standardFlag);
		sql.append(" ,FHL, left(FHLDJ").append(standardFlag).append(",CHAR_LENGTH(FHLDJ").append(standardFlag).append(")-1)");//肺活量，评价
		sql.append(" ,MB, left(MBPJ,CHAR_LENGTH(MBPJ)-1)");//脉搏，评价
		sql.append(" ,SSY, case SSYPJ when '无异常' then '-' when '正常' then '-' else SSYPJ end SSYPJ");//收缩压，评价
		sql.append(" ,SZY, case SZYPJ when '无异常' then '-' when '正常' then '-' else SZYPJ end SZYPJ");//舒张压，评价
		sql.append(",LSL,RSL,case when SLPJ is null then '' else (case SLPJ when '' then '' when '正常' then '-' else '●' end) end SLPJ");
		//		sql.append(",LJSL,RJSL,case when ljsl<5.0 or rjsl<5.0 then '视力低下' when ljsl>=5.0 and rjsl>=5.0 then '正常' else '' end JSLPJ");
		processItemResultSQL(sql, "LQG");//屈光不正左
		processItemResultSQL(sql, "RQG");//屈光不正右
		processItemResultSQL(sql, "RS");//弱视
		processItemResultSQL(sql, "JMY");//结膜炎
		processItemResultSQL(sql, "LSY");//左眼沙眼
		processItemResultSQL(sql, "RSY");//右眼沙眼
		processItemResultSQL(sql, "BS");//色觉
		processItemResultSQL(sql, "YB");//眼病
		processItemResultSQL(sql, "TL");//听力左
		processItemResultSQL(sql, "RTL");//听力右
		processItemResultSQL(sql, "EB");//耳病
		processItemResultSQL(sql, "BB");//鼻病
		processItemResultSQL(sql, "BTT");//扁桃体
		sql.append(",case when qsbnum>0 then '●' else '-' end YCPJ");//龋齿
		processItemResultSQL(sql, "YZB");//牙周
		processItemResultSQL(sql, "WGFB");//窝沟封闭
		processItemResultSQL(sql, "YYB");//牙龈
		processItemResultSQL(sql, "XZ");//心
		processItemResultSQL(sql, "FEI");//肺
		processItemResultSQL(sql, "GP");//肝
		processItemResultSQL(sql, "PEI");//脾
		processItemResultSQL(sql, "TB");//头部
		processItemResultSQL(sql, "JB");//颈部
		processItemResultSQL(sql, "XK");//胸部
		processItemResultSQL(sql, "JZ");//脊柱
		processItemResultSQL(sql, "SZ");//四肢
		processItemResultSQL(sql, "PZ");//平足
		processItemResultSQL(sql, "PF");//皮肤
		processItemResultSQL(sql, "JZX");//甲状腺
		processItemResultSQL(sql, "LBJ");//淋巴结
		processItemResultSQL(sql, "XJ");//嗅觉
		processItemResultSQL(sql, "PFB");//皮肤病
		sql.append(", SJSR");//神经衰弱
		processItemResultSQL(sql, "WSZQ");//男性外生殖器
		//		sql.append(", XHDB, PXPJ");//血红蛋白(g/L)、评价
		sql.append(", XHDB");//血红蛋白(g/L)
		processItemResultSQL(sql, "FHCL");//蠕虫卵
		processItemResultSQL(sql, "GG");//肝功能
		processItemResultSQL(sql, "JHJS");//结核菌素试验
		sql.append(", YYPJ, YYPJB, FYPJ");//营养评价、BIM评价、发育
		sql.append(",ZG,JK,GPK,WL");//坐高、肩宽、骨盆宽、握力
		processItemResultSQL(sql, "XT");//胸透
		sql.append(",BJL,GSTJ,JJX,HXP");//背肌力、肱三头肌皮褶厚度、肩胛下皮褶厚度、红细胞
		processItemResultSQL(sql, "YGHKT");//乙肝核心抗体
		processItemResultSQL(sql, "YGEKY");//乙肝e抗原
		processItemResultSQL(sql, "YGEKT");//乙肝e抗体
		processItemResultSQLNoTran(sql, "BLOOD");//血型
		sql.append(",GBZAM,DHSA,DHSZ");//谷丙转氨酶(参考值0-40 IU/L)
		processItemResultSQL(sql, "BMKY");//表面抗原
		processItemResultSQL(sql, "BMKT");//表面抗体
		sql.append(", ID \nfrom Result");//ID
		sql.append("\n where 1=1 ");
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		Pair<SchoolTreeNode, DefaultMutableTreeNode> pair = FilterCommonAction.processFilter4SchoolRange(filterParams, sql, hqlParam);
		SchoolTreeNode schoolNode = pair.getKey();
		FilterCommonAction.processFilter4SchoolTerm(filterParams, sql, hqlParam);
		FilterCommonAction.processFilter4QueryPair(filterParams, sql, hqlParam);
		sql.append("\n order by schoolBH,ClassBH,XM");
		rptParamInfo.setQuerySQL(sql.toString());

		left1Lbl.setText(ReportBasePanel.EmptyString4Show + "受检单位（学校）：" + FilterCommonAction.getPrintMsg4SchoolRange(null, schoolNode));
	}

	private StringBuffer processNotNullSQL(StringBuffer sql, String columnName) {
		//SQL空函数
		if (DataBaseType.Current == DataBaseType.Only_SqlServer)
			sql.append("\n, isnull(").append(columnName).append(",'') ").append(columnName);
		else
			sql.append("\n, ifnull(").append(columnName).append(",'') ").append(columnName);

		return sql;
	}

	private StringBuffer processItemResultSQLNoTranMap(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.setLength(0);
		sql.append("\n, ifnull((select ItemResult from ItemResult where FieldMc = '");
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",'')) ").append(columnName);
		field2SelectMap.put(columnName, sql.toString());
		return sql;
	}

	private StringBuffer processItemResultSQLMap(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.setLength(0);
		sql.append("\n, ifnull((select case ItemCode when 0 then '-' else '●' end ItemResult from ItemResult where FieldMc = '");//'未见异常'
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",'')) ").append(columnName);
		field2SelectMap.put(columnName, sql.toString());
		return sql;
	}

	private StringBuffer processItemResultSQLNoTran(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.append("\n, ifnull((select ItemResult from ItemResult where FieldMc = '");
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",'')) ").append(columnName);
		return sql;
	}

	private StringBuffer processItemResultSQL(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.append("\n, ifnull((select case ItemCode when 0 then '-' else '●' end ItemResult from ItemResult where FieldMc = '");//'未见异常'
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",'')) ").append(columnName);
		return sql;
	}

	@Override
	public void loadReportData(List datas) throws Exception {
		//		super.loadReportData(datas);
		Object[][] array = (Object[][]) datas.toArray(new Object[][] {});
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(new RptDefaultTableModel(array, rptParamInfo.getColumnNames()));
		tblMain.setModel(sortableTreeTableModel);
		initTable(true);

		int k;
		TableColumnModel localTableColumnModel = tblMain.getColumnModel();
		//		int j = localTableColumnModel.getColumnCount() - 1;
		TableCellRenderer localTableCellRenderer1 = tblMain.getTableHeader().getDefaultRenderer();
		boolean hasText = false;
		int rowLength = array.length;
		int colLength = array[0].length;
		for (int col = 0; col < colLength; col++) {
			hasText = false;
			for (int row = 0; row < rowLength; row++) {
				if (array[row][col] != null && StringUtils.hasText(array[row][col].toString())) {
					//有内容，不能隐藏
					hasText = true;
					break;
				}
			}
			if (!hasText) {
				//没有内容，隐藏
				MyTableUtils.hiddenColumn(tblMain, col);
			} else {
				//有内容，按表头长度设置列宽
				k = -1;
				TableColumn localTableColumn = localTableColumnModel.getColumn(col);
				TableCellRenderer localTableCellRenderer2 = localTableColumn.getHeaderRenderer();
				if (localTableCellRenderer2 == null)
					localTableCellRenderer2 = localTableCellRenderer1;
				Object localObject2;
				if ((localTableCellRenderer2 != null)) {
					localObject2 = localTableCellRenderer2.getTableCellRendererComponent(tblMain, localTableColumn.getHeaderValue(), false, false,
							-1, col);
					k = Math.max(k, ((Component) localObject2).getPreferredSize().width + tblMain.getIntercellSpacing().width)
							+ ((tblMain instanceof SortableTable) ? 5 : 0);
				}
				k = Math.max(k, localTableColumn.getMinWidth());

				//				localTableColumn.setPreferredWidth(k + 4);
				//				localTableColumn.setWidth(k + 4);
			}
		}
		btnChoolAllRecord_ActionPerformed(null);
	}

	@Override
	protected void processTableExportParam(JTableExportParam param) {
		param.setReportTitle("学生健康检查表");
		String text = left1Lbl.getText().substring(ReportBasePanel.EmptyString4Show.length());
		text += ReportBasePanel.EmptyString4Show + ReportBasePanel.EmptyString4Show + mid1Lbl.getText();
		text += ReportBasePanel.EmptyString4Show + ReportBasePanel.EmptyString4Show + right1Lbl.getText();
		param.addHeaderMessage(new MessageFormat(text), null, null);
		param.addFooterMessage(new MessageFormat("备注1：符号“-”代表该项目未见异常，符号“●”代表该项目检出异常。"), null, null);
		param.addFooterMessage(new MessageFormat("备注2：在龋齿项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。"), null, null);
		param.addFooterMessage(new MessageFormat("备注3：在视力项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。"), null, null);
	}

	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {
		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		PrintTableUtil.addHeadSubTitle(pageFormat, 1);
		PageBorder headSubTitle = pageFormat.getHeadSubTitle(0);
		headSubTitle.setLeftContent(left1Lbl.getText());
		headSubTitle.setMidContent(mid1Lbl.getText());
		headSubTitle.setRightContent(right1Lbl.getText());
		PrintTableUtil.addFootSubTitle(pageFormat, 1);
		PageBorder footSubTitle = pageFormat.getFootSubTitle(0);
		footSubTitle.setLeftContent(ReportBasePanel.EmptyString4Show + "备注：符号“-”代表该项目未见异常，符号“●”代表该项目检出异常。包括龋齿项目和视力项目。");
	}

	private void btnAppraiseReportActionPerformed(ActionEvent evt) throws Exception {
		viewJasperReport(evt, "StudentAppraise4One.jasper");
	}

	@Override
	protected void actionPrint_ActionPerformed(ActionEvent evt) throws Exception {
		StudentAppraiseFilterPanel filterPanel = (StudentAppraiseFilterPanel) getFilterBasePanel();
		if (filterPanel.isTableFormReportPrint()) {
			super.actionPrint_ActionPerformed(evt);
		} else {
			viewJasperReport(evt, "StudentAppraise4Multi.jasper");
		}
	}

	private void viewJasperReport(ActionEvent evt, String jasperFilePath) throws JRException, SQLException {
		if (jasperReportService == null) {
			jasperReportService = AppContext.getBean("jasperReportService", IJasperReportService.class);
		}
		int[] selectedRows = tblMain.getSelectedRows();
		if (selectedRows == null || selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this, "请先选中要查看的行！！！");
			return;
		}
		Set<String> idSet = new HashSet<String>();
		int idIndex = rptParamInfo.getColumnNames().length - 1;
		for (int i = 0; i < selectedRows.length; i++) {
			idSet.add(String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], idIndex)));
			logger.info("select id:" + String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], idIndex)));
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p_ids", idSet);
		params.put("p_orgName", new AboutInfoUtils().getOrgName());
		JasperPrint print = jasperReportService.getJasperPrint(jasperFilePath, params);
		JasperViewer viewer = new JasperViewer(print, false);
		viewer.setVisible(true);
	}

	protected void btnChoolAllRecord_ActionPerformed(ActionEvent evt) throws Exception {
		int totalRow = tblMain.getModel().getRowCount();
		if (totalRow != 0) {
			tblMain.setRowSelectionInterval(0, totalRow - 1);
		}
	}

	private void initComponents() {
		JPanel reportHead = new JPanel();
		reportHead.setLayout(new java.awt.GridLayout(1, 3, 10, 10));
		left1Lbl = new JLabel(ReportBasePanel.EmptyString4Show + "受检单位（学校）：");
		reportHead.add(left1Lbl);
		mid1Lbl = new JLabel("体检单位：（盖章）");
		reportHead.add(mid1Lbl);
		right1Lbl = new JLabel("统计日期：" + DateUtil.getDateString(new Date(), "yyyy年MM月dd日"));
		reportHead.add(right1Lbl);
		tblPanel.add(reportHead, java.awt.BorderLayout.NORTH);

		btnAppraiseReport = new javax.swing.JButton();
		btnAppraiseReport.setText("报告书");
		btnAppraiseReport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnAppraiseReportActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraise2RptPanel.this, logger, e);
				}
			}
		});
		toolbarPanel.add(btnAppraiseReport, 3);

		btnChoolAllRecord = new javax.swing.JButton();
		btnChoolAllRecord.setText("全选");
		btnChoolAllRecord.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnChoolAllRecord_ActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraise2RptPanel.this, logger, e);
				}
			}
		});
		toolbarPanel.add(btnChoolAllRecord, 4);
	}
}