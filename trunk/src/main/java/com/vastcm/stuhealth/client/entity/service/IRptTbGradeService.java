/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.RptTbGrade;
import com.vastcm.stuhealth.client.entity.RptZbAge;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public interface IRptTbGradeService extends ICoreService<RptTbGrade>{
	public List<RptTbGrade> getBySchoolBh(String schoolBh, String year);
}
