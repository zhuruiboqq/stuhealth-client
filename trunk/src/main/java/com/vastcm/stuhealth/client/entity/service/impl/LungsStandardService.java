/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.LungsStandard;
import com.vastcm.stuhealth.client.entity.service.ILungsStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public class LungsStandardService extends CoreService<LungsStandard> implements ILungsStandardService{
	private static final long serialVersionUID = 1L;

	@Override
	public List<LungsStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM LungsStandard ORDER BY version DESC, age ").list();
	}
	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_lungs ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}
	
	@Override
	public List<LungsStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM LungsStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}
}
