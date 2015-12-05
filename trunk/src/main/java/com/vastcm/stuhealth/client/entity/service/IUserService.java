/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import com.vastcm.stuhealth.client.entity.User;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 *
 * @author House
 */
public interface IUserService extends ICoreService<User> {
    public User getByName(String name);
}
