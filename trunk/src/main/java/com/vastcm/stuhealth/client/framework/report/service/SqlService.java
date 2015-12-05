package com.vastcm.stuhealth.client.framework.report.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.vastcm.stuhealth.client.framework.report.DataBaseType;
import com.vastcm.stuhealth.client.utils.HqlUtil;

/**
 * 
 * @author bob
 * 
 */
@SuppressWarnings("rawtypes")
@Transactional
public class SqlService implements ISqlService {

	private SessionFactory sessionFactory;

	private boolean isUseCodeTransaction = (DataBaseType.Current == DataBaseType.Only_SqlServer);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession(boolean isNewSession) {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionFactory.openSession();
	}

	private boolean parseExistResult(List result) {
		if (Integer.parseInt(result.get(0).toString()) == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isExist(String sql) {
		return isExist(sql, new Object[] {});
	}

	@Override
	public boolean isExist(String sql, Object[] paras) {
		sql = HqlUtil.format2CountHql(sql);
		List result = query(sql, paras);
		return parseExistResult(result);
	}

	@Override
	public boolean isExist(String sql, String[] paraNames, Object[] paraValues) {
		sql = HqlUtil.format2CountHql(sql);
		List result = query(sql, paraNames, paraValues);
		return parseExistResult(result);
	}

	@Override
	public boolean isExist(String sql, Map paras) {
		sql = HqlUtil.format2CountHql(sql);
		List result = query(sql, paras);
		return parseExistResult(result);
	}

	@Override
	public List query(String sql) {
		return query(sql, new Object[] {});
	}

	@Override
	public List query(String sql, Object[] paras) {
		List result = null;
		Session session = getSession(true);
		//		Transaction tx = null;
		try {
			//			tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paras);
			result = query.list();
			//			tx.commit();
		} catch (RuntimeException e) {
			//			if (tx != null)
			//				tx.rollback();
			throw e;
		} finally {
			//			if (session.isConnected())
			//			session.close();
		}
		return result;
	}

	@Override
	public List query(String sql, String[] paraNames, Object[] paraValues) {
		List result = null;
		Session session = getSession(true);
		//		Transaction tx = null;
		try {
			//			tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paraNames, paraValues);
			result = query.list();
			//			tx.commit();
		} catch (RuntimeException e) {
			//			if (tx != null)
			//				tx.rollback();
			throw e;
		} finally {
			//			if (session.isConnected())
			//			session.close();
		}
		return result;
	}

	@Override
	public List query(String sql, Map paras) {
		List result = null;
		Session session = getSession(true);
		Transaction tx = null;
		try {
			if (isUseCodeTransaction)
				tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paras);
			result = query.list();
			if (isUseCodeTransaction)
				tx.commit();
		} catch (RuntimeException e) {
			if (isUseCodeTransaction)
				if (tx != null)
					tx.rollback();
			throw e;
		} finally {
			if (isUseCodeTransaction)
				if (session.isConnected())
					session.close();
		}
		return result;
	}

	@Override
	public int update(String sql) {
		return update(sql, new Object[] {});
	}

	@Override
	public int update(String sql, Object[] paras) {
		int result = 0;
		Session session = getSession(true);
		//		Transaction tx = null;
		try {
			//			tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paras);
			result = query.executeUpdate();
			//			tx.commit();
		} catch (RuntimeException e) {
			//			if (tx != null)
			//				tx.rollback();
			throw e;
		} finally {
			//			if (session.isConnected())
			//				session.close();
		}
		return result;
	}

	@Override
	public int update(String sql, String[] paraNames, Object[] paraValues) {
		int result = 0;
		Session session = getSession(true);
		//		Transaction tx = null;
		try {
			//			tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paraNames, paraValues);
			result = query.executeUpdate();
			//			tx.commit();
		} catch (RuntimeException e) {
			//			if (tx != null)
			//				tx.rollback();
			throw e;
		} finally {
			//			if (session.isConnected())
			//			session.close();
		}
		return result;
	}

	@Override
	public int update(String sql, Map paras) {
		int result = 0;
		Session session = getSession(true);
		//		Transaction tx = null;
		try {
			//			tx = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			HqlUtil.setParas(query, paras);
			result = query.executeUpdate();
			//			tx.commit();
		} catch (RuntimeException e) {
			//			if (tx != null)
			//				tx.rollback();
			throw e;
		} finally {
			//			if (session.isConnected())
			//			session.close();
		}
		return result;
	}
}
