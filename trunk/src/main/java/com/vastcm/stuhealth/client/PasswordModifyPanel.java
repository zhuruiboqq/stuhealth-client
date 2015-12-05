/**
 * 
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.entity.User;
import com.vastcm.stuhealth.client.entity.service.IUserService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.utils.SecurityUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 5, 2013
 */
public class PasswordModifyPanel extends KernelUI {
	private JPasswordField txtOldPwd;
	private JPasswordField txtNewPwd;
	private JPasswordField txtNewPwd2;
	
	public PasswordModifyPanel() {
		setLayout(null);
		
		setPreferredSize(new Dimension(280, 180));
		
		JLabel lblNewLabel = new JLabel("旧密码");
		lblNewLabel.setBounds(16, 17, 80, 24);
		add(lblNewLabel);
		
		txtOldPwd = new JPasswordField();
		txtOldPwd.setBounds(104, 15, 151, 28);
		add(txtOldPwd);
		
		JLabel label = new JLabel("新密码");
		label.setBounds(16, 53, 80, 24);
		add(label);
		
		txtNewPwd = new JPasswordField();
		txtNewPwd.setBounds(104, 51, 151, 28);
		add(txtNewPwd);
		
		JLabel label_1 = new JLabel("确认新密码");
		label_1.setBounds(16, 89, 80, 24);
		add(label_1);
		
		txtNewPwd2 = new JPasswordField();
		txtNewPwd2.setBounds(104, 91, 151, 28);
		add(txtNewPwd2);
		
		JButton btnConfirm = new JButton("确定");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				String oldPwd = new String(txtOldPwd.getPassword());
				String newPwd = new String(txtNewPwd.getPassword());
				String newPwd2 = new String(txtNewPwd2.getPassword());
				boolean isNewPwdConsistent = true;
				if(newPwd == null || newPwd2 == null) {
					if(newPwd != null || newPwd2 != null) {
						isNewPwdConsistent = false;
					}
				}
				if(newPwd != null && newPwd2 != null) {
					if(!newPwd.equals(newPwd2)) {
						isNewPwdConsistent = false;
					}
				}
				if(!isNewPwdConsistent) {
					JOptionPane.showMessageDialog(PasswordModifyPanel.this, "新密码不一致！");
					return;
				}
				IUserService userSrv = AppContext.getBean("userService", IUserService.class);
				User user = userSrv.getByName(GlobalVariables.getCurrentUser());
				String oldPwdDB = user.getPassword();
				boolean isOldPwdCorrect = false;
				if(oldPwd == null && oldPwdDB == null) {
					isOldPwdCorrect = true;
				}
				else {
					if( oldPwd != null && oldPwdDB != null) {
						if(SecurityUtils.cryptPassword(oldPwd).equals(oldPwdDB)) {
							isOldPwdCorrect = true;
						}
					}
				}
				if(!isOldPwdCorrect) {
					JOptionPane.showMessageDialog(PasswordModifyPanel.this,  "旧密码不正确！");
					return;
				}
				user.setPassword(SecurityUtils.cryptPassword(newPwd));
				userSrv.save(user);
				JOptionPane.showMessageDialog(PasswordModifyPanel.this,  "密码修改成功！");
				disposeUI();
			}
		});
		btnConfirm.setBounds(84, 131, 80, 29);
		add(btnConfirm);
		
		JButton button = new JButton("取消");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				disposeUI();
			}
		});
		button.setBounds(175, 131, 80, 29);
		add(button);
	}
}
