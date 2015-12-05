/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 30, 2013
 */
public interface IHeightStandardService extends ICoreService<HeightStandard>{
	public String getNewestVersionCode();
	public List<HeightStandard> getStandardByVersion(String version);
}
