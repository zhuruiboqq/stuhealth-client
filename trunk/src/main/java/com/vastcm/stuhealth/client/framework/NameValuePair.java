/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework;

/**
 *
 * @author House
 */
public class NameValuePair {
    private String name;
    private Object value;
    private String compareType;
    
    public static final String COMPARETYPE_EQUAL = "=";
    public static final String COMPARETYPE_LIKE =  "LIKE";

    public NameValuePair() {
    }
    
    public NameValuePair(String name, Object value, String compareType) {
        this.name = name;
        this.value = value;
        this.compareType = compareType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }
}
