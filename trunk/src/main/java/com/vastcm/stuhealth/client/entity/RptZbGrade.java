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
 * 健康检查统计表1说明
 * @author house
 */
@Entity
@Table(name="Report_ZB_Grade")
public class RptZbGrade extends CoreEntity {
    @Column(name="GuId")                    private Integer     guId;
    @Column(name="RID",     length=30)      private String  rid;
    @Column(name="TJND",    length=30)      private String  tjnd;
    @Column(name="Term",    length=3)       private String  term;
    @Column(name="Grade",   length=60)      private String  grade;
    @Column(name="XB",      length=30)      private String  xb;
    @Column(name="CITYID",  length=60)      private String  cityId;
    @Column(name="DISTRICTID", length=60)   private String  districtId;
    @Column(name="SCHOOLBH", length=60)     private String  schoolBh;
    @Column(name="SchoolType", length=60)   private String  schoolType;
    @Column(name="XS")                      private Integer     xs;
    @Column(name="SJ")                      private Integer     sj;         // 受检人数
    @Column(name="H1")                      private Integer     h1;         // 体检无异常人数
    @Column(name="H1B")                     private BigDecimal  h1B;    // 体检无异常人数百分比
    @Column(name="H2")                      private Integer     h2;         // 体检无异常人数
    @Column(name="H2B")                     private BigDecimal  h2B;    // 体检无异常人数百分比
    @Column(name="H3")                      private Integer     h3;         // 视力4.9人数
    @Column(name="H3B")                     private BigDecimal  h3B;    // 视力4.9人数百分比
    @Column(name="H4")                      private Integer     h4;         // 视力4.6~4.8人数
    @Column(name="H4B")                     private BigDecimal  h4B;    // 
    @Column(name="H5")                      private Integer     h5;         // 视力4.5以下人数
    @Column(name="H5B")                     private BigDecimal  h5B;    // 
    @Column(name="H6")                      private Integer     h6;         // 视力不良合计人数
    @Column(name="H6B")                     private BigDecimal  h6B;    // 
    @Column(name="H7")                      private Integer     h7;         // 新发病人数
    @Column(name="H7B")                     private BigDecimal  h7B;    // 
    @Column(name="H8")                      private Integer     h8;         // 色盲色弱人数
    @Column(name="H8B")                     private BigDecimal  h8B;    // 
    @Column(name="H9")                      private Integer     h9;         // 沙眼人数
    @Column(name="H9B")                     private BigDecimal  h9B;    // 
    @Column(name="H10")                      private Integer     h10;         // 结膜炎人数
    @Column(name="H10B")                     private BigDecimal  h10B;    // 
    @Column(name="H11")                      private Integer     h11;         // 历年新发病人数
    @Column(name="H11B")                     private BigDecimal  h11B;    // 
    @Column(name="H12")                      private Integer     h12;         // 
    @Column(name="H12B")                     private BigDecimal  h12B;    // 
    @Column(name="H13")                      private Integer     h13;         // 
    @Column(name="H13B")                     private BigDecimal  h13B;    // 
    @Column(name="H14")                      private Integer     h14;         // 
    @Column(name="H14B")                     private BigDecimal  h14B;    // 
    @Column(name="H15")                      private Integer     h15;         // 
    @Column(name="H15B")                     private BigDecimal  h15B;    // 
    @Column(name="H16")                      private Integer     h16;         // 
    @Column(name="H16B")                     private BigDecimal  h16B;    // 
    @Column(name="H17")                      private Integer     h17;         // 
    @Column(name="H17B")                     private BigDecimal  h17B;    // 
    @Column(name="H18")                      private Integer     h18;         // 
    @Column(name="H18B")                     private BigDecimal  h18B;    // 
    @Column(name="H19")                      private Integer     h19;         // 
    @Column(name="H19B")                     private BigDecimal  h19B;    // 
    @Column(name="H20")                      private Integer     h20;         // 
    @Column(name="H20B")                     private BigDecimal  h20B;    // 

    public Integer getGuId() {
        return guId;
    }

