/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.CodeGenerator;
import com.vastcm.stuhealth.client.entity.service.ICodeGeneratorService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author house
 */
public class CodeGeneratorService extends CoreService<CodeGenerator> implements ICodeGeneratorService{
    
    @Override
    public String getCode(String schoolCode, String year) {
//        Session session = getSessionFactory().getCurrentSession();
//        
//        StringBuilder getSql = new StringBuilder();
//        getSql.append(" SELECT code FROM CodeGenerator ");
//        getSql.append(" WHERE schoolCode = :schoolCode AND year = :year ");
//        Query queryGet = session.createSQLQuery(getSql.toString());
//        queryGet.setParameter("schoolCode", schoolCode);
//        queryGet.setParameter("year", year);
//        
//        List ls = queryGet.list();
//        String code = null;
//        if(ls.size() > 0) {
//            code = ls.get(0).toString();
//            int codeInt = Integer.valueOf(code);
//            codeInt += 1;
//            code = String.valueOf(codeInt);
//            for(int i = code.length(); i < 4; i++) {
//                code = "0" + code;
//            }
//            StringBuilder updateSql = new StringBuilder();
//            updateSql.append("UPDATE CodeGenerator SET code = :code WHERE schoolCode = :schoolCode AND year = :year ");
//            Query queryUpdate = session.createSQLQuery(updateSql.toString());
//            queryUpdate.setParameter("code", code);
//            queryUpdate.setParameter("schoolCode", schoolCode);
//            queryUpdate.setParameter("year", year);
//            queryUpdate.executeUpdate();
//        }
//        else {
//            code = "0001";
//            String id = schoolCode + year;
//            StringBuilder insertSql = new StringBuilder();
//            insertSql.append(" INSERT INTO CodeGenerator (id, code, schoolCode, year) VALUES (:id, :code, :schoolCode, :year) ");
//            Query queryInsert = session.createSQLQuery(insertSql.toString());
//            queryInsert.setParameter("id", id);
//            queryInsert.setParameter("code", code);
//            queryInsert.setParameter("schoolCode", schoolCode);
//            queryInsert.setParameter("year", year);
//            queryInsert.executeUpdate();
//        }
//       
//        return code;
    	
    	return getCodes(schoolCode, year, 1).get(0);
    }

	@Override
	/*
	 * 	一次性获取多个编码
	 *	interval	一次性获取的编码数量 
	 */
	public List<String> getCodes(String schoolCode, String year, int interval) {
		Session session = getSessionFactory().getCurrentSession();
        List<String> codes = new ArrayList<String>();;
        StringBuilder getSql = new StringBuilder();
        getSql.append(" SELECT code FROM CodeGenerator ");
        getSql.append(" WHERE schoolCode = :schoolCode AND year = :year ");
        Query queryGet = session.createSQLQuery(getSql.toString());
        queryGet.setParameter("schoolCode", schoolCode);
        queryGet.setParameter("year", year);
        
        List ls = queryGet.list();
        String code = null;
        if(ls.size() > 0) {
            code = ls.get(0).toString();
            int codeInt = Integer.valueOf(code);
            for(int i = 1; i <= interval; i++) {
            	codeInt += i;
                code = String.valueOf(codeInt);
                for(int j = code.length(); j < 4; j++) {
                    code = "0" + code;
                }
                codes.add(code);
            }
            
            StringBuilder updateSql = new StringBuilder();
            updateSql.append("UPDATE CodeGenerator SET code = :code WHERE schoolCode = :schoolCode AND year = :year ");
            Query queryUpdate = session.createSQLQuery(updateSql.toString());
            queryUpdate.setParameter("code", code);
            queryUpdate.setParameter("schoolCode", schoolCode);
            queryUpdate.setParameter("year", year);
            queryUpdate.executeUpdate();
        }
        else {
            code = "0000";
            int codeInt = Integer.valueOf(code);
            for(int i = 1; i <= interval; i++) {
            	codeInt += i;
                code = String.valueOf(codeInt);
                for(int j = code.length(); j < 4; j++) {
                    code = "0" + code;
                }
                codes.add(code);
            }
            String id = schoolCode + year;
            StringBuilder insertSql = new StringBuilder();
            insertSql.append(" INSERT INTO CodeGenerator (id, code, schoolCode, year) VALUES (:id, :code, :schoolCode, :year) ");
            Query queryInsert = session.createSQLQuery(insertSql.toString());
            queryInsert.setParameter("id", id);
            queryInsert.setParameter("code", code);
            queryInsert.setParameter("schoolCode", schoolCode);
            queryInsert.setParameter("year", year);
            queryInsert.executeUpdate();
        }
        return codes;
	}
}
