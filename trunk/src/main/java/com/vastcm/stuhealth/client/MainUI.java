package com.vastcm.stuhealth.client;

import importfirebird.ImportCore;
import importfirebird.ImportFireBird;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vastcm.stuhealth.client.entity.BaseSetting;
import com.vastcm.stuhealth.client.entity.Notice;
import com.vastcm.stuhealth.client.entity.service.IBaseSettingService;
import com.vastcm.stuhealth.client.entity.service.INoticeService;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.framework.Request;
import com.vastcm.stuhealth.client.framework.SystemInitTask;
import com.vastcm.stuhealth.client.framework.exception.AbortException;
import com.vastcm.stuhealth.client.framework.exception.ConnectionException;
import com.vastcm.stuhealth.client.framework.exception.RequestException;
import com.vastcm.stuhealth.client.framework.ui.MenuItemInfo;
import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIManagerHelper;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.report.ui.FixItemRptPanel;
import com.vastcm.stuhealth.client.report.ui.StatureWeightBustLungCapacityRptPanel;
import com.vastcm.stuhealth.client.report.ui.StudentAppraiseRptPanel;
import com.vastcm.stuhealth.client.report.ui.StudentChangeClassRptPanel;
import com.vastcm.stuhealth.client.report.ui.StudentExamineStatSumRptPanel;
import com.vastcm.stuhealth.client.ui.TreeUtils;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.framework.Response;
import com.vastcm.stuhealth.framework.ResponseContent;
import com.vastcm.stuhealth.framework.ResponseUtils;
import com.vastcm.swing.LinkLabel;
import com.vastcm.swing.dialog.PopWin;
import com.vastcm.swing.jtree.MyTreeCellRender;

/**
 * 
 * @author House
 */
public class MainUI extends javax.swing.JFrame implements PropertyChangeListener {

