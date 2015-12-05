package com.vastcm.stuhealth.client.report.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.tree.DefaultMutableTreeNode;

import com.vastcm.stuhealth.client.GlobalVariables;
import com.vastcm.stuhealth.client.SchoolTreeSelectorPopupUI;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.framework.NameValuePair;
import com.vastcm.stuhealth.client.framework.Pair;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.biz.SchoolTermItem4UI;
import com.vastcm.swing.selector.JSelectorBox;

public class FilterCommonAction {

	public static JSelectorBox createSchoolRange() {
		JSelectorBox selectorBox = new JSelectorBox(new SchoolTreeSelectorPopupUI());
		selectorBox.getSelectorParam().setEnableMultiSelect(true);
		return selectorBox;
	}

	public static JComboBox createSchoolTrem() {
		return createSchoolTrem(GlobalVariables.getGlobalVariables().getYear());
	}

	public static JComboBox createSchoolTrem(int year) {
		JComboBox cmbSchoolTerm = new JComboBox();
		//		Calendar cal = Calendar.getInstance();
		//		int year = cal.get(Calendar.YEAR);
		//		int term = GlobalVariables.getGlobalVariables().getTerm();
		SchoolTermItem4UI currentTerm = new SchoolTermItem4UI(year, SchoolTermItem4UI.First_Term);
		for (int i = 10; i > 0; i--) {
			cmbSchoolTerm.addItem(new SchoolTermItem4UI(year - i, SchoolTermItem4UI.First_Term));
		}
		cmbSchoolTerm.addItem(currentTerm);
		cmbSchoolTerm.addItem(new SchoolTermItem4UI(year + 1, SchoolTermItem4UI.First_Term));

		//		cmbSchoolTerm.addItem(new SchoolTermItem(year - 1, SchoolTermItem.First_Term));
		//		cmbSchoolTerm.addItem(new SchoolTermItem(year - 1, SchoolTermItem.Second_Term));
		//		if (SchoolTermItem.Second_Term == term) {
		//			cmbSchoolTerm.addItem(new SchoolTermItem(year, SchoolTermItem.First_Term));
		//			cmbSchoolTerm.addItem(currentTerm);
		//		} else {
		//			cmbSchoolTerm.addItem(currentTerm);
		//			cmbSchoolTerm.addItem(new SchoolTermItem(year, SchoolTermItem.Second_Term));
		//		}
		//		cmbSchoolTerm.addItem(new SchoolTermItem(year + 1, SchoolTermItem.First_Term));
		//		cmbSchoolTerm.addItem(new SchoolTermItem(year + 1, SchoolTermItem.Second_Term));

		cmbSchoolTerm.setSelectedItem(currentTerm);
		return cmbSchoolTerm;
	}

	public static ButtonGroup createGender() {
		ButtonGroup genderGroup = new ButtonGroup();
		JRadioButton radioAllGender = new JRadioButton("全部");
		radioAllGender.setSelected(true);
		genderGroup.add(radioAllGender);

		JRadioButton radioMale = new JRadioButton("男");
		genderGroup.add(radioMale);

		JRadioButton radioFemale = new JRadioButton("女");
		genderGroup.add(radioFemale);

		return genderGroup;
	}

	public static void addButtonGroup2Panel(JPanel containPanel, ButtonGroup group) {
		Enumeration<AbstractButton> enumeration = group.getElements();
		while (enumeration.hasMoreElements()) {
			containPanel.add(enumeration.nextElement());
		}
	}

	public static void addButtonGroup2FilterParam(Map<String, Object> param, String filterName, ButtonGroup group) {
		Enumeration<AbstractButton> enumeration = group.getElements();
		while (enumeration.hasMoreElements()) {
			AbstractButton button = enumeration.nextElement();
			if (button.isSelected()) {
				if (!button.getText().equals("全部")) {
					param.put(filterName, button.getText());
				}
				break;
			}
		}
	}

	public static Pair<SchoolTreeNode, DefaultMutableTreeNode> processFilter4SchoolRange(Map<String, Object> filterParams, StringBuffer sql,
			Map<String, Object> hqlParam) {
		return processFilter4SchoolRange(filterParams, sql, hqlParam, "SCHOOLBH", "CLASSBH", "classLongNo");
	}

