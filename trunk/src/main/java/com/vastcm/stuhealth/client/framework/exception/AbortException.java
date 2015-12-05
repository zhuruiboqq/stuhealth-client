/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.exception;

/**
 *
 * @author House
 */
public class AbortException extends MyRunTimeException{

    public AbortException() {
    }

    public AbortException(String message) {
        super(message);
    }
    
    public AbortException(Throwable cause) {
        super(cause);
    }

    public AbortException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
