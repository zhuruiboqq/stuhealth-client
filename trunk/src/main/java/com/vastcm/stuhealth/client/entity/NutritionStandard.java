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
 */
@Entity
@Table(name="standard_nutrition")
public class NutritionStandard extends CoreEntity {
    @Column(name="sex")     private String  sex;
    @Column(name="grade")     private int     grade;
    @Column(name="sg")      private int     sg;
    @Column(name="p10")     private BigDecimal  p10;
    @Column(name="p25")     private BigDecimal  p25;
    @Column(name="p75")     private BigDecimal  p75;
    @Column(name="p90")     private BigDecimal  p90;
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
	 * @return the grade
	 */
	public int getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	/**
	 * @return the sg
	 */
	public int getSg() {
		return sg;
	}
	/**
	 * @param sg the sg to set
	 */
	public void setSg(int sg) {
		this.sg = sg;
	}
	/**
	 * @return the p10
	 */
	public BigDecimal getP10() {
		return p10;
	}
	/**
	 * @param p10 the p10 to set
	 */
	public void setP10(BigDecimal p10) {
		this.p10 = p10;
	}
	/**
	 * @return the p25
	 */
	public BigDecimal getP25() {
		return p25;
	}
	/**
	 * @param p25 the p25 to set
	 */
	public void setP25(BigDecimal p25) {
		this.p25 = p25;
	}
	/**
	 * @return the p75
	 */
	public BigDecimal getP75() {
		return p75;
	}
	/**
	 * @param p75 the p75 to set
	 */
	public void setP75(BigDecimal p75) {
		this.p75 = p75;
	}
	/**
	 * @return the p90
	 */
	public BigDecimal getP90() {
		return p90;
	}
	/**
	 * @param p90 the p90 to set
	 */
	public void setP90(BigDecimal p90) {
		this.p90 = p90;
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
