/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import com.vastcm.stuhealth.client.entity.service.IChestStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public class ChestStandardService extends CoreService<ChestStandard> implements IChestStandardService {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<ChestStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM ChestStandard ORDER BY version DESC, age ").list();
	}

	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_chest ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}
	
	@Override
	public List<ChestStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM ChestStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}

}
