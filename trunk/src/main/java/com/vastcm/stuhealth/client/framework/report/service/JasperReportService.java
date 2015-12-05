package com.vastcm.stuhealth.client.framework.report.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

import com.vastcm.stuhealth.client.utils.JasperReportUtil;

public class JasperReportService implements IJasperReportService {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession(boolean isNewSession) {
		try {
			return getSessionFactory().getCurrentSession();
		} catch (Exception e) {
		}
		return getSessionFactory().openSession();
	}

	private static final String customStaticText = "customStaticText";
	private static final String customDynamicText = "customDynamicText";
	private static final String customSplitor = "customSplitor";

	public static JasperReport getJasperReport(String xmlFilePath, String[][] sizeGroup) throws JRException {
		JasperDesign design = getJasperDesign(xmlFilePath);
		JRDesignBand columnHeader = (JRDesignBand) design.getColumnHeader();

		reSetColumnHeaderHeight(columnHeader, sizeGroup);
		reSetshapeAndPosition(columnHeader, sizeGroup);
		addElementToColumnHeader(columnHeader, sizeGroup);
		return JasperCompileManager.compileReport(design);
	}

	private static JasperDesign getJasperDesign(String filePath) throws JRException {
		return JRXmlLoader.load(new File(filePath));
	}

	private static void reSetColumnHeaderHeight(JRDesignBand columnHeader, String[][] sizeGroup) {
		columnHeader.setHeight(columnHeader.getHeight() * sizeGroup.length);
	}

	private static JRDesignStaticText getFlagTextInDesign(JRDesignBand columnHeader) {
		return (JRDesignStaticText) columnHeader.getElementByKey(customStaticText);
	}

	private static void reSetshapeAndPosition(JRDesignBand columnHeader, String[][] sizeGroup) {
		JRDesignStaticText flagText = getFlagTextInDesign(columnHeader);
		Iterator<JRChild> children = columnHeader.getChildren().iterator();
		while (children.hasNext()) {
			JRDesignStaticText element = (JRDesignStaticText) children.next();
			if (element.getX() > flagText.getX()) {
				element.setX(flagText.getX() + flagText.getWidth() * sizeGroup[0].length);
			}
			if (!customStaticText.equals(element.getKey())) {
				element.setHeight(element.getHeight() * sizeGroup.length);
			}
		}
	}

	private static void addElementToColumnHeader(JRDesignBand columnHeader, String[][] sizeGroup) {
		JRDesignStaticText flagText = getFlagTextInDesign(columnHeader);
		columnHeader.removeElement(flagText);
		for (int i = 0; i < sizeGroup.length; i++) {
			for (int j = 0; j < sizeGroup[i].length; j++) {
				try {
					JRDesignStaticText newElement = (JRDesignStaticText) BeanUtils.cloneBean(flagText);
					newElement.setText(sizeGroup[i][j]);
					newElement.setX(flagText.getX() + flagText.getWidth() * j);
					newElement.setY(flagText.getY() + flagText.getHeight() * i);
					columnHeader.addElement(newElement);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public JasperPrint getJasperPrint(String jasperFilePath, Map<String, Object> params) throws JRException, SQLException {
		JasperReport jasperReport = JasperReportUtil.getJasperReport(jasperFilePath);
		return getJasperPrint(jasperReport, params);
	}

	public JasperPrint getJasperPrint(JasperReport jasperReport, Map<String, Object> params) throws JRException, SQLException {
		//获取路径和数据库连接
		//		final String jasperFilePathTemp = jasperFilePath;
		//		final Map<String, Object> paramsTemp = params;
		//		final JasperPrint print = null;
		//		getSession(true).doWork(new Work() {
		//
		//			@Override
		//			public void execute(Connection paramConnection) throws SQLException {
		//				try {
		//					print = JasperFillManager.fillReport(JasperReportPath + jasperFilePathTemp, paramsTemp, paramConnection);
		//				} catch (JRException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//		});
		if (params != null && !params.containsKey("p_SUBREPORT_DIR")) {
			params.put("p_SUBREPORT_DIR", JasperReportUtil.JasperReportDir + "/");
		}
		ConnectionProvider connectionProvider = ((SessionFactoryImplementor) sessionFactory).getConnectionProvider();
		JasperPrint print = JasperFillManager.fillReport(jasperReport, params, connectionProvider.getConnection());
		return print;
	}

}