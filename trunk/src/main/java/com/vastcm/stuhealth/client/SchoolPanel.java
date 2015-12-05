/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.entity.Grade;
import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.IGradeService;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;
import com.vastcm.stuhealth.client.entity.service.ISchoolTreeNodeService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import com.vastcm.stuhealth.client.framework.ui.EditUI;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.ui.TreeUtils;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;
import com.vastcm.swing.jtable.CellValueChangeListener;
import com.vastcm.swing.jtable.MyRow;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * 
 * @author House
 */
public class SchoolPanel extends EditUI<School> {

	private Logger logger = LoggerFactory.getLogger(SchoolPanel.class);
	private IGradeService gradeService;
	private ISchoolService schoolService;
	private IStudentService studentService;
	private ICheckResultService checkResultService;
	private ISchoolTreeNodeService schoolTreeNodeService;
	// key=gradeCode, value=map{key=classCode, value=classNode}
	private Map<String, TreeMap<String, SchoolTreeNode>> classMap = new HashMap<String, TreeMap<String, SchoolTreeNode>>();
	private String schoolCode;
	private String schoolLongNumber;
	private String lastSelectedGradeCode;

	// 年级编码规则：第1位代表学校类型（1:小学；2:初中；3:高中),第2位代表年级
	//    private String[][] gradesData = new String[][] {
	//        { "11",  "小学一年级" },
	//        { "12",  "小学二年级" },
	//        { "13",  "小学三年级" },
	//        { "14",  "小学四年级" },
	//        { "15",  "小学五年级" },
	//        { "16",  "小学六年级" },
	//        { "21",  "初中一年级" },
	//        { "22",  "初中二年级" },
	//        { "23",  "初中三年级" },
	//        { "24",  "初中四年级" },
	//        { "31",  "高中一年级" },
	//        { "32",  "高中二年级" },
	//        { "33",  "高中三年级" }
	//    };

	private List<Grade> grades;

	private Map<String, String> gradeDataMap = new HashMap<String, String>();

