/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 *
 * @author House
 */
public interface ISchoolService extends ICoreService<School> {
    public School getByCode(String schoolCode);
    public School getByClassLongNumber(String classLongNumber);
    public void updateStatus(String schoolCode, String status, String hostSchoolCode);
    public void removeByCode(String code);
}
