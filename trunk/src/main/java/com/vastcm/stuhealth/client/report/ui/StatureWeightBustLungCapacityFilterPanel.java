package com.vastcm.stuhealth.client.report.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
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
 * 身高体重胸围肺活量，过滤界面
 * @author bob
 * 
 */
public class StatureWeightBustLungCapacityFilterPanel extends CustomCommonFilterPanel {
	public static final String Param_Stat_Item = "Param_Stat_Item";
	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	private ButtonGroup schoolTypeGroup;
	private ButtonGroup genderGroup;
	private JRadioButton radioStatGrade;
	private JPanel statItemPanel;
	private boolean[] isSelected = new boolean[4];
	private ButtonGroup statTypeGroup;

	/**
	 * Create the panel.
	 */
	public StatureWeightBustLungCapacityFilterPanel() {
		//不允许显示班级，会影响过滤条件
		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();
		((SchoolTreeSelectorPopupUI) selSchoolRange.getSelectorPopupUI()).setIsDisplayClass(false);

		JLabel lblSchoolTerm = new JLabel("体检学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		JPanel statTypePanel = new JPanel();
		statTypePanel.setBorder(BorderFactory.createTitledBorder("统计方式"));

		statTypeGroup = new ButtonGroup();
		radioStatGrade = new JRadioButton("按年级");
		radioStatGrade.setSelected(true);
		statTypePanel.add(radioStatGrade);
		statTypeGroup.add(radioStatGrade);

		JRadioButton radioStatAge = new JRadioButton("按年龄");
		statTypePanel.add(radioStatAge);
		statTypeGroup.add(radioStatAge);

		JPanel schoolTypePanel = new JPanel();
		schoolTypePanel.setBorder(BorderFactory.createTitledBorder("学校类型"));
		schoolTypeGroup = FilterCommonAction.createSchoolType();
		FilterCommonAction.addButtonGroup2Panel(schoolTypePanel, schoolTypeGroup);

		JPanel genderPanel = new JPanel();
		genderPanel.setBorder(BorderFactory.createTitledBorder("性别"));
		genderGroup = FilterCommonAction.createGender();
		FilterCommonAction.addButtonGroup2Panel(genderPanel, genderGroup);

		statItemPanel = new JPanel();
		statItemPanel.setBorder(BorderFactory.createTitledBorder("统计项目"));
		JCheckBox checkBox = new JCheckBox("身高");
		checkBox.setSelected(true);
		statItemPanel.add(checkBox);
		checkBox = new JCheckBox("体重");
		checkBox.setSelected(true);
		statItemPanel.add(checkBox);
		checkBox = new JCheckBox("胸围");
		checkBox.setSelected(true);
		statItemPanel.add(checkBox);
		checkBox = new JCheckBox("肺活量");
		checkBox.setSelected(true);
		statItemPanel.add(checkBox);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][200px:n][][]", "[][][]"));
		panel.add(lblSchoolRange, "cell 0 0,alignx left,aligny center");
		panel.add(selSchoolRange, "cell 1 0,growx");
		panel.add(genderPanel, "cell 2 0 1 2");
		panel.add(statTypePanel, "cell 3 0 1 2");
		panel.add(lblSchoolTerm, "cell 0 1,alignx left,aligny center");
		panel.add(cmbSchoolTerm, "cell 1 1,growx");
		panel.add(schoolTypePanel, "cell 0 2 3 1,alignx left,aligny center");
		panel.add(statItemPanel, "cell 3 2,alignx left,aligny center");
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
	}

	/**
	 * 是否按年级统计。如果是，返回true
	 * @return
	 */
	public boolean isStatGrade() {
		return radioStatGrade.isSelected();
	}

	@Override
	public void verifyInput(ActionEvent evt) {
		super.verifyInput(evt);
		if (selSchoolRange.getValue() == null) {
			JOptionPane.showMessageDialog(this, "学校范围输入不能为空，请输入后再做查询。");
			selSchoolRange.requestFocusInWindow();
			SystemUtils.abort();
		}

		Component[] checkBoxArray = statItemPanel.getComponents();
		boolean isOneSelected = false;
		for (int i = 0; i < checkBoxArray.length; i++) {
			isSelected[i] = ((JCheckBox) checkBoxArray[i]).isSelected();
			isOneSelected = isOneSelected || isSelected[i];
		}
		if (!isOneSelected) {
			JOptionPane.showMessageDialog(this, "请至少选择一项统计项目！");
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
		FilterCommonAction.addButtonGroup2FilterParam(param, Param_Stat_Type, statTypeGroup);
		FilterCommonAction.addButtonGroup2FilterParam(param, Param_Gender, genderGroup);
		FilterCommonAction.addButtonGroup2FilterParam(param, Param_School_Type, schoolTypeGroup);
		param.put(Param_Stat_Item, isSelected);
		return param;
	}
}