package com.vastcm.stuhealth.client.report.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.stuhealth.client.framework.report.service.ISqlService;
import com.vastcm.stuhealth.client.utils.biz.FixItem4UI;
import com.vastcm.swing.selector.JSelectorBox;

public class FixItemFilterPanel extends CustomCommonFilterPanel {
	public static final String Param_Fix_Item_List = "Param_Fix_Item_List";

	private JSelectorBox selSchoolRange;
	private JComboBox cmbSchoolTerm;
	private JList jListFixItem;

	/**
	 * Create the panel.
	 */
	public FixItemFilterPanel() {
		JLabel lblSchoolRange = new JLabel("选择范围：");
		selSchoolRange = FilterCommonAction.createSchoolRange();

		JLabel lblSchoolTerm = new JLabel("体检学期：");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem();

		JPanel fixItemPanel = new JPanel();
		fixItemPanel.setBorder(new TitledBorder(null, "定性项目", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct hw_checkitem.ItemName, itemResult.FieldMc from itemResult \n");
		sql.append("inner join hw_checkitem on itemResult.FieldMc = hw_checkitem.FieldName \n");
		sql.append("order by hw_checkitem.Sort \n");
		ISqlService sqlService = AppContext.getBean("sqlService", ISqlService.class);
		List<Object[]> resultData = sqlService.query(sql.toString());
		Vector<FixItem4UI> fixItemArray = new Vector<FixItem4UI>();
		for (Object[] obj : resultData) {
			FixItem4UI fixItem = new FixItem4UI();
			fixItem.setName(obj[0].toString());
			fixItem.setCode(obj[1].toString());
			fixItemArray.add(fixItem);
		}

		jListFixItem = new JList(fixItemArray);
		fixItemPanel.add(new JScrollPane(jListFixItem));

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow][200px:n][][]", "[][]"));
		panel.add(lblSchoolRange, "cell 0 0,alignx left,aligny center");
		panel.add(selSchoolRange, "cell 1 0,growx");
		panel.add(fixItemPanel, "cell 2 0 1 2");
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
		if (jListFixItem.getSelectedValues() == null || jListFixItem.getSelectedValues().length == 0) {
			JOptionPane.showMessageDialog(this, "请至少选择一项定性项目，再做查询。");
			jListFixItem.requestFocusInWindow();
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
		if (jListFixItem.getSelectedValues() != null) {
			param.put(Param_Fix_Item_List, jListFixItem.getSelectedValues());
		}
		return param;
	}

}
