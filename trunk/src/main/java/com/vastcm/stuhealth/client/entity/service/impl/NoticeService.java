/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.Date;
import java.util.List;

import com.vastcm.stuhealth.client.entity.Notice;
import com.vastcm.stuhealth.client.entity.service.INoticeService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;


/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 4, 2013
 */
public class NoticeService extends CoreService<Notice> implements INoticeService{

	@Override
	public List<Notice> getAll() {
		return getSessionFactory().getCurrentSession().createQuery(" FROM Notice ORDER BY CreateDate DESC ").list();
	}
	
	@Override
	public Date getLatestUpdateDate() {
		List ls = getSessionFactory().getCurrentSession()
				.createSQLQuery(" SELECT max(createDate) FROM HW_Notice ").list();
		Date rs = null;
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? rs : (Date) ls.get(0);
		}
		return rs;
	}

	@Override
	public List<Notice> getNoticeByDate(Date createDate) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM Notice WHERE createDate > '" + createDate + "'").list();
	}

}
