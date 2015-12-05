/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.LungsStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 30, 2013
 */
public interface ILungsStandardService extends ICoreService<LungsStandard>{
	public String getNewestVersionCode();
	public List<LungsStandard> getStandardByVersion(String version);
}
