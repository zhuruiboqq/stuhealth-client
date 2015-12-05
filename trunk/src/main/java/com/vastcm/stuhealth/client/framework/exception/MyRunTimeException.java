/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.exception;

/**
 *
 * @author House
 */
public class MyRunTimeException extends RuntimeException{
    public MyRunTimeException() {
    }

    public MyRunTimeException(String message) {
        super(message);
    }
    
    public MyRunTimeException(Throwable cause) {
        super(cause);
    }

    public MyRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
