/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import java.util.List;

/**
 *
 * @author house
 */
public interface ICheckItemService extends ICoreService<CheckItem>{
    public List<CheckItem> getNormalItems();
    public List<CheckItem> getSelectedItems();
}
