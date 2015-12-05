/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.BmiStandard;
import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.service.IBmiStandardService;
import com.vastcm.stuhealth.client.entity.service.IHeightStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Nov 23, 2013
 */
public class BmiStandardService extends CoreService<BmiStandard> implements IBmiStandardService {
	private static final long serialVersionUID = 1L;

	@Override
	public List<BmiStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM BmiStandard ORDER BY version DESC, age ").list();
	}
	
	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_bmi ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}
	
	@Override
	public List<BmiStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM BmiStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}
}
