/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import net.sf.jasperreports.engine.util.Pair;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.GlobalVariables;
import com.vastcm.stuhealth.client.entity.CheckResult;
import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.utils.HqlUtil;
import com.vastcm.stuhealth.client.utils.SQLUtils;
import com.vastcm.stuhealth.client.utils.biz.GradeMessage;
import com.vastcm.stuhealth.client.utils.biz.SQLImportDataUtil;
import com.vastcm.stuhealth.client.utils.biz.SchoolMessage;

/**
 * 
 * @author house
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CheckResultService extends CoreService<CheckResult> implements ICheckResultService {

	private Logger logger = LoggerFactory.getLogger(CheckResultService.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String getYearConition() {
		return " AND   tjnd = '" + GlobalVariables.getGlobalVariables().getYear() + "' ";
	}

	@Override
	public List<CheckResult> getByName(String name, String classLongNo) {
		name = name.trim();
		classLongNo = classLongNo.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM  CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   classLongNo = '").append(classLongNo).append("' ");
		sql.append(" AND   xm = '").append(name).append("' ");
		sql.append(getYearConition());
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

	@Override
	public List<CheckResult> getByStudentCode(String studentCode, String classLongNo) {
		studentCode = studentCode.trim();
		classLongNo = classLongNo.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   classLongNo = '").append(classLongNo).append("' ");
		sql.append(" AND   studentCode = '").append(studentCode).append("' ");
		sql.append(getYearConition());
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

	@Override
	public List<CheckResult> getByXh(String xh, String classLongNo) {
		xh = xh.trim();
		classLongNo = classLongNo.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   classLongNo = '").append(classLongNo).append("' ");
		sql.append(" AND   xh = '").append(xh).append("' ");
		sql.append(getYearConition());
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

	@Override
	public List<CheckResult> getBySchool(String schoolCode, String year, String term) {
		schoolCode = schoolCode.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   schoolBh = '").append(schoolCode).append("' ");
		sql.append(" AND   tjnd = '").append(year).append("' ");
		sql.append(" AND   term = '").append(term).append("' \n");
		sql.append(getYearConition());
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

	@Override
	public List<String> getStudentCodeByClass(String classLongNo) {
		classLongNo = classLongNo.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT studentCode ");
		sql.append(" FROM   CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   classLongNo = '").append(classLongNo).append("' ");
		sql.append(getYearConition());
		return getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
	}

	@Override
	public Map<String, Object[]> get(List<String> studentCodes, List<String> itemFieldNames, String schoolCode, String year, String term) {
		if (studentCodes.isEmpty()) {
			return new HashMap();
		}
		String stuCondition = SQLUtils.getInStatement(studentCodes);
		StringBuilder fields = new StringBuilder();
		for (int i = 0; i < itemFieldNames.size(); i++) {
			fields.append(itemFieldNames.get(i)).append(",");
		}
		if (fields.length() > 0) {
			fields.deleteCharAt(fields.lastIndexOf(","));
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT id, studentCode, ").append(fields).append(" \n");
		sql.append(" FROM   Result ").append(" \n");
		sql.append(" WHERE  studentCode IN (").append(stuCondition).append(") \n");
		sql.append(" AND tjnd = '" + year + "'\n");
		sql.append(" AND term = '").append(term).append("' \n");
		sql.append(" AND schoolBh = '").append(schoolCode).append("' \n");
		logger.info("Executing SQL:\n" + sql.toString());
		List ls = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		Map<String, Object[]> rs = new HashMap<String, Object[]>();
		for (Object obj : ls) {
			Object[] rec = (Object[]) obj;
			rs.put((String) rec[1], rec);
		}
		return rs;
	}

	@Override
	public Map<String, Object> getItemValueMap(String studentCode, List<String> itemFieldNames, String schoolCode, String year, String term) {
		return getItemValueMap(studentCode, itemFieldNames.toArray(), schoolCode, year, term);
	}

	@Override
	public Map<String, Object> getItemValueMap(String studentCode, Object[] itemFieldNames, String schoolCode, String year, String term) {
		Map<String, Object> rs = new TreeMap<String, Object>();
		if (studentCode.isEmpty() || itemFieldNames == null || itemFieldNames.length == 0) {
			return rs;
		}
		StringBuilder fields = new StringBuilder();
		for (int i = 0; i < itemFieldNames.length; i++) {
			fields.append(itemFieldNames[i]).append(",");
		}
		if (fields.length() > 0) {
			fields.deleteCharAt(fields.lastIndexOf(","));
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ").append(fields).append(" \n");
		sql.append(" FROM   Result ").append(" \n");
		sql.append(" WHERE  studentCode = '").append(studentCode).append("' \n");
		sql.append(" AND tjnd = '").append(year).append("' \n");
		sql.append(" AND term = '").append(term).append("' \n");
		sql.append(" AND schoolBh = '").append(schoolCode).append("' \n");
		logger.info("Executing SQL:\n" + sql.toString());
		List ls = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		if (ls.size() == 0) {
			return rs;
		}
		Object[] rec = (Object[]) ls.get(0);
		for (int i = 0; i < rec.length; i++) {
			rs.put((String) itemFieldNames[i], rec[i]);
		}
		return rs;
	}

	@Override
	public int update(List<String> studentCodes, Map<String, Object> entrys, String schoolCode, String year, String term) {
		if (entrys.isEmpty()) {
			return 0;
		}
		String allStu = SQLUtils.getInStatement(studentCodes);
		StringBuilder sqlRecExist = new StringBuilder();
		sqlRecExist.append(" SELECT studentCode FROM Result WHERE studentCode IN (");
		sqlRecExist.append(allStu);
		sqlRecExist.append(" )");
		sqlRecExist.append(" AND tjnd = '").append(year).append("' \n");
		sqlRecExist.append(" AND term = '").append(term).append("' \n");
		sqlRecExist.append(" AND schoolBh = '").append(schoolCode).append("' \n");
		List<String> existStu = getSessionFactory().getCurrentSession().createSQLQuery(sqlRecExist.toString()).list();
		List<String> notExistStu = new ArrayList<String>();
		for (String s : studentCodes) {
			if (!existStu.contains(s)) {
				notExistStu.add(s);
			}
		}

		StringBuilder updateFields = new StringBuilder(); // Format: A=1,B=2,C=3...

		Iterator<Map.Entry<String, Object>> iter = entrys.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = iter.next();
			//			updateFields.append(entry.getKey()).append("=").append("'").append(entry.getValue()).append("'");
			if (entry.getValue() == null) {
				updateFields.append(entry.getKey()).append("=null");
			} else {
				updateFields.append(entry.getKey()).append("=").append("'").append(entry.getValue()).append("'");
			}

			updateFields.append(",");
		}
		if (updateFields.length() > 0) {
			updateFields.deleteCharAt(updateFields.lastIndexOf(","));
		}

		int updatedRows = 0;
		if (existStu.size() > 0) {
			StringBuilder sqlUpdate = new StringBuilder();
			sqlUpdate.append(" UPDATE Result ").append("\n");
			sqlUpdate.append(" SET ").append(updateFields).append(" \n");
			sqlUpdate.append(" WHERE  studentCode IN (");
			sqlUpdate.append(SQLUtils.getInStatement(existStu)).append(") \n");
			sqlUpdate.append(" AND tjnd = '").append(year).append("' \n");
			sqlUpdate.append(" AND term = '").append(term).append("' \n");
			sqlUpdate.append(" AND schoolBh = '").append(schoolCode).append("' \n");
			logger.info("Executing SQL:\n" + sqlUpdate.toString());
			updatedRows += getSessionFactory().getCurrentSession().createSQLQuery(sqlUpdate.toString()).executeUpdate();
		}

		StringBuilder insertFields = new StringBuilder(); // Format: A,B,C...
		int insertedRows = 0;
		Query queryStudent = getSessionFactory().getCurrentSession().createQuery(
				" FROM Student WHERE studentCode = :stuCode AND schoolNo = :schoolCode ");
		if (!entrys.containsKey("tjrq")) {
			entrys.put("tjrq", sdf.format(new Date()));
		}
		entrys.remove("studentCode");
		entrys.remove("xm");
		entrys.remove("xb");
		entrys.remove("classLongNo");
		entrys.remove("classBh");
		entrys.remove("CLASS_NAME");
		entrys.remove("gradeBh");
		entrys.remove("grade_Name");
		entrys.remove("schoolBh");
		entrys.remove("SCHOOL_NAME");
		entrys.remove("schoolType");
		entrys.remove("xh");
		entrys.remove("bh");
		entrys.put("tjnd", year);
		entrys.put("term", term);
		Iterator<Map.Entry<String, Object>> iterInsert = entrys.entrySet().iterator();
		while (iterInsert.hasNext()) {
			Map.Entry<String, Object> entry = iterInsert.next();
			insertFields.append(entry.getKey());
			insertFields.append(",");
		}
		if (insertFields.length() > 0) {
			insertFields.deleteCharAt(insertFields.lastIndexOf(","));
		}
		for (String stuCode : notExistStu) {
			StringBuilder sqlInsert = new StringBuilder();
			queryStudent.setString("stuCode", stuCode);
			queryStudent.setString("schoolCode", schoolCode);
			Student student = (Student) queryStudent.list().get(0);
			sqlInsert.append(" INSERT INTO Result ").append("\n");
			sqlInsert.append(" ( id, studentCode, xm, xb, classLongNo, classBh, CLASS_NAME, "
					+ "gradeBh, grade_Name, schoolBh, SCHOOL_NAME, schoolType, xh, bh, ");

			sqlInsert.append(insertFields).append(" ) \n");
			sqlInsert.append(" VALUES ( ");
			sqlInsert.append(" '").append(UUID.randomUUID().toString()).append("', ");
			sqlInsert.append(" '").append(stuCode).append("', ");
			sqlInsert.append(" '").append(student.getName()).append("', ");
			sqlInsert.append(" '").append(student.getSex()).append("', ");
			sqlInsert.append(" '").append(student.getClassLongNumber()).append("', ");
			sqlInsert.append(" '").append(student.getClassNo()).append("', ");
			sqlInsert.append(" '").append(student.getClassName()).append("', ");
			GradeMessage gradeMsg = GradeMessage.getGradeMessageByClassCode(student.getClassNo());
			sqlInsert.append(" '").append(gradeMsg.getGradeCode()).append("', ");
			sqlInsert.append(" '").append(gradeMsg.getGradeName()).append("', ");
			sqlInsert.append(" '").append(student.getSchoolNo()).append("', ");
			School school = SchoolMessage.getSchoolByClassLongNumber(student.getClassLongNumber());
			logger.info("schoolName=" + school.getName());
			sqlInsert.append(" '").append(school.getName()).append("', ");
			sqlInsert.append(" '").append(school.getSchoolType()).append("', ");
			sqlInsert.append(" '").append(student.getStudentCode()).append("', ");
			SchoolMessage schMsg = SchoolMessage.getSchoolMessageByClassLongNumber(student.getClassLongNumber());
			sqlInsert.append(" '").append(schMsg.getSchoolCode() + stuCode).append("', ");

			sqlInsert.append(SQLUtils.getInStatement(entrys.values().toArray()));
			sqlInsert.append(" )");
			logger.info("Executing SQL:\n" + sqlInsert);
			insertedRows += getSessionFactory().getCurrentSession().createSQLQuery(sqlInsert.toString()).executeUpdate();
		}
		logger.info("inserted rows: " + insertedRows);

		return insertedRows + updatedRows;
	}

	@Override
	public List<String> getStudentXhByClass(String classLongNo) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Set<String> importRecord(List<Map<String, Object>> datas, boolean isUpdateExist) {
		//应当使用学生的姓名和出生日期作为学生唯一的判断条件，是否要更新
		if (datas == null || datas.size() == 0)
			return null;
		String idColumn = "ID";
		String[] mustContainColumn = { "XM", "CSRQ", "XB", "TJND", "TERM", "CLASSBH", "GRADEBH", "SCHOOLBH", "CLASS_NAME", "GRADE_NAME",
				"SCHOOL_NAME", "SCHOOLTYPE" };
		String[] logicalColumnKeys = { "SCHOOLBH", "TJND", "TERM", "XM", "XB", "CSRQ" };
		Pair<Set<String>, Set<String>> insertUpdatePair = SQLImportDataUtil.insertOrUpdateRecord(getSessionFactory().getCurrentSession(), datas,
				isUpdateExist, "Result", idColumn, mustContainColumn, logicalColumnKeys);
		insertUpdatePair.first().addAll(insertUpdatePair.second());
		afterImportRecord(insertUpdatePair.first());
		return insertUpdatePair.first();
	}

	protected void afterImportRecord(Set<String> idSet) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("id", idSet);
		SQLQuery query = null;
		StringBuffer sql = new StringBuffer(500);
		int resultCount;
		//重算龋失补牙数，update 2014-10-15取消计算，保持为空
//		sql.setLength(0);
//		sql.append("update result r set QSBNUM=IFNULL(RQH,0)+IFNULL(RQS,0)+IFNULL(RQB,0)+IFNULL(HQH,0)+IFNULL(HQS,0)+IFNULL(HQB,0) \n");
//		sql.append(" where r.id in (:id)");
//		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
//		HqlUtil.setParas(query, params);
//		resultCount = query.executeUpdate();
//		logger.info("update table result set QSBNUM" + resultCount);

		//更新学生代码
		sql.setLength(0);
		sql.append(" update result r inner join Student s on r.xm=s.name and r.csrq=s.bornDate and s.schoolNo=r.SCHOOLBH and s.classNo=r.CLASSBH \n");
		sql.append(" set r.studentCode=s.studentCode, r.xh=s.studentCode, r.BH=concat(s.schoolNo,s.studentCode), r.rxsj=s.enterDate \n");
		sql.append(" where r.id in (:id)");
		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		resultCount = query.executeUpdate();
		logger.info("update table result set studentCode" + resultCount);

		updateEvaluation(idSet);
		recalculateStatRptByResult(idSet);
	}

	@Override
	public int updateEvaluationByClass(String classNo) {
		GlobalVariables g = GlobalVariables.getGlobalVariables();
		StringBuilder sql4ResultID = new StringBuilder();
		sql4ResultID.append(" SELECT id FROM CheckResult WHERE 1=1 ");
		sql4ResultID.append(" AND classBh = '").append(classNo).append("' ");
		sql4ResultID.append(" AND tjnd = '").append(g.getYear()).append("' ");
		sql4ResultID.append(" AND term = '").append(g.getTerm()).append("' ");
		List<String> ls = getSessionFactory().getCurrentSession().createQuery(sql4ResultID.toString()).list();
		return updateEvaluation(new HashSet<String>(ls));
	}

	@Override
	public int updateEvaluation(Set<String> idSet) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("id", idSet);
		return updateEvaluation("\nwhere id in (:id)", params);
	}

	@Override
	public int updateEvaluation(String sqlFilter, Map<String, Object> params) {
		// 更新结果表中评价类字段的值，注意必须要有年龄和性别，部分字段还与年级相关
		SQLQuery query;
		int result;
		StringBuffer sql = new StringBuffer(500);

		//更新一个默认的年龄
		sql.setLength(0);
		sql.append("update result r set NL=(case when CSRQ is null then 12 else :CheckYearInnerParam-year(CSRQ) end)");
		sql.append(sqlFilter).append(" and NL is null");
		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		Map<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.putAll(params);
		updateParams.put("CheckYearInnerParam", GlobalVariables.getGlobalVariables().getYear());
		HqlUtil.setParas(query, updateParams);
		result = query.executeUpdate();

		sql.setLength(0);
		sql.append("update result r set SGDJQ=");
		appendStandarSelectSQL5Level(sql, "SG", "standard_height").append(",TZDJQ=");
		appendStandarSelectSQL5Level(sql, "TZ", "standard_weight").append(",XWDJQ=");
		appendStandarSelectSQL3Level(sql, "XW", "standard_Chest").append(",MBPJ=");
		appendStandarSelectSQL3Level(sql, "MB", "standard_pulsation").append(",FHLDJQ=");
		appendStandarSelectSQL5Level(sql, "FHL", "standard_lungs").append(",YYPJB=");
		appendStandarSelectSQL3Level4BMI(sql, "TZ*10000/(SG*SG)", "standard_bmi").append(",");
		sql.append("\n\tSLPJ=(case when LSL<5.0 or rsl<5.0 then '视力低下' when LSL>=5.0 and rsl>=5.0 then '正常' else '' end),");
		sql.append("\n\tSSYPJ=(case when SSY<=140 then '无异常' when SSY>140 then '高血压' else null end),");
		sql.append("\n\tSZYPJ=(case when SZY<=90 then '无异常' when SZY>90 then '高血压' else null end),");
		sql.append("\n\tPXPJ=(case when (NL>=7 and NL<=13 and XHDB<120) or (XB='男' and NL>=14 and XHDB<130) or (XB='女' and NL>=14 and XHDB<120) then '低血红蛋白' when XHDB>0 then '正常' else null end)");
		sql.append("\n\t,YYPJ=");
		appendStandarSelectSQL5Level4YYPJ(sql, "TZ", "Standard_Nutrition");
		sql.append(sqlFilter);

		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		result = query.executeUpdate();

		//
		sql.setLength(0);
		sql.append("update result r set SGDJQ=SGDJQ,SGDJD=").append(AppCache.getInstance().getMaxVersion("SG"));
		sql.append(",TZDJQ=TZDJQ,TZDJD=").append(AppCache.getInstance().getMaxVersion("TZ"));
		sql.append(",XWDJQ=XWDJQ,XWDJD=").append(AppCache.getInstance().getMaxVersion("XW"));
		sql.append(",FHLDJQ=FHLDJQ,FHLDJD=").append(AppCache.getInstance().getMaxVersion("FHL"));
		sql.append(sqlFilter);
		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		result = query.executeUpdate();

		//更新身高超出标准表的营养评价
		sql.setLength(0);
		updateYYPJ(sql);
		sql.append(sqlFilter).append(" and r.YYPJ IS NULL");
		query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		result = query.executeUpdate();

		return result;
	}

	private StringBuffer appendStandarSelectSQL5Level4YYPJ(StringBuffer sql, String checkValueColumn, String standarTable) {
		String versionCode = "YY";
		String filter = " and r.GRADEBH=s.grade and round(r.SG,0)=s.SG ";
		String maxVersion = AppCache.getInstance().getMaxVersion(versionCode);
		sql.append("\n\t(select case when r.").append(checkValueColumn).append("<P10 then '营养不良'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P10 and r.").append(checkValueColumn).append("<P25 then '较低体重'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P25 and r.").append(checkValueColumn).append("<P75 then '正常体重'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P75 and r.").append(checkValueColumn).append("<P90 then '超重'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P90 then '肥胖'");
		sql.append("\n\t\telse null end");
		sql.append("\n\tfrom ").append(standarTable).append(" s");
		//过滤条件：性别、年级、身高、最新版本
		sql.append("\n\twhere r.XB=s.sex and version=").append(maxVersion).append(filter).append(")");
		return sql;
	}

	private StringBuffer updateYYPJ(StringBuffer sql) {
		String versionCode = "YY";
		String maxVersion = AppCache.getInstance().getMaxVersion(versionCode);
		sql.append(" UPDATE result r SET YYPJ=(                                                                      \n");
		sql.append(" 	SELECT CASE WHEN ROUND(r.SG,0)<t.sg_min THEN                                                   \n");
		sql.append(" 		CASE WHEN (t.sg_min-r.SG)*0.5+r.TZ <p10 THEN '营养不良'                                      \n");
		sql.append(" 		WHEN (t.sg_min-r.SG)*0.5+r.TZ <p25 THEN '较低体重'                                           \n");
		sql.append(" 		WHEN (t.sg_min-r.SG)*0.5+r.TZ <p75 THEN '正常体重'                                           \n");
		sql.append(" 		WHEN (t.sg_min-r.SG)*0.5+r.TZ <p90 THEN '超重'                                               \n");
		sql.append(" 		WHEN (t.sg_min-r.SG)*0.5+r.TZ >=p90 THEN '肥胖'                                              \n");
		sql.append(" 		ELSE NULL END                                                                                \n");
		sql.append(" 	WHEN ROUND(r.SG,0)>t.sg_max THEN                                                               \n");
		sql.append(" 		CASE WHEN r.TZ-(r.SG-t.sg_max)*0.9 <p10 THEN '营养不良'                                      \n");
		sql.append(" 		WHEN r.TZ-(r.SG-t.sg_max)*0.9 <p25 THEN '较低体重'                                           \n");
		sql.append(" 		WHEN r.TZ-(r.SG-t.sg_max)*0.9 <p75 THEN '正常体重'                                           \n");
		sql.append(" 		WHEN r.TZ-(r.SG-t.sg_max)*0.9 <p90 THEN '超重'                                               \n");
		sql.append(" 		WHEN r.TZ-(r.SG-t.sg_max)*0.9 >=p90 THEN '肥胖'                                              \n");
		sql.append(" 		ELSE NULL END                                                                                \n");
		sql.append(" 	ELSE NULL END yypj                                                                             \n");
		sql.append(" 	FROM Standard_Nutrition s                                                                      \n");
		sql.append(" 	LEFT JOIN (                                                                                    \n");
		sql.append(" 		SELECT VERSION,grade,sex,MAX(sg) sg_max,MIN(sg) sg_min                                       \n");
		sql.append(" 		FROM Standard_Nutrition                                                                      \n");
		sql.append(" 		GROUP BY VERSION,grade,sex                                                                   \n");
		sql.append(" 		)t ON s.version = t.version AND s.grade = t.grade AND s.sex = t.sex                          \n");
		sql.append(" 	WHERE s.version=").append(maxVersion).append(" AND s.grade=r.GRADEBH AND s.sex=r.XB                    \n");
		sql.append(" 	AND ((ROUND(r.SG,0)<t.sg_min AND s.sg=t.sg_min) OR (ROUND(r.SG,0)>t.sg_max AND s.sg=t.sg_max)) \n");
		sql.append(" )                                                                                               \n");
		return sql;
	}

	private StringBuffer appendStandarSelectSQL5Level(StringBuffer sql, String checkValueColumn, String standarTable) {
		String versionCode = checkValueColumn;
		String filter = " and r.NL=s.age ";
		//		if ("Standard_Nutrition".equals(standarTable)) {
		//			versionCode = "YY";
		//			filter = " and r.GRADEBH=s.grade and round(r.SG,0)=s.SG ";
		//		}
		String maxVersion = AppCache.getInstance().getMaxVersion(versionCode);
		sql.append("\n\t(select case when r.").append(checkValueColumn).append("<P10 then '下等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P10 and r.").append(checkValueColumn).append("<P25 then '中下等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P25 and r.").append(checkValueColumn).append("<P75 then '中等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P75 and r.").append(checkValueColumn).append("<P90 then '中上等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P90 then '上等'");
		sql.append("\n\t\telse null end");
		sql.append("\n\tfrom ").append(standarTable).append(" s");
		sql.append("\n\twhere r.XB=s.sex and version=").append(maxVersion).append(filter).append(")");
		return sql;
	}

	private StringBuffer appendStandarSelectSQL3Level(StringBuffer sql, String checkValueColumn, String standarTable) {
		String maxVersion = AppCache.getInstance().getMaxVersion(checkValueColumn);
		sql.append("\n\t(select case when r.").append(checkValueColumn).append("<P10 then '下等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P10 and r.").append(checkValueColumn).append("<P90 then '中等'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P90 then '上等'");
		sql.append("\n\t\telse null end");
		sql.append("\n\tfrom ").append(standarTable).append(" s");
		sql.append("\n\twhere r.XB=s.sex and r.NL=s.age and version=").append(maxVersion).append(")");
		return sql;
	}

	private StringBuffer appendStandarSelectSQL3Level4BMI(StringBuffer sql, String checkValueColumn, String standarTable) {
		String maxVersion = AppCache.getInstance().getMaxVersion(checkValueColumn);
		sql.append("\n\t(select case when r.SG is null or r.SG=0 then null");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append("<P1 then '正常'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P1 and r.").append(checkValueColumn).append("<P2 then '超重'");
		sql.append("\n\t\twhen r.").append(checkValueColumn).append(">=P2 then '肥胖'");
		sql.append("\n\t\telse null end");
		sql.append("\n\tfrom ").append(standarTable).append(" s");
		sql.append("\n\twhere r.XB=s.sex and r.NL=s.age and version=").append(maxVersion).append(")");
		return sql;
	}

	@Override
	public int recalculateStatRptByResult(Set<String> idSet) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("id", idSet);
		return recalculateStatRptByResult(" where result.id in (:id)\n", params);
	}

	@Override
	public int recalculateStatRptByResult(String sqlFilter, Map<String, Object> params) {
		return recalculateStatRptByResult(sqlFilter, params, false);
	}

	@Override
	public int recalculateStatRptByResult4Delete(String schoolNo, int tjnd, boolean isUseThread) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("schoolBh", schoolNo);
		params.put("tjnd", tjnd);
		return recalculateStatRptByResult(" where schoolBh = :schoolBh AND tjnd=:tjnd \n", params, isUseThread, true);
	}

	@Override
	public int recalculateStatRptByResult(String sqlFilter, Map<String, Object> params, boolean isUseThread) {
		return recalculateStatRptByResult(sqlFilter, params, isUseThread, false);
	}

	private int recalculateStatRptByResult(String sqlFilter, Map<String, Object> params, boolean isUseThread, boolean isForDelete) {
		int result = -1;
		Session currentSession = getSessionFactory().getCurrentSession();
		//		isUseThread = false;
		if (!isUseThread) {
			result = inner_recalculateStatRptByResult(3, currentSession, sqlFilter, params, isForDelete);
		} else {
			logger.info("新的线程----------------------------------------");
			RecalculateStatRpt newInstance = new RecalculateStatRpt(getSessionFactory().openSession(), sqlFilter, params, isForDelete);
			Thread t = new Thread(newInstance);
			t.start();
			result = inner_recalculateStatRptByResult(1, currentSession, sqlFilter, params, isForDelete);//分步骤执行
		}
		return result;
	}

	class RecalculateStatRpt implements Runnable {
		private Session currentSession;
		String sqlFilter;
		Map<String, Object> params;
		boolean isForDelete;
		int result;

		RecalculateStatRpt(Session currentSession, String sqlFilter, Map<String, Object> params, boolean isForDelete) {
			this.currentSession = currentSession;
			this.sqlFilter = sqlFilter;
			this.params = params;
			this.isForDelete = isForDelete;
		}

		@Override
		public void run() {
			Transaction transaction = currentSession.beginTransaction();
			try {
				result = inner_recalculateStatRptByResult(2, currentSession, sqlFilter, params, isForDelete);
				transaction.commit();
			} catch (HibernateException ee) {
				transaction.rollback();
				throw ee;
			} finally {
				currentSession.close();
			}
		}

		public int getResult() {
			return result;
		}
	}

	public int inner_recalculateStatRptByResult(int step, Session currentSession, String sqlFilter, Map<String, Object> params, boolean isForDelete) {
		//sqlFilter条件，要使用子查询
		int result = 0;
		StringBuffer sql = new StringBuffer(2000);
		long d10 = System.currentTimeMillis();
		if (step == 1 || step == 3) {
			//身高体重胸围肺活量评价报表，按年龄统计。删除
			appendDeleteSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Age", true, sqlFilter, isForDelete);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Age", "01", true, "SG", "TZ", sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Age", "02", true, "XW", "FHL", sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d20 = System.currentTimeMillis();
			logger.info("计算身高体重胸围肺活量评价报表（按年龄统计） 耗时：" + (d20 - d10) + "ms.");

			//身高体重胸围肺活量评价报表，按年级统计。删除
			sql.setLength(0);
			appendDeleteSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Grade", false, sqlFilter, isForDelete);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Grade", "01", false, "SG", "TZ", sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4StatureWeightBustLungCapacity(sql, "Report_TB_Grade", "02", false, "XW", "FHL", sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d30 = System.currentTimeMillis();
			logger.info("计算身高体重胸围肺活量评价报表（按年级统计） 耗时：" + (d30 - d20) + "ms.");
		}
		if (step == 2 || step == 3) {
			long d30 = System.currentTimeMillis();
			//视力、牙齿、内科、其他，删除
			sql.setLength(0);
			appendDeleteSQL4StatureWeightBustLungCapacity(sql, "Report_ZB_Age", true, sqlFilter, isForDelete);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendDeleteSQL4StatureWeightBustLungCapacity(sql, "Report_ZB_Grade", false, sqlFilter, isForDelete);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d40 = System.currentTimeMillis();
			logger.info("删除视力、牙齿、内科、其他 耗时：" + (d40 - d30) + "ms.");

			//视力01
			sql.setLength(0);
			appendInsertSQL4RptStatSign(sql, "Report_ZB_Age", true, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4RptStatSign(sql, "Report_ZB_Grade", false, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d50 = System.currentTimeMillis();
			logger.info("计算视力 耗时：" + (d50 - d40) + "ms.");

			//牙齿02
			sql.setLength(0);
			appendInsertSQL4RptStatTool(sql, "Report_ZB_Age", true, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4RptStatTool(sql, "Report_ZB_Grade", false, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d60 = System.currentTimeMillis();
			logger.info("计算牙齿 耗时：" + (d60 - d50) + "ms.");

			//内科03
			sql.setLength(0);
			appendInsertSQL4RptStatInternalMedicine(sql, "Report_ZB_Age", true, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4RptStatInternalMedicine(sql, "Report_ZB_Grade", false, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d70 = System.currentTimeMillis();
			logger.info("计算内科 耗时：" + (d70 - d60) + "ms.");

			//其他04
			sql.setLength(0);
			appendInsertSQL4RptStatOther(sql, "Report_ZB_Age", true, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendInsertSQL4RptStatOther(sql, "Report_ZB_Grade", false, sqlFilter);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d80 = System.currentTimeMillis();
			logger.info("计算其他 耗时：" + (d80 - d70) + "ms.");
			params = null;
			sql.setLength(0);
			appendCalculateStudentCount(sql, "Report_TB_Age", true);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendCalculateStudentCount(sql, "Report_TB_Grade", false);
			result += commonExecuteUpdate(currentSession, sql, params);

			//						currentSession.getTransaction().commit();
			//						currentSession.getTransaction().begin();
			sql.setLength(0);
			appendCalculateStudentCount(sql, "Report_ZB_Age", "Report_TB_Age", true);
			result += commonExecuteUpdate(currentSession, sql, params);

			sql.setLength(0);
			appendCalculateStudentCount(sql, "Report_ZB_Grade", "Report_TB_Grade", false);
			result += commonExecuteUpdate(currentSession, sql, params);

			long d90 = System.currentTimeMillis();
			logger.info("计算学校学生人数 耗时：" + (d90 - d80) + "ms.");
		}
		return result;
	}

	private void appendCalculateStudentCount(StringBuffer sql, String rptTable, boolean isAgeTable) {
		int year = GlobalVariables.getGlobalVariables().getYear();
		String schoolFilterStr = isAgeTable ? "and rpt.TJND-year(Student.bornDate) = rpt.Age" : "and Student.gradeNo = rpt.Grade";
		sql.append(" update ").append(rptTable).append(" rpt set XS = \n");
		sql.append(" (select count(*) from Student where Student.schoolNo=rpt.schoolBH \n");
		sql.append("     and (Student.status is null or Student.status ='T') and Student.sex=rpt.XB ").append(schoolFilterStr).append(")  \n");
		sql.append(" where XS = -1 and term = 1 and tjnd = ").append(year);
	}

	private void appendCalculateStudentCount(StringBuffer sql, String rptTable, String srcRptTable, boolean isAgeTable) {
		String schoolFilterStr = isAgeTable ? "and srcRpt.Age = rpt.Age" : "and srcRpt.Grade = rpt.Grade";
		sql.append(" update ").append(rptTable).append(" rpt set XS = \n");
		sql.append(" (select srcRpt.XS from ").append(srcRptTable).append(" srcRpt \n");
		sql.append("     where srcRpt.tjnd=rpt.tjnd and srcRpt.term=rpt.term and srcRpt.schoolBH=rpt.schoolBH and srcRpt.SchoolType = rpt.SchoolType \n");
		sql.append("     and srcRpt.RID = '01' and srcRpt.XB=rpt.XB ").append(schoolFilterStr).append(")  \n");
		sql.append(" where XS = -1 ");
	}

	private int commonExecuteUpdate(Session currentSession, StringBuffer sql, Map<String, Object> params) {
		SQLQuery query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		return query.executeUpdate();
	}

	private void appendSchoolCount(StringBuffer sql, boolean isAgeTable) {
		//		String schoolFilterStr = isAgeTable ? "and result.TJND-year(Student.bornDate) = result.NL" : "and Student.gradeNo = result.GradeBH";
		//		sql.append("   , (select count(*) from Student where Student.schoolNo=result.SCHOOLBH    \n");
		//		sql.append("     and (Student.status is null or Student.status ='T') and Student.sex=result.XB ").append(schoolFilterStr).append(")  \n");
		sql.append("   , -1");
	}

	private void appendSchoolType(StringBuffer sql) {
		sql.append(" , case substring(result.gradeBh,1,1) when 1 then '小学' when 2 then '初中' when 3 then '高中' when 4 then '大学' else ''end \n");
	}

	private void appendDeleteSQL4StatureWeightBustLungCapacity(StringBuffer sql, String rptTable, boolean isAgeTable, String sqlFilter,
			boolean isForDelete) {
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" delete rpt from ").append(rptTable).append(" rpt                             \n");
		if (isForDelete) {
			sql.append(sqlFilter);
		} else {
			sql.append(" inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
			sql.append(sqlFilter);
			sql.append(") resultTemp on rpt.tjnd=resultTemp.tjnd and rpt.term=resultTemp.term    \n");
			sql.append(" and rpt.xb=resultTemp.xb and rpt.schoolBH=resultTemp.schoolBH   \n");
			sql.append(" and rpt.").append(rptColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");//and rpt.age = result.NL
		}
	}

	private void appendInsertSQL4StatureWeightBustLungCapacity(StringBuffer sql, String rptTable, String rid, boolean isAgeTable,
			String resultColumnName1, String resultColumnName2, String sqlFilter) {
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" insert into ").append(rptTable);
		sql.append("(ID, RID, TJND, Term, XB, CITYID, DISTRICTID, SCHOOLBH, SCHOOLTYPE, ").append(rptColumnName);
		sql.append(", XS,SJ,H1,H2,H3,H4,H5,H6,H15B,H16B, H7,H8,H9,H10,H11,H12,H17B,H18B) \n");
		sql.append(" (select uuid(),'").append(rid);
		sql.append("', result.TJND, result.Term, result.XB, cityTree.code, distTree.code, result.SCHOOLBH \n");
		appendSchoolType(sql);
		sql.append(", result.").append(resultSelectColumnName).append("\n");
		appendSchoolCount(sql, isAgeTable);
		sql.append("   , count(*) \n");//SJ
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append(" is not null then 1 else 0 end)                          \n");//H1
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append("DJQ='上等' then 1 else 0 end)                               \n");//H2
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append("DJQ='中上等' then 1 else 0 end)                              \n");//H3
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append("DJQ='中等' then 1 else 0 end)                                \n");//H4
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append("DJQ='中下等' then 1 else 0 end)                              \n");//H5
		sql.append("   , sum(case WHEN ").append(resultColumnName1).append("DJQ='下等' then 1 else 0 end)                                \n");//H6
		sql.append("   , sum(ifnull(").append(resultColumnName1).append(",0))");//H15B
		sql.append("   , sum(ifnull(").append(resultColumnName1).append("*").append(resultColumnName1).append(",0)) \n");//H16B
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append(" is not null then 1 else 0 end)                          \n");//H7
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append("DJQ='上等' then 1 else 0 end)                               \n");//H8
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append("DJQ='中上等' then 1 else 0 end)                              \n");//H9
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append("DJQ='中等' then 1 else 0 end)                                \n");//H10
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append("DJQ='中下等' then 1 else 0 end)                              \n");//H11
		sql.append("   , sum(case WHEN ").append(resultColumnName2).append("DJQ='下等' then 1 else 0 end)                                \n");//H12
		sql.append("   , sum(ifnull(").append(resultColumnName2).append(",0))");//H17B
		sql.append("   , sum(ifnull(").append(resultColumnName2).append("*").append(resultColumnName2).append(",0)) \n");//H18B
		sql.append("   from Result result                                                                                              \n");
		sql.append(" inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
		sql.append(sqlFilter);
		sql.append(") resultTemp on result.tjnd=resultTemp.tjnd and result.term=resultTemp.term    \n");
		sql.append(" and result.xb=resultTemp.xb and result.schoolBH=resultTemp.schoolBH   \n");
		sql.append(" and result.").append(resultSelectColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");
		sql.append("   inner join SchoolTree schoolTree on result.SCHOOLBH=schoolTree.code \n");
		sql.append("   inner join SchoolTree distTree on schoolTree.parentCode=distTree.code \n");
		sql.append("   inner join SchoolTree cityTree on distTree.parentCode=cityTree.code \n");
		sql.append("   group by result.TJND, result.Term, result.XB, result.SCHOOLBH, substring(result.gradeBh,1,1)");
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		sql.append(" )                                                                                                                 \n");
	}

	private void appendInsertSQL4RptStatSign(StringBuffer sql, String rptTable, boolean isAgeTable, String sqlFilter) {
		//视力
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" insert into ").append(rptTable);
		sql.append(" (ID, RID, TJND, Term, XB, CITYID, DISTRICTID, SCHOOLBH, SCHOOLTYPE, ").append(rptColumnName);
		sql.append(", XS, SJ, H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11)\n");
		sql.append(" (select uuid(), '01', result.TJND, result.Term, result.XB, cityTree.code, distTree.code, result.SCHOOLBH \n");
		appendSchoolType(sql);
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		appendSchoolCount(sql, isAgeTable);
		sql.append("   , sum(case WHEN result.LSL is not null or result.RSL is not null or result.BS is not null or result.LSY is not null or result.RSY is not null then 1 else 0 end) \n");//SJ
		sql.append("   , sum(case when !(result.LSL is null and result.RSL is null and result.BS is null and result.JMY is null and result.LSY is null and result.RSY is null) \n");//H1
		sql.append("             and (result.LSL>4.9 or result.LSL is null) and (result.RSL>4.9  or result.RSL is null) and (result.BS=0 or result.BS is null) \n");//H1
		sql.append("             and (result.JMY=0 or result.JMY is null) and (result.LSY=0 or result.LSY is null) and (result.RSY=0 or result.RSY is null) then 1 else 0 end) \n");//H1
		sql.append("   , sum(case WHEN result.LSL>4.9 and result.RSL>4.9  then 1 else 0 end)                                     \n");//H2
		sql.append("   , sum(case WHEN least(result.LSL, result.RSL)=4.9  then 1 else 0 end)                                     \n");//H3
		sql.append("   , sum(case WHEN least(result.LSL, result.RSL)>=4.6 and least(result.LSL, result.RSL)<4.9 then 1 else 0 end)\n");//H4
		sql.append("   , sum(case WHEN least(result.LSL, result.RSL)<4.6 then 1 else 0 end)                                      \n");//H5
		sql.append("   , sum(case WHEN least(result.LSL, result.RSL)<=4.9 then 1 else 0 end)                                     \n");//H6
		sql.append("   , sum(case WHEN (lastYear.LSL>4.9 and lastYear.RSL>4.9) and (result.LSL<5.0 or result.RSL<5.0) then 1 else 0 end)\n");//H7
		sql.append("   , sum(case WHEN result.BS<>0 then 1 else 0 end)                                                           \n");
		sql.append("   , sum(case WHEN result.LSY=2 or result.RSY=2 then 1 else 0 end)                                           \n");
		sql.append("   , sum(case WHEN result.JMY=1 then 1 else 0 end)                                                           \n");
		sql.append("   , sum(case WHEN (lastYear.LSL>4.9 and lastYear.RSL>4.9) then 1 else 0 end)                                \n");//H11
		sql.append("   from Result result                                                                                        \n");
		sql.append("   inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
		sql.append(sqlFilter);
		sql.append("   ) resultTemp on result.tjnd=resultTemp.tjnd and result.term=resultTemp.term    \n");
		sql.append("     and result.xb=resultTemp.xb and result.schoolBH=resultTemp.schoolBH   \n");
		sql.append("     and result.").append(resultSelectColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");
		sql.append("   inner join SchoolTree schoolTree on result.SCHOOLBH=schoolTree.code \n");
		sql.append("   inner join SchoolTree distTree on schoolTree.parentCode=distTree.code \n");
		sql.append("   inner join SchoolTree cityTree on distTree.parentCode=cityTree.code \n");
		sql.append("   left join Result lastYear on result.TJND-1=lastYear.TJND and result.XM=lastYear.XM and result.CSRQ=lastYear.CSRQ and result.SCHOOLBH=lastYear.SCHOOLBH\n");
		sql.append("   group by result.TJND, result.Term, result.XB, result.SCHOOLBH, substring(result.gradeBh,1,1)");
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		sql.append(" )\n");
	}

	private void appendInsertSQL4RptStatTool(StringBuffer sql, String rptTable, boolean isAgeTable, String sqlFilter) {
		//牙齿
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" insert into ").append(rptTable);
		sql.append(" (ID, RID, TJND, Term, XB, CITYID, DISTRICTID, SCHOOLBH, SCHOOLTYPE, ").append(rptColumnName);
		sql.append(", XS, SJ, H1,H2,H2B,H3,H4,H5,H6,H7,H8,H9,H10,H11)\n");
		sql.append(" (select uuid(), '02', result.TJND, result.Term, result.XB, cityTree.code, distTree.code, result.SCHOOLBH \n");
		appendSchoolType(sql);
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		appendSchoolCount(sql, isAgeTable);
		sql.append("   , sum(case WHEN QSBNUM is not null or RQB is not null or RQS is not null or RQH is not null or HQB is not null or HQS is not null or HQH is not null or YZB is not null then 1 else 0 end) \n");//SJ
		sql.append("   , sum(case when QSBNUM<>0 then 1 else 0 end)                   \n");//H1
		sql.append("   , sum(ifnull(QSBNUM,0))                                                  \n");//H2
		sql.append("   , case WHEN count(*)=0 then 0 else QSBNUM/count(*) end         \n");//H2B
		sql.append("   , sum(case when HQB<>0 or HQS<>0 or HQH<>0 then 1 else 0 end)  \n");//H7
		sql.append("   , sum(ifnull(HQB,0))                                                     \n");//H8
		sql.append("   , sum(ifnull(HQS,0))                                                     \n");//H9
		sql.append("   , sum(ifnull(HQH,0))                                                     \n");//H10
		sql.append("   , sum(case when RQB<>0 or RQS<>0 or RQH<>0 then 1 else 0 end)  \n");//H3
		sql.append("   , sum(ifnull(RQB,0))                                                     \n");//H4
		sql.append("   , sum(ifnull(RQS,0))                                                     \n");//H5
		sql.append("   , sum(ifnull(RQH,0))                                                     \n");//H6
		sql.append("   , sum(case when YZB<>0 then 1 else 0 end)                      \n");//H11
		sql.append("   from Result result                                                                                        \n");
		sql.append(" inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
		sql.append(sqlFilter);
		sql.append(") resultTemp on result.tjnd=resultTemp.tjnd and result.term=resultTemp.term    \n");
		sql.append(" and result.xb=resultTemp.xb and result.schoolBH=resultTemp.schoolBH   \n");
		sql.append(" and result.").append(resultSelectColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");
		sql.append("   inner join SchoolTree schoolTree on result.SCHOOLBH=schoolTree.code \n");
		sql.append("   inner join SchoolTree distTree on schoolTree.parentCode=distTree.code \n");
		sql.append("   inner join SchoolTree cityTree on distTree.parentCode=cityTree.code \n");
		sql.append("   group by result.TJND, result.Term, result.XB, result.SCHOOLBH, substring(result.gradeBh,1,1)");
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		sql.append(" )\n");
	}

	private void appendInsertSQL4RptStatInternalMedicine(StringBuffer sql, String rptTable, boolean isAgeTable, String sqlFilter) {
		//内科
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" insert into ").append(rptTable);
		sql.append(" (ID, RID, TJND, Term, XB, CITYID, DISTRICTID, SCHOOLBH, SCHOOLTYPE, ").append(rptColumnName);
		sql.append(", XS, SJ, H1,H2,H3,H4,H5,H6,H7,H8,H9,H10)\n");
		sql.append(" (select uuid(), '03', result.TJND, result.Term, result.XB, cityTree.code, distTree.code, result.SCHOOLBH \n");
		appendSchoolType(sql);
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		appendSchoolCount(sql, isAgeTable);
		sql.append("   , sum(case WHEN YYPJ is not null or PXPJ is not null or JHJS is not null or GBZAM is not null or DHSA is not null or SSYPJ is not null or SZYPJ is not null or JZX is not null or JZ is not null or FHCL is not null then 1 else 0 end) \n");//SJ
		sql.append("   , sum(case when YYPJ='超重' then 1 else 0 end)                      \n");//H1
		sql.append("   , sum(case when YYPJ='肥胖' then 1 else 0 end)                         \n");//H2
		sql.append("   , sum(case when PXPJ='低血红蛋白' then 1 else 0 end)                     \n");//H3
		sql.append("   , sum(case when JHJS=0 or JHJS=1 or JHJS=3 then 1 else 0 end)        \n");//H4
		sql.append("   , sum(case when GBZAM>40 then 1 else 0 end)                          \n");//H5
		sql.append("   , sum(case when DHSA>21 then 1 else 0 end)                           \n");//H6
		sql.append("   , sum(case when SSYPJ='高血压' or SZYPJ='高血压' then 1 else 0 end)      \n");//H7
		sql.append("   , sum(case when JZX=1 then 1 else 0 end)                             \n");//H8
		sql.append("   , sum(case when JZ=2 or JZ=3 or Jz=4 then 1 else 0 end)              \n");//H9
		sql.append("   , sum(case when FHCL=1 then 1 else 0 end)                            \n");//H10
		sql.append("   from Result result                                                                                        \n");
		sql.append(" inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
		sql.append(sqlFilter);
		sql.append(") resultTemp on result.tjnd=resultTemp.tjnd and result.term=resultTemp.term    \n");
		sql.append(" and result.xb=resultTemp.xb and result.schoolBH=resultTemp.schoolBH   \n");
		sql.append(" and result.").append(resultSelectColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");
		sql.append("   inner join SchoolTree schoolTree on result.SCHOOLBH=schoolTree.code \n");
		sql.append("   inner join SchoolTree distTree on schoolTree.parentCode=distTree.code \n");
		sql.append("   inner join SchoolTree cityTree on distTree.parentCode=cityTree.code \n");
		sql.append("   group by result.TJND, result.Term, result.XB, result.SCHOOLBH, substring(result.gradeBh,1,1)");
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		sql.append(" )\n");
	}

	private void appendInsertSQL4RptStatOther(StringBuffer sql, String rptTable, boolean isAgeTable, String sqlFilter) {
		//其它
		String rptColumnName = isAgeTable ? "Age" : "Grade";
		String resultSelectColumnName = isAgeTable ? "NL" : "gradeBh";
		sql.append(" insert into ").append(rptTable);
		sql.append(" (ID, RID, TJND, Term, XB, CITYID, DISTRICTID, SCHOOLBH, SCHOOLTYPE, ").append(rptColumnName);
		sql.append(", XS, SJ, H1,H2,H3,H4,H5,H6,H7,H8,H9,H10)\n");
		sql.append(" (select uuid(), '04', result.TJND, result.Term, result.XB, cityTree.code, distTree.code, result.SCHOOLBH \n");
		appendSchoolType(sql);
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		appendSchoolCount(sql, isAgeTable);
		sql.append("   , sum(case WHEN XZ is not null or GP is not null or PEI is not null or FEI is not null or SZ is not null or PFB is not null or EB is not null or YB is not null or TB is not null or JB is not null or PF is not null then 1 else 0 end) \n");//SJ
		sql.append("   , sum(case when XZ<>0 and XZ is not null then 1 else 0 end)               \n");//H1 心
		sql.append("   , sum(case WHEN GP=1 then 1 else 0 end)                                   \n");//H2 肝
		sql.append("   , sum(case when PEI=1 then 1 else 0 end)                                  \n");//H3 脾
		sql.append("   , sum(case when FEI=1 then 1 else 0 end)                                  \n");//H4 肺
		sql.append("   , sum(case when SZ<>0 and SZ is not null then 1 else 0 end)               \n");//H5 四肢
		sql.append("   , sum(case when PFB<>0 and PFB is not null then 1 else 0 end)             \n");//H6 皮肤
		sql.append("   , sum(case when (TL<>0 and TL is not null) or (RTL<>0 and RTL is not null) then 1 else 0 end)             \n");//H7 听力
		sql.append("   , sum(case when EB<>0 and EB is not null then 1 else 0 end)             \n");//H8 耳
		sql.append("   , sum(case when BB<>0 and BB is not null  then 1 else 0 end)             \n");//H9 鼻
		sql.append("   , sum(case when BTT<>0 and BTT is not null then 1 else 0 end)             \n");//H10 扁桃体
		//		sql.append("   , sum(case when (EB<>0 and EB is not null) or (YB<>0 and YB is not null)  \n");//H7
		//		sql.append("         or TB=1 or JB=1 or PF=1 then 1 else 0 end)                          \n");
		sql.append("   from Result result                                                                                        \n");
		sql.append(" inner join ( select distinct tjnd,term,xb,schoolBH,").append(resultSelectColumnName).append(" from result \n");
		sql.append(sqlFilter);
		sql.append(") resultTemp on result.tjnd=resultTemp.tjnd and result.term=resultTemp.term    \n");
		sql.append(" and result.xb=resultTemp.xb and result.schoolBH=resultTemp.schoolBH   \n");
		sql.append(" and result.").append(resultSelectColumnName).append("=resultTemp.").append(resultSelectColumnName).append(" \n");
		sql.append("   inner join SchoolTree schoolTree on result.SCHOOLBH=schoolTree.code \n");
		sql.append("   inner join SchoolTree distTree on schoolTree.parentCode=distTree.code \n");
		sql.append("   inner join SchoolTree cityTree on distTree.parentCode=cityTree.code \n");
		sql.append("   group by result.TJND, result.Term, result.XB, result.SCHOOLBH, substring(result.gradeBh,1,1)");
		sql.append(", result.").append(resultSelectColumnName).append(" \n");
		sql.append(" )\n");
	}

	@Override
	public int updateStudentInfo4Result(String classLongNo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE result a ");
		sql.append(" SET a.csrq = ");
		sql.append(" ( SELECT b.bornDate FROM student b WHERE b.studentCode=a.StudentCode AND b.schoolNo=a.schoolBh ) ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND a.tjnd = :tjnd ");
		sql.append(" AND a.classLongNo = :classLongNo ");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		String tjnd = GlobalVariables.getGlobalVariables().getYear() + "";
		query.setParameter("tjnd", tjnd);
		query.setParameter("classLongNo", classLongNo);
		logger.info("sql:\n" + sql.toString());
		logger.info("param classLongNo=" + classLongNo + ", tjnd=" + tjnd);
		return query.executeUpdate();
	}

	@Override
	public void removeByClassLongNumber(String classLongNo) {
		classLongNo = classLongNo.trim();
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   classLongNo = '").append(classLongNo).append("' ");
		sql.append(getYearConition());
		logger.info("sql:\n" + sql);
		getSessionFactory().getCurrentSession().createQuery(sql.toString()).executeUpdate();
	}

	@Override
	public void removeByStudentCode(String[] studentCodes, String classLongNumber) {
		String studentInSQL = SQLUtils.getInStatement(studentCodes);
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM CheckResult ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND   studentCode IN (").append(studentInSQL).append(") ");
		sql.append(" AND   classLongNo = '").append(classLongNumber).append("' ");
		sql.append(getYearConition());
		logger.info("sql:\n" + sql);
		getSessionFactory().getCurrentSession().createQuery(sql.toString()).executeUpdate();
	}

	@Override
	public void save(List<CheckResult> list, String year) {
		super.save(list);
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE result r                                                               \n");
		sql.append(" LEFT JOIN schooltree st ON r.SCHOOLBH = st.parentCode AND r.CLASSBH = st.code \n");
		sql.append(" SET r.tjnd = :year, r.studentCode=r.xh, r.CLASSLONGNO = st.longNumber              \n");
		sql.append(" WHERE tjnd IS NULL                                                            \n");
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("year", year);
		query.executeUpdate();
	}
}