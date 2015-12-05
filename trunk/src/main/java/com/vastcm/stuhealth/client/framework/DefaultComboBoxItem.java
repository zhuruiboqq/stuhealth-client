package com.vastcm.stuhealth.client.framework;

public class DefaultComboBoxItem {

	private String displayText;
	private Object value;

	public DefaultComboBoxItem(Object value) {
		this(null, value);
	}

	public DefaultComboBoxItem(String displayText, Object value) {
		if (value == null)
			throw new IllegalArgumentException("value argument can not be null!");
		this.displayText = displayText;
		this.value = value;
		if (this.displayText == null)
			this.displayText = value.toString();
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return displayText;
	}
}