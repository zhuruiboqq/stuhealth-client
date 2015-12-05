/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author House
 */
public class UI<T> {
    
    private T uiObject;
    private Window window;
    private UIContext ctx;
    
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    
    private Logger logger = LoggerFactory.getLogger(UI.class);
    
    public UI() {
        
    } 
    
    public UI(final KernelUI uiObject, Window window, UIContext ctx) {
        this.uiObject = (T) uiObject;
        this.window = window;
        this.ctx = ctx;
        if(window == null) {
            final JTabbedPane mainPanel = UIFrameworkUtils.getMainUI().getMainPanel();
            mainPanel.addTab((String)ctx.get(UIContext.UI_TITLE), uiObject);
            JLabel lblTitle = new JLabel((String)ctx.get(UIContext.UI_TITLE) + " ");
            final JLabel lblClose = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/image/close.png")));
            final Font font = lblClose.getFont();
            final JPanel pnlTitle = new JPanel();
            pnlTitle.setLayout(new BorderLayout());
            pnlTitle.add(lblTitle, BorderLayout.CENTER);
            pnlTitle.add(lblClose, BorderLayout.EAST);
            pnlTitle.setOpaque(false);
//            JLabel.setSize(80, 12);
            mainPanel.setTabComponentAt(mainPanel.indexOfComponent(uiObject), pnlTitle);
            lblClose.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    lblClose.setFont(new Font(Font.SANS_SERIF, Font.BOLD, font.getSize()));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lblClose.setFont(font);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                	try {
                		 uiObject.uiClosing();
                         mainPanel.remove(mainPanel.indexOfTabComponent(pnlTitle));
                         UIFactory.disposeUI(uiObject.getClass());
                	}
                    catch(Exception ex) {
                    	logger.error("Error occurs when closing UI.", ex);
                    }
                }
            });
        }
        else {
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                	try {
                		uiObject.uiClosing();
                	}
                	catch(Exception ex) {
                    	logger.error("Error occurs when closing UI.", ex);
                    }
                }
            });
            ScreenUtils.center(window);
        }
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                propertyChange(evt);
            }
        });
    }
    
    public T getUIObject() {
        return uiObject;
    }
    
    public void setModal(boolean isModal) {
        if(window == null) {
            return;
        }
        if(window instanceof JDialog) {
            ((JDialog) window).setModal(isModal);
        }
    }
    
    public Window getWindow() {
        return window;
    }
    
    public void setVisible(boolean isVisible) {
    	 if(isVisible) {
         	((KernelUI)uiObject).onShow();
         }
        if(window == null) {
            KernelUI kernelUI = (KernelUI)uiObject;
            kernelUI.setVisible(isVisible);
            UIFrameworkUtils.getMainUI().getMainPanel().setSelectedComponent(kernelUI);
        }
        else {
        	ScreenUtils.center(window);
        	window.setVisible(isVisible);
        }
       
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        if("isModal".equalsIgnoreCase(evt.getPropertyName())) {
            if(window instanceof JDialog) {
                ((JDialog) window).setModal(true);
            }
        }
    } 

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
