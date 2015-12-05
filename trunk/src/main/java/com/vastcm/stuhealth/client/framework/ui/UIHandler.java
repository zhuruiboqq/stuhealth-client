package com.vastcm.stuhealth.client.framework.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.slf4j.Logger;

import com.vastcm.stuhealth.client.framework.exception.AbortException;

public class UIHandler {

	public static void handleException(Component component, Throwable throwable) {
		handleException(component, null, throwable);
	}

	public static void handleException(Component component, Logger logger, Throwable throwable) {
		if (throwable != null) {
			String message = throwable.getMessage();
			if (message == null)
				message = "没有错误信息，详细请查看日志！";
			if (!(throwable instanceof AbortException))
				JOptionPane.showMessageDialog(component, message);
		}
		if (logger != null)
			logger.error(null, throwable);
	}
}