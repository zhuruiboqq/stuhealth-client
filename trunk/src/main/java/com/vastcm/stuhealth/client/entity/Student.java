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
 *
 * @author House
 */
@Entity
@Table(name="Student")
public class Student extends CoreEntity{
//    @Column(name="stYear")              private String stYear;
//    @Column(name="term")                private int    term;
    @Column(name="studentCode")         private String studentCode;
    @Column(name="schoolNo")            private String schoolNo;
    @Column(name="gradeNo")             private String gradeNo;
    @Column(name="classNo")             private String classNo;
    @Column(name="className")           private String className;
    @Column(name="classLongNumber")     private String classLongNumber;
    @Column(name="studentNo")           private String studentNo;
    @Column(name="name")                private String name;
    @Column(name="mobilePhone")         private String mobilePhone;
    @Column(name="studentType")         private String studentType;
    @Column(name="bornDate")            private Date   bornDate;
    @Column(name="nation")              private String nation;
    @Column(name="enterDate")           private Date   enterDate;
    @Column(name="address")             private String address;
    @Column(name="phone")               private String phone;
    @Column(name="status")              private String status;
    @Column(name="sex")                 private String sex;
    @Column(name="zzmm")                private String zzmm;    // 政治面貌
    @Column(name="sy")                  private String sy;      // 生源（乡村，城市)
    @Column(name="xx")                  private String xx;      // 血型
    @Column(name="sfz")                 private String sfz;     // 身份证
    @Column(name="fqxm")                private String fqxm;    // 父亲姓名
    @Column(name="fqdw")                private String fqdw;    // 父亲单位
    @Column(name="mqxm")                private String mqxm;    // 母亲姓名
    @Column(name="mqdw")                private String mqdw;    // 母亲单位
    @Column(name="xjh")                 private String xjh;     // 学籍号
    @Column(name="xh")                  private String xh;      // 学号


    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
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

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getZzmm() {
        return zzmm;
    }

    public void setZzmm(String zzmm) {
        this.zzmm = zzmm;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getXx() {
        return xx;
    }

    public void setXx(String xx) {
        this.xx = xx;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getFqxm() {
        return fqxm;
    }

    public void setFqxm(String fqxm) {
        this.fqxm = fqxm;
    }

    public String getFqdw() {
        return fqdw;
    }

    public void setFqdw(String fqdw) {
        this.fqdw = fqdw;
    }

    public String getMqxm() {
        return mqxm;
    }

    public void setMqxm(String mqxm) {
        this.mqxm = mqxm;
    }

    public String getMqdw() {
        return mqdw;
    }

    public void setMqdw(String mqdw) {
        this.mqdw = mqdw;
    }

    public String getXjh() {
        return xjh;
    }

    public void setXjh(String xjh) {
        this.xjh = xjh;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }
    
}
