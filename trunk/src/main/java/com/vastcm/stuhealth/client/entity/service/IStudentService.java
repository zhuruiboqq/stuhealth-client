/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 *
 * @author House
 */
public interface IStudentService extends ICoreService<Student> {
    public Student getStudentByCode(String studentCode, String classLongNumber);
    public List<Object[]> getStudentAndSchoolByClass(String classLongNo, boolean includeSubNodes, String field, String keyword);
    public List<Student> getStudentsByClass(String classLongNo, boolean includeSubNodes, String field, String keyword);
    public List<Student> getStudentsByClass(String classLongNo, boolean includeSubNodes);
    public int changeClassByClass(String srcClassLongNo, String destClassLongNo);
    public int changeClassByStudent(String destClassLongNo, String... studentIDs);
    public int graduate(String sqlFilter, Map<String, Object> params);
    public String isUpgradeble(String schoolCode);
    public int oneKeyUpgrade(String schoolCode);
    public int getStudentCountByClass(String classLongNo);
	//Map's key is database column name, must be upper case.
	public Set<String> importRecord(List<Map<String, Object>> datas, boolean isUpdateExist);
	public void remove(String[] ids);
	public void removeByClassLongNumber(String classLongNumber);
	
	/**
	 * 更新学生记录中的班级名（主要用于学校维护修改班级名称时调用）
	 * @author house
	 * @email  yyh2001@gmail.com
	 * @since  Jun 16, 2013
	 * @param classes key=classLongNumber, value=className
	 */
	public void updateClassName(Map<String, String> classes);
}
