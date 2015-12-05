/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework;

import java.io.Serializable;

/**
 * 
 * @author House
 */
public class SessionClientUtils {
    private static Serializable sessionId;
    
    private SessionClientUtils() {
    }
    
    public static Serializable getSessionId() {
        return sessionId;
    }
    
    public static void setSessionId(Serializable sessionId) {
        SessionClientUtils.sessionId = sessionId;
    }
}
