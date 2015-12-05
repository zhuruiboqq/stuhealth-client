package com.vastcm.stuhealth.client.report.ui;

import javax.swing.JPanel;

import com.vastcm.stuhealth.client.framework.report.ui.FilterBasePanel;

public class CustomCommonFilterPanel extends FilterBasePanel {
	/**
	 * 班级、学校
	 */
	public static final String Param_School_Range = "Param_School_Range";
	/**
	 * 体检学期
	 */
	public static final String Param_School_Term = "Param_School_Term";
	/**
	 * 统计评价标准
	 */
	@Deprecated
	public static final String Param_Standard_Option = "Param_Standard_Option";
	/**
	 * 学校类型：小学、初中、高中
	 */
	public static final String Param_School_Type = "Param_School_Type";
	/**
	 * 统计方式：年级、年龄
	 */
	public static final String Param_Stat_Type = "Param_Stat_Type";
	/**
	 * 性别：男、女
	 */
	public static final String Param_Gender = "Param_Gender";
	/**
	 * 通用过滤条件
	 */
	public static final String Param_Query_Pair = "Param_Query_Pair";

	public CustomCommonFilterPanel() {

	}

	@Override
	public JPanel getFilterPanel() {
		return this;
	}
}