/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import java.util.Iterator;

/**
 * 
 * @author house
 */
public class StringUtils {
	public static String notNull(String s) {
		if (s == null) {
			return "";
		} else {
			return s;
		}
	}

	public static String toString(Object[] objs, String splitorStr) {
		if (objs == null || objs.length == 0) {
			return null;
		}
		String splitorStrTemp = splitorStr;
		if (splitorStrTemp == null) {
			splitorStrTemp = "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			sb.append(obj).append(splitorStrTemp);
		}
		sb.setLength(sb.length() - splitorStrTemp.length());
		return sb.toString();
	}

	public static String toString(Iterator objs, String splitorStr) {
		if (objs == null || !objs.hasNext()) {
			return null;
		}
		String splitorStrTemp = splitorStr;
		if (splitorStrTemp == null) {
			splitorStrTemp = "";
		}
		StringBuilder sb = new StringBuilder();
		while (objs.hasNext()) {
			sb.append(objs.next()).append(splitorStrTemp);
		}
		sb.setLength(sb.length() - splitorStrTemp.length());
		return sb.toString();
	}
}
