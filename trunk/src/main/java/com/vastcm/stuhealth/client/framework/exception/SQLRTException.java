/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.exception;

/**
 *
 * @author House
 */
public class SQLRTException extends MyRunTimeException{
    
    public SQLRTException() {
    }

    public SQLRTException(String message) {
        super(message);
    }
    
    public SQLRTException(Throwable cause) {
        super(cause);
    }

    public SQLRTException(String message, Throwable cause) {
        super(message, cause);
    }  
}
