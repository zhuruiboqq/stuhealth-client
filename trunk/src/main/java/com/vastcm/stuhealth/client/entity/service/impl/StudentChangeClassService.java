package com.vastcm.stuhealth.client.entity.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.vastcm.stuhealth.client.entity.StudentChangeClass;
import com.vastcm.stuhealth.client.entity.service.IStudentChangeClass;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import com.vastcm.stuhealth.client.framework.exception.BizRunTimeException;
import com.vastcm.stuhealth.client.utils.HqlUtil;

public class StudentChangeClassService extends CoreService<StudentChangeClass> implements IStudentChangeClass {

	@Override
	public int cancelOperation(String sqlFilter, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder(500);
		Session currentSession = getSessionFactory().getCurrentSession();
		SQLQuery query;
		int result;
		// 先判断学生是否有最新的操作记录
		sql.setLength(0);
		sql.append(" select student.name, stuCC.createTime                                                        \n");
		sql.append(" from StudentChangeClass stuCC                                                                \n");
		sql.append(" inner join student on stuCC.studentId = student.id                                           \n");
		sql.append(" INNER join ( select studentId,createTime from StudentChangeClass ").append(sqlFilter);
		sql.append(" ) stuCCTemp  \n");
		sql.append("     on stuCC.studentId=stuCCTemp.studentId and stuCC.createTime>stuCCTemp.createTime         \n");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		List list = query.list();
		if (list != null && list.size() > 0) {
			StringBuffer message = new StringBuffer();
			message.append("以下学生存在新的操作记录：\n");
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] objs = (Object[]) it.next();
				message.append(objs[0]).append("\t\t").append(objs[1]).append("\n");
			}
			throw new BizRunTimeException("部分学生存在新的操作记录，当前操作中断！", message.toString());
		}

		//更新学生信息
		sql.setLength(0);
		sql.append(" update Student inner join (select studentId,gradeNo,classNo,className,classLongNumber from StudentChangeClass \n");
		sql.append(sqlFilter);
		sql.append(") stuCC on student.id = stuCC.studentId \n");
		sql.append("set student.gradeNo=stuCC.gradeNo, student.classNo=stuCC.classNo, student.className=stuCC.className \n");
		sql.append("      , student.classLongNumber=stuCC.classLongNumber, student.status='T'");
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		result = query.executeUpdate();

		//删除记录
		sql.setLength(0);
		sql.append(" delete from StudentChangeClass \n");
		sql.append(sqlFilter);
		query = currentSession.createSQLQuery(sql.toString());
		HqlUtil.setParas(query, params);
		query.executeUpdate();
		return result;
	}
}