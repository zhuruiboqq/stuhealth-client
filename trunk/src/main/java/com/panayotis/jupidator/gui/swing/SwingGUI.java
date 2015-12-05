/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.panayotis.jupidator.gui.swing;

import com.panayotis.jupidator.ApplicationInfo;
import com.panayotis.jupidator.Updater;
import com.panayotis.jupidator.UpdaterException;
import com.panayotis.jupidator.data.TextUtils;
import com.panayotis.jupidator.data.UpdaterAppElements;
import com.panayotis.jupidator.elements.security.PermissionManager;
import com.panayotis.jupidator.gui.JupidatorGUI;
import com.panayotis.jupidator.i18n.I18N;
import com.panayotis.jupidator.loglist.creators.HTMLCreator;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

@SuppressWarnings("unused")
public class SwingGUI implements JupidatorGUI {
	private SwingDialog gui;
	private Updater callback;
	private String newver;
	private String versinfo;
	private String title;
	private String infopane;
	private BufferedImage icon;
	private boolean skipvisible;
	private boolean prevvisible;
	private boolean detailedvisible;
	private boolean infovisible;
	private boolean systemlook;

	public SwingGUI() {
		this.detailedvisible = false;
		this.infovisible = true;
		this.systemlook = true;
	}

	public boolean isHeadless() {
		return false;
	}

	public void setInformation(Updater callback, UpdaterAppElements el,
			ApplicationInfo info) throws UpdaterException {
		this.callback = callback;
		this.newver = el.getAppName() + " 发现新版本 ";
//				I18N._("A new version of {0} is available!",
//				new Object[] { el.getAppName() });
		this.versinfo =  " 新版本 " + el.getNewestVersion() + " 现可下载! ";
//				" I18N._("{0} version {1} is now available",
//				new Object[] { el.getAppName(), el.getNewestVersion() })
//				+ ((info.getVersion() == null) ? "" : new StringBuilder()
//						.append(" - ")
//						.append(I18N._("you have {0}",
//								new Object[] { info.getVersion() })).toString())
//				+ ".";

		this.title = "系统更新"; 
//			I18N._("New version of {0} found!",
//				new Object[] { el.getAppName() });
		this.infopane = HTMLCreator.getList(el.getLogList(), true);
		try {
			String iconpath = el.getIconpath();
			if ((iconpath != null) && (!(iconpath.equals(""))))
				this.icon = ImageIO.read(new URL(iconpath));
		} catch (IOException ex) {
		}
		this.skipvisible = (!(info.isSelfUpdate()));
		this.prevvisible = PermissionManager.manager.isRequiredPrivileges();
	}

	public void setProperty(String key, String value) {
		key = key.toLowerCase();
		if (key.equals("about"))
			this.infovisible = TextUtils.isTrue(value);
		else if (key.equals("systemlook"))
			this.systemlook = TextUtils.isTrue(value);
		else if (key.equals("loglist"))
			this.detailedvisible = TextUtils.isTrue(value);
	}

	public void startDialog() {
		if (this.gui != null)
			return;
		try {
			if (this.systemlook)
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
		this.gui = new SwingDialog();
		this.gui.callback = this.callback;
		this.gui.NewVerL.setText(this.newver);
		this.gui.VersInfoL.setText(this.versinfo);
		this.gui.setTitle(this.title);
		this.gui.InfoPane.setContentType("text/html");
		this.gui.InfoPane.setText(this.infopane);
		this.gui.icon = this.icon;
		this.gui.SkipB.setVisible(this.skipvisible);
		this.gui.PrevL.setVisible(this.prevvisible);
		this.gui.InfoB.setVisible(this.infovisible);
		this.gui.DetailedP.setVisible(this.detailedvisible);
		this.gui.ActionB.setVisible(false);
		this.gui.LaterB.setVisible(false);
		this.gui.SkipB.setVisible(false);
		this.gui.DetailsB.setText("详情");
		this.gui.UpdateB.setText("安装更新");
		this.gui.pack();
		this.gui.setLocationRelativeTo(null);
		this.gui.setVisible(true);
	}

	public void endDialog() {
		this.gui.setVisible(false);
		this.gui.dispose();
	}

	public void setIndetermined() {
		this.gui.ActionB.setEnabled(false);
		this.gui.PBar.setIndeterminate(true);
		this.gui.PBar
				.setToolTipText("现在更新，请稍候...");
//				.setToolTipText(I18N._("Processing update", new Object[0]));
		this.gui.PBar.setString("");
		this.gui.InfoL.setText("现在部署文件，请稍候..."); //I18N._("Deploying files...", new Object[0]));
	}

	public void errorOnCommit(String message) {
		setInfoArea(message);
		this.gui.InfoL.setForeground(Color.RED);
		this.gui.ProgressP.revalidate();
	}

	public void successOnCommit(boolean restartableApp) {
		setInfoArea("成功下载更新！"); //I18N._("Successfully downloaded updates", new Object[0]));
		this.gui.ActionB.setText((restartableApp) ? "重启体检系统" : "清理更新"); 
//		this.gui.ActionB.setText((restartableApp) ? I18N._(
//				"Restart application", new Object[0]) : I18N._(
//				"Finalize update", new Object[0]));
		this.gui.ActionB.setActionCommand("restart");
		this.gui.ActionB.setVisible(true);
		this.gui.ProgressP.revalidate();
	}

	public void errorOnRestart(String message) {
		if (message == null)
			setInfoArea("重启体检系统失败！");
//			setInfoArea(I18N._("Application cancelled restart", new Object[0]));
		else
			setInfoArea(message);
		this.gui.ActionB.setText("取消"); //I18N._("Cancel", new Object[0]));
		this.gui.ActionB.setActionCommand("cancel");
	}

	public void setDownloadRatio(String ratio, float percent) {
		this.gui.PBar.setValue(Math.round(percent * 100.0F));
		this.gui.PBar.setToolTipText("下载速率：" + ratio);
//		this.gui.PBar.setToolTipText(I18N._("Download speed: {0}",
//				new Object[] { ratio }));
		this.gui.PBar.setString(ratio);
	}

	private void setInfoArea(String message) {
		this.gui.ActionB.setEnabled(true);
		this.gui.BarPanel.remove(this.gui.PBar);
		this.gui.ProgressP.remove(this.gui.InfoL);
		this.gui.BarPanel.add(this.gui.InfoL);
		this.gui.InfoL.setText(message);
	}
}