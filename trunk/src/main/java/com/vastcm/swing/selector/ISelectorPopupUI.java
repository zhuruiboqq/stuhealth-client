package com.vastcm.swing.selector;

import com.vastcm.stuhealth.client.framework.report.RptParamInfo;

public interface ISelectorPopupUI {

	public Object getValue();

	public void setValue(Object value);

	public void show();
	
	public boolean isCancel();

	public void setRptParamInfo(RptParamInfo rptParamInfo);
	
	public RptParamInfo getRptParamInfo();
	
	public void setSelectorParam(SelectorParam selectorParam);
	
	public SelectorParam getSelectorParam();
}