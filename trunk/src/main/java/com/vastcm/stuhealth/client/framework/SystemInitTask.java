/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.jidesoft.utils.Q;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.DatabaseBackupRecover;

/**
 * 系统初始化时执行的任务
 * 
 * @author bob
 */
public class SystemInitTask {

	public static void onInit() {
		Q.a = true;
		AppCache.initAppCache();
		ComboPooledDataSource dataSource = (ComboPooledDataSource) AppContext.getBean("dataSource", ComboPooledDataSource.class);
		DatabaseBackupRecover.Database_User = dataSource.getUser();
		DatabaseBackupRecover.Database_Password = dataSource.getPassword();

		initGlobalFontSetting(new Font("Dialog", Font.PLAIN, 12));
	}

	public static void initGlobalFontSetting(Font fnt) {
		FontUIResource fontRes = new FontUIResource(fnt);
		for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
}