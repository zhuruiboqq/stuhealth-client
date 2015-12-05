/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.PulsationStandard;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public interface IPulsationStandardService extends ICoreService<PulsationStandard>{
	public String getNewestVersionCode();
	public List<PulsationStandard> getStandardByVersion(String version);
}
