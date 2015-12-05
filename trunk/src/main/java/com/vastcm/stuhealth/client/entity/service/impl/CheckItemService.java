/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

/**
 *
 * @author house
 */
public class CheckItemService extends CoreService<CheckItem> implements ICheckItemService{

    public List<CheckItem> getNormalItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM CheckItem WHERE status='T' ORDER BY itemId").list();
    }

    public List<CheckItem> getSelectedItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM CheckItem WHERE status='T' AND isSelected='1' ORDER BY sort").list();
    }
    
    
}
