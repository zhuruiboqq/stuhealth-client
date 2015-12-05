/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.awt.Window;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author House
 */
public class KernelUI extends JPanel {
    private UIContext uiCtx;
    
    public KernelUI() {
        super();
    }
    
    public void onLoad() throws Exception {
        
    }
    
    public UIContext getUIContext() {
        return uiCtx;
    }
    
    public void setUIContext(UIContext uiCtx) {
        this.uiCtx = uiCtx;
    }
    
    protected void disposeUI() {
        Window win = SwingUtilities.getWindowAncestor(this);
        if(win != null) {
            win.dispose();
        }
        else {
            setVisible(false);
        }
    }
    
    public void onShow() {
    	
    }
    
    public void uiClosing() {
    }
    
    public Object getValue() {
        return null;
    }
}
