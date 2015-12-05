/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework;

import com.vastcm.stuhealth.client.framework.exception.AbortException;

/**
 *
 * @author House
 */
public class SystemUtils {
    public static void abort() {
        throw new AbortException();
    }
}
