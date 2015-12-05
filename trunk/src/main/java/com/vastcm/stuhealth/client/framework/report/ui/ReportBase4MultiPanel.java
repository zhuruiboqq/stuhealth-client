package com.vastcm.stuhealth.client.framework.report.ui;

import java.awt.BorderLayout;
import java.awt.print.PageFormat;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.RowStripeTableStyleProvider;
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

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class ReportBase4MultiPanel extends KernelUI {
	private static final long serialVersionUID = -4289975919468361562L;
	protected int DefaultReportIndex = 0;
	protected int totalReportSize;
	protected String[] tabNames;
	protected RptParamInfo[] rptParamInfos;
	protected GroupTable[] tblMains;
	protected PrintTable[] printTables;
	private FilterBasePanel filterBasePanel;
	protected Logger logger;

	public ReportBase4MultiPanel() {
		initComponents();
		logger = LoggerFactory.getLogger(this.getClass());

		tabNames = getReportTabName();
		totalReportSize = tabNames.length;
		tblMains = new GroupTable[totalReportSize];
		rptParamInfos = new RptParamInfo[totalReportSize];
		for (int index = 0; index < totalReportSize; index++) {
			tblMains[index] = new GroupTable();
			tblMains[index].setNestedTableHeader(true);
			((NestedTableHeader) tblMains[index].getTableHeader()).setAlignmentX(CENTER_ALIGNMENT);
			if (tblMains[index].getTableHeader().getDefaultRenderer() instanceof JLabel) {
				JLabel label = (JLabel) tblMains[index].getTableHeader().getDefaultRenderer();
				label.setHorizontalAlignment(JLabel.CENTER);
			} else {
				StyledTableCellRenderer thr = new StyledTableCellRenderer();
				thr.setHorizontalAlignment(JLabel.CENTER);
				tblMains[index].getTableHeader().setDefaultRenderer(thr);
			}
			tblMains[index].setAlwaysRequestFocusForEditor(true);
			tblMains[index].setClickCountToStart(2);
			tblMains[index].setEditorAutoCompletionMode(JideTable.EDITOR_AUTO_COMPLETION_MODE_TABLE);
			tblMains[index].setColumnAutoResizable(true);
			tblMains[index].setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
			tblMains[index].setTableStyleProvider(new RowStripeTableStyleProvider());
			//			tblMain.setEnabled(false);//设置后，就不允许修改和选择

			JPanel insidePanel = new JPanel(new BorderLayout());
			insidePanel.add(new JScrollPane(tblMains[index]), BorderLayout.CENTER);
			mainTab.addTab(tabNames[index], insidePanel);
		}

		initRptParamInfo();
	}

	public abstract String[] getReportTabName();

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		btnPrintPreview.setVisible(false);
		if (filterBasePanel != null) {
			UIContext filterCtx = new UIContext(getUIContext());
			filterCtx.set(UIContext.UI_OWNER, this);
			filterBasePanel.setUIContext(filterCtx);
			filterBasePanel.onLoad();
			add(filterBasePanel.getFilterPanel(), java.awt.BorderLayout.NORTH);
			if (rptParamInfos[DefaultReportIndex] != null && rptParamInfos[DefaultReportIndex].getFilterParam() != null) {
				filterBasePanel.initFilterParam(rptParamInfos[DefaultReportIndex].getFilterParam());
			}
		}
		if (rptParamInfos[DefaultReportIndex].isIsFirstFilter()) {
			actionQuery_ActionPerformed(null);
		} else {
			initTable(false);
		}
	}

	protected void initRptParamInfo() {
	}

	protected void initTable(boolean isRebuild) {
		if (!isRebuild) {
			for (int index = 0; index < totalReportSize; index++) {
				tblMains[index].setModel(new RptDefaultTableModel(null, rptParamInfos[index].getColumnNames()));
				if (rptParamInfos[index].getColumnWidths() != null) {
					int[] columnWidths = rptParamInfos[index].getColumnWidths();
					String[] columnNames = rptParamInfos[index].getColumnNames();
					for (int i = 0; i < columnWidths.length; i++)
						tblMains[index].getColumn(columnNames[i]).setPreferredWidth(columnWidths[i]);
				}
			}
		}
		for (int index = 0; index < totalReportSize; index++) {
			TableUtils.autoResizeAllColumns(tblMains[index], true);
		}
	}

	/**
	 * 在查询前子类必须构造报表参数的查询语句和参数
	 */
	public void beforeQuery(java.awt.event.ActionEvent evt) {
		if (filterBasePanel != null) {
			filterBasePanel.verifyInput(evt);
			for (int index = 0; index < totalReportSize; index++) {
				rptParamInfos[index].setFilterParam(filterBasePanel.getFilterParam());
			}
		}
	}

	protected void actionQuery_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		beforeQuery(evt);
		for (int index = 0; index < totalReportSize; index++) {
			List datas = rptParamInfos[index].getSqlService().query(rptParamInfos[index].getQuerySQL(), rptParamInfos[index].getHqlParam());
			loadReportData(datas, index);
		}
		initTable(true);
	}

	public void loadReportData(List datas, int index) throws Exception {
		SortableTreeTableModel sortableTreeTableModel = new SortableTreeTableModel(new RptDefaultTableModel(
				(Object[][]) datas.toArray(new Object[][] {}), rptParamInfos[index].getColumnNames()));
		tblMains[index].setModel(sortableTreeTableModel);
	}

	protected void actionExportExcel_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		File selectFile = FileChooseUtil.chooseFile4Save(this, new ExcelFileFilter(), ".xls");
		if (selectFile != null) {
			JTableExportParam[] params = new JTableExportParam[totalReportSize];
			for (int index = 0; index < totalReportSize; index++) {
				JTableExportParam param = new JTableExportParam();
				param.setReportTitle(tabNames[index]);
				param.setTabTitle(param.getReportTitle());
				param.setExportTable(tblMains[index]);
				processTableExportParam(param, index);
				params[index] = param;
			}
			JTableExportUtil.exportToSheet(selectFile, params);

			JOptionPane.showMessageDialog(this, "导出成功!");
		}
	}

	protected void processTableExportParam(JTableExportParam param, int index) {
		//		param.addHeaderMessage(new MessageFormat("左边"), new MessageFormat("中间"), new MessageFormat("右边"));
		//		param.addHeaderMessage(new MessageFormat("左边1"), new MessageFormat("中间1"), new MessageFormat("右边1"));
		//		param.addHeaderMessage(new MessageFormat("左边2"), new MessageFormat("中间2"), new MessageFormat("右边2"));
		//		param.addFooterMessage(new MessageFormat("左边3"), new MessageFormat("中间"), new MessageFormat("右边"));
	}

	protected void actionPrintPreview_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		//TODO zrb actionPrintPreview
	}

	protected void actionPrint_ActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
		if (printTables == null) {
			printTables = new PrintTable[totalReportSize];
		}
		int index = mainTab.getSelectedIndex();
		if (index == -1) {
			JOptionPane.showMessageDialog(this, "请先选择一张具体报表，再打印。");
			return;
		}
		//		for (int index = 0; index < totalReportSize; index++) {
		String tablePreferenceByName = TableUtils.getTablePreferenceByName(tblMains[index]);

		//		if (printTable == null) {
		printTables[index] = PrintTableUtil.createPrintTable(tblMains[index], PageFormat.LANDSCAPE, 0);
		ExtPageFormat pageFormat = (ExtPageFormat) printTables[index].getPageFormat();
		pageFormat.getHead().setMidContent(tabNames[index]);
		//		}
		beforePrintTable(printTables[index], false);
		printTables[index].printPreview();

		TableUtils.setTablePreferenceByName(tblMains[index], tablePreferenceByName);
		//		}
	}

	protected void beforePrintTable(PrintTable printTable, boolean isPrintPreview) {

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
	private void initComponents() {

		centerPanel = new javax.swing.JPanel();
		toolbarPanel = new javax.swing.JPanel();
		btnQuery = new javax.swing.JButton();
		btnExportExcel = new javax.swing.JButton();
		btnPrint = new javax.swing.JButton();
		btnPrintPreview = new javax.swing.JButton();
		btnExit = new javax.swing.JButton();
		mainTab = new javax.swing.JTabbedPane();
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
		tblPanel.add(mainTab, java.awt.BorderLayout.CENTER);
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
	protected javax.swing.JTabbedPane mainTab;
	protected javax.swing.JPanel tblPanel;
	protected javax.swing.JPanel toolbarPanel;
}