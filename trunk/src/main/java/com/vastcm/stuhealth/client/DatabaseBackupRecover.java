package com.vastcm.stuhealth.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vastcm.io.file.GZFileFilter;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.utils.DateUtil;
import com.vastcm.stuhealth.client.utils.FileChooseUtil;
import com.vastcm.stuhealth.client.utils.MessageDialogUtil;
import com.vastcm.swing.dialog.longtime.LongTimeTaskAdapter;
import com.vastcm.swing.dialog.longtime.LongTimeTaskDialog;

public class DatabaseBackupRecover extends KernelUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage = "ERROR>Warning: Using a password on the command line interface can be insecure.\n";
	protected Logger logger;
	public static final String File_Encode = "utf8";
	public static String Database_Name;
	public static String Database_User;
	public static String Database_Password ;
	private boolean isRecoveryVisible = true;
	JButton btnBackupDatabase = new JButton("备份数据库");
	JButton btnRecoverDatabase = new JButton("恢复数据库");

	public DatabaseBackupRecover() {
		logger = LoggerFactory.getLogger(getClass());
//		setDbInfo();
		setLayout(new MigLayout("", "[grow]", "[grow]"));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("数据库管理"));

		add(panel, "cell 0 0,grow");

		btnBackupDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LongTimeTaskDialog dialog = LongTimeTaskDialog.getInstance("备份数据库", new LongTimeTaskAdapter() {
					@Override
					public Object exec() throws Exception {
						try {
							btnBackupDatabase_actionPerformed(null);
						} catch (Exception ee) {
							UIHandler.handleException(DatabaseBackupRecover.this, logger, ee);
						}
						return null;
					}
				});
				dialog.show();
			}
		});
		panel.add(btnBackupDatabase);

		btnRecoverDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LongTimeTaskDialog dialog = LongTimeTaskDialog.getInstance("恢复数据库", new LongTimeTaskAdapter() {
					@Override
					public Object exec() throws Exception {
						try {
							btnRecoverDatabase_actionPerformed(null);
						} catch (Exception ee) {
							UIHandler.handleException(DatabaseBackupRecover.this, logger, ee);
						}
						return null;
					}
				});
				dialog.show();
			}
		});
		panel.add(btnRecoverDatabase);
	}
	
	private void setDbInfo() {
		if(!AppCache.getInstance().isSchoolEdition()) {
			Database_Name = "stuhealth_client";
			Database_User = "root";
			Database_Password = "stuhealthdoor";
		}
		else {
			Database_Name = "stuhealth_client_school";
			Database_User = "school";
			Database_Password = "youarethefuture";
		}
	}

	public boolean isRecoveryVisible() {
		return isRecoveryVisible;
	}

	public void setRecoveryVisible(boolean isRecoveryVisible) {
		this.isRecoveryVisible = isRecoveryVisible;
		btnRecoverDatabase.setVisible(isRecoveryVisible);
	}

	protected void btnBackupDatabase_actionPerformed(ActionEvent e) throws Exception {
		//System.getProperty("appHome")获取到C:\stuhealth_client\app
		String fileName = DateUtil.getDateString(Calendar.getInstance().getTime(), "yyyy_MM_dd_hhmmssSSS") + ".gz";
		File appRoot = new File(System.getProperty("appHome")).getParentFile();
		String mysqlBinPath = appRoot.getAbsolutePath() + File.separator + "mysql" + File.separator + "bin" + File.separator;
		File backupFile = new File(appRoot.getAbsolutePath() + File.separator + "mysql" + File.separator + "backup" + File.separator + fileName);
		backupFile.getParentFile().mkdir();
		backupFile = FileChooseUtil.chooseFile4Save(this, new GZFileFilter(), ".gz", backupFile);
		if (backupFile == null)
			return;
		long countTime = System.currentTimeMillis();
		String command = mysqlBinPath + "mysqldump -u" + Database_User + " -p" + Database_Password + " " + Database_Name + " | " + mysqlBinPath + "gzip "
				+ "- > \"" + backupFile.getAbsolutePath() + "\"";//exe,bat文件名OR DOS命令
		{
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";
			cmd[2] = command;
			Runtime rt = Runtime.getRuntime();
			//			System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			String message = errorGobbler.message.toString();
			if (StringUtils.hasText(message) && !errorMessage.equals(message)) {
				logger.error("备份出错：" + message);
				MessageDialogUtil.showErrorDetail(this, "备份出错，请复制详细的错误信息，并反馈给管理员！", message);
				return;
			}
			System.out.println("ExitValue: " + exitVal);
		}
		logger.info("备份完成！用时：" + (System.currentTimeMillis() - countTime) + "。备份文件：" + backupFile.getAbsolutePath());
		JOptionPane.showMessageDialog(this, "备份完成！备份到：" + backupFile.getAbsolutePath());
		disposeUI();
	}

	protected void btnRecoverDatabase_actionPerformed(ActionEvent e) throws Exception {
		//System.getProperty("appHome")获取到C:\stuhealth_client\app
		File appRoot = new File(System.getProperty("appHome")).getParentFile();
		String mysqlBinPath = appRoot.getAbsolutePath() + File.separator + "mysql" + File.separator + "bin" + File.separator;
		File backupPath = new File(appRoot.getAbsolutePath() + File.separator + "mysql" + File.separator + "backup" + File.separator + "*.gz");
		File backupFile = FileChooseUtil.chooseFile4Open(this, new GZFileFilter(), backupPath);
		if (backupFile == null)
			return;

		long countTime = System.currentTimeMillis();
		String command = mysqlBinPath + "gzip -dc < \"" + backupFile.getAbsolutePath() + "\" | " + mysqlBinPath + "mysql -u" + Database_User + " -p"
				+ Database_Password + " " + Database_Name + " ";// ;//exe,bat文件名OR DOS命令
		{
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";
			cmd[2] = command;
			Runtime rt = Runtime.getRuntime();
			//			System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			String message = errorGobbler.message.toString();
			if (StringUtils.hasText(message) && !errorMessage.equals(message)) {
				logger.error("恢复出错：" + message);
				MessageDialogUtil.showErrorDetail(this, "恢复出错，请复制详细的错误信息，并反馈给管理员！", message);
				return;
			}
			System.out.println("ExitValue: " + exitVal);
		}
		logger.info("恢复完成！用时：" + (System.currentTimeMillis() - countTime) + "。恢复文件：" + backupFile.getAbsolutePath());
		JOptionPane.showMessageDialog(this, "恢复完成！恢复文件：" + backupFile.getAbsolutePath());
	}

	class StreamGobbler extends Thread {
		InputStream is;
		String type;
		StringBuffer message = new StringBuffer();

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					logger.info(type + ">" + line);
					message.append(type).append(">").append(line).append("\n");
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
