/**
 * 
 */
package com.vastcm.stuhealth.client.ui;

import java.util.Calendar;
import java.util.Locale;

import com.toedter.calendar.JDateChooser;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 13, 2013
 */
public class MyDateChooser extends JDateChooser {

	public MyDateChooser() {
		super();
		dateEditor.setEnabled(false);
		initDefault();
	}
	
	protected void initDefault() {
		setLocale(Locale.CHINA);
		setDateFormatString("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		setMinSelectableDate(cal.getTime());
		cal.add(Calendar.YEAR, 100);
		setMaxSelectableDate(cal.getTime());
	}
}
