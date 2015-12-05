package com.vastcm.stuhealth.client.report.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.stuhealth.client.ui.MyDateChooser;
import com.vastcm.swing.selector.JSelectorBox;
import javax.swing.JTextPane;

public class StudentChangeClassFilterPanel extends CustomCommonFilterPanel {
	public static final String Param_Operate_Start_Time = "Param_Operate_Start_Time";
	public static final String Param_Operate_End_Time = "Param_Operate_End_Time";

	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	private MyDateChooser pkoperateStartTime;
	private MyDateChooser pkoperateEndTime;

	/**
	 * Create the panel.
	 */
	public StudentChangeClassFilterPanel() {
		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();

		JLabel lblSchoolTerm = new JLabel("转换学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		JPanel operateTimePanel = new JPanel();
		operateTimePanel.setBorder(BorderFactory.createTitledBorder("操作时间"));
		FlowLayout fl_operateTimePanel = (FlowLayout) operateTimePanel.getLayout();
		fl_operateTimePanel.setAlignment(FlowLayout.LEFT);

		JLabel lblOperateStartTime = new JLabel("从：");
		pkoperateStartTime = new MyDateChooser();
		JLabel lblOperateEndTime = new JLabel("到：");
		pkoperateEndTime = new MyDateChooser();
		operateTimePanel.add(lblOperateStartTime);
		operateTimePanel.add(pkoperateStartTime);
		operateTimePanel.add(lblOperateEndTime);
		operateTimePanel.add(pkoperateEndTime);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][200px:n][][]", "[][]"));
		panel.add(lblSchoolRange, "cell 0 0,alignx left,aligny center");
		//		panel.add(cmbSchoolRange, "cell 1 0");
		panel.add(selSchoolRange, "cell 1 0,growx");
		//		panel.add(standardPanel, "cell 2 0 1 2");
		panel.add(lblSchoolTerm, "cell 2 0,alignx left,aligny center");
		panel.add(cmbSchoolTerm, "cell 3 0,growx");
		panel.add(operateTimePanel, "cell 0 1 4 1");
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
	}

	@Override
	public void verifyInput(ActionEvent evt) {
		super.verifyInput(evt);
		if (selSchoolRange.getValue() == null) {
			JOptionPane.showMessageDialog(this, "学校范围输入不能为空，请输入后再做查询。");
			selSchoolRange.requestFocusInWindow();
			SystemUtils.abort();
		}
	}

	@Override
	public Map<String, Object> getFilterParam() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		if (selSchoolRange.getValue() != null) {
			param.put(Param_School_Range, selSchoolRange.getValue());
		}
		if (cmbSchoolTerm.getSelectedItem() != null) {
			param.put(Param_School_Term, cmbSchoolTerm.getSelectedItem());
		}
		if (pkoperateStartTime.getDate() != null) {
			param.put(Param_Operate_Start_Time, pkoperateStartTime.getDate());
		}
		if (pkoperateEndTime.getDate() != null) {
			param.put(Param_Operate_End_Time, pkoperateEndTime.getDate());
		}
		return param;
	}

}
