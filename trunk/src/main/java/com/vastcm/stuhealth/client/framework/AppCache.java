package com.vastcm.stuhealth.client.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.framework.report.service.ISqlService;

public class AppCache {
	private static AppCache instance = new AppCache();

	private static Logger logger = LoggerFactory.getLogger(AppCache.class);
	private Map<String, String> maxVersionMap = new HashMap<String, String>(10);
	private boolean isSchoolEdition = false;
	private String studentAppraiseRpt_ItemOrder = null;
	private String lookAndFeel = "Nimbus";
	private File customConfigFile = new File(System.getProperty("appHome") + File.separator + "config/custom.properties");

	private AppCache() {

	}

	public static AppCache getInstance() {
		return instance;
	}

	public String putMaxVersion(String itemFieldName, String maxVersion) {
		if (org.springframework.util.StringUtils.hasText(itemFieldName)) {
			return maxVersionMap.put(itemFieldName.toUpperCase(), maxVersion);
		}
		return null;
	}

	public String getMaxVersion(String itemFieldName) {
		if (org.springframework.util.StringUtils.hasText(itemFieldName)) {
			return maxVersionMap.get(itemFieldName.toUpperCase());
		}
		return null;
	}

	public String getStudentAppraiseRpt_ItemOrder() {
		return studentAppraiseRpt_ItemOrder;
	}

	public void setStudentAppraiseRpt_ItemOrder(String studentAppraiseRpt_ItemOrder) throws IOException {
		this.studentAppraiseRpt_ItemOrder = studentAppraiseRpt_ItemOrder;

		Properties prop = new Properties();
		prop.load(new FileInputStream(customConfigFile));
		prop.setProperty("studentAppraiseRpt_ItemOrder", studentAppraiseRpt_ItemOrder);
		prop.store(new FileOutputStream(customConfigFile), "CREATE BY VASTCM");
	}

	public boolean isSchoolEdition() {
		return isSchoolEdition;
	}

	public String lookAndFeel() {
		return lookAndFeel;
	}

	public static void initAppCacheNoDb() {
		try {
			instance.loadCustomProperty();
		} catch (IOException e) {
			logger.error("loadCustomProperty error.", e);
		}
	}

	public static void initAppCache() {
		ISqlService sqlService = AppContext.getBean("sqlService", ISqlService.class);
		StringBuffer sql = new StringBuffer();
		sql.append("select 'SG' itemField, max(version) version from standard_height    \n");
		sql.append("union all                                                           \n");
		sql.append("select 'TZ' itemField, max(version) version from standard_weight    \n");
		sql.append("union all                                                           \n");
		sql.append("select 'XW' itemField, max(version) version from standard_Chest     \n");
		sql.append("union all                                                           \n");
		sql.append("select 'MB' itemField, max(version) version from standard_pulsation \n");
		sql.append("union all                                                           \n");
		sql.append("select 'FHL' itemField, max(version) version from standard_lungs    \n");
		sql.append("union all                                                           \n");
		sql.append("select 'YY' itemField, max(version) version from Standard_Nutrition \n");
		sql.append("union all                                                           \n");
		sql.append("select 'TZ*10000/(SG*SG)' itemField, max(version) version from Standard_bmi \n");
		List rs = sqlService.query(sql.toString());
		for (int i = 0, size = rs.size(); i < size; i++) {
			Object[] datas = (Object[]) rs.get(i);
			String itemField = String.valueOf(datas[0]);
			String version = String.valueOf(datas[1]);
			AppCache.getInstance().putMaxVersion(itemField, version);
		}
	}

	private void loadCustomProperty() throws IOException {
		if (!customConfigFile.exists()) {
			customConfigFile.createNewFile();
		}
		Properties prop = new Properties();
		prop.load(new FileInputStream(customConfigFile));
		studentAppraiseRpt_ItemOrder = prop.getProperty("studentAppraiseRpt_ItemOrder");
		String editionName = prop.getProperty("editionName");
		isSchoolEdition = (editionName != null && "School_Edition".equals(editionName));//Medical_Institutions ,School_Edition

		if (prop.getProperty("lookAndFeel") == null) {
			prop.setProperty("lookAndFeel", "Nimbus");
			lookAndFeel = "Nimbus";
		} else {
			lookAndFeel = prop.getProperty("lookAndFeel");
		}
	}
}