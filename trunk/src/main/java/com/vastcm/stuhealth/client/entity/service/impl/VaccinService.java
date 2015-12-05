/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service.impl;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.entity.service.core.impl.CoreService;
import java.util.List;

/**
 *
 * @author house
 */
public class VaccinService extends CoreService<Vaccin> implements IVaccinService {
    public List<Vaccin> getNormalItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM Vaccin WHERE state='1' ORDER BY sort").list();
    }

    public List<Vaccin> getSelectedItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery(" FROM Vaccin WHERE state='1' AND isSelected='1' ORDER BY sort").list();
    }
}
