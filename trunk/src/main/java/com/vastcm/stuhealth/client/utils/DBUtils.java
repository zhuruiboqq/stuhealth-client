/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import com.vastcm.stuhealth.client.framework.exception.SQLRTException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author House
 */
public class DBUtils {
    public static void release(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } 
            catch (SQLException ex) {
                throw new SQLRTException("Close Connection Error.");
            }
        }
    }
}
