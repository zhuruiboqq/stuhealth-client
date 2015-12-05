/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.jasperreports.engine.util.Pair;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.GlobalVariables;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import com.vastcm.stuhealth.client.framework.exception.BizRunTimeException;
import com.vastcm.stuhealth.client.utils.HqlUtil;
import com.vastcm.stuhealth.client.utils.biz.SQLImportDataUtil;
import com.vastcm.stuhealth.client.utils.biz.StudentCode;

/**
 * 
 * @author House
 */
public class StudentService extends CoreService<Student> implements IStudentService {

	private Logger logger = LoggerFactory.getLogger(StudentService.class);
	private static final String ClassNo_Graduate_Pre_String = "g_";

	public void remove(String[] ids) {
		StringBuilder sb = new StringBuilder();
		for (String id : ids) {
			sb.append("'").append(id).append("',");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		String sql = " DELETE FROM " + getEntityClass().getSimpleName() + " WHERE id IN (" + sb + ")";
		logger.debug("sql:\n" + sql);
		getSessionFactory().getCurrentSession().createQuery(sql).executeUpdate();
	}

	@Override
	public void removeByClassLongNumber(String classLongNumber) {
		String sql = " DELETE FROM " + getEntityClass().getSimpleName() + " WHERE classLongNumber = '" + classLongNumber + "'";
		logger.debug("sql:\n" + sql);
		getSessionFactory().getCurrentSession().createQuery(sql).executeUpdate();
	}

	@Override
	public void save(Student entity) {
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query;
		int count;
		StringBuffer sql = new StringBuffer();

		Date oldBornDate = null;
		if (entity.getId() != null) {
			query = currentSession.createSQLQuery("select bornDate from student where id=:id");
			query.setString("id", entity.getId());
			oldBornDate = (Date) query.uniqueResult();
		}
		boolean isChangeDate = oldBornDate != null && entity.getBornDate() != null && !entity.getBornDate().equals(oldBornDate);
		super.save(entity);
		if (isChangeDate) {
			//根据学生代号，重新更新体检表中学生的出生日期和年龄
			sql.setLength(0);
			sql.append(" update result set CSRQ=:CSRQ,NL=(TJND-year(:CSRQ)) where studentCode=:studentCode and schoolBh=:schoolBh ");
			query = currentSession.createSQLQuery(sql.toString());
			query.setDate("CSRQ", entity.getBornDate());
			query.setString("studentCode", entity.getStudentCode());
			query.setString("schoolBh", entity.getSchoolNo());
			count = query.executeUpdate();
			if (count == 0)
				return;
			//重算评价和报表统计
			sql.setLength(0);
			sql.append(" select id from result where studentCode=:studentCode and schoolBh=:schoolBh");
			query = currentSession.createSQLQuery(sql.toString());
			query.setString("studentCode", entity.getStudentCode());
			query.setString("schoolBh", entity.getSchoolNo());
			List<Object> listData = query.list();
			if (listData == null || listData.size() == 0)
				return;

			Set<String> idSet = new LinkedHashSet<String>(listData.size());
			for (Object idObj : listData) {
				idSet.add(String.valueOf(idObj));
			}
			ICheckResultService checkResultService = AppContext.getBean("checkResultService", ICheckResultService.class);
			checkResultService.updateEvaluation(idSet);
			checkResultService.recalculateStatRptByResult(idSet);
		}
	}

	@Override
	public Student getStudentByCode(String studentCode, String classLongNumber) {
		String query = " FROM Student WHERE studentCode = '" + studentCode + "' AND classLongNumber = '" + classLongNumber + "' ";
		List<Student> ls = getSessionFactory().getCurrentSession().createQuery(query).list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	@Override
	public List<Student> getStudentsByClass(String classLongNo, boolean includeSubNodes) {
		return getStudentsByClass(classLongNo, includeSubNodes, null, null);
	}

	@Override
	public List<Student> getStudentsByClass(String classLongNo, boolean includeSubNodes, String field, String keyword) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM Student WHERE 1=1 ");
		if (classLongNo != null && classLongNo.trim().length() > 0) {
			if (includeSubNodes) {
				query.append(" AND classLongNumber LIKE '").append(classLongNo).append("%' ");
			} else {
				query.append(" AND classLongNumber = '").append(classLongNo).append("' ");
			}
		}

		if (field != null && !field.isEmpty() && keyword != null && !keyword.isEmpty()) {
			query.append(" AND ").append(field).append(" LIKE '%").append(keyword).append("%' ");
		}
		query.append(" order by classNo, studentCode ");
		return getSessionFactory().getCurrentSession().createQuery(query.toString()).list();

	}

	@Override
	public List<Object[]> getStudentAndSchoolByClass(String classLongNo, boolean includeSubNodes, String field, String keyword) {
		StringBuilder query = new StringBuilder();
		query.append(" FROM Student student,SchoolTreeNode schoolTree WHERE student.schoolNo=schoolTree.code ");
		if (classLongNo != null && classLongNo.trim().length() > 0) {
			if (includeSubNodes) {
				query.append(" AND student.classLongNumber LIKE '").append(classLongNo).append("%' ");
			} else {
				query.append(" AND student.classLongNumber = '").append(classLongNo).append("' ");
			}
		}

		if (field != null && !field.isEmpty() && keyword != null && !keyword.isEmpty()) {
			query.append(" AND student.").append(field).append(" LIKE '%").append(keyword).append("%' ");
		}
		query.append(" order by student.classNo, student.studentCode ");
		return getSessionFactory().getCurrentSession().createQuery(query.toString()).list();

	}

	@Override
	public int changeClassByClass(String srcClassLongNo, String destClassLongNo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM SchoolTreeNode WHERE longNumber = '").append(destClassLongNo).append("' ");
		SchoolTreeNode schoolNode = (SchoolTreeNode) getSessionFactory().getCurrentSession().createQuery(sql.toString()).list().get(0);
		String destClassNo = schoolNode.getCode();
		String destClassName = schoolNode.getName();

		//插入调班记录
		Map<String, Object> params = new LinkedHashMap<String, Object>();

		params.put("classLongNumber", srcClassLongNo);
		insertStudentChangeClass("WHERE classLongNumber = :classLongNumber", params, destClassNo, destClassName, destClassLongNo);

		StringBuilder sqlChangeClass = new StringBuilder();
		sqlChangeClass.append(" UPDATE Student ");
		sqlChangeClass.append(" SET classLongNumber = '").append(destClassLongNo).append("', ");
		sqlChangeClass.append("  classNo = '").append(destClassNo).append("', ");
		sqlChangeClass.append("  className = '").append(destClassName).append("' ");
		sqlChangeClass.append(" WHERE classLongNumber = '").append(srcClassLongNo).append("' ");
		return getSessionFactory().getCurrentSession().createQuery(sqlChangeClass.toString()).executeUpdate();
	}

	@Override
	public int changeClassByStudent(String destClassLongNo, String... studentIDs) {
		Session currentSession = getSessionFactory().getCurrentSession();
		Query query;
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM SchoolTreeNode WHERE longNumber = '").append(destClassLongNo).append("' ");
		SchoolTreeNode schoolNode = (SchoolTreeNode) currentSession.createQuery(sql.toString()).list().get(0);
		String destClassNo = schoolNode.getCode();
		String destClassName = schoolNode.getName();
		//插入调班记录
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("id", studentIDs);
		insertStudentChangeClass("WHERE id in (:id)", params, destClassNo, destClassName, destClassLongNo);

		StringBuilder sqlChangeClass = new StringBuilder();
		sqlChangeClass.append(" UPDATE Student ");
		sqlChangeClass.append(" SET classLongNumber = '").append(destClassLongNo).append("', ");
		sqlChangeClass.append("  classNo = '").append(destClassNo).append("', ");
		sqlChangeClass.append("  className = '").append(destClassName).append("' ");
		sqlChangeClass.append(" WHERE 1=1 ");
		sqlChangeClass.append(" AND id IN (:id)");
		query = currentSession.createQuery(sqlChangeClass.toString());
		HqlUtil.setParas(query, params);
		return query.executeUpdate();
	}

	@Override
	public String isUpgradeble(String schoolCode) {
		StringBuilder sql = new StringBuilder();
		//按学校类型和年级类型，如果有多个校区时，无法取年级名称
		sql.append(" SELECT left(code, 2) gradeCode, count(*) classCount                \n");
		sql.append(" FROM SchoolTree                                                    \n");
		sql.append(" WHERE 1=1 AND type=40 and parentCode=?                             \n");
		sql.append(" GROUP BY left(code, 2)                                             \n");
		sql.append(" order by code                                                      \n");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString(0, schoolCode);
		List ls = query.list();
		if (ls.size() == 0) {
			return "所选学校没有班级，不允许做一键升学！";
		}
		int[] classCount = new int[10];//小学，初中、高中、大学分别统计
		String[][] gradeName = { { "小学一年级", "小学二年级", "小学三年级", "小学四年级", "小学五年级", "小学六年级" }, { "初中一年级", "初中二年级", "初中三年级" },
				{ "高中一年级", "高中二年级", "高中三年级" }, { "大学一年级", "大学二年级", "大学三年级", "大学三年级" } };
		for (Object obj : ls) {
			Object[] rs = (Object[]) obj;
			int gradeCode = Integer.valueOf(rs[0].toString());
			int schoolType = gradeCode / 10;//小学，初中、高中、大学分别统计
			gradeCode = gradeCode % 10;//几年级
			int count = ((BigInteger) rs[1]).intValue();
			if (classCount[schoolType] == 0) {
				classCount[schoolType] = count;
			} else if (classCount[schoolType] != count) {
				return gradeName[schoolType - 1][gradeCode - 2] + "有" + classCount[schoolType] + "个班，" + gradeName[schoolType - 1][gradeCode - 1]
						+ "有" + count + "个班，两个年级的班数不一样，请先维护班级，才能做升学";
			}
		}
		return null;
	}

	public String isUpgradeble_old(String schoolCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT left(code, 2) gradeCode                                     \n");
		sql.append("   , left(name, char_length(name)-5) gradeName, count(*) classCount \n");
		sql.append(" FROM SchoolTree                                                    \n");
		sql.append(" WHERE 1=1 AND type=40 and parentCode=?                             \n");
		sql.append(" GROUP BY left(code, 2), left(name, char_length(name)-5)            \n");
		sql.append(" order by code                                                      \n");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString(0, schoolCode);
		List ls = query.list();
		if (ls.size() == 0) {
			return "所选学校没有班级，不允许做一键升学！";
		}
		int[] classCount = new int[10];//小学，初中、高中、大学分别统计
		String preGradeName = null;
		int preGradeCode = 0;
		for (Object obj : ls) {
			Object[] rs = (Object[]) obj;
			int gradeCode = Integer.valueOf(rs[0].toString());
			int schoolType = gradeCode / 10;//小学，初中、高中、大学分别统计
			gradeCode = gradeCode % 10;//几年级
			if (preGradeCode == 0) {
				preGradeCode = gradeCode;
			}
			String gradeName = rs[1].toString();
			int count = ((BigInteger) rs[2]).intValue();
			if (classCount[schoolType] == 0) {
				classCount[schoolType] = count;
			} else if (classCount[schoolType] != count) {
				return preGradeName + "有" + classCount[schoolType] + "个班，" + gradeName + "有" + count + "个班，两个年级的班数不一样，请先维护班级，才能做升学";
			}
			preGradeName = gradeName;
			preGradeCode = gradeCode;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int oneKeyUpgrade(String schoolCode) {
		/*
		 * 按办学制度，对毕业班做毕业处理。
		 * 按班级编号排序，低年级顺序升级到高年度
		 */
		//升学，按学校办学制度相应处理毕业班
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("schoolNo", schoolCode);
		StringBuffer sql = new StringBuffer();
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query;
		int result;

		sql.append("select schoolSystem from HW_School where schoolCode=:schoolNo");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		List schoolSystemList = query.list();
		String schoolSystem = null;
		if (schoolSystemList != null && schoolSystemList.size() > 0) {
			schoolSystem = (String) schoolSystemList.get(0);
		}
		if (!StringUtils.hasText(schoolSystem)) {
			schoolSystem = "63";
		}
		String xiaoXueGraduateCode = "1" + schoolSystem.substring(0, 1);
		String chuZhongGraduateCode = "2" + schoolSystem.substring(1, 2);

		//先取得当前所有班级信息
		sql.setLength(0);
		sql.append("select code,name,longNumber from SchoolTree where parentCode=:schoolNo and type=40 order by code ");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		List schoolTreeList = query.list();
		List<SchoolTreeNode> allClassList = new ArrayList<SchoolTreeNode>(schoolTreeList.size());
		List<SchoolTreeNode> srcClassList = new ArrayList<SchoolTreeNode>();
		List<SchoolTreeNode> destClassList = new ArrayList<SchoolTreeNode>();
		//3 4 5
		//3 4
		//(3) (4)
		String oldGradeCode = null;
		List<Integer> gradeClassCountList = new ArrayList<Integer>();
		List<Boolean> isGraduateClassList = new ArrayList<Boolean>();
		//		int gradeClassCountIndex = 0;
		for (Object obj : schoolTreeList) {
			Object[] value = (Object[]) obj;
			SchoolTreeNode node = new SchoolTreeNode();
			node.setCode(String.valueOf(value[0]));
			node.setName(String.valueOf(value[1]));
			node.setLongNumber(String.valueOf(value[2]));
			allClassList.add(node);

			String gradeCode = String.valueOf(node.getCode().substring(0, 2));
			boolean isGraduate = gradeCode.equals(xiaoXueGraduateCode) || gradeCode.equals(chuZhongGraduateCode) || gradeCode.equals("33");
			//毕业班不需要添加到源班级中
			if (!isGraduate) {
				srcClassList.add(node);
			}
			//构造两个班级统计列表，用于添加destClassList
			if (gradeCode.equals(oldGradeCode)) {
				gradeClassCountList.set(gradeClassCountList.size() - 1, gradeClassCountList.get(gradeClassCountList.size() - 1) + 1);
			} else {
				//新的年级
				isGraduateClassList.add(isGraduate);
				gradeClassCountList.add(1);
				oldGradeCode = gradeCode;
			}
		}
		//处理目标班级列表
		int allIndex = 0;
		for (int i = 0, size = gradeClassCountList.size() - 1; i < size; i++) {//最后一个毕业班不用处理
			allIndex += gradeClassCountList.get(i);
			if (!isGraduateClassList.get(i)) {//当前班级不是毕业班
				for (int j = 0; j < gradeClassCountList.get(i); j++) {
					destClassList.add(allClassList.get(allIndex + j));

					//					System.out.println(allClassList.get(allIndex + j).getCode());
				}
			}

		}
		if (srcClassList.size() != destClassList.size()) {
			throw new BizRunTimeException("低年级与高年级对应不上，操作中止！", "低年级：["
					+ com.vastcm.stuhealth.client.utils.StringUtils.toString(srcClassList.iterator(), "，") + "]\n高年级：["
					+ com.vastcm.stuhealth.client.utils.StringUtils.toString(destClassList.iterator(), "，") + "]");
		}
		//先做毕业处理
		sql.setLength(0);
		sql.append("where (classNo like '" + xiaoXueGraduateCode + "%' or classNo like '" + chuZhongGraduateCode
				+ "%' or classNo like '33%') and schoolNo=:schoolNo");
		result = graduate(sql.toString(), params);
		logger.info("毕业：" + result);
		//班级自动升级，排除毕业班
		//		sql.setLength(0);
		//		sql.append(" update student set classNo=concat(substring(classNo, 1, 1),substring(classNo, 2, 1)+1, substring(classNo, 3)) \n");
		//		sql.append(" where classNo not like '").append(ClassNo_Graduate_Pre_String).append("%' and schoolNo=:schoolNo \n");
		//		query = currentSession.createSQLQuery(sql.toString());
		//		HqlUtil.setParas(query, params);
		//		result += query.executeUpdate();
		sql.setLength(0);
		sql.append(" update student set classNo=:destClass \n");
		sql.append(" where classNo = :srcClass and schoolNo=:schoolNo \n");
		query = currentSession.createSQLQuery(sql.toString());
		for (int i = srcClassList.size() - 1; i >= 0; i--) {
			params.put("srcClass", srcClassList.get(i).getCode());
			params.put("destClass", destClassList.get(i).getCode());
			HqlUtil.setParas(query, params);
			result += query.executeUpdate();
		}
		params.remove("srcClass");
		params.remove("destClass");

		// 更新班级信息和年级信息
		sql.setLength(0);
		sql.append(" update student s                                                                             \n");
		sql.append(" left join schooltree st on s.schoolNo = st.parentCode and s.classNo = st.code and st.type=40 \n");
		sql.append(" set s.className=st.name, s.classLongNumber=st.longnumber, s.gradeNo=substring(classNo,1,2)              \n");
		sql.append(" where s.classNo not like '").append(ClassNo_Graduate_Pre_String).append("%' and s.schoolNo=:schoolNo \n");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		query.executeUpdate();

		/*
		 * 插入调班记录
		 */
		int tempResult = 0;
		int year = GlobalVariables.getGlobalVariables().getYear();
		int term = GlobalVariables.getGlobalVariables().getTerm();
		Date today = new Date();
		sql.setLength(0);
		sql.append(" insert into StudentChangeClass(id,studentId,year,term,schoolNo,gradeNo,classNo,destClassNo,destClassName,destClassLongNumber,createTime) \n");
		sql.append(" select uuid(),id,").append(year).append(",").append(term);
		sql.append(",schoolNo,concat(substring(classNo, 1, 1),substring(classNo, 2, 1)-1),concat(substring(classNo, 1, 1),substring(classNo, 2, 1)-1, substring(classNo, 3))");//原班级编号
		sql.append(",classNo,className,classLongNumber,:today \n");//目标班级信息
		sql.append(" from Student \n");
		sql.append(" where classNo not like '").append(ClassNo_Graduate_Pre_String).append("%' and schoolNo=:schoolNo \n");
		query = currentSession.createSQLQuery(sql.toString());
		Map<String, Object> innerParams = new LinkedHashMap<String, Object>();
		if (params != null) {
			innerParams.putAll(params);
		}
		innerParams.put("today", today);
		HqlUtil.setParas(query, innerParams);
		tempResult = query.executeUpdate();
		logger.info("插入调班记录：" + String.valueOf(tempResult));
		//按上面的对应关系，更新原班级编号
		sql.setLength(0);
		sql.append(" update StudentChangeClass set classNo=:srcClass \n");
		sql.append(" where destClassNo = :destClass and schoolNo=:schoolNo and createTime=:today \n");
		params.put("today", today);
		query = currentSession.createSQLQuery(sql.toString());
		tempResult = 0;
		for (int i = srcClassList.size() - 1; i >= 0; i--) {
			params.put("srcClass", srcClassList.get(i).getCode());
			params.put("destClass", destClassList.get(i).getCode());
			HqlUtil.setParas(query, params);
			tempResult += query.executeUpdate();
		}
		params.remove("today");
		params.remove("srcClass");
		params.remove("destClass");
		logger.info("更新调班记录的原班级：" + String.valueOf(tempResult));

		//更新原班级信息
		sql.setLength(0);
		sql.append(" update StudentChangeClass s                                                                  \n");
		sql.append(" left join schooltree st on s.schoolNo = st.parentCode and s.classNo = st.code and st.type=40 \n");
		sql.append(" set s.className=st.name, s.classLongNumber=st.longnumber                                     \n");
		sql.append(" where s.className is null and s.schoolNo=:schoolNo \n");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		query.executeUpdate();

		return result;
	}

	public Set<String> importRecord(List<Map<String, Object>> datas, boolean isUpdateExist) {
		//应当使用学生的姓名和出生日期作为学生唯一的判断条件，是否要更新
		if (datas == null || datas.size() == 0)
			return null;
		String idColumn = "ID";
		String[] mustContainColumn = { "NAME", "BORNDATE", "SEX", "CLASSNO", "CLASSNAME", "CLASSLONGNUMBER", "GRADENO", "SCHOOLNO", "XJH" };
		String[] logicalColumnKeys = { "NAME", "SEX", "CLASSLONGNUMBER" };
		Session currentSession = getSessionFactory().getCurrentSession();
		Pair<Set<String>, Set<String>> insertUpdatePair = SQLImportDataUtil.insertOrUpdateRecord(currentSession, datas, isUpdateExist, "Student",
				idColumn, mustContainColumn, logicalColumnKeys);

		//为新增的学生添加代码
		Set<String> insertSet = insertUpdatePair.first();
		if (insertSet.size() > 0) {
			StringBuilder sql = new StringBuilder();
			//更新入学日期
			sql.append(" UPDATE student SET enterDate=CASE SUBSTRING(gradeNo,1,1) \n");
			sql.append(" WHEN 1 THEN CONCAT(YEAR(bornDate)+6+SUBSTRING(gradeNo,2,1),'-09-01')            \n");
			sql.append(" WHEN 2 THEN CONCAT(YEAR(bornDate)+12+SUBSTRING(gradeNo,2,1),'-09-01')           \n");
			sql.append(" WHEN 3 THEN CONCAT(YEAR(bornDate)+15+SUBSTRING(gradeNo,2,1),'-09-01')           \n");
			sql.append(" ELSE CONCAT(YEAR(bornDate)+18+SUBSTRING(gradeNo,2,1),'-09-01') END              \n");
			sql.append(" WHERE enterDate IS NULL AND bornDate IS NOT NULL         \n");
			sql.append(" and id in (:ids) \n");
			SQLQuery updateQuery;
			updateQuery = currentSession.createSQLQuery(sql.toString());
			updateQuery.setParameterList("ids", insertSet);
			updateQuery.executeUpdate();
			//如果入学日期大于实际年度，改为当前年度入学
			sql.setLength(0);
			sql.append(" UPDATE student SET enterDate=CONCAT(YEAR(now()),'-09-01') \n");
			sql.append(" WHERE year(enterDate)>year(now())         \n");
			sql.append(" and id in (:ids) \n");
			updateQuery = currentSession.createSQLQuery(sql.toString());
			updateQuery.setParameterList("ids", insertSet);
			updateQuery.executeUpdate();

			//更新学生编号
			updateQuery = currentSession.createSQLQuery("update Student set studentCode=? where id=?");
			final String CLASSNO = String.valueOf(datas.get(0).get("CLASSNO"));
			final String SCHOOLNO = String.valueOf(datas.get(0).get("SCHOOLNO"));
			boolean isSelfAddSeq = false;//自行添加顺序号
			if (isSelfAddSeq) {//先取一次，自加顺序号，最后更新顺序号到数据库
				List<String> newCodes = StudentCode.getNewCodes(SCHOOLNO, CLASSNO, null, insertSet.size());
				int index = 0;
				for (String id : insertSet) {
					updateQuery.setString(0, newCodes.get(index));
					updateQuery.setString(1, id);
					updateQuery.executeUpdate();
					index++;
				}
			} else {//每次都重新取学生代号
				for (String id : insertSet) {
					updateQuery.setString(0, StudentCode.getNewCode(SCHOOLNO, CLASSNO, null));
					updateQuery.setString(1, id);
					updateQuery.executeUpdate();
				}
			}
		}
		//		session.doWork(new Work() {
		//			@Override
		//			public void execute(Connection connection) throws SQLException {
		//				
		//			}
		//		});

		insertUpdatePair.first().addAll(insertUpdatePair.second());
		return insertUpdatePair.first();
	}

	@Override
	public int graduate(String sqlFilter, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder(500);//先插入记录到调班表
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query;
		int result;
		//先检查是否符合毕业要求
		sql.append(" select student.name, student.studentCode, school.schoolSystem       \n");
		sql.append(" from student                                                        \n");
		sql.append(" inner join HW_School school on school.schoolCode = student.schoolNo \n");
		sql.append(" where student.id in (select id from student ").append(sqlFilter).append(") \n");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		List list = query.list();
		if (list != null && list.size() > 0) {
			Object[] objs = (Object[]) list.get(0);
			if (objs[2] == null || objs[2].toString().length() == 0) {
				throw new RuntimeException("当前学校的办学制度为空，请在【学校维护】中重新保存学校的办学制度。");
			}

			sql.append(" and substring(student.classNo,1,2) not in (concat('1',substring(school.schoolSystem,1,1)),concat('2',substring(school.schoolSystem,2,1)),'33') \n");
			query = currentSession.createSQLQuery(sql.toString());
			HqlUtil.setParas(query, params);
			list = query.list();
			if (list != null && list.size() > 0) {
				StringBuffer message = new StringBuffer();
				message.append("以下学生不在毕业班：\n");
				Iterator it = list.iterator();
				while (it.hasNext()) {
					objs = (Object[]) it.next();
					message.append(objs[0]).append("\t\t").append(objs[1]).append("\n");
				}
				throw new BizRunTimeException("部分学生不在毕业班，不能做毕业操作，当前操作中断！", message.toString());
			}
		}

		//插入变更记录
		result = insertStudentChangeClass(sqlFilter, params, null, "毕业班", null);

		//再更新学生表中的学生信息
		sql.setLength(0);
		sql.append(" update Student set classNo=concat('").append(ClassNo_Graduate_Pre_String)
				.append("',classNo),className='毕业班',classLongNumber=null,status='F'");
		sql.append(sqlFilter);
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		query.executeUpdate();
		return result;
	}

	public int insertStudentChangeClass(String sqlFilter, Map<String, Object> params, String destClassNo, String destClassName,
			String destClassLongNumber) {
		int year = GlobalVariables.getGlobalVariables().getYear();
		int term = GlobalVariables.getGlobalVariables().getTerm();
		StringBuilder sql = new StringBuilder(500);//先插入记录到调班表
		sql.append(" insert into StudentChangeClass(id,studentId,year,term,schoolNo,gradeNo,classNo,className,classLongNumber,destClassNo,destClassName,destClassLongNumber,createTime) \n");
		sql.append(" select uuid(),id,").append(year).append(",").append(term);
		sql.append(",schoolNo,gradeNo,classNo,className,classLongNumber,:destClassNo,:destClassName,:destClassLongNumber,now() \n");
		sql.append(" from Student \n");
		sql.append(sqlFilter);
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query = currentSession.createSQLQuery(sql.toString());
		Map<String, Object> innerParams = new LinkedHashMap<String, Object>();
		if (params != null) {
			innerParams.putAll(params);
		}
		innerParams.put("destClassNo", destClassNo);
		innerParams.put("destClassName", destClassName);
		innerParams.put("destClassLongNumber", destClassLongNumber);
		HqlUtil.setParas(query, innerParams);
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public void updateClassName(Map<String, String> classes) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE Student  ");
		sql.append(" SET className = :className ");
		sql.append(" WHERE classLongNumber = :classLongNumber ");
		Iterator<Entry<String, String>> iter = classes.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			Query q = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
			q.setParameter("className", entry.getValue());
			q.setParameter("classLongNumber", entry.getKey());
			q.executeUpdate();
		}
	}

	@Override
	public int getStudentCountByClass(String classLongNo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(*) FROM Student WHERE classLongNumber = :classLongNo ");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setParameter("classLongNo", classLongNo);
		BigInteger count = (BigInteger) query.list().get(0);
		return count.intValue();
	}

}