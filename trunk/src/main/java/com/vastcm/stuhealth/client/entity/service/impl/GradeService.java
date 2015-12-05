/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.Grade;
import com.vastcm.stuhealth.client.entity.service.IGradeService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 16, 2013
 */
public class GradeService extends CoreService<Grade> implements IGradeService {
	@Override
	public List<Grade> getAll() {
		return getSessionFactory().getCurrentSession()
                .createQuery(" FROM Grade ORDER BY gradeCode ").list();
	}
}
