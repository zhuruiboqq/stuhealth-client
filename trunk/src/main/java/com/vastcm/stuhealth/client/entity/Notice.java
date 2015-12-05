/**
 * 
 */
package com.vastcm.stuhealth.client.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;


/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 4, 2013
 */
@Entity
@Table(name="HW_Notice")
public class Notice extends CoreEntity {
	private static final long serialVersionUID = 4459030252989459133L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Column(name="NoticeCode")      private BigDecimal  noticeCode;  
    @Column(name="NoticeTiltle")    private String      noticeTitle;  
    @Column(name="URL")				private String		url;
    @Column(name="NoticeContent")  	private String      noticeContent;
    @Column(name="CreateDate")     	private Date      	createDate; 
    @Column(name="StartDate")       private Date      	startDate; 
    @Column(name="EndDate")    		private Date      	endDate;   
    @Column(name="State")     		private Boolean      state;
    
    
    @Override
    public String toString() {
    	return noticeTitle + "  -  " + sdf.format(createDate);
    }
    
	/**
	 * @return the noticeCode
	 */
	public BigDecimal getNoticeCode() {
		return noticeCode;
	}
	/**
	 * @param noticeCode the noticeCode to set
	 */
	public void setNoticeCode(BigDecimal noticeCode) {
		this.noticeCode = noticeCode;
	}
	/**
	 * @return the noticeTitle
	 */
	public String getNoticeTitle() {
		return noticeTitle;
	}
	/**
	 * @param noticeTitle the noticeTitle to set
	 */
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the noticeContent
	 */
	public String getNoticeContent() {
		return noticeContent;
	}
	/**
	 * @param noticeContent the noticeContent to set
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the state
	 */
	public Boolean getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(Boolean state) {
		this.state = state;
	} 
    
    
}
