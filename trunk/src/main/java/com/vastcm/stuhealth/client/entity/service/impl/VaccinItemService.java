/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.VaccinItem;
import com.vastcm.stuhealth.client.entity.service.IVaccinItemService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

/**
 *
 * @author house
 */
public class VaccinItemService extends CoreService<VaccinItem> implements IVaccinItemService{

    @Override
    public List<VaccinItem> getAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM VaccinItem ORDER BY vaccinId, vaccItemId ").list();
    }
    
}