	private Logger logger = LoggerFactory.getLogger(MainUI.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DefaultMutableTreeNode nodeImportOldData;

	/**
	 * Creates new form MainUI
	 */
	public MainUI() {
		try {
			SystemInitTask.onInit();
			UpdateChecker.startItemStandardUpdateChecker();
			//        BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
			//        theme.setBaseColor(new Color(50, 190, 150), true, "default");
			//        ((Office2003Painter) Office2003Painter.getInstance()).addTheme(theme);
			//
			//        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();

			initComponents();
			init();
		} catch (Exception e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		}
	}

	private void init() throws ConnectionException, RequestException, IOException {
		UIFrameworkUtils.setMainUI(this);
		initMenu();
		initStatusBar();
		BufferedImage bImg = ImageIO.read(getClass().getResource("/image/logo_2.png"));
		setIconImage(bImg);
		fetchNotice();
		jTree2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = jTree2.getPathForLocation(e.getX(), e.getY());
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					if (node.getUserObject() instanceof MenuItemInfo) {
						MenuItemInfo menuItemInfo = (MenuItemInfo) node.getUserObject();
						if (menuItemInfo.getClass() == null)
							return;

						logger.info("ui class name=" + menuItemInfo.getUiClass().getName());
						if (menuItemInfo.getUiClass().getName().equals("importfirebird.ImportFireBird")) {
							importOldData();
						} else if (menuItemInfo.getUiClass().getName().equals("com.vastcm.stuhealth.client.Restore2Init")) {
							Restore2Init r = new Restore2Init();
							r.startRestore();
						} else {
							showUI(menuItemInfo.getUiTitle(), menuItemInfo.getUiClass(), menuItemInfo.getOpenType());
						}
						logger.info("mouse click returned.");
						return;
					}
					System.out.println("node clicked: " + node.toString());
				}
			}
		});
	}

	private void importOldData() {
		final String oldDataFilePath = ImportFireBird.getOldDataFile();
		//    	ImportFireBird.owner = MainUI.this;
		if (oldDataFilePath == null || oldDataFilePath.trim().isEmpty()) {
			return;
		}
		final StringBuilder completeTag = new StringBuilder();
		Runnable task = new Runnable() {

			@Override
			public void run() {
				ImportFireBird.startImport(oldDataFilePath);
				//				ImportFireBird.afterImport();
				//				completeTag.delete(0, completeTag.length()-1);
				completeTag.append("done");
				logger.info("import done.");
			}
		};
		Thread t = new Thread(task);
		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			ExceptionUtils.writeExceptionLog(logger, e1);
		}

		Runnable updateTask = new Runnable() {

			@Override
			public void run() {
				if ("done".equals(completeTag.toString())) {
					logger.info("import thread done.");
					return;
				}
				logger.info("ImportCore.importcount < ImportCore.allcount = " + (ImportCore.importcount < ImportCore.allcount));
				logger.info("import thread tag=" + completeTag.toString());
				ImportFireBird.ShowBar();
				logger.info("show bar completed.");
				while (ImportCore.importcount < ImportCore.allcount && !"done".equals(completeTag.toString())) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						ExceptionUtils.writeExceptionLog(logger, e);
					}
					ImportFireBird.jpb.setValue(ImportCore.importcount);
					logger.info("set ImportCore.importcount=" + ImportCore.importcount);
				}
			}
		};

		Thread t1 = new Thread(updateTask);
		t1.start();
	}

	private void initStatusBar() {
		setTerm();
	}

	protected void setTerm(BaseSetting... settings) {
		StringBuilder sb = new StringBuilder();
		BaseSetting setting = null;
		if (settings.length == 0) {
			IBaseSettingService srv = AppContext.getBean("baseSettingService", IBaseSettingService.class);
			List<BaseSetting> ls = srv.getAll();
			if (ls != null && !ls.isEmpty()) {
				setting = ls.get(0);
			}
		} else {
			setting = settings[0];
		}
		int year = setting.getYear();
		sb.append("当前学年: ").append(year).append(" - ").append(year + 1); // 第 ").append(setting.getTerm()).append(" 学期");
		lblStatus1.setText(sb.toString());
	}

	private ImageIcon getImageIcon(String path) {
		try {
			return new ImageIcon(ImageIO.read(getClass().getResource(path)));
		} catch (IOException e) {
		}
		return null;
	}

	private void initMenu() throws IOException {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new MenuItemInfo("功能菜单", null, getImageIcon("/menu_icon/功能菜单.png")));
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		jTree2.setModel(treeModel);
		jTree2.setCellRenderer(new MyTreeCellRender());

		DefaultMutableTreeNode baseInfo = new DefaultMutableTreeNode(new MenuItemInfo("基本信息", null, getImageIcon("/menu_icon/基本信息.png")));

		MenuItemInfo about = new MenuItemInfo("关于", AboutPanel.class, getImageIcon("/menu_icon/关于.png"));
		about.setOpenType(UIType.MODAL);
		baseInfo.add(new DefaultMutableTreeNode(about));

		MenuItemInfo modifyPwd = new MenuItemInfo("修改密码", PasswordModifyPanel.class, getImageIcon("/menu_icon/修改密码.png"));
		modifyPwd.setOpenType(UIType.MODAL);
		baseInfo.add(new DefaultMutableTreeNode(modifyPwd));

		MenuItemInfo baseSetting = new MenuItemInfo("设置体检学年", BaseSettingPanel.class, getImageIcon("/menu_icon/设置体检学年.png"));
		baseSetting.setOpenType(UIType.MODAL);
		if (!AppCache.getInstance().isSchoolEdition())
			baseInfo.add(new DefaultMutableTreeNode(baseSetting));

		MenuItemInfo customSetting = new MenuItemInfo("个性化设置", CustomSettingPanel.class, getImageIcon("/menu_icon/个性化设置.png"));
		customSetting.setOpenType(UIType.MODAL);
		if (!AppCache.getInstance().isSchoolEdition())
			baseInfo.add(new DefaultMutableTreeNode(customSetting));

		MenuItemInfo backAndRestore = new MenuItemInfo("数据备份与还原", DatabaseBackupRecover.class, getImageIcon("/menu_icon/数据备份与还原.png"));
		backAndRestore.setOpenType(UIType.MODAL);
		baseInfo.add(new DefaultMutableTreeNode(backAndRestore));

		boolean isImportOldDataEnabled = false;
		File pfile = new File(System.getProperty("appHome") + File.separator + "config/oldData.properties");
		try {
			if (!pfile.exists()) {
				isImportOldDataEnabled = true;
			} else {
				Properties prop = new Properties();
				prop.load(new FileInputStream(pfile));
				String isImported = prop.getProperty("isImported");
				if (isImported != null && "false".equalsIgnoreCase(isImported)) {
					isImportOldDataEnabled = true;
				}
			}

		} catch (IOException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		}
		if (isImportOldDataEnabled && !AppCache.getInstance().isSchoolEdition()) {
			MenuItemInfo importFireBirdData = new MenuItemInfo("旧版客户端学生数据导入", ImportFireBird.class, getImageIcon("/menu_icon/旧版客户端学生数据导入.png"));
			importFireBirdData.setOpenType(UIType.MODAL);
			nodeImportOldData = new DefaultMutableTreeNode(importFireBirdData);
			baseInfo.add(nodeImportOldData);
		}

		MenuItemInfo notice = new MenuItemInfo("通知", NoticeListPanel.class, getImageIcon("/menu_icon/通知.png"));
		baseInfo.add(new DefaultMutableTreeNode(notice));

		baseInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("学校维护", SchoolTreePanel.class, getImageIcon("/menu_icon/学校维护.png"))));
		baseInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("体检标准查询", StandardListPanel.class, getImageIcon("/menu_icon/体检标准查询.png"))));
		root.add(baseInfo);

		DefaultMutableTreeNode studentInfo = new DefaultMutableTreeNode(new MenuItemInfo("学生信息", NoticeListPanel.class,
				getImageIcon("/menu_icon/学生信息.png")));
		studentInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("学生信息维护", StudentListPanel.class, getImageIcon("/menu_icon/学生信息维护.png"))));
		studentInfo.add(new DefaultMutableTreeNode(
				new MenuItemInfo("调班毕业查询", StudentChangeClassRptPanel.class, getImageIcon("/menu_icon/调班毕业查询.png"))));
		if (!AppCache.getInstance().isSchoolEdition())
			root.add(studentInfo);

		DefaultMutableTreeNode checkInfo = new DefaultMutableTreeNode(new MenuItemInfo("体检信息", NoticeListPanel.class,
				getImageIcon("/menu_icon/体检信息.png")));
		checkInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("体检项目选择", CheckItemSelectorPanel.class, getImageIcon("/menu_icon/体检项目选择.png"))));
		checkInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("体检结果录入", CheckResultListPanel.class, getImageIcon("/menu_icon/体检结果录入.png"))));
		checkInfo.add(new DefaultMutableTreeNode(new MenuItemInfo("Excel导入", ImportResultDataPanel.class, getImageIcon("/menu_icon/Excel导入.png"))));
		if (!AppCache.getInstance().isSchoolEdition())
			root.add(checkInfo);

		DefaultMutableTreeNode rptMgmt = new DefaultMutableTreeNode(new MenuItemInfo("报表", null, getImageIcon("/menu_icon/报表_目录.png")));
		rptMgmt.add(new DefaultMutableTreeNode(new MenuItemInfo("个体评价报表", StudentAppraiseRptPanel.class, getImageIcon("/menu_icon/报表.png"))));
		// rptMgmt.add(new DefaultMutableTreeNode(new MenuItemInfo("常见疾患查询", CommonDiseaseRptPanel.class, getImageIcon("/menu_icon/test.png"))));
		rptMgmt.add(new DefaultMutableTreeNode(
				new MenuItemInfo("学生健康体检统计汇总表", StudentExamineStatSumRptPanel.class, getImageIcon("/menu_icon/报表.png"))));
		rptMgmt.add(new DefaultMutableTreeNode(new MenuItemInfo("身高体重胸围肺活量", StatureWeightBustLungCapacityRptPanel.class,
				getImageIcon("/menu_icon/报表.png"))));
		rptMgmt.add(new DefaultMutableTreeNode(new MenuItemInfo("定性项目统计", FixItemRptPanel.class, getImageIcon("/menu_icon/报表.png"))));
		root.add(rptMgmt);

		DefaultMutableTreeNode uploadMgmt = new DefaultMutableTreeNode(new MenuItemInfo("数据上传下载", null, getImageIcon("/menu_icon/数据上传下载_目录.png")));

		MenuItemInfo serverSetting = new MenuItemInfo("服务器连接设置", ServerSettingPanel.class, getImageIcon("/menu_icon/服务器连接设置.png"));
		serverSetting.setOpenType(UIType.MODAL);
		uploadMgmt.add(new DefaultMutableTreeNode(serverSetting));

		MenuItemInfo upload = new MenuItemInfo("数据上传", UploadPanel.class, getImageIcon("/menu_icon/数据上传.png"));
		upload.setOpenType(UIType.MODAL);
		if (!AppCache.getInstance().isSchoolEdition())
			uploadMgmt.add(new DefaultMutableTreeNode(upload));

		MenuItemInfo download = new MenuItemInfo("数据下载", DownloadPanel.class, getImageIcon("/menu_icon/数据下载.png"));
		download.setOpenType(UIType.MODAL);
		uploadMgmt.add(new DefaultMutableTreeNode(download));

		root.add(uploadMgmt);

		DefaultMutableTreeNode sysInit = new DefaultMutableTreeNode(new MenuItemInfo("初始化", null, getImageIcon("/menu_icon/数据备份与还原.png")));
		MenuItemInfo restore2Init = new MenuItemInfo("系统数据初始化", Restore2Init.class, getImageIcon("/menu_icon/数据备份与还原.png"));
		restore2Init.setOpenType(UIType.MODAL);
		sysInit.add(new DefaultMutableTreeNode(restore2Init));
		root.add(sysInit);

		TreeUtils.expand(jTree2, new TreePath(root), 4);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void removeOldDataImportMenu() {
		if (nodeImportOldData != null)
			((DefaultTreeModel) jTree2.getModel()).removeNodeFromParent(nodeImportOldData);
	}

	private void showUI(String title, Class clazz) {
		showUI(title, clazz, UIType.TAB);
	}

	private void showUI(String title, Class clazz, UIType openType) {
		try {
			UIContext ctx = new UIContext();
			ctx.set(UIContext.UI_TITLE, title);
			UI ui = UIFactory.create(clazz, openType, ctx, MainUI.this);
			ui.setVisible(true);
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
			//            JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	public void fetchNotice() {
		Request reqTestConnection = new Request("");
		try {
			reqTestConnection.testConnection();
		} catch (Exception e) {
			logger.error("连接服务器 " + reqTestConnection.getServerIP() + " 失败！ 跳过通知检查程序！");
			ExceptionUtils.writeExceptionLog(logger, e);
			return;
		}

		final DefaultListModel lsModel = new DefaultListModel();

		Runnable task = new Runnable() {

			@Override
			public void run() {
				INoticeService noticeSrv = AppContext.getBean("noticeService", INoticeService.class);
				Date clientUpdateDate = noticeSrv.getLatestUpdateDate();
				if (clientUpdateDate == null) {
					try {
						clientUpdateDate = sdf.parse("2001-01-01 00:00:00");
					} catch (ParseException e) {
						ExceptionUtils.writeExceptionLog(logger, e);
					}
				}
				String url = "/download/GetNewNotice";
				Request req = new Request(url);
				req.setParam("latestDate", sdf.format(clientUpdateDate));
				logger.info("Client's lastUpdate Notice is: " + sdf.format(clientUpdateDate));
				try {
					Response resp = req.send();
					ResponseContent respContent = resp.getExContent();
					boolean isExistUpdate = Boolean.parseBoolean(respContent.get("isExist"));
					if (isExistUpdate) {
						String serverNoticeDate = respContent.get("serverLatestDate");
						logger.info("Server's lastUpdate Notice is: " + serverNoticeDate);
						String json = respContent.get("data");
						logger.info("charset=" + Charset.defaultCharset());
						//						try {ˇ
						//							json = new String(json.getBytes("GBK"), "UTF-8");
						//						} catch (UnsupportedEncodingException e1) {
						//							e1.printStackTrace();
						//						}
						java.lang.reflect.Type collectionType = new TypeToken<List<Notice>>() {
						}.getType();
						List<Notice> notices = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(json, collectionType);
						for (Notice n : notices) {
							logger.info("title=" + n.getNoticeTitle());
							logger.info("content=" + n.getNoticeContent());
							logger.info("url=" + n.getUrl());
						}
						if (notices.size() > 0) {
							Notice n = notices.get(0);
							LinkLabel linkLabel = new LinkLabel("新通知：" + n.getNoticeTitle(), n.getUrl());
							PopWin popWin = new PopWin(linkLabel);
							popWin.setVisible(true);
						}
						noticeSrv.save(notices);
						logger.info("Update notice completed.");
					} else {
						logger.info("Client's Notice is update to date.");
					}
					List<Notice> allNotices = noticeSrv.getAll();
					for (int i = 0; i < allNotices.size(); i++) {
						Notice n = allNotices.get(i);
						lsModel.add(0, n);
					}
				} catch (ConnectionException e) {
					logger.error("Update Notice excpetion.");
					ExceptionUtils.writeExceptionLog(logger, e);
				} catch (RequestException e) {
					logger.error("Update Notice excpetion.");
					ExceptionUtils.writeExceptionLog(logger, e);
				}
			}
		};

		SwingUtilities.invokeLater(task);

	}

	public void retrieveNews() throws ConnectionException, RequestException {
		Request reqAuth = new Request("/auth");
		reqAuth.setParam("username", "admin");
		reqAuth.setParam("password", "admin");
		Response respAuth = reqAuth.send();
		if (respAuth.getRetCode() != ResponseUtils.RETCODE_OK) {
			logger.error("ERROR [" + respAuth.getRetCode() + "]-" + respAuth.getRetMsg());
			return;
		}
		String sessionId = respAuth.getSessionId();
		logger.debug("news: " + sessionId);

		Request req = new Request("/news");
		req.setParam("sessionId", sessionId);
		Response resp = req.send();
		Iterator<String> iter = resp.getExContent().iteratorValues();
		StringBuilder newsContent = new StringBuilder();
		while (iter.hasNext()) {
			String value = iter.next();
			newsContent.append(value).append("\n");
		}
		logger.debug("news: " + newsContent.toString());

		Request reqUpdateLs = new Request("/UpdateList");
		reqUpdateLs.setParam("sessionId", sessionId);
		Response resUpdateLs = reqUpdateLs.send();
		Iterator<String> iterUpdateLs = resUpdateLs.getExContent().iteratorValues();
		while (iterUpdateLs.hasNext()) {
			String value = iterUpdateLs.next();
			Request reqDownload = new Request("/Download");
			reqDownload.setParam("sessionId", sessionId);
			reqDownload.setParam("file", value);
			String userDir = System.getProperty("user.dir");
			logger.debug("user dir: " + userDir);
			reqDownload.download(userDir);
		}
		logger.debug("news: " + newsContent.toString());
	}

	public JTabbedPane getMainPanel() {
		return this.mainPanel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (BaseSettingPanel.PROPERTY_PERIOD.equals(pce.getPropertyName())) {
			setTerm((BaseSetting) pce.getNewValue());
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		mainPanel = new javax.swing.JTabbedPane();
		jPanel2 = new javax.swing.JPanel();
		jSplitPane2 = new javax.swing.JSplitPane();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTree2 = new javax.swing.JTree();
		jPanel5 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		lblStatus1 = new javax.swing.JLabel();

		String title = "广东省中小学生健康体检管理系统";
		if (AppCache.getInstance().isSchoolEdition()) {
			title += "（学校版）";
			lblStatus1.setVisible(false);
		}
		setTitle(title);

		jPanel2.setLayout(new java.awt.BorderLayout());

		jSplitPane2.setDividerLocation(220);

		jScrollPane2.setViewportView(jTree2);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
				javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2,
				javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE));

		jSplitPane2.setLeftComponent(jPanel4);

		jPanel5.setLayout(new java.awt.BorderLayout());

		jSplitPane2.setRightComponent(jPanel5);

		lblImage = new JLabel("");
		BufferedImage bImg;
		try {
			String bgImg = "/image/mainbg.jpg";
			if (AppCache.getInstance().isSchoolEdition())
				bgImg = "/image/mainbg_school.jpg";
			bImg = ImageIO.read(getClass().getResource(bgImg));
			lblImage.setBackground(Color.WHITE);
			lblImage.setHorizontalAlignment(JLabel.CENTER);
			lblImage.setVerticalAlignment(JLabel.CENTER);
			lblImage.setIcon(new ImageIcon(bImg));
		} catch (IOException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		}
		jPanel5.setBackground(Color.WHITE);
		jPanel5.add(lblImage, BorderLayout.CENTER);

		jPanel2.add(jSplitPane2, java.awt.BorderLayout.CENTER);

		mainPanel.addTab("主控台", jPanel2);

		jPanel1.setPreferredSize(new java.awt.Dimension(36, 15));

		lblStatus1.setText("状态栏");
		jPanel1.add(lblStatus1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1157, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(mainPanel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		mainPanel.getAccessibleContext().setAccessibleName("主控台");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(MainUI.this, "确认要退出系统吗？", "确认", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) throws IOException {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		AppCache.initAppCacheNoDb();
		UIManagerHelper.setLookAndFeel(UIManagerHelper.getLafClassName(AppCache.getInstance().lookAndFeel()));
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI ui = new MainUI();
					//                    ui.retrieveNews();
					ui.setVisible(true);
				} catch (AbortException ex) {
					System.out.println("abort");
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				} catch (Exception ex) {
					System.out.println("Encounter An Error!");
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JTree jTree2;
	private javax.swing.JLabel lblStatus1;
	private javax.swing.JTabbedPane mainPanel;
	private JLabel lblImage;
	// End of variables declaration//GEN-END:variables

}
