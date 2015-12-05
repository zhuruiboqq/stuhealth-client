/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.core;

import java.util.List;


/**
 *
 * @author House
 */
public interface ICoreService<T> {
    public T getById(String id);
    public List<T> getAll();
    public void save(T entity);
    public void save(List<T> entities);
    public void removeAll();
    public void remove(String id);
    public void remove(String[] id);
    
    public List<T> getList(String whereSql);
    public int removeBySql(String whereSql);
	public boolean isExists(String whereSql);
}
