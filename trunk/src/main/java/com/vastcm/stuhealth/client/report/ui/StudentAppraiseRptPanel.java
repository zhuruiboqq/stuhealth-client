/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.report.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.SortableTreeTableModel;
import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.framework.NameValuePair;
import com.vastcm.stuhealth.client.framework.Pair;
import com.vastcm.stuhealth.client.framework.report.DataBaseType;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.service.IJasperReportService;
import com.vastcm.stuhealth.client.framework.report.ui.PrintTableUtil;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.report.ui.RptDefaultTableModel;
import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.JTableExportParam;
import com.vastcm.stuhealth.client.utils.JasperReportUtil;
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
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StudentAppraiseRptPanel extends ReportBasePanel {
	private static final long serialVersionUID = 1L;
	private IJasperReportService jasperReportService;
	private Map<String, RptColumn> columnMap;
	private boolean isChangeItemOrder = false;
	private List<String> columnOrder;
	private String[] allColumnOrder;
	private Set<String> columnMainGroupSet;
	private Set<String> columnSubGroupSet;
	private int Start_Column_Index = 6;
	private int ID_Column_Index = 0;

	protected javax.swing.JButton btnAppraiseReport;
	protected javax.swing.JButton btnChoolAllRecord;
	protected javax.swing.JButton btnChangeItemOrder;
	private JLabel left1Lbl;
	private JLabel mid1Lbl;
	private JLabel right1Lbl;

	public StudentAppraiseRptPanel() {
		initComponents();

		setFilterBasePanel(new StudentAppraiseFilterPanel());
		tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
		tblMain.getTableHeader().setReorderingAllowed(false);
	}

	private void addColumn2Map(RptColumn rptColumn) {
		columnMap.put(rptColumn.getColumnName(), rptColumn);
		columnOrder.add(rptColumn.getColumnName());
		if (rptColumn.getColumnGroups() != null) {
			columnMainGroupSet.add(rptColumn.getColumnName());
			String[] columnGroups = rptColumn.getColumnGroups();
			for (int i = 0; i < columnGroups.length; i++) {
				columnSubGroupSet.add(columnGroups[i]);
			}
		}
	}

	private void initData() {
		StringBuffer sql = new StringBuffer();
		columnMap = new HashMap<String, RptColumn>();
		columnOrder = new ArrayList<String>();
		columnMainGroupSet = new LinkedHashSet<String>();
		columnSubGroupSet = new LinkedHashSet<String>();
		RptColumn rptColumn;

		rptColumn = new RptColumn("id隐藏", "ID", "ID");
		rptColumn.setHidden(true);
		addColumn2Map(rptColumn);
		addColumn2Map(new RptColumn("班级", "CLASS_NAME", "replace(CONCAT(SUBSTR(CLASS_NAME,1,1),SUBSTR(CLASS_NAME,3,1),SUBSTR(CLASS_NAME,6)),'班','')"));
		addColumn2Map(new RptColumn("编号", "BH", "BH"));
		addColumn2Map(new RptColumn("姓名", "XM", "XM"));
		addColumn2Map(new RptColumn("性别", "XB", "XB"));
		addColumn2Map(new RptColumn("年龄", "NL", "NL"));
		rptColumn = new RptColumn("(cm)", "SG", "SG", "身高", new String[] { "SGDJQ" });
		rptColumn.setPrintDisplayName("身高(cm)");
		addColumn2Map(rptColumn);//第6个，index为5，身高
		addColumn2Map(new RptColumn("评价", "SGDJQ", "left(SGDJQ,CHAR_LENGTH(SGDJQ)-1)"));
		rptColumn = new RptColumn("(kg)", "TZ", "TZ", "体重", new String[] { "TZDJQ" });
		rptColumn.setPrintDisplayName("体重(kg)");
		addColumn2Map(rptColumn);//体重
		addColumn2Map(new RptColumn("评价", "TZDJQ", "left(TZDJQ,CHAR_LENGTH(TZDJQ)-1)"));
		addColumn2Map(new RptColumn("腰围", "YW", "YW"));
		addColumn2Map(new RptColumn("臀围", "TW", "TW"));
		addColumn2Map(new RptColumn("胸围(cm)", "XW", "XW"));
		rptColumn = new RptColumn("(ml)", "FHL", "FHL", "肺活量", new String[] { "FHLDJQ" });
		rptColumn.setPrintDisplayName("肺活量(ml)");
		addColumn2Map(rptColumn);//肺活量
		addColumn2Map(new RptColumn("评价", "FHLDJQ", "left(FHLDJQ,CHAR_LENGTH(FHLDJQ)-1) "));
		addColumn2Map(new RptColumn("脉搏", "MB", "MB", "脉搏", new String[] { "MBPJ" }));//脉搏
		addColumn2Map(new RptColumn("评价", "MBPJ", "left(MBPJ,CHAR_LENGTH(MBPJ)-1) "));
		rptColumn = new RptColumn("(mmHg)", "SSY", "SSY", "收缩压", new String[] { "SSYPJ" });
		rptColumn.setPrintDisplayName("收缩压(mmHg)");
		addColumn2Map(rptColumn);//收缩压
		addColumn2Map(new RptColumn("评价", "SSYPJ", "case SSYPJ when '无异常' then '-' when '正常' then '-' else SSYPJ end "));
		rptColumn = new RptColumn("(mmHg)", "SZY", "SZY", "舒张压", new String[] { "SZYPJ" });
		rptColumn.setPrintDisplayName("舒张压(mmHg)");
		addColumn2Map(rptColumn);//舒张压
		addColumn2Map(new RptColumn("评价", "SZYPJ", "case SZYPJ when '无异常' then '-' when '正常' then '-' else SZYPJ end"));
		rptColumn = new RptColumn("左眼", "LSL", "LSL", "视力", new String[] { "RSL", "SLPJ" });
		rptColumn.setPrintDisplayName("视力左");
		addColumn2Map(rptColumn);//视力
		rptColumn = new RptColumn("右眼", "RSL", "RSL");
		rptColumn.setPrintDisplayName("视力右");
		addColumn2Map(rptColumn);
		addColumn2Map(new RptColumn("评价", "SLPJ",
				"case when SLPJ is null then '' else (case SLPJ when '' then '' when '正常' then '-' else '●' end) end"));
		rptColumn = new RptColumn("左眼", "LQG", processItemResultSQL4Clear(sql, "LQG"), "屈光不正", new String[] { "RQG" });
		rptColumn.setPrintDisplayName("屈光不正左眼");
		addColumn2Map(rptColumn);//屈光不正
		rptColumn = new RptColumn("右眼", "RQG", processItemResultSQL4Clear(sql, "RQG"));
		rptColumn.setPrintDisplayName("屈光不正右眼");
		addColumn2Map(rptColumn);
		addColumn2Map(new RptColumn("弱视", "RS", processItemResultSQL4Clear(sql, "RS")));
		addColumn2Map(new RptColumn("结膜炎", "JMY", processItemResultSQL4Clear(sql, "JMY")));
		rptColumn = new RptColumn("左眼", "LSY", processItemResultSQL4Clear(sql, "LSY"), "沙眼", new String[] { "RSY" });
		rptColumn.setPrintDisplayName("沙眼左");
		addColumn2Map(rptColumn);//沙眼
		rptColumn = new RptColumn("右眼", "RSY", processItemResultSQL4Clear(sql, "RSY"));
		rptColumn.setPrintDisplayName("沙眼右");
		addColumn2Map(rptColumn);
		addColumn2Map(new RptColumn("色觉", "BS", processItemResultSQL4Clear(sql, "BS")));
		addColumn2Map(new RptColumn("眼", "YB", processItemResultSQL4Clear(sql, "YB")));
		addColumn2Map(new RptColumn("听力左", "TL", processItemResultSQL4Clear(sql, "TL"), "听力", new String[] { "RTL" }));
		addColumn2Map(new RptColumn("听力右", "RTL", processItemResultSQL4Clear(sql, "RTL")));
		addColumn2Map(new RptColumn("耳", "EB", processItemResultSQL4Clear(sql, "EB")));
		addColumn2Map(new RptColumn("鼻", "BB", processItemResultSQL4Clear(sql, "BB")));
		addColumn2Map(new RptColumn("扁桃体", "BTT", processItemResultSQL4Clear(sql, "BTT")));
		addColumn2Map(new RptColumn("龋齿", "QSBNUM", "case when qsbnum>0 then '●' else '-' end"));
		addColumn2Map(new RptColumn("牙周", "YZB", processItemResultSQL4Clear(sql, "YZB")));
		addColumn2Map(new RptColumn("窝沟封闭", "WGFB", processItemResultSQL4Clear(sql, "WGFB")));
		addColumn2Map(new RptColumn("牙龈", "YYB", processItemResultSQL4Clear(sql, "YYB")));
		addColumn2Map(new RptColumn("心", "XZ", processItemResultSQL4Clear(sql, "XZ")));
		addColumn2Map(new RptColumn("肺", "FEI", processItemResultSQL4Clear(sql, "FEI")));
		addColumn2Map(new RptColumn("肝", "GP", processItemResultSQL4Clear(sql, "GP")));
		addColumn2Map(new RptColumn("脾", "PEI", processItemResultSQL4Clear(sql, "PEI")));
		addColumn2Map(new RptColumn("头部", "TB", processItemResultSQL4Clear(sql, "TB")));
		addColumn2Map(new RptColumn("颈部", "JB", processItemResultSQL4Clear(sql, "JB")));
		addColumn2Map(new RptColumn("胸部", "XK", processItemResultSQL4Clear(sql, "XK")));
		addColumn2Map(new RptColumn("脊柱", "JZ", processItemResultSQL4Clear(sql, "JZ")));
		addColumn2Map(new RptColumn("四肢", "SZ", processItemResultSQL4Clear(sql, "SZ")));
		addColumn2Map(new RptColumn("平足", "PZ", processItemResultSQL4Clear(sql, "PZ")));
		addColumn2Map(new RptColumn("皮肤", "PF", processItemResultSQL4Clear(sql, "PF")));
		addColumn2Map(new RptColumn("甲状腺", "JZX", processItemResultSQL4Clear(sql, "JZX")));
		addColumn2Map(new RptColumn("淋巴结", "LBJ", processItemResultSQL4Clear(sql, "LBJ")));
		addColumn2Map(new RptColumn("嗅觉", "XJ", processItemResultSQL4Clear(sql, "XJ")));
		addColumn2Map(new RptColumn("皮肤病", "PFB", processItemResultSQL4Clear(sql, "PFB")));
		addColumn2Map(new RptColumn("神经衰弱", "SJSR", "SJSR"));
		addColumn2Map(new RptColumn("男性外生殖器", "WSZQ", processItemResultSQL4Clear(sql, "WSZQ")));
		addColumn2Map(new RptColumn("血红蛋白", "XHDB", "XHDB"));
		addColumn2Map(new RptColumn("蠕虫卵", "FHCL", processItemResultSQL4Clear(sql, "FHCL")));
		addColumn2Map(new RptColumn("肝功能", "GG", processItemResultSQL4Clear(sql, "GG")));
		addColumn2Map(new RptColumn("结核菌素试验", "JHJS", processItemResultSQL4Clear(sql, "JHJS")));
		addColumn2Map(new RptColumn("营养评价", "YYPJ", "YYPJ"));
		addColumn2Map(new RptColumn("BMI评价", "YYPJB", "YYPJB"));
		addColumn2Map(new RptColumn("发育", "FYPJ", "FYPJ"));
		addColumn2Map(new RptColumn("坐高", "ZG", "ZG"));
		addColumn2Map(new RptColumn("肩宽", "JK", "JK"));
		addColumn2Map(new RptColumn("骨盆宽", "GPK", "GPK"));
		addColumn2Map(new RptColumn("握力", "WL", "WL"));
		addColumn2Map(new RptColumn("胸透", "XT", processItemResultSQL4Clear(sql, "XT")));
		addColumn2Map(new RptColumn("背肌力", "BJL", "BJL"));
		addColumn2Map(new RptColumn("肱三头肌皮褶厚度", "GSTJ", "GSTJ"));
		addColumn2Map(new RptColumn("肩胛下皮褶厚度", "JJX", "JJX"));
		addColumn2Map(new RptColumn("红细胞", "HXP", "HXP"));
		addColumn2Map(new RptColumn("乙肝核心抗体", "YGHKT", processItemResultSQL4Clear(sql, "YGHKT")));
		addColumn2Map(new RptColumn("乙肝e抗原", "YGEKY", processItemResultSQL4Clear(sql, "YGEKY")));
		addColumn2Map(new RptColumn("乙肝e抗体", "YGEKT", processItemResultSQL4Clear(sql, "YGEKT")));
		addColumn2Map(new RptColumn("血型", "BLOOD", processItemResultSQLNoTran4Clear(sql, "BLOOD")));
		addColumn2Map(new RptColumn("白细胞", "BXP", "BXP"));
		addColumn2Map(new RptColumn("血小板", "XXB", "XXB"));
		addColumn2Map(new RptColumn("谷丙转氨酶\n参考值0-40IU/L", "GBZAM", "GBZAM"));
		addColumn2Map(new RptColumn("总胆红素\n参考值0-20umol/L", "DHSA", "DHSA"));
		addColumn2Map(new RptColumn("直胆红素\n参考值0-20umol/L", "DHSZ", "DHSZ"));
		addColumn2Map(new RptColumn("表面抗原", "BMKY", processItemResultSQL4Clear(sql, "BMKY")));
		addColumn2Map(new RptColumn("表面抗体", "BMKT", processItemResultSQL4Clear(sql, "BMKT")));

		ICheckItemService checkItemService = AppContext.getBean("checkItemService", ICheckItemService.class);
		List<CheckItem> checkItemList = checkItemService.getList("where status='T' and isCustom=1");
		//		List<CheckItem> checkItemList = checkItemService.getList("where status='T' AND isSelected=1 and isCustom=1");
		for (int i = 0; i < checkItemList.size(); i++) {
			CheckItem checkItem = checkItemList.get(i);
			if (checkItem.isIsCustom()) {//自定义项目
				addColumn2Map(new RptColumn(checkItem.getItemName(), checkItem.getFieldname(), checkItem.getFieldname()));
			}
		}
		addColumn2Map(new RptColumn("         医务评语         ", "XMPY", "XMPY"));

		Iterator<String> iterator = columnSubGroupSet.iterator();
		while (iterator.hasNext()) {
			columnOrder.remove(iterator.next());
		}
		try {
			if (StringUtils.hasLength(AppCache.getInstance().getStudentAppraiseRpt_ItemOrder())) {
				List<String> tempColumnOrder = new ArrayList<String>();
				String[] strings = AppCache.getInstance().getStudentAppraiseRpt_ItemOrder().split(",");
				if (columnOrder.size() != strings.length) {
					saveItemOrder();
				} else {
					boolean isError = false;
					for (int i = 0; i < strings.length; i++) {
						if (!columnMap.containsKey(strings[i])) {
							isError = true;
							break;
						}
						tempColumnOrder.add(strings[i]);
					}
					if (isError) {
						saveItemOrder();
					} else {
						columnOrder = tempColumnOrder;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		initData();

		rptParamInfo = new RptParamInfo();
		recreateColumn();
	}

	private void recreateColumn() {
		String[] allItem = new String[columnMap.size()];
		allColumnOrder = new String[columnMap.size()];
		int i = 0;
		Iterator<String> iterator = columnOrder.iterator();
		while (iterator.hasNext()) {
			String columnName = iterator.next();
			RptColumn rptColumn = columnMap.get(columnName);
			allItem[i] = rptColumn.getDisplayName();
			allColumnOrder[i] = rptColumn.getColumnName();
			i++;
			if (rptColumn.getColumnGroups() != null) {
				String[] columnGroups = rptColumn.getColumnGroups();
				for (int j = 0; j < columnGroups.length; j++) {
					RptColumn subRptColumn = columnMap.get(columnGroups[j]);
					allItem[i] = subRptColumn.getDisplayName();
					allColumnOrder[i] = subRptColumn.getColumnName();
					i++;
				}
			}
		}
		rptParamInfo.setColumnNames(allItem);
	}

	@Override
	protected void initTable(boolean isRebuild) {
		super.initTable(isRebuild);
		//处理合并单元格
		for (int i = 0; i < allColumnOrder.length; i++) {
			RptColumn rptColumn = columnMap.get(allColumnOrder[i]);
			if (rptColumn.isHidden()) {
				MyTableUtils.hiddenColumn(tblMain, i);
			}
			if (rptColumn.getColumnGroups() != null) {
				if (rptColumn.getColumnGroups().length == 1) {
					MyTableUtils.addColumnGroup2Table(tblMain, rptColumn.getColumnGroupName(), i++, i);
				} else {
					MyTableUtils.addColumnGroup2Table(tblMain, rptColumn.getColumnGroupName(), i++, i++, i);
				}
			}
		}
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		if (!isChangeItemOrder) {
			super.beforeQuery(evt);
		}
		//		System.out.println(getWholeSQL());
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		NameValuePair queryPair = (NameValuePair) filterParams.get(CustomCommonFilterPanel.Param_Query_Pair);
		boolean isNotNormalQuery = queryPair != null && StudentAppraiseFilterPanel.Param_Query_Field_Not_Normal.equals(queryPair.getName());

		StringBuffer notNormalWhereSql = new StringBuffer();
		StringBuffer sql = new StringBuffer(4000);
		sql.append("select ");
		RptColumn rptColumn = columnMap.get(allColumnOrder[0]);
		sql.append(rptColumn.getSelectSql()).append(" ").append(rptColumn.getColumnName());
		notNormalWhereSql.append("\n where 1=2 ");
		for (int i = 1; i < allColumnOrder.length; i++) {
			rptColumn = columnMap.get(allColumnOrder[i]);
			sql.append("\n, ").append(rptColumn.getSelectSql()).append(" ").append(rptColumn.getColumnName());
			notNormalWhereSql.append("\n or ").append(rptColumn.getColumnName()).append("='●'");
		}
		notNormalWhereSql.append("\n or SSYPJ<>'-' or SZYPJ<>'-' ");//特殊的列字段异常
		if (isNotNormalQuery) {
			sql.append(",schoolBH,ClassBH");
		}
		sql.append("\nfrom Result");//ID
		sql.append("\n where 1=1 ");
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		Pair<SchoolTreeNode, DefaultMutableTreeNode> pair = FilterCommonAction.processFilter4SchoolRange(filterParams, sql, hqlParam);
		if ((pair == null || pair.getKey() == null) && isChangeItemOrder) {//更改顺序的查询
			sql.append(" and 1=2 ");
			rptParamInfo.setQuerySQL(sql.toString());
			return;
		}
		SchoolTreeNode schoolNode = pair.getKey();
		FilterCommonAction.processFilter4SchoolTerm(filterParams, sql, hqlParam);

		if (isNotNormalQuery) {
			sql.insert(0, "select * from (\n");
			sql.append(") tempTable");
			sql.append(notNormalWhereSql);
		} else {
			FilterCommonAction.processFilter4QueryPair(filterParams, sql, hqlParam);
		}
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

	private String processItemResultSQLNoTran4Clear(StringBuffer sql, String columnName) {
		sql.setLength(0);
		return processItemResultSQLNoTran(sql, columnName).toString();
	}

	private StringBuffer processItemResultSQLNoTran(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.append(" ifnull((select ItemResult from ItemResult where FieldMc = '");
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",'')) ");
		return sql;
	}

	private String processItemResultSQL4Clear(StringBuffer sql, String columnName) {
		sql.setLength(0);
		return processItemResultSQL(sql, columnName).toString();
	}

	private StringBuffer processItemResultSQL(StringBuffer sql, String columnName) {
		//ifnull((select ItemResult from ItemResult where FieldMc = '@columnName' and ItemCode = @columnName), ifnull(@columnName,'')) @columnName
		sql.append(" ifnull((select case ItemCode when 0 then '-' else '●' end ItemResult from ItemResult where FieldMc = '");//'未见异常'
		sql.append(columnName).append("' and ItemCode = ").append(columnName);
		sql.append("), ifnull(").append(columnName).append(",''))");
		return sql;
	}

	@Override
	public void loadReportData(List datas) throws Exception {
		//		super.loadReportData(datas);
		Object[][] array = (Object[][]) datas.toArray(new Object[][] {});
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(new RptDefaultTableModel(array, rptParamInfo.getColumnNames()));
		tblMain.setModel(sortableTreeTableModel);
		initTable(true);

		if (array.length == 0)
			return;

		int k;
		TableColumnModel localTableColumnModel = tblMain.getColumnModel();
		//		int j = localTableColumnModel.getColumnCount() - 1;
		TableCellRenderer tableCellRendererDef = tblMain.getTableHeader().getDefaultRenderer();
		boolean hasText = false;
		int rowLength = array.length;
		int colLength = localTableColumnModel.getColumnCount();
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
				TableColumn tableColumn = localTableColumnModel.getColumn(col);
				TableCellRenderer tableCellRenderer = tableColumn.getHeaderRenderer();
				if (tableCellRenderer == null)
					tableCellRenderer = tableCellRendererDef;
				Component cellComponent;
				if ((tableCellRenderer != null)) {
					cellComponent = tableCellRenderer.getTableCellRendererComponent(tblMain, tableColumn.getHeaderValue(), false, false, -1, col);
					k = Math.max(k, cellComponent.getPreferredSize().width + tblMain.getIntercellSpacing().width)
							+ ((tblMain instanceof SortableTable) ? 5 : 0);
				}
				k = Math.max(k, tableColumn.getMinWidth());

				//				localTableColumn.setPreferredWidth(k + 4);
				//				localTableColumn.setWidth(k + 4);
			}
		}
		btnChoolAllRecord_ActionPerformed(null);
	}

	@Override
	protected void processTableExportParam(JTableExportParam param) {
		param.setReportTitle("学生健康检查表");
		param.setTableStyle(JTableExportParam.TableStyle.Tow_Line);
		String text = left1Lbl.getText().substring(ReportBasePanel.EmptyString4Show.length());
		text += ReportBasePanel.EmptyString4Show + ReportBasePanel.EmptyString4Show + mid1Lbl.getText();
		text += ReportBasePanel.EmptyString4Show + ReportBasePanel.EmptyString4Show + right1Lbl.getText();
		param.addHeaderMessage(new MessageFormat(text), null, null);
		param.addFooterMessage(new MessageFormat("备注1：符号“-”代表该项目未见异常，符号“●”代表该项目检出异常。"), null, null);
		param.addFooterMessage(new MessageFormat("备注2：在龋齿项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。"), null, null);
		param.addFooterMessage(new MessageFormat("备注3：在视力项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。"), null, null);
	}

	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {
		Font font = new Font("宋体", Font.PLAIN, 8);

		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		PrintTableUtil.addHeadSubTitle(pageFormat, 1, font);
		PageBorder headSubTitle = pageFormat.getHeadSubTitle(0);
		headSubTitle.setFont(font);
		headSubTitle.setLeftContent(left1Lbl.getText());
		headSubTitle.setMidContent(mid1Lbl.getText());
		headSubTitle.setRightContent(right1Lbl.getText());
		PrintTableUtil.addFootSubTitle(pageFormat, 2, font);
		PageBorder footSubTitle = pageFormat.getFootSubTitle(0);
		footSubTitle.setLeftContent(ReportBasePanel.EmptyString4Show + "备注1：符号“-”代表该项目未见异常，符号“●”代表该项目检出异常。");
		footSubTitle = pageFormat.getFootSubTitle(1);
		footSubTitle.setLeftContent(ReportBasePanel.EmptyString4Show + "备注2：在龋齿项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。在视力项目中，符号“-”表示未见异常，符号“●”表示视力低下。");
	}

	protected void btnAppraiseReport_ActionPerformed(ActionEvent evt) throws Exception {
		viewJasperReport(evt, "StudentAppraise4One.jasper");
	}

	@Override
	protected void actionPrint_ActionPerformed(ActionEvent evt) throws Exception {
		StudentAppraiseFilterPanel filterPanel = (StudentAppraiseFilterPanel) getFilterBasePanel();
		if (filterPanel.isTableFormReportPrint()) {
			super.actionPrint_ActionPerformed(evt);
		} else {
			//						viewJasperReport(evt, "StudentAppraise4Multi.jasper");
			viewJaserReportXml(evt, "StudentAppraise4Multi_code.jrxml");
		}
	}

	private static final String customStaticTextStr = "customStaticText";
	private static final String customDynamicTextStr = "customDynamicText";
	private static final String customSplitorStr = "customSplitor";

	private void viewJaserReportXml(ActionEvent evt, String jasperFilePath) throws Exception {
		if (jasperReportService == null) {
			jasperReportService = AppContext.getBean("jasperReportService", IJasperReportService.class);
		}
		int[] selectedRows = tblMain.getSelectedRows();
		if (selectedRows == null || selectedRows.length == 0) {
			JOptionPane.showMessageDialog(this, "请先选中要查看的行！！！");
			return;
		}
		Set<String> idSet = new HashSet<String>();
		for (int i = 0; i < selectedRows.length; i++) {
			idSet.add(String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], ID_Column_Index)));
			logger.info("select id:" + String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], ID_Column_Index)));
		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p_ids", idSet);
		params.put("p_orgName", new AboutInfoUtils().getOrgName());

		//处理报表打印
		JasperDesign jasperDesign = JasperReportUtil.getJasperDesign(jasperFilePath);
		if (jasperDesign == null) {
			JOptionPane.showMessageDialog(this, "找不到jasper report打印文件！");
			logger.error("找不到jasper report打印文件！");
			return;
		}

		int detailMaxWidth = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();

		int minWidth = 50;
		int x, y, width, height, startX, startY, sigleWidth, staticTextHeight, dynamicTextHeight;
		JRDesignStaticText lastStaticText = null, curStaticText;
		JRDesignTextField lastDynamicText = null, curDynamicText;
		JRDesignSection detailSection = (JRDesignSection) jasperDesign.getDetailSection();
		JRBand[] bands = (JRBand[]) detailSection.getBands();
		JRDesignBand designBand = (JRDesignBand) bands[0];

		JRDesignStaticText customStaticText = (JRDesignStaticText) designBand.getElementByKey(customStaticTextStr);
		x = startX = customStaticText.getX();
		y = startY = customStaticText.getY();
		sigleWidth = customStaticText.getWidth() / 5;
		staticTextHeight = customStaticText.getHeight();
		designBand.removeElement(customStaticText);

		JRDesignTextField customDynamicText = (JRDesignTextField) designBand.getElementByKey(customDynamicTextStr);
		dynamicTextHeight = customDynamicText.getHeight();
		designBand.removeElement(customDynamicText);
		//循环表格
		int columnCount = tblMain.getColumnCount();
		int colWidth;
		int sortTableAddWidth = (tblMain instanceof SortableTable) ? 5 : 0;
		TableColumnModel tempColumnModel = tblMain.getColumnModel();
		JLabel cellComponent = MyTableUtils.getTableHeaderCellComponent(tblMain);
		for (int colIndex = Start_Column_Index; colIndex < columnCount; colIndex++) {
			TableColumn tableColumn = tempColumnModel.getColumn(colIndex);
			if ((tableColumn.getWidth() == 0)) {//隐藏的列
				continue;
			}
			RptColumn rptColumn = columnMap.get(allColumnOrder[colIndex]);
			String text = rptColumn.getPrintDisplayName();
			text = text.replaceAll("\n", "");

			colWidth = -1;
			if (cellComponent != null) {
				cellComponent.setText(text);
				colWidth = Math.max(colWidth, cellComponent.getPreferredSize().width + tblMain.getIntercellSpacing().width) + sortTableAddWidth;
			}
			colWidth = Math.max(colWidth, tableColumn.getMinWidth());
			width = Math.max(minWidth, colWidth);

			//			width = text.length() * sigleWidth;
			if (width + x > detailMaxWidth) {//新的一行，上一行最后的格要充满
				lastStaticText.setWidth(detailMaxWidth - x + lastStaticText.getWidth());
				lastDynamicText.setWidth(detailMaxWidth - x + lastDynamicText.getWidth());

				x = startX;
				y = y + staticTextHeight + dynamicTextHeight;
			}

			curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x, y, width, staticTextHeight);
			curStaticText.setText(text);
			designBand.addElement(curStaticText);

			curDynamicText = (JRDesignTextField) copyJRElement(customDynamicText, x, y + staticTextHeight, width, dynamicTextHeight);
			JRDesignExpression expression = new JRDesignExpression();
			//		expression.setValueClass(java.lang.String.class);
			expression.setText("$F{" + rptColumn.getColumnName() + "}");
			curDynamicText.setExpression(expression);
			designBand.addElement(curDynamicText);

			x += width;
			lastStaticText = curStaticText;
			lastDynamicText = curDynamicText;
		}
		if (true) {//处理最后一行，填充空单元格
			width = detailMaxWidth - x;
			if (width < minWidth) {//由最后的单元格直接填充
				lastStaticText.setWidth(detailMaxWidth - x + lastStaticText.getWidth());
				lastDynamicText.setWidth(detailMaxWidth - x + lastDynamicText.getWidth());
			} else {//新增单元格
				curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x, y, width, staticTextHeight);
				curStaticText.setText("");
				designBand.addElement(curStaticText);

				curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x, y + staticTextHeight, width, dynamicTextHeight);
				curStaticText.setText("");
				designBand.addElement(curStaticText);
			}
		}
		//初始化下一个坐标
		x = startX;
		y = y + staticTextHeight + dynamicTextHeight;

		// 处理最后的备注和信息反馈
		StudentAppraiseFilterPanel filterPanel = (StudentAppraiseFilterPanel) getFilterBasePanel();
		if (filterPanel.isPrintRemark()) {//备注
			width = sigleWidth * 5;
			height = staticTextHeight * 2;
			curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x, y, width, height);
			curStaticText.setText("备注");
			designBand.addElement(curStaticText);

			curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x + width, y, detailMaxWidth - width, height);
			curStaticText.setText("");
			designBand.addElement(curStaticText);
			//初始化下一个坐标
			x = startX;
			y = y + height;
		}
		if (filterPanel.isPrintFeedback()) {
			width = sigleWidth * 5;
			height = staticTextHeight * 3;
			curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x, y, width, height);
			curStaticText.setText("信息反馈");
			designBand.addElement(curStaticText);

			curStaticText = (JRDesignStaticText) copyJRElement(customStaticText, x + width, y, detailMaxWidth - width, height);
			curStaticText.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
			curStaticText.setVerticalAlignment(VerticalAlignEnum.BOTTOM);
			curStaticText.setText("家长签名：          学生签名：          ");
			designBand.addElement(curStaticText);
			//初始化下一个坐标
			x = startX;
			y = y + height;
		}

		//处理分隔线
		JRDesignLine customSplitor = (JRDesignLine) designBand.getElementByKey(customSplitorStr);
		customSplitor.setY(y + 10);
		designBand.setHeight(y + 20);

		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint print = jasperReportService.getJasperPrint(jasperReport, params);
		JasperViewer viewer = new JasperViewer(print, false);
		viewer.setVisible(true);
	}

	private JRDesignElement copyJRElement(JRDesignElement customDesignElement, int x, int y, int width, int height) throws IllegalAccessException,
			InstantiationException, InvocationTargetException, NoSuchMethodException {
		JRDesignElement curDesignElement;
		curDesignElement = (JRDesignElement) BeanUtils.cloneBean(customDesignElement);
		curDesignElement.setX(x);
		curDesignElement.setY(y);
		curDesignElement.setWidth(width);
		curDesignElement.setHeight(height);
		return curDesignElement;
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
		for (int i = 0; i < selectedRows.length; i++) {
			idSet.add(String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], ID_Column_Index)));
			logger.info("select id:" + String.valueOf(tblMain.getModel().getValueAt(selectedRows[i], ID_Column_Index)));
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

	private void saveItemOrder() throws Exception {
		StringBuffer studentAppraiseRpt_ItemOrder = new StringBuffer();
		for (int i = 0; i < columnOrder.size(); i++) {
			studentAppraiseRpt_ItemOrder.append(columnOrder.get(i)).append(",");
		}
		studentAppraiseRpt_ItemOrder.setLength(studentAppraiseRpt_ItemOrder.length() - 1);
		AppCache.getInstance().setStudentAppraiseRpt_ItemOrder(studentAppraiseRpt_ItemOrder.toString());
	}

	protected void btnChangeItemOrder_ActionPerformed(ActionEvent evt) throws Exception {
		UIContext ctx = new UIContext();
		ctx.set(UIContext.UI_TITLE, "更改选项顺序");
		ctx.set(StudentAppraiseItemOrderPanel.Param_Column_Order, columnOrder);
		ctx.set(StudentAppraiseItemOrderPanel.Param_Column_Map, columnMap);
		ctx.set(StudentAppraiseItemOrderPanel.Param_Column_Fixed, Start_Column_Index);
		UI ui = UIFactory.create(StudentAppraiseItemOrderPanel.class, UIType.MODAL, ctx, UIFrameworkUtils.getMainUI());
		ui.setVisible(true);
		//		StudentAppraiseItemOrderPanel uiObject = (StudentAppraiseItemOrderPanel) ui.getUIObject();
		Boolean isOk = (Boolean) ctx.get(StudentAppraiseItemOrderPanel.Param_IS_OK);
		if (isOk == null || !isOk)
			return;

		columnOrder = (List<String>) ctx.get(StudentAppraiseItemOrderPanel.Param_Column_Order);
		saveItemOrder();
		recreateColumn();
		isChangeItemOrder = true;
		actionQuery_ActionPerformed(evt);
		isChangeItemOrder = false;
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
					btnAppraiseReport_ActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraiseRptPanel.this, logger, e);
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
					UIHandler.handleException(StudentAppraiseRptPanel.this, logger, e);
				}
			}
		});
		toolbarPanel.add(btnChoolAllRecord, 4);

		btnChangeItemOrder = new javax.swing.JButton();
		btnChangeItemOrder.setText("更改顺序");
		btnChangeItemOrder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnChangeItemOrder_ActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraiseRptPanel.this, logger, e);
				}
			}
		});
		toolbarPanel.add(btnChangeItemOrder, 5);
	}

	public String getWholeSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("select CHECKID, TJND, Term");
		processNotNullSQL(sql, "BH");
		processNotNullSQL(sql, "SCHOOLBH");
		processNotNullSQL(sql, "GRADEBH");
		processNotNullSQL(sql, "CLASSBH");
		processNotNullSQL(sql, "RXSJ");
		processNotNullSQL(sql, "XH");
		processNotNullSQL(sql, "SCHOOL_NAME");
		processNotNullSQL(sql, "GRADE_NAME");
		processNotNullSQL(sql, "CLASS_NAME");
		processNotNullSQL(sql, "XM");
		processNotNullSQL(sql, "XB");
		processNotNullSQL(sql, "CSRQ");
		processNotNullSQL(sql, "MZ");
		processNotNullSQL(sql, "ZZMM");
		processNotNullSQL(sql, "SY");
		processNotNullSQL(sql, "TJRQ");
		processNotNullSQL(sql, "NL");
		processNotNullSQL(sql, "MARK");
		processNotNullSQL(sql, "SG");
		processNotNullSQL(sql, "SGDJQ");
		processNotNullSQL(sql, "SGDJD");
		processItemResultSQL(sql, "SGBJ");
		processNotNullSQL(sql, "TZ");
		processNotNullSQL(sql, "TZDJQ");
		processNotNullSQL(sql, "TZDJD");
		processItemResultSQL(sql, "TZBJ");
		processNotNullSQL(sql, "YYPJ");
		processNotNullSQL(sql, "YYPJX");
		processNotNullSQL(sql, "TXPJ");
		processNotNullSQL(sql, "FYPJ");
		processNotNullSQL(sql, "XW");
		processNotNullSQL(sql, "XWDJQ");
		processNotNullSQL(sql, "XWDJD");
		processItemResultSQL(sql, "XWBJ");
		processNotNullSQL(sql, "FHL");
		processNotNullSQL(sql, "FHLDJQ");
		processNotNullSQL(sql, "FHLDJD");
		processItemResultSQL(sql, "FHLBJ");
		processNotNullSQL(sql, "WL");
		processNotNullSQL(sql, "WLDJQ");
		processNotNullSQL(sql, "WLDJD");
		processItemResultSQL(sql, "WLBJ");
		processNotNullSQL(sql, "MB");
		processItemResultSQL(sql, "MBBJ");
		processNotNullSQL(sql, "SSY");
		processItemResultSQL(sql, "SSYBJ");
		processNotNullSQL(sql, "SZY");
		processItemResultSQL(sql, "SZYBJ");
		processNotNullSQL(sql, "LSL");
		processNotNullSQL(sql, "RSL");
		processNotNullSQL(sql, "QTYB");
		processItemResultSQL(sql, "LQG");
		processItemResultSQL(sql, "RQG");
		processItemResultSQL(sql, "LSY");
		processItemResultSQL(sql, "RSY");
		processItemResultSQL(sql, "BS");
		processItemResultSQL(sql, "TL");
		processItemResultSQL(sql, "XJ");
		processItemResultSQL(sql, "BB");
		processNotNullSQL(sql, "QSBNUM");
		processNotNullSQL(sql, "RQB");
		processNotNullSQL(sql, "RQS");
		processNotNullSQL(sql, "RQH");
		processNotNullSQL(sql, "HQB");
		processNotNullSQL(sql, "HQS");
		processNotNullSQL(sql, "HQH");
		processItemResultSQL(sql, "YZB");
		processItemResultSQL(sql, "BTT");
		processItemResultSQL(sql, "XK");
		processItemResultSQL(sql, "JZ");
		processItemResultSQL(sql, "SZ");
		processItemResultSQL(sql, "PZ");
		processItemResultSQL(sql, "XZ");
		processItemResultSQL(sql, "GP");
		processItemResultSQL(sql, "FEI");
		processItemResultSQL(sql, "XT");
		processNotNullSQL(sql, "XHDB");
		processItemResultSQL(sql, "FHCL");
		processItemResultSQL(sql, "PFB");
		processNotNullSQL(sql, "ZG");
		processNotNullSQL(sql, "ZGDJQ");
		processNotNullSQL(sql, "ZGDJD");
		processItemResultSQL(sql, "ZGBJ");
		processNotNullSQL(sql, "JK");
		processNotNullSQL(sql, "JKDJQ");
		processNotNullSQL(sql, "JKDJD");
		processItemResultSQL(sql, "JKBJ");
		processNotNullSQL(sql, "GPK");
		processNotNullSQL(sql, "GPKDJQ");
		processNotNullSQL(sql, "GPKDJD");
		processItemResultSQL(sql, "GPKBJ");
		processNotNullSQL(sql, "BJL");
		processNotNullSQL(sql, "GSTJ");
		processNotNullSQL(sql, "JJX");
		processItemResultSQL(sql, "BMKY");
		processItemResultSQL(sql, "SJSR");
		processNotNullSQL(sql, "WSM");
		processNotNullSQL(sql, "WSMDJQ");
		processNotNullSQL(sql, "WSMDJD");
		processItemResultSQL(sql, "WSMBJ");
		processNotNullSQL(sql, "LDTY");
		processNotNullSQL(sql, "LDTYDJQ");
		processNotNullSQL(sql, "LDTYDJD");
		processItemResultSQL(sql, "LDTYBJ");
		processNotNullSQL(sql, "LL");
		processNotNullSQL(sql, "LLDJQ");
		processNotNullSQL(sql, "LLDJD");
		processItemResultSQL(sql, "LLBJ");
		processNotNullSQL(sql, "NNL");
		processNotNullSQL(sql, "NNLDJQ");
		processNotNullSQL(sql, "NNLDJD");
		processItemResultSQL(sql, "NNLBJ");
		processNotNullSQL(sql, "ZWT");
		processNotNullSQL(sql, "ZWTDJQ");
		processNotNullSQL(sql, "ZWTDJD");
		processItemResultSQL(sql, "ZWTBJ");
		processNotNullSQL(sql, "T1Name");
		processNotNullSQL(sql, "T1");
		processNotNullSQL(sql, "T2name");
		processNotNullSQL(sql, "T2");
		processNotNullSQL(sql, "T3Name");
		processNotNullSQL(sql, "T3");
		processNotNullSQL(sql, "T4Name");
		processNotNullSQL(sql, "T4");
		processNotNullSQL(sql, "T5Name");
		processNotNullSQL(sql, "T5");
		processNotNullSQL(sql, "T6Name");
		processNotNullSQL(sql, "T6");
		processNotNullSQL(sql, "T7Name");
		processNotNullSQL(sql, "T7");
		processNotNullSQL(sql, "T8Name");
		processNotNullSQL(sql, "T8");
		processNotNullSQL(sql, "T9Name");
		processNotNullSQL(sql, "T9");
		processNotNullSQL(sql, "T10Name");
		processNotNullSQL(sql, "T10");
		processItemResultSQL(sql, "SLBH");
		processNotNullSQL(sql, "MBPJ");
		processNotNullSQL(sql, "SSYPJ");
		processNotNullSQL(sql, "SZYPJ");
		processNotNullSQL(sql, "PXPJ");
		processNotNullSQL(sql, "SLPJ");
		processNotNullSQL(sql, "XJH");
		processNotNullSQL(sql, "YYPJB");
		processNotNullSQL(sql, "TJSY");
		processNotNullSQL(sql, "MB1");
		processNotNullSQL(sql, "MB2");
		processNotNullSQL(sql, "MB3");
		processNotNullSQL(sql, "TJFS");
		processNotNullSQL(sql, "TJPJ");
		processNotNullSQL(sql, "YYPJH");
		processNotNullSQL(sql, "XMPY");
		processNotNullSQL(sql, "HXP");
		processNotNullSQL(sql, "HXPPJ");
		processItemResultSQL(sql, "EB");
		processItemResultSQL(sql, "YB");
		processItemResultSQL(sql, "TB");
		processItemResultSQL(sql, "JB");
		processItemResultSQL(sql, "PF");
		processItemResultSQL(sql, "JZX");
		processItemResultSQL(sql, "LBJ");
		processItemResultSQL(sql, "JHJS");
		processNotNullSQL(sql, "GBZAM");
		processNotNullSQL(sql, "DHSA");
		processNotNullSQL(sql, "DHSZ");
		processItemResultSQL(sql, "GG");
		processItemResultSQL(sql, "BMKT");
		processItemResultSQL(sql, "YGHKT");
		processItemResultSQL(sql, "YGEKY");
		processItemResultSQL(sql, "YGEKT");
		processItemResultSQLNoTran(sql, "BLOOD");
		processNotNullSQL(sql, "YW");
		processNotNullSQL(sql, "TW");
		processNotNullSQL(sql, "CXBH");
		processNotNullSQL(sql, "LJSL");
		processNotNullSQL(sql, "RJSL");
		processItemResultSQL(sql, "JMY");
		processItemResultSQL(sql, "WGFB");
		processItemResultSQL(sql, "YYB");
		processItemResultSQL(sql, "PEI");
		processNotNullSQL(sql, "LSLS");
		processNotNullSQL(sql, "RSLS");
		processNotNullSQL(sql, "LJSLS");
		processNotNullSQL(sql, "RJSLS");
		processItemResultSQL(sql, "RTL");
		processItemResultSQL(sql, "WSZQ");
		processNotNullSQL(sql, "RS");
		processNotNullSQL(sql, "BXP");
		processNotNullSQL(sql, "XXB");
		processNotNullSQL(sql, "SFZH");
		processNotNullSQL(sql, "DH");
		processNotNullSQL(sql, "ADDRESS");
		processNotNullSQL(sql, "RXYFJZS");
		processNotNullSQL(sql, "YMA");
		processNotNullSQL(sql, "YMAJY");
		processNotNullSQL(sql, "YMB");
		processNotNullSQL(sql, "YMBJY");
		processNotNullSQL(sql, "YMC");
		processNotNullSQL(sql, "YMCJY");
		processNotNullSQL(sql, "YMD");
		processNotNullSQL(sql, "YMDJY");
		processNotNullSQL(sql, "YME");
		processNotNullSQL(sql, "YMEJY");
		processNotNullSQL(sql, "YMQT");
		processNotNullSQL(sql, "RXHJZS");
		processNotNullSQL(sql, "JWBS");
		processNotNullSQL(sql, "QCQFY");
		processNotNullSQL(sql, "STUDGUID");
		processNotNullSQL(sql, "SCHOOLID");
		processNotNullSQL(sql, "STUDNUM");
		processNotNullSQL(sql, "YMF");
		processNotNullSQL(sql, "YMFJY");
		processNotNullSQL(sql, "V1Name");
		processNotNullSQL(sql, "V1");
		processNotNullSQL(sql, "V2Name");
		processNotNullSQL(sql, "V2");
		processNotNullSQL(sql, "V3Name");
		processNotNullSQL(sql, "V3");
		processNotNullSQL(sql, "V4Name");
		processNotNullSQL(sql, "V4");
		processNotNullSQL(sql, "V5Name");
		processNotNullSQL(sql, "V5");
		return sql.toString();
	}

	public class RptColumn {
		private String displayName;
		private String columnName;
		private String selectSql;
		private String printDisplayName;
		private String columnGroupName;
		private String[] columnGroups;
		private boolean isHidden = false;

		public RptColumn(String displayName, String columnName, String selectSql) {
			this.displayName = displayName;
			this.columnName = columnName;
			this.selectSql = selectSql;
			this.printDisplayName = displayName;
		}

		public RptColumn(String displayName, String columnName, String selectSql, String columnGroupName, String[] columnGroups) {
			this(displayName, columnName, selectSql);
			this.columnGroupName = columnGroupName;
			this.columnGroups = columnGroups;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getSelectSql() {
			return selectSql;
		}

		public void setSelectSql(String selectSql) {
			this.selectSql = selectSql;
		}

		public String getPrintDisplayName() {
			return printDisplayName;
		}

		public void setPrintDisplayName(String printDisplayName) {
			this.printDisplayName = printDisplayName;
		}

		public String getColumnGroupName() {
			return columnGroupName;
		}

		public void setColumnGroupName(String columnGroupName) {
			this.columnGroupName = columnGroupName;
		}

		public String[] getColumnGroups() {
			return columnGroups;
		}

		public void setColumnGroups(String[] columnGroups) {
			this.columnGroups = columnGroups;
		}

		public boolean isHidden() {
			return isHidden;
		}

		public void setHidden(boolean isHidden) {
			this.isHidden = isHidden;
		}

	}
}