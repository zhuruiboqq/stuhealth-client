package com.vastcm.stuhealth.client.report.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.service.IStudentChangeClass;
import com.vastcm.stuhealth.client.framework.exception.BizRunTimeException;
import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.report.ui.ReportBasePanel;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.utils.MessageDialogUtil;
import com.vastcm.stuhealth.client.utils.MyTableUtils;

public class StudentChangeClassRptPanel extends ReportBasePanel {

	private IStudentChangeClass studentChangeClassService;

	public StudentChangeClassRptPanel() {
		initComponents();

		setFilterBasePanel(new StudentChangeClassFilterPanel());
		tblMain.setTableStyleProvider(new RowStripeTableStyleProvider());
	}

	@Override
	protected void initRptParamInfo() {
		super.initRptParamInfo();
		rptParamInfo = new RptParamInfo();
		rptParamInfo.setColumnNames(new String[] { "id隐藏", "编号", "姓名", "性别", "原班级", "调入班级", "操作时间" });
	}

	@Override
	protected void initTable(boolean isRebuild) {
		super.initTable(isRebuild);
		MyTableUtils.hiddenColumn(tblMain, 0);
	}

	@Override
	public void beforeQuery(ActionEvent evt) {
		super.beforeQuery(evt);
		Map<String, Object> filterParams = rptParamInfo.getFilterParam();
		StringBuffer sql = new StringBuffer(200);
		sql.append(" select stuCC.id, student.studentCode, student.name, student.sex  \n");
		sql.append("  , stuCC.className, stuCC.destClassName, stuCC.createTime        \n");
		sql.append(" from StudentChangeClass stuCC                      \n");
		sql.append(" inner join student on stuCC.studentID = student.id \n");
		sql.append(" where 1=1 ");
		LinkedHashMap<String, Object> hqlParam = new LinkedHashMap<String, Object>();
		rptParamInfo.setHqlParam(hqlParam);
		FilterCommonAction.processFilter4SchoolRange(filterParams, sql, hqlParam, "stuCC.schoolNo", "stuCC.classNo","stuCC.classLongNumber");
		FilterCommonAction.processFilter4SchoolTerm(filterParams, sql, hqlParam, "stuCC.year", "stuCC.term");
		FilterCommonAction.processDateFilter("stuCC.createTime", sql,
				(Date) filterParams.get(StudentChangeClassFilterPanel.Param_Operate_Start_Time),
				(Date) filterParams.get(StudentChangeClassFilterPanel.Param_Operate_End_Time));
		rptParamInfo.setQuerySQL(sql.toString());
	}

	private void initComponents() {
		initPopupMenu();
	}

	private void initPopupMenu() {
		final JPopupMenu pMenu4StudentRecord = new JPopupMenu();
		JMenuItem cancelOperation = new JMenuItem("取消调班或毕业..");
		cancelOperation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					cancelOperation(ae);
				} catch (Exception e) {
					UIHandler.handleException(StudentChangeClassRptPanel.this, logger, e);
				}
			}
		});
		pMenu4StudentRecord.add(cancelOperation);

		tblMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					pMenu4StudentRecord.show(tblMain, e.getX(), e.getY());
				}
			}

		});
	}

	protected void cancelOperation(ActionEvent ae) throws Exception {
		int optionResult = JOptionPane.showConfirmDialog(this, "取消学生调班或毕业，此操作不可逆，是否继续?", "取消调班或毕业", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.NO_OPTION == optionResult)
			return;
		int[] rowIndices = tblMain.getSelectedRows();
		if (rowIndices.length == 0) {
			JOptionPane.showMessageDialog(this, "请选中要取消调班或毕业的记录！");
			return;
		}
		String[] studentCCIDs = new String[rowIndices.length];
		for (int i = 0; i < rowIndices.length; i++) {
			studentCCIDs[i] = (String) tblMain.getModel().getValueAt(rowIndices[i], 0);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", studentCCIDs);
		if (studentChangeClassService == null) {
			studentChangeClassService = AppContext.getBean("studentChangeClassService", IStudentChangeClass.class);
		}
		try {
			int result = studentChangeClassService.cancelOperation("where id in (:id)", params);
			JOptionPane.showMessageDialog(this, "成功操作人数：" + result);
			actionQuery_ActionPerformed(ae);
		} catch (BizRunTimeException e) {
			MessageDialogUtil.showErrorDetail(this, e.getMessage(), e.getBizDetailMessage());
		}
	}
}