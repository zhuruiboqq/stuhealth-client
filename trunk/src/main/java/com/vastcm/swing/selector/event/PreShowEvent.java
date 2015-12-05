package com.vastcm.swing.selector.event;

import java.util.EventObject;

public class PreShowEvent extends EventObject {
	private static final long serialVersionUID = 8153764370864354932L;
	protected boolean canceled = false;

	public PreShowEvent(Object source) {
		super(source);
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setCanceled(boolean b) {
		this.canceled = b;
	}
}
