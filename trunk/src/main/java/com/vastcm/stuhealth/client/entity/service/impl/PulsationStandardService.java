/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.PulsationStandard;
import com.vastcm.stuhealth.client.entity.service.IPulsationStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public class PulsationStandardService extends CoreService<PulsationStandard> implements IPulsationStandardService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<PulsationStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM PulsationStandard ORDER BY version DESC, age ").list();
	}
	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_pulsation ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}

	@Override
	public List<PulsationStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM PulsationStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}
}
