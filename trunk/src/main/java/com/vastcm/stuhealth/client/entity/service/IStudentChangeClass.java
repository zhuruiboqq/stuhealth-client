package com.vastcm.stuhealth.client.entity.service;

import java.util.Map;

import com.vastcm.stuhealth.client.entity.StudentChangeClass;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

public interface IStudentChangeClass extends ICoreService<StudentChangeClass> {
	public int cancelOperation(String sqlFilter, Map<String, Object> params);
}
