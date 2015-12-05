/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.User;
import com.vastcm.stuhealth.client.entity.service.IUserService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

/**
 *
 * @author House
 */
public class UserService extends CoreService<User> implements IUserService{
     /*
     * return null if no data matched.
     */
    public User getByName(String name) {
        StringBuilder hql = new StringBuilder();
        hql.append(" FROM ").append(getEntityClass().getSimpleName());
        hql.append(" WHERE username = ? ");
        List<User> ls = getSessionFactory().getCurrentSession().createQuery(hql.toString()).setString(0, name).list();
        if(ls.size() > 0) {
            return ls.get(0);
        }
        return null;
    }
}
