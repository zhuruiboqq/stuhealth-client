package com.vastcm.swing.selector.event;

import java.util.EventObject;

public class PreChangeEvent extends EventObject {
	private static final long serialVersionUID = -8119819867059432656L;
	public static final int S_OK = 1;
	public static final int S_FALSE = 0;
	public static final int E_FAIL = -1;

	protected Object oldData;
	protected Object data;
	protected int result = 1;

	public PreChangeEvent(Object source) {
		super(source);
	}

	public PreChangeEvent(Object source, Object newData, Object oldData) {
		super(source);
		setData(newData);
		setOldData(oldData);
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getOldData() {
		return this.oldData;
	}

	public void setOldData(Object object) {
		this.oldData = object;
	}

	public int getResult() {
		return this.result;
	}

	public void setResult(int r) {
		this.result = r;
	}

	public boolean succeeded() {
		return (getResult() >= 0);
	}
}