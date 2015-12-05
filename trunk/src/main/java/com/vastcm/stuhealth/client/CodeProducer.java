/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.util.List;

import com.vastcm.stuhealth.client.entity.service.ICodeGeneratorService;

/**
 *
 * @author house
 */
public class CodeProducer {
    private static CodeProducer producer = new CodeProducer();
    
    private CodeProducer() {}
    
    public static CodeProducer getCodeProducer() {
        return producer;
    }
    
    public String getCode(String schoolCode, String year) {
        ICodeGeneratorService srv = AppContext.getBean("codeGenerator", ICodeGeneratorService.class);
        return srv.getCode(schoolCode, year);
    }
    
    public List<String> getCodes(String schoolCode, String year, int interval) {
        ICodeGeneratorService srv = AppContext.getBean("codeGenerator", ICodeGeneratorService.class);
        return srv.getCodes(schoolCode, year, interval);
    }
}
