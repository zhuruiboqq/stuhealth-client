/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.RptTbAge;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public interface IRptTbAgeService extends ICoreService<RptTbAge>{
	public List<RptTbAge> getBySchoolBh(String schoolBh, String year);
}
