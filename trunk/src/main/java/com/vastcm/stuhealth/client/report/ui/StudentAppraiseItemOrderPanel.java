package com.vastcm.stuhealth.client.report.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.report.ui.StudentAppraiseRptPanel.RptColumn;
import com.vastcm.stuhealth.client.utils.MyTableUtils;
import com.vastcm.swing.jtable.DragDropRowTableUI;

public class StudentAppraiseItemOrderPanel extends KernelUI {
	protected Logger logger;
	public final static String Param_IS_OK = "IS_OK";
	public final static String Param_Column_Order = "Column_Order";
	public final static String Param_Column_Map = "Column_Map";
	public final static String Param_Column_Fixed = "Column_Fixed";
	private JTable tblMain;
	List<String> columnOrder;

	public StudentAppraiseItemOrderPanel() {
		logger = LoggerFactory.getLogger(getClass());

		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		tblMain = new JTable() {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		scrollPane.setViewportView(tblMain);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JButton btnOK = new JButton("确  定");
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnOK_ActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraiseItemOrderPanel.this, logger, e);
				}
			}
		});
		panel.add(btnOK);

		JButton btnCancel = new JButton("取  消");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					btnCancel_ActionPerformed(evt);
				} catch (Exception e) {
					UIHandler.handleException(StudentAppraiseItemOrderPanel.this, logger, e);
				}
			}
		});
		panel.add(btnCancel);

	}

	public void onLoad() throws Exception {
		UIContext uiCtx = getUIContext();
		columnOrder = new ArrayList<String>();
		List<String> columnOrderTemp = (List<String>) uiCtx.get(StudentAppraiseItemOrderPanel.Param_Column_Order);
		Map<String, RptColumn> columnMap = (Map<String, RptColumn>) uiCtx.get(StudentAppraiseItemOrderPanel.Param_Column_Map);
		int Start_Column_Index = (Integer) uiCtx.get(StudentAppraiseItemOrderPanel.Param_Column_Fixed);
		String[][] datas = new String[columnOrderTemp.size() - Start_Column_Index][2];
		for (int i = 0; i < Start_Column_Index; i++) {
			columnOrder.add(columnOrderTemp.get(i));
		}
		for (int i = Start_Column_Index; i < columnOrderTemp.size(); i++) {
			RptColumn rptColumn = columnMap.get(columnOrderTemp.get(i));
			if (rptColumn.isHidden())
				continue;
			datas[i - Start_Column_Index][0] = rptColumn.getColumnGroupName() != null ? rptColumn.getColumnGroupName() : rptColumn.getDisplayName();
			datas[i - Start_Column_Index][1] = columnOrderTemp.get(i);
		}
		tblMain.setModel(new DefaultTableModel(datas, new String[] { "项目", "字段名" }));
		tblMain.setUI(new DragDropRowTableUI());
		MyTableUtils.hiddenColumn(tblMain, 1);
	}

	private void btnOK_ActionPerformed(ActionEvent evt) {
		UIContext uiCtx = getUIContext();
		uiCtx.set(Param_IS_OK, Boolean.TRUE);

		for (int i = 0; i < tblMain.getRowCount(); i++) {
			columnOrder.add(tblMain.getValueAt(i, 1).toString());
		}
		uiCtx.set(StudentAppraiseItemOrderPanel.Param_Column_Order, columnOrder);
		this.disposeUI();
	}

	private void btnCancel_ActionPerformed(ActionEvent evt) {
		UIContext uiCtx = getUIContext();
		uiCtx.set(Param_IS_OK, Boolean.FALSE);
		this.disposeUI();
	}
}