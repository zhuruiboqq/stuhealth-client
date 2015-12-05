package com.vastcm.swing.selector.event;

import java.util.EventListener;

public abstract interface PreChangeListener extends EventListener {
	public abstract void preChange(PreChangeEvent paramPreChangeEvent);
}