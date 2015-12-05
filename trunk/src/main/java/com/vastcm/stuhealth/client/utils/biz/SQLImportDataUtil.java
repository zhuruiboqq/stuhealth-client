package com.vastcm.stuhealth.client.utils.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.jasperreports.engine.util.Pair;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.vastcm.stuhealth.client.framework.AppConstants;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.HqlUtil;

public class SQLImportDataUtil {

	/**
	 * @param currentSession
	 * @param datas
	 * @param isUpdateExist
	 * @param table
	 * @param idColumn
	 * @param mustContainColumn
	 * @param logicalColumnKeys
	 * @return 插入记录的id和更新记录的id
	 */
	public static Pair<Set<String>, Set<String>> insertOrUpdateRecord(Session currentSession, List<Map<String, Object>> datas, boolean isUpdateExist,
			String table, String idColumn, String[] mustContainColumn, String[] logicalColumnKeys) {
		Map<String, Object> rowData = datas.get(0);
		//检查是否包含关键列
		for (String columnKey : mustContainColumn) {
			if (!rowData.containsKey(columnKey))
				throw new IllegalArgumentException("import data must contain column '" + columnKey + "'");
		}
		if (rowData.keySet().size() < mustContainColumn.length)
			throw new IllegalArgumentException("import data at least cantain " + (mustContainColumn.length + 1) + "column ");

		StringBuffer updateSql = new StringBuffer(500);
		StringBuffer insertSql = new StringBuffer(500);
		StringBuffer tempSql = new StringBuffer(200);
		insertSql.append("insert into ").append(table).append("(");
		updateSql.append("update ").append(table).append(" set ");
		for (String name : rowData.keySet()) {
			if (!idColumn.equals(name)) {
				updateSql.append(name).append("=:").append(name).append(", ");
			}
			insertSql.append(name).append(", ");
			tempSql.append(":").append(name).append(", ");
		}
		if (!rowData.containsKey(idColumn)) {
			insertSql.append(idColumn).append(", ");
			tempSql.append(":").append(idColumn).append(", ");
		}
		updateSql.setLength(updateSql.length() - 2);
		insertSql.setLength(insertSql.length() - 2);
		tempSql.setLength(tempSql.length() - 2);
		updateSql.append(" where ").append(idColumn).append("=:").append(idColumn);
		insertSql.append(") \n Values(").append(tempSql).append(")");

		Map<String, String> existRowMap = getExistRowMap(currentSession, datas, table, idColumn, logicalColumnKeys, true);

		SQLQuery updateQuery = currentSession.createSQLQuery(updateSql.toString());
		SQLQuery insertQuery = currentSession.createSQLQuery(insertSql.toString());
		Set<String> insertIDSet = new LinkedHashSet<String>(datas.size());
		Set<String> UpdateIDSet = new LinkedHashSet<String>(datas.size());
		String rowKey, idValue;
		for (Map<String, Object> tempRowData : datas) {
			rowKey = "";
			for (String columnKey : logicalColumnKeys) {
				rowKey += getAsString(tempRowData.get(columnKey)) + AppConstants.KEY_WORD_SPLITOR;
			}
			if (existRowMap.containsKey(rowKey)) {
				if (isUpdateExist) {
					//update
					idValue = existRowMap.get(rowKey);
					tempRowData.put(idColumn, idValue);
					HqlUtil.setParas(updateQuery, tempRowData);
					updateQuery.executeUpdate();
					UpdateIDSet.add(idValue);
				}
			} else {
				//insert
				idValue = UUID.randomUUID().toString();
				tempRowData.put(idColumn, idValue);
				HqlUtil.setParas(insertQuery, tempRowData);
				insertQuery.executeUpdate();
				insertIDSet.add(idValue);
			}
		}
		return new Pair<Set<String>, Set<String>>(insertIDSet, UpdateIDSet);
	}

	public static Map<String, String> getExistRowMap(Session currentSession, List<Map<String, Object>> datas, String table, String idColumn,
			String[] logicalColumnKeys, boolean isLogicalColumnMustInput) {
		StringBuffer querySql = new StringBuffer(500);
		querySql.append(" select ").append(idColumn);
		for (String columnKey : logicalColumnKeys) {
			querySql.append(", ").append(columnKey);
		}
		querySql.append(" from ").append(table).append(" \n where ");
		List<Object> queryParam = new ArrayList<Object>(datas.size() * logicalColumnKeys.length);
		int index = 0;
		for (Map<String, Object> tempRowData : datas) {
			index++;
			querySql.append("(");
			for (String columnKey : logicalColumnKeys) {
				querySql.append(columnKey).append("=? and ");
				Object value = tempRowData.get(columnKey);
				if (isLogicalColumnMustInput && value == null)
					throw new IllegalArgumentException("导入数据中，第" + index + "行的" + columnKey + "不能录入为空！");
				queryParam.add(getAsString(value));
			}
			querySql.setLength(querySql.length() - 4);//去掉and
			querySql.append(") \n or ");
		}
		querySql.setLength(querySql.length() - 4);//去掉or
		SQLQuery query = currentSession.createSQLQuery(querySql.toString());
		HqlUtil.setParas(query, queryParam.toArray());
		List queryValueList = query.list();
		int size = queryValueList.size();
		String rowKey;
		Map<String, String> existRowMap = new HashMap<String, String>(size + ((int) size / 2));
		for (int i = 0; i < size; i++) {
			Object[] queryRowData = (Object[]) queryValueList.get(i);
			rowKey = "";
			for (int j = 0; j < logicalColumnKeys.length; j++) {
				rowKey += getAsString(queryRowData[j + 1]) + AppConstants.KEY_WORD_SPLITOR;
			}
			existRowMap.put(rowKey, String.valueOf(queryRowData[0]));
		}
		return existRowMap;
	}

	public static String getAsString(Object obj) {
		if (obj == null)
			return "null";
		if (Date.class.isAssignableFrom(obj.getClass())) {
			return DateUtil.getDateString((Date) obj);
		}
		return String.valueOf(obj);
	}
}