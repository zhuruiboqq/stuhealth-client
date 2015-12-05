/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 30, 2013
 */
public interface IChestStandardService extends ICoreService<ChestStandard>{
	public String getNewestVersionCode();
	public List<ChestStandard> getStandardByVersion(String version);
}
