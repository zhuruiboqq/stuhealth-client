/**
 * 
 */
package com.vastcm.stuhealth.client.entity;

import java.sql.Date;

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
@Table(name="UploadLog")
public class UploadLog extends CoreEntity {
	@Column(name="UploadId")	private String		uploadID;
	@Column(name="YearCode")	private String		year;
	@Column(name="term")		private String		term;
	@Column(name="UpSchoolCode")	private String	schoolCode;
	@Column(name="UploadDate")		private Date	uploadDate;
	@Column(name="UploadUser")		private String	uploadUser;
	/**
	 * @return the uploadID
	 */
	public String getUploadID() {
		return uploadID;
	}
	/**
	 * @param uploadID the uploadID to set
	 */
	public void setUploadID(String uploadID) {
		this.uploadID = uploadID;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
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
	 * @return the uploadDate
	 */
	public Date getUploadDate() {
		return uploadDate;
	}
	/**
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	/**
	 * @return the uploadUser
	 */
	public String getUploadUser() {
		return uploadUser;
	}
	/**
	 * @param uploadUser the uploadUser to set
	 */
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	
	
}
