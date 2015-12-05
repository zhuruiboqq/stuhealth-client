/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.panayotis.jupidator.UpdatedApplication;
import com.panayotis.jupidator.Updater;
import com.panayotis.jupidator.UpdaterException;
import com.vastcm.stuhealth.client.entity.User;
import com.vastcm.stuhealth.client.entity.service.IUserService;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.framework.Request;
import com.vastcm.stuhealth.client.framework.SQLExecutor;
import com.vastcm.stuhealth.client.framework.SystemUtils;
import com.vastcm.stuhealth.client.framework.ui.ScreenUtils;
import com.vastcm.stuhealth.client.framework.ui.UIManagerHelper;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.SecurityUtils;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.BorderLayout;

/** 
 *
 * @author House
 */
public class LoginUI extends javax.swing.JFrame implements UpdatedApplication {

    private static final Logger logger = LoggerFactory.getLogger(LoginUI.class);
    /**
     * Creates new form LoginUI
     */
    public LoginUI() { 
        initComponents();
        try {
            initComponentsEx();
        } 
        catch (IOException ex) {
            ExceptionUtils.writeExceptionLog(logger, ex);
        }
        updateSQL();
    }
    
   
    
    private void updateSQL() {
    	Runnable updateSQLTask = new Runnable() {
			
			@Override
			public void run() {
				SQLExecutor sqlExec = new SQLExecutor();
				sqlExec.executeSQL();
			}
		};
		Thread t = new Thread(updateSQLTask);
		t.start();
    }
    
    public void startUpdater() {
    	String updateXmlUrl = System.getProperty("updateXmlUrl");
    	String appHome = System.getProperty("appHome");
    	Request reqTestConnection = new Request("");
    	String url = updateXmlUrl.substring("http://".length());
    	int endOfIP = url.indexOf("/");
    	String[] ipport = url.substring(0, endOfIP).split(":");
    	String serverIP = ipport[0];
    	String serverPort = ipport.length == 2 ? ipport[1] : "80";
    	reqTestConnection.setServerIP(serverIP);
    	reqTestConnection.setServerPort(serverPort);
		try {
			reqTestConnection.testConnection();
		} 
		catch (Exception e) {
			logger.error("连接服务器 " + reqTestConnection.getServerIP() + " 失败！ 跳过客户端自动更新检查程序！");
			ExceptionUtils.writeExceptionLog(logger, e);
			return;
		}
    	Updater updater = null;
		try {
			updater = new Updater(updateXmlUrl, appHome, this);
			updater.actionDisplay();
		} 
		catch (UpdaterException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
		}
    	
    }
    
