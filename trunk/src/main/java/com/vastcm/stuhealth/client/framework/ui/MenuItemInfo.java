/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import javax.swing.ImageIcon;

/**
 * 
 * @author bob
 */
public class MenuItemInfo {

	private String uiTitle;
	private Class uiClass;
	private UIType openType;
	private ImageIcon icon;

	public MenuItemInfo() {
		openType = UIType.TAB;
	}

	public MenuItemInfo(String uiTitle, Class uiClass) {
		this(uiTitle, uiClass, null);
	}

	public MenuItemInfo(String uiTitle, Class uiClass, ImageIcon icon) {
		this.uiTitle = uiTitle;
		this.uiClass = uiClass;
		this.icon = icon;
		openType = UIType.TAB;
	}

	public String getUiTitle() {
		return uiTitle;
	}

	public void setUiTitle(String uiTitle) {
		this.uiTitle = uiTitle;
	}

	public Class getUiClass() {
		return uiClass;
	}

	public void setUiClass(Class uiClass) {
		this.uiClass = uiClass;
	}

	public UIType getOpenType() {
		return openType;
	}

	public void setOpenType(UIType openType) {
		this.openType = openType;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return this.uiTitle;
	}
}