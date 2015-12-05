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
@Table(name="ItemResult")
public class CheckItemResult extends CoreEntity {
    @Column(name="ItemId")      private String  itemId;
    @Column(name="ItemCode")    private String  itemCode;
    @Column(name="ItemResult")  private String  itemResult;
    @Column(name="FieldMc")     private String  fieldMc;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemResult() {
        return itemResult;
    }

    public void setItemResult(String itemResult) {
        this.itemResult = itemResult;
    }

    public String getFieldMc() {
        return fieldMc;
    }

    public void setFieldMc(String fieldMc) {
        this.fieldMc = fieldMc;
    }
    
    
}
