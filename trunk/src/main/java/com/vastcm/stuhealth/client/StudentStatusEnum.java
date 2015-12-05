/**
 * 
 */
package com.vastcm.stuhealth.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jul 2, 2013
 */
public enum StudentStatusEnum {
	在学("T"),
	退学("F");
	
	private String value;
	private static Map<String, StudentStatusEnum> index = new HashMap<String, StudentStatusEnum>();
	static {
		for(StudentStatusEnum t : StudentStatusEnum.values()) {
			index.put(t.getValue(), t);
		}
	}
	
	public static StudentStatusEnum getStudentStatus(String value) {
		return index.get(value);
	}
	
	private StudentStatusEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
