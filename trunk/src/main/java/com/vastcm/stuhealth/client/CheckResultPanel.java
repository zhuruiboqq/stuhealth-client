/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.CheckResult;
import com.vastcm.stuhealth.client.entity.Nation;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.VaccinItem;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.INationService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.IVaccinItemService;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.stuhealth.client.framework.ui.EditUI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIStatusEnum;
import com.vastcm.stuhealth.client.ui.MyDateChooser;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.biz.CheckResultItemMapping;
import com.vastcm.stuhealth.client.utils.biz.GradeMessage;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;
import com.vastcm.stuhealth.client.utils.biz.StudentCode;
import com.vastcm.stuhealth.client.utils.biz.VaccinItemMapping;
import com.vastcm.swing.jtable.CellValueChangeListener;
import com.vastcm.swing.jtable.MyCellEditor;
import com.vastcm.swing.jtable.MyRow;

/**
 * 
 * @author House
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CheckResultPanel extends EditUI<CheckResult> {

	private Logger logger = LoggerFactory.getLogger(CheckResultPanel.class);
	private SchoolTreeNode schClass;
	private Student student = new Student();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private List<String> stuCodeLs = new ArrayList<String>();
	//    private Map<String, Object> itemValueMap;
	private DefaultTableModel tblCheckItem1Model;
	private DefaultTableModel tblVaccin1Model;
	private DefaultTableModel tblVaccin2Model;
	private final int colIndex_checkItemFieldname = 4;
	private final int colIndex_checkItemResult = 3;
	private final int colIndex_checkItemAlias = 1;
	private final int colIndex_vaccin1Fieldname = 4;
	private final int colIndex_vaccin1Alias = 1;
	private final int colIndex_vaccin1Result = 2;
	private final int colIndex_vaccin1Advise = 3;
	private final int colIndex_vaccin2Fieldname = 3;
	private final int colIndex_vaccin2Result = 2;
	private List<String> checkItemFieldLs = new ArrayList<String>();
	private List<String> vaccinFieldLs = new ArrayList<String>();
	private List<String> fixItemFields = new ArrayList<String>();
	private ListIterator<String> iterStuCode;
	private CheckResultItemMapping checkResultItemMapping = new CheckResultItemMapping();
	private VaccinItemMapping vaccinItemMapping = new VaccinItemMapping();
	private boolean isTriggerChangeEvent = true;

	class TeethProblem {
		String name;
		int index;
		BigDecimal value;

		public TeethProblem(String name, BigDecimal value, int index) {
			this.name = name;
			this.index = index;
			this.value = value;
		}
	}

	// 用于计算体检项目：龋失补牙数=乳龋患+乳龋失+乳龋补+恒龋患+恒龋失+恒龋补，记录各项在table中的行号、名称及值
	private Map<String, TeethProblem> teethProblemMap = new HashMap<String, TeethProblem>();

	private static Set<String> customFields = new HashSet<String>();

	static {
		customFields.add("T1");
		customFields.add("T2");
		customFields.add("T3");
		customFields.add("T4");
		customFields.add("T5");
		customFields.add("T6");
		customFields.add("T7");
		customFields.add("T8");
		customFields.add("T9");
		customFields.add("T10");
		customFields.add("V1");
		customFields.add("V2");
		customFields.add("V3");
		customFields.add("V4");
		customFields.add("V5");
	}

	/**
	 * Creates new form CheckResultPanel
	 */
	public CheckResultPanel() {
		initComponents();
	}

	@Override
	public void onLoad() throws Exception {
		//        selectClass();
		initComponentsEx();
		super.onLoad();
	}

	@Override
	protected void initData() throws Exception {
		schClass = (SchoolTreeNode) getUIContext().get("classNode");
		IStudentService stuSrv = getStudentService();
		List<Student> ls = stuSrv.getStudentsByClass(schClass.getLongNumber(), false);
		for (Student s : ls) {
			stuCodeLs.add(s.getStudentCode());
		}
		logger.info("student count: " + stuCodeLs.size());
		iterStuCode = stuCodeLs.listIterator();
		String studentCode = (String) getUIContext().get("studentCode");
		if (studentCode != null) {
			student = getStudentService().getStudentByCode(studentCode, schClass.getLongNumber());
			while (iterStuCode.hasNext()) {
				String stuCode = iterStuCode.next();
				if (studentCode.equals(stuCode)) {
					break;
				}
			}
		} else {
			initNewStudent();
		}
		loadData();

		//        super.initData(); 
	}

	private void initTeethProblemMap() {
		teethProblemMap.put("QSBNUM", new TeethProblem("QSBNUM", new BigDecimal("0"), -1)); // '龋失补牙数'
		teethProblemMap.put("RQH", new TeethProblem("RQH", new BigDecimal("0"), -1)); // '乳龋患'
		teethProblemMap.put("RQS", new TeethProblem("RQS", new BigDecimal("0"), -1)); // 乳龋失
		teethProblemMap.put("RQB", new TeethProblem("RQB", new BigDecimal("0"), -1)); // 乳龋补
		teethProblemMap.put("HQH", new TeethProblem("HQH", new BigDecimal("0"), -1)); // 恒龋患
		teethProblemMap.put("HQS", new TeethProblem("HQS", new BigDecimal("0"), -1)); // 恒龋失
		teethProblemMap.put("HQB", new TeethProblem("HQB", new BigDecimal("0"), -1)); // 恒龋补
	}

	protected void selectClass() {
		//        UIFactory.create(SchoolTreePanel, UIType.TAB, null, null)
		SchoolTreePanel destSchool = new SchoolTreePanel();
		destSchool.setToolbarVisible(false);
		destSchool.setMergedSchoolVisible(true);
		SelectorDialog<SchoolTreeNode> selectorUI = new SelectorDialog<SchoolTreeNode>((JFrame) SwingUtilities.getWindowAncestor(this), true);
		selectorUI.setUI(destSchool);
		selectorUI.setVisible(true);
		schClass = selectorUI.getValue();
		logger.info("selected value: " + schClass.getCode() + " " + schClass.getName());
	}

	protected void initComponentsEx() {
		lblXh.setVisible(false);
		txtXh.setVisible(false);
		lblBh.setVisible(false);
		txtBh.setVisible(false);
		lblSFZ.setVisible(false);
		txtSFZ.setVisible(false);

		INationService nationSrv = AppContext.getBean("nationService", INationService.class);
		List<Nation> lsNation = nationSrv.getAll();
		//		cbFolk.setModel(new DefaultComboBoxModel(lsNation.toArray()));
		for (Nation n : lsNation) {
			cbFolk.addItem(n.getNationName());
		}

		for (StudentSourceEnum src : StudentSourceEnum.values()) {
			cbSource.addItem(src);
		}

		txtBh.setEditable(false);

		initVaccinTable1();
		initVaccinTable2();
		initCheckItemTable1();

		setEnterAndTabEvent(tblCheckItem1);
		setEnterAndTabEvent(tblVaccin1);
		setEnterAndTabEvent(tblVaccin2);

	}

	/*
	 * 设置表格中回车和TAB键响应事件。当按回车或TAB键时，焦点跳到下一个可编辑的单元格
	 */
	protected void setEnterAndTabEvent(final JTable table1) {
		AbstractAction moveForward = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean ifNext = true;
				if (table1.isEditing()) {
					ifNext = table1.getCellEditor().stopCellEditing();
				}
				if (ifNext) {
					int rowCount = table1.getRowCount();
					int colCount = table1.getColumnCount();
					int selRow = table1.getSelectedRow();
					int selCol = table1.getSelectedColumn();
					int nextcol = 0;
					do {
						if (selCol + nextcol == colCount - 1) //if last column
						{
							if (selRow == rowCount - 1) //if last row
							{
								selCol = -1;
								//selRow=0;
								selRow++;
								if (table1 == tblVaccin1) {
									tblVaccin2.requestFocus();
								} else if (table1 == tblVaccin2) {
									tblCheckItem1.requestFocus();
								} else if (table1 == tblCheckItem1) {
									txtComment.requestFocus();
								}
							} else {
								selCol = -1;
								selRow++;
							}
						}
						nextcol = nextcol + 1;
					} while ((table1.getModel().isCellEditable(selRow, selCol + nextcol)) == false); //find next editable cell

					table1.changeSelection(selRow, selCol + nextcol, false, false);
					table1.editCellAt(selRow, selCol + nextcol);
				}

			}

		};
		//change Enter to Tab in table
		table1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "moveForward");
		table1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "moveForward");
		table1.getActionMap().put("moveForward", moveForward);

	}

	protected void initCheckItemTable1() {
		initTeethProblemMap();
		TableColumn colId = tblCheckItem1.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		TableColumn fieldName = tblCheckItem1.getTableHeader().getColumnModel().getColumn(colIndex_checkItemFieldname);
		fieldName.setMinWidth(0);
		fieldName.setMaxWidth(0);
		fieldName.setPreferredWidth(0);
		fieldName.setWidth(0);

		tblCheckItem1.setRowSelectionAllowed(false);
		tblCheckItem1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ICheckItemService checkItemSrv = AppContext.getBean("checkItemService", ICheckItemService.class);
		List<CheckItem> checkItemLs = checkItemSrv.getSelectedItems();
		for (CheckItem item : checkItemLs) {
			MyRow row = tblCheckItem1.addRow();
			row.setValue("ID", item.getFieldname());
			row.setValue("项目名称", item.getItemName());
			row.setValue("单位", item.getUnit());
			row.setValue("ItemFieldName", item.getFieldname());
			checkItemFieldLs.add(item.getFieldname());

			TeethProblem tp = teethProblemMap.get(item.getFieldname());
			if (tp != null) {
				tp.index = tblCheckItem1.getRowCount() - 1;
				logger.info("init teeth , fieldname=" + item.getFieldname() + ", rowIndex=" + tp.index);
			}
		}

		ICheckItemResultService itemResult = AppContext.getBean("checkItemResultService", ICheckItemResultService.class);
		List<CheckItemResult> itemResultLs = itemResult.getAll();
		Map<String, List> itemResultMap = new HashMap<String, List>();
		for (CheckItemResult item : itemResultLs) {
			List ls = itemResultMap.get(item.getFieldMc());
			if (ls == null) {
				ls = new ArrayList();
				itemResultMap.put(item.getFieldMc(), ls);
			}
			ls.add(item.getItemResult());
		}

		tblCheckItem1.getColumnModel().getColumn(colIndex_checkItemResult)
				.setCellEditor(new MyCellEditor(new JComboBox(), itemResultMap, BigDecimal.class));
		tblCheckItem1.validate();

		tblCheckItem1Model = (DefaultTableModel) tblCheckItem1.getModel();

		tblCheckItem1.addCellValueChangeListener(new CellValueChangeListener() {

			@Override
			public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
				tableValueChanged_checkItem(oldValue, newValue, rowIndex, colIndex);
			}
		});
	}

	protected void tableValueChanged_checkItem(Object oldValue, Object newValue, int rowIndex, int colIndex) {
		if (!isTriggerChangeEvent) {
			return;
		}
		// 重算龋失补牙数
		//		logger.info("invoke tableValueChanged_checkItem()");
		TeethProblem teethNo1 = teethProblemMap.get("QSBNUM");
		if (teethNo1.index == -1) {
			return;
		}
		BigDecimal sum = new BigDecimal("0");
		boolean isCount = false;
		for (TeethProblem tp : teethProblemMap.values()) {
			if (tp.name.equals("QSBNUM") || tp.index == -1) {
				continue;
			}
			Object v = tblCheckItem1.getValueAt(tp.index, colIndex_checkItemResult);
			if (v != null && !v.toString().isEmpty()) {
				//				logger.info("v type:" + v.getClass().getName());
				//				logger.info("v=" + v);
				isCount = true;
				sum = sum.add(new BigDecimal(v.toString()));
			}
		}
		//		logger.info("QSBNUM=" + sum);
		if (!isCount) {
			sum = null;
		}
		isTriggerChangeEvent = false;
		tblCheckItem1.setValueAt(sum, teethNo1.index, colIndex_checkItemResult);
		isTriggerChangeEvent = true;
	}

	protected void initVaccinTable1() {
		TableColumn colId = tblVaccin1.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		TableColumn fieldName = tblVaccin1.getTableHeader().getColumnModel().getColumn(colIndex_vaccin1Fieldname);
		fieldName.setMinWidth(0);
		fieldName.setMaxWidth(0);
		fieldName.setPreferredWidth(0);
		fieldName.setWidth(0);

		tblVaccin1.setRowSelectionAllowed(false);
		tblVaccin1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		IVaccinService vaccinService = AppContext.getBean("vaccinService", IVaccinService.class);
		List<Vaccin> vaccinLs = vaccinService.getSelectedItems();
		for (Vaccin vaccin : vaccinLs) {
			MyRow row = tblVaccin1.addRow();
			row.setValue("ID", vaccin.getId());
			row.setValue("项目名称", vaccin.getVaccinName());
			row.setValue("ItemFieldName", vaccin.getFieldname());
			vaccinFieldLs.add(vaccin.getFieldname());
		}

		IVaccinItemService vaccinItemService = AppContext.getBean("vaccinItemService", IVaccinItemService.class);
		List<VaccinItem> vaccinItemLs = vaccinItemService.getAll();
		Map<String, List> vaccinMap = new HashMap<String, List>();
		for (VaccinItem item : vaccinItemLs) {
			List ls = vaccinMap.get(item.getVaccinId());
			if (ls == null) {
				ls = new ArrayList();
				vaccinMap.put(item.getVaccinId(), ls);
			}
			ls.add(item.getVaccItem());
		}

		tblVaccin1.getColumnModel().getColumn(colIndex_vaccin1Result).setCellEditor(new MyCellEditor(new JComboBox(), vaccinMap, BigDecimal.class));
		tblVaccin1.validate();

		tblVaccin1Model = (DefaultTableModel) tblVaccin1.getModel();
	}

	protected void initVaccinTable2() {
		TableColumn colId = tblVaccin2.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		TableColumn fieldName = tblVaccin2.getTableHeader().getColumnModel().getColumn(colIndex_vaccin2Fieldname);
		fieldName.setMinWidth(0);
		fieldName.setMaxWidth(0);
		fieldName.setPreferredWidth(0);
		fieldName.setWidth(0);

		tblVaccin2.setRowSelectionAllowed(false);
		tblVaccin2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		String[][] fixItems = new String[][] { { "小学入学前预防接种史", "RXYFJZS" }, { "入学后预防接种史", "RXHJZS" }, { "其他疫苗", "YMQT" }, { "既往病史", "JWBS" } };

		for (int i = 0; i < fixItems.length; i++) {
			MyRow row = tblVaccin2.addRow();
			row.setValue("项目名称", fixItems[i][0]);
			row.setValue("ItemFieldName", fixItems[i][1]);
			fixItemFields.add(fixItems[i][1]);
		}

		tblVaccin2Model = (DefaultTableModel) tblVaccin2.getModel();

	}

	public ICheckResultService getCheckResultService() {
		return (ICheckResultService) getService();
	}

	@Override
	public ICoreService<CheckResult> getService() {
		return AppContext.getBean("checkResultService", ICheckResultService.class);
	}

	public IStudentService getStudentService() {
		return AppContext.getBean("studentService", IStudentService.class);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jToolBar1 = new javax.swing.JToolBar();
		btnSave = new javax.swing.JButton();
		btnPre = new javax.swing.JButton();
		btnNext = new javax.swing.JButton();
		jSplitPane5 = new javax.swing.JSplitPane();
		jSplitPane2 = new javax.swing.JSplitPane();
		jSplitPane1 = new javax.swing.JSplitPane();
		jPanel2 = new javax.swing.JPanel();
		jSplitPane3 = new javax.swing.JSplitPane();
		jSplitPane4 = new javax.swing.JSplitPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		tblVaccin1 = new com.vastcm.swing.jtable.MyTable();
		jScrollPane3 = new javax.swing.JScrollPane();
		tblVaccin2 = new com.vastcm.swing.jtable.MyTable();
		jPanel3 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();
		lblYouth = new javax.swing.JLabel();
		txtQcqfy = new javax.swing.JTextField();
		jLabel9 = new javax.swing.JLabel();
		jSplitPane6 = new javax.swing.JSplitPane();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane4 = new javax.swing.JScrollPane();
		tblCheckItem1 = new com.vastcm.swing.jtable.MyTable();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();
		lblBh = new javax.swing.JLabel();
		txtBh = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		cbSex = new javax.swing.JComboBox();
		jLabel3 = new javax.swing.JLabel();
		lblXh = new javax.swing.JLabel();
		txtXh = new javax.swing.JTextField();
		lblFolk = new javax.swing.JLabel();
		cbFolk = new javax.swing.JComboBox();
		jLabel5 = new javax.swing.JLabel();
		cbSource = new javax.swing.JComboBox();
		jLabel6 = new javax.swing.JLabel();
		txtAge = new javax.swing.JTextField();
		txtAge.setEditable(false);
		pkBirthday = new MyDateChooser();
		lblSFZ = new javax.swing.JLabel();
		txtSFZ = new javax.swing.JTextField();
		lblStudentCode = new javax.swing.JLabel();
		txtStudentCode = new javax.swing.JTextField();
		jPanel5 = new javax.swing.JPanel();
		jLabel10 = new javax.swing.JLabel();
		jLabel10.setBounds(6, 6, 90, 25);
		txtComment = new javax.swing.JTextField();
		txtComment.setBounds(95, 5, 510, 25);

		setPreferredSize(new Dimension(980, 620));
		setLayout(new java.awt.BorderLayout());

		jToolBar1.setFloatable(false);
		jToolBar1.setRollover(true);

		btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/system_save.gif"))); // NOI18N
		btnSave.setText("保    存");
		btnSave.setFocusable(false);
		btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSaveActionPerformed(evt);
			}
		});
		jToolBar1.add(btnSave);

		btnPre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/last.png"))); // NOI18N
		btnPre.setText("上一个");
		btnPre.setFocusable(false);
		btnPre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnPre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnPre.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPreActionPerformed(evt);
			}
		});
		jToolBar1.add(btnPre);

		btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/next.png"))); // NOI18N
		btnNext.setText("下一个");
		btnNext.setFocusable(false);
		btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNextActionPerformed(evt);
			}
		});
		jToolBar1.add(btnNext);

		add(jToolBar1, java.awt.BorderLayout.PAGE_START);

		jSplitPane5.setDividerLocation(540);
		jSplitPane5.setDividerSize(1);
		jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane5.setPreferredSize(new java.awt.Dimension(950, 680));

		jSplitPane2.setDividerLocation(80);
		jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane2.setToolTipText("");
		jSplitPane2.setPreferredSize(new java.awt.Dimension(946, 680));

		jSplitPane1.setDividerLocation(150);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jPanel2.setLayout(new java.awt.BorderLayout());

		jSplitPane3.setDividerLocation(700);
		jSplitPane3.setDividerSize(3);

		jSplitPane4.setDividerLocation(400);
		jSplitPane4.setDividerSize(3);

		tblVaccin1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "项目名称", "结果", "建议", "ItemFieldName" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class };

			boolean[] isCellEditable = new boolean[] { false, false, true, true, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return isCellEditable[column];
			}

		});
		jScrollPane2.setViewportView(tblVaccin1);

		jSplitPane4.setLeftComponent(jScrollPane2);

		tblVaccin2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "项目名称", "结果", "ItemFieldName" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

			boolean[] isCellEditable = new boolean[] { false, false, true, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return isCellEditable[column];
			}

		});
		jScrollPane3.setViewportView(tblVaccin2);

		jSplitPane4.setRightComponent(jScrollPane3);

		jSplitPane3.setLeftComponent(jSplitPane4);

		jPanel3.setLayout(null);

		jLabel7.setText("青春发育期");
		jPanel3.add(jLabel7);
		jLabel7.setBounds(10, 10, 80, 25);

		lblYouth.setText("男生：若出现遗精，首次遗精年龄");
		jPanel3.add(lblYouth);
		lblYouth.setBounds(10, 40, 210, 25);
		jPanel3.add(txtQcqfy);
		txtQcqfy.setBounds(210, 40, 30, 25);

		jLabel9.setText("岁");
		jPanel3.add(jLabel9);
		jLabel9.setBounds(240, 40, 13, 25);

		jSplitPane3.setRightComponent(jPanel3);

		jPanel2.add(jSplitPane3, java.awt.BorderLayout.CENTER);

		jSplitPane1.setTopComponent(jPanel2);

		jSplitPane6.setDividerLocation(700);
		jSplitPane6.setPreferredSize(new java.awt.Dimension(938, 300));
		jSplitPane6.setRightComponent(jPanel4);

		tblCheckItem1
				.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "项目名称", "单位", "结果", "ItemFieldName" }) {
					Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
							java.lang.String.class };

					boolean[] isCellEditable = new boolean[] { false, false, false, true, false };

					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}

					@Override
					public boolean isCellEditable(int row, int column) {
						return isCellEditable[column];
					}
				});
		jScrollPane4.setViewportView(tblCheckItem1);

		jSplitPane6.setLeftComponent(jScrollPane4);

		jSplitPane1.setRightComponent(jSplitPane6);

		jSplitPane2.setBottomComponent(jSplitPane1);

		jPanel1.setLayout(null);

		jLabel1.setText("姓名");
		jPanel1.add(jLabel1);
		jLabel1.setBounds(190, 10, 80, 25);
		jPanel1.add(txtName);
		txtName.setBounds(230, 10, 120, 25);

		lblBh.setText("编号");
		jPanel1.add(lblBh);
		lblBh.setBounds(360, 65, 80, 25);

		txtBh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtBhActionPerformed(evt);
			}
		});
		jPanel1.add(txtBh);
		txtBh.setBounds(400, 64, 120, 25);

		jLabel2.setText("性别*");
		jPanel1.add(jLabel2);
		jLabel2.setBounds(360, 10, 80, 25);

		cbSex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "男", "女" }));
		cbSex.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cbSexItemStateChanged(evt);
			}
		});
		jPanel1.add(cbSex);
		cbSex.setBounds(400, 10, 120, 25);

		jLabel3.setText("出生日期*");
		jPanel1.add(jLabel3);
		jLabel3.setBounds(530, 10, 80, 25);

		lblXh.setText("学号");
		jPanel1.add(lblXh);
		lblXh.setBounds(10, 53, 80, 25);

		txtXh.setEditable(false);
		txtXh.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtXhFocusLost(evt);
			}
		});
		jPanel1.add(txtXh);
		txtXh.setBounds(60, 52, 120, 25);

		lblFolk.setText("民族");
		jPanel1.add(lblFolk);
		lblFolk.setBounds(190, 40, 80, 25);

		jPanel1.add(cbFolk);
		cbFolk.setBounds(230, 40, 120, 25);

		jLabel5.setText("生源");
		jPanel1.add(jLabel5);
		jLabel5.setBounds(360, 40, 80, 25);

		jPanel1.add(cbSource);
		cbSource.setBounds(400, 40, 120, 25);

		jLabel6.setText("年龄");
		jPanel1.add(jLabel6);
		jLabel6.setBounds(530, 40, 80, 25);
		jPanel1.add(txtAge);
		txtAge.setBounds(590, 40, 120, 25);
		jPanel1.add(pkBirthday);
		pkBirthday.setBounds(590, 10, 120, 25);

		lblSFZ.setText("身份证");
		jPanel1.add(lblSFZ);
		lblSFZ.setBounds(532, 65, 80, 25);

		txtSFZ.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtSFZFocusLost(evt);
			}
		});
		jPanel1.add(txtSFZ);
		txtSFZ.setBounds(600, 64, 170, 25);

		lblStudentCode.setText("学生代码");
		jPanel1.add(lblStudentCode);
		lblStudentCode.setBounds(720, 40, 80, 25);

		txtStudentCode.setEditable(false);
		jPanel1.add(txtStudentCode);
		txtStudentCode.setBounds(780, 40, 170, 25);

		jSplitPane2.setTopComponent(jPanel1);

		JLabel lbljjad = new JLabel("体检日期");
		lbljjad.setBounds(10, 40, 80, 25);
		jPanel1.add(lbljjad);

		pkCheckDate = new MyDateChooser();
		pkCheckDate.setBounds(60, 39, 120, 25);
		jPanel1.add(pkCheckDate);

		JLabel label = new JLabel("学籍号");
		label.setBounds(10, 10, 80, 24);
		jPanel1.add(label);

		txtXjh = new JTextField();
		txtXjh.setBounds(60, 10, 120, 24);
		jPanel1.add(txtXjh);
		txtXjh.setColumns(10);

		JLabel lblEnterDate = new JLabel("入学日期*");
		lblEnterDate.setBounds(722, 14, 61, 16);
		jPanel1.add(lblEnterDate);

		pkEnterDate = new MyDateChooser();
		pkEnterDate.setBounds(780, 10, 170, 25);
		jPanel1.add(pkEnterDate);

		jSplitPane5.setTopComponent(jSplitPane2);

		jPanel5.setPreferredSize(new java.awt.Dimension(120, 30));
		jPanel5.setLayout(null);

		jLabel10.setText("医务人员评语");
		jPanel5.add(jLabel10);
		jPanel5.add(txtComment);

		jSplitPane5.setBottomComponent(jPanel5);

		add(jSplitPane5, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	@Override
	public void storeData() throws Exception {
		storeStudentData();

		// store student info
		int year = GlobalVariables.getGlobalVariables().getYear();
		//        int term = GlobalVariables.getGlobalVariables().getTerm();
		int term = 1;
		if (data.getTjnd() == null) {
			data.setTjnd(String.valueOf(year));
		}
		if (data.getTerm() == null) {
			data.setTerm(String.valueOf(term));
		}
		data.setXm(txtName.getText());
		if (pkCheckDate.getDate() != null) {
			data.setTjrq(new Date(pkCheckDate.getDate().getTime()));
		}
		data.setStudentCode(txtStudentCode.getText());
		String classLongNo = schClass.getLongNumber();
		data.setClassLongNo(classLongNo);
		data.setClassBh(schClass.getCode());
		data.setClassName(schClass.getName());

		GradeMessage gradeMsg = GradeMessage.getGradeMessageByClassCode(schClass.getCode());
		data.setGradeBh(gradeMsg.getGradeCode());
		data.setGradeName(gradeMsg.getGradeName());
		logger.info("gradeBh: " + gradeMsg.getGradeCode());
		SchoolMessage schMsg = SchoolMessage.getSchoolMessageByClassLongNumber(classLongNo);
		data.setSchoolBh(schMsg.getSchoolCode());
		data.setSchoolName(schMsg.getSchoolName());
		logger.info("school_name........" + schMsg.getSchoolName());

		data.setBh(txtBh.getText());
		data.setXjh(txtXjh.getText());

		data.setXb((String) cbSex.getSelectedItem());
		if (pkBirthday.getDate() != null) {
			data.setCsrq(new Date(pkBirthday.getDate().getTime()));
		}
		if (pkEnterDate.getDate() != null) {
			data.setRxsj(new Date(pkEnterDate.getDate().getTime()));
		}
		data.setSfzh(txtSFZ.getText());
		data.setXh(txtStudentCode.getText()); // 20130729 xh存studentCode
		if (cbFolk.getSelectedItem() != null) {
			data.setMz(cbFolk.getSelectedItem().toString());
		}
		if (cbSource.getSelectedItem() != null) {
			data.setSy(cbSource.getSelectedItem().toString());
		}
		String qcqfy = txtQcqfy.getText();
		if (qcqfy != null && !qcqfy.isEmpty()) {
			data.setQcqfy(Integer.valueOf(qcqfy));
		} else {
			data.setQcqfy(null);
		}
		//        else {
		//        	data.setQcqfy(0);
		//        }
		// 医务人员评语
		data.setXmpy(txtComment.getText());
	}

	protected void storeStudentData() throws ParseException {
		//    	student.setXjh(txtBh.getText());
		String studentCode = txtStudentCode.getText();
		if (studentCode == null || studentCode.isEmpty()) {
			SchoolTreeNode schoolTreeNode = (SchoolTreeNode) getUIContext().get("classNode");
			String schoolCode = schoolTreeNode.getParentCode();
			String classNo = schoolTreeNode.getCode();
			studentCode = StudentCode.getNewCode(schoolCode, classNo, student.getEnterDate());
			String waterCode = StudentCode.getWaterCode(studentCode);
			txtStudentCode.setText(studentCode);
			student.setStudentCode(studentCode);
			student.setStudentNo(waterCode);
		}
		student.setStudentCode(txtStudentCode.getText());
		student.setName(txtName.getText());
		student.setXjh(txtXjh.getText());
		student.setSex((String) cbSex.getSelectedItem());
		student.setClassNo(schClass.getCode());
		student.setClassLongNumber(schClass.getLongNumber());
		student.setClassName(schClass.getName());
		logger.info("classCode=" + schClass.getCode() + "; classLongNo=" + schClass.getLongNumber());
		student.setGradeNo(GradeMessage.getGradeMessageByClassCode(schClass.getCode()).getGradeCode());
		student.setSchoolNo(SchoolMessage.getSchoolMessageByClassLongNumber(schClass.getLongNumber()).getSchoolCode());
		student.setStatus("T");
		if (pkBirthday.getDate() != null) {
			Date birthday = new Date(pkBirthday.getDate().getTime());
			student.setBornDate(birthday);
			data.setCsrq(birthday);
		}
		// 入学时间
		if (pkEnterDate.getDate() != null) {
			Date enterDate = new Date(pkEnterDate.getDate().getTime());
			student.setEnterDate(enterDate);
			data.setRxsj(enterDate);
		}
		student.setSfz(txtSFZ.getText());
		if (cbFolk.getSelectedItem() != null) {
			student.setNation(cbFolk.getSelectedItem().toString());
		}
		if (cbSource.getSelectedItem() != null) {
			student.setSy(cbSource.getSelectedItem().toString());
		}
	}

	public void initNewStudent() {
		student = new Student();
		//        student.setStudentCode(StudentCode.getNewCode(schClass.getParentCode()));
		student.setClassLongNumber(schClass.getLongNumber());
	}

	protected void clearUIValues() {
		txtBh.setText(null);
		txtName.setText(null);
		pkBirthday.setDate(null);
		pkEnterDate.setDate(null);
		txtSFZ.setText(null);
		pkCheckDate.setDate(null);
		txtXh.setText(null);
		txtAge.setText(null);
		txtStudentCode.setText(null);
		txtQcqfy.setText(null);

		int countTblVaccin1 = tblVaccin1.getRowCount();
		for (int i = 0; i < countTblVaccin1; i++) {
			tblVaccin1.setValueAt(null, i, colIndex_vaccin1Result);
			tblVaccin1.setValueAt(null, i, colIndex_vaccin1Advise);
		}

		int countTblVaccin2 = tblVaccin2.getRowCount();
		for (int i = 0; i < countTblVaccin2; i++) {
			tblVaccin2.setValueAt(null, i, colIndex_vaccin2Result);
		}

		int countTblCheckItem = tblCheckItem1.getRowCount();
		for (int i = 0; i < countTblCheckItem; i++) {
			tblCheckItem1.setValueAt(null, i, colIndex_checkItemResult);
		}

		txtComment.setText(null);
	}

	protected void loadDefaultValues() {
		pkCheckDate.setDate(new java.util.Date());
		if (student == null || student.getStudentCode() == null) {
			cbSex.setSelectedIndex(0);
		}

	}

	@Override
	public void loadData() throws Exception {
		clearUIValues();
		List<CheckResult> results = null;
		if (student.getStudentCode() != null) {
			results = getCheckResultService().getByStudentCode(student.getStudentCode(), schClass.getLongNumber());
		}
		UIStatusEnum uiStatus = UIStatusEnum.NEW;
		if (results == null || results.size() == 0) {
			uiStatus = UIStatusEnum.NEW;
		} else {
			uiStatus = UIStatusEnum.EDIT;
		}

		if (UIStatusEnum.NEW.equals(uiStatus)) {
			data = new CheckResult();
			loadStudentData();
			loadDefaultValues();
			return;
		}

		data = results.get(0);

		//        if(student == null) {
		//            initNewStudent();
		//            loadStudentData();
		//            return;
		//        }

		loadStudentData();
		if (student.getStudentCode() == null) {
			return;
		}
		List<String> itemFieldLs = new ArrayList<String>();
		itemFieldLs.addAll(checkItemFieldLs);
		itemFieldLs.addAll(vaccinFieldLs);
		itemFieldLs.addAll(fixItemFields);

		pkCheckDate.setDate(data.getTjrq());
		txtBh.setText(data.getBh());
		GlobalVariables gv = GlobalVariables.getGlobalVariables();
		int year = gv.getYear();
		//        int term = gv.getTerm();
		int term = 1;
		Map<String, Object> itemValueMap = getCheckResultService().getItemValueMap(student.getStudentCode(), itemFieldLs, data.getSchoolBh(),
				String.valueOf(year), String.valueOf(term));
		if (itemValueMap.size() > 0) {
			int rowCountCheckItem = tblCheckItem1Model.getRowCount();
			for (int i = 0; i < rowCountCheckItem; i++) {
				String fieldname = (String) tblCheckItem1Model.getValueAt(i, colIndex_checkItemFieldname);
				Object value = itemValueMap.get(fieldname);
				if (value != null) {
					CheckResultItemMapping mapping = checkResultItemMapping.getMappingByCode(fieldname, value.toString());
					if (mapping != null) {
						value = mapping.getAlias();
					}
				}
				tblCheckItem1Model.setValueAt(value, i, colIndex_checkItemResult);
			}

			int rowCountVaccin1Item = tblVaccin1Model.getRowCount();
			for (int i = 0; i < rowCountVaccin1Item; i++) {
				String fieldname = (String) tblVaccin1Model.getValueAt(i, colIndex_vaccin1Fieldname);
				Object value = itemValueMap.get(fieldname);
				if (value != null) {
					VaccinItemMapping mapping = vaccinItemMapping.getMappingByCode(fieldname, value.toString());
					if (mapping != null) {
						value = mapping.getAlias();
					}
				}
				tblVaccin1Model.setValueAt(value, i, colIndex_vaccin1Result);
			}

			int rowCountFixItem = tblVaccin2.getRowCount();
			for (int i = 0; i < rowCountFixItem; i++) {
				String fieldname = (String) tblVaccin2Model.getValueAt(i, colIndex_vaccin2Fieldname);
				tblVaccin2Model.setValueAt(itemValueMap.get(fieldname), i, colIndex_vaccin2Result);
			}
		}

		// store check result

		//        tblVaccin1.getRow(0).setValue("结果", data.getBs());
		//        tblVaccin1.getRow(1).setValue("结果", data.getBh());
		Integer qcqfy = data.getQcqfy();
		if (qcqfy != null) {
			txtQcqfy.setText(String.valueOf(data.getQcqfy()));
		}
		txtComment.setText(data.getXmpy());
		cbSexItemStateChanged(null);
	}

	public void loadStudentData() {
		txtStudentCode.setText(student.getStudentCode());
		txtName.setText(student.getName());
		txtXjh.setText(student.getXjh());
		cbSex.setSelectedItem(student.getSex());
		pkBirthday.setDate(student.getBornDate());
		txtSFZ.setText(student.getSfz());
		txtXh.setText(student.getXh());
		cbFolk.setSelectedItem(student.getNation());
		cbSource.setSelectedItem(student.getSy());
		Date bornDate = student.getBornDate();
		if (bornDate != null) {
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(Calendar.YEAR);
			cal.setTime(student.getBornDate());
			int bornYear = cal.get(Calendar.YEAR);
			int age = currentYear - bornYear;
			txtAge.setText(String.valueOf(age));
		}
		pkEnterDate.setDate(student.getEnterDate());

	}

	public Map<String, Object> getItemValueMap() {

		Map<String, Object> itemValueMap = new HashMap<String, Object>();
		DefaultTableModel tblCheckItemModel = (DefaultTableModel) tblCheckItem1.getModel();
		int checkCount = tblCheckItemModel.getRowCount();
		for (int i = 0; i < checkCount; i++) {
			String key = (String) tblCheckItemModel.getValueAt(i, colIndex_checkItemFieldname);
			Object value = tblCheckItemModel.getValueAt(i, colIndex_checkItemResult);
			if (value == null) {
				continue;
			}
			try {
				value = new BigDecimal(value.toString());
			} catch (NumberFormatException e) {
				CheckResultItemMapping mapping = checkResultItemMapping.getMappingByAlias(key, value.toString());
				if (mapping != null) {
					value = mapping.getCode();
				} else {
					value = null; //new BigDecimal("0");
				}
			}
			itemValueMap.put(key, value);
			if (customFields.contains(key)) {
				itemValueMap.put(key + "Name", tblCheckItemModel.getValueAt(i, colIndex_checkItemAlias));
			}
		}

		DefaultTableModel tblVaccinModel = (DefaultTableModel) tblVaccin1.getModel();
		int vaccinCount = tblVaccinModel.getRowCount();
		for (int i = 0; i < vaccinCount; i++) {
			String key = (String) tblVaccinModel.getValueAt(i, colIndex_vaccin1Fieldname);
			Object value = tblVaccinModel.getValueAt(i, colIndex_vaccin1Result);
			if (value == null) {
				continue;
			}
			try {
				value = new BigDecimal(value.toString());
			} catch (NumberFormatException e) {
				VaccinItemMapping mapping = vaccinItemMapping.getMappingByAlias(key, value.toString());
				if (mapping != null) {
					value = mapping.getCode();
				}
			}
			itemValueMap.put(key, value);
			if (customFields.contains(key)) {
				itemValueMap.put(key + "Name", tblVaccin1.getValueAt(i, colIndex_vaccin1Alias));
			}
		}

		DefaultTableModel tblVaccin2Model = (DefaultTableModel) tblVaccin2.getModel();
		int vaccinCount2 = tblVaccin2Model.getRowCount();
		for (int i = 0; i < vaccinCount2; i++) {
			String key = (String) tblVaccin2Model.getValueAt(i, colIndex_vaccin2Fieldname);
			Object value = tblVaccin2Model.getValueAt(i, colIndex_vaccin2Result);
			if (value == null) {
				continue;
			}
			try {
				value = new BigDecimal(value.toString());
			} catch (NumberFormatException e) {
			}
			itemValueMap.put(key, value);
		}

		return itemValueMap;
	}

	@Override
	public void save() throws Exception {
		long d1 = System.currentTimeMillis();
		storeData();
		getStudentService().save(student);
		if (tblCheckItem1.getCellEditor() != null) {
			tblCheckItem1.getCellEditor().stopCellEditing();
		}
		if (tblVaccin1.getCellEditor() != null) {
			tblVaccin1.getCellEditor().stopCellEditing();
		}
		if (tblVaccin2.getCellEditor() != null) {
			tblVaccin2.getCellEditor().stopCellEditing();
		}

		List<String> stuCodeLs2Save = new ArrayList<String>();
		stuCodeLs2Save.add(student.getStudentCode());
		Map<String, Object> map = getItemValueMap();
		int year = GlobalVariables.getGlobalVariables().getYear();
		//        map.put("tjnd", data.getTjnd());
		//        map.put("term", data.getTerm());
		map.put("bh", data.getBh());
		map.put("xm", data.getXm());
		map.put("xjh", data.getXjh());
		if (data.getTjrq() != null) {
			map.put("tjrq", data.getTjrq());
		} else {
			map.put("tjrq", new Date(System.currentTimeMillis()));
		}
		map.put("studentCode", data.getStudentCode());
		map.put("classLongNo", data.getClassLongNo());
		map.put("classBh", data.getClassBh());
		map.put("CLASS_NAME", data.getClassName());
		map.put("gradeBh", data.getGradeBh());
		map.put("grade_Name", data.getGradeName());
		logger.info("grade_name............" + data.getGradeName());
		map.put("schoolBh", data.getSchoolBh());
		logger.info("school_name........" + data.getSchoolName());
		map.put("SCHOOL_NAME", data.getSchoolName());
		//        String schoolType = GradeMessage.getGradeMessageByClassCode(data.getClassBh()).getSchoolType();
		String schoolType = SchoolMessage.getSchoolByClassLongNumber(data.getClassLongNo()).getSchoolType();
		logger.info("schoolType: " + schoolType);
		map.put("schoolType", schoolType);
		map.put("xh", data.getXh());
		map.put("xb", data.getXb());
		if (data.getCsrq() != null) {
			map.put("csrq", data.getCsrq());
		}
		map.put("sfzh", data.getSfzh());
		map.put("mz", data.getMz());
		map.put("sy", data.getSy());
		map.put("qcqfy", data.getQcqfy());
		//        if(data.getQcqfy() != null) {
		//        	map.put("qcqfy", data.getQcqfy());
		//        }
		map.put("xmpy", data.getXmpy());
		calcMisc(map);
		logger.info("studentCode=" + stuCodeLs2Save.get(0) + ", schoolCode=" + data.getSchoolBh());
		getCheckResultService().update(stuCodeLs2Save, map, data.getSchoolBh(), data.getTjnd(), data.getTerm());
		long d2 = System.currentTimeMillis();
		logger.info("保存体检结果耗时：" + (d2 - d1) + "ms.");

		StringBuilder sql = new StringBuilder();
		sql.append(" WHERE studentCode=:studentCode ");
		sql.append(" AND   tjnd=:tjnd ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentCode", student.getStudentCode());
		params.put("tjnd", String.valueOf(year));
		getCheckResultService().updateEvaluation(sql.toString(), params);
		long d3 = System.currentTimeMillis();
		logger.info("计算体检结果标准耗时：" + (d3 - d2) + "ms.");

		//		StringBuilder sql1 = new StringBuilder();
		//		sql1.append(" WHERE result.studentCode=:studentCode ");
		//		sql1.append(" AND   result.tjnd=:tjnd ");
		//		Map<String, Object> params1 = new HashMap<String, Object>();
		//		params1.put("studentCode", student.getStudentCode());
		//		params1.put("tjnd", String.valueOf(year));
		//		getCheckResultService().recalculateStatRptByResult(sql1.toString(), params1, true);
		getCheckResultService().recalculateStatRptByResult4Delete(data.getSchoolBh(), year, false); // 重算统计报表
		long d4 = System.currentTimeMillis();
		logger.info("重算报表耗时：" + (d4 - d3) + "ms.");
	}

	// 计算需要后台自动计算的字段
	private void calcMisc(Map<String, Object> map) {
		// 龋失补牙数=乳龋患+乳龋失+乳龋补+恒龋患+恒龋失+恒龋补
		/*
		'HQB','恒龋补','HQB'
		'HQH','恒龋患','HQH'
		'HQS','恒龋失','HQS'
		'QSBNUM','龋失补牙数','QSBNUM'
		'RQB','乳龋补','RQB'
		'RQH','乳龋患','RQH'
		'RQS','乳龋失','RQS'
		*/
		Object hqbObj = map.get("HQB");
		BigDecimal hqb = (hqbObj == null || !(hqbObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) hqbObj;
		Object hqhObj = map.get("HQH");
		BigDecimal hqh = (hqhObj == null || !(hqhObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) hqhObj;
		Object hqsObj = map.get("HQS");
		BigDecimal hqs = (hqsObj == null || !(hqsObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) hqsObj;
		Object rqbObj = map.get("RQB");
		BigDecimal rqb = (rqbObj == null || !(rqbObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) rqbObj;
		Object rqhObj = map.get("RQH");
		BigDecimal rqh = (rqhObj == null || !(rqhObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) rqhObj;
		Object rqsObj = map.get("RQS");
		BigDecimal rqs = (rqsObj == null || !(rqsObj instanceof BigDecimal)) ? new BigDecimal("0") : (BigDecimal) rqsObj;

		BigDecimal qsbnum = hqb.add(hqh).add(hqs).add(rqb).add(rqh).add(rqs);
		if (hqbObj == null && hqhObj == null && hqsObj == null && rqbObj == null && rqhObj == null && rqsObj == null) {
			qsbnum = null;
		}
		map.put("QSBNUM", qsbnum);
	}

	public void checkInputValidation() {
		if (cbSex.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "请选择 性别！");
			SystemUtils.abort();
		}
		if (pkBirthday.getDate() == null) {
			JOptionPane.showMessageDialog(this, "请填写 出生日期！");
			SystemUtils.abort();
		}
		if (pkEnterDate.getDate() == null) {
			JOptionPane.showMessageDialog(this, "请填写 入学日期！");
			SystemUtils.abort();
		}
	}

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		checkInputValidation();
		try {
			String bh = txtBh.getText();
			if (bh == null || bh.isEmpty()) {
				SchoolMessage schMsg = SchoolMessage.getSchoolMessageByClassLongNumber(schClass.getLongNumber());
				txtBh.setText(schMsg.getSchoolCode() + txtStudentCode.getText());
			}
			save();
			JOptionPane.showMessageDialog(this, "保存成功！");
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, "保存失败！" + "[" + ex.getMessage() + "]");
		}
	}//GEN-LAST:event_btnSaveActionPerformed

	private void txtSFZFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSFZFocusLost
		// 查找是否存在相同身份证的学生和体检结果（同学期），有则（部分）带出
		List<CheckResult> ls = ((ICheckResultService) getService()).getByName(txtSFZ.getText(), schClass.getLongNumber());
		if (ls != null && ls.size() > 0) {
			try {
				data = ls.get(0);
				loadData();
			} catch (Exception ex) {
				ExceptionUtils.writeExceptionLog(logger, ex);
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
	}//GEN-LAST:event_txtSFZFocusLost

	private void txtXhFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtXhFocusLost
		// 查找是否存在相同学号的学生和体检结果（同学期），有则（部分）带出
		List<CheckResult> ls = ((ICheckResultService) getService()).getByXh(txtXh.getText(), schClass.getLongNumber());
		if (ls != null && ls.size() > 0) {
			try {
				data = ls.get(0);
				loadData();
			} catch (Exception ex) {
				ExceptionUtils.writeExceptionLog(logger, ex);
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
	}//GEN-LAST:event_txtXhFocusLost

	private void txtBhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBhActionPerformed
	}//GEN-LAST:event_txtBhActionPerformed

	private void cbSexItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSexItemStateChanged
		if ("男".equals(cbSex.getSelectedItem())) {
			lblYouth.setText("男生：若出现遗精，首次遗精年龄");
		}
		if ("女".equals(cbSex.getSelectedItem())) {
			lblYouth.setText("女生：若出现月经，初潮年龄");
		}
	}//GEN-LAST:event_cbSexItemStateChanged

	private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
		logger.info("next student.");
		if (iterStuCode.hasNext()) {
			try {
				String stuCode = iterStuCode.next();
				if (student.getStudentCode().equals(stuCode)) {
					if (iterStuCode.hasNext()) {
						stuCode = iterStuCode.next();
					} else {
						JOptionPane.showMessageDialog(this, "已经是本班最后一个学生了！");
						return;
					}
				}

				logger.info("current stu code: " + stuCode);
				student = getStudentService().getStudentByCode(stuCode, schClass.getLongNumber());
				loadData();
			} catch (Exception ex) {
				ExceptionUtils.writeExceptionLog(logger, ex);
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		} else {
			JOptionPane.showMessageDialog(this, "已经是本班最后一个学生了！");
		}
	}//GEN-LAST:event_btnNextActionPerformed

	private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
		logger.info("pre student.");
		if (iterStuCode.hasPrevious()) {
			try {
				String stuCode = iterStuCode.previous();
				if (student.getStudentCode().equals(stuCode)) {
					if (iterStuCode.hasPrevious()) {
						stuCode = iterStuCode.previous();
					} else {
						JOptionPane.showMessageDialog(this, "已经是本班第一个学生了！");
						return;
					}
				}

				logger.info("current stu code: " + stuCode);
				student = getStudentService().getStudentByCode(stuCode, schClass.getLongNumber());
				loadData();
			} catch (Exception ex) {
				ExceptionUtils.writeExceptionLog(logger, ex);
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		} else {
			JOptionPane.showMessageDialog(this, "已经是本班第一个学生了！");
		}
	}//GEN-LAST:event_btnPreActionPerformed

	@Override
	public void uiClosing() {
		CheckResultListPanel listPanel = (CheckResultListPanel) getUIContext().get(UIContext.UI_OWNER);
		listPanel.refreshList();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnNext;
	private javax.swing.JButton btnPre;
	private javax.swing.JButton btnSave;
	private javax.swing.JComboBox cbFolk;
	private javax.swing.JComboBox cbSex;
	private javax.swing.JComboBox cbSource;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel lblSFZ;
	private javax.swing.JLabel lblStudentCode;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel lblXh;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JSplitPane jSplitPane3;
	private javax.swing.JSplitPane jSplitPane4;
	private javax.swing.JSplitPane jSplitPane5;
	private javax.swing.JSplitPane jSplitPane6;
	private javax.swing.JTextField txtQcqfy;
	private javax.swing.JTextField txtComment;
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JLabel lblFolk;
	private javax.swing.JLabel lblBh;
	private javax.swing.JLabel lblYouth;
	private com.vastcm.swing.jtable.MyTable tblCheckItem1;
	private com.vastcm.swing.jtable.MyTable tblVaccin1;
	private com.vastcm.swing.jtable.MyTable tblVaccin2;
	private javax.swing.JTextField txtAge;
	private javax.swing.JTextField txtBh;
	private MyDateChooser pkBirthday;
	private javax.swing.JTextField txtName;
	private javax.swing.JTextField txtSFZ;
	private javax.swing.JTextField txtStudentCode;
	private javax.swing.JTextField txtXh;
	private MyDateChooser pkCheckDate;
	private MyDateChooser pkEnterDate;
	private JTextField txtXjh;
}
