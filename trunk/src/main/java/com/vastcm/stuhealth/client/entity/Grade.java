/**
 * 
 */
package com.vastcm.stuhealth.client.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 16, 2013
 */
@Entity
@Table(name="Grade")
public class Grade extends CoreEntity{
	@Column(name="GradeCode")		private String		gradeCode;
	@Column(name="Name")			private String		name;
	@Column(name="ShortName")		private String		shortName;
	/**
	 * @return the gradeCode
	 */
	public String getGradeCode() {
		return gradeCode;
	}
	/**
	 * @param gradeCode the gradeCode to set
	 */
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}
	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	
}
