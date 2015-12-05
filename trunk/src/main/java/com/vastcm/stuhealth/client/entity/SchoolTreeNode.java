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
 * @author House
 */
@Entity
@Table(name="SchoolTree")
public class SchoolTreeNode extends CoreEntity{
    public static final int TYPE_CITY       = 10;
    public static final int TYPE_DISTRICT   = 20;
    public static final int TYPE_SCHOOL     = 30;
    //public static final int TYPE_GRADE      = 30;
    public static final int TYPE_CLASS      = 40;
    public static final String STATUS_NORMAL     = "T";
    public static final String STATUS_MERGED     = "F";
    public static final String STATUS_BACKOUT    = "X";
    
    @Column(name="code")
    private String      code;
    
    @Column(name="name")
    private String      name;
    
    @Column(name="parentCode")
    private String      parentCode;
    
    @Column(name="level")
    private int         level;
    
    @Column(name="type")
    private int         type;
    
    @Column(name="status") 
    private String      status; // T:正常 F:合并 X:撤销
    
    @Column(name="longNumber")
    private String      longNumber;

    @Override
    public String toString() {
        return name;
    }
    
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLongNumber() {
        return longNumber;
    }

    public void setLongNumber(String longNumber) {
        this.longNumber = longNumber;
    }
}
