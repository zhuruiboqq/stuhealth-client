/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import org.hibernate.Query;

import com.vastcm.stuhealth.client.entity.UploadLog;
import com.vastcm.stuhealth.client.entity.service.IUploadLogService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 16, 2013
 */
public class UploadLogService extends CoreService<UploadLog> implements IUploadLogService {

	@Override
	public List<UploadLog> getLogs(String year, String term) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM UploadLog WHERE year = :year AND term = :term " );
		Query query = getSessionFactory().getCurrentSession().createQuery(sql.toString());
		query.setParameter("year", year);
		query.setParameter("term", term);
		return query.list();
	}

}
