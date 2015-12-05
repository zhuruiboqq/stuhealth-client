/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.entity.service;

import java.util.List;

import com.vastcm.stuhealth.client.entity.CodeGenerator;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;

/**
 *
 * @author house
 */
public interface ICodeGeneratorService extends ICoreService<CodeGenerator>{
    public String getCode(String schoolCode, String year);
    public List<String> getCodes(String schoolCode, String year, int interval);
}
