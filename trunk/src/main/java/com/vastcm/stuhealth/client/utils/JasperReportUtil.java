package com.vastcm.stuhealth.client.utils;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JasperReportUtil {
	protected static Logger logger = LoggerFactory.getLogger(JasperReportUtil.class);;
	public static final String JasperReportDir = "jasper_report";
	private static String JasperReportPath;
	static {
		if (JasperReportPath == null) {
			logger.info(System.getenv("localProjectDebug") + "============");
			//			logger.info(PathUtil.getFilePathByClassRoot(JasperReportDir));//fuck, what happen
			//			JasperReportPath = PathUtil.getFilePathByClassRoot(JasperReportDir) + File.separator;
		}
	}

	public static JasperDesign getJasperDesign(String jasperXmlFilePath) throws JRException {
		JasperDesign jasperDesign;
		if (System.getenv("localProjectDebug") != null) {
			jasperDesign = JRXmlLoader.load(new File(PathUtil.getFilePathByClassRoot("jasper_report") + File.separator + jasperXmlFilePath));
		} else {
			jasperXmlFilePath = "/jasper_report/" + jasperXmlFilePath;
			jasperDesign = JRXmlLoader.load(JasperReportUtil.class.getResourceAsStream(jasperXmlFilePath));
		}
		return jasperDesign;
	}

	public static JasperReport getJasperReport(String jasperFilePath) throws JRException {
		JasperReport jasperReport = null;
		if (System.getenv("localProjectDebug") != null) {
			jasperReport = (JasperReport) JRLoader.loadObject(PathUtil.getFilePathByClassRoot("jasper_report") + File.separator + jasperFilePath);
		} else {
			jasperFilePath = "/" + JasperReportDir + "/" + jasperFilePath;
			jasperReport = (JasperReport) JRLoader.loadObject(JasperReportUtil.class.getResourceAsStream(jasperFilePath));
		}
		return jasperReport;
	}
}