	public SchoolPanel() {
		initComponents();
	}

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		initComponentsEx();
	}

	public void initComponentsEx() {

		tblGrades.setCellSelectionEnabled(true);
		TableColumn colId = tblGrades.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		initTableGrade();
		initTblClass();
	}

	@Override
	public void onShow() {
		super.onShow();
		SwingUtilities.getWindowAncestor(this).setSize(560, 520);
	}

	@Override
	protected void initData() throws Exception {
		gradeService = AppContext.getBean("gradeService", IGradeService.class);
		grades = gradeService.getAll();
		for (Grade g : grades) {
			gradeDataMap.put(g.getGradeCode(), g.getName());
		}
		schoolCode = (String) getUIContext().get("schoolCode");
		schoolLongNumber = getSchoolTreeNodeService().getByCode(schoolCode).get(0).getLongNumber();
		data = ((ISchoolService) getService()).getByCode(schoolCode);
		super.initData();
	}

	public void initTableGrade() {
		TableColumn colCode = tblGrade.getTableHeader().getColumnModel().getColumn(0);
		colCode.setMinWidth(0);
		colCode.setMaxWidth(0);
		colCode.setPreferredWidth(0);
		colCode.setWidth(0);

		//    	DefaultTableModel tblGradeModel = (DefaultTableModel) tblGrade.getModel();
		//        
		//        for(Grade g : grades) {
		//        	tblGradeModel.addRow(new String[]{ g.getGradeCode(), g.getName() });
		//        }

		tblGrade.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				tblGrade_rowSelectChange(e);
			}
		});
	}

	public void refreshGrades(String schoolSystem, String schoolType) {
		DefaultTableModel tblGradeModel = (DefaultTableModel) tblGrade.getModel();
		int rowCount = tblGradeModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			tblGradeModel.removeRow(0);
		}
		for (Grade g : grades) {
			int code = Integer.valueOf(g.getGradeCode());
			
			if ("十二年制".equals(schoolType)) {
				if (schoolSystem.startsWith("63")) {
					if ((code >= 11 && code <= 16) || (code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("54")) {
					if ((code >= 11 && code <= 15) || (code >= 21 && code <= 24)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("53")) {
					if ((code >= 11 && code <= 15) || (code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				
				if (code >= 31 && code <= 33) {
					tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
				}
			}

			if ("九年制".equals(schoolType)) {
				if (schoolSystem.startsWith("63")) {
					if ((code >= 11 && code <= 16) || (code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("54")) {
					if ((code >= 11 && code <= 15) || (code >= 21 && code <= 24)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("53")) {
					if ((code >= 11 && code <= 15) || (code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
			}

			if ("小学".equals(schoolType)) {
				if (schoolSystem.startsWith("6")) {
					if ((code >= 11 && code <= 16)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("5")) {
					if ((code >= 11 && code <= 15)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
			}

			if ("初中".equals(schoolType)) {
				if (schoolSystem.startsWith("63") || schoolSystem.startsWith("53")) {
					if ((code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("54")) {
					if ((code >= 21 && code <= 24)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
			}

			if ("高中".equals(schoolType)) {
				if ((code >= 31 && code <= 33)) {
					tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
				}
			}

			if ("完中".equals(schoolType)) {
				if (schoolSystem.startsWith("63") || schoolSystem.startsWith("53")) {
					if ((code >= 21 && code <= 23)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (schoolSystem.startsWith("54")) {
					if ((code >= 21 && code <= 24)) {
						tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
					}
				}
				if (code >= 31 && code <= 33) {
					tblGradeModel.addRow(new String[] { g.getGradeCode(), g.getName() });
				}
			}
		}
	}

	public void initTblClass() {
		TableColumn colCode = tblClass.getTableHeader().getColumnModel().getColumn(0);
		colCode.setMinWidth(0);
		colCode.setMaxWidth(0);
		colCode.setPreferredWidth(0);
		colCode.setWidth(0);

		//        TableColumn colClassLongName = tblClass.getTableHeader().getColumnModel().getColumn(0);
		//        colClassLongName.setMinWidth(0);
		//        colClassLongName.setMaxWidth(0);
		//        colClassLongName.setPreferredWidth(0);
		//        colClassLongName.setWidth(0);

		tblClass.addCellValueChangeListener(new CellValueChangeListener() {

			@Override
			public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
				tblClass_cellValueChange(oldValue, newValue, rowIndex, colIndex);
			}
		});
	}

	public void tblClass_cellValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
		if (colIndex == 1) {
			tblClass.setValueAt(gradeDataMap.get(getSelectedGradeCode()) + newValue, rowIndex, 2);
			//    		storeClassesUpdates(getSelectedGradeCode());
		}
	}

	public void tblGrade_rowSelectChange(ListSelectionEvent e) {
		logger.info("tblGrade_rowSelectChange...");
		if (lastSelectedGradeCode != null) {

			storeClassesUpdates(lastSelectedGradeCode);
		}
		refreshClasses();
		lastSelectedGradeCode = getSelectedGradeCode();
	}

	public void storeClassesUpdates(String gradeCode) {
		if (tblClass.isEditing()) {
			tblClass.getCellEditor().stopCellEditing();
		}
		TreeMap<String, SchoolTreeNode> classes = classMap.get(gradeCode);
		int rowCount = tblClass.getRowCount();
		DefaultTableModel classModel = (DefaultTableModel) tblClass.getModel();
		logger.info("rowCount...:" + rowCount);
		Map<String, String> changedClassNames = new HashMap<String, String>(); // key=classLongNumber, value=className
		for (int i = 0; i < rowCount; i++) {
			String code = (String) classModel.getValueAt(i, 0);
			String name = (String) classModel.getValueAt(i, 2);
			if (classes == null) {
				classes = new TreeMap<String, SchoolTreeNode>();
				classMap.put(gradeCode, classes);
			}
			SchoolTreeNode node = classes.get(code);
			if (node != null) {
				logger.info("class name=" + name);
				node.setName(name);
				changedClassNames.put(getSelectGradeLongNumber() + code.substring(2), name);
			} else { // add new class
				SchoolTreeNode newNode = new SchoolTreeNode();
				newNode.setCode(code);
				newNode.setName(name);
				newNode.setParentCode(schoolCode);
				newNode.setStatus(SchoolTreeNode.STATUS_NORMAL);
				newNode.setType(SchoolTreeNode.TYPE_CLASS);
				newNode.setLongNumber(getSelectGradeLongNumber() + code.substring(2));
				classes.put(code, newNode);
			}
		}
		if (changedClassNames.size() > 0) {
			updateClassNameForStudent(changedClassNames);
		}
	}

	protected void updateClassNameForStudent(Map<String, String> changedClassNames) {
		IStudentService studentSrv = getStudentService();
		studentSrv.updateClassName(changedClassNames);
	}

	public void refreshClasses() {
		TreeMap<String, SchoolTreeNode> classes = classMap.get(getSelectedGradeCode());
		((DefaultTableModel) tblClass.getModel()).getDataVector().clear();
		((DefaultTableModel) tblClass.getModel()).fireTableDataChanged();
		if (classes != null) {
			for (SchoolTreeNode n : classes.values()) {
				MyRow row = tblClass.addRow();
				row.setValue("code", n.getCode());
				row.setValue("班级全名", n.getName());
				row.setValue("班级名称", n.getName().substring(5));
			}
		}
	}

	public String getSelectedGradeCode() {
		int rowIndex = tblGrade.getSelectionModel().getMinSelectionIndex();
		if (rowIndex == -1)
			return null;
		return (String) tblGrade.getValueAt(rowIndex, 0);
	}

	public String getSelectGradeLongNumber() {
		return schoolLongNumber + "!" + getSelectedGradeCode();
	}

	@Override
	public void loadData() throws Exception {
		txtCode.setText(data.getSchoolCode());
		txtName.setText(data.getName());
		txtAddr.setText(data.getAddress());
		txtOperator.setText(data.getLinkMan());
		txtTel.setText(data.getOfficePhone());
		txtMobile.setText(data.getMobilePhone());
		txtSchoolType.setText(data.getSchoolType());
		if (data.getSchoolSystem() == null) {
			cbSchoolSystem.setSelectedIndex(0);
		} else {
			cbSchoolSystem.setSelectedItem(data.getSchoolSystem());
		}
		List<SchoolTreeNode> classNodes = schoolTreeNodeService.getClassNodesBySchool(schoolCode);
		for (SchoolTreeNode n : classNodes) {
			String gradeCode = n.getCode().substring(0, 2);
			TreeMap<String, SchoolTreeNode> map = classMap.get(gradeCode);
			if (map == null) {
				map = new TreeMap<String, SchoolTreeNode>();
				map.put(n.getCode(), n);
				classMap.put(gradeCode, map);
			} else {
				map.put(n.getCode(), n);
			}
		}

		refreshGrades(cbSchoolSystem.getSelectedItem().toString(), txtSchoolType.getText());
	}

	@Override
	public void storeData() throws Exception {
		data.setAddress(txtAddr.getText());
		data.setLinkMan(txtOperator.getText());
		data.setMobilePhone(txtMobile.getText());
		data.setOfficePhone(txtTel.getText());
		data.setSchoolSystem(cbSchoolSystem.getSelectedItem().toString());
	}

	public void btnAddClass_actionPerformed(ActionEvent e) {
		String gradeCode = getSelectedGradeCode();
		if (gradeCode == null) {
			JOptionPane.showMessageDialog(this, "请先选择年级！");
			return;
		}
		int newClassCode = classMap.get(gradeCode) == null || classMap.get(gradeCode).size() == 0 ? 1 : Integer.parseInt(classMap.get(gradeCode)
				.lastKey().substring(2)) + 1;
		String classCode = newClassCode < 10 ? "0" + newClassCode : "" + newClassCode;
		String className = "(" + (classCode) + ")班";
		String classLongName = gradeDataMap.get(getSelectedGradeCode()) + className;
		classCode = gradeCode + classCode;
		MyRow row = tblClass.addRow();
		row.setValue("code", classCode);
		row.setValue("班级名称", className);
		row.setValue("班级全名", classLongName);
		storeClassesUpdates(gradeCode);
	}

	private ISchoolTreeNodeService getSchoolTreeNodeService() {
		if (schoolTreeNodeService == null) {
			schoolTreeNodeService = AppContext.getBean("schoolTreeNodeService", ISchoolTreeNodeService.class);
		}
		return schoolTreeNodeService;
	}

	private IStudentService getStudentService() {
		if (studentService == null) {
			studentService = AppContext.getBean("studentService", IStudentService.class);
		}
		return studentService;
	}

	private ICheckResultService getCheckResultService() {
		if (checkResultService == null) {
			checkResultService = AppContext.getBean("checkResultService", ICheckResultService.class);
		}
		return checkResultService;
	}

	public void btnRemoveClass_actionPerformed(ActionEvent e) {
		int selectedRowIndex = tblClass.getSelectedRow();
		int lastRowIndex = tblClass.getRowCount() - 1;
		String className2Remove = (String) tblClass.getValueAt(selectedRowIndex, 2);
		String classCode2Remove = (String) tblClass.getValueAt(selectedRowIndex, 0);
		String msg = "确认删除";
		if (lastRowIndex != selectedRowIndex) {
			msg = "您所选的班级不是本年级最后一个班，系统只会删除该班级对应的学生、体检结果以及报表数据，确定要删除吗？";
		}
		int confirm = JOptionPane.showConfirmDialog(this, className2Remove + "," + msg, className2Remove, JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// TODO 检查学生、体检结果是否用引用到要删除的班级
			String classLongNumber = schoolLongNumber + "!" + classCode2Remove;
			getCheckResultService().removeByClassLongNumber(classLongNumber);
			getStudentService().removeByClassLongNumber(classLongNumber);
			// 重算统计报表
			int year = GlobalVariables.getGlobalVariables().getYear();
			//    		StringBuilder sql1 = new StringBuilder();
			//    		sql1.append(" WHERE 1=1 ");
			//    		sql1.append("   AND result.schoolBh = :schoolBh ");
			//    		sql1.append("   AND result.tjnd=:tjnd ");
			String schoolBh = SchoolMessage.getSchoolByClassLongNumber(classLongNumber).getSchoolCode();
			//    		Map<String, Object> params1 = new HashMap<String, Object>();
			//    		params1.put("schoolBh", schoolBh);
			//    		params1.put("tjnd", String.valueOf(year));
			logger.info("param schoolBh=" + schoolBh + ", tjnd=" + year);
			//    		logger.info("recalculateStatRptByResult condition:\n" + sql1);
			//    		getCheckResultService().recalculateStatRptByResult(sql1.toString(), params1, true); // 重算统计报表
			getCheckResultService().recalculateStatRptByResult4Delete(schoolBh, year, false);
			// 只有所选班级是本年级最后一个班时，才将班级本身删除。否则，只删除班级里的学生、体检记录并重算报表，但不删除班级本身
//			if (lastRowIndex == selectedRowIndex) {
				classMap.get(getSelectedGradeCode()).remove(classCode2Remove);
				((DefaultTableModel) tblClass.getModel()).removeRow(selectedRowIndex);
				getSchoolTreeNodeService().removeClass(classLongNumber);
				logger.info("class [" + classLongNumber + "] has been deleted.");
//			}
			JOptionPane.showMessageDialog(this, "删除成功！");
		}
	}

	public void schoolSystemChanged(ItemEvent e) {
		String schoolSystem = e.getItem().toString();
		String schoolType = txtSchoolType.getText();
		refreshGrades(schoolSystem, schoolType);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setLayout(null);
		setBounds(0, 0, 580, 540);
		setPreferredSize(new Dimension(580, 540));

		jTextField3 = new javax.swing.JTextField();
		jTextField4 = new javax.swing.JTextField();
		jToolBar1 = new javax.swing.JToolBar();
		jToolBar1.setBounds(0, 0, 560, 42);
		btnSave = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBounds(22, 12, 52, 16);
		txtCode = new javax.swing.JTextField();
		txtCode.setBounds(86, 6, 180, 28);
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setBounds(278, 12, 52, 16);
		txtName = new javax.swing.JTextField();
		txtName.setBounds(342, 6, 180, 28);
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setBounds(22, 53, 52, 16);
		txtOperator = new javax.swing.JTextField();
		txtOperator.setBounds(86, 47, 180, 28);
		jLabel4 = new javax.swing.JLabel();
		jLabel4.setBounds(278, 53, 52, 16);
		txtTel = new javax.swing.JTextField();
		txtTel.setBounds(342, 47, 180, 28);
		jLabel5 = new javax.swing.JLabel();
		jLabel5.setBounds(288, 87, 42, 16);
		txtAddr = new javax.swing.JTextField();
		txtAddr.setBounds(342, 81, 180, 28);
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setVisible(false);
		jScrollPane1.setBounds(474, 402, 302, 161);
		tblGrades = new javax.swing.JTable();
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setBounds(36, 87, 38, 16);
		txtMobile = new javax.swing.JTextField();
		txtMobile.setBounds(86, 81, 180, 28);

		jTextField3.setText("jTextField3");

		jTextField4.setText("jTextField4");

		jToolBar1.setFloatable(false);
		jToolBar1.setRollover(true);

		btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/system_save.gif"))); // NOI18N
		btnSave.setText("保存");
		btnSave.setFocusable(false);
		btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSaveActionPerformed(evt);
			}
		});
		jToolBar1.add(btnSave);

		btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/door_out.png"))); // NOI18N
		btnCancel.setText("取消");
		btnCancel.setFocusable(false);
		btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		jToolBar1.add(btnCancel);

		add(jToolBar1);

		jLabel1.setText("学校代码");

		txtCode.setEditable(false);

		jLabel2.setText("学校名称");

		txtName.setEditable(false);

		jLabel3.setText("操 作 员");

		jLabel4.setText("办公电话");

		jLabel5.setText("地    址");
		jLabel5.setToolTipText("");

		tblGrades.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "code", "启用", "年级名称", "班级数目" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Integer.class };
			boolean[] canEdit = new boolean[] { false, true, false, true };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(tblGrades);

		jLabel6.setText("手   机");

		add(jPanel1);
		jPanel1.setLayout(null);
		jPanel1.setBounds(0, 42, 560, 520);
		jPanel1.add(jScrollPane1);
		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jLabel4);
		jPanel1.add(jLabel5);
		jPanel1.add(jLabel6);
		jPanel1.add(txtCode);
		jPanel1.add(txtName);
		jPanel1.add(txtOperator);
		jPanel1.add(txtTel);
		jPanel1.add(txtAddr);
		jPanel1.add(txtMobile);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 154, 130, 273);
		jPanel1.add(scrollPane);

		tblGrade = new com.vastcm.swing.jtable.MyTable();
		tblGrade.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "code", "年级名称" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, true, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		scrollPane.setViewportView(tblGrade);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(267, 154, 272, 273);
		jPanel1.add(scrollPane_1);

		tblClass = new com.vastcm.swing.jtable.MyTable();
		tblClass.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "code", "班级名称", "班级全名" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, true, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		scrollPane_1.setViewportView(tblClass);

		JButton btnAddClass = new JButton("增加班级");

		btnAddClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddClass_actionPerformed(e);
			}
		});
		btnAddClass.setBounds(164, 174, 90, 29);
		jPanel1.add(btnAddClass);

		JButton btnRemoveClass = new JButton("删除班级");
		btnRemoveClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemoveClass_actionPerformed(e);
			}
		});
		btnRemoveClass.setBounds(164, 215, 90, 29);
		jPanel1.add(btnRemoveClass);

		JLabel label = new JLabel();
		label.setText("办学制度");
		label.setBounds(21, 115, 80, 24);
		jPanel1.add(label);

		cbSchoolSystem = new JComboBox();
		cbSchoolSystem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				schoolSystemChanged(e);
			}
		});
		cbSchoolSystem.setModel(new DefaultComboBoxModel(new String[] { "63制", "54制", "53制" }));
		cbSchoolSystem.setBounds(86, 115, 180, 27);
		jPanel1.add(cbSchoolSystem);

		JLabel label_1 = new JLabel();
		label_1.setText("学校类型");
		label_1.setBounds(278, 115, 80, 24);
		jPanel1.add(label_1);

		txtSchoolType = new JTextField();
		txtSchoolType.setEditable(false);
		txtSchoolType.setBounds(342, 113, 180, 28);
		jPanel1.add(txtSchoolType);
	}// </editor-fold>//GEN-END:initComponents

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		try {
			String selectedGradeCode = getSelectedGradeCode();
			if (selectedGradeCode != null)
				storeClassesUpdates(selectedGradeCode);
			save(); // save school info.
			List<SchoolTreeNode> lsClass2Save = new ArrayList<SchoolTreeNode>();
			Iterator<TreeMap<String, SchoolTreeNode>> iter = classMap.values().iterator();
			while (iter.hasNext()) {
				TreeMap<String, SchoolTreeNode> treeMap = (TreeMap<String, SchoolTreeNode>) iter.next();
				lsClass2Save.addAll(treeMap.values());
			}
			getSchoolTreeNodeService().save(lsClass2Save);
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, "保存失败，详见日志！");
		}
		//        List<SchoolTreeNode> classTreeNodes = getSchoolTreeNodeService().getClassNodes();
		//        Iterator<SchoolTreeNode> iterClass = classTreeNodes.iterator();
		//        List<String> classNoLs = new ArrayList<String>();
		//        while (iterClass.hasNext()) {
		//            SchoolTreeNode schoolTreeNode = iterClass.next();
		//            classNoLs.add(schoolTreeNode.getLongNumber());
		//        }
		//        DefaultMutableTreeNode schoolNode = (DefaultMutableTreeNode) getUIContext().get("schoolTreeNode");
		//        SchoolTreePanel schoolTreePanel = (SchoolTreePanel) getUIContext().get("owner");
		//        Queue dirtyTreeNodes = schoolTreePanel.getDirtyTreeNodes();
		//        SchoolTreeNode schoolTreeNode = (SchoolTreeNode) schoolNode.getUserObject();
		//        SchoolTreeNode classTreeNode = null;
		//        DefaultTableModel tblModel = (DefaultTableModel) tblGrades.getModel();
		//        int rowCount = tblModel.getRowCount();
		//        for(int i=0; i < rowCount; i++) {
		//            Boolean isSelect = (Boolean) tblModel.getValueAt(i, 1);
		//            if(!isSelect) {
		//                continue;
		//            }
		//            Integer classCount = (Integer) tblModel.getValueAt(i, 3);
		//            for(int j = 1; j <= classCount; j++) {
		//                String gradeCode = (String)tblModel.getValueAt(i, 0);
		//                classTreeNode = new SchoolTreeNode();
		//                String code = j < 10 ? "0" + j : "" + j;
		//                code = gradeCode + code;
		//                String longNo = schoolTreeNode.getLongNumber() + "!" + code;
		//                if(classNoLs.contains(longNo)) {
		//                    continue;
		//                }
		//                classTreeNode.setCode(code);
		//                classTreeNode.setName((String)tblModel.getValueAt(i, 2) + "(" + j + ")班");
		//                classTreeNode.setType(SchoolTreeNode.TYPE_CLASS);
		//                classTreeNode.setLongNumber(longNo);
		//                classTreeNode.setParentCode(schoolTreeNode.getCode());
		//                classTreeNode.setStatus(SchoolTreeNode.STATUS_NORMAL);
		//                DefaultMutableTreeNode gradeNode = new DefaultMutableTreeNode(classTreeNode);
		//                schoolNode.add(gradeNode);
		//                dirtyTreeNodes.add(gradeNode);
		//            }
		//        }
		//        schoolTreePanel.saveTree();
		SchoolTreePanel schoolTreePanel = (SchoolTreePanel) getUIContext().get("owner");
		schoolTreePanel.refreshTree();
		JTree tree = (JTree) getUIContext().get("tree");
		TreeUtils.expand(tree, new TreePath(tree.getModel().getRoot()), 4);
		//        disposeUI();
	}//GEN-LAST:event_btnSaveActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		disposeUI();

	}//GEN-LAST:event_btnCancelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnSave;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JTextField jTextField4;
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JTable tblGrades;
	private javax.swing.JTextField txtAddr;
	private javax.swing.JTextField txtCode;
	private javax.swing.JTextField txtMobile;
	private javax.swing.JTextField txtName;
	private javax.swing.JTextField txtOperator;
	private javax.swing.JTextField txtTel;
	private JScrollPane scrollPane;
	private com.vastcm.swing.jtable.MyTable tblGrade;
	private JScrollPane scrollPane_1;
	private com.vastcm.swing.jtable.MyTable tblClass;
	private JTextField txtSchoolType;
	private JComboBox cbSchoolSystem;

	// End of variables declaration//GEN-END:variables

	@Override
	public ICoreService<School> getService() {
		return AppContext.getBean("schoolService", ISchoolService.class);
	}
}
