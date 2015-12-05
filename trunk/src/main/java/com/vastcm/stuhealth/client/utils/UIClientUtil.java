package com.vastcm.stuhealth.client.utils;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.springframework.util.StringUtils;

import com.toedter.calendar.JDateChooser;
import com.vastcm.stuhealth.client.framework.SystemUtils;

public class UIClientUtil {

	public static void checkEmpty(JComponent owner, JComponent[] comps, String[] labels) {
		boolean hasText = false;
		for (int i = 0, size = comps.length; i < size; i++) {
			hasText = false;
			JComponent com = comps[i];
			if (JTextField.class.isAssignableFrom(com.getClass())) {
				JTextField field = (JTextField) com;
				hasText = StringUtils.hasText(field.getText());
			} else if (JComboBox.class.isAssignableFrom(com.getClass())) {
				JComboBox comBox = (JComboBox) com;
				hasText = comBox.getSelectedItem() != null;
			} else if (JDateChooser.class.isAssignableFrom(com.getClass())) {
				JDateChooser comBox = (JDateChooser) com;
				hasText = comBox.getDate() != null;
			}
			if (!hasText) {
				JOptionPane.showMessageDialog(owner, labels[i] + "不允许为空，请重新输入", "字段不允许为空", JOptionPane.ERROR_MESSAGE);
				com.requestFocusInWindow();
				SystemUtils.abort();
			}
		}
	}
}