/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;

/**
 *
 * @author house
 */
@Entity
@Table(name="Result")
public class CheckResult extends CoreEntity {
    @Column(name="CHECKID")                private String      checkId;  
    @Column(name="TERM", length=1)                   private String      term;       // 学期
    @Column(name="TJND", length=5)        private String      tjnd;       // 体检年度
    @Column(name="BH", length=32)          private String      bh;         // 编号
    @Column(name="CLASSLONGNO")             private String      classLongNo; // 班级长编码
    @Column(name="SCHOOLBH", length=20)    private String      schoolBh;   // 学校编号
    @Column(name="GRADEBH", length=20)     private String      gradeBh;    // 年级编号
    @Column(name="CLASSBH", length=20)     private String      classBh;    // 班级编号
    @Column(name="RXSJ")        private Date      rxsj;       // 入学时间
    @Column(name="XH", length=20)          private String      xh;         // 学号
    @Column(name="StudentCode", length=200) private String      studentCode;
    @Column(name="SCHOOL_NAME", length=50) private String      schoolName; // 学校名称
	@Column(name="SCHOOLTYPE", length=30)  private String      schoolType;  // 学校类型，字段名与统计表的一样
    @Column(name="GRADE_NAME", length=30)  private String      gradeName;  // 年级名称
    @Column(name="CLASS_NAME", length=30)  private String      className;  // 班级名称
    @Column(name="XM", length=30)          private String      xm;         // 姓名
    @Column(name="XB", length=10)          private String      xb;         // 性别
    @Column(name="CSRQ")        private Date      csrq;       // 出生日期
    @Column(name="MZ", length=20)          private String      mz;         // 民族
    @Column(name="ZZMM", length=20)        private String      zzmm;       // 政治面貌
    @Column(name="SY", length=8)          private String      sy;         // 生源
    @Column(name="TJRQ")        private Date        tjrq;       // 体检日期
    @Column(name="NL")          private Integer         nl;         // 年龄
    @Column(name="MARK", length=100)        private String      mark;       // 备注
    @Column(name="SG")          private BigDecimal  sg;         // 身高
    @Column(name="SGDJQ", length=24)       private String      sgdjq;      // 身高等级（全国标准)
    @Column(name="SGDJD", length=24)       private String      sgdjd;      // 身高等级(广东标准)
    @Column(name="SGBJ", length=24)        private String      sgbj;       // 身高是否正常
    @Column(name="TZ")          private BigDecimal  tz;         // 体重
    @Column(name="TZDJQ", length=24)       private String      tzdjq;      // 体重等级（全国标准)
    @Column(name="TZDJD", length=24)       private String      tzdjd;      // 体重等级(广东标准)
    @Column(name="TZBJ", length=24)        private String      tzbj;       // 体重是否正常
    @Column(name="YYPJ", length=30)        private String      yypj;       // 营养评价
    @Column(name="YYPJX", length=30)       private String      yypjx;      // 营养评价X
    @Column(name="TXPJ", length=30)        private String      txpj;
    @Column(name="FYPJ", length=36)        private String      fypj;
    @Column(name="XW")          private BigDecimal  xw;         // 胸围
    @Column(name="XWDJQ", length=24)       private String      xwdjq;      // 胸围等级（全国标准)
    @Column(name="XWDJD", length=24)       private String      xwdjd;      // 胸围等级(广东标准)
    @Column(name="XWBJ", length=24)        private String      xwbj;       // 胸围是否正常
    @Column(name="FHL")         private BigDecimal  fhl;        // 肺活量
    @Column(name="FHLDJQ", length=24)      private String      fhldjq;     // 肺活量等级（全国标准)
    @Column(name="FHLDJD", length=24)      private String      fhldjd;     // 肺活量等级(广东标准)
    @Column(name="FHLBJ", length=24)       private String      fhlbj;      // 肺活量是否正常
    @Column(name="WL")          private BigDecimal  wl;         // 握力
    @Column(name="WLDJQ", length=24)       private String      wldjq;      // 握力等级（全国标准)
    @Column(name="WLDJD", length=24)       private String      wldjd;      // 握力等级(广东标准)
    @Column(name="WLBJ", length=24)        private String      wlbj;       // 握力是否正常
    @Column(name="MB")          private BigDecimal  mb;         // 脉搏
    @Column(name="MBBJ", length=24)        private String      mbbj;       // 脉搏是否正常
    @Column(name="SSY")         private BigDecimal  ssy;        // 收缩压
    @Column(name="SSYBJ", length=24)       private String      ssybj;      // 收缩压是否正常
    @Column(name="SZY")         private BigDecimal  szy;        // 舒张压
    @Column(name="SZYBJ", length=24)       private String      szybj;      // 舒张压是否正常
    @Column(name="LSL")         private BigDecimal  lsl;        // 左眼视力
    @Column(name="RSL")         private BigDecimal  rsl;        // 右眼视力
    @Column(name="QTYB", length=24)        private String      qtyb;       // 其它眼病
    @Column(name="LQG", length=24)         private String      lqg;        // 左眼屈光不正
    @Column(name="RQG", length=24)         private String      rqg;        // 右眼屈光不正
    @Column(name="LSY", length=24)        private String      lsy;       // 左眼沙眼
    @Column(name="RSY", length=24)         private String      rsy;        // 右眼沙眼
    @Column(name="BS", length=24)         private String      bs;        // 辨色
    @Column(name="TL", length=24)         private String      tl;       // 听力
    @Column(name="XJ", length=24)        private String      xj;       // 嗅觉
    @Column(name="BB", length=24)         private String      bb;        // 鼻病
    @Column(name="QSBNUM")         private Integer      qsbnum;        // 
    @Column(name="RQB")        private Integer      rqb;       // 
    @Column(name="RQS")         private Integer      rqs;        // 
    @Column(name="RQH")         private Integer      rqh;        // 
    @Column(name="HQB")        private Integer      hqb;       // 
    @Column(name="HQS")         private Integer      hqs;        // 
    @Column(name="HQH")         private Integer      hqh;        // 
    @Column(name="YZB", length=24)        private String      yzb;       // 牙周疾病
    @Column(name="BTT", length=24)         private String      btt;        // 扁桃体
    @Column(name="XK", length=24)         private String      xk;        // 胸廓
    @Column(name="JZ", length=24)        private String      jz;       // 脊柱侧弯
    @Column(name="SZ", length=24)         private String      sz;        // 四肢
    @Column(name="PZ", length=24)         private String      pz;        // 平足
    @Column(name="XZ", length=24)        private String      xz;       // 心脏
    @Column(name="GP", length=24)         private String      gp;        // 肝脾
    @Column(name="FEI", length=24)         private String      fei;        // 肺
    @Column(name="XT", length=24)        private String      xt;       // 胸透
    @Column(name="XHDB")         private Integer      xhdb;        // 
    @Column(name="FHCL", length=24)         private String      fhcl;        // 肠道蠕虫
    @Column(name="PFB", length=24)        private String      pfb;       // 皮肤病
    @Column(name="ZG")         private BigDecimal      zg;        // 
    @Column(name="ZGDJQ", length=24)         private String      zgdjq;        // 
    @Column(name="ZGDJD", length=24)        private String      zgdjd;       // 
    @Column(name="ZGBJ", length=24)         private String      zgbj;        // 
    @Column(name="JK")         private BigDecimal      jk;        // 肩宽
    @Column(name="JKDJQ", length=24)        private String      jkdjq;       // 肩宽等级（全国标准)
    @Column(name="JKDJD", length=24)         private String      jkdjd;        // 肩宽等级(广东标准)
    @Column(name="JKBJ", length=24)         private String      jkbj;        // 肩宽是否正常
    @Column(name="GPK")        private BigDecimal      gpk;       // 
    @Column(name="GPKDJQ", length=24)         private String      gpkdjq;        // 
    @Column(name="GPKDJD", length=24)         private String      gpkdjd;        // 
    @Column(name="GPKBJ", length=24)        private String      gpkbj;       // 
    @Column(name="BJL")         private BigDecimal      bjl;        // 
    @Column(name="GSTJ")         private BigDecimal      gstj;        // 
    @Column(name="JJX")        private BigDecimal      jjx;       // 
    @Column(name="BMKY", length=24)         private String      bmky;        // 表面抗原
    @Column(name="SJSR", length=24)         private String      sjsr;        // 神经衰弱
    @Column(name="WSM")        private BigDecimal      wsm;       // 
    @Column(name="WSMDJQ", length=24)         private String      wsmdjq;        // 
    @Column(name="WSMDJD", length=24)         private String      wsmdjd;        // 
    @Column(name="WSMBJ", length=24)        private String      wsmbj;       // 
    @Column(name="LDTY")         private BigDecimal      ldty;        // 
    @Column(name="LDTYDJQ", length=24)         private String      ldtydjq;        // 
    @Column(name="LDTYDJD", length=24)        private String      ldtydjd;       // 
    @Column(name="LDTYBJ", length=24)         private String      ldtybj;        // 
    @Column(name="LL")         private BigDecimal      ll;        // 
    @Column(name="LLDJQ", length=24)        private String      lldjq;       // 
    @Column(name="LLDJD", length=24)         private String      lldjd;        // 
    @Column(name="LLBJ", length=24)         private String      llbj;        // 
    @Column(name="NNL")        private BigDecimal      nnl;       // 
    @Column(name="NNLDJQ", length=24)         private String      nnldjq;        // 
    @Column(name="NNLDJD", length=24)         private String      nnldjd;        // 
    @Column(name="NNLBJ", length=24)        private String      nnlbj;       // 
    @Column(name="ZWT")         private BigDecimal      zwt;        // 
    @Column(name="ZWTDJQ", length=24)         private String      zwtdjq;        // 
    @Column(name="ZWTDJD", length=24)        private String      zwtdjd;       // 
    @Column(name="ZWTBJ", length=24)         private String      zwtbj;        // 
    @Column(name="T1", length=50)         private String      t1;        // 自定义检查项目
    @Column(name="T2", length=50)        private String      t2;       // 自定义检查项目
    @Column(name="T3", length=50)         private String      t3;        // 自定义检查项目
    @Column(name="T4", length=50)         private String      t4;        // 自定义检查项目
    @Column(name="T5", length=50)        private String      t5;       // 自定义检查项目
    @Column(name="T6", length=50)         private String      t6;        // 自定义检查项目
    @Column(name="T7", length=50)         private String      t7;        // 自定义检查项目
    @Column(name="T8", length=50)        private String      t8;       // 自定义检查项目
    @Column(name="T9", length=50)         private String      t9;        // 自定义检查项目
    @Column(name="T10", length=50)         private String      t10;        // 自定义检查项目
    @Column(name="T1Name", length=50)         private String      t1Name;        // 自定义检查项目
    @Column(name="T2Name", length=50)        private String      t2Name;       // 自定义检查项目
    @Column(name="T3Name", length=50)         private String      t3Name;        // 自定义检查项目
    @Column(name="T4Name", length=50)         private String      t4Name;        // 自定义检查项目
    @Column(name="T5Name", length=50)        private String      t5Name;       // 自定义检查项目
    @Column(name="T6Name", length=50)         private String      t6Name;        // 自定义检查项目
    @Column(name="T7Name", length=50)         private String      t7Name;        // 自定义检查项目
    @Column(name="T8Name", length=50)        private String      t8Name;       // 自定义检查项目
    @Column(name="T9Name", length=50)         private String      t9Name;        // 自定义检查项目
    @Column(name="T10Name", length=50)         private String      t10Name;        // 自定义检查项目
    @Column(name="SLBH", length=24)        private String      slbh;       // 皮肤病
    @Column(name="MBPJ", length=24)         private String      mbpj;        // 
    @Column(name="SSYPJ", length=24)         private String      ssypj;        // 
    @Column(name="SZYPJ", length=24)        private String      szypj;       // 
    @Column(name="PXPJ", length=24)         private String      pxpj;        // 
    @Column(name="SLPJ", length=24)         private String      slpj;        // 
    @Column(name="XJH", length=50)        private String      xjh;       // 
    @Column(name="YYPJB", length=50)         private String      yypjb;        // 
    @Column(name="TJSY", length=50)         private Integer      tjsy;        // 
    @Column(name="MB1")        private Integer      mb1;       // 
    @Column(name="MB2")         private Integer      mb2;        // 
    @Column(name="MB3")         private Integer      mb3;        // 
    @Column(name="TJFS")        private Integer      tjfs;       // 
    @Column(name="TJPJ", length=24)         private String      tjpj;        // 
    @Column(name="YYPJH", length=24)         private String      yypjh;        // 
    @Column(name="XMPY", length=24)        private String      xmpy;       // 
    @Column(name="HXP")         private BigDecimal      hxp;        // 
    @Column(name="HXPPJ", length=30)         private String      hxppj;        // 
    @Column(name="EB", length=24)        private String      eb;       // 耳病
    @Column(name="YB", length=24)         private String      yb;        // 眼病
    @Column(name="TB", length=24)         private String      tb;        // 头部
    @Column(name="JB", length=24)        private String      jb;       // 颈部
    @Column(name="PF", length=24)         private String      pf;        // 皮肤
    @Column(name="JZX", length=24)         private String      jzx;        // 甲状腺
    @Column(name="LBJ", length=24)        private String      lbj;       // 淋巴结
    @Column(name="JHJS", length=24)         private String      jhjs;        // 结核菌素
    @Column(name="GBZAM")         private BigDecimal      gbzam;        // 
    @Column(name="DHSA")        private BigDecimal      dhsa;       // 
    @Column(name="DHSZ")         private BigDecimal      dhsz;        // 
    @Column(name="GG", length=24)         private String      gg;        // 肝功
    @Column(name="BMKT", length=24)        private String      bmkt;       // 表面抗体
    @Column(name="YGHKT", length=24)         private String      yghkt;        // 乙肝核心抗体
    @Column(name="YGEKY", length=24)         private String      ygeky;        // 乙肝e抗原
    @Column(name="YGEKT", length=24)        private String      ygekt;       // 乙肝e抗体
    @Column(name="BLOOD", length=24)         private String      blood;        // 血型
    @Column(name="YW")         private BigDecimal      yw;        // 
    @Column(name="TW")        private BigDecimal      tw;       // 
    @Column(name="CXBH", length=24)         private String      cxbh;        // 
    @Column(name="LJSL")         private BigDecimal      ljsl;        // 
    @Column(name="RJSL")        private BigDecimal      rjsl;       // 
    @Column(name="JMY", length=24)         private String      jmy;        // 结膜炎
    @Column(name="WGFB", length=24)         private String      wgfb;        // 窝沟封闭
    @Column(name="YYB", length=24)        private String      yyb;       // 牙龈
    @Column(name="PEI", length=24)         private String      pei;        // 脾
    @Column(name="LSLS")         private BigDecimal      lsls;        // 
    @Column(name="RSLS")        private BigDecimal      rsls;       // 
    @Column(name="LJSLS")         private BigDecimal      ljsls;        // 
    @Column(name="RJSLS")         private BigDecimal      rjsls;        // 
    @Column(name="RTL", length=50)        private String      rtl;       // 听力右
    @Column(name="WSZQ", length=50)         private String      wszq;        // 男性外生殖器
    @Column(name="RS", length=50)         private String      rs;        // 右眼沙眼
    @Column(name="BXP")        private BigDecimal      bxp;       // 
    @Column(name="XXB")         private BigDecimal      xxb;        // 
    @Column(name="SFZH", length=50)         private String      sfzh;        // 
    @Column(name="DH", length=50)        private String      dh;       // 
    @Column(name="ADDRESS", length=200)         private String      address;        // 地址
    @Column(name="RXYFJZS", length=100)         private String      rxyfjzs;        // 
    @Column(name="YMA", length=100)        private String      yma;       // 
    @Column(name="YMAJY", length=100)         private String      ymajy;        // 
    @Column(name="YMB", length=100)         private String      ymb;        // 
    @Column(name="YMBJY", length=100)        private String      ymbjy;       // 
    @Column(name="YMC", length=100)         private String      ymc;        // 
    @Column(name="YMCJY", length=100)         private String      ymcjy;        // 
    @Column(name="YMD", length=100)        private String      ymd;       // 
    @Column(name="YMDJY", length=100)         private String      ymdjy;        // 
    @Column(name="YME", length=100)         private String      yme;        // 
    @Column(name="YMEJY", length=100)        private String      ymejy;       // 
    @Column(name="YMQT", length=100)         private String      ymqt;        // 
    @Column(name="RXHJZS", length=100)         private String      rxhjzs;        // 
    @Column(name="JWBS", length=100)        private String      jwbs;       // 
    @Column(name="QCQFY", length=100)         private Integer      qcqfy;        // 
    @Column(name="STUDGUID", length=50)         private String      studGuid;        // 
    @Column(name="SCHOOLID", length=50)         private String      schoolId;        // 学校ID
    @Column(name="STUDNUM", length=50)        private String      studNum;       // 
    @Column(name="YMF", length=50)         private String      ymf;        // 
    @Column(name="YMFJY")         private String      ymfjy;        // 
    @Column(name="V1", length=50)          private String v1;
    @Column(name="V1Name", length=50)          private String v1Name;
    @Column(name="V2", length=50)          private String v2;
    @Column(name="V2Name", length=50)          private String v2Name;
    @Column(name="V3", length=50)          private String v3;
    @Column(name="V3Name", length=50)          private String v3Name;
    @Column(name="V4", length=50)          private String v4;
    @Column(name="V4Name", length=50)          private String v4Name;
    @Column(name="V5", length=50)          private String v5;
    @Column(name="V5Name", length=50)          private String v5Name;
    @Column(name="YMG", length=100)        private String      ymg;       // 流脑疫苗
    @Column(name="YMGJY", length=100)         private String      ymgjy; 
    @Column(name="YMH", length=100)        private String      ymh;       // 甲肝疫苗
    @Column(name="YMHJY", length=100)         private String      ymhjy; 