	public static Pair<SchoolTreeNode, DefaultMutableTreeNode> processFilter4SchoolRange(Map<String, Object> filterParams, StringBuffer sql,
			Map<String, Object> hqlParam, String schoolNoField, String classNoField, String classLongNoField) {
		Object paramValue = filterParams.get(CustomCommonFilterPanel.Param_School_Range);
		Pair<SchoolTreeNode, DefaultMutableTreeNode> pair = new Pair<SchoolTreeNode, DefaultMutableTreeNode>();
		if (paramValue != null) {
			// zrb filter Param_School_Range
			//SCHOOLBH or GRADEBH or CLASSBH
			if (paramValue instanceof Object[]) {
				Object[] objectArray = (Object[]) paramValue;
				boolean isClass = false, isSchool = false, isDistrict = false, isCity = false;
				String[] codeArray = new String[objectArray.length];
				String[] longNumberArray = new String[objectArray.length];
				SchoolTreeNode schoolNode = null;
				for (int i = 0; i < objectArray.length; i++) {
					if (objectArray[i] instanceof SchoolTreeNode) {
						schoolNode = (SchoolTreeNode) objectArray[i];
					} else if (objectArray[i] instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) objectArray[i];
						schoolNode = (SchoolTreeNode) treeNode.getUserObject();
						pair.setValue(treeNode);
					}
					pair.setKey(schoolNode);
					isClass = isClass || SchoolTreeNode.TYPE_CLASS == schoolNode.getType();
					isSchool = isSchool || SchoolTreeNode.TYPE_SCHOOL == schoolNode.getType();
					isDistrict = isDistrict || SchoolTreeNode.TYPE_DISTRICT == schoolNode.getType();
					isCity = isCity || SchoolTreeNode.TYPE_CITY == schoolNode.getType();
					codeArray[i] = schoolNode.getCode();
					longNumberArray[i] = schoolNode.getLongNumber();
				}
				if (isCity) {
					sql.append(" and ").append(schoolNoField).append(" in (");
					sql.append(" select schoolNode.code from SchoolTree cityNode \n");
					sql.append(" left join SchoolTree schoolNode on schoolNode.type = ").append(SchoolTreeNode.TYPE_SCHOOL).append(" \n");
					sql.append("     and schoolNode.longNumber like concat(cityNode.code ,'%') \n");
					sql.append(" where cityNode.code in (:school_or_class_inner_param) )               \n");
				} else if (isDistrict) {
					sql.append(" and ").append(schoolNoField)
							.append(" in ( select code from SchoolTree where parentCode in ( :school_or_class_inner_param))");
				} else if (isClass && isSchool) {
					sql.append(" and (").append(classLongNoField).append(" in (:school_or_class_inner_param) ");
					//					sql.append(" or ").append(schoolNoField).append(" in (:school_or_class_inner_param)) ");
				} else if (isClass) {
					sql.append(" and ").append(classLongNoField).append(" in (:school_or_class_inner_param)");
				} else if (isSchool) {
					sql.append(" and ").append(schoolNoField).append(" in (:school_or_class_inner_param)");
				}
				if (isClass) {
					hqlParam.put("school_or_class_inner_param", longNumberArray);
				} else {
					hqlParam.put("school_or_class_inner_param", codeArray);
				}
				return pair;
			} else {
				SchoolTreeNode schoolNode = null;
				if (paramValue instanceof SchoolTreeNode) {
					schoolNode = (SchoolTreeNode) paramValue;
				} else if (paramValue instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) paramValue;
					schoolNode = (SchoolTreeNode) treeNode.getUserObject();
					pair.setValue(treeNode);
				}
				pair.setKey(schoolNode);
				if (SchoolTreeNode.TYPE_CLASS == schoolNode.getType()) {
					sql.append(" and ").append(classLongNoField).append(" = :school_or_class_inner_param");
					hqlParam.put("school_or_class_inner_param", schoolNode.getLongNumber());
					return pair;
				} else if (SchoolTreeNode.TYPE_SCHOOL == schoolNode.getType()) {
					sql.append(" and ").append(schoolNoField).append(" = :school_or_class_inner_param");
				} else if (SchoolTreeNode.TYPE_DISTRICT == schoolNode.getType()) {
					sql.append(" and ").append(schoolNoField)
							.append(" in ( select code from SchoolTree where parentCode = :school_or_class_inner_param)");
				} else if (SchoolTreeNode.TYPE_CITY == schoolNode.getType()) {
					sql.append(" and ").append(schoolNoField)
							.append(" in ( select code from SchoolTree where longNumber like  :school_or_class_inner_param and type=")
							.append(SchoolTreeNode.TYPE_SCHOOL).append(")");
					hqlParam.put("school_or_class_inner_param", schoolNode.getCode() + "%");
					return pair;
				}
				hqlParam.put("school_or_class_inner_param", schoolNode.getCode());
				return pair;
			}
		}
		return null;
	}

	/**
	 * @param schoolType 可以为空
	 * @param schoolNode 不可以为空
	 * @return
	 */
	public static String getPrintMsg4SchoolRange(String schoolType, SchoolTreeNode schoolNode) {
		if (schoolNode != null) {
			return schoolNode.getName();
		}
		return "";
	}

	/**
	 * 根据学校取得所在市区
	 * @param schoolType 可以为空
	 * @param schoolNode 不可以为空
	 * @return
	 */
	public static String getPrintMsg4SchoolCity(Pair<SchoolTreeNode, DefaultMutableTreeNode> pair) {
		DefaultMutableTreeNode treeNode = pair.getValue();
		SchoolTreeNode schoolNode = pair.getKey();
		if (treeNode != null) {
			SchoolTreeNode districtNode = null;
			SchoolTreeNode cityNode = null;

			DefaultMutableTreeNode districtTreeNode = treeNode;
			districtNode = (SchoolTreeNode) districtTreeNode.getUserObject();
			DefaultMutableTreeNode cityTreeNode = (DefaultMutableTreeNode) districtTreeNode.getParent();
			if (cityTreeNode == null || districtNode.getType() == SchoolTreeNode.TYPE_CITY) {
				return districtNode.getName();
			} else {
				cityNode = (SchoolTreeNode) cityTreeNode.getUserObject();
				while (cityTreeNode.getParent() != null && cityNode.getType() != SchoolTreeNode.TYPE_CITY) {
					districtTreeNode = cityTreeNode;
					cityTreeNode = (DefaultMutableTreeNode) cityTreeNode.getParent();
					cityNode = (SchoolTreeNode) cityTreeNode.getUserObject();
				}
				districtNode = (SchoolTreeNode) districtTreeNode.getUserObject();
				return cityNode.getName() + districtNode.getName();
			}
		}
		if (schoolNode != null) {
			return schoolNode.getName();
		}
		return "";
	}

	public static SchoolTermItem4UI processFilter4SchoolTerm(Map<String, Object> filterParams, StringBuffer sql, Map<String, Object> hqlParam) {
		return processFilter4SchoolTerm(filterParams, sql, hqlParam, "TJND", "Term");
	}

	public static SchoolTermItem4UI processFilter4SchoolTerm(Map<String, Object> filterParams, StringBuffer sql, Map<String, Object> hqlParam,
			String yearField, String termField) {
		Object paramValue = filterParams.get(CustomCommonFilterPanel.Param_School_Term);
		if (paramValue != null) {
			SchoolTermItem4UI item = (SchoolTermItem4UI) paramValue;
			//正式使用的过滤
			sql.append(" and (").append(yearField).append("=:yearField_inner_param and ").append(termField).append("=:termField_inner_param)");
			hqlParam.put("yearField_inner_param", item.getYear());
			hqlParam.put("termField_inner_param", item.getTerm());
			return item;
		}
		return null;
	}

	public static ButtonGroup createSchoolType() {
		ButtonGroup schoolTypeGroup = new ButtonGroup();
		JRadioButton radioAllSchoolType = new JRadioButton("全部");
		radioAllSchoolType.setSelected(true);
		schoolTypeGroup.add(radioAllSchoolType);

		JRadioButton radioXiaoXue = new JRadioButton("小学");
		schoolTypeGroup.add(radioXiaoXue);

		JRadioButton radioChuZhong = new JRadioButton("初中");
		schoolTypeGroup.add(radioChuZhong);

		JRadioButton radioGaoZhong = new JRadioButton("高中");
		schoolTypeGroup.add(radioGaoZhong);

		return schoolTypeGroup;
	}

	public static String processFilter4SchoolType(Map<String, Object> filterParams, StringBuffer sql, Map<String, Object> hqlParam) {
		Object paramValue = filterParams.get(CustomCommonFilterPanel.Param_School_Type);
		if (paramValue != null) {
			sql.append(" and SchoolType=:SchoolType");
			hqlParam.put("SchoolType", paramValue);
			return paramValue.toString();
		}
		return "全部";
	}

	public static String getPrintMsg4SchoolType(String schoolType, SchoolTreeNode schoolNode) {
		if ("全部".equals(schoolType)) {
			return "小学(√ )初中(√ )高中(√ )";
		} else if ("小学".equals(schoolType)) {
			return "小学(√ )初中( )高中( )";
		} else if ("初中".equals(schoolType)) {
			return "小学( )初中(√ )高中( )";
		} else if ("高中".equals(schoolType)) {
			return "小学( )初中( )高中(√ )";
		}
		return "小学(√ )初中(√ )高中(√ )";
	}

	public static void processFilter4Gender(Map<String, Object> filterParams, StringBuffer sql, Map<String, Object> hqlParam) {
		Object paramValue = filterParams.get(CustomCommonFilterPanel.Param_Gender);
		if (paramValue != null) {
			sql.append(" and XB=:XB");
			hqlParam.put("XB", paramValue);
		}
	}

	public static void processFilter4QueryPair(Map<String, Object> filterParams, StringBuffer sql, Map<String, Object> hqlParam) {
		Object paramValue = filterParams.get(CustomCommonFilterPanel.Param_Query_Pair);
		if (paramValue != null) {
			NameValuePair pair = (NameValuePair) paramValue;
			sql.append(" and ").append(pair.getName()).append(" ").append(pair.getCompareType()).append(" :").append(pair.getName());
			String compareValue = pair.getValue().toString();
			if (NameValuePair.COMPARETYPE_LIKE.equals(pair.getCompareType()))
				compareValue = "%" + compareValue + "%";
			hqlParam.put(pair.getName(), compareValue);
		}
	}

	/**
	 * 添加日期过滤条件
	 * 
	 * @param filterName 过滤条件名称，例如：sale.FBizDate
	 * @param sql 查询条件
	 * @param dateFrom 可以为空，空时不添加过滤条件，filterName >= dateFrom
	 * @param dateTo 可以为空，空时不添加过滤条件，filterName < dateTo+1
	 */
	public static void processDateFilter(String filterName, StringBuffer sql, Date dateFrom, Date dateTo) {
		processDateFilter(filterName, sql, dateFrom, dateTo, " and ");
	}

	/**
	 * 添加日期过滤条件
	 * 
	 * @param filterName 过滤条件名称，例如：sale.FBizDate
	 * @param sql 查询条件
	 * @param dateFrom 可以为空，空时不添加过滤条件，filterName >= dateFrom
	 * @param dateTo 可以为空，空时不添加过滤条件，filterName < dateTo+1
	 * @param AndOr 使用 and 或者 or 连接条件，注意前后要加上空格
	 */
	public static void processDateFilter(String filterName, StringBuffer sql, Date dateFrom, Date dateTo, String AndOr) {
		// 根据不同的数据库处理日期过滤
		if (dateFrom != null) {
			if (sql.length() != 0 && AndOr != null)
				sql.append(AndOr);
			sql.append(filterName).append(">='").append(DateUtil.getDateString(dateFrom)).append("'");
		}
		if (dateTo != null) {
			if (dateFrom != null)
				sql.append(" and ");
			else if (sql.length() != 0 && AndOr != null)
				sql.append(AndOr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateTo);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			dateTo = cal.getTime();
			sql.append(filterName).append("<'").append(DateUtil.getDateString(dateTo)).append("'");
		}
	}
}