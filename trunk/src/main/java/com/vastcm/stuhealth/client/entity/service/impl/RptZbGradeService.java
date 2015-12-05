/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.RptZbGrade;
import com.vastcm.stuhealth.client.entity.service.IRptZbGradeService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public class RptZbGradeService extends CoreService<RptZbGrade> implements IRptZbGradeService{

	@Override
	public List<RptZbGrade> getBySchoolBh(String schoolBh, String year) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM   RptZbGrade ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   schoolBh = '").append(schoolBh).append("' ");
		sql.append(" AND   tjnd = '").append(year).append("' ");
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

}
