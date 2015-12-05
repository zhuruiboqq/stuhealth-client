/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.BmiStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 30, 2013
 */
public interface IBmiStandardService extends ICoreService<BmiStandard>{
	public String getNewestVersionCode();
	public List<BmiStandard> getStandardByVersion(String version);
}
