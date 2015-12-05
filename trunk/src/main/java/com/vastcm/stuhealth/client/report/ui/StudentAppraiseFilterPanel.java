package com.vastcm.stuhealth.client.report.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.springframework.util.StringUtils;

import com.vastcm.stuhealth.client.framework.DefaultComboBoxItem;
import com.vastcm.stuhealth.client.framework.NameValuePair;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.swing.selector.JSelectorBox;

/**
 * 个体评价报表，过滤界面
 * @author bob
 * 
 */
public class StudentAppraiseFilterPanel extends CustomCommonFilterPanel {
	public static final String Param_Print_Option = "Param_Print_Option";
	public static final String Param_Print_Remark = "Param_Print_Remark";
	public static final String Param_Print_Feedback = "Param_Print_Feedback";
	public static final String Param_Print_Feedback2 = "Param_Print_Feedback2";
	public static final String Param_Query_Field_Not_Normal = "异常记录";

	//	private TreeComboBox cmbSchoolRange;
	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	private JComboBox cmbQueryField;
	private JTextField txtQueryValue;
	//	private JRadioButton radioGDStandard;
	private JRadioButton radioTableFormReport;
	private JCheckBox cbPrintRemark;
	private JCheckBox cbPrintFeedback;
	private JCheckBox cbPrintFeedback2;

	/**
	 * Create the panel.
	 */
	public StudentAppraiseFilterPanel() {
		//		cmbSchoolRange = new TreeComboBox();
		//		cmbSchoolRange.setEditable(false); // combobox searchable only works when combobox is not editable.
		//		TreeComboBoxSearchable treeComboBoxSearchable = new TreeComboBoxSearchable(cmbSchoolRange);
		//		treeComboBoxSearchable.setPopupTimeout(1000);
		//		treeComboBoxSearchable.setRecursive(true);

		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();

		JLabel lblSchoolTerm = new JLabel("体检学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		//		JPanel standardPanel = new JPanel();
		//		standardPanel.setBorder(BorderFactory.createTitledBorder("评价标准"));
		//
		//		ButtonGroup standardGroup = new ButtonGroup();
		//		radioGDStandard = new JRadioButton("广东标准");
		//		radioGDStandard.setSelected(true);
		//		standardPanel.add(radioGDStandard);
		//		standardGroup.add(radioGDStandard);
		//
		//		JRadioButton radioQGStandard = new JRadioButton("全国标准");
		//		standardPanel.add(radioQGStandard);
		//		standardGroup.add(radioQGStandard);

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
		cmbQueryField.addItem(new DefaultComboBoxItem("异常记录", Param_Query_Field_Not_Normal));
		//		cmbQueryField.addItem(new DefaultComboBoxItem("学籍号", "XH"));

		txtQueryValue = new JTextField();
		queryOptionPanel.add(txtQueryValue);
		txtQueryValue.setColumns(10);

		JPanel msgPanel = new JPanel();
		//		add(msgPanel);
		msgPanel.setLayout(new GridLayout(5, 1, 0, 0));
		msgPanel.add(new JLabel("备注1：符号“-”代表该项目未见异常，符号“●”代表该项目检出异常"));
		msgPanel.add(new JLabel("备注2：在龋齿项目中，符号“-”表示未见龋齿，符号“●”表示检出龋齿。"));
		msgPanel.add(new JLabel("备注3：在视力项目中，符号“-”表示未见异常，符号“●”表示视力低下。"));

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][200px:n][][][grow]", "[grow][][]"));
		panel.add(lblSchoolRange, "cell 0 0,alignx left,aligny center");
		//		panel.add(cmbSchoolRange, "cell 1 0");
		panel.add(selSchoolRange, "cell 1 0,growx");
		//		panel.add(standardPanel, "cell 2 0 1 2");
		panel.add(printOptionPanel, "cell 3 0 1 2");

		JPanel printOption2Panel = new JPanel();
		FlowLayout fl_printOption2Panel = (FlowLayout) printOption2Panel.getLayout();
		fl_printOption2Panel.setAlignment(FlowLayout.LEFT);
		panel.add(printOption2Panel, "cell 4 0,grow");

		cbPrintRemark = new JCheckBox("打印备注");
		printOption2Panel.add(cbPrintRemark);

		cbPrintFeedback = new JCheckBox("信息反馈");
		printOption2Panel.add(cbPrintFeedback);

		cbPrintFeedback2 = new JCheckBox("打印回执");
		printOption2Panel.add(cbPrintFeedback2);
		panel.add(lblSchoolTerm, "cell 0 1,alignx left, aligny center");
		panel.add(cmbSchoolTerm, "cell 1 1,growx");
		panel.add(queryOptionPanel, "cell 0 2 4 1,growx,alignx left,aligny center");
		panel.add(msgPanel, "cell 4 1 1 2,growx,aligny bottom");
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
	 * 是否打印报告单，如果是，返回true
	 * @return
	 */
	public boolean isTableFormReportPrint() {
		return radioTableFormReport.isSelected();
	}

	public boolean isPrintRemark() {
		return cbPrintRemark.isSelected();
	}

	public boolean isPrintFeedback() {
		return cbPrintFeedback.isSelected();
	}

	public boolean isPrintFeedback2() {
		return cbPrintFeedback2.isSelected();
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
		//		param.put(Param_Standard_Option, radioGDStandard.isSelected() ? "D" : "Q");
		param.put(Param_Print_Option, radioTableFormReport.isSelected() ? "form" : "appraise");
		param.put(Param_Print_Remark, cbPrintRemark.isSelected());
		param.put(Param_Print_Feedback, cbPrintFeedback.isSelected());
		param.put(Param_Print_Feedback2, cbPrintFeedback2.isSelected());
		if (cmbQueryField.getSelectedItem() != null) {
			String queryField = ((DefaultComboBoxItem) cmbQueryField.getSelectedItem()).getValue().toString();
			if (Param_Query_Field_Not_Normal.equals(queryField) || StringUtils.hasText(txtQueryValue.getText())) {
				NameValuePair pair = new NameValuePair();
				pair.setName(queryField);
				pair.setValue(txtQueryValue.getText());
				pair.setCompareType(NameValuePair.COMPARETYPE_LIKE);
				param.put(Param_Query_Pair, pair);
			}
		}
		return param;
	}
}