/**
 * 
 */
package com.vastcm.stuhealth.client.utils.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.VaccinItem;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;
import com.vastcm.stuhealth.client.entity.service.IVaccinItemService;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 19, 2013
 */
public class VaccinItemMapping {
	
	// key=field+code, for example, field=bs, code=0, then key=bs0
	private Map<String, VaccinItemMapping> fieldCode2Result = new HashMap<String, VaccinItemMapping>();
	private Map<String, VaccinItemMapping> fieldAlias2Result = new HashMap<String, VaccinItemMapping>();
	
	private String field;
	private String code;
	private String alias;
	
	public VaccinItemMapping() {
		IVaccinItemService iVaccinItemSrv = AppContext.getBean("vaccinItemService", IVaccinItemService.class);
	    List<VaccinItem> vaccinItemLs = iVaccinItemSrv.getAll();
	    for(VaccinItem r : vaccinItemLs) {
	    	fieldCode2Result.put(r.getFieldMc()+r.getVaccItemId(), new VaccinItemMapping(r.getFieldMc(), r.getVaccItemId(), r.getVaccItem()));
	    	fieldAlias2Result.put(r.getFieldMc()+r.getVaccItem(), new VaccinItemMapping(r.getFieldMc(), r.getVaccItemId(), r.getVaccItem()));
	   	}
	}
	
	public VaccinItemMapping(String field, String code, String alias) {
		this.field = field;
		this.code = code;
		this.alias = alias;
	}
	
	public VaccinItemMapping getMappingByCode(String field, String code) {
		return fieldCode2Result.get(field+code);
	}
	
	public VaccinItemMapping getMappingByAlias(String field, String alias) {
		return fieldAlias2Result.get(field+alias);
	}

	public Map<String, VaccinItemMapping> getFieldCode2Result() {
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
