/**
 * 
 */
package com.vastcm.stuhealth.client.utils.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 19, 2013
 */
public class CheckResultItemMapping {
	
	// key=field+code, for example, field=bs, code=0, then key=bs0
	private Map<String, CheckResultItemMapping> fieldCode2Result = new HashMap<String, CheckResultItemMapping>();
	private Map<String, CheckResultItemMapping> fieldAlias2Result = new HashMap<String, CheckResultItemMapping>();
	
	private String field;
	private String code;
	private String alias;
	
	public CheckResultItemMapping() {
		ICheckItemResultService iCheckItemResultSrv = AppContext.getBean("checkItemResultService", ICheckItemResultService.class);
	    List<CheckItemResult> checkItemResultLs = iCheckItemResultSrv.getAll();
	    for(CheckItemResult r : checkItemResultLs) {
	    	fieldCode2Result.put(r.getFieldMc()+r.getItemCode(), new CheckResultItemMapping(r.getFieldMc(), r.getItemCode(), r.getItemResult()));
	    	fieldAlias2Result.put(r.getFieldMc()+r.getItemResult(), new CheckResultItemMapping(r.getFieldMc(), r.getItemCode(), r.getItemResult()));
	   	}
	}
	
	public CheckResultItemMapping(String field, String code, String alias) {
		this.field = field;
		this.code = code;
		this.alias = alias;
	}
	
	public CheckResultItemMapping getMappingByCode(String field, String code) {
		return fieldCode2Result.get(field+code);
	}
	
	public CheckResultItemMapping getMappingByAlias(String field, String alias) {
		return fieldAlias2Result.get(field+alias);
	}

	public Map<String, CheckResultItemMapping> getFieldCode2Result() {
		return fieldCode2Result;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getAlias() {
		return alias;
	}
	
	
	
}
