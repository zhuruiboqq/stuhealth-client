/**
 * 
 */
package com.vastcm.stuhealth.client;


import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.utils.biz.AboutInfoUtils;

import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Nov 1, 2013
 */
public class AboutPanel extends KernelUI {
	
	private Logger logger = LoggerFactory.getLogger(AboutPanel.class);
	private JTextArea textArea;
	private String version;
	private final String aboutInfoFilePath;
	private final File aboutInfoFile;
	private final String KEY_ORGNAME = "orgName";
	private final String KEY_VERSION = "version";

	/**
	 * Create the panel.
	 */
	public AboutPanel() {
		setLayout(null);
		
		setPreferredSize(new Dimension(450, 310));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(6, 6, 438, 258);
		add(textArea);
		
		JButton btnConfirm = new JButton("确定");
		btnConfirm.setBounds(173, 271, 112, 29);
		btnConfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnConfirm_actionPerformed(e);
			}
		});
		add(btnConfirm);
		
		JButton btnSetOrgName = new JButton("设置单位名称");
		btnSetOrgName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSetOrgName_actionPerformed(e);
			}
		});
		btnSetOrgName.setBounds(312, 271, 132, 29);
		add(btnSetOrgName);
		
		aboutInfoFilePath = System.getProperty("appHome") + "/config/about.properties";
		aboutInfoFile = new File(aboutInfoFilePath);
		if(!aboutInfoFile.exists()) {
			try {
				aboutInfoFile.createNewFile();
			} catch (IOException e) {
				logger.error("create about.properties error.", e);
			}
		}
		initContent();
	}
	
	private void btnSetOrgName_actionPerformed(ActionEvent e) {
		String orgName = JOptionPane.showInputDialog(this, "请输入 单位名称 ");
		if(orgName != null) {
			try {
				Properties p = getAboutInfo();
				p.setProperty(KEY_ORGNAME, orgName);
				saveAboutInfo(p);
			}
			catch(IOException ex) {
				logger.error("save about.properties error.", ex);
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
			loadContent();
		}
	}
	
	private void btnConfirm_actionPerformed(ActionEvent e) {
		disposeUI();
	}
	
	private void initContent() {
		loadContent();
	}
	
	private void loadContent() {
		StringBuilder txt = new StringBuilder();
		txt.append("版本：").append(getVersion()).append("\n");
		Properties about = null;
		try {
			about = getAboutInfo();
			String orgName = about.getProperty(KEY_ORGNAME);
			orgName = orgName == null ? "" : orgName;
			txt.append("单位：").append(orgName).append("\n");
		} 
		catch (IOException e) {
			logger.error("load about.properties error.", e);
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		txt.append("All Rights Reserved. 2013");
		textArea.setText(txt.toString());
	}
	
	private void saveAboutInfo(Properties p) throws IOException {
		try {
			p.store(new FileOutputStream(aboutInfoFile), "Created by stuhealth team.");
		} 
		catch (Exception e) {
			logger.error("store about.properties failed.", e);
			throw new IOException("保存 关于 信息失败!", e);
		}
	}
	
	private Properties getAboutInfo() throws IOException {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(aboutInfoFile));
		} 
		catch (Exception e) {
			logger.error("load about.properties failed.", e);
			throw new IOException("读取 关于 信息失败!", e);
		}
		return p;
	}
	
	private String getVersion() {
		if(version != null) {
			return version;
		}
		File jupidatorUpdateFile = new File(System.getProperty("appHome") + File.separator + ".last_successful_update");
		Properties pUpdate = new Properties();
		version = "unknown";
		try {
			pUpdate.load(new FileInputStream(jupidatorUpdateFile));
			version = pUpdate.getProperty(KEY_VERSION);
		} 
		catch (FileNotFoundException e) {
			logger.error("找不到版本文件", e);
		} 
		catch (IOException e) {
			logger.error("读取版本文件失败", e);
		}
		if(version == null) version = "";
		return version;
	}
}
