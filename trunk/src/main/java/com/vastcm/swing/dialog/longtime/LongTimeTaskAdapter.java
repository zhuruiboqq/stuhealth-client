package com.vastcm.swing.dialog.longtime;

public abstract class LongTimeTaskAdapter {
	public Object obj = null;

	public LongTimeTaskAdapter() {
	}

	public LongTimeTaskAdapter(Object o) {
		this.obj = o;
	}

	public abstract Object exec() throws Exception;

	public void finish(Object obj) throws Exception {
	}
}