    public String getTjnd() {
        return tjnd;
    }

    public void setTjnd(String tjnd) {
        this.tjnd = tjnd;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getSchoolBh() {
        return schoolBh;
    }

    public void setSchoolBh(String schoolBh) {
        this.schoolBh = schoolBh;
    }

    public String getGradeBh() {
        return gradeBh;
    }

    public void setGradeBh(String gradeBh) {
        this.gradeBh = gradeBh;
    }

    public String getClassBh() {
        return classBh;
    }

    public void setClassBh(String classBh) {
        this.classBh = classBh;
    }

    public Date getRxsj() {
        return rxsj;
    }

    public void setRxsj(Date rxsj) {
        this.rxsj = rxsj;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public Date getCsrq() {
        return csrq;
    }

    public void setCsrq(Date csrq) {
        this.csrq = csrq;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
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

    public Date getTjrq() {
        return tjrq;
    }

    public void setTjrq(Date tjrq) {
        this.tjrq = tjrq;
    }

    public Integer getNl() {
        return nl;
    }

    public void setNl(Integer nl) {
        this.nl = nl;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getSg() {
        return sg;
    }

    public void setSg(BigDecimal sg) {
        this.sg = sg;
    }

    public String getSgdjq() {
        return sgdjq;
    }

    public void setSgdjq(String sgdjq) {
        this.sgdjq = sgdjq;
    }

    public String getSgdjd() {
        return sgdjd;
    }

    public void setSgdjd(String sgdjd) {
        this.sgdjd = sgdjd;
    }

    public String getSgbj() {
        return sgbj;
    }

    public void setSgbj(String sgbj) {
        this.sgbj = sgbj;
    }

    public BigDecimal getTz() {
        return tz;
    }

    public void setTz(BigDecimal tz) {
        this.tz = tz;
    }

    public String getTzdjq() {
        return tzdjq;
    }

    public void setTzdjq(String tzdjq) {
        this.tzdjq = tzdjq;
    }

    public String getTzdjd() {
        return tzdjd;
    }

    public void setTzdjd(String tzdjd) {
        this.tzdjd = tzdjd;
    }

    public String getTzbj() {
        return tzbj;
    }

    public void setTzbj(String tzbj) {
        this.tzbj = tzbj;
    }

    public String getYypj() {
        return yypj;
    }

    public void setYypj(String yypj) {
        this.yypj = yypj;
    }

    public String getYypjx() {
        return yypjx;
    }

    public void setYypjx(String yypjx) {
        this.yypjx = yypjx;
    }

    public String getTxpj() {
        return txpj;
    }

    public void setTxpj(String txpj) {
        this.txpj = txpj;
    }

    public String getFypj() {
        return fypj;
    }

    public void setFypj(String fypj) {
        this.fypj = fypj;
    }

    public BigDecimal getXw() {
        return xw;
    }

    public void setXw(BigDecimal xw) {
        this.xw = xw;
    }

    public String getXwdjq() {
        return xwdjq;
    }

    public void setXwdjq(String xwdjq) {
        this.xwdjq = xwdjq;
    }

    public String getXwdjd() {
        return xwdjd;
    }

    public void setXwdjd(String xwdjd) {
        this.xwdjd = xwdjd;
    }

    public String getXwbj() {
        return xwbj;
    }

    public void setXwbj(String xwbj) {
        this.xwbj = xwbj;
    }

    public BigDecimal getFhl() {
        return fhl;
    }

    public void setFhl(BigDecimal fhl) {
        this.fhl = fhl;
    }

    public String getFhldjq() {
        return fhldjq;
    }

    public void setFhldjq(String fhldjq) {
        this.fhldjq = fhldjq;
    }

    public String getFhldjd() {
        return fhldjd;
    }

    public void setFhldjd(String fhldjd) {
        this.fhldjd = fhldjd;
    }

    public String getFhlbj() {
        return fhlbj;
    }

    public void setFhlbj(String fhlbj) {
        this.fhlbj = fhlbj;
    }

    public BigDecimal getWl() {
        return wl;
    }

    public void setWl(BigDecimal wl) {
        this.wl = wl;
    }

    public String getWldjq() {
        return wldjq;
    }

    public void setWldjq(String wldjq) {
        this.wldjq = wldjq;
    }

    public String getWldjd() {
        return wldjd;
    }

    public void setWldjd(String wldjd) {
        this.wldjd = wldjd;
    }

    public String getWlbj() {
        return wlbj;
    }

    public void setWlbj(String wlbj) {
        this.wlbj = wlbj;
    }

    public BigDecimal getMb() {
        return mb;
    }

    public void setMb(BigDecimal mb) {
        this.mb = mb;
    }

    public String getMbbj() {
        return mbbj;
    }

    public void setMbbj(String mbbj) {
        this.mbbj = mbbj;
    }

    public BigDecimal getSsy() {
        return ssy;
    }

    public void setSsy(BigDecimal ssy) {
        this.ssy = ssy;
    }

    public String getSsybj() {
        return ssybj;
    }

    public void setSsybj(String ssybj) {
        this.ssybj = ssybj;
    }

    public BigDecimal getSzy() {
        return szy;
    }

    public void setSzy(BigDecimal szy) {
        this.szy = szy;
    }

    public String getSzybj() {
        return szybj;
    }

    public void setSzybj(String szybj) {
        this.szybj = szybj;
    }

    public BigDecimal getLsl() {
        return lsl;
    }

    public void setLsl(BigDecimal lsl) {
        this.lsl = lsl;
    }

    public BigDecimal getRsl() {
        return rsl;
    }

    public void setRsl(BigDecimal rsl) {
        this.rsl = rsl;
    }

    public String getQtyb() {
        return qtyb;
    }

    public void setQtyb(String qtyb) {
        this.qtyb = qtyb;
    }

    public String getLqg() {
        return lqg;
    }

    public void setLqg(String lqg) {
        this.lqg = lqg;
    }

    public String getRqg() {
        return rqg;
    }

    public void setRqg(String rqg) {
        this.rqg = rqg;
    }

    public String getLsy() {
        return lsy;
    }

    public void setLsy(String lsy) {
        this.lsy = lsy;
    }

    public String getRsy() {
        return rsy;
    }

    public void setRsy(String rsy) {
        this.rsy = rsy;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getXj() {
        return xj;
    }

    public void setXj(String xj) {
        this.xj = xj;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public Integer getQsbnum() {
        return qsbnum;
    }

    public void setQsbnum(Integer qsbnum) {
        this.qsbnum = qsbnum;
    }

    public Integer getRqb() {
        return rqb;
    }

    public void setRqb(Integer rqb) {
        this.rqb = rqb;
    }

    public Integer getRqs() {
        return rqs;
    }

    public void setRqs(Integer rqs) {
        this.rqs = rqs;
    }

    public Integer getRqh() {
        return rqh;
    }

    public void setRqh(Integer rqh) {
        this.rqh = rqh;
    }

    public Integer getHqb() {
        return hqb;
    }

    public void setHqb(Integer hqb) {
        this.hqb = hqb;
    }

    public Integer getHqs() {
        return hqs;
    }

    public void setHqs(Integer hqs) {
        this.hqs = hqs;
    }

    public Integer getHqh() {
        return hqh;
    }

    public void setHqh(Integer hqh) {
        this.hqh = hqh;
    }

    public String getYzb() {
        return yzb;
    }

    public void setYzb(String yzb) {
        this.yzb = yzb;
    }

    public String getBtt() {
        return btt;
    }

    public void setBtt(String btt) {
        this.btt = btt;
    }

    public String getXk() {
        return xk;
    }

    public void setXk(String xk) {
        this.xk = xk;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getSz() {
        return sz;
    }

    public void setSz(String sz) {
        this.sz = sz;
    }

    public String getPz() {
        return pz;
    }

    public void setPz(String pz) {
        this.pz = pz;
    }

    public String getXz() {
        return xz;
    }

    public void setXz(String xz) {
        this.xz = xz;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getFei() {
        return fei;
    }

    public void setFei(String fei) {
        this.fei = fei;
    }

    public String getXt() {
        return xt;
    }

    public void setXt(String xt) {
        this.xt = xt;
    }

    public Integer getXhdb() {
        return xhdb;
    }

    public void setXhdb(Integer xhdb) {
        this.xhdb = xhdb;
    }

    public String getFhcl() {
        return fhcl;
    }

    public void setFhcl(String fhcl) {
        this.fhcl = fhcl;
    }

    public String getPfb() {
        return pfb;
    }

    public void setPfb(String pfb) {
        this.pfb = pfb;
    }

    public BigDecimal getZg() {
        return zg;
    }

    public void setZg(BigDecimal zg) {
        this.zg = zg;
    }

    public String getZgdjq() {
        return zgdjq;
    }

    public void setZgdjq(String zgdjq) {
        this.zgdjq = zgdjq;
    }

    public String getZgdjd() {
        return zgdjd;
    }

    public void setZgdjd(String zgdjd) {
        this.zgdjd = zgdjd;
    }

    public String getZgbj() {
        return zgbj;
    }

    public void setZgbj(String zgbj) {
        this.zgbj = zgbj;
    }

    public BigDecimal getJk() {
        return jk;
    }

    public void setJk(BigDecimal jk) {
        this.jk = jk;
    }

    public String getJkdjq() {
        return jkdjq;
    }

    public void setJkdjq(String jkdjq) {
        this.jkdjq = jkdjq;
    }

    public String getJkdjd() {
        return jkdjd;
    }

    public void setJkdjd(String jkdjd) {
        this.jkdjd = jkdjd;
    }

    public String getJkbj() {
        return jkbj;
    }

    public void setJkbj(String jkbj) {
        this.jkbj = jkbj;
    }

    public BigDecimal getGpk() {
        return gpk;
    }

    public void setGpk(BigDecimal gpk) {
        this.gpk = gpk;
    }

    public String getGpkdjq() {
        return gpkdjq;
    }

    public void setGpkdjq(String gpkdjq) {
        this.gpkdjq = gpkdjq;
    }

    public String getGpkdjd() {
        return gpkdjd;
    }

    public void setGpkdjd(String gpkdjd) {
        this.gpkdjd = gpkdjd;
    }

    public String getGpkbj() {
        return gpkbj;
    }

    public void setGpkbj(String gpkbj) {
        this.gpkbj = gpkbj;
    }

    public BigDecimal getBjl() {
        return bjl;
    }

    public void setBjl(BigDecimal bjl) {
        this.bjl = bjl;
    }

    public BigDecimal getGstj() {
        return gstj;
    }

    public void setGstj(BigDecimal gstj) {
        this.gstj = gstj;
    }

    public BigDecimal getJjx() {
        return jjx;
    }

    public void setJjx(BigDecimal jjx) {
        this.jjx = jjx;
    }

    public String getBmky() {
        return bmky;
    }

    public void setBmky(String bmky) {
        this.bmky = bmky;
    }

    public String getSjsr() {
        return sjsr;
    }

    public void setSjsr(String sjsr) {
        this.sjsr = sjsr;
    }

    public BigDecimal getWsm() {
        return wsm;
    }

    public void setWsm(BigDecimal wsm) {
        this.wsm = wsm;
    }

    public String getWsmdjq() {
        return wsmdjq;
    }

    public void setWsmdjq(String wsmdjq) {
        this.wsmdjq = wsmdjq;
    }

    public String getWsmdjd() {
        return wsmdjd;
    }

    public void setWsmdjd(String wsmdjd) {
        this.wsmdjd = wsmdjd;
    }

    public String getWsmbj() {
        return wsmbj;
    }

    public void setWsmbj(String wsmbj) {
        this.wsmbj = wsmbj;
    }

    public BigDecimal getLdty() {
        return ldty;
    }

    public void setLdty(BigDecimal ldty) {
        this.ldty = ldty;
    }

    public String getLdtydjq() {
        return ldtydjq;
    }

    public void setLdtydjq(String ldtydjq) {
        this.ldtydjq = ldtydjq;
    }

    public String getLdtydjd() {
        return ldtydjd;
    }

    public void setLdtydjd(String ldtydjd) {
        this.ldtydjd = ldtydjd;
    }

    public String getLdtybj() {
        return ldtybj;
    }

    public void setLdtybj(String ldtybj) {
        this.ldtybj = ldtybj;
    }

    public BigDecimal getLl() {
        return ll;
    }

    public void setLl(BigDecimal ll) {
        this.ll = ll;
    }

    public String getLldjq() {
        return lldjq;
    }

    public void setLldjq(String lldjq) {
        this.lldjq = lldjq;
    }

    public String getLldjd() {
        return lldjd;
    }

    public void setLldjd(String lldjd) {
        this.lldjd = lldjd;
    }

    public String getLlbj() {
        return llbj;
    }

    public void setLlbj(String llbj) {
        this.llbj = llbj;
    }

    public BigDecimal getNnl() {
        return nnl;
    }

    public void setNnl(BigDecimal nnl) {
        this.nnl = nnl;
    }

    public String getNnldjq() {
        return nnldjq;
    }

    public void setNnldjq(String nnldjq) {
        this.nnldjq = nnldjq;
    }

    public String getNnldjd() {
        return nnldjd;
    }

    public void setNnldjd(String nnldjd) {
        this.nnldjd = nnldjd;
    }

    public String getNnlbj() {
        return nnlbj;
    }

    public void setNnlbj(String nnlbj) {
        this.nnlbj = nnlbj;
    }

    public BigDecimal getZwt() {
        return zwt;
    }

    public void setZwt(BigDecimal zwt) {
        this.zwt = zwt;
    }

    public String getZwtdjq() {
        return zwtdjq;
    }

    public void setZwtdjq(String zwtdjq) {
        this.zwtdjq = zwtdjq;
    }

    public String getZwtdjd() {
        return zwtdjd;
    }

    public void setZwtdjd(String zwtdjd) {
        this.zwtdjd = zwtdjd;
    }

    public String getZwtbj() {
        return zwtbj;
    }

    public void setZwtbj(String zwtbj) {
        this.zwtbj = zwtbj;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public String getT6() {
        return t6;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getT8() {
        return t8;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public String getT9() {
        return t9;
    }

    public void setT9(String t9) {
        this.t9 = t9;
    }

    public String getT10() {
        return t10;
    }

    public void setT10(String t10) {
        this.t10 = t10;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getMbpj() {
        return mbpj;
    }

    public void setMbpj(String mbpj) {
        this.mbpj = mbpj;
    }

    public String getSsypj() {
        return ssypj;
    }

    public void setSsypj(String ssypj) {
        this.ssypj = ssypj;
    }

    public String getSzypj() {
        return szypj;
    }

    public void setSzypj(String szypj) {
        this.szypj = szypj;
    }

    public String getPxpj() {
        return pxpj;
    }

    public void setPxpj(String pxpj) {
        this.pxpj = pxpj;
    }

    public String getSlpj() {
        return slpj;
    }

    public void setSlpj(String slpj) {
        this.slpj = slpj;
    }

    public String getXjh() {
        return xjh;
    }

    public void setXjh(String xjh) {
        this.xjh = xjh;
    }

    public String getYypjb() {
        return yypjb;
    }

    public void setYypjb(String yypjb) {
        this.yypjb = yypjb;
    }

    public Integer getTjsy() {
        return tjsy;
    }

    public void setTjsy(Integer tjsy) {
        this.tjsy = tjsy;
    }

    public Integer getMb1() {
        return mb1;
    }

    public void setMb1(Integer mb1) {
        this.mb1 = mb1;
    }

    public Integer getMb2() {
        return mb2;
    }

    public void setMb2(Integer mb2) {
        this.mb2 = mb2;
    }

    public Integer getMb3() {
        return mb3;
    }

    public void setMb3(Integer mb3) {
        this.mb3 = mb3;
    }

    public Integer getTjfs() {
        return tjfs;
    }

    public void setTjfs(Integer tjfs) {
        this.tjfs = tjfs;
    }

    public String getTjpj() {
        return tjpj;
    }

    public void setTjpj(String tjpj) {
        this.tjpj = tjpj;
    }

    public String getYypjh() {
        return yypjh;
    }

    public void setYypjh(String yypjh) {
        this.yypjh = yypjh;
    }

    public String getXmpy() {
        return xmpy;
    }

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    public BigDecimal getHxp() {
        return hxp;
    }

    public void setHxp(BigDecimal hxp) {
        this.hxp = hxp;
    }

    public String getHxppj() {
        return hxppj;
    }

    public void setHxppj(String hxppj) {
        this.hxppj = hxppj;
    }

    public String getEb() {
        return eb;
    }

    public void setEb(String eb) {
        this.eb = eb;
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb;
    }

    public String getTb() {
        return tb;
    }

    public void setTb(String tb) {
        this.tb = tb;
    }

    public String getJb() {
        return jb;
    }

    public void setJb(String jb) {
        this.jb = jb;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getJzx() {
        return jzx;
    }

    public void setJzx(String jzx) {
        this.jzx = jzx;
    }

    public String getLbj() {
        return lbj;
    }

    public void setLbj(String lbj) {
        this.lbj = lbj;
    }

    public String getJhjs() {
        return jhjs;
    }

    public void setJhjs(String jhjs) {
        this.jhjs = jhjs;
    }

    public BigDecimal getGbzam() {
        return gbzam;
    }

    public void setGbzam(BigDecimal gbzam) {
        this.gbzam = gbzam;
    }

    public BigDecimal getDhsa() {
        return dhsa;
    }

    public void setDhsa(BigDecimal dhsa) {
        this.dhsa = dhsa;
    }

    public BigDecimal getDhsz() {
        return dhsz;
    }

    public void setDhsz(BigDecimal dhsz) {
        this.dhsz = dhsz;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getBmkt() {
        return bmkt;
    }

    public void setBmkt(String bmkt) {
        this.bmkt = bmkt;
    }

    public String getYghkt() {
        return yghkt;
    }

    public void setYghkt(String yghkt) {
        this.yghkt = yghkt;
    }

    public String getYgeky() {
        return ygeky;
    }

    public void setYgeky(String ygeky) {
        this.ygeky = ygeky;
    }

    public String getYgekt() {
        return ygekt;
    }

    public void setYgekt(String ygekt) {
        this.ygekt = ygekt;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public BigDecimal getYw() {
        return yw;
    }

    public void setYw(BigDecimal yw) {
        this.yw = yw;
    }

    public BigDecimal getTw() {
        return tw;
    }

    public void setTw(BigDecimal tw) {
        this.tw = tw;
    }

    public String getCxbh() {
        return cxbh;
    }

    public void setCxbh(String cxbh) {
        this.cxbh = cxbh;
    }

    public BigDecimal getLjsl() {
        return ljsl;
    }

    public void setLjsl(BigDecimal ljsl) {
        this.ljsl = ljsl;
    }

    public BigDecimal getRjsl() {
        return rjsl;
    }

    public void setRjsl(BigDecimal rjsl) {
        this.rjsl = rjsl;
    }

    public String getJmy() {
        return jmy;
    }

    public void setJmy(String jmy) {
        this.jmy = jmy;
    }

    public String getWgfb() {
        return wgfb;
    }

    public void setWgfb(String wgfb) {
        this.wgfb = wgfb;
    }

    public String getYyb() {
        return yyb;
    }

    public void setYyb(String yyb) {
        this.yyb = yyb;
    }

    public String getPei() {
        return pei;
    }

    public void setPei(String pei) {
        this.pei = pei;
    }

    public BigDecimal getLsls() {
        return lsls;
    }

    public void setLsls(BigDecimal lsls) {
        this.lsls = lsls;
    }

    public BigDecimal getRsls() {
        return rsls;
    }

    public void setRsls(BigDecimal rsls) {
        this.rsls = rsls;
    }

    public BigDecimal getLjsls() {
        return ljsls;
    }

    public void setLjsls(BigDecimal ljsls) {
        this.ljsls = ljsls;
    }

    public BigDecimal getRjsls() {
        return rjsls;
    }

    public void setRjsls(BigDecimal rjsls) {
        this.rjsls = rjsls;
    }

    public String getRtl() {
        return rtl;
    }

    public void setRtl(String rtl) {
        this.rtl = rtl;
    }

    public String getWszq() {
        return wszq;
    }

    public void setWszq(String wszq) {
        this.wszq = wszq;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public BigDecimal getBxp() {
        return bxp;
    }

    public void setBxp(BigDecimal bxp) {
        this.bxp = bxp;
    }

    public BigDecimal getXxb() {
        return xxb;
    }

    public void setXxb(BigDecimal xxb) {
        this.xxb = xxb;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRxyfjzs() {
        return rxyfjzs;
    }

    public void setRxyfjzs(String rxyfjzs) {
        this.rxyfjzs = rxyfjzs;
    }

    public String getYma() {
        return yma;
    }

    public void setYma(String yma) {
        this.yma = yma;
    }

    public String getYmajy() {
        return ymajy;
    }

    public void setYmajy(String ymajy) {
        this.ymajy = ymajy;
    }

    public String getYmb() {
        return ymb;
    }

    public void setYmb(String ymb) {
        this.ymb = ymb;
    }

    public String getYmbjy() {
        return ymbjy;
    }

    public void setYmbjy(String ymbjy) {
        this.ymbjy = ymbjy;
    }

    public String getYmc() {
        return ymc;
    }

    public void setYmc(String ymc) {
        this.ymc = ymc;
    }

    public String getYmcjy() {
        return ymcjy;
    }

    public void setYmcjy(String ymcjy) {
        this.ymcjy = ymcjy;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getYmdjy() {
        return ymdjy;
    }

    public void setYmdjy(String ymdjy) {
        this.ymdjy = ymdjy;
    }

    public String getYme() {
        return yme;
    }

    public void setYme(String yme) {
        this.yme = yme;
    }

    public String getYmejy() {
        return ymejy;
    }

    public void setYmejy(String ymejy) {
        this.ymejy = ymejy;
    }

    public String getYmqt() {
        return ymqt;
    }

    public void setYmqt(String ymqt) {
        this.ymqt = ymqt;
    }

    public String getRxhjzs() {
        return rxhjzs;
    }

    public void setRxhjzs(String rxhjzs) {
        this.rxhjzs = rxhjzs;
    }

    public String getJwbs() {
        return jwbs;
    }

    public void setJwbs(String jwbs) {
        this.jwbs = jwbs;
    }

    public Integer getQcqfy() {
        return qcqfy;
    }

    public void setQcqfy(Integer qcqfy) {
        this.qcqfy = qcqfy;
    }

    public String getStudGuid() {
        return studGuid;
    }

    public void setStudGuid(String studGuid) {
        this.studGuid = studGuid;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getStudNum() {
        return studNum;
    }

    public void setStudNum(String studNum) {
        this.studNum = studNum;
    }

    public String getYmf() {
        return ymf;
    }

    public void setYmf(String ymf) {
        this.ymf = ymf;
    }

    public String getYmfjy() {
        return ymfjy;
    }

    public void setYmfjy(String ymfjy) {
        this.ymfjy = ymfjy;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getT1Name() {
        return t1Name;
    }

    public void setT1Name(String t1Name) {
        this.t1Name = t1Name;
    }

    public String getT2Name() {
        return t2Name;
    }

    public void setT2Name(String t2Name) {
        this.t2Name = t2Name;
    }

    public String getT3Name() {
        return t3Name;
    }

    public void setT3Name(String t3Name) {
        this.t3Name = t3Name;
    }

    public String getT4Name() {
        return t4Name;
    }

    public void setT4Name(String t4Name) {
        this.t4Name = t4Name;
    }

    public String getT5Name() {
        return t5Name;
    }

    public void setT5Name(String t5Name) {
        this.t5Name = t5Name;
    }

    public String getT6Name() {
        return t6Name;
    }

    public void setT6Name(String t6Name) {
        this.t6Name = t6Name;
    }

    public String getT7Name() {
        return t7Name;
    }

    public void setT7Name(String t7Name) {
        this.t7Name = t7Name;
    }

    public String getT8Name() {
        return t8Name;
    }

    public void setT8Name(String t8Name) {
        this.t8Name = t8Name;
    }

    public String getT9Name() {
        return t9Name;
    }

    public void setT9Name(String t9Name) {
        this.t9Name = t9Name;
    }

    public String getT10Name() {
        return t10Name;
    }

    public void setT10Name(String t10Name) {
        this.t10Name = t10Name;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV1Name() {
        return v1Name;
    }

    public void setV1Name(String v1Name) {
        this.v1Name = v1Name;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public String getV2Name() {
        return v2Name;
    }

    public void setV2Name(String v2Name) {
        this.v2Name = v2Name;
    }

    public String getV3() {
        return v3;
    }

    public void setV3(String v3) {
        this.v3 = v3;
    }

    public String getV3Name() {
        return v3Name;
    }

    public void setV3Name(String v3Name) {
        this.v3Name = v3Name;
    }

    public String getV4() {
        return v4;
    }

    public void setV4(String v4) {
        this.v4 = v4;
    }

    public String getV4Name() {
        return v4Name;
    }

    public void setV4Name(String v4Name) {
        this.v4Name = v4Name;
    }

    public String getV5() {
        return v5;
    }

    public void setV5(String v5) {
        this.v5 = v5;
    }

    public String getV5Name() {
        return v5Name;
    }

    public void setV5Name(String v5Name) {
        this.v5Name = v5Name;
    }

    public String getClassLongNo() {
        return classLongNo;
    }

    public void setClassLongNo(String classLongNo) {
        this.classLongNo = classLongNo;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

	/**
	 * @return the ymg
	 */
	public String getYmg() {
		return ymg;
	}

	/**
	 * @param ymg the ymg to set
	 */
	public void setYmg(String ymg) {
		this.ymg = ymg;
	}

	/**
	 * @return the ymgjy
	 */
	public String getYmgjy() {
		return ymgjy;
	}

	/**
	 * @param ymgjy the ymgjy to set
	 */
	public void setYmgjy(String ymgjy) {
		this.ymgjy = ymgjy;
	}

	/**
	 * @return the ymh
	 */
	public String getYmh() {
		return ymh;
	}

	/**
	 * @param ymh the ymh to set
	 */
	public void setYmh(String ymh) {
		this.ymh = ymh;
	}

	/**
	 * @return the ymhjy
	 */
	public String getYmhjy() {
		return ymhjy;
	}

	/**
	 * @param ymhjy the ymhjy to set
	 */
	public void setYmhjy(String ymhjy) {
		this.ymhjy = ymhjy;
	}
    
    
}
