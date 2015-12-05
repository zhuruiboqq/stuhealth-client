package com.vastcm.stuhealth.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;

import net.miginfocom.swing.MigLayout;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vastcm.io.file.ExcelFileFilter;
import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.framework.report.service.ISqlService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.report.ui.FilterCommonAction;
import com.vastcm.stuhealth.client.ui.MyDateChooser;
import com.vastcm.stuhealth.client.utils.FileChooseUtil;
import com.vastcm.stuhealth.client.utils.HSSFUtil;
import com.vastcm.stuhealth.client.utils.XSSFReadHelper;
import com.vastcm.stuhealth.client.utils.XSSFReadOption;
import com.vastcm.stuhealth.client.utils.biz.GradeMessage;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;
import com.vastcm.swing.dialog.longtime.LongTimeTaskAdapter;
import com.vastcm.swing.dialog.longtime.LongTimeTaskDialog;
import com.vastcm.swing.jtable.MyTable;
import com.vastcm.swing.selector.JSelectorBox;

/**
 * 从Excel导入结果到Result表
 * @author bob
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ImportResultDataPanel extends KernelUI {
	private static final long serialVersionUID = 1L;
	private static final String StudentCodeFieldName = "XJH";
	private static final String[] MustInputExcelColumn = { "XM", "XB", "CSRQ", StudentCodeFieldName };
	private static final Map<String, String> result2studentMap = new HashMap<String, String>();

	private Logger logger;
	private ISqlService iSqlService;
	private ICheckResultService iCheckResultService;
	private IStudentService iStudentService;

	private JTextField txtFilePath;
	private JTable table;
	private DateCellRenderer dateCellRenderer;
	private MyDateChooser dsCheckDate;
	private JLabel lblSchoolName;
	private JSelectorBox selSchoolRange;
	private MyTable tblImportItem;
	private JFormattedTextField txtEndRow;
	private JFormattedTextField txtStartRow;
	private SchoolTreeNode classTreeNode;
	private SchoolTreeNode schoolTreeNode;

	private int igronKeyRowCount = 5, keyColumnIndex = 1;//仅针对当前的模板设置

	static {
		result2studentMap.put("XM", "NAME");
		result2studentMap.put("XB", "SEX");
		result2studentMap.put("CSRQ", "BORNDATE");
		result2studentMap.put("XJH", "XJH");
		result2studentMap.put("MZ", "NATION");
		result2studentMap.put("DH", "PHONE");
		result2studentMap.put("ZZMM", "ZZMM");
		result2studentMap.put("SY", "SY");
		result2studentMap.put("ADDRESS", "ADDRESS");
		result2studentMap.put("SFZH", "SFZ");
	}

	public ImportResultDataPanel() {
		logger = LoggerFactory.getLogger(getClass());
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);

		JPanel topPanel = new JPanel();
		splitPane.setLeftComponent(topPanel);
		topPanel.setLayout(new MigLayout("insets 0,gap 0", "[grow]", "[grow][grow][160!][grow][grow][grow][grow]"));

		JPanel onePanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) onePanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		topPanel.add(onePanel, "cell 0 0,grow");

		JLabel lblNewLabel = new JLabel("1、点“浏览”按钮，找到您所要导入Excel文件：");
		onePanel.add(lblNewLabel);

		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		onePanel.add(txtFilePath);
		txtFilePath.setColumns(50);

		JButton btnBrowser = new JButton("浏览");
		btnBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LongTimeTaskDialog dialog = LongTimeTaskDialog.getInstance("Excel导入", new LongTimeTaskAdapter() {
					@Override
					public Object exec() throws Exception {
						try {
							btnBrowser_actionPerformed(null);
						} catch (Exception ee) {
							UIHandler.handleException(ImportResultDataPanel.this, logger, ee);
						}
						return null;
					}
				});
				dialog.show();
			}
		});
		onePanel.add(btnBrowser);

		JPanel twoPanel = new JPanel();
		FlowLayout fl_twoPanel = (FlowLayout) twoPanel.getLayout();
		fl_twoPanel.setAlignment(FlowLayout.LEFT);
		topPanel.add(twoPanel, "cell 0 1,grow");

		JLabel lblNewLabel_1 = new JLabel("2、在下面表格的列名内输入相应字段的Excel列名，如姓名对应为A列，则输入A，如没有则为空(生源填入城市或乡村)");
		twoPanel.add(lblNewLabel_1);

		JPanel two1Panel = new JPanel();
		FlowLayout fl_two1Panel = (FlowLayout) two1Panel.getLayout();
		fl_two1Panel.setAlignment(FlowLayout.LEFT);
		topPanel.add(two1Panel, "cell 0 2,grow");

		initTblImportItem();
		JScrollPane scrollPane_1 = new JScrollPane(tblImportItem);
		scrollPane_1.setPreferredSize(new Dimension(400, 150));
		two1Panel.add(scrollPane_1);

		JPanel threePanel = new JPanel();
		topPanel.add(threePanel, "cell 0 3,grow");
		threePanel.setLayout(new MigLayout("gap 0", "[306px][160px][70px][120px][80px]", "[22px]"));

		JLabel lblNewLabel_3 = new JLabel("3、用鼠标点击右面编辑框旁边的按钮，选择要导入的班级");
		threePanel.add(lblNewLabel_3, "cell 0 0,alignx left,aligny center");

		selSchoolRange = FilterCommonAction.createSchoolRange();
		selSchoolRange.getSelectorParam().setEnableMultiSelect(false);
		selSchoolRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				selSchoolRange_actionPerformed(event);
			}
		});
		threePanel.add(selSchoolRange, "cell 1 0,grow");

		JLabel lblNewLabel_4 = new JLabel("体检日期");
		threePanel.add(lblNewLabel_4, "cell 2 0,alignx left,aligny center");

		dsCheckDate = new MyDateChooser();
		threePanel.add(dsCheckDate, "cell 3 0,alignx left,aligny top,grow");

		//班级对应的学校
		lblSchoolName = new JLabel();
		lblSchoolName.setForeground(Color.RED);
		threePanel.add(lblSchoolName, "cell 4 0,alignx left,aligny center");

		JPanel fourPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) fourPanel.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		topPanel.add(fourPanel, "cell 0 4,grow");

		JLabel lblNewLabel_5 = new JLabel("4、请输入当前班级在Excel中的起始行");
		fourPanel.add(lblNewLabel_5);

		txtStartRow = new JFormattedTextField();
		txtStartRow.setText("6");
		txtStartRow.setColumns(4);
		fourPanel.add(txtStartRow);

		JLabel label = new JLabel("结束行");
		fourPanel.add(label);

		txtEndRow = new JFormattedTextField();
		txtEndRow.setColumns(4);
		fourPanel.add(txtEndRow);

		JPanel fivePanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) fivePanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		topPanel.add(fivePanel, "cell 0 5,grow");

		JLabel lblNewLabel_6 = new JLabel("5、以上四步完毕后，点“导入”按钮进行学生信息导入");
		fivePanel.add(lblNewLabel_6);

		JButton btnImport = new JButton("导入");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LongTimeTaskDialog dialog = LongTimeTaskDialog.getInstance("导入数据", new LongTimeTaskAdapter() {
					@Override
					public Object exec() throws Exception {
						try {
							btnImport_actionPerformed(null);
						} catch (Exception ee) {
							UIHandler.handleException(ImportResultDataPanel.this, logger, ee);
						}
						return null;
					}
				});
				dialog.show();
			}
		});
		fivePanel.add(btnImport);

		JButton btnExit = new JButton("退出");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnExit_actionPerformed(event);
			}
		});
		fivePanel.add(btnExit);

		JPanel sixPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) sixPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		topPanel.add(sixPanel, "cell 0 6,grow");

		JLabel lblNewLabel_2 = new JLabel("6、如有多个班级要导入，重复以上步骤");
		sixPanel.add(lblNewLabel_2);

		JPanel bottomPanel = new JPanel();
		splitPane.setRightComponent(bottomPanel);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		ListModel lm = new AbstractListModel() {

			public int getSize() {
				return 10000;
			}

			public Object getElementAt(int index) {
				return index + 1;
			}
		};

		DefaultTableModel dm = new DefaultTableModel(lm.getSize(), 20);
		table = new JTable(dm);
		dateCellRenderer = new DateCellRenderer();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JList rowHeader = new JList(lm);
		rowHeader.setFixedCellWidth(50);
		//		rowHeader.setFixedCellHeight(table.getRowHeight() + table.getRowMargin());
		rowHeader.setFixedCellHeight(table.getRowHeight());
		rowHeader.setCellRenderer(new RowHeaderRenderer(table));
		//		rowHeader.setCellRenderer(table.getTableHeader().getColumnModel().getColumn(0).getCellRenderer());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		scrollPane.setRowHeaderView(rowHeader);

		bottomPanel.add(scrollPane, BorderLayout.CENTER);
	}

	private void initTblImportItem() {
		ICheckItemService checkItemService = AppContext.getBean("checkItemService", ICheckItemService.class);
		List<CheckItem> checkItemList = checkItemService.getSelectedItems();
		IVaccinService vaccinService = AppContext.getBean("vaccinService", IVaccinService.class);
		List<Vaccin> vaccinList = vaccinService.getSelectedItems();

		int colSize = 3;
		Vector dataVector = new Vector(checkItemList.size() + vaccinList.size());
		// 默认列名
		Map<String, String> defatulExcelColumn = getDefaultMapping();
		addDefaultItem(dataVector, "XM", "姓名", defatulExcelColumn);
		addDefaultItem(dataVector, "XB", "性别", defatulExcelColumn);
		addDefaultItem(dataVector, "CSRQ", "出生日期", defatulExcelColumn);
		addDefaultItem(dataVector, "MZ", "民族", defatulExcelColumn);
		addDefaultItem(dataVector, "ZZMM", "政治面貌", defatulExcelColumn);
		addDefaultItem(dataVector, "SY", "生源", defatulExcelColumn);
		addDefaultItem(dataVector, "DH", "电话", defatulExcelColumn);
		addDefaultItem(dataVector, "ADDRESS", "家庭住址", defatulExcelColumn);
		addDefaultItem(dataVector, StudentCodeFieldName, "学籍号", defatulExcelColumn);
		addDefaultItem(dataVector, "SFZH", "身份证", defatulExcelColumn);
		Vector rowData;
		for (CheckItem item : checkItemList) {
			rowData = new Vector(colSize);
			rowData.add(item.getFieldname());
			rowData.add(item.getItemName());
			rowData.add(defatulExcelColumn.get(item.getFieldname()));
			dataVector.add(rowData);
		}
		for (Vaccin item : vaccinList) {
			rowData = new Vector(colSize);
			rowData.add(item.getFieldname());
			rowData.add(item.getVaccinName());
			rowData.add(defatulExcelColumn.get(item.getFieldname()));
			dataVector.add(rowData);
		}
		addDefaultItem(dataVector, "QCQFY", "月经初潮/首次遗精年龄", defatulExcelColumn);
		addDefaultItem(dataVector, "RXYFJZS", "小学入学前预防接种史", defatulExcelColumn);
		addDefaultItem(dataVector, "RXHJZS", "小学入学后预防接种史", defatulExcelColumn);
		addDefaultItem(dataVector, "JWBS", "既往病史", defatulExcelColumn);

		rowData = new Vector(colSize);
		rowData.add("数据库列名");
		rowData.add("名称");
		rowData.add("列名");
		DefaultTableModel tableModel = new DefaultTableModel(dataVector, rowData) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == this.columnIdentifiers.size() - 1;//最后列才允许编辑
			}
		};
		tblImportItem = new com.vastcm.swing.jtable.MyTable();
		tblImportItem.setModel(tableModel);
	}

	private Map<String, String> getDefaultMapping() {
		Map<String, String> defatulExcelColumn = new HashMap<String, String>(50);
		//除了A列外，模板中所有列已包含
		defatulExcelColumn.put("XM", "B");
		defatulExcelColumn.put("XB", "C");
		defatulExcelColumn.put("CSRQ", "D");
		defatulExcelColumn.put("MZ", "E");
		defatulExcelColumn.put("ZZMM", "F");
		defatulExcelColumn.put("SY", "G");
		defatulExcelColumn.put("DH", "H");
		defatulExcelColumn.put("ADDRESS", "I");
		defatulExcelColumn.put("XJH", "J");
		defatulExcelColumn.put("SFZH", "K");
		defatulExcelColumn.put("SG", "L");
		defatulExcelColumn.put("TZ", "M");
		defatulExcelColumn.put("XW", "N");
		defatulExcelColumn.put("FHL", "O");
		defatulExcelColumn.put("RSL", "P");
		defatulExcelColumn.put("LSL", "Q");
		defatulExcelColumn.put("MB", "R");
		defatulExcelColumn.put("SSY", "S");
		defatulExcelColumn.put("SZY", "T");
		defatulExcelColumn.put("XHDB", "U");
		defatulExcelColumn.put("RSY", "V");
		defatulExcelColumn.put("LSY", "W");
		defatulExcelColumn.put("RQH", "X");
		defatulExcelColumn.put("RQS", "Y");
		defatulExcelColumn.put("RQB", "Z");
		defatulExcelColumn.put("HQH", "AA");
		defatulExcelColumn.put("HQS", "AB");
		defatulExcelColumn.put("HQB", "AC");
		defatulExcelColumn.put("YZB", "AD");
		defatulExcelColumn.put("XZ", "AE");
		defatulExcelColumn.put("FEI", "AF");
		defatulExcelColumn.put("GP", "AG");
		defatulExcelColumn.put("PEI", "AH");
		defatulExcelColumn.put("JMY", "AI");
		defatulExcelColumn.put("TB", "AJ");
		defatulExcelColumn.put("JB", "AK");
		defatulExcelColumn.put("XK", "AL");
		defatulExcelColumn.put("JZ", "AM");
		defatulExcelColumn.put("SZ", "AN");
		defatulExcelColumn.put("PF", "AO");
		defatulExcelColumn.put("LBJ", "AP");
		defatulExcelColumn.put("BS", "AQ");
		defatulExcelColumn.put("EB", "AR");
		defatulExcelColumn.put("BB", "AS");
		defatulExcelColumn.put("BTT", "AT");
		defatulExcelColumn.put("XJ", "AU");
		defatulExcelColumn.put("TL", "AV");
		defatulExcelColumn.put("RTL", "AW");
		defatulExcelColumn.put("JZX", "AX");
		defatulExcelColumn.put("YYB", "AY");
		defatulExcelColumn.put("PZ", "AZ");
		defatulExcelColumn.put("WSZQ", "BA");
		defatulExcelColumn.put("LQG", "BB");
		defatulExcelColumn.put("RQG", "BC");
		defatulExcelColumn.put("RS", "BD");
		defatulExcelColumn.put("YB", "BE");
		defatulExcelColumn.put("LJSL", "BF");
		defatulExcelColumn.put("RJSL", "BG");
		defatulExcelColumn.put("JHJS", "BH");
		defatulExcelColumn.put("GBZAM", "BI");
		defatulExcelColumn.put("DHSA", "BJ");
		defatulExcelColumn.put("DHSZ", "BK");
		defatulExcelColumn.put("BLOOD", "BL");
		defatulExcelColumn.put("GG", "BM");
		defatulExcelColumn.put("BMKY", "BN");
		defatulExcelColumn.put("BMKT", "BO");
		defatulExcelColumn.put("HXP", "BP");
		defatulExcelColumn.put("BXP", "BQ");
		defatulExcelColumn.put("XXB", "BR");
		defatulExcelColumn.put("YMA", "BS");
		defatulExcelColumn.put("YMB", "BT");
		defatulExcelColumn.put("YMC", "BU");
		defatulExcelColumn.put("YMD", "BV");
		defatulExcelColumn.put("YME", "BW");
		defatulExcelColumn.put("YMF", "BX");
		defatulExcelColumn.put("YMG", "BY");
		defatulExcelColumn.put("YMH", "BZ");
		defatulExcelColumn.put("YW", "CA");
		defatulExcelColumn.put("TW", "CB");
		defatulExcelColumn.put("FHCL", "CC");
		defatulExcelColumn.put("QCQFY", "CD");
		defatulExcelColumn.put("RXYFJZS", "CE");
		defatulExcelColumn.put("RXHJZS", "CF");
		defatulExcelColumn.put("JWBS", "CG");

		return defatulExcelColumn;
	}

	private void addDefaultItem(Vector dataVector, String databaseFieldName, String name, Map<String, String> defatulExcelColumn) {
		Vector rowData = new Vector(2);
		rowData.add(databaseFieldName);
		rowData.add(name);
		rowData.add(defatulExcelColumn.get(databaseFieldName));
		dataVector.add(rowData);
	}

	protected void selSchoolRange_actionPerformed(ActionEvent event) {
		Object paramValue = selSchoolRange.getValue();
		SchoolTreeNode schoolNode = null;
		if (paramValue instanceof SchoolTreeNode) {
			schoolNode = (SchoolTreeNode) paramValue;
		} else if (paramValue instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) paramValue;
			schoolNode = (SchoolTreeNode) treeNode.getUserObject();
		}
		if (SchoolTreeNode.TYPE_CLASS != schoolNode.getType()) {
			selSchoolRange.setValue(null);
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		classTreeNode = (SchoolTreeNode) schoolNode;
		if (paramValue instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) paramValue).getParent();
			schoolTreeNode = (SchoolTreeNode) parentTreeNode.getUserObject();
			lblSchoolName.setText(schoolTreeNode.getName());
		}
	}

	protected void btnBrowser_actionPerformed(ActionEvent event) {
		File selectFile = null;
		if (StringUtils.hasText(txtFilePath.getText())) {
			selectFile = new File(txtFilePath.getText());
		}
		selectFile = FileChooseUtil.chooseFile4Open(this, new ExcelFileFilter(), selectFile);
		if (selectFile == null)
			return;
		String absolutePath = selectFile.getAbsolutePath();
		txtFilePath.setText(absolutePath);
		if (absolutePath.toLowerCase().endsWith(".xls")) {
			read2003Excel(selectFile);
		} else {
			read2007Excel(selectFile);
		}
	}

	protected void read2003Excel(File selectFile) {

		try {
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(selectFile));
			// 创建对工作表的引用。
			// 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
			HSSFSheet sheet = workbook.getSheetAt(0);
			// 也可用getSheetAt(int index)按索引引用，
			// 在Excel文档中，第一张工作表的缺省索引是0，
			// 其语句为：HSSFSheet sheet = workbook.getSheet("Sheet1");
			int totalCol = 0, totalRow = 0;
			HSSFRow row = sheet.getRow(0);
			if (row != null) {
				totalCol = row.getPhysicalNumberOfCells();
			}
			totalRow = sheet.getLastRowNum();
			if (totalRow < 2 || totalCol < 5) {
				JOptionPane.showMessageDialog(this, "工作簿中第一个表格的数据，至少1行，至少4列！");
				return;
			}

			Vector<Object> rowData;
			Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>(totalRow);
			for (int r = 0; r < totalRow; r++) {
				rowData = new Vector(totalCol);
				row = sheet.getRow(r);
				if (r > igronKeyRowCount && HSSFUtil.getCellValue(row.getCell(keyColumnIndex)) == null)
					break;
				for (int c = 0; c < totalCol; c++) {
					rowData.add(HSSFUtil.getCellValue(row.getCell(c)));
				}
				dataVector.add(rowData);
			}
			fillTableData(dataVector);
		} catch (Exception exc) {
			UIHandler.handleException(this, logger, exc);
		}
	}

	protected void read2007Excel(File selectFile) {
		try {
			XSSFReadOption option = new XSSFReadOption();
			option.setMinColumns(-1);
			option.setIgronKeyRowCount(igronKeyRowCount);
			option.setKeyColumnIndex(keyColumnIndex);

			XSSFReadHelper xlsx2csv = new XSSFReadHelper(selectFile, option);
			Vector<Vector<Object>> dataVector = xlsx2csv.process(0);
			fillTableData(dataVector);
		} catch (Exception exc) {
			UIHandler.handleException(this, logger, exc);
		}
	}

	protected void fillTableData(Vector<Vector<Object>> dataVector) {
		if (dataVector != null && dataVector.size() > 0) {
			int totalCol = dataVector.get(0).size();
			if (totalCol == 0) {
				JOptionPane.showMessageDialog(this, "选择的Excel没有记录！");
				return;
			}

			Vector rowData = new Vector(totalCol);
			rowData.setSize(totalCol);
			table.setModel(new DefaultTableModel(dataVector, rowData));
			txtEndRow.setText(String.valueOf(dataVector.size()));

			table.getColumnModel().getColumn(3).setCellRenderer(dateCellRenderer);
		}
	}

	protected void btnExit_actionPerformed(ActionEvent event) {
		JTabbedPane mainPanel = UIFrameworkUtils.getMainUI().getMainPanel();
		mainPanel.remove(mainPanel.indexOfComponent(this));
		UIFactory.disposeUI(this.getClass());
	}

	protected void btnImport_actionPerformed(ActionEvent event) {
		int year = GlobalVariables.getGlobalVariables().getYear();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		if (!((year == currentYear && currentMonth >= 9) || (year == (currentYear - 1) && currentMonth < 9))) {
			int confirm = JOptionPane.showConfirmDialog(this, "系统年份与当前时间不一致（系统年份=" + year + ",当前时间=" + currentYear + "年" + currentMonth + "月） 是否继续？",
					"确认", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
		}
		/*
		 * 判断输入是否符合
		 */
		if (classTreeNode == null) {
			JOptionPane.showMessageDialog(this, "班级不能为空！");
			selSchoolRange.requestFocusInWindow();
			return;
		}
		if (schoolTreeNode == null) {
			JOptionPane.showMessageDialog(this, "班级对应的学校不能为空！");
			selSchoolRange.requestFocusInWindow();
			return;
		}
		java.util.Date checkDate = dsCheckDate.getDate();
		if (checkDate == null) {
			JOptionPane.showMessageDialog(this, "体检日期不能为空！");
			return;
		}
		DefaultTableModel dataTableModel = (DefaultTableModel) table.getModel();
		int totalDataRowCount = dataTableModel.getRowCount();
		if (totalDataRowCount < 1) {
			JOptionPane.showMessageDialog(this, "没有记录需要导入！");
			return;
		}
		if (!StringUtils.hasText(txtStartRow.getText())) {
			JOptionPane.showMessageDialog(this, "起始行不能为空，请重新输入！");
			txtStartRow.requestFocusInWindow();
			return;
		}
		if (!StringUtils.hasText(txtEndRow.getText())) {
			JOptionPane.showMessageDialog(this, "结束行不能为空，请重新输入！");
			txtEndRow.requestFocusInWindow();
			return;
		}
		int startRowIndex = Integer.parseInt(txtStartRow.getText()) - 1;
		int endRowIndex = Integer.parseInt(txtEndRow.getText()) - 1;
		if (startRowIndex < 0 || startRowIndex > totalDataRowCount) {
			JOptionPane.showMessageDialog(this, "起始行超出范围，请重新输入！");
			txtStartRow.requestFocusInWindow();
			return;
		}
		if (endRowIndex < 0 || endRowIndex > totalDataRowCount) {
			JOptionPane.showMessageDialog(this, "结束行超出范围，请重新输入！");
			txtEndRow.requestFocusInWindow();
			return;
		}
		if (startRowIndex > endRowIndex) {
			JOptionPane.showMessageDialog(this, "起始行大于结束行，请重新输入！");
			txtStartRow.requestFocusInWindow();
			return;
		}

		int totalDataColCount = dataTableModel.getColumnCount();

		Vector<String> dbFieldName = new Vector<String>();
		Vector<Integer> dataIndex = new Vector<Integer>();
		DefaultTableModel itemTableModel = (DefaultTableModel) tblImportItem.getModel();
		for (int r = 0, size = itemTableModel.getRowCount(); r < size; r++) {
			String itemName = (String) itemTableModel.getValueAt(r, 1);
			String excelColumn = (String) itemTableModel.getValueAt(r, 2);
			if (!StringUtils.hasText(excelColumn)) {
				continue;
			}
			if (excelColumn.length() > 2) {
				JOptionPane.showMessageDialog(this, itemName + "行导入的列名超出两个字符，请重新输入");
				return;
			}
			int index = excelColumn2Index(excelColumn) - 1;
			if (index < 0 || index >= totalDataColCount) {
				JOptionPane.showMessageDialog(this, itemName + "行导入的列名超出可导入的范围，请重新输入" + totalDataColCount + " " + index);
				return;
			}
			String fieldName = (String) itemTableModel.getValueAt(r, 0);
			dbFieldName.add(fieldName);
			dataIndex.add(index);
		}
		for (String name : MustInputExcelColumn) {
			if (!dbFieldName.contains(name)) {
				JOptionPane.showMessageDialog(this, "必须要导入列" + name);
				return;
			}

		}
		/*
		 * 开始导入
		 */
		Map<String, Object> fixedParamMap = new LinkedHashMap<String, Object>();
		//		String id = UUID.randomUUID().toString();
		//		fixedParamMap.put("ID", id);
		//		fixedParamMap.put("CHECKID", id);
		fixedParamMap.put("TJRQ", checkDate);
		//		Calendar cal = Calendar.getInstance();
		//		cal.setTime(checkDate);
		//		fixedParamMap.put("TJND", cal.get(Calendar.YEAR));
		//		fixedParamMap.put("TERM", (cal.get(Calendar.MONTH) + 1) <= 6 ? 1 : 2);
		fixedParamMap.put("TJND", GlobalVariables.getGlobalVariables().getYear());
		fixedParamMap.put("TERM", GlobalVariables.getGlobalVariables().getTerm());
		fixedParamMap.put("SCHOOLID", schoolTreeNode.getId());
		fixedParamMap.put("SCHOOLBH", schoolTreeNode.getCode());
		fixedParamMap.put("SCHOOL_NAME", schoolTreeNode.getName());
		GradeMessage gradeMessage = GradeMessage.getGradeMessageByClassCode(classTreeNode.getCode());
		String schoolType = SchoolMessage.getSchoolMessageByClassLongNumber(classTreeNode.getLongNumber()).getSchoolType();
		fixedParamMap.put("SCHOOLTYPE", schoolType);
		fixedParamMap.put("GRADEBH", gradeMessage.getGradeCode());
		fixedParamMap.put("GRADE_NAME", gradeMessage.getGradeName());
		fixedParamMap.put("CLASSBH", classTreeNode.getCode());
		fixedParamMap.put("CLASS_NAME", classTreeNode.getName());
		fixedParamMap.put("CLASSLONGNO", classTreeNode.getLongNumber());

		if (insertResult(dataTableModel, startRowIndex, endRowIndex, dbFieldName, dataIndex, fixedParamMap)) {
			JOptionPane.showMessageDialog(this, "导入成功！");
		}
	}

	private boolean insertResult(DefaultTableModel dataTableModel, int startRowIndex, int endRowIndex, Vector<String> dbFieldName,
			Vector<Integer> dataIndex, Map<String, Object> fixedParamMap) {
		List<Map<String, Object>> resultDatas = new ArrayList<Map<String, Object>>(endRowIndex - startRowIndex + 1);
		List<Map<String, Object>> studentDatas = new ArrayList<Map<String, Object>>(endRowIndex - startRowIndex + 1);
		int columnSize = dataIndex.size();
		String excelClassName;

		Date checkDate = (Date) fixedParamMap.get("TJRQ");
		Calendar calendar = Calendar.getInstance();
		int endYear = calendar.get(Calendar.YEAR);
		calendar.setTime(checkDate);
		int checkYear = calendar.get(Calendar.YEAR);//体检日期
		endYear = Math.min(endYear, checkYear);
		endYear -= 3;
		int beginYear = endYear - 30;
		for (int r = startRowIndex; r <= endRowIndex; r++) {
			Map<String, Object> resultParamMap = new LinkedHashMap<String, Object>((int) (columnSize * 1.2));
			Map<String, Object> studentParamMap = new LinkedHashMap<String, Object>(16);
			excelClassName = (String) dataTableModel.getValueAt(r, 0);//第一列的为班级
			if (excelClassName == null || !excelClassName.equals(fixedParamMap.get("CLASS_NAME"))) {
				JOptionPane.showMessageDialog(this, "第" + (r + 1) + "行的班级名称与当前选中的班级不一致，中止导入！");
				return false;
			}

			resultParamMap.putAll(fixedParamMap);
			//			studentParamMap.put("stYear".toUpperCase(), fixedParamMap.get("TJND"));//已删除
			//			studentParamMap.put("TERM".toUpperCase(), fixedParamMap.get("TERM"));
			studentParamMap.put("STATUS", "T");
			studentParamMap.put("CLASSNO".toUpperCase(), fixedParamMap.get("CLASSBH"));
			studentParamMap.put("ClassName".toUpperCase(), fixedParamMap.get("CLASS_NAME"));
			studentParamMap.put("ClassLongNumber".toUpperCase(), fixedParamMap.get("CLASSLONGNO"));
			studentParamMap.put("GRADENO".toUpperCase(), fixedParamMap.get("GRADEBH"));
			studentParamMap.put("SchoolNo".toUpperCase(), fixedParamMap.get("SCHOOLBH"));
			for (int indexC = 0; indexC < columnSize; indexC++) {
				Object value = dataTableModel.getValueAt(r, dataIndex.get(indexC));
				String key = dbFieldName.get(indexC);
				//				if (isDateField(key)) {
				//					value = getAsDate(value);
				//				}
				if ("CSRQ".equals(key)) {
					if (!(value instanceof Date)) {
						JOptionPane.showMessageDialog(this, "第" + (r + 1) + "行的出生日期输入格式有误，不能转换成日期，中止导入！");
						return false;
					}
					calendar.setTime((Date) value);
					int bornYear = calendar.get(Calendar.YEAR);
					if (bornYear > endYear || bornYear < beginYear) {
						JOptionPane.showMessageDialog(this, "第" + (r + 1) + "行的出生日期输入有误，超出合理时间范围，中止导入！");
						return false;
					}
				} else if ("XB".equals(key)) {
					if ("男".equals(value) || "女".equals(value)) {
					} else {
						JOptionPane.showMessageDialog(this, "第" + (r + 1) + "行的性别输入有误，只允许输入男或女，中止导入！");
						return false;
					}
				} else if ("SFZH".equals(key) || StudentCodeFieldName.equals(key)) {
					if (value != null && value.toString().contains("'")) {
						JOptionPane.showMessageDialog(this, "第" + (r + 1) + "行的身份证或学籍号输入有误，包含了\"'\"，中止导入！");
						return false;
					}
				}
				resultParamMap.put(key, value);
				String studentField = result2studentMap.get(key);
				if (studentField != null) {
					studentParamMap.put(studentField, value);
				}
			}
			resultDatas.add(resultParamMap);
			studentDatas.add(studentParamMap);
		}
		//必须先导入学生，产生学生流水号，再导入检查结果
		getStudentService().importRecord(studentDatas, true);
		getCheckResultService().importRecord(resultDatas, true);
		return true;
	}

	private int excelColumn2Index(String excelColumn) {
		//ASCII码中，大写A对应整数65，大写Z对应整数90
		excelColumn = excelColumn.toUpperCase();
		int size = excelColumn.length();
		int index = 0;
		if (size == 1) {
			index = (excelColumn.charAt(0) - 64);
		} else if (size == 2) {
			index = (excelColumn.charAt(0) - 64) * 26 + (excelColumn.charAt(1) - 64);
		} else if (size >= 3) {
			throw new IllegalArgumentException("列名超长");
		}
		return index;
	}

	public ICheckResultService getCheckResultService() {
		if (iCheckResultService == null) {
			iCheckResultService = AppContext.getBean("checkResultService", ICheckResultService.class);
		}
		return iCheckResultService;
	}

	public IStudentService getStudentService() {
		if (iStudentService == null) {
			iStudentService = AppContext.getBean("studentService", IStudentService.class);
		}
		return iStudentService;
	}

	public ISqlService getSqlService() {
		if (iSqlService == null) {
			iSqlService = AppContext.getBean("sqlService", ISqlService.class);
		}
		return iSqlService;
	}
}

class DateCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cmp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		//		System.out.println(row + "    " + value + "    " + (value != null ? value.getClass().isAssignableFrom(Date.class) : ""));
		if (value != null && value.getClass().isAssignableFrom(Date.class)) {
			setText(dateFormat.format(value));
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return cmp;
	}
}

class RowHeaderRenderer extends DefaultTableCellRenderer implements ListCellRenderer {

	private static final long serialVersionUID = 1L;

	//	private JTable table;

	RowHeaderRenderer(JTable table) {
		//		this.table = table;
		JTableHeader header = table.getTableHeader();
		//		TableCellRenderer cellRenderer = header.getColumnModel().getColumn(0).getHeaderRenderer();
		//		TableCellRenderer cellRenderer = header.getDefaultRenderer();
		//		Component component = cellRenderer.getTableCellRendererComponent(table, "test", false, false, 0, 0);
		setOpaque(true);
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(CENTER);
		setForeground(header.getForeground());
		setBackground(header.getBackground());
		setFont(header.getFont());
		//		setForeground(component.getForeground());
		//		setBackground(component.getBackground());
		//		setFont(component.getFont());
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		setText((value == null) ? "" : value.toString());
		return this;
		//		TableCellRenderer cellRenderer = table.getTableHeader().getDefaultRenderer();
		//		return cellRenderer.getTableCellRendererComponent(table, value, false, false, -1, 0);
	}
}