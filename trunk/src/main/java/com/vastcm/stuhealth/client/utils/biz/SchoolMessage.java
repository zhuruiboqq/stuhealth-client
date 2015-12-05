/**
 * 
 */
package com.vastcm.stuhealth.client.utils.biz;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 19, 2013
 */
public class SchoolMessage {
	private String schoolName;
	private String schoolCode;
	private String schoolType;
	
	public static SchoolMessage getSchoolMessageByClassLongNumber(String classLongNo) {
		String schBh = classLongNo.substring(0, classLongNo.lastIndexOf("!"));
        schBh = schBh.substring(schBh.lastIndexOf("!")+1);
        SchoolMessage schMsg = new SchoolMessage();
        schMsg.setSchoolCode(schBh);
        School school = AppContext.getBean("schoolService", ISchoolService.class).getByClassLongNumber(classLongNo);
        if(school != null) {
        	schMsg.setSchoolName(school.getName());
        	schMsg.setSchoolType(school.getSchoolType());
        }
        
        return schMsg;
	}
	
	public static School getSchoolByClassLongNumber(String classLongNo) {
		return AppContext.getBean("schoolService", ISchoolService.class).getByClassLongNumber(classLongNo);
	}

	/**
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/**
	 * @return the schoolCode
	 */
	public String getSchoolCode() {
		return schoolCode;
	}

	/**
	 * @param schoolCode the schoolCode to set
	 */
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	/**
	 * @return the schoolType
	 */
	public String getSchoolType() {
		return schoolType;
	}

	/**
	 * @param schoolType the schoolType to set
	 */
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	
	
}