    public void initComponentsEx() throws IOException {
        BufferedImage bImg = ImageIO.read(getClass().getResource("/image/logo_2.png"));
        setIconImage(bImg);
//        Dimension lblSize = getSize();
//        int imgWidth = bImg.getWidth();
//        int imgHeight = bImg.getHeight();
//        logger.info("image size (" + imgWidth + ", " + imgHeight + ")");
//        logger.info("label size (" + lblSize.width + ", " + lblSize.height + ")");
//        Image img = bImg.getScaledInstance(lblSize.width, imgHeight*lblSize.width/imgWidth, Image.SCALE_SMOOTH);
//        setIconImage(img);
        
        txtPassword.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent paramKeyEvent) {
        		if(paramKeyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
        			btnConfirm.doClick();
        		}
        	}
		});
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel() {
        	@Override
        	protected void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		super.paintComponent(g);
        		ImageIcon image = new ImageIcon(getClass().getResource(
                        "/image/logbg.png"));
        		  g.drawImage(image.getImage(), 0, 0, null);
        	}
        };
        
        jPanel1.setPreferredSize(new Dimension(600, 400));
        
        jLabel1 = new javax.swing.JLabel();
        jLabel1.setBounds(318, 252, 83, 15);
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setBounds(321, 289, 83, 15);
        txtUsername = new javax.swing.JTextField();
        txtUsername.setBounds(396, 247, 160, 25);
        txtPassword = new javax.swing.JPasswordField();
        txtPassword.setBounds(396, 284, 160, 25);
        btnConfirm = new javax.swing.JButton();
        btnConfirm.setBounds(396, 333, 75, 25);
        btnReset = new javax.swing.JButton();
        btnReset.setBounds(483, 333, 75, 25);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        String title = "广东省中小学生健康体检管理系统";
		if(AppCache.getInstance().isSchoolEdition()) {
			title += "（学校版）";
		}
        setTitle(title);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel1.setText("用户名：");

        jLabel2.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        jLabel2.setText("密  码：");

        txtUsername.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N

        txtPassword.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        txtPassword.setName(""); // NOI18N

        btnConfirm.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        btnConfirm.setLabel("确定");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("宋体", 1, 12)); // NOI18N
        btnReset.setLabel("重置");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtUsername.getAccessibleContext().setAccessibleName("txtUsername");
        txtPassword.getAccessibleContext().setAccessibleName("txtPassword");
        btnConfirm.getAccessibleContext().setAccessibleName("btnConfirm");
        btnReset.getAccessibleContext().setAccessibleName("btnReset");
        getContentPane().setLayout(new BorderLayout(0, 0));
        jPanel1.setLayout(null);
        jPanel1.add(jLabel1);
        jPanel1.add(jLabel2);
        jPanel1.add(btnConfirm);
        jPanel1.add(btnReset);
        jPanel1.add(txtUsername);
        jPanel1.add(txtPassword);
        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        password = SecurityUtils.cryptPassword(password);
        logger.info("username:" + username);
        logger.info("password:" + password);
        if(username == null || username.length() == 0) {
            JOptionPane.showMessageDialog(this, "请输入用户名！");
            SystemUtils.abort();
        }
        if(password == null || password.length() == 0) {
            JOptionPane.showMessageDialog(this, "请输入密码！");
            SystemUtils.abort();
        }
        IUserService userService = AppContext.getBean("userService", IUserService.class);
        User user =  userService.getByName(username);
        if(user != null) {
//            logger.info("User [" + user.getUsername() + "] logined.");  
        }
        if(user == null) {
           if("admin".equals(username)) {
               user = new User();
               user.setUsername("admin");
               user.setPassword(SecurityUtils.cryptPassword("admin")); 
               user.setUserType(1);
               userService.save(user);
            }
            else {
                JOptionPane.showMessageDialog(this, "用户不存在，或者密码不正确！");
                SystemUtils.abort();
            } 
        }
        
        if(!password.equals(user.getPassword())) {
            JOptionPane.showMessageDialog(this, "用户不存在，或者密码不正确！");
            SystemUtils.abort();
        }
        logger.info("user " + user.getUsername() + " login success.");
        try {
        	GlobalVariables.setCurrentUser(username);
            MainUI mainUI = new MainUI();
            this.setVisible(false);
            mainUI.setVisible(true);
        } 
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
//        logger.info("current dir: " + System.getProperty("user.dir"));
        txtUsername.setText("");
        txtPassword.setText("");
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    	logger.info("appHome is: " + System.getProperty("appHome"));
		AppCache.initAppCacheNoDb();
	    UIManagerHelper.setLookAndFeel(UIManagerHelper.getLafClassName(AppCache.getInstance().lookAndFeel()));
        //</editor-fold>
//        try {
//            System.out.println(SecurityUtils.verifySignature("".getBytes(), null));
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(LoginUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    	
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginUI ui = new LoginUI();  
                    ScreenUtils.center(ui);
                    ui.setVisible(true);
                    ui.startUpdater();
                }
                catch(RuntimeException ex) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    JOptionPane.showMessageDialog(null, sw.toString());
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnReset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
	
	@Override
	public boolean requestRestart() {
		logger.info("requestRestart");
		return true;
	}

	@Override
	public void receiveMessage(String message) {
		logger.info("receiveMessage " + message);
	}
}