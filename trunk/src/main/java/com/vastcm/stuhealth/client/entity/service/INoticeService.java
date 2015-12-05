/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.Date;
import java.util.List;

import com.vastcm.stuhealth.client.entity.Notice;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;


/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 4, 2013
 */
public interface INoticeService extends ICoreService<Notice>{
	public Date getLatestUpdateDate();
	// return notice that created later than the param createDate.
	public List<Notice> getNoticeByDate(Date createDate);
}
