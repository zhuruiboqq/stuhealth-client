/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.impl.SchoolService;
import com.vastcm.stuhealth.client.framework.exception.BizRunTimeException;
import com.vastcm.stuhealth.client.framework.report.service.ISqlService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.MessageDialogUtil;
import com.vastcm.stuhealth.client.utils.MyTableUtils;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;
import com.vastcm.swing.dialog.longtime.ILongTimeTask;
import com.vastcm.swing.dialog.longtime.LongTimeProcessDialog;
import com.vastcm.swing.dialog.longtime.LongTimeTaskAdapter;
import com.vastcm.swing.dialog.longtime.LongTimeTaskDialog;
import com.vastcm.swing.jtable.MyRow;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author House
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class StudentListPanel extends KernelUI implements SchoolTreeListener {

	private Logger logger = LoggerFactory.getLogger(StudentListPanel.class);
	private IStudentService studentService;
	private ISchoolService schoolService;
	private ICheckResultService checkResultService;
	private JTree treeSchool;
	private SchoolTreePanel schoolTreePanel;
	private JPopupMenu pMenu4SchoolTreeNode;
	private JMenuItem menuChangeClassByClass; // 按班调班
	private JMenuItem menuExportStudents; // Excel导出学生
	private JPopupMenu pMenu4Student;
	private JMenuItem menuChangeClassByStudent; // 按学生调班
	private Map<String, String> stuFieldsMap;
	private Map<String, String> stuFieldsRMap;
	private ISqlService sqlService;
	private JMenuItem menuGraduateByClass;//按班毕业
	private JMenuItem menuGraduateByStudent;//按学生毕业
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String excelDir = System.getProperty("user.dir");

	public StudentListPanel() {
		initComponents();
	}

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		schoolTreePanel = new SchoolTreePanel();
		schoolTreePanel.setToolbarVisible(false);
		schoolTreePanel.addSchoolTreeListener(this);
		jSplitPane1.setLeftComponent(schoolTreePanel);
		treeSchool = schoolTreePanel.getTree();
		treeSchool.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent me) {
				mousePressedOnSchoolTree(me);
			}

		});
		tblStudent.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				mouseClickedOnTblStudent(me);
			}

		});

		DefaultComboBoxModel cbModelStuFields = new DefaultComboBoxModel();
		stuFieldsMap = new LinkedHashMap<String, String>() {
		};
		stuFieldsRMap = new HashMap<String, String>() {
		};
		stuFieldsMap.put("name", "名字");
		stuFieldsMap.put("studentCode", "学生代码");
		stuFieldsMap.put("studentNo", "在学校编号");
		stuFieldsMap.put("xjh", "学籍号");
		//		stuFieldsMap.put("SchoolNo", "学校代码");
		//		stuFieldsMap.put("GradeNo", "年级代码");
		//		stuFieldsMap.put("ClassNo", "班级代码");
		stuFieldsMap.put("className", "班级名");
		//		stuFieldsMap.put("StYear", "年份");
		stuFieldsMap.put("zzmm", "政治面目");
		//		stuFieldsMap.put("Term", "学期");
		//		stuFieldsMap.put("CityId", "市代码");
		//		stuFieldsMap.put("DistrictId", "区代码");
		stuFieldsMap.put("mobilePhone", "手机");
		stuFieldsMap.put("studentType", "学生类型");
		stuFieldsMap.put("bornDate", "出生日期");
		stuFieldsMap.put("nation", "民族");
		stuFieldsMap.put("enterDate", "入学时间");
		stuFieldsMap.put("address", "地址");
		stuFieldsMap.put("phone", "电话");
		stuFieldsMap.put("status", "状态");
		stuFieldsMap.put("sex", "性别");
		stuFieldsMap.put("sy", "生源");
		stuFieldsMap.put("xx", "血型");
		stuFieldsMap.put("sfz", "身份证");
		stuFieldsMap.put("fqxm", "父亲姓名");
		stuFieldsMap.put("fqdw", "父亲单位");
		stuFieldsMap.put("mqxm", "母亲姓名");
		stuFieldsMap.put("mqdw", "母亲单位");
		Iterator<Entry<String, String>> iterStuFields = stuFieldsMap.entrySet().iterator();
		while (iterStuFields.hasNext()) {
			Entry entry = iterStuFields.next();
			cbModelStuFields.addElement(entry.getValue().toString());
			stuFieldsRMap.put(entry.getValue().toString(), entry.getKey().toString());
		}
		cbFields.setModel(cbModelStuFields);

		initComponentsEx();
		//        refreshList();
	}

	protected void initComponentsEx() {
		MyTableUtils.hiddenColumn(tblStudent, 0);
		MyTableUtils.hiddenColumn(tblStudent, 1);

		tblStudent.setIsLocked(true);

	}

	private void initPopupMenu() {
		pMenu4SchoolTreeNode = new JPopupMenu();
		menuChangeClassByClass = new JMenuItem("按班级调班（全班调班）..");
		menuChangeClassByClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					changeClassByClass();
				} catch (Exception e) {
					UIHandler.handleException(StudentListPanel.this, logger, e);
				}
			}
		});
		pMenu4SchoolTreeNode.add(menuChangeClassByClass);
		
		menuGraduateByClass = new JMenuItem("按班级毕业（全班毕业）..");
		menuGraduateByClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					graduateByClass();
				} catch (Exception e) {
					UIHandler.handleException(StudentListPanel.this, logger, e);
				}
			}
		});
		pMenu4SchoolTreeNode.add(menuGraduateByClass);
		
		pMenu4SchoolTreeNode.addSeparator();
		
		menuExportStudents = new JMenuItem("将学生信息导出成Excel");
		menuExportStudents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportStu2Excel();
			}
		});
		pMenu4SchoolTreeNode.add(menuExportStudents);

		pMenu4Student = new JPopupMenu();
		menuChangeClassByStudent = new JMenuItem("调班..");
		menuChangeClassByStudent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					changeClassByStudent();
				} catch (Exception e) {
					UIHandler.handleException(StudentListPanel.this, logger, e);
				}
			}
		});
		pMenu4Student.add(menuChangeClassByStudent);

		menuGraduateByStudent = new JMenuItem("学生毕业..");
		menuGraduateByStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					graduateByStudent();
				} catch (Exception e) {
					UIHandler.handleException(StudentListPanel.this, logger, e);
				}
			}
		});
		pMenu4Student.add(menuGraduateByStudent);
	}
	
	public void exportStu2Excel() {
		LongTimeProcessDialog longTimeDialog = new LongTimeProcessDialog(UIFrameworkUtils.getMainUI());
 		longTimeDialog.setLongTimeTask(new ILongTimeTask() {
 			
 			@Override
 			public Object exec() throws Exception {
 					String filePath = genExcel();
 	 				JOptionPane.showMessageDialog(null, "生成Excel成功！文件路径：\n" + filePath);
 	 				return "success";
 			}
 			
 			@Override
 			public void afterExec(Object paramObject) throws Exception {
 				
 			}
 		});
 		longTimeDialog.setTitle("正在导出学生信息，请稍候。。。");
 		longTimeDialog.show();
		
	}
	
	/**
	 * 将学生信息生成EXCEL表
	 * @author 		house
	 * @email  		yyh2001@gmail.com
	 * @bornDate  	Feb 16, 2014
	 * @return		excel文件路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String genExcel() throws FileNotFoundException, IOException {
		logger.info("genExcel()...");
		SchoolTreeNode node = getSelectedTreeNode();
		Map<String, String> schoolCodeNameMap = new HashMap<String, String>();
		String name = node.getName();
		String longNum = node.getLongNumber();
		String filename = name;
		if(node.getType() ==  SchoolTreeNode.TYPE_CLASS) {
			logger.info("going to get school name.");
			filename = getSchoolService().getByCode(node.getParentCode()).getName() + "_" + name;
			logger.info("Get school name success.");
		}
		
		List<Student> students = getService().getStudentsByClass(longNum, true);
		logger.info("Get students success.");
		
		HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件  
		logger.info("new work book");
	    HSSFSheet sheet = workbook.createSheet("学生信息");// 创建一个Excel的Sheet
	    logger.info("new sheet");
	    // Sheet样式    
	    HSSFCellStyle headRowStyle = workbook.createCellStyle();    
	    // 背景色的设定    
	    // headRowStyle.setFillBackgroundColor(HSSFColor.YELLOW.index); 
	    HSSFRow row0 = sheet.createRow(0);    
	    // 设置行高    
	    // row0.setHeight((short) 900);    
	    
	    String[] header = new String[] {
	    	"学校", "班级", "学生姓名", "代码", "学籍号", "性别", 
	    	"出生日期", "入学时间", "手机号码", "学生类型", "血型", 
	    	"民族", "地址", "电话", "政治面貌", "身份证", 
	    	"父亲姓名", "父亲单位", "母亲姓名", "母亲单位", "生源"
	    };
	    for(int i = 0; i < header.length; i++) {
	    	HSSFCell cell = row0.createCell(i); 
	    	cell.setCellValue(new HSSFRichTextString(header[i]));
	    	cell.setCellStyle(headRowStyle);
	    }
	      
	    logger.info("loop students");
	    for(int i = 0; i < students.size(); i++) {
	    	Student stu = students.get(i);
	    	HSSFRow r = sheet.createRow(i+1);
	    	
	    	HSSFCell cSchool = r.createCell(0); 
	    	String schoolNo = stu.getSchoolNo();
	    	String schoolName = schoolCodeNameMap.get(schoolNo);
	    	if(schoolName == null) {
	    		logger.info("get school " + schoolNo);
	    		schoolName = getSchoolService().getByCode(schoolNo).getName();
	    		schoolCodeNameMap.put(schoolNo, schoolName);
	    		logger.info("get school ok");
	    	}
	    	cSchool.setCellValue(schoolName);
	    	
	    	HSSFCell cClass = r.createCell(1);
	    	cClass.setCellValue(stu.getClassName());
	    	
	    	HSSFCell cStuName = r.createCell(2);
	    	cStuName.setCellValue(stu.getName());
	    	
	    	HSSFCell cCode = r.createCell(3);
	    	cCode.setCellValue(stu.getStudentCode());
	    	
	    	HSSFCell cXjh = r.createCell(4);
	    	cXjh.setCellValue(stu.getXjh());
	    	
	    	HSSFCell cSex = r.createCell(5);
	    	cSex.setCellValue(stu.getSex());
	    	
	    	HSSFCell cBornDate = r.createCell(6);
	    	if(stu.getBornDate() != null)
	    		cBornDate.setCellValue(sdf.format(stu.getBornDate()));
	    	
	    	HSSFCell cEnterDate = r.createCell(7);
	    	if(stu.getEnterDate() != null)
	    		cEnterDate.setCellValue(sdf.format(stu.getEnterDate()));
	    	
	    	HSSFCell cCellphone = r.createCell(8);
	    	cCellphone.setCellValue(stu.getMobilePhone());
	    	
	    	HSSFCell cStuType = r.createCell(9);
	    	cStuType.setCellValue(stu.getStudentType());
	    	
	    	HSSFCell cBlood = r.createCell(10);
	    	cBlood.setCellValue(stu.getXx());
	    	
	    	HSSFCell cNation = r.createCell(11);
	    	cNation.setCellValue(stu.getNation());
	    	
	    	HSSFCell cAddr = r.createCell(12);
	    	cAddr.setCellValue(stu.getAddress());
	    	
	    	HSSFCell cTel = r.createCell(13);
	    	cTel.setCellValue(stu.getPhone());
	    	
	    	HSSFCell cPolitics = r.createCell(14);
	    	cPolitics.setCellValue(stu.getZzmm());
	    	
	    	HSSFCell cIdCard = r.createCell(15);
	    	cIdCard.setCellValue(stu.getSfz());
	    	
	    	HSSFCell cFather = r.createCell(16);
	    	cFather.setCellValue(stu.getFqxm());
	    	
	    	HSSFCell cFatherCompany = r.createCell(17);
	    	cFatherCompany.setCellValue(stu.getFqdw());
	    	
	    	HSSFCell cMother = r.createCell(18);
	    	cMother.setCellValue(stu.getMqxm());
	    	
	    	HSSFCell cMotherCompany = r.createCell(19);
	    	cMotherCompany.setCellValue(stu.getMqdw());
	    	
	    	HSSFCell cStuSource = r.createCell(20);
	    	cStuSource.setCellValue(stu.getSy());
	    }
	    logger.info("excelDir=" + excelDir);
	    logger.info("file=" + filename+".xls");
	    File f = new File(excelDir, filename+".xls");
	    logger.info("saving file " + f.getAbsolutePath());
	    workbook.write(new FileOutputStream(f));
	    logger.info("save file success.");
	    return f.getAbsolutePath();
	}

	private IStudentService getService() {
		if (studentService == null) {
			studentService = AppContext.getBean("studentService", IStudentService.class);
		}
		return studentService;
	}
	
	private ISchoolService getSchoolService() {
		if (schoolService == null) {
			schoolService = AppContext.getBean("schoolService", ISchoolService.class);
		}
		return schoolService;
	}

	private ICheckResultService getCheckResultService() {
		if (checkResultService == null) {
			checkResultService = AppContext.getBean("checkResultService", ICheckResultService.class);
		}
		return checkResultService;
	}

	private ISqlService getSqlService() {
		if (sqlService == null) {
			sqlService = AppContext.getBean("sqlService", ISqlService.class);
		}
		return sqlService;
	}

	public void refreshList() {
		refreshList(false);
	}

	public void refreshList(boolean withKeyword) {
		DefaultTableModel tblModel = (DefaultTableModel) tblStudent.getModel();
		int rowCount = tblModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			tblModel.removeRow(0);
		}
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeSchool.getLastSelectedPathComponent();
		SchoolTreeNode schoolTreeNode = treeNode == null ? null : (SchoolTreeNode) treeNode.getUserObject();
		if (schoolTreeNode == null || (schoolTreeNode.getType() != SchoolTreeNode.TYPE_CLASS && !withKeyword)) {
			return;
		}
		String classLongNumber = schoolTreeNode == null ? "" : schoolTreeNode.getLongNumber();
		String field = "";
		String keyword = "";
		if (withKeyword) {
			logger.debug("queryField: " + stuFieldsRMap.get(cbFields.getSelectedItem()) + "=" + txtQuery.getText());
			field = stuFieldsRMap.get(cbFields.getSelectedItem().toString());
			keyword = txtQuery.getText();
			// 对于枚举字段，在搜索前要对关键字进行转换
			if ("status".equalsIgnoreCase(field)) {
				keyword = StudentStatusEnum.valueOf(keyword).getValue();
			}
			//			else if("xx".equalsIgnoreCase(field)) {
			//				keyword = BloodTypeEnum.valueOf(keyword).getValue();
			//			}

		}
		List<Object[]> ls = getService().getStudentAndSchoolByClass(classLongNumber, true, field, keyword);
		int stuCount = ls.size();
		int boyCount = 0;
		int girlCount = 0;
		for (Object[] item : ls) {
			Student stu = (Student) item[0];
			SchoolTreeNode school = (SchoolTreeNode) item[1];
			MyRow row = tblStudent.addRow();
			row.setValue("ID", stu.getId());
			row.setValue("学校编号", school.getCode());
			row.setValue("学校名称", school.getName());
			row.setValue("班级", stu.getClassName());
			row.setValue("学生代码", stu.getStudentCode());
			row.setValue("名字", stu.getName());
			String sex = stu.getSex();
			if("男".equals(sex)) {
				boyCount++;
			}
			row.setValue("性别", sex);
			row.setValue("出生日期", sdf.format(stu.getBornDate()));
			row.setValue("血型", stu.getXx());
			row.setValue("学籍号", stu.getXjh());
		}
		girlCount = stuCount - boyCount;
		initPopupMenu();
		lblTblStat.setText("学生共 " + stuCount + " 人, 其中，男生 " + boyCount + " 人， 女生 " + girlCount + " 人。");
		//        treeSchool.addMouseListener(this);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jToolBar2 = new javax.swing.JToolBar();
		btnAdd = new javax.swing.JButton();
		btnEdit = new javax.swing.JButton();
		btnRemove = new javax.swing.JButton();
		btnUpgrade = new javax.swing.JButton();
		chkDisplayMergedSchool = new javax.swing.JCheckBox();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		txtQuery = new javax.swing.JTextField();
		cbFields = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		btnQuery = new javax.swing.JButton();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblStudent = new com.vastcm.swing.jtable.MyTable();

		setLayout(new java.awt.BorderLayout());

		jToolBar2.setFloatable(false);
		jToolBar2.setRollover(true);

		btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_add.png"))); // NOI18N
		btnAdd.setText("新增");
		btnAdd.setFocusable(false);
		btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAddActionPerformed(evt);
			}
		});
		jToolBar2.add(btnAdd);

		btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_edit.png"))); // NOI18N
		btnEdit.setText("编辑");
		btnEdit.setFocusable(false);
		btnEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnEdit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnEditActionPerformed(evt);
			}
		});
		jToolBar2.add(btnEdit);

		btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_delete.png"))); // NOI18N
		btnRemove.setText("删除");
		btnRemove.setFocusable(false);
		btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnRemove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnRemoveActionPerformed(evt);
			}
		});
		jToolBar2.add(btnRemove);

		btnUpgrade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/top.png"))); // NOI18N
		btnUpgrade.setText("升学");
		btnUpgrade.setFocusable(false);
		btnUpgrade.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnUpgrade.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnUpgrade.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnUpgradeActionPerformed(evt);
			}
		});
		jToolBar2.add(btnUpgrade);

		chkDisplayMergedSchool.setText("显示被合并学校");
		chkDisplayMergedSchool.setFocusable(false);
		chkDisplayMergedSchool.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		chkDisplayMergedSchool.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		chkDisplayMergedSchool.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				chkDisplayMergedSchoolItemStateChanged(evt);
			}
		});
		jToolBar2.add(chkDisplayMergedSchool);

		add(jToolBar2, java.awt.BorderLayout.NORTH);

		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel3.setPreferredSize(new java.awt.Dimension(605, 30));
		jPanel3.setRequestFocusEnabled(false);

		cbFields.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel1.setText("类似于");

		btnQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/system_search.gif"))); // NOI18N
		btnQuery.setText("查询");
		btnQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnQueryActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel3Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(cbFields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(txtQuery, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnQuery)
						.addContainerGap(243, Short.MAX_VALUE)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel3Layout
						.createSequentialGroup()
						.addGroup(
								jPanel3Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(txtQuery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(cbFields, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(btnQuery))
						.addGap(0, 0, Short.MAX_VALUE)));

		jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

		jSplitPane1.setDividerLocation(220);
		jSplitPane1.setDividerSize(2);
		jSplitPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		jSplitPane1.setDoubleBuffered(true);

		tblStudent.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { },
				new String[] { "ID", "学校编号", "学校名称", "班级", "学生代码", "名字", "性别", "出生日期", "血型",
				"学籍号" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, true, true, true, true, true, true, true, true };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(tblStudent);
		tblStudent.getColumnModel().getColumn(0).setPreferredWidth(0);
		tblStudent.getColumnModel().getColumn(1).setPreferredWidth(0);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jScrollPane1, BorderLayout.CENTER);
		jSplitPane1.setRightComponent(panel);
		
		lblTblStat = new JLabel("");
		lblTblStat.setForeground(Color.BLUE);
		lblTblStat.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel.add(lblTblStat, BorderLayout.SOUTH);

		jPanel2.add(jSplitPane1, java.awt.BorderLayout.CENTER);

		add(jPanel2, java.awt.BorderLayout.CENTER);
		
		
	}// </editor-fold>//GEN-END:initComponents

	public SchoolTreeNode getSelectedTreeNode() {
		TreePath path = treeSchool.getSelectionPath();
		if (path == null) {
			JOptionPane.showMessageDialog(this, "请先选择班级！");
			ExceptionUtils.abort();
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		SchoolTreeNode treeNode = (SchoolTreeNode) node.getUserObject();
		return treeNode;
	}

	private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
		SchoolTreeNode treeNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != treeNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		try {
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "学生信息维护");
			uiCtx.set(UIContext.UI_STATUS, "NEW");
			uiCtx.set("owner", this);
			uiCtx.set("classNode", treeNode);
			uiCtx.set("tree", treeSchool);
			UI<StudentPanel> ui = UIFactory.create(StudentPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);

		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex);
		}
	}//GEN-LAST:event_btnAddActionPerformed

	private void chkDisplayMergedSchoolItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDisplayMergedSchoolItemStateChanged
		if (ItemEvent.SELECTED == evt.getStateChange()) {
			schoolTreePanel.setMergedSchoolVisible(true);
		} else {
			schoolTreePanel.setMergedSchoolVisible(false);
		}

	}//GEN-LAST:event_chkDisplayMergedSchoolItemStateChanged

	private void btnQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQueryActionPerformed
		refreshList(true);
	}//GEN-LAST:event_btnQueryActionPerformed

	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
		editStudent();
	}//GEN-LAST:event_btnEditActionPerformed

	private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
		int[] selectedRowIndex = tblStudent.getSelectedRows();
		if (selectedRowIndex.length <= 0) {
			JOptionPane.showMessageDialog(this, "请先选中要删除的记录！");
			return;
		}
		if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this, "确定要删除选中的学生吗？", "确认", JOptionPane.YES_NO_OPTION)) {
			return;
		}
		DefaultTableModel tblModel = (DefaultTableModel) tblStudent.getModel();
		String[] ids = new String[selectedRowIndex.length];
		String[] studentCodes = new String[selectedRowIndex.length];
		for (int i = 0; i < selectedRowIndex.length; i++) {
			ids[i] = (String) tblModel.getValueAt(selectedRowIndex[i], 0);
			studentCodes[i] = (String) tblModel.getValueAt(selectedRowIndex[i], 4);
		}
		SchoolTreeNode treeNode = getSelectedTreeNode();
		String classLongNo = treeNode.getLongNumber();
		int year = GlobalVariables.getGlobalVariables().getYear();

		getCheckResultService().removeByStudentCode(studentCodes, classLongNo); // 删除体检结果
		//		StringBuilder sql1 = new StringBuilder();
		//		Map<String, Object> params1 = new HashMap<String, Object>();
		//		sql1.append(" WHERE 1=1 ");
		//		sql1.append("   AND result.studentCode IN (:studentCode) ");
		//		if(tblModel.getRowCount() > selectedRowIndex.length) {
		//			sql1.append("   AND result.classLongNo = :classLongNo ");
		//			params1.put("classLongNo", classLongNo);
		//			logger.info("param classLongNo=" + classLongNo);
		//		}
		//		else {
		//			sql1.append("   AND result.schoolBh = :schoolBh ");
		//			String schoolBh = SchoolMessage.getSchoolByClassLongNumber(classLongNo).getSchoolCode();
		//			params1.put("schoolBh", schoolBh);
		//			logger.info("param schoolBh=" + schoolBh);
		//		} 
		//		sql1.append("   AND result.schoolBh = :schoolBh ");
		String schoolBh = SchoolMessage.getSchoolByClassLongNumber(classLongNo).getSchoolCode();
		//		params1.put("schoolBh", schoolBh);
		logger.info("param schoolBh=" + schoolBh);
		//		sql1.append("   AND result.tjnd=:tjnd ");
		//		params1.put("tjnd", String.valueOf(year));
		logger.info("param tjnd=" + year);
		//		logger.info("recalculateStatRptByResult condition:\n" + sql1);
		//		getCheckResultService().recalculateStatRptByResult(sql1.toString(), params1, true); // 重算统计报表
		getService().remove(ids); // 删除学生
		getCheckResultService().recalculateStatRptByResult4Delete(schoolBh, year, false); // 重算统计报表
		refreshList();
		JOptionPane.showMessageDialog(this, "删除成功！");
	}//GEN-LAST:event_btnRemoveActionPerformed

	private void btnUpgradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpgradeActionPerformed
		SchoolTreeNode treeNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_SCHOOL != treeNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择学校！");
			return;
		}
		int optionResult = JOptionPane.showConfirmDialog(this, "一键升级，自动把高年级学生自动毕业，低年级学生升一级，是否继续?", "一键升级", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.NO_OPTION == optionResult)
			return;
		String schoolLongNo = treeNode.getLongNumber();
		final String schoolNo = treeNode.getCode();
		String message = getService().isUpgradeble(schoolNo);
		if (message != null) {
			JOptionPane.showMessageDialog(this, message);
			return;
		}
		logger.info("Upgrading School: " + schoolNo + "    " + schoolLongNo);
		LongTimeTaskDialog dlg = LongTimeTaskDialog.getInstance("一键升学", new LongTimeTaskAdapter() {
			@Override
			public Object exec() throws Exception {
				try {
					int count = getService().oneKeyUpgrade(schoolNo);
					JOptionPane.showMessageDialog(StudentListPanel.this, "升级完毕! 共调班 " + count + " 个学生。");
					refreshList();
				} catch (BizRunTimeException e) {
					MessageDialogUtil.showErrorDetail(StudentListPanel.this, e.getMessage(), e.getBizDetailMessage());
				} catch (Exception e) {
					UIHandler.handleException(StudentListPanel.this, logger, e);
				}
				return null;
			}
		});
		dlg.show();
	}//GEN-LAST:event_btnUpgradeActionPerformed

	private void editStudent() {
		int selectedRowIndex = tblStudent.getSelectedRow();
		if (selectedRowIndex == -1) {
			JOptionPane.showMessageDialog(this, "请先选中要编辑的记录！");
			return;
		}
		DefaultTableModel tblModel = (DefaultTableModel) tblStudent.getModel();
		String id = (String) tblModel.getValueAt(selectedRowIndex, 0);
		logger.debug("id is: " + id);
		try {
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "学生信息维护");
			uiCtx.set(UIContext.UI_STATUS, "EDIT");
			uiCtx.set("owner", this);
			uiCtx.set("id", id);
			uiCtx.set("tree", treeSchool);
			UI<StudentPanel> ui = UIFactory.create(StudentPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);

		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnAdd;
	private javax.swing.JButton btnEdit;
	private javax.swing.JButton btnQuery;
	private javax.swing.JButton btnRemove;
	private javax.swing.JButton btnUpgrade;
	private javax.swing.JComboBox cbFields;
	private javax.swing.JCheckBox chkDisplayMergedSchool;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JToolBar jToolBar2;
	private com.vastcm.swing.jtable.MyTable tblStudent;
	private javax.swing.JTextField txtQuery;
	private JPanel panel;
	private JLabel lblTblStat;

	// End of variables declaration//GEN-END:variables

	public void actionPerformed(ActionEvent e) {
		//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSchool.getLastSelectedPathComponent();
		if (menuChangeClassByClass == e.getSource()) {
			SchoolTreePanel destSchool = new SchoolTreePanel();
			destSchool.setToolbarVisible(false);
			destSchool.setMergedSchoolVisible(true);
			SelectorDialog selectorUI = new SelectorDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
			selectorUI.setUI(destSchool);
			selectorUI.setVisible(true);
			logger.info("selected value: " + selectorUI.getValue());
			//  处理调班
			JOptionPane.showMessageDialog(this, "调班完毕");
		}
	}

	private boolean checkDestClassHasStudentAndTip(String classLongNo) {
		boolean isDestClassExistStu = getSqlService().isExist("select * from Student where classLongNumber=?", new Object[] { classLongNo });
		if (isDestClassExistStu) {
			int optionResult = JOptionPane.showConfirmDialog(this, "要调入的班里已经存在学生数据，是否合并?", "是否合并", JOptionPane.YES_NO_OPTION);
			if (JOptionPane.NO_OPTION == optionResult)
				return false;
		}
		return true;
	}

	private void graduateByStudent() {
		//  学生毕业处理
		int optionResult = JOptionPane.showConfirmDialog(this, "学生毕业，此操作不可逆，是否继续?", "毕业", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.NO_OPTION == optionResult)
			return;
		int[] rowIndices = tblStudent.getSelectedRows();
		if (rowIndices == null || rowIndices.length == 0) {
			JOptionPane.showMessageDialog(this, "请选择学生记录！");
			return;
		}

		String[] studentIDs = new String[rowIndices.length];
		for (int i = 0; i < rowIndices.length; i++) {
			studentIDs[i] = (String) tblStudent.getModel().getValueAt(rowIndices[i], 0);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", studentIDs);
		try {
			int count = getService().graduate("where id in (:id)", params);
			JOptionPane.showMessageDialog(this, "毕业处理完毕! 共毕业 " + count + " 个学生。");
			refreshList();
		} catch (BizRunTimeException e) {
			MessageDialogUtil.showErrorDetail(this, e.getMessage(), e.getBizDetailMessage());
		}
	}

	private void graduateByClass() {
		//  班级毕业处理
		int optionResult = JOptionPane.showConfirmDialog(this, "班级毕业，此操作不可逆，是否继续?", "毕业", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.NO_OPTION == optionResult)
			return;
		SchoolTreeNode srcNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != srcNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		String srcClassLongNo = srcNode.getLongNumber();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classLongNumber", srcClassLongNo);
		try {
			int count = getService().graduate("where classLongNumber=:classLongNumber", params);
			JOptionPane.showMessageDialog(this, "毕业处理完毕! 共毕业 " + count + " 个学生。");
			refreshList();
		} catch (BizRunTimeException e) {
			MessageDialogUtil.showErrorDetail(this, e.getMessage(), e.getBizDetailMessage());
		}
	}

	private void changeClassByClass() {
		SchoolTreeNode srcNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != srcNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		String srcSchoolNo = srcNode.getParentCode();
		String srcClassLongNo = srcNode.getLongNumber();
		SchoolTreePanel destSchool = new SchoolTreePanel();
		destSchool.setToolbarVisible(false);
		destSchool.setMergedSchoolVisible(false);
		SelectorDialog selectorUI = new SelectorDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
		selectorUI.setUI(destSchool);
		selectorUI.setVisible(true);
		logger.info("selected value: " + selectorUI.getValue());
		SchoolTreeNode destNode = (SchoolTreeNode) selectorUI.getValue();
		if (destNode == null)
			return;
		if (SchoolTreeNode.TYPE_CLASS != destNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		String destClassLongNo = destNode.getLongNumber();
		if (destClassLongNo.indexOf(srcSchoolNo) == -1) {
			JOptionPane.showMessageDialog(this, "目标班级与原班级不是同一个学校，不允许调班！");
			return;
		}
		//先判断目标班级是否已包含学生
		if (!checkDestClassHasStudentAndTip(destClassLongNo))
			return;

		int count = getService().changeClassByClass(srcClassLongNo, destClassLongNo);
		JOptionPane.showMessageDialog(this, "调班完毕! 共调班 " + count + " 个学生。");
		refreshList();
	}

	private void changeClassByStudent() {
		int[] rowIndices = tblStudent.getSelectedRows();
		if (rowIndices == null || rowIndices.length == 0) {
			JOptionPane.showMessageDialog(this, "请选择学生记录！");
			return;
		}
		String[] stuCodes = new String[rowIndices.length];
		String[] studentIDs = new String[rowIndices.length];
		String[] studentSchoolNos = new String[rowIndices.length];
		for (int i = 0; i < rowIndices.length; i++) {
			stuCodes[i] = tblStudent.getRow(rowIndices[i]).getValue("学生代码").toString(); // 这里有BUG，单元格的值与界面不一致（学生代码）
			studentIDs[i] = (String) tblStudent.getModel().getValueAt(rowIndices[i], 0);
			studentSchoolNos[i] = (String) tblStudent.getModel().getValueAt(rowIndices[i], 1);
		}
		SchoolTreePanel destSchool = new SchoolTreePanel();
		destSchool.setToolbarVisible(false);
		destSchool.setMergedSchoolVisible(false);
		SelectorDialog selectorUI = new SelectorDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
		selectorUI.setUI(destSchool);
		selectorUI.setVisible(true);
		logger.info("selected value: " + selectorUI.getValue());
		SchoolTreeNode destNode = (SchoolTreeNode) selectorUI.getValue();
		if (destNode == null)
			return;
		if (SchoolTreeNode.TYPE_CLASS != destNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		String destClassLongNo = destNode.getLongNumber();
		for (String srcSchoolNo : studentSchoolNos) {
			if (destClassLongNo.indexOf(srcSchoolNo) == -1) {
				JOptionPane.showMessageDialog(this, "目标班级与原班级不是同一个学校，不允许调班！");
				return;
			}
		}
		int count = getService().changeClassByStudent(destClassLongNo, studentIDs);
		JOptionPane.showMessageDialog(this, "调班完毕! 共调班 " + count + " 个学生。");
		refreshList();
	}

	public void mouseClickedOnTblStudent(MouseEvent e) {
		if (tblStudent == e.getSource() && e.getClickCount() == 2) {
			editStudent();
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			pMenu4Student.show(tblStudent, e.getX(), e.getY());
		}
	}

	public void mousePressedOnSchoolTree(MouseEvent e) {
		TreePath path = treeSchool.getPathForLocation(e.getX(), e.getY());
		if (path == null) {
			return;
		}
		treeSchool.setSelectionPath(path);

		refreshList();
		if (e.getButton() == MouseEvent.BUTTON3) {
			pMenu4SchoolTreeNode.show(treeSchool, e.getX(), e.getY());
		}
	}

	@Override
	public void nodeSelectChanged(SchoolTreeNode node) {
		refreshList();
	}
}
