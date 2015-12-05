/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vastcm.stuhealth.client.entity.CheckResult;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 * 
 * @author house
 */
public interface ICheckResultService extends ICoreService<CheckResult> {
	public List<CheckResult> getByName(String name, String classLongNo);

	public List<CheckResult> getByStudentCode(String studentCode, String classLongNo); // 学生代码

	public List<CheckResult> getByXh(String xh, String classLongNo); // 学号

	public List<CheckResult> getBySchool(String schoolCode, String year, String term); // 按学校+体检年度取体检记录

	public List<String> getStudentCodeByClass(String classLongNo); // 取指定班级所有学生的学生代码

	public List<String> getStudentXhByClass(String classLongNo);

	public int updateStudentInfo4Result(String classLongNo);

	// retValue: key=studentCode, value=array of checkresult.
	// Array of CheckResult: 1st column is id. And followed by values according to itemFieldNames.
	public Map<String, Object[]> get(List<String> studentCodes, List<String> itemFieldNames, String schoolCode, String year, String term);

	public Map<String, Object> getItemValueMap(String studentCode, Object[] itemFieldNames, String schoolCode, String year, String term);

	public Map<String, Object> getItemValueMap(String studentCode, List<String> itemFieldNames, String schoolCode, String year, String term);

	// entrys: update XXX set x=y 中的 x y 对
	// insert if not exists and update if exists, base on studentCode
	public int update(List<String> studentCodes, Map<String, Object> entrys, String schoolCode, String year, String term);

	public void removeByClassLongNumber(String classLongNo);

	public void removeByStudentCode(String[] studentCodes, String classLongNumber);

	/**
	 * 按班级更新评价列的信息
	 * @param idSet
	 * @return
	 */
	public int updateEvaluationByClass(String classNo);

	/**
	 * 更新评价列的信息
	 * @param idSet
	 * @return
	 */
	public int updateEvaluation(Set<String> idSet);

	/**
	 * 更新评价列的信息
	 * @param sqlFilter 要包含where，不允许为空
	 * @param params 参数集合
	 * @return
	 */
	public int updateEvaluation(String sqlFilter, Map<String, Object> params);

	//Map's key is database column name, must be upper case.
	public Set<String> importRecord(List<Map<String, Object>> datas, boolean isUpdateExist);

	//recalculate RptTbAge\RptTbGrade\RptZbAge\RptZbGrade by effect (insert or update) Result id
	public int recalculateStatRptByResult(Set<String> idSet);

	//recalculate RptTbAge\RptTbGrade\RptZbAge\RptZbGrade by effect (filter) Result id
	/**
	 * 更新报表统计信息
	 * @param sqlFilter 要包含where，不允许为空
	 * @param params 参数集合
	 * @return
	 */
	public int recalculateStatRptByResult(String sqlFilter, Map<String, Object> params);

	/**
	 * 更新报表统计信息
	 * @param sqlFilter 要包含where，不允许为空
	 * @param params 参数集合
	 * @param isUseThread 是否使用线程
	 * @return
	 */
	public int recalculateStatRptByResult(String sqlFilter, Map<String, Object> params, boolean isUseThread);

	/**
	 * 更新报表统计信息，主要供删除体验记录使用
	 * @param schoolNo 学校编号
	 * @param tjnd 体检年度
	 * @param isUseThread 是否使用线程
	 * @return
	 */
	public int recalculateStatRptByResult4Delete(String schoolNo, int tjnd, boolean isUseThread);

	public void save(List<CheckResult> list, String year);
}
