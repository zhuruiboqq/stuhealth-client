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
@Table(name="HW_School")
public class School extends CoreEntity {
	
	public static String SCHOOLTYPE_PRIMARY = "小学";
	public static String SCHOOLTYPE_MIDDLE  = "初中";
	public static String SCHOOLTYPE_HIGH    = "高中";
	
	public static final String STATUS_NORMAL = "T";
	
    @Column(name="cityId")          private String 	cityId;
    @Column(name="cityName")        private String      cityName;
    @Column(name="schoolCode")     private String 	schoolCode;
    @Column(name="districtCode")    private String 	districtCode;
    @Column(name="districtName")    private String      districtName;
    @Column(name="address")         private String 	address;
    @Column(name="email")           private String 	email;
    @Column(name="linkMan")         private String 	linkMan;
    @Column(name="name")            private String 	name;
    @Column(name="mobilePhone")     private String 	mobilePhone;
    @Column(name="officePhone")     private String 	officePhone;
    @Column(name="version")         private int	   	version;
    @Column(name="status")          private String 	status;
    @Column(name="createDate")      private Date   	createDate;
    @Column(name="outDate")         private Date   	outDate;
    @Column(name="schoolCharacter") private String 	schoolCharacter;
    @Column(name="schoolType")      private String 	schoolType;
    @Column(name="permit")          private boolean     permit;
    @Column(name="cityTown")        private String 	cityTown;
    @Column(name="remark")          private String 	remark;
    @Column(name="secCode")         private String 	secCode; 
    @Column(name="hostSchoolCode")  private String  hostSchoolCode;
    @Column(name="schoolSystem")	private String  schoolSystem;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
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

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public String getSchoolCharacter() {
        return schoolCharacter;
    }

    public void setSchoolCharacter(String schoolCharacter) {
        this.schoolCharacter = schoolCharacter;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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
	 * @return the hostSchoolCode
	 */
	public String getHostSchoolCode() {
		return hostSchoolCode;
	}

	/**
	 * @param hostSchoolCode the hostSchoolCode to set
	 */
	public void setHostSchoolCode(String hostSchoolCode) {
		this.hostSchoolCode = hostSchoolCode;
	}

	/**
	 * @return the schoolSystem
	 */
	public String getSchoolSystem() {
		return schoolSystem;
	}

	/**
	 * @param schoolSystem the schoolSystem to set
	 */
	public void setSchoolSystem(String schoolSystem) {
		this.schoolSystem = schoolSystem;
	}
    
}
