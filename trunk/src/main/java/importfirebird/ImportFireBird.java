/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfirebird;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.DatabaseBackupRecover;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.swing.dialog.longtime.ILongTimeTask;
import com.vastcm.swing.dialog.longtime.LongTimeProcessDialog;
import com.vastcm.swing.dialog.longtime.LongTimeTaskAdapter;
import com.vastcm.swing.dialog.longtime.LongTimeTaskDialog;

/**
 * 
 * @author Administrator
 */
public class ImportFireBird {
	private static Logger logger = LoggerFactory.getLogger(ImportFireBird.class);
	//    private static JDialog dialog;
	public final static JProgressBar jpb = new JProgressBar();
	public static JFrame frm;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		//    	start();
	}

	public static String getOldDataFile() {
		String oldDataFile = null;
		final StringBuilder oldDataFilePath = new StringBuilder("");
		LongTimeProcessDialog longTimeDialog = new LongTimeProcessDialog(UIFrameworkUtils.getMainUI());
		longTimeDialog.setLongTimeTask(new ILongTimeTask() {

			@Override
			public Object exec() throws Exception {
				logger.info("Start to find old data...");
				String path = FindFile.GetOldData();
				if (path != null) {
					oldDataFilePath.append(path);
				}

				logger.info("Finish finding old data.");
				return "success";
			}

			@Override
			public void afterExec(Object paramObject) throws Exception {

			}
		});
		longTimeDialog.setTitle("正在查找历史数据。。。");
		longTimeDialog.show();
		oldDataFile = oldDataFilePath.toString();
		logger.info("old data file=" + oldDataFile);
		if (oldDataFile == null || oldDataFile.trim().isEmpty()) {
			//如果不存在浏览原系统文件 如C://HealthData.FDB
			//            String FilePath="";//等于浏览到的文件路径
			//            oldDataFile=FilePath;
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			File f = fc.getSelectedFile();
			if (f != null) {
				logger.info("File " + f + " is selected.");
				oldDataFile = f.getAbsolutePath();
			}

		} else {
			oldDataFile = oldDataFile.replace("\\", "/");
		}

		return oldDataFile;
	}

	public static void afterImport() {
		LongTimeTaskDialog dialog = LongTimeTaskDialog.getInstance("更新体检结果表", new LongTimeTaskAdapter() {
			@Override
			public Object exec() throws Exception {
				try {
					//更新体检表的评价项目和统计报表
					ICheckResultService checkResultService = AppContext.getBean("checkResultService", ICheckResultService.class);
					String sqlFilter = " where 1=1 ";
					Map<String, Object> params = new HashMap<String, Object>();
					checkResultService.updateEvaluation(sqlFilter, params);
					checkResultService.recalculateStatRptByResult(sqlFilter, params);
				} catch (Exception ee) {
					UIHandler.handleException(UIFrameworkUtils.getMainUI(), logger, ee);
				}
				return null;
			}
		});
		dialog.show();

		UIFrameworkUtils.getMainUI().removeOldDataImportMenu();
		File pfile = new File(System.getProperty("appHome") + File.separator + "config/oldData.properties");
		logger.info("file path: " + pfile.getAbsolutePath());
		try {
			if (!pfile.exists()) {
				pfile.createNewFile();
			}
			Properties prop = new Properties();
			prop.setProperty("isImported", "true");
			prop.store(new FileOutputStream(pfile), "");
			logger.info("tag old data import success.");
			JOptionPane.showMessageDialog(null, "导入数据成功！");
		} catch (IOException e) {
			logger.error("Write old data imported tag failed.");
			ExceptionUtils.writeExceptionLog(logger, e);
		}
	}

	public static void startImport(String filePath) {
		try {
			ImportCore.DataPath = filePath;
			ImportCore.Count();//统计总数量
			//            ShowBar();
			ImportCore.Import();
			afterImport();
			frm.setVisible(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			ExceptionUtils.writeExceptionLog(logger, e);
		}
	}

	public static void ShowBar() {
		frm = new JFrame();
		//    	dialog.setModal(true);
		frm.setResizable(false);
		Container contentPane = frm.getContentPane();
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		jpb.setOrientation(JProgressBar.HORIZONTAL);
		jpb.setSize(100, 100);
		logger.info("ImportCore.allcount=" + ImportCore.allcount);
		jpb.setMaximum(ImportCore.allcount);
		jpb.setMinimum(0);
		jpb.setValue(0);
		jpb.setStringPainted(true);
		jpb.setPreferredSize(new Dimension(400, 20));
		contentPane.add(jpb, BorderLayout.EAST);

		//        final Timer timer = new Timer();  
		//        timer.schedule(new mytask(jpb), 100, 1000); 

		//        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frm.pack();
		frm.setTitle("旧版客户端数据导入");
		// 窗口居中  
		frm.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frm.getSize().width) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - frm.getSize().height) / 2);
		frm.setVisible(true);

	}

}
