package com.vastcm.stuhealth.client.utils.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.Grade;
import com.vastcm.stuhealth.client.entity.service.IGradeService;

public class GradeMessage {

	private static Map<String, GradeMessage> class2GradeMap = new HashMap<String, GradeMessage>();
	static {
		GradeMessage temp = null;
		IGradeService gradeSrv = AppContext.getBean("gradeService", IGradeService.class);
		List<Grade> grades = gradeSrv.getAll();
		for(Grade g : grades) {
			temp = new GradeMessage(g.getGradeCode(), g.getName().substring(2));
			class2GradeMap.put(g.getGradeCode(), temp);
		}
	}

	public static GradeMessage getGradeMessageByClassCode(String classCode) {
		if (!StringUtils.hasText(classCode))
			return null;
		String gradeCode;
//		if (classCode.length() >= 5) {
//			gradeCode = classCode.substring(0, 3);
//		} else {
		if (classCode.length() >= 5) {
			gradeCode = classCode.substring(classCode.length()-4, classCode.length()-2);
		} else {
			gradeCode = classCode.substring(0, 2);
		}
		return class2GradeMap.get(gradeCode);
	}

	private String gradeCode;
	private String gradeName;

	public GradeMessage() {
		super();
	}

	public GradeMessage(String gradeCode, String gradeName) {
		super();
		this.gradeCode = gradeCode;
		this.gradeName = gradeName;
	}


	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

}