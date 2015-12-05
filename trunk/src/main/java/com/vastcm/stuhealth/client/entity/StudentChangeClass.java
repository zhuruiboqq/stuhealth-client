package com.vastcm.stuhealth.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;

@Entity
@Table(name = "StudentChangeClass")
public class StudentChangeClass extends CoreEntity {
	@Column(name = "studentId", nullable = false)
	private String studentId;
	@Column(name = "year")
	private String year;
	@Column(name = "term")
	private int term;
	@Column(name = "schoolNo")
	private String schoolNo;
	@Column(name = "gradeNo")
	private String gradeNo;
	@Column(name = "classNo")
	private String classNo;
	@Column(name = "className")
	private String className;
	@Column(name = "classLongNumber")
	private String classLongNumber;
	@Column(name = "destGradeNo")
	private String destGradeNo;
	@Column(name = "destClassNo")
	private String destClassNo;
	@Column(name = "destClassName")
	private String destClassName;
	@Column(name = "destClassLongNumber")
	private String destClassLongNumber;
	@Column(name = "createTime")
	private Date createTime;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getSchoolNo() {
		return schoolNo;
	}

	public void setSchoolNo(String schoolNo) {
		this.schoolNo = schoolNo;
	}

	public String getGradeNo() {
		return gradeNo;
	}

	public void setGradeNo(String gradeNo) {
		this.gradeNo = gradeNo;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassLongNumber() {
		return classLongNumber;
	}

	public void setClassLongNumber(String classLongNumber) {
		this.classLongNumber = classLongNumber;
	}

	public String getDestGradeNo() {
		return destGradeNo;
	}

	public void setDestGradeNo(String destGradeNo) {
		this.destGradeNo = destGradeNo;
	}

	public String getDestClassNo() {
		return destClassNo;
	}

	public void setDestClassNo(String destClassNo) {
		this.destClassNo = destClassNo;
	}

	public String getDestClassName() {
		return destClassName;
	}

	public void setDestClassName(String destClassName) {
		this.destClassName = destClassName;
	}

	public String getDestClassLongNumber() {
		return destClassLongNumber;
	}

	public void setDestClassLongNumber(String destClassLongNumber) {
		this.destClassLongNumber = destClassLongNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}