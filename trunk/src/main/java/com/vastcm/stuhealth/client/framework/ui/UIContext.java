/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author House
 */
public class UIContext {

	public static final String UI_TITLE = "title";
	public static final String UI_OWNER = "owner";
	public static final String UI_STATUS = "status";
    public static final String VO_ID = "id";
    public static final String UI_CAN_CLOSE = "can_close";    

	private Map<String, Object> ctx = new HashMap<String, Object>();

	public UIContext() {

	}

	public UIContext(UIContext uiCtx) {
		ctx.putAll(uiCtx.ctx);
	}

	public void set(String key, Object value) {
		ctx.put(key, value);
	}

	public Object get(String key) {
		return ctx.get(key);
	}

	public void putAll(UIContext uiCtx) {
		ctx.putAll(uiCtx.ctx);
	}
    
}
