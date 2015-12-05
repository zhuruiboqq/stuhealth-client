/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.entity.BaseSetting;
import com.vastcm.stuhealth.client.entity.service.IBaseSettingService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author house
 */
public class GlobalVariables implements PropertyChangeListener {
    private static GlobalVariables global;
    private static boolean isDirty = false;
    private int year = 1900;
    private int term = 0;
    private static String currentUser = null;
    
    private GlobalVariables() {}
    
    public static GlobalVariables getGlobalVariables() {
        if(global == null || isDirty) {
            global = new GlobalVariables();
            IBaseSettingService settingSrv = AppContext.getBean("baseSettingService", IBaseSettingService.class);
            BaseSetting setting = settingSrv.getAll().get(0);
            global.setYear(setting.getYear());
            global.setTerm(setting.getTerm());
        }
        return global;
    }
    
    public static void setIsDirty(boolean isDirty) {
    	GlobalVariables.isDirty = isDirty;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
    
    /**
	 * @return the currentUser
	 */
	public static String getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public static void setCurrentUser(String currentUser) {
		GlobalVariables.currentUser = currentUser;
	}

	@Override
    public void propertyChange(PropertyChangeEvent pce) {
        if(BaseSettingPanel.PROPERTY_PERIOD.equals(pce.getPropertyName())) {
            BaseSetting setting = (BaseSetting) pce.getNewValue();
            setYear(setting.getYear());
            setTerm(setting.getTerm());
        }
    }
    
    
}
