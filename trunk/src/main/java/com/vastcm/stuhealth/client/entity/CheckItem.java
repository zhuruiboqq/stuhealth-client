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
 *
 * @author house
 */
@Entity
@Table(name="HW_CheckItem")
public class CheckItem extends CoreEntity {
    @Column(name="ItemId")      private int     itemId;
    @Column(name="ItemName")    private String  itemName;
    @Column(name="Status")      private String  status;
    @Column(name="Unit")        private String  unit;
    @Column(name="BanBen")      private int     banBen;
    @Column(name="EnCode")      private String  enCode;
    @Column(name="Sort")        private int     sort;
    @Column(name="Type")        private int     type;
    @Column(name="IsSelected")  private boolean isSelected;
    @Column(name="IsCustom")    private boolean isCustom;
    @Column(name="Fieldname")   private String  fieldname;
    @Column(name="Flag")   		private String  flag;
    

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getBanBen() {
        return banBen;
    }

    public void setBanBen(int banBen) {
        this.banBen = banBen;
    }

    public String getEnCode() {
        return enCode;
    }

    public void setEnCode(String enCode) {
        this.enCode = enCode;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
