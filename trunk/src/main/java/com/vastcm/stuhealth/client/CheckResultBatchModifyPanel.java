/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.VaccinItem;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.IVaccinItemService;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.utils.biz.CheckResultItemMapping;
import com.vastcm.stuhealth.client.utils.biz.VaccinItemMapping;
import com.vastcm.swing.jtable.CellValueChangeListener;
import com.vastcm.swing.jtable.MyCellEditor;
import com.vastcm.swing.jtable.MyRow;

/**
 *
 * @author house
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CheckResultBatchModifyPanel extends KernelUI {

	private Logger logger = LoggerFactory.getLogger(CheckResultBatchModifyPanel.class);

	private DefaultTableModel tblCheckItemModel;
	private DefaultTableModel tblVaccinModel;
	private final int colIndex_checkItem = 1;
	private final int colIndex_checkItemValue = 3;
	private final int colIndex_isCheckItemUpdated = 4;
	private final int colIndex_checkItemFieldName = 5;
	private final int colIndex_vaccin = 1;
	private final int colIndex_vaccinValue = 2;
	private final int colIndex_isVaccinUpdated = 3;
	private final int colIndex_vaccinFieldName = 4;
	private Map<String, Object> retValue = new TreeMap<String, Object>();
	private boolean isCanceled = false;
	private CheckResultItemMapping checkResultItemMapping = new CheckResultItemMapping();
	private VaccinItemMapping vaccinItemMapping = new VaccinItemMapping();

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

	private boolean isTriggerChangeEvent = true;

	/**
	 * Creates new form CheckResultBatchModifyPanel
	 */
	public CheckResultBatchModifyPanel() {
		initComponents();
	}

	@Override
	public void onLoad() throws Exception {
		super.onLoad();
		initComponentsEx();
	}

	public void initComponentsEx() {
		initCheckItemTable();
		initVaccinTable();
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

	protected void initCheckItemTable() {
		initTeethProblemMap();
		TableColumn colId = tblCheckItem.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		TableColumn updateCol = tblCheckItem.getTableHeader().getColumnModel().getColumn(colIndex_isCheckItemUpdated);
		updateCol.setMinWidth(0);
		updateCol.setMaxWidth(0);
		updateCol.setPreferredWidth(0);
		updateCol.setWidth(0);

		TableColumn fieldName = tblCheckItem.getTableHeader().getColumnModel().getColumn(colIndex_checkItemFieldName);
		fieldName.setMinWidth(0);
		fieldName.setMaxWidth(0);
		fieldName.setPreferredWidth(0);
		fieldName.setWidth(0);

		tblCheckItem.setRowSelectionAllowed(false);
		tblCheckItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ICheckItemService checkItemSrv = AppContext.getBean("checkItemService", ICheckItemService.class);
		List<CheckItem> checkItemLs = checkItemSrv.getSelectedItems();
		for (CheckItem item : checkItemLs) {
			MyRow row = tblCheckItem.addRow();
			row.setValue("ID", item.getFieldname());
			row.setValue("项目名称", item.getItemName());
			row.setValue("单位", item.getUnit());
			row.setValue("ItemFieldName", item.getFieldname());
			row.setValue("是否更新", Boolean.FALSE);

			TeethProblem tp = teethProblemMap.get(item.getFieldname());
			if (tp != null) {
				tp.index = tblCheckItem.getRowCount() - 1;
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

		tblCheckItem.getColumnModel().getColumn(colIndex_checkItemValue)
				.setCellEditor(new MyCellEditor(new JComboBox(), itemResultMap, BigDecimal.class));
		tblCheckItem.validate();

		tblCheckItemModel = (DefaultTableModel) tblCheckItem.getModel();
		tblCheckItemModel.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent tme) {
				if (TableModelEvent.UPDATE == tme.getType() && tme.getColumn() == colIndex_checkItemValue) {
					int colIndex = tme.getColumn();
					int rowIndex = tme.getFirstRow();
					String value = (String) tblCheckItemModel.getValueAt(rowIndex, colIndex_checkItem);
					if (value != null && !value.isEmpty()) {
						tableChanged_tblCheckItem(value, rowIndex, colIndex);
					}
				}
			}
		});

		tblCheckItem.addCellValueChangeListener(new CellValueChangeListener() {

			@Override
			public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
				if (newValue != null) {
					tableValueChanged_checkItem(oldValue, newValue, rowIndex, colIndex);
				}
			}
		});
	}

	protected void tableValueChanged_checkItem(Object oldValue, Object newValue, int rowIndex, int colIndex) {
		if (!isTriggerChangeEvent) {
			return;
		}
		// 重算龋失补牙数
		logger.info("invoke tableValueChanged_checkItem()");
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
			Object v = tblCheckItem.getValueAt(tp.index, colIndex_checkItemValue);
			if (v != null && !v.toString().isEmpty()) {
				isCount = true;
				sum = sum.add(new BigDecimal(v.toString()));
			}
		}
		//		logger.info("QSBNUM=" + sum);
		if (!isCount) {
			sum = null;
		}
		isTriggerChangeEvent = false;
		tblCheckItem.setValueAt(sum, teethNo1.index, colIndex_checkItemValue);
		isTriggerChangeEvent = true;
	}

	public void tableChanged_tblCheckItem(String value, int rowIndex, int colIndex) {
		logger.info("tableChanged_checkItem value: " + value);
		tblCheckItemModel.setValueAt(Boolean.TRUE, rowIndex, colIndex_isCheckItemUpdated);
	}

	protected void initVaccinTable() {
		TableColumn colId = tblVaccin.getTableHeader().getColumnModel().getColumn(0);
		colId.setMinWidth(0);
		colId.setMaxWidth(0);
		colId.setPreferredWidth(0);
		colId.setWidth(0);

		TableColumn updateCol = tblVaccin.getTableHeader().getColumnModel().getColumn(colIndex_isVaccinUpdated);
		updateCol.setMinWidth(0);
		updateCol.setMaxWidth(0);
		updateCol.setPreferredWidth(0);
		updateCol.setWidth(0);

		TableColumn fieldName = tblVaccin.getTableHeader().getColumnModel().getColumn(colIndex_vaccinFieldName);
		fieldName.setMinWidth(0);
		fieldName.setMaxWidth(0);
		fieldName.setPreferredWidth(0);
		fieldName.setWidth(0);

		tblVaccin.setRowSelectionAllowed(false);
		tblVaccin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		IVaccinService vaccinService = AppContext.getBean("vaccinService", IVaccinService.class);
		List<Vaccin> vaccinLs = vaccinService.getSelectedItems();
		for (Vaccin vaccin : vaccinLs) {
			MyRow row = tblVaccin.addRow();
			row.setValue("ID", vaccin.getId());
			row.setValue("项目名称", vaccin.getVaccinName());
			row.setValue("ItemFieldName", vaccin.getFieldname());
			row.setValue("是否更新", Boolean.FALSE);
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

		tblVaccin.getColumnModel().getColumn(colIndex_vaccinValue).setCellEditor(new MyCellEditor(new JComboBox(), vaccinMap, BigDecimal.class));
		tblVaccin.validate();

		tblVaccinModel = (DefaultTableModel) tblVaccin.getModel();
		tblVaccinModel.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent tme) {
				if (TableModelEvent.UPDATE == tme.getType() && tme.getColumn() == colIndex_vaccinValue) {
					int colIndex = tme.getColumn();
					int rowIndex = tme.getFirstRow();
					String value = (String) tblVaccin.getValueAt(rowIndex, colIndex_vaccinValue);
					if (value != null && !value.isEmpty()) {
						tableChanged_tblVaccin(value, rowIndex, colIndex);
					}
				}
			}
		});
	}

	public void tableChanged_tblVaccin(String value, int rowIndex, int colIndex) {
		logger.info("tableChanged_vaccin value: " + value);
		tblVaccin.setValueAt(Boolean.TRUE, rowIndex, colIndex_isVaccinUpdated);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jSplitPane1 = new javax.swing.JSplitPane();
		jPanel1 = new javax.swing.JPanel();
		btnConfirm = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		jSplitPane2 = new javax.swing.JSplitPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblCheckItem = new com.vastcm.swing.jtable.MyTable();
		jScrollPane2 = new javax.swing.JScrollPane();
		tblVaccin = new com.vastcm.swing.jtable.MyTable();

		setPreferredSize(new java.awt.Dimension(610, 400));
		setLayout(new java.awt.BorderLayout());

		jSplitPane1.setDividerLocation(350);
		jSplitPane1.setDividerSize(1);
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		jPanel1.setLayout(null);

		btnConfirm.setText("确定");
		btnConfirm.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnConfirmActionPerformed(evt);
			}
		});
		jPanel1.add(btnConfirm);
		btnConfirm.setBounds(400, 10, 75, 29);

		btnCancel.setText("取消");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});
		jPanel1.add(btnCancel);
		btnCancel.setBounds(500, 10, 75, 29);

		jSplitPane1.setBottomComponent(jPanel1);

		jSplitPane2.setDividerLocation(300);
		jSplitPane2.setDividerSize(2);

		jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("体检项目"));

		tblCheckItem.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "项目名称", "单位", "结果", "是否更新",
				"ItemFieldName" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.Boolean.class, java.lang.String.class };

			boolean[] canEdit = new boolean[] { false, false, false, true, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return canEdit[colIndex];
			}

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});
		jScrollPane1.setViewportView(tblCheckItem);

		jSplitPane2.setTopComponent(jScrollPane1);

		jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("疫苗项目"));

		tblVaccin.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "项目名称", "结果", "是否更新", "ItemFieldName" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class,
					java.lang.String.class };

			boolean[] canEdit = new boolean[] { false, false, true, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return canEdit[colIndex];
			}

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
		});
		jScrollPane2.setViewportView(tblVaccin);

		jSplitPane2.setRightComponent(jScrollPane2);

		jSplitPane1.setTopComponent(jSplitPane2);

		add(jSplitPane1, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		isCanceled = true;
		disposeUI();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
		// 让表格完成编辑事件
		if (tblCheckItem.getCellEditor() != null) {
			tblCheckItem.getCellEditor().stopCellEditing();
		}
		if (tblVaccin.getCellEditor() != null) {
			tblVaccin.getCellEditor().stopCellEditing();
		}

		retValue.clear();
		int checkCount = tblCheckItemModel.getRowCount();
		for (int i = 0; i < checkCount; i++) {
			Boolean isUpdated = (Boolean) tblCheckItemModel.getValueAt(i, colIndex_isCheckItemUpdated);
			if (isUpdated) {
				String key = (String) tblCheckItemModel.getValueAt(i, colIndex_checkItemFieldName);
				Object v = tblCheckItemModel.getValueAt(i, colIndex_checkItemValue);
				if (v == null || v.toString().isEmpty()) {
					continue;
				}
				String value = v.toString();
				if (value != null && checkResultItemMapping.getMappingByAlias(key, value) != null) {
					value = checkResultItemMapping.getMappingByAlias(key, value).getCode();
				}
				retValue.put(key, value);
			}
		}

		int vaccinCount = tblVaccinModel.getRowCount();
		for (int i = 0; i < vaccinCount; i++) {
			Boolean isUpdated = (Boolean) tblVaccinModel.getValueAt(i, colIndex_isVaccinUpdated);
			if (isUpdated) {
				String key = (String) tblVaccinModel.getValueAt(i, colIndex_vaccinFieldName);
				String value = (String) tblVaccinModel.getValueAt(i, colIndex_vaccinValue);
				if (value != null && vaccinItemMapping.getMappingByAlias(key, value) != null) {
					value = vaccinItemMapping.getMappingByAlias(key, value).getCode();
				}
				retValue.put(key, value);
			}
		}
		isCanceled = false;
		disposeUI();
	}//GEN-LAST:event_btnConfirmActionPerformed

	public boolean isCanceled() {
		return isCanceled;
	}

	public Map<String, Object> getValue() {
		return retValue;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnConfirm;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private com.vastcm.swing.jtable.MyTable tblCheckItem;
	private com.vastcm.swing.jtable.MyTable tblVaccin;
	// End of variables declaration//GEN-END:variables
}
