/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author House
 */
public class ScreenUtils {
    public static void center(Window window) {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((int)(scrSize.getWidth()-window.getWidth())/2, 
                (int)(scrSize.getHeight()-window.getHeight())/2 );
    }
}
