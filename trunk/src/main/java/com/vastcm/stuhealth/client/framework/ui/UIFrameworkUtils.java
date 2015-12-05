/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import com.vastcm.stuhealth.client.MainUI;
import javax.swing.JTabbedPane;

/**
 *
 * @author House
 */
public class UIFrameworkUtils {
    private static MainUI mainUI;
    
    public static void setMainUI(MainUI mainUI) {
        UIFrameworkUtils.mainUI = mainUI;
    }
    
    public static MainUI getMainUI() {
        return UIFrameworkUtils.mainUI;
    }
    
    public static JTabbedPane getTabPane() {
        if(mainUI != null) {
            return mainUI.getMainPanel();
        }
        return null;
    }
}
