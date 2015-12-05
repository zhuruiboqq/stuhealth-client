/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.WeightStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 9, 2013
 */
public interface IWeightStandardService extends ICoreService<WeightStandard>{
	public String getNewestVersionCode();
	public List<WeightStandard> getStandardByVersion(String version);
}
