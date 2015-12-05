/**
 * 
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.utils.ExceptionUtils;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 11, 2013
 */
public class UIManagerHelper {
	private static Logger logger = LoggerFactory.getLogger(UIManagerHelper.class);
	private static Map<String, String> customLafs = new HashMap<String,String>();
	private static Map<String, String> systemLafs = new HashMap<String, String>();
	
	static {
		customLafs.put("SubstanceMistSilver", 	"org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel");
		customLafs.put("SubstanceMistAqua", 	"org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel");
		customLafs.put("SubstanceNebula",  	 	"org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel");
		customLafs.put("SubstanceModerate",  	"org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel");
		customLafs.put("SubstanceBusiness",  	"org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");
		
		UIManager.LookAndFeelInfo[] sysLafs = getInstalledLookAndFeels();
		for(UIManager.LookAndFeelInfo laf : sysLafs) {
			systemLafs.put(laf.getName(), laf.getClassName());
		}
	}
	
	public static UIManager.LookAndFeelInfo[] getInstalledLookAndFeels() {
		UIManager.LookAndFeelInfo[] sys = UIManager.getInstalledLookAndFeels();
		UIManager.LookAndFeelInfo[] my = new UIManager.LookAndFeelInfo[sys.length + customLafs.size()];
		for(int i = 0; i < sys.length; i++) {
			my[i] = sys[i];
		}
		Iterator<Entry<String, String>> iter = customLafs.entrySet().iterator();
		int j = sys.length;
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			my[j++] = new UIManager.LookAndFeelInfo(entry.getKey(), entry.getValue());
		}
		return my;
	}
	
	public static String getLafClassName(String lafName) {
		if(customLafs.containsKey(lafName)) {
			return customLafs.get(lafName);
		}
		if(systemLafs.containsKey(lafName)) {
			return systemLafs.get(lafName);
		}
		return UIManager.getSystemLookAndFeelClassName();
	}
	
	public static void setLookAndFeel(String lafClassName) {
		try {
			UIManager.setLookAndFeel(lafClassName);
		} 
		catch (ClassNotFoundException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		} 
		catch (InstantiationException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		} 
		catch (IllegalAccessException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		} 
		catch (UnsupportedLookAndFeelException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		}
	}
}
