/**
 * 
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.UploadLog;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 16, 2013
 */
public interface IUploadLogService extends ICoreService<UploadLog> {
	public List<UploadLog> getLogs(String year, String term);
}
