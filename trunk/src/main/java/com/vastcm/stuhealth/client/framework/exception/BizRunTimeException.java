package com.vastcm.stuhealth.client.framework.exception;

public class BizRunTimeException extends RuntimeException {
	private String bizDetailMessage;

	public BizRunTimeException() {
	}

	public BizRunTimeException(String message) {
		super(message);
	}

	public BizRunTimeException(String message, String bizDetailMessage) {
		super(message);
		this.bizDetailMessage = bizDetailMessage;
	}

	public BizRunTimeException(Throwable cause) {
		super(cause);
	}

	public BizRunTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizRunTimeException(String message, String bizDetailMessage, Throwable cause) {
		super(message, cause);
		this.bizDetailMessage = bizDetailMessage;
	}

	public String getBizDetailMessage() {
		return bizDetailMessage;
	}
}