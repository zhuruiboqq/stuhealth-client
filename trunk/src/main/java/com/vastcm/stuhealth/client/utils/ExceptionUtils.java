/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import com.vastcm.stuhealth.client.framework.exception.AbortException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;

/**
 *
 * @author house
 */
public class ExceptionUtils {
    public static String formatExceptionStack(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    public static void writeExceptionLog(Logger logger, Throwable ex) {
        logger.error(formatExceptionStack(ex));
    }
    
    public static void abort() {
        throw new AbortException();
    }
}
