/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author house
 * @since  2013-11-23
 */
@Entity
@Table(name="standard_bmi")
public class BmiStandard extends CoreEntity {
    @Column(name="sex")     private String  sex;
    @Column(name="age")     private int     age;
    @Column(name="p1")     private BigDecimal  p1;
    @Column(name="p2")     private BigDecimal  p2;
    @Column(name="version") private String version;
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * @return the p1
	 */
	public BigDecimal getP1() {
		return p1;
	}
	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(BigDecimal p1) {
		this.p1 = p1;
	}
	/**
	 * @return the p2
	 */
	public BigDecimal getP2() {
		return p2;
	}
	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(BigDecimal p2) {
		this.p2 = p2;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
    
    
}
