package com.vastcm.stuhealth.client.report.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

import com.vastcm.stuhealth.client.SchoolTreeSelectorPopupUI;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.swing.selector.JSelectorBox;

/**
 * 学生健康体检统计汇总表（四个表：视力、牙齿、内科、其他），过滤界面
 * @author bob
 * 
 */
public class StudentExamineStatSumFilterPanel extends CustomCommonFilterPanel {
	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	//	private JComboBox cmbQueryField;
	//	private JTextField txtQueryValue;
	private ButtonGroup schoolTypeGroup;
	private JRadioButton radioStatGrade;

	/**
	 * Create the panel.
	 */
	public StudentExamineStatSumFilterPanel() {
		//不允许显示班级，会影响过滤条件
		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();
		((SchoolTreeSelectorPopupUI) selSchoolRange.getSelectorPopupUI()).setIsDisplayClass(false);

		JLabel lblSchoolTerm = new JLabel("体检学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		JPanel schoolTypePanel = new JPanel();
		schoolTypePanel.setBorder(BorderFactory.createTitledBorder("学校类型"));
		schoolTypeGroup = FilterCommonAction.createSchoolType();
		FilterCommonAction.addButtonGroup2Panel(schoolTypePanel, schoolTypeGroup);

		JPanel statTypePanel = new JPanel();
		statTypePanel.setBorder(BorderFactory.createTitledBorder("统计方式"));

		ButtonGroup statTypeGroup = new ButtonGroup();
		radioStatGrade = new JRadioButton("按年级");
		radioStatGrade.setSelected(true);
		statTypePanel.add(radioStatGrade);
		statTypeGroup.add(radioStatGrade);

		JRadioButton radioStatAge = new JRadioButton("按年龄");
		statTypePanel.add(radioStatAge);
		statTypeGroup.add(radioStatAge);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][200px:n][][]", "[][][]"));
		panel.add(lblSchoolRange, "cell 0 0,alignx left,aligny center");
		panel.add(selSchoolRange, "cell 1 0,growx");
		panel.add(schoolTypePanel, "cell 2 0 1 2");
		panel.add(statTypePanel, "cell 3 0 1 2");
		panel.add(lblSchoolTerm, "cell 0 1,alignx left,aligny center");
		panel.add(cmbSchoolTerm, "cell 1 1,growx");
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

	/**
	 * 是否按年级统计。如果是，返回true
	 * @return
	 */
	public boolean isStatGrade() {
		return radioStatGrade.isSelected();
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
		param.put(Param_Stat_Type, radioStatGrade.isSelected() ? "grade" : "age");
		FilterCommonAction.addButtonGroup2FilterParam(param, Param_School_Type, schoolTypeGroup);
		return param;
	}
}