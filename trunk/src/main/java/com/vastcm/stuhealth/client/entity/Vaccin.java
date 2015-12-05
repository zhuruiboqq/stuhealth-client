/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 疫苗。机构可以自定义添加疫苗信息(最多5个)
 * @author house
 */
@Entity
@Table(name="Vaccin")
public class Vaccin extends CoreEntity {
    @Column(name="VaccinId")    private int     vaccinId;
    @Column(name="AcDate")      private Date    acDate;
    @Column(name="VaccinName")  private String  vaccinName;
    @Column(name="State")       private String  state;
    @Column(name="Sort")        private int     sort;
    @Column(name="Remark")      private String  remark;
    @Column(name="ChooseState") private String  chooseState;
    @Column(name="ChooseSort")  private int     chooseSort;
    @Column(name="IsSelected")  private boolean isSelected;
    @Column(name="IsCustom")    private boolean isCustom;
    @Column(name="Fieldname")   private String  fieldname;

    public int getVaccinId() {
        return vaccinId;
    }

    public void setVaccinId(int vaccinId) {
        this.vaccinId = vaccinId;
    }

    public Date getAcDate() {
        return acDate;
    }

    public void setAcDate(Date acDate) {
        this.acDate = acDate;
    }

    public String getVaccinName() {
        return vaccinName;
    }

    public void setVaccinName(String vaccinName) {
        this.vaccinName = vaccinName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChooseState() {
        return chooseState;
    }

    public void setChooseState(String chooseState) {
        this.chooseState = chooseState;
    }

    public int getChooseSort() {
        return chooseSort;
    }

    public void setChooseSort(int chooseSort) {
        this.chooseSort = chooseSort;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isIsCustom() {
        return isCustom;
    }

    public void setIsCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }
    
    
}
