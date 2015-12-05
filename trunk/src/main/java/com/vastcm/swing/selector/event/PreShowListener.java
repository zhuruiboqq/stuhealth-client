package com.vastcm.swing.selector.event;

import java.util.EventListener;

public abstract interface PreShowListener extends EventListener {
	public abstract void preShow(PreShowEvent preShowEvent);
}
