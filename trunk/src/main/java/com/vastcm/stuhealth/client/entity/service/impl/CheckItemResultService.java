/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

/**
 *
 * @author house
 */
public class CheckItemResultService extends CoreService<CheckItemResult> implements ICheckItemResultService{

    @Override
    public List<CheckItemResult> getAll() {
         return getSessionFactory().getCurrentSession()
                .createQuery(" FROM CheckItemResult ORDER BY itemId, itemCode ").list();
    }
    
}
