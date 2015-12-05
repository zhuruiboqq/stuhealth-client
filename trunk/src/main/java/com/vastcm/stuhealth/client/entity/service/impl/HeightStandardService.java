/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.service.IHeightStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public class HeightStandardService extends CoreService<HeightStandard> implements IHeightStandardService {
	private static final long serialVersionUID = 1L;

	@Override
	public List<HeightStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM HeightStandard ORDER BY version DESC, age ").list();
	}
	
	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_height ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}
	
	@Override
	public List<HeightStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM HeightStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}
}
