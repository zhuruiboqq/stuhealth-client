/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.RptZbAge;
import com.vastcm.stuhealth.client.entity.RptZbGrade;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public interface IRptZbAgeService extends ICoreService<RptZbAge>{
	public List<RptZbAge> getBySchoolBh(String schoolBh, String year);
}
