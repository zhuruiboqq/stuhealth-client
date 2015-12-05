/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

import org.hibernate.Query;

/**
 *
 * @author House
 */
public class SchoolService extends CoreService<School> implements ISchoolService{
    
    @Override
    public School getByCode(String schoolCode) {
        String query = " FROM School WHERE schoolCode = '" + schoolCode + "' ";
        List<School> ls = getSessionFactory().getCurrentSession().createQuery(query).list();
        if(ls.size() > 0) {
            return ls.get(0);
        }
        return null;
    }

	@Override
	public void updateStatus(String schoolCode, String status, String hostSchoolCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE School SET status = '" + status + "' " );
		if(hostSchoolCode != null) {
			sql.append(" , hostSchoolCode = '" + hostSchoolCode + "' ");
		}
		sql.append(" WHERE 1=1 AND schoolCode = '" + schoolCode + "' ");
		getSessionFactory().getCurrentSession()
			.createQuery(sql.toString()).executeUpdate();
	}

	@Override
	public School getByClassLongNumber(String classLongNumber) {
		String schoolCode = classLongNumber.substring(0, classLongNumber.lastIndexOf("!"));
		schoolCode = schoolCode.substring(schoolCode.lastIndexOf("!") + 1);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM School WHERE schoolCode = :schoolCode ");
		Query query = getSessionFactory().getCurrentSession().createQuery(sql.toString());
		query.setParameter("schoolCode", schoolCode);
		List<School> ls = query.list();
		if(ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	@Override
	public void removeByCode(String code) {
		getSessionFactory().getCurrentSession()
		.createQuery(" DELETE FROM School WHERE schoolCode = '" + code + "' ").executeUpdate();
	}
}
