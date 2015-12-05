/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.Dimension;
import java.awt.Window;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.entity.Nation;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.INationService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.ScreenUtils;
import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.ui.MyDateChooser;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.UIClientUtil;
import com.vastcm.stuhealth.client.utils.biz.GradeMessage;
import com.vastcm.stuhealth.client.utils.biz.StudentCode;

/**
 * 
 * @author House
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class StudentPanel extends KernelUI {

	private Logger logger = LoggerFactory.getLogger(StudentPanel.class);
	private IStudentService studentService;
	private Dimension normalSize = new Dimension(700, 120);
	private Dimension detailSize = new Dimension(700, 320);
	private String id = null;

	public StudentPanel() {
		initComponents();

		lblYear.setVisible(false);
		txtYear.setVisible(false);
		lblTerm.setVisible(false);
		txtTerm.setVisible(false);
		lblbStudentNo.setVisible(false);
		txtStudentNo.setVisible(false);
	}

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		initComponentsEx();
		initData();
	}

	protected void initComponentsEx() {
		//this.setSize(120, (int) getSize().getWidth());
		setPreferredSize(detailSize);

		INationService nationSrv = AppContext.getBean("nationService", INationService.class);
		List<Nation> lsNation = nationSrv.getAll();
		for (Nation n : lsNation) {
			cbFolk.addItem(n.getNationName());
		}

		for (StudentSourceEnum src : StudentSourceEnum.values()) {
			cbSource.addItem(src);
		}

		cbXX.addItem(null);
		for (BloodTypeEnum bt : BloodTypeEnum.values()) {
			cbXX.addItem(bt);
		}

		for (StudentStatusEnum ss : StudentStatusEnum.values()) {
			cbStatus.addItem(ss);
		}
	}

	private IStudentService getService() {
		if (studentService == null) {
			studentService = AppContext.getBean("studentService", IStudentService.class);
		}
		return studentService;
	}

	private void initData() {
		String uiStatus = (String) getUIContext().get(UIContext.UI_STATUS);
		if ("NEW".equals(uiStatus)) {
			initDataWhenAddNew();
			initComponentsWhenAddNew();
		}
		if ("EDIT".equals(uiStatus)) {
			initDataWhenEdit();
			initComponentsWhenEdit();
		}
	}

	private void initDataWhenAddNew() {
		SchoolTreeNode schoolTreeNode = (SchoolTreeNode) getUIContext().get("classNode");
		String schoolCode = schoolTreeNode.getParentCode();
		int currentYear = GlobalVariables.getGlobalVariables().getYear();
		String year = String.valueOf(currentYear);
		//		String waterCode = CodeProducer.getCodeProducer().getCode(schoolCode, year);
		//		String studentCode = schoolTreeNode.getCode().charAt(0) + year.substring(2) + waterCode;
		//		txtStudentCode.setText(studentCode);
		//		txtStudentNo.setText(waterCode);
		txtClassName.setText(schoolTreeNode.getName());
		txtClassNo.setText(schoolTreeNode.getCode());
		txtClassLongNumber.setText(schoolTreeNode.getLongNumber());
		txtSchoolNo.setText(schoolCode);
		txtGradeNo.setText(GradeMessage.getGradeMessageByClassCode(schoolTreeNode.getCode()).getGradeCode());
		txtYear.setText(year);
		txtTerm.setText(String.valueOf(GlobalVariables.getGlobalVariables().getTerm()));
		txtZZMM.setText("学生");
		cbStatus.setSelectedItem(StudentStatusEnum.在学);
	}

	private void initComponentsWhenAddNew() {
		txtClassName.setEnabled(false);
		txtSchoolNo.setEnabled(false);
	}

	private void initDataWhenEdit() {
		id = (String) getUIContext().get("id");
		Student student = getService().getById(id);

		txtClassName.setText(student.getClassName());
		txtStudentCode.setText(student.getStudentCode());
		txtName.setText(student.getName());
		cbSex.setSelectedItem(student.getSex());
		txtXJH.setText(student.getXjh());
		//		txtXX.setText(student.getXx());
		if (student.getXx() != null) {
			cbXX.setSelectedItem(BloodTypeEnum.getBloodType(student.getXx()));
		}

		//        txtYear.setText(student.getStYear());
		//        txtTerm.setText(String.valueOf(student.getTerm()));
		txtSchoolNo.setText(student.getSchoolNo());
		txtGradeNo.setText(student.getGradeNo());
		txtClassNo.setText(student.getClassNo());
		txtClassLongNumber.setText(student.getClassLongNumber());
		txtStudentNo.setText(student.getStudentNo());
		txtMobilePhone.setText(student.getMobilePhone());
		txtStudentType.setText(student.getStudentType());
		pkBornDate.setDate(student.getBornDate());
		pkEnterDate.setDate(student.getEnterDate());
		cbFolk.setSelectedItem(student.getNation());
		txtAddress.setText(student.getAddress());
		txtPhone.setText(student.getPhone());
		//		txtStatus.setText(student.getStatus());
		if (student.getStatus() != null) {
			cbStatus.setSelectedItem(StudentStatusEnum.getStudentStatus(student.getStatus()));
		}
		txtZZMM.setText(student.getZzmm());
		try {
			cbSource.setSelectedItem(StudentSourceEnum.valueOf(student.getSy()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		txtSFZ.setText(student.getSfz());
		// targetClass
		txtFQXM.setText(student.getFqxm());
		txtFQDW.setText(student.getFqdw());
		txtMQXM.setText(student.getMqxm());
		txtMQDW.setText(student.getMqdw());
	}

	private void initComponentsWhenEdit() {
		txtClassNo.setEnabled(false);
		txtClassName.setEnabled(false);
		txtClassLongNumber.setEnabled(false);
		txtStudentCode.setEnabled(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jToolBar1 = new javax.swing.JToolBar();
		btnSave = new javax.swing.JButton();
		btnSaveAndAddNew = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		btnDetail = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		txtClassName = new javax.swing.JTextField();
		txtStudentCode = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();
		cbSex = new javax.swing.JComboBox();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		txtXJH = new javax.swing.JTextField();
		jLabel11 = new javax.swing.JLabel();
		jLabel26 = new javax.swing.JLabel();
		txtClassLongNumber = new javax.swing.JTextField();
		jLabel8 = new javax.swing.JLabel();
		txtSchoolNo = new javax.swing.JTextField();
		jLabel9 = new javax.swing.JLabel();
		txtGradeNo = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		txtClassNo = new javax.swing.JTextField();
		jlabelxx = new javax.swing.JLabel();
		txtMobilePhone = new javax.swing.JTextField();
		jLabel13 = new javax.swing.JLabel();
		txtStudentType = new javax.swing.JTextField();
		lblYear = new javax.swing.JLabel();
		txtYear = new javax.swing.JTextField();
		lblTerm = new javax.swing.JLabel();
		txtTerm = new javax.swing.JTextField();
		lblbStudentNo = new javax.swing.JLabel();
		txtStudentNo = new javax.swing.JTextField();
		jlabel2x = new javax.swing.JLabel();
		pkBornDate = new MyDateChooser();
		jLabel15 = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		pkEnterDate = new MyDateChooser();
		jLabel17 = new javax.swing.JLabel();
		txtAddress = new javax.swing.JTextField();
		jLabel18 = new javax.swing.JLabel();
		txtPhone = new javax.swing.JTextField();
		jLabel20 = new javax.swing.JLabel();
		txtZZMM = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		txtSFZ = new javax.swing.JTextField();
		jLabel22 = new javax.swing.JLabel();
		txtFQXM = new javax.swing.JTextField();
		jLabel23 = new javax.swing.JLabel();
		txtFQDW = new javax.swing.JTextField();
		jLabel24 = new javax.swing.JLabel();
		txtMQXM = new javax.swing.JTextField();
		jLabel25 = new javax.swing.JLabel();
		txtMQDW = new javax.swing.JTextField();
		jLabel14 = new javax.swing.JLabel();
		txtTargetClass = new javax.swing.JTextField();
		jLabel21 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		jSeparator2 = new javax.swing.JSeparator();
		cbFolk = new javax.swing.JComboBox();
		cbSource = new javax.swing.JComboBox();

		setLayout(new java.awt.BorderLayout());

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

		btnSaveAndAddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/system_save.gif"))); // NOI18N
		btnSaveAndAddNew.setText("保存并新建");
		btnSaveAndAddNew.setFocusable(false);
		btnSaveAndAddNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnSaveAndAddNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnSaveAndAddNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSaveAndAddNewActionPerformed(evt);
			}
		});
		jToolBar1.add(btnSaveAndAddNew);

		btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/door_out.png"))); // NOI18N
		btnCancel.setText("  取消");
		btnCancel.setFocusable(false);
		btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});
		jToolBar1.add(btnCancel);

		btnDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_view_detail.png"))); // NOI18N
		btnDetail.setText("详细资料");
		btnDetail.setFocusable(false);
		btnDetail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnDetail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnDetail.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDetailActionPerformed(evt);
			}
		});
		jToolBar1.add(btnDetail);

		add(jToolBar1, java.awt.BorderLayout.NORTH);

		jPanel2.setLayout(null);

		jLabel5.setText("班级名*");
		jLabel5.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(jLabel5);
		jLabel5.setBounds(20, 10, 80, 25);

		txtClassName.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(txtClassName);
		txtClassName.setBounds(80, 10, 120, 25);

		txtStudentCode.setEditable(false);
		txtStudentCode.setPreferredSize(new java.awt.Dimension(120, 21));
		jPanel2.add(txtStudentCode);
		txtStudentCode.setBounds(310, 10, 120, 25);

		jLabel1.setText("代码");
		jLabel1.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(jLabel1);
		jLabel1.setBounds(238, 10, 80, 25);

		jLabel2.setText("名字*");
		jLabel2.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(jLabel2);
		jLabel2.setBounds(20, 40, 80, 25);

		txtName.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(txtName);
		txtName.setBounds(80, 40, 120, 25);

		cbSex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "男", "女" }));
		cbSex.setMinimumSize(new java.awt.Dimension(6, 21));
		cbSex.setPreferredSize(new java.awt.Dimension(120, 20));
		jPanel2.add(cbSex);
		cbSex.setBounds(310, 40, 120, 25);

		jLabel3.setText("性别*");
		jLabel3.setPreferredSize(new java.awt.Dimension(80, 20));
		jPanel2.add(jLabel3);
		jLabel3.setBounds(240, 40, 80, 25);

		jLabel4.setText("学籍号");
		jLabel4.setPreferredSize(new java.awt.Dimension(60, 21));
		jPanel2.add(jLabel4);
		jLabel4.setBounds(453, 10, 80, 25);

		txtXJH.setPreferredSize(new java.awt.Dimension(120, 21));
		jPanel2.add(txtXJH);
		txtXJH.setBounds(520, 10, 120, 25);

		jLabel11.setText("血型");
		jPanel2.add(jLabel11);
		jLabel11.setBounds(453, 93, 80, 25);

		jLabel26.setText("班级长编码");
		jLabel26.setFocusable(false);
		jPanel2.add(jLabel26);
		jLabel26.setBounds(20, 302, 80, 25);

		txtClassLongNumber.setEditable(false);
		jPanel2.add(txtClassLongNumber);
		txtClassLongNumber.setBounds(90, 302, 120, 25);

		jLabel8.setText("学校代码");
		jPanel2.add(jLabel8);
		jLabel8.setBounds(230, 272, 80, 25);

		txtSchoolNo.setEditable(false);
		jPanel2.add(txtSchoolNo);
		txtSchoolNo.setBounds(310, 272, 120, 25);

		jLabel9.setText("年级代码");
		jPanel2.add(jLabel9);
		jLabel9.setBounds(230, 302, 80, 25);

		txtGradeNo.setEditable(false);
		jPanel2.add(txtGradeNo);
		txtGradeNo.setBounds(310, 302, 120, 25);

		jLabel10.setText("班级代码");
		jPanel2.add(jLabel10);
		jLabel10.setBounds(20, 272, 80, 25);

		txtClassNo.setEditable(false);
		jPanel2.add(txtClassNo);
		txtClassNo.setBounds(90, 272, 120, 25);

		jlabelxx.setText("手机");
		jPanel2.add(jlabelxx);
		jlabelxx.setBounds(20, 92, 80, 25);
		jPanel2.add(txtMobilePhone);
		txtMobilePhone.setBounds(80, 92, 120, 25);

		jLabel13.setText("学生类型");
		jPanel2.add(jLabel13);
		jLabel13.setBounds(230, 92, 80, 25);
		jPanel2.add(txtStudentType);
		txtStudentType.setBounds(310, 92, 120, 25);

		lblYear.setText("年份");
		jPanel2.add(lblYear);
		lblYear.setBounds(20, 365, 80, 25);
		jPanel2.add(txtYear);
		txtYear.setBounds(80, 365, 120, 25);

		lblTerm.setText("学期");
		jPanel2.add(lblTerm);
		lblTerm.setBounds(230, 365, 80, 25);
		jPanel2.add(txtTerm);
		txtTerm.setBounds(310, 365, 120, 25);

		lblbStudentNo.setText("在学校编号");
		jPanel2.add(lblbStudentNo);
		lblbStudentNo.setBounds(450, 365, 80, 25);
		jPanel2.add(txtStudentNo);
		txtStudentNo.setBounds(520, 365, 120, 25);

		jlabel2x.setText("出生日期*");
		jPanel2.add(jlabel2x);
		jlabel2x.setBounds(449, 39, 80, 25);
		jPanel2.add(pkBornDate);
		pkBornDate.setBounds(521, 40, 120, 25);

		jLabel15.setText("民族");
		jPanel2.add(jLabel15);
		jLabel15.setBounds(20, 122, 80, 25);

		jLabel16.setText("入学时间*");
		jPanel2.add(jLabel16);
		jLabel16.setBounds(230, 122, 80, 25);
		jPanel2.add(pkEnterDate);
		pkEnterDate.setBounds(310, 122, 120, 25);

		jLabel17.setText("地址");
		jPanel2.add(jLabel17);
		jLabel17.setBounds(450, 122, 80, 25);
		jPanel2.add(txtAddress);
		txtAddress.setBounds(520, 122, 120, 25);

		jLabel18.setText("电话");
		jPanel2.add(jLabel18);
		jLabel18.setBounds(20, 152, 80, 25);
		jPanel2.add(txtPhone);
		txtPhone.setBounds(80, 152, 120, 25);

		jLabel20.setText("政治面貌");
		jPanel2.add(jLabel20);
		jLabel20.setBounds(230, 152, 80, 25);
		jPanel2.add(txtZZMM);
		txtZZMM.setBounds(310, 152, 120, 25);

		jLabel12.setText("身份证");
		jPanel2.add(jLabel12);
		jLabel12.setBounds(450, 152, 80, 25);
		jPanel2.add(txtSFZ);
		txtSFZ.setBounds(520, 152, 120, 25);

		jLabel22.setText("父亲姓名");
		jPanel2.add(jLabel22);
		jLabel22.setBounds(20, 182, 80, 25);
		jPanel2.add(txtFQXM);
		txtFQXM.setBounds(80, 182, 120, 25);

		jLabel23.setText("父亲单位");
		jPanel2.add(jLabel23);
		jLabel23.setBounds(230, 182, 80, 25);
		jPanel2.add(txtFQDW);
		txtFQDW.setBounds(310, 182, 120, 25);

		jLabel24.setText("母亲姓名");
		jPanel2.add(jLabel24);
		jLabel24.setBounds(20, 212, 80, 25);
		jPanel2.add(txtMQXM);
		txtMQXM.setBounds(80, 212, 120, 25);

		jLabel25.setText("母亲单位");
		jPanel2.add(jLabel25);
		jLabel25.setBounds(230, 212, 80, 25);
		jPanel2.add(txtMQDW);
		txtMQDW.setBounds(310, 212, 120, 25);

		jLabel14.setText("目标班级");
		jPanel2.add(jLabel14);
		jLabel14.setBounds(450, 272, 80, 25);

		txtTargetClass.setEditable(false);
		jPanel2.add(txtTargetClass);
		txtTargetClass.setBounds(520, 272, 120, 25);

		jLabel21.setText("生源");
		jPanel2.add(jLabel21);
		jLabel21.setBounds(450, 182, 80, 25);

		jLabel19.setText("状态");
		jPanel2.add(jLabel19);
		jLabel19.setBounds(450, 212, 80, 25);
		jPanel2.add(jSeparator1);
		jSeparator1.setBounds(10, 70, 640, 12);
		jPanel2.add(jSeparator2);
		jSeparator2.setBounds(10, 242, 640, 12);

		jPanel2.add(cbFolk);
		cbFolk.setBounds(80, 122, 120, 25);

		jPanel2.add(cbSource);
		cbSource.setBounds(520, 182, 120, 25);

		add(jPanel2, java.awt.BorderLayout.CENTER);

		cbXX = new JComboBox();
		cbXX.setBounds(520, 93, 120, 25);
		jPanel2.add(cbXX);

		cbStatus = new JComboBox();
		cbStatus.setBounds(520, 212, 120, 25);
		jPanel2.add(cbStatus);
	}// </editor-fold>//GEN-END:initComponents

	public boolean isDataValidate() {
		String name = txtName.getText();
		if (name == null || name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "姓名 不能为空！");
			return false;
		}
		if (pkBornDate.getDate() == null) {
			JOptionPane.showMessageDialog(this, "出生日期 不能为空！");
			return false;
		}
		return true;
	}

	private JComponent[] checkEmptyFields = null;
	private String[] checkEmptyLabels = null;

	protected void checkEmpty() {
		if (checkEmptyFields == null) {
			checkEmptyFields = new JComponent[] { txtClassName, txtName, cbSex, pkBornDate, pkEnterDate };
			checkEmptyLabels = new String[] { "班级", "姓名", "性别", "出生日期", "入学时间" };
		}
		UIClientUtil.checkEmpty(this, checkEmptyFields, checkEmptyLabels);
	}

	private void btnSaveAndAddNewActionPerformed(java.awt.event.ActionEvent evt) {
		btnSaveActionPerformed(evt);
		try {
			UIContext oldUICtx = getUIContext();
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "学生信息维护");
			uiCtx.set(UIContext.UI_STATUS, "NEW");
			uiCtx.set("owner", oldUICtx.get("owner"));
			uiCtx.set("classNode", oldUICtx.get("classNode"));
			JTree treeSchool = (JTree) oldUICtx.get("tree");
			uiCtx.set("tree", treeSchool);
			if (uiCtx.get("classNode") == null) {
				uiCtx.set("classNode", ((DefaultMutableTreeNode) treeSchool.getSelectionPath().getLastPathComponent()).getUserObject());
			}
			UI<StudentPanel> ui = UIFactory.create(StudentPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex);
		}
	}

	private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		//		if (!isDataValidate()) {
		//			return;
		//		}
		checkEmpty();
		if(pkBornDate.getDate().after(pkEnterDate.getDate())) {
			JOptionPane.showMessageDialog(this, "出生日期 不能比 入学日期 晚！");
			ExceptionUtils.abort();
		}
		Student student = new Student();
		student.setId(id);
		// StudentCode,  Name, Sex, XX, XJH
		student.setClassName(txtClassName.getText());
		String studentCode = txtStudentCode.getText();
		if (studentCode == null || studentCode.isEmpty()) {
			SchoolTreeNode schoolTreeNode = (SchoolTreeNode) getUIContext().get("classNode");
			String schoolCode = schoolTreeNode.getParentCode();
			String classNo = schoolTreeNode.getCode();
			studentCode = StudentCode.getNewCode(schoolCode, classNo, pkEnterDate.getDate());
			String waterCode = StudentCode.getWaterCode(studentCode);
			txtStudentCode.setText(studentCode);
			txtStudentNo.setText(waterCode);
			student.setStudentCode(studentCode);
			student.setStudentNo(waterCode);
		} else {
			student.setStudentCode(studentCode);
		}
		student.setName(txtName.getText());
		student.setSex(cbSex.getSelectedItem().toString());
		student.setXjh(txtXJH.getText());
		//		student.setXx(txtXX.getText());
		student.setXx(cbXX.getSelectedItem() == null ? "" : ((BloodTypeEnum) cbXX.getSelectedItem()).getValue());
		student.setClassLongNumber(txtClassLongNumber.getText());
		//        student.setStYear(txtYear.getText());
		//        student.setTerm(Integer.parseInt(txtTerm.getText()));
		student.setSchoolNo(txtSchoolNo.getText());
		student.setGradeNo(txtGradeNo.getText());
		student.setClassNo(txtClassNo.getText());
		student.setStudentNo(txtStudentNo.getText());
		student.setMobilePhone(txtMobilePhone.getText());
		student.setStudentType(txtStudentType.getText());
		//处理年份
		int gradeCode = Integer.parseInt(txtClassNo.getText().substring(0, 2));
		int schoolCode = gradeCode / 10;
		int schoolYear = gradeCode % 10;
		if (schoolCode > 1) {//小学以上
			schoolYear += 6;
		}
		if (schoolCode > 2) {//初中以上
			schoolYear += 3;
		}
		if (schoolCode > 3) {//高中以上
			schoolYear += 3;
		}
		int currentYear = GlobalVariables.getGlobalVariables().getYear();
		int startBornYear = currentYear - schoolYear - 7;
		int endBornYear = currentYear - schoolYear - 6;
		int startEnterYear = currentYear - schoolYear - 1;
		int endEnterYear = currentYear - schoolYear;

		Calendar cal = Calendar.getInstance();
		if (pkBornDate.getDate() != null) {
			cal.setTime(pkBornDate.getDate());
			int bornYear = cal.get(Calendar.YEAR);
			if (bornYear > endBornYear || bornYear < startBornYear) {
				int result = JOptionPane.showConfirmDialog(this, "出生年份应在" + startBornYear + "与" + endBornYear + "之间，继续保存请按\"是\"？");
				if (result != JOptionPane.YES_OPTION) {
					return;
				}
			}

			student.setBornDate(new Date(cal.getTimeInMillis()));
		}
		if (pkEnterDate.getDate() != null) {
			cal.setTime(pkEnterDate.getDate());
			int enterYear = cal.get(Calendar.YEAR);
			if (enterYear > endEnterYear || enterYear < startEnterYear) {
				int result = JOptionPane.showConfirmDialog(this, "入学年份应在" + startEnterYear + "与" + endEnterYear + "之间，继续保存请按\"是\"？");
				if (result != JOptionPane.YES_OPTION) {
					return;
				}
			}

			student.setEnterDate(new Date(cal.getTimeInMillis()));
		}
		student.setNation(cbFolk.getSelectedItem() == null ? "" : cbFolk.getSelectedItem().toString());
		student.setAddress(txtAddress.getText());
		student.setPhone(txtPhone.getText());
		//		student.setStatus(txtStatus.getText());
		student.setStatus(cbStatus.getSelectedItem() == null ? "" : ((StudentStatusEnum) cbStatus.getSelectedItem()).getValue());
		student.setZzmm(txtZZMM.getText());
		student.setSy(cbSource.getSelectedItem() == null ? "" : cbSource.getSelectedItem().toString());
		student.setSfz(txtSFZ.getText());
		// target class
		student.setFqxm(txtFQXM.getText());
		student.setFqdw(txtFQDW.getText());
		student.setMqxm(txtMQXM.getText());
		student.setMqdw(txtMQDW.getText());
		getService().save(student);

		StudentListPanel listPanel = (StudentListPanel) getUIContext().get("owner");
		listPanel.refreshList();
		disposeUI();
	}//GEN-LAST:event_btnSaveActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		disposeUI();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
		Dimension size = getPreferredSize();
		if (size.equals(normalSize)) {
			setPreferredSize(detailSize);
		} else {
			setPreferredSize(normalSize);
		}
		Window win = SwingUtilities.getWindowAncestor(this);
		if (win != null) {
			win.pack();
			ScreenUtils.center(win);
		}
	}//GEN-LAST:event_btnDetailActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnDetail;
	private javax.swing.JButton btnSave;
	private javax.swing.JButton btnSaveAndAddNew;
	private javax.swing.JComboBox cbFolk;
	private javax.swing.JComboBox cbSex;
	private javax.swing.JComboBox cbSource;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel23;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel25;
	private javax.swing.JLabel jLabel26;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel lblYear;
	private javax.swing.JLabel lblTerm;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JLabel jlabel2x;
	private javax.swing.JLabel lblbStudentNo;
	private javax.swing.JLabel jlabelxx;
	private javax.swing.JTextField txtAddress;
	private MyDateChooser pkBornDate;
	private javax.swing.JTextField txtClassLongNumber;
	private javax.swing.JTextField txtClassName;
	private javax.swing.JTextField txtClassNo;
	private MyDateChooser pkEnterDate;
	private javax.swing.JTextField txtFQDW;
	private javax.swing.JTextField txtFQXM;
	private javax.swing.JTextField txtGradeNo;
	private javax.swing.JTextField txtMQDW;
	private javax.swing.JTextField txtMQXM;
	private javax.swing.JTextField txtMobilePhone;
	private javax.swing.JTextField txtName;
	private javax.swing.JTextField txtPhone;
	private javax.swing.JTextField txtSFZ;
	private javax.swing.JTextField txtSchoolNo;
	private javax.swing.JTextField txtStudentCode;
	private javax.swing.JTextField txtStudentNo;
	private javax.swing.JTextField txtStudentType;
	private javax.swing.JTextField txtTargetClass;
	private javax.swing.JTextField txtTerm;
	private javax.swing.JTextField txtXJH;
	private javax.swing.JTextField txtYear;
	private javax.swing.JTextField txtZZMM;
	private JComboBox cbXX;
	private JComboBox cbStatus;
	// End of variables declaration//GEN-END:variables
}
