/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.List;

import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.NutritionStandard;
import com.vastcm.stuhealth.client.entity.service.INutritionStandardService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

/**
 * @author house
 * @email yyh2001@gmail.com
 * @since May 31, 2013
 */
public class NutritionStandardService extends CoreService<NutritionStandard> implements INutritionStandardService {
	private static final long serialVersionUID = 1L;

	@Override
	public List<NutritionStandard> getAll() {
		return getSessionFactory().getCurrentSession().createQuery(" FROM NutritionStandard ORDER BY version DESC, sex, grade, sg ").list();
	}

	@Override
	public String getNewestVersionCode() {
		List ls = getSessionFactory().getCurrentSession().createSQLQuery(" SELECT max(version) FROM standard_nutrition ").list();
		String rs = "";
		if (ls != null && ls.size() > 0) {
			rs = ls.get(0) == null ? "0" : (String) ls.get(0);
		}
		return rs;
	}

	@Override
	public List<NutritionStandard> getStandardByVersion(String version) {
		return getSessionFactory().getCurrentSession()
				.createQuery(" FROM NutritionStandard WHERE version = '" + version + "' ORDER BY version DESC, sex, grade, sg ").list();
	}
}
