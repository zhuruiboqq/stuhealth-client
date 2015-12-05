/**
 * 
 */
package com.vastcm.stuhealth.client.utils.biz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vastcm.stuhealth.client.CodeProducer;
import com.vastcm.stuhealth.client.GlobalVariables;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 19, 2013
 */
public class StudentCode {
	public static String getNewCode(String schoolCode, String classNo, Date enterDate) {
		return getNewCodes(schoolCode, classNo, enterDate, 1).get(0);
	}
	
	public static List<String> getNewCodes(String schoolCode, String classNo, Date enterDate, int interval) {
		List<String> codes = new ArrayList<String>();
		int intYear = 0;
		if(enterDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(enterDate);
			intYear = cal.get(Calendar.YEAR);
		}
		else {
			intYear = GlobalVariables.getGlobalVariables().getYear();
		}
		String year = String.valueOf(intYear);
		List<String> waterCodes = CodeProducer.getCodeProducer().getCodes(schoolCode, year, interval);
		for(int i = 1; i <= interval; i++) {
			codes.add(classNo.charAt(0) + year.substring(2) + waterCodes.get(i-1));
		}
		
		return codes;
	}
	
	public static String getWaterCode(String studentCode) {
		return studentCode.substring(studentCode.length() - 5);
	}
}
