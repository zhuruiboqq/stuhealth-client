/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.report;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.framework.report.service.ISqlService;

/**
 * 报表参数，配合ReportBasePanel使用
 * 
 * @author bob
 */
public class RptParamInfo {
	//接口

	private ISqlService iSqlService;
	//全局控制参数
	private boolean isDynamicTable = false;//使用tableModel，不需要此参数
	private boolean isFirstFilter = false;
	//构造报表使用的参数
	private String[] columnNames;
	private int[] columnWidths;
	private int[] sumColumnIndexs;

	private String[][] columnLabels;
	//查询使用的参数
	private String querySQL;
	private Map<String, Object> filterParam;
	private Map<String, Object> hqlParam;

	public RptParamInfo() {
		filterParam = new LinkedHashMap<String, Object>();
	}

	public ISqlService getSqlService() {
		if (iSqlService == null) {
			iSqlService = AppContext.getBean("sqlService", ISqlService.class);
		}
		return iSqlService;
	}

	public void setSqlService(ISqlService iSqlService) {
		this.iSqlService = iSqlService;
	}

	public boolean isIsDynamicTable() {
		return isDynamicTable;
	}

	public void setIsDynamicTable(boolean isDynamicTable) {
		this.isDynamicTable = isDynamicTable;
	}

	public boolean isIsFirstFilter() {
		return isFirstFilter;
	}

	public void setIsFirstFilter(boolean isFirstFilter) {
		this.isFirstFilter = isFirstFilter;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].indexOf("\n") != -1) {
				columnNames[i] = "<html>" + columnNames[i].replaceAll("\n", "<br/>") + "</html>";
			}
		}
		this.columnNames = columnNames;
	}

	public int[] getSumColumnIndexs() {
		return sumColumnIndexs;
	}

	public void setSumColumnIndexs(int[] sumColumnIndexs) {
		this.sumColumnIndexs = sumColumnIndexs;
	}

	public int[] getColumnWidths() {
		return columnWidths;
	}

	public void setColumnWidths(int[] columnWidths) {
		this.columnWidths = columnWidths;
	}

	public String[][] getColumnLabels() {
		return columnLabels;
	}

	public void setColumnLabels(String[][] columnLabels) {
		this.columnLabels = columnLabels;
	}

	public String getQuerySQL() {
		return querySQL;
	}

	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}

	public Map<String, Object> getFilterParam() {
		return filterParam;
	}

	public void setFilterParam(Map<String, Object> filterParam) {
		if (filterParam != null) {
			this.filterParam = filterParam;
		}
	}

	public Map<String, Object> getHqlParam() {
		return hqlParam;
	}

	public void setHqlParam(Map<String, Object> hqlParam) {
		this.hqlParam = hqlParam;
	}
}