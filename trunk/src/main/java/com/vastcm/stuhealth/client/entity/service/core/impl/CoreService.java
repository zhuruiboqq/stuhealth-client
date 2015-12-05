/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.core.impl;

import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author House
 */
@Transactional
public abstract class CoreService<T extends CoreEntity> implements ICoreService<T> {

	@Resource
	private SessionFactory sessionFactory;

	public CoreService() {
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Class<?> getEntityClass() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return (Class<?>) params[0];
	}

	public T getById(String id) {
		return (T) getSessionFactory().getCurrentSession().get(getEntityClass(), id);
	}

	public void beforeSave(T entity) {
		if (entity.getId() == null) {
			entity.setId(UUID.randomUUID().toString());
		}
	}

	public void save(T entity) {
		beforeSave(entity);
		getSessionFactory().getCurrentSession().saveOrUpdate(entity);
	}

	public void save(List<T> entities) {
		for (T entity : entities) {
			save(entity);
		}
	}

	public void removeAll() {
		getSessionFactory().getCurrentSession().createQuery(" DELETE FROM " + getEntityClass().getSimpleName()).executeUpdate();
	}

	public List<T> getAll() {
		return getSessionFactory().getCurrentSession().createQuery(" FROM " + getEntityClass().getSimpleName()).list();
	}

	@Override
	public void remove(String id) {
		getSessionFactory().getCurrentSession().createQuery(" DELETE FROM " + getEntityClass().getSimpleName() + " WHERE id = '" + id + "'")
				.executeUpdate();
	}

	public void remove(String[] ids) {
		StringBuilder sb = new StringBuilder();
		for (String id : ids) {
			sb.append("'").append(id).append("',");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		String sql = " DELETE FROM " + getEntityClass().getSimpleName() + " WHERE id IN (" + sb + ")";
		getSessionFactory().getCurrentSession().createQuery(sql).executeUpdate();
	}

	public List<T> getList(String whereSql) {
		return getSessionFactory().getCurrentSession().createQuery(" FROM " + getEntityClass().getSimpleName() + "\n" + whereSql).list();
	}

	@Override
	public int removeBySql(String whereSql) {
		return getSessionFactory().getCurrentSession().createQuery("delete FROM " + getEntityClass().getSimpleName() + "\n" + whereSql)
				.executeUpdate();
	}

	@Override
	public boolean isExists(String whereSql) {
		Query query = getSessionFactory().getCurrentSession().createQuery(
				"select count(*) FROM " + getEntityClass().getSimpleName() + "\n" + whereSql);
		Object result = query.uniqueResult();
		if (result == null) {
			return false;
		}
		return ((Number) result).intValue() != 0;
	}
}