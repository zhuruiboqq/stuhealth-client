/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.vastcm.stuhealth.client.framework.AppConstants;

/**
 * 
 * @author house
 */
public class SQLUtils {
	public static String getInStatement(List<String> ls) {
		return getInStatement(ls.toArray());
	}

	public static String getInStatement(Object[] ls) {
		StringBuilder sb = new StringBuilder();
		int size = ls.length;
		for (int i = 0; i < size; i++) {
			if(ls[i] == null) {
				sb.append("null,");
			}
			else {
				sb.append("'").append(ls[i]).append("'").append(",");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		List<String> ls = new ArrayList<String>();
		ls.add("a");
		ls.add("b");
		System.out.println(getInStatement(ls));
		
		Map<String, BigDecimal> m = new HashMap<String, BigDecimal>();
		m.put("a", new BigDecimal("1"));
		m.put("b", null);
		m.put("c", new BigDecimal("3"));
		
		System.out.println(getInStatement(m.values().toArray()));
	}
}
