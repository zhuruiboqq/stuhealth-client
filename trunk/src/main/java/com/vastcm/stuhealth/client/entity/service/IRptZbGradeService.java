/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.RptZbGrade;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public interface IRptZbGradeService extends ICoreService<RptZbGrade>{
	public List<RptZbGrade> getBySchoolBh(String schoolBh, String year);
}
