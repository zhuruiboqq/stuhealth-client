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
public enum BloodTypeEnum {
	
	A型("A"),
	B型("B"),
	AB型("AB"),
	O型("O");
	
	private String value;
	private static Map<String, BloodTypeEnum> index = new HashMap<String, BloodTypeEnum>();
	static {
		for(BloodTypeEnum t : BloodTypeEnum.values()) {
			index.put(t.getValue(), t);
		}
	}
	
	public static BloodTypeEnum getBloodType(String value) {
		return index.get(value);
	}
	
	private BloodTypeEnum(String typeCode) {
		this.value = typeCode;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
