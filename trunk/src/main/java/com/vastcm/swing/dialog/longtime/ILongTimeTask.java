package com.vastcm.swing.dialog.longtime;

public interface ILongTimeTask {
	public abstract Object exec() throws Exception;

	public abstract void afterExec(Object paramObject) throws Exception;
}