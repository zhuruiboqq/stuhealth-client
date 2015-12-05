/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.NutritionStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 30, 2013
 */
public interface INutritionStandardService extends ICoreService<NutritionStandard>{
	public String getNewestVersionCode();
	public List<NutritionStandard> getStandardByVersion(String version);
}
