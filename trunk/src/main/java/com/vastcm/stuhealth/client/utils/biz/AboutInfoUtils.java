/**
 * 
 */
package com.vastcm.stuhealth.client.utils.biz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Nov 24, 2013
 */
public class AboutInfoUtils {
	
	private Logger logger = LoggerFactory.getLogger(AboutInfoUtils.class);
	private Map<String, String> aboutInfoMap = new HashMap<String, String>();
	private final String KEY_ORG_NAME = "orgName";
	
	public AboutInfoUtils() {
		updateAboutInfo();
	}
	
	/**
	 * 重新读取about.properties文件，刷新信息
	 * @author house
	 * @email  yyh2001@gmail.com
	 * @since  Nov 24, 2013
	 */
	public void updateAboutInfo() {
		Properties p = new Properties();
		String aboutFilePath = System.getProperty("appHome") + "/config/about.properties";
		try {
			p.load(new FileInputStream(aboutFilePath));
		} 
		catch (FileNotFoundException e) {
			logger.error("can't find about.properties.", e);
		} catch (IOException e) {
			logger.error("read about.properties failed.", e);
		}
		String orgName = p.getProperty(KEY_ORG_NAME);
		orgName = orgName == null ? "" : orgName;
		aboutInfoMap.clear();
		aboutInfoMap.put(KEY_ORG_NAME, orgName);
	}
	
	/**
	 * 获取机构名称
	 * @author house
	 * @email  yyh2001@gmail.com
	 * @since  Nov 24, 2013
	 * @return
	 */
	public String getOrgName() {
		return aboutInfoMap.get(KEY_ORG_NAME);
	}
	
}
