/**
 * 
 */
package com.vastcm.stuhealth.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vastcm.stuhealth.client.framework.AppCache;

/**
 * @author 		yangyihao
 * @email		yyh2001@gmail.com
 * @date   		2013-1-13
 * @description	Encapsulate spring's ApplicationContext class for convenient.
 * @history		2013-1-13		Create.
 *
 */
public class AppContext { 

	private static Logger logger = LoggerFactory.getLogger(AppContext.class);
    private static ApplicationContext ctx;
	
    private AppContext() {
    }
    
    private static boolean isSchoolEdition()  {
    	boolean isSchoolEdition = false;
    	try {
    		File pfile = new File(System.getProperty("appHome") + File.separator + "config/custom.properties");
    		if (!pfile.exists()) {
    			pfile.createNewFile();
    		}
    		Properties prop = new Properties();
    		prop.load(new FileInputStream(pfile));
    		String editionName = prop.getProperty("editionName");
    		isSchoolEdition = (editionName != null && "School_Edition".equals(editionName));
    	} catch(IOException e) {
    		logger.error("read custom.properties error.", e);
    	}
    	
		return isSchoolEdition;
    }
	
    public static synchronized <T> T getBean(String objName, Class<T> classType) {
    	String beanName = "beans.xml";
    	if(isSchoolEdition()) {
    		beanName = "beans-school.xml";
    	}
    	if(ctx == null) {
            ctx = new ClassPathXmlApplicationContext(beanName);
        }
    	
		return ctx.getBean(objName, classType);	
    }

}
