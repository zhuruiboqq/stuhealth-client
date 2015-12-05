/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 疫苗结果
 * @author house
 */
@Entity
@Table(name="VaccinItem")
public class VaccinItem extends CoreEntity {
    @Column(name="VaccItem")    private String vaccItem;    // 结果名
    @Column(name="VaccItemId")  private String vaccItemId;  // 结果项
    @Column(name="VaccinId")    private String vaccinId;    // 疫苗Id
    @Column(name="FieldMc")     private String  fieldMc;

    public String getVaccItem() {
        return vaccItem;
    }

    public void setVaccItem(String vaccItem) {
        this.vaccItem = vaccItem;
    }

    public String getVaccItemId() {
        return vaccItemId;
    }

    public void setVaccItemId(String vaccItemId) {
        this.vaccItemId = vaccItemId;
    }

    public String getVaccinId() {
        return vaccinId;
    }

    public void setVaccinId(String vaccinId) {
        this.vaccinId = vaccinId;
    }

	/**
	 * @return the fieldMc
	 */
	public String getFieldMc() {
		return fieldMc;
	}

	/**
	 * @param fieldMc the fieldMc to set
	 */
	public void setFieldMc(String fieldMc) {
		this.fieldMc = fieldMc;
	}
    
}
