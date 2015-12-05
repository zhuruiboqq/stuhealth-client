/**
 * 
 */
package com.vastcm.stuhealth.client;

import importfirebird.FindFile;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.framework.ui.UI;
import com.vastcm.stuhealth.client.framework.ui.UIContext;
import com.vastcm.stuhealth.client.framework.ui.UIFactory;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIType;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.swing.dialog.longtime.ILongTimeTask;
import com.vastcm.swing.dialog.longtime.LongTimeProcessDialog;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Sep 12, 2013
 */
public class Restore2Init {
	Logger logger = LoggerFactory.getLogger(Restore2Init.class);
	
	public void startRestore() {
		int confirm = JOptionPane.showConfirmDialog(null, "此操作会删除所有现有数据!请先进行备份数据！", "提示", JOptionPane.YES_NO_OPTION);
		if(confirm != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			UIContext ctx = new UIContext();
			ctx.set(UIContext.UI_TITLE, "备份与还原");
			ctx.set(UIContext.UI_CAN_CLOSE, false);
			UI ui = UIFactory.create(DatabaseBackupRecover.class, UIType.MODAL, ctx, null);
			((DatabaseBackupRecover)ui.getUIObject()).setRecoveryVisible(false);
			ui.setVisible(true);
		} catch (Exception ex) {
			ExceptionUtils.writeExceptionLog(logger, ex);
		}
		LongTimeProcessDialog longTimeDialog = new LongTimeProcessDialog(UIFrameworkUtils.getMainUI());
 		longTimeDialog.setLongTimeTask(new ILongTimeTask() {
 			
 			@Override
 			public Object exec() throws Exception {
 					logger.info("Start to restore...");
 	 				runTask();
 	 				logger.info("Finish restore.");
 	 				JOptionPane.showMessageDialog(null, "系统数据初始化成功！");
 	 				return "success";
 			}
 			
 			@Override
 			public void afterExec(Object paramObject) throws Exception {
 				if(paramObject.equals("success")) {
 					JOptionPane.showMessageDialog(null, "系统数据初始化成功！");
 				}
 			}
 		});
 		longTimeDialog.setTitle("正在做系统数据初始化。。。");
 		longTimeDialog.show();
	}
	
	private void runTask() {
		
			DataSource ds = AppContext.getBean("dataSource", DataSource.class);
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = ds.getConnection();
				stmt = conn.createStatement();
				stmt.execute("truncate table student");
				stmt.execute("truncate table hw_school");
				stmt.execute("truncate table schooltree");
				stmt.execute("truncate table result");
				stmt.execute("truncate table report_tb_age");
				stmt.execute("truncate table report_tb_grade");
				stmt.execute("truncate table report_zb_age");
				stmt.execute("truncate table report_zb_grade");
			}
			catch(SQLException e) {
				logger.error("restore init error", e);
			}
			finally {
				try {
					stmt.close();
					conn.close();
				} 
				catch (SQLException e) {
					logger.error("close connection error.", e);
				}
			}
	}
}
