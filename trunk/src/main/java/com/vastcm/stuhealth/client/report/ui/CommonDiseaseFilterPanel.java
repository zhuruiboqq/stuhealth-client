/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.report.ui;

import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.springframework.util.StringUtils;

import com.vastcm.stuhealth.client.framework.DefaultComboBoxItem;
import com.vastcm.stuhealth.client.framework.NameValuePair;
import com.vastcm.swing.selector.JSelectorBox;

/**
 * 常见疾病查询
 * @author bob
 */
public class CommonDiseaseFilterPanel extends CustomCommonFilterPanel {
	public static final String Param_Print_Option = "Param_Print_Option";

	//	private TreeComboBox cmbSchoolRange;
	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	private JComboBox cmbQueryField;
	private JTextField txtQueryValue;
	private JRadioButton radioGDStandard;
	private JRadioButton radioTableFormReport;

	/**
	 * Create the panel.
	 */
	public CommonDiseaseFilterPanel() {
		//		cmbSchoolRange = new TreeComboBox();
		//		cmbSchoolRange.setEditable(false); // combobox searchable only works when combobox is not editable.
		//		TreeComboBoxSearchable treeComboBoxSearchable = new TreeComboBoxSearchable(cmbSchoolRange);
		//		treeComboBoxSearchable.setPopupTimeout(1000);
		//		treeComboBoxSearchable.setRecursive(true);

		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();

		JLabel lblSchoolTerm = new JLabel("体检学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		JPanel standardPanel = new JPanel();
		standardPanel.setBorder(BorderFactory.createTitledBorder("评价标准"));

		ButtonGroup standardGroup = new ButtonGroup();
		radioGDStandard = new JRadioButton("广东标准");
		radioGDStandard.setSelected(true);
		standardPanel.add(radioGDStandard);
		standardGroup.add(radioGDStandard);

		JRadioButton radioQGStandard = new JRadioButton("全国标准");
		standardPanel.add(radioQGStandard);
		standardGroup.add(radioQGStandard);

		JPanel printOptionPanel = new JPanel();
		printOptionPanel.setBorder(BorderFactory.createTitledBorder("打印方式"));

		ButtonGroup printOptionGroup = new ButtonGroup();
		radioTableFormReport = new JRadioButton("表格");
		radioTableFormReport.setSelected(true);
		printOptionPanel.add(radioTableFormReport);
		printOptionGroup.add(radioTableFormReport);

		JRadioButton radioAppraiseReport = new JRadioButton("报告单");
		printOptionPanel.add(radioAppraiseReport);
		printOptionGroup.add(radioAppraiseReport);

		JPanel queryOptionPanel = new JPanel();
		FlowLayout fl_queryOptionPanel = (FlowLayout) queryOptionPanel.getLayout();
		fl_queryOptionPanel.setAlignment(FlowLayout.LEFT);

		JLabel lblQueryOption = new JLabel("查询方式：");
		queryOptionPanel.add(lblQueryOption);

		cmbQueryField = new JComboBox();
		queryOptionPanel.add(cmbQueryField);
		cmbQueryField.addItem(new DefaultComboBoxItem("编号", "BH"));
		cmbQueryField.addItem(new DefaultComboBoxItem("姓名", "XM"));
		cmbQueryField.addItem(new DefaultComboBoxItem("性别", "XB"));
		cmbQueryField.addItem(new DefaultComboBoxItem("学籍号", "XH"));

		txtQueryValue = new JTextField();
		queryOptionPanel.add(txtQueryValue);
		txtQueryValue.setColumns(10);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][][][]", "[][][]"));
		panel.add(lblSchoolRange, "cell 0 0,aligny center");
		//		panel.add(cmbSchoolRange, "cell 1 0");
		panel.add(selSchoolRange, "cell 1 0,growx");
		panel.add(standardPanel, "cell 2 0 1 2");
		panel.add(printOptionPanel, "cell 3 0 1 2");
		panel.add(lblSchoolTerm, "cell 0 1,alignx left,aligny center");
		panel.add(cmbSchoolTerm, "cell 1 1,alignx left,aligny center");
		panel.add(queryOptionPanel, "cell 0 2 4 1,growx,alignx left,aligny center");
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
	}

	/**
	 * 是否打印报告单，如果是，返回true
	 * @return
	 */
	public boolean isTableFormReportPrint() {
		return radioTableFormReport.isSelected();
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
		param.put(Param_Standard_Option, radioGDStandard.isSelected() ? "D" : "Q");
		param.put(Param_Print_Option, radioTableFormReport.isSelected() ? "form" : "appraise");
		if (cmbQueryField.getSelectedItem() != null && StringUtils.hasText(txtQueryValue.getText())) {
			NameValuePair pair = new NameValuePair();
			pair.setName(((DefaultComboBoxItem) cmbQueryField.getSelectedItem()).getValue().toString());
			pair.setValue(txtQueryValue.getText());
			pair.setCompareType(NameValuePair.COMPARETYPE_LIKE);
			param.put(Param_Query_Pair, pair);
		}
		return param;
	}
}