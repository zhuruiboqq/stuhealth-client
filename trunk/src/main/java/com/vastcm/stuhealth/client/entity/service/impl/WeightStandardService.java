/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.WeightStandard;
import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import com.vastcm.stuhealth.client.entity.service.IWeightStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 9, 2013
 */
public class WeightStandardService extends CoreService<WeightStandard> implements IWeightStandardService{

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<WeightStandard> getAll() {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM WeightStandard ORDER BY version DESC, age ").list();
	}
	
	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession()
			.createSQLQuery(" SELECT max(version) FROM standard_weight ").list();
		String rs = "";
		if(ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}
	
	@Override
	public List<WeightStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM WeightStandard WHERE version = '" + version + "' ORDER BY version DESC, age ").list();
	}

}
