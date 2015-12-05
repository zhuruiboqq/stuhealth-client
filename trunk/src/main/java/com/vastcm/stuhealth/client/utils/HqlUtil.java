package com.vastcm.stuhealth.client.utils;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.hibernate.Query;

public class HqlUtil {

	/**格式化成查询数量的语句
	 * @param hql
	 * @return
	 */
	public static String format2CountHql(String hql) {
		String tempHql = hql.toLowerCase();
		if (tempHql.indexOf("select") != -1) {
			hql = "select count(*) " + hql.substring(tempHql.indexOf("from"));
		} else {
			hql = "select count(*) " + hql;
		}
		return hql;
	}
	
	/**设置参数
	 * @param query
	 * @param paras
	 */
	public static void setParas(Query query, Object[] paras) {
		if(paras==null || paras.length==0)return;
		
		for (int i = 0; i < paras.length; i++) {
			Object obj = paras[i];
			if (obj instanceof Timestamp)
				query.setTimestamp(i, (Timestamp) obj);
			else if (obj instanceof Date)
				query.setDate(i, (Date) obj);
			else
				query.setString(i, String.valueOf(obj));
		}
	}
	
	/**设置参数
	 * @param query
	 * @param paraNames
	 * @param paraValues
	 */
	public static void setParas(Query query, String[] paraNames, Object[] paraValues) {
		if(paraNames == null || paraValues == null)return;
		
		if(paraNames.length != paraValues.length)
			throw new IllegalArgumentException("参数名称和参数值个数不相等");
		
		for (int i = 0; i < paraNames.length; i++) {
			Object obj = paraValues[i];
			if (obj instanceof Object[])
				query.setParameterList(paraNames[i], (Object[])obj);
			else if (obj instanceof Collection)
				query.setParameterList(paraNames[i], (Collection) obj);
			else
				query.setParameter(paraNames[i], obj);
		}
	}
	
	/**设置参数
	 * @param query
	 * @param paras
	 */
	public static void setParas(Query query, Map paras) {
		if(paras == null || paras.size() == 0)return;
		
		for (Object key : paras.keySet()) {
			Object obj = paras.get(key);
			if (obj instanceof Object[])
				query.setParameterList(String.valueOf(key), (Object[])obj);
			else if (obj instanceof Collection)
				query.setParameterList(String.valueOf(key), (Collection) obj);
			else
				query.setParameter(String.valueOf(key), obj);
		}
	}
}