    public void setGuId(int guId) {
        this.guId = guId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTjnd() {
        return tjnd;
    }

    public void setTjnd(String tjnd) {
        this.tjnd = tjnd;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getSchoolBh() {
        return schoolBh;
    }

    public void setSchoolBh(String schoolBh) {
        this.schoolBh = schoolBh;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getXs() {
        return xs;
    }

    public void setXs(int xs) {
        this.xs = xs;
    }

    public Integer getSj() {
        return sj;
    }

    public void setSj(int sj) {
        this.sj = sj;
    }

    public Integer getH1() {
        return h1;
    }

    public void setH1(int h1) {
        this.h1 = h1;
    }

    public BigDecimal getH1B() {
        return h1B;
    }

    public void setH1B(BigDecimal h1B) {
        this.h1B = h1B;
    }

    public Integer getH2() {
        return h2;
    }

    public void setH2(int h2) {
        this.h2 = h2;
    }

    public BigDecimal getH2B() {
        return h2B;
    }

    public void setH2B(BigDecimal h2B) {
        this.h2B = h2B;
    }

    public Integer getH3() {
        return h3;
    }

    public void setH3(int h3) {
        this.h3 = h3;
    }

    public BigDecimal getH3B() {
        return h3B;
    }

    public void setH3B(BigDecimal h3B) {
        this.h3B = h3B;
    }

    public Integer getH4() {
        return h4;
    }

    public void setH4(int h4) {
        this.h4 = h4;
    }

    public BigDecimal getH4B() {
        return h4B;
    }

    public void setH4B(BigDecimal h4B) {
        this.h4B = h4B;
    }

    public Integer getH5() {
        return h5;
    }

    public void setH5(int h5) {
        this.h5 = h5;
    }

    public BigDecimal getH5B() {
        return h5B;
    }

    public void setH5B(BigDecimal h5B) {
        this.h5B = h5B;
    }

    public Integer getH6() {
        return h6;
    }

    public void setH6(int h6) {
        this.h6 = h6;
    }

    public BigDecimal getH6B() {
        return h6B;
    }

    public void setH6B(BigDecimal h6B) {
        this.h6B = h6B;
    }

    public Integer getH7() {
        return h7;
    }

    public void setH7(int h7) {
        this.h7 = h7;
    }

    public BigDecimal getH7B() {
        return h7B;
    }

    public void setH7B(BigDecimal h7B) {
        this.h7B = h7B;
    }

    public Integer getH8() {
        return h8;
    }

    public void setH8(int h8) {
        this.h8 = h8;
    }

    public BigDecimal getH8B() {
        return h8B;
    }

    public void setH8B(BigDecimal h8B) {
        this.h8B = h8B;
    }

    public Integer getH9() {
        return h9;
    }

    public void setH9(int h9) {
        this.h9 = h9;
    }

    public BigDecimal getH9B() {
        return h9B;
    }

    public void setH9B(BigDecimal h9B) {
        this.h9B = h9B;
    }

    public Integer getH10() {
        return h10;
    }

    public void setH10(int h10) {
        this.h10 = h10;
    }

    public BigDecimal getH10B() {
        return h10B;
    }

    public void setH10B(BigDecimal h10B) {
        this.h10B = h10B;
    }

    public Integer getH11() {
        return h11;
    }

    public void setH11(int h11) {
        this.h11 = h11;
    }

    public BigDecimal getH11B() {
        return h11B;
    }

    public void setH11B(BigDecimal h11B) {
        this.h11B = h11B;
    }

    public Integer getH12() {
        return h12;
    }

    public void setH12(int h12) {
        this.h12 = h12;
    }

    public BigDecimal getH12B() {
        return h12B;
    }

    public void setH12B(BigDecimal h12B) {
        this.h12B = h12B;
    }

    public Integer getH13() {
        return h13;
    }

    public void setH13(int h13) {
        this.h13 = h13;
    }

    public BigDecimal getH13B() {
        return h13B;
    }

    public void setH13B(BigDecimal h13B) {
        this.h13B = h13B;
    }

    public Integer getH14() {
        return h14;
    }

    public void setH14(int h14) {
        this.h14 = h14;
    }

    public BigDecimal getH14B() {
        return h14B;
    }

    public void setH14B(BigDecimal h14B) {
        this.h14B = h14B;
    }

    public Integer getH15() {
        return h15;
    }

    public void setH15(int h15) {
        this.h15 = h15;
    }

    public BigDecimal getH15B() {
        return h15B;
    }

    public void setH15B(BigDecimal h15B) {
        this.h15B = h15B;
    }

    public Integer getH16() {
        return h16;
    }

    public void setH16(int h16) {
        this.h16 = h16;
    }

    public BigDecimal getH16B() {
        return h16B;
    }

    public void setH16B(BigDecimal h16B) {
        this.h16B = h16B;
    }

    public Integer getH17() {
        return h17;
    }

    public void setH17(int h17) {
        this.h17 = h17;
    }

    public BigDecimal getH17B() {
        return h17B;
    }

    public void setH17B(BigDecimal h17B) {
        this.h17B = h17B;
    }

    public Integer getH18() {
        return h18;
    }

    public void setH18(int h18) {
        this.h18 = h18;
    }

    public BigDecimal getH18B() {
        return h18B;
    }

    public void setH18B(BigDecimal h18B) {
        this.h18B = h18B;
    }

    public Integer getH19() {
        return h19;
    }

    public void setH19(int h19) {
        this.h19 = h19;
    }

    public BigDecimal getH19B() {
        return h19B;
    }

    public void setH19B(BigDecimal h19B) {
        this.h19B = h19B;
    }

    public Integer getH20() {
        return h20;
    }

    public void setH20(int h20) {
        this.h20 = h20;
    }

    public BigDecimal getH20B() {
        return h20B;
    }

    public void setH20B(BigDecimal h20B) {
        this.h20B = h20B;
    }
    
    
}
