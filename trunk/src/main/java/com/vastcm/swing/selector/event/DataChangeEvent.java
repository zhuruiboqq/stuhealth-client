package com.vastcm.swing.selector.event;

import java.util.EventObject;

public class DataChangeEvent extends EventObject {
	private static final long serialVersionUID = 8144305194555443546L;
	private Object oldValue;
	private Object newValue;

	public DataChangeEvent(Object source) {
		this(source, null, null);
	}

	public DataChangeEvent(Object source, Object newValue, Object oldValue) {
		super(source);
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

}