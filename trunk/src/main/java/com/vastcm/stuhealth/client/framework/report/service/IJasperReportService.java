package com.vastcm.stuhealth.client.framework.report.service;

import java.sql.SQLException;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public interface IJasperReportService {

	public JasperPrint getJasperPrint(String jasperFilePath, Map<String, Object> params) throws JRException, SQLException;

	public JasperPrint getJasperPrint(JasperReport jasperReport, Map<String, Object> params) throws JRException, SQLException;
}