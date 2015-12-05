/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.report.ui;

import java.awt.print.PageFormat;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.StyledTableCellRenderer;
import com.jidesoft.grid.TableUtils;
import com.vastcm.io.file.ExcelFileFilter;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.utils.FileChooseUtil;
import com.vastcm.stuhealth.client.utils.JTableExportParam;
import com.vastcm.stuhealth.client.utils.JTableExportUtil;
import com.vastcm.swing.print.table.ExtPageFormat;
import com.vastcm.swing.print.table.PrintTable;

/**
 * 
 * @author bob
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportBasePanel extends KernelUI {
	private static final long serialVersionUID = -4289975919468361562L;
	protected RptParamInfo rptParamInfo;
	protected GroupTable tblMain;
	protected JScrollPane scrollTablePanel;
	private FilterBasePanel filterBasePanel;
	private String tablePreferenceByName;
	protected Logger logger;
	public static String EmptyString4Show = "        ";

	public ReportBasePanel() {
		initComponents();
		logger = LoggerFactory.getLogger(getClass());

		if (scrollTablePanel == null) {
			tblMain = new GroupTable();
			tblMain.setNestedTableHeader(true);
			//            ((NestedTableHeader) tblMain.getTableHeader()).setUseNativeHeaderRenderer(true);
			((NestedTableHeader) tblMain.getTableHeader()).setAlignmentX(CENTER_ALIGNMENT);
			//            TableCellRenderer cellRe = tblMain.getTableHeader().getDefaultRenderer();
			//            System.out.println(cellRe.getClass());
			if (tblMain.getTableHeader().getDefaultRenderer() instanceof JLabel) {
				JLabel label = (JLabel) tblMain.getTableHeader().getDefaultRenderer();
				label.setHorizontalAlignment(JLabel.CENTER);
			} else {
				StyledTableCellRenderer thr = new StyledTableCellRenderer();
				thr.setHorizontalAlignment(JLabel.CENTER);
				tblMain.getTableHeader().setDefaultRenderer(thr);
			}
			tblMain.setAlwaysRequestFocusForEditor(true);
			tblMain.setClickCountToStart(2);
			tblMain.setEditorAutoCompletionMode(JideTable.EDITOR_AUTO_COMPLETION_MODE_TABLE);
			tblMain.setColumnAutoResizable(true);
			tblMain.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
			//			tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
			//			tblMain.setEnabled(false);//设置后，就不允许修改和选择

			scrollTablePanel = new JScrollPane(tblMain);
			tblPanel.add(scrollTablePanel, java.awt.BorderLayout.CENTER);

		}
		//		filterBasePanel = new StudentAppraiseFilterPanel();
		//		CollapsiblePane pane = new CollapsiblePane("过滤条件");
		//		pane.setContentPane(filterBasePanel.getFilterPanel());
		//		add(pane, java.awt.BorderLayout.NORTH);
		//		filterBasePanel = null;
		initRptParamInfo();
		initTable(false);
	}

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		btnPrintPreview.setVisible(false);
		if (filterBasePanel != null) {
			UIContext filterCtx = new UIContext(getUIContext());
			filterCtx.set(UIContext.UI_OWNER, this);
			filterBasePanel.setUIContext(filterCtx);
			filterBasePanel.onLoad();
			//			filterBasePanel.setBounds(0, 0, 700, 130);
			add(filterBasePanel.getFilterPanel(), java.awt.BorderLayout.NORTH);
			if (rptParamInfo != null && rptParamInfo.getFilterParam() != null) {
				filterBasePanel.initFilterParam(rptParamInfo.getFilterParam());
			}
		}
		if (rptParamInfo.isIsFirstFilter()) {
			actionQuery_ActionPerformed(null);
		} else {
			initTable(false);
		}
	}

	protected void initRptParamInfo() {
	}

	protected void initTable(boolean isRebuild) {
		if (!isRebuild) {
			tblMain.setModel(new RptDefaultTableModel(null, rptParamInfo.getColumnNames()));
			if (rptParamInfo.getColumnWidths() != null) {
				int[] columnWidths = rptParamInfo.getColumnWidths();
				String[] columnNames = rptParamInfo.getColumnNames();
				for (int i = 0; i < columnWidths.length; i++)
					tblMain.getColumn(columnNames[i]).setPreferredWidth(columnWidths[i]);
			}
		}
		TableUtils.autoResizeAllColumns(tblMain, true);
	}

	/**
	 * 在查询前子类必须构造报表参数的查询语句和参数
	 */
	public void beforeQuery(java.awt.event.ActionEvent evt) {
		if (filterBasePanel != null) {
			filterBasePanel.verifyInput(evt);
			rptParamInfo.setFilterParam(filterBasePanel.getFilterParam());
		}
	}

	protected void actionQuery_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		tablePreferenceByName = TableUtils.getTablePreferenceByName(tblMain);
		beforeQuery(evt);
		List datas = rptParamInfo.getSqlService().query(rptParamInfo.getQuerySQL(), rptParamInfo.getHqlParam());
		loadReportData(datas);
	}

	public void loadReportData(List datas) throws Exception {
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(new RptDefaultTableModel(
				(Object[][]) datas.toArray(new Object[][] {}), rptParamInfo.getColumnNames()));
		tblMain.setModel(sortableTreeTableModel);
		initTable(true);
	}

	protected void actionExportExcel_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		File selectFile = FileChooseUtil.chooseFile4Save(this, new ExcelFileFilter(), ".xls");
		if (selectFile != null) {
			JTableExportParam param = new JTableExportParam();
			param.setReportTitle((String) getUIContext().get(UIContext.UI_TITLE));
			param.setTabTitle(param.getReportTitle());
			param.setExportTable(tblMain);
			processTableExportParam(param);
			JTableExportUtil.exportToSheet(selectFile, param);

			JOptionPane.showMessageDialog(this, "导出成功!");
		}
	}

	protected void processTableExportParam(JTableExportParam param) {
		//		param.addHeaderMessage(new MessageFormat("左边"), new MessageFormat("中间"), new MessageFormat("右边"));
		//		param.addHeaderMessage(new MessageFormat("左边1"), new MessageFormat("中间1"), new MessageFormat("右边1"));
		//		param.addHeaderMessage(new MessageFormat("左边2"), new MessageFormat("中间2"), new MessageFormat("右边2"));
		//		param.addFooterMessage(new MessageFormat("左边3"), new MessageFormat("中间"), new MessageFormat("右边"));
	}

	protected PrintTable printTable;

	protected void actionPrintPreview_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		//		if (printTable == null) {
		printTable = PrintTableUtil.createPrintTable(tblMain, PageFormat.LANDSCAPE, 0);
		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		pageFormat.getHead().setMidContent((String) getUIContext().get(UIContext.UI_TITLE));
		//		}
		beforePrintTable(printTable, true);
		printTable.printPreview();
	}

	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {

	}

	protected void actionPrint_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		tablePreferenceByName = TableUtils.getTablePreferenceByName(tblMain);

		//		if (printTable == null) {
		printTable = PrintTableUtil.createPrintTable(tblMain, PageFormat.LANDSCAPE, 0);
		ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
		pageFormat.getHead().setMidContent((String) getUIContext().get(UIContext.UI_TITLE));
		//		}
		beforePrintTable(printTable, false);
		printTable.printPreview();
		//		printTable.doPrintWithDialog();

		//		MessageFormat headerFmt = new MessageFormat((String) getUIContext().get(UIContext.UI_TITLE));
		//		MessageFormat footerFmt = new MessageFormat("当前页 {0}");
		//		boolean isShowPrintSet = false;
		//		PrintRequestAttributeSet printAttrSet = new HashPrintRequestAttributeSet();
		//		printAttrSet.add(javax.print.attribute.standard.OrientationRequested.LANDSCAPE);
		//		tblMain.print(JTable.PrintMode.NORMAL, headerFmt, footerFmt, isShowPrintSet, printAttrSet, true);

		TableUtils.setTablePreferenceByName(tblMain, tablePreferenceByName);
	}

	public FilterBasePanel getFilterBasePanel() {
		return filterBasePanel;
	}

	public void setFilterBasePanel(FilterBasePanel filterBasePanel) {
		this.filterBasePanel = filterBasePanel;
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		centerPanel = new javax.swing.JPanel();
		toolbarPanel = new javax.swing.JPanel();
		btnQuery = new javax.swing.JButton();
		btnExportExcel = new javax.swing.JButton();
		btnPrint = new javax.swing.JButton();
		btnPrintPreview = new javax.swing.JButton();
		btnExit = new javax.swing.JButton();
		tblPanel = new javax.swing.JPanel();

		setLayout(new java.awt.BorderLayout());

		centerPanel.setLayout(new java.awt.BorderLayout());

		toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		btnQuery.setText("查  询");
		btnQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnQueryActionPerformed(evt);
			}
		});
		toolbarPanel.add(btnQuery);

		btnExportExcel.setText("导出Excel");
		btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnExportExcelActionPerformed(evt);
			}
		});
		toolbarPanel.add(btnExportExcel);

		btnPrint.setText("打  印");
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrintActionPerformed(evt);
			}
		});
		toolbarPanel.add(btnPrint);

		btnPrintPreview.setText("打印预览");
		btnPrintPreview.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrintPreviewActionPerformed(evt);
			}
		});
		toolbarPanel.add(btnPrintPreview);

		btnExit.setText("退  出");
		btnExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnExitActionPerformed(evt);
			}
		});
		toolbarPanel.add(btnExit);

		centerPanel.add(toolbarPanel, java.awt.BorderLayout.NORTH);

		tblPanel.setLayout(new java.awt.BorderLayout());
		centerPanel.add(tblPanel, java.awt.BorderLayout.CENTER);

		add(centerPanel, java.awt.BorderLayout.CENTER);
	}

	private void btnQueryActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			actionQuery_ActionPerformed(evt);
		} catch (Exception ex) {
			UIHandler.handleException(this, logger, ex);
		}
	}

	private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			actionExportExcel_ActionPerformed(evt);
		} catch (Exception ex) {
			UIHandler.handleException(this, logger, ex);
		}
	}

	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			actionPrint_ActionPerformed(evt);
		} catch (Exception ex) {
			UIHandler.handleException(this, logger, ex);
		}
	}

	private void btnPrintPreviewActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			actionPrintPreview_ActionPerformed(evt);
		} catch (Exception ex) {
			UIHandler.handleException(this, logger, ex);
		}
	}

	private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
		JTabbedPane mainPanel = UIFrameworkUtils.getMainUI().getMainPanel();
		mainPanel.remove(mainPanel.indexOfComponent(this));
		UIFactory.disposeUI(this.getClass());
	}

	protected javax.swing.JButton btnExit;
	protected javax.swing.JButton btnExportExcel;
	protected javax.swing.JButton btnPrint;
	protected javax.swing.JButton btnPrintPreview;
	protected javax.swing.JButton btnQuery;
	private javax.swing.JPanel centerPanel;
	protected javax.swing.JPanel tblPanel;
	protected javax.swing.JPanel toolbarPanel;
}