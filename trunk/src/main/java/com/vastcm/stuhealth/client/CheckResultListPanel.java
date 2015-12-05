/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIStatusEnum;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.biz.CheckResultItemMapping;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;
import com.vastcm.stuhealth.client.utils.biz.VaccinItemMapping;

/**
 * 
 * @author house
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class CheckResultListPanel extends KernelUI implements SchoolTreeListener {

	private Logger logger = LoggerFactory.getLogger(CheckResultListPanel.class);
	private JTree treeSchool;
	private SchoolTreePanel schoolTreePanel;
	private final int colIndex_studentCode = 1;
	private final int colIndex_studentName = 2;
	private final int colIndex_studentSex = 3;
	private final int colIndex_1stCheckItem = 4;
	private DefaultTableModel tblModel;
	private List<String> checkItemNameLs = new ArrayList<String>();
	private List<String> vaccinNameLs = new ArrayList<String>();
	private List<String> allItemNameLs = new ArrayList<String>();
	private List<String> studentCodeLs = new ArrayList<String>();
	private CheckResultItemMapping checkResultItemMapping = new CheckResultItemMapping();
	private VaccinItemMapping vaccinItemMapping = new VaccinItemMapping();

	/**
	 * Creates new form CheckResultListPanel
	 */
	public CheckResultListPanel() {
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
		initResultTable();
	}

	protected void initResultTable() {
		//        tblResult.setAutoCreateColumnsFromModel(true);
		tblResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblModel = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int i) {
				return String.class;
			}
		};
		tblResult.setModel(tblModel);

		tblResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				mouseClickedOnTblResult(me);
			}
		});

		DefaultTableColumnModel tblColModel = (DefaultTableColumnModel) tblResult.getColumnModel();

		// 必须先通过TableModel.addColumn，一次性把所有Column加完，
		// 再通过TableColumnModel获取TableColumn去做修改。
		// 不然会出现TableColumnModel与TableModel不一致等问题。
		tblModel.addColumn("ID");
		tblModel.addColumn("studentCode");
		tblModel.addColumn("studentName");
		tblModel.addColumn("sex");
		List<CheckItem> chkItemLs = getCheckItemService().getSelectedItems();
		for (int i = 0; i < chkItemLs.size(); i++) {
			CheckItem item = chkItemLs.get(i);
			tblModel.addColumn(item.getItemName());
		}
		List<Vaccin> vaccinLs = getVaccinService().getSelectedItems();
		for (int i = 0; i < vaccinLs.size(); i++) {
			Vaccin item = vaccinLs.get(i);
			tblModel.addColumn(item.getVaccinName());
		}

		TableColumn colId = tblColModel.getColumn(0);
		colId.setHeaderValue("ID");
		colId.setIdentifier("ID");
		colId.setCellRenderer(new DefaultTableCellRenderer());
		colId.setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "A" })));
		colId.setResizable(false);
		colId.setMinWidth(0);
		colId.setPreferredWidth(0);

		TableColumn colStudentCode = tblColModel.getColumn(1);
		colStudentCode.setHeaderValue("学生代码");
		colStudentCode.setIdentifier("studentCode");
		colStudentCode.setCellRenderer(new DefaultTableCellRenderer());
		colStudentCode.setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "AA" })));

		TableColumn colStudentName = tblColModel.getColumn(2);
		colStudentName.setHeaderValue("姓名");
		colStudentName.setIdentifier("studentName");
		colStudentName.setCellRenderer(new DefaultTableCellRenderer());
		colStudentName.setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "BB" })));

		TableColumn colStudentSex = tblColModel.getColumn(3);
		colStudentSex.setHeaderValue("性别");
		colStudentSex.setIdentifier("sex");
		colStudentSex.setCellRenderer(new DefaultTableCellRenderer());
		colStudentSex.setCellEditor(new DefaultCellEditor(new JComboBox(new String[] { "CC" })));

		int chkItemCount = chkItemLs.size();
		for (int i = 0; i < chkItemCount; i++) {
			CheckItem item = chkItemLs.get(i);
			TableColumn col = tblColModel.getColumn(colIndex_1stCheckItem + i);
			col.setHeaderValue(item.getItemName());
			col.setIdentifier(item.getFieldname());
			col.setCellRenderer(new DefaultTableCellRenderer());
			col.setCellEditor(new DefaultCellEditor(new JTextField()));
			checkItemNameLs.add(item.getFieldname());
		}

		allItemNameLs.addAll(checkItemNameLs);

		for (int i = 0; i < vaccinLs.size(); i++) {
			Vaccin item = vaccinLs.get(i);
			TableColumn col = tblColModel.getColumn(colIndex_1stCheckItem + chkItemCount + i);
			col.setHeaderValue(item.getVaccinName());
			col.setIdentifier(item.getFieldname());
			col.setCellRenderer(new DefaultTableCellRenderer());
			col.setCellEditor(new DefaultCellEditor(new JTextField()));
			vaccinNameLs.add(item.getFieldname());
		}

		allItemNameLs.addAll(vaccinNameLs);

		tblResult.setIsLocked(true);
		// refreshList();
	}

	protected ICheckItemService getCheckItemService() {
		return AppContext.getBean("checkItemService", ICheckItemService.class);
	}

	protected IVaccinService getVaccinService() {
		return AppContext.getBean("vaccinService", IVaccinService.class);
	}

	protected IStudentService getStudentService() {
		return AppContext.getBean("studentService", IStudentService.class);
	}

	protected ICheckResultService getCheckResultService() {
		return AppContext.getBean("checkResultService", ICheckResultService.class);
	}

	public void refreshList() {

		int rowCount = tblModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			tblModel.removeRow(0);
		}

		int colCount = tblModel.getColumnCount();
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeSchool.getLastSelectedPathComponent();
		SchoolTreeNode schoolTreeNode = treeNode == null ? null : (SchoolTreeNode) treeNode.getUserObject();
		if (schoolTreeNode == null || schoolTreeNode.getType() != SchoolTreeNode.TYPE_CLASS) {
			return;
		}
		String classLongNumber = schoolTreeNode == null ? "" : schoolTreeNode.getLongNumber();
		List<Student> ls = getStudentService().getStudentsByClass(classLongNumber, true);
		studentCodeLs.clear();
		for (int i = 0; i < ls.size(); i++) {
			Student stu = ls.get(i);
			studentCodeLs.add(stu.getStudentCode());
			Vector v = new Vector(colCount);
			for (int j = 0; j < colCount; j++) {
				v.add("");
			}
			v.setElementAt(stu.getStudentCode(), colIndex_studentCode);
			v.setElementAt(stu.getName(), colIndex_studentName);
			v.setElementAt(stu.getSex(), colIndex_studentSex);
			tblModel.addRow(v);
		}
		GlobalVariables gv = GlobalVariables.getGlobalVariables();
		int year = gv.getYear();
		int term = gv.getTerm();
		logger.info("classLongNumber=" + classLongNumber);
		String schoolCode = SchoolMessage.getSchoolByClassLongNumber(classLongNumber).getSchoolCode();
		Map<String, Object[]> results = getCheckResultService().get(studentCodeLs, allItemNameLs, schoolCode, String.valueOf(year),
				String.valueOf(term));
		//        for(int i = 0; i < results.size(); i++) {
		//            Object[] r = (Object[]) results.get(i);
		//            tblModel.setValueAt(r[0], i, 0); // set id.
		//            for(int j = colIndex_1stCheckItem; j < colCount; j++) {
		//                tblModel.setValueAt(r[j-colIndex_1stCheckItem+1], i, j);
		//            }
		//        }
		rowCount = tblModel.getRowCount();
		logger.info("rowCount: " + rowCount);
		for (int i = 0; i < rowCount; i++) {
			//            logger.info("(" + i + "," + colIndex_studentCode + ")");
			String stuCode = (String) tblModel.getValueAt(i, colIndex_studentCode);
			if (!results.containsKey(stuCode)) {
				continue;
			}
			Object[] r = results.get(stuCode);
			tblModel.setValueAt(r[0], i, 0); // set id.
			for (int j = colIndex_1stCheckItem; j < colCount; j++) {
				String fieldname = allItemNameLs.get(j - colIndex_1stCheckItem);
				Object value = r[j - colIndex_1stCheckItem + 2];
				if (value != null) {
					if (checkResultItemMapping.getMappingByCode(fieldname, value.toString()) != null) {
						value = checkResultItemMapping.getMappingByCode(fieldname, value.toString()).getAlias();
					} else if (vaccinItemMapping.getMappingByCode(fieldname, value.toString()) != null) {
						value = vaccinItemMapping.getMappingByCode(fieldname, value.toString()).getAlias();
					}
				}
				tblModel.setValueAt(value, i, j);
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jToolBar1 = new javax.swing.JToolBar();
		btnAddNew = new javax.swing.JButton();
		btnEdit = new javax.swing.JButton();
		btnBatchModify = new javax.swing.JButton();
		btnRemove = new javax.swing.JButton();
		jSplitPane1 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblResult = new com.vastcm.swing.jtable.MyTable();

		setLayout(new java.awt.BorderLayout());

		jToolBar1.setFloatable(false);
		jToolBar1.setRollover(true);

		btnAddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_add.png"))); // NOI18N
		btnAddNew.setText("新增");
		btnAddNew.setFocusable(false);
		btnAddNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnAddNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnAddNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAddNewActionPerformed(evt);
			}
		});
		jToolBar1.add(btnAddNew);

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
		jToolBar1.add(btnEdit);

		btnBatchModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/application_form_edit.png"))); // NOI18N
		btnBatchModify.setText("批量");
		btnBatchModify.setFocusable(false);
		btnBatchModify.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnBatchModify.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnBatchModify.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnBatchModifyActionPerformed(evt);
			}
		});
		jToolBar1.add(btnBatchModify);

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
		jToolBar1.add(btnRemove);

		add(jToolBar1, java.awt.BorderLayout.PAGE_START);

		jScrollPane1.setViewportView(tblResult);

		jSplitPane1.setRightComponent(jScrollPane1);

		add(jSplitPane1, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private void btnBatchModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchModifyActionPerformed
		SchoolTreeNode treeNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != treeNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		GlobalVariables gv = GlobalVariables.getGlobalVariables();
		String year = String.valueOf(gv.getYear());
		//        String term = String.valueOf(gv.getTerm());
		String term = "1";
		int studentCount = getStudentService().getStudentCountByClass(treeNode.getLongNumber());
		if (studentCount == 0) {
			JOptionPane.showMessageDialog(this, "此班级没有学生，不能做批量修改！");
			return;
		}
		try {
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "批量修改");
			uiCtx.set("owner", this);
			UI<CheckResultBatchModifyPanel> ui = UIFactory.create(CheckResultBatchModifyPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);
			CheckResultBatchModifyPanel uiObj = ui.getUIObject();

			if (!uiObj.isCanceled()) {
				Map<String, Object> map = uiObj.getValue();
				String schoolCode = SchoolMessage.getSchoolByClassLongNumber(treeNode.getLongNumber()).getSchoolCode();
				int updateRows = getCheckResultService().update(studentCodeLs, map, schoolCode, year, term);
				logger.info("updateRows: " + updateRows);

				int updateRows1 = getCheckResultService().updateStudentInfo4Result(treeNode.getLongNumber());
				logger.info("update student info completed. " + updateRows1 + " rows affected.");

				refreshList();

				getCheckResultService().updateEvaluationByClass(treeNode.getCode());

				StringBuilder sql1 = new StringBuilder();
				sql1.append(" WHERE result.classLongNo=:classLongNo ");
				sql1.append(" AND   result.tjnd=:tjnd ");
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("classLongNo", treeNode.getLongNumber());
				params1.put("tjnd", String.valueOf(year));
				getCheckResultService().recalculateStatRptByResult(sql1.toString(), params1);
			}
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}

	}//GEN-LAST:event_btnBatchModifyActionPerformed

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

	private void addNewResult() {
		logger.info(getClass().getName() + " AddNew.");
		SchoolTreeNode treeNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != treeNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		int year = GlobalVariables.getGlobalVariables().getYear();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		logger.info("App year=" + year + "; current year=" + currentYear);
		if (!((year == currentYear && currentMonth >= 9) || (year == (currentYear - 1) && currentMonth < 9))) {
			int confirm = JOptionPane.showConfirmDialog(this, "系统年份与当前时间不一致（系统年份=" + year + ",当前时间=" + currentYear + "年" + currentMonth + "月） 是否继续？",
					"确认", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}
		}
		try {
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "体检结果");
			uiCtx.set(UIContext.UI_STATUS, UIStatusEnum.NEW);
			uiCtx.set(UIContext.UI_OWNER, this);
			uiCtx.set("classNode", treeNode);
			uiCtx.set("tree", treeSchool);
			uiCtx.set("studentCode", getSelectedStudentCode());
			//            uiCtx.set("studentCode", getSelectedStudentCode());
			UI<CheckResultPanel> ui = UIFactory.create(CheckResultPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	public String getSelectedId() {
		return (String) tblModel.getValueAt(tblResult.getSelectedRow(), 0);
	}

	public String getSelectedStudentCode() {
		String stuCode = null;
		if (tblResult.getSelectedRow() != -1) {
			stuCode = (String) tblModel.getValueAt(tblResult.getSelectedRow(), colIndex_studentCode);
		}
		return stuCode;
	}

	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
		openEditUI();
	}//GEN-LAST:event_btnEditActionPerformed

	protected void openEditUI() {
		String id = getSelectedId();
		if (id != null && !id.isEmpty()) {
			editResult();
		} else {
			addNewResult();
		}
	}

	private void editResult() {
		logger.info(getClass().getName() + " Edit.");
		logger.info(getClass().getName() + " AddNew.");
		SchoolTreeNode treeNode = getSelectedTreeNode();
		if (SchoolTreeNode.TYPE_CLASS != treeNode.getType()) {
			JOptionPane.showMessageDialog(this, "请选择班级！");
			return;
		}
		int selectedRowIndex = tblResult.getSelectedRow();
		if (selectedRowIndex == -1) {
			JOptionPane.showMessageDialog(this, "请先选中要编辑的记录！");
			return;
		}
		String id = (String) tblModel.getValueAt(selectedRowIndex, 0);
		logger.info("id is: " + id);
		try {
			UIContext uiCtx = new UIContext();
			uiCtx.set(UIContext.UI_TITLE, "体检结果");
			uiCtx.set(UIContext.UI_STATUS, UIStatusEnum.EDIT);
			uiCtx.set(UIContext.UI_OWNER, this);
			//            SchoolTreeNode treeNode = getSelectedTreeNode();
			uiCtx.set("classNode", treeNode);
			uiCtx.set(UIContext.VO_ID, id);
			uiCtx.set("studentCode", getSelectedStudentCode());
			uiCtx.set("tree", treeSchool);
			UI<CheckResultPanel> ui = UIFactory.create(CheckResultPanel.class, UIType.MODAL, uiCtx, null);
			ui.setVisible(true);

		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
	}

	public void mouseClickedOnTblResult(MouseEvent e) {
		if (tblResult == e.getSource() && e.getClickCount() == 2) {
			openEditUI();
		}
	}

	private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
		int[] selectedRowIndex = tblResult.getSelectedRows();
		if (selectedRowIndex.length <= 0) {
			JOptionPane.showMessageDialog(this, "请先选中要删除的记录！");
			return;
		}
		if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this, "确定要删除此体检结果吗？", "确认", JOptionPane.YES_NO_OPTION)) {
			return;
		}
		String[] ids = new String[selectedRowIndex.length];
		for (int i = 0; i < selectedRowIndex.length; i++) {
			ids[i] = (String) tblModel.getValueAt(selectedRowIndex[i], 0);
		}
		getCheckResultService().remove(ids);
		int year = GlobalVariables.getGlobalVariables().getYear();
		getCheckResultService().recalculateStatRptByResult4Delete(getSelectedTreeNode().getParentCode(), year, false); // 重算统计报表
		refreshList();
	}//GEN-LAST:event_btnRemoveActionPerformed

	private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed
		addNewResult();
	}//GEN-LAST:event_btnAddNewActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnAddNew;
	private javax.swing.JButton btnBatchModify;
	private javax.swing.JButton btnEdit;
	private javax.swing.JButton btnRemove;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JToolBar jToolBar1;
	private com.vastcm.swing.jtable.MyTable tblResult;

	// End of variables declaration//GEN-END:variables

	@Override
	public void nodeSelectChanged(SchoolTreeNode node) {
		refreshList();
	}
}
