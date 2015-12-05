/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vastcm.stuhealth.client.entity.CheckResult;
import com.vastcm.stuhealth.client.entity.RptTbAge;
import com.vastcm.stuhealth.client.entity.RptTbGrade;
import com.vastcm.stuhealth.client.entity.RptZbAge;
import com.vastcm.stuhealth.client.entity.RptZbGrade;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.Student;
import com.vastcm.stuhealth.client.entity.UploadLog;
import com.vastcm.stuhealth.client.entity.core.CoreEntity;
import com.vastcm.stuhealth.client.entity.service.ICheckResultService;
import com.vastcm.stuhealth.client.entity.service.IRptTbAgeService;
import com.vastcm.stuhealth.client.entity.service.IRptTbGradeService;
import com.vastcm.stuhealth.client.entity.service.IRptZbAgeService;
import com.vastcm.stuhealth.client.entity.service.IRptZbGradeService;
import com.vastcm.stuhealth.client.entity.service.ISchoolTreeNodeService;
import com.vastcm.stuhealth.client.entity.service.IStudentService;
import com.vastcm.stuhealth.client.entity.service.IUploadLogService;
import com.vastcm.stuhealth.client.framework.Request;
import com.vastcm.stuhealth.client.framework.exception.ConnectionException;
import com.vastcm.stuhealth.client.framework.exception.RequestException;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;
import com.vastcm.stuhealth.client.report.ui.FilterCommonAction;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.client.utils.FileIOUtil;
import com.vastcm.stuhealth.client.utils.biz.DownloadUploadHelper;
import com.vastcm.stuhealth.client.utils.biz.SchoolTermItem4UI;
import com.vastcm.stuhealth.framework.Response;
import com.vastcm.stuhealth.framework.ResponseContent;
import com.vastcm.stuhealth.framework.ResponseUtils;
import com.vastcm.swing.selector.JSelectorBox;
import com.vastcm.swing.selector.event.PreChangeEvent;
import com.vastcm.swing.selector.event.PreChangeListener;

/**
 * 
 * @author bob
 */
public class DownloadPanel extends KernelUI {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(DownloadPanel.class);
	private ProgressPanel progressPanel;
	private JComboBox cmbSchoolTerm;
	private javax.swing.JLabel lblUser;
	private javax.swing.JLabel lblPassword;
	private javax.swing.JLabel lblSchool;
	private javax.swing.JPanel centerPanel;
	private javax.swing.JPasswordField txtPassword;
	private JSelectorBox selSchoolRange;
	private javax.swing.JTextField txtUser;
	private Panel panel;

	public DownloadPanel() {
		initComponents();
	}

	private void initComponents() {
		centerPanel = new javax.swing.JPanel();
		//不允许显示班级，会影响过滤条件

		setPreferredSize(new Dimension(500, 180));
		setLayout(new java.awt.BorderLayout());
		centerPanel.setLayout(new MigLayout("", "[40px][200px][40px][200px]", "[25px][25px][23px]"));

		lblUser = new javax.swing.JLabel("用户名");
		txtUser = new javax.swing.JTextField();
		centerPanel.add(lblUser, "cell 0 0,grow");
		centerPanel.add(txtUser, "cell 1 0,grow");

		lblPassword = new javax.swing.JLabel("密码");
		txtPassword = new javax.swing.JPasswordField();
		centerPanel.add(lblPassword, "cell 2 0 ,alignx left,growy");
		centerPanel.add(txtPassword, "cell 3 0,grow");
		//		txtUser.setText("admin");
		//		txtPassword.setText("admin");

		lblSchool = new javax.swing.JLabel("学校");
		selSchoolRange = FilterCommonAction.createSchoolRange();
		SchoolTreeSelectorPopupUI selectorPopupUI = (SchoolTreeSelectorPopupUI) selSchoolRange.getSelectorPopupUI();
		selectorPopupUI.setToolbarVisible(false);
		selectorPopupUI.setMergedSchoolVisible(false);
		selectorPopupUI.setIsDisplayClass(false);
		selectorPopupUI.displayUploadMsg();
		selectorPopupUI.displaySecCodeMsg();

		selSchoolRange.addPreChangeListener(new PreChangeListener() {

			@Override
			public void preChange(PreChangeEvent paramPreChangeEvent) {

				Object value = paramPreChangeEvent.getData();
				if (value == null)
					return;

				SchoolTreeNode schoolNode;
				if (value instanceof DefaultMutableTreeNode) {
					schoolNode = (SchoolTreeNode) ((DefaultMutableTreeNode) value).getUserObject();
				} else {
					schoolNode = (SchoolTreeNode) value;
				}
				if (schoolNode.getType() != SchoolTreeNode.TYPE_SCHOOL) {
					JOptionPane.showMessageDialog(DownloadPanel.this, "请选择学校，不允许选择市或区！");
					return;
				}
				if (schoolNode.getName().indexOf("验证文件已过期") != -1) {
					JOptionPane.showMessageDialog(DownloadPanel.this, "该学校的验证文件已过期，请重新导入！");
					paramPreChangeEvent.setResult(PreChangeEvent.E_FAIL);
				}
			}
		});
		centerPanel.add(lblSchool, "cell 0 1,grow");
		centerPanel.add(selSchoolRange, "cell 1 1,grow");

		JLabel lblSchoolTerm = new JLabel("学年");
		cmbSchoolTerm = FilterCommonAction.createSchoolTrem(Calendar.getInstance().get(Calendar.YEAR));
		centerPanel.add(lblSchoolTerm, "cell 2 1,grow");
		centerPanel.add(cmbSchoolTerm, "cell 3 1,grow");

		panel = new Panel();
		centerPanel.add(panel, "cell 0 2 4 1,alignx center");

		JButton btnDownload = new JButton("下  载");
		panel.add(btnDownload);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnDownload_actionPerformed(e);
				} catch (Exception exc) {
					UIHandler.handleException(DownloadPanel.this, logger, exc);
				}
			}
		});

		add(centerPanel, java.awt.BorderLayout.CENTER);
	}

	protected void btnDownload_actionPerformed(ActionEvent e) throws Exception {
		final String username = txtUser.getText().trim();
		if (username == null || username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "请输入 用户名！");
			return;
		}
		char[] password = txtPassword.getPassword();
		if (password == null || password.length == 0) {
			JOptionPane.showMessageDialog(this, "请输入 密码！");
			return;
		}
		Object value = selSchoolRange.getValue();
		SchoolTreeNode schoolNode;
		if (value instanceof DefaultMutableTreeNode) {
			schoolNode = (SchoolTreeNode) ((DefaultMutableTreeNode) value).getUserObject();
		} else {
			schoolNode = (SchoolTreeNode) value;
		}

		if (schoolNode == null) {
			JOptionPane.showMessageDialog(this, "请选择学校！");
			return;
		}
		if (schoolNode.getType() != SchoolTreeNode.TYPE_SCHOOL) {
			JOptionPane.showMessageDialog(this, "请选择学校，不允许选择市或区！");
			return;
		}
		final String schoolBh = schoolNode.getCode();
		SchoolTermItem4UI schoolTermItem = (SchoolTermItem4UI) cmbSchoolTerm.getSelectedItem();
		final String year = String.valueOf(schoolTermItem.getYear());
		final String term = String.valueOf(schoolTermItem.getTerm());
		DownloadUploadHelper.checkAuth(this, username, password, schoolBh);
		//            final String sessionId = DownloadUploadHelper.checkAuth(this, username, password, schoolBh);

		if (!DownloadUploadHelper.isCheckResultExistence(schoolBh, year)) {
			JOptionPane.showMessageDialog(this, "该校" + year + "学年没有上传数据，无法从服务器上下载！");
			return;
		} else {
			int confirm = JOptionPane.showConfirmDialog(this, "该校本学年已上传过数据，请确定是否下载？", "确认", JOptionPane.YES_NO_OPTION);
			if (confirm != JOptionPane.OK_OPTION) {
				return;
			}
		}

		progressPanel = new ProgressPanel(null);
		progressPanel.getProgressBar().setValue(10);
		progressPanel.getLblDescription().setText("连接服务器下载数据...");
		progressPanel.setProgressKeeper(new Runnable() {

			@Override
			public void run() {
				try {
					Request reqAuth = new Request("/DownloadBySchool");
					reqAuth.setParam("schoolBh", schoolBh);
					reqAuth.setParam("year", year);
					reqAuth.setParam("term", term);
					Response respAuth = reqAuth.send();
					if (respAuth.getRetCode() != ResponseUtils.RETCODE_OK) {
						String msg = "ERROR [" + respAuth.getRetCode() + "]-" + respAuth.getRetMsg();
						logger.error(msg);
						JOptionPane.showMessageDialog(DownloadPanel.this, msg);
						return;
					}
					ResponseContent exContent = respAuth.getExContent();
					updateProgressPanel(50, "从服务器下载数据，完成！");

					final String msg = saveLocalRecord(schoolBh, year, term, exContent);

					writeUploadLog(year, term, schoolBh, username, new Date());
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							progressPanel.dispose();
							JOptionPane.showMessageDialog(DownloadPanel.this, msg);
						}
					});
				} catch (Exception e) {
					ExceptionUtils.writeExceptionLog(logger, e);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(DownloadPanel.this, "数据下载失败！");
							progressPanel.dispose();
						}
					});

				}
				return;
			}

			private String saveLocalRecord(String schoolBh, String year, String term, ResponseContent exContent) {
				//				try {
				//					FileIOUtil.saveFile("D:/SchoolTreeNode.txt", exContent.get("SchoolTreeNode"));
				//					FileIOUtil.saveFile("D:/Student.txt", exContent.get("Student"));
				//					FileIOUtil.saveFile("D:/CheckResult.txt", exContent.get("CheckResult"));
				//					FileIOUtil.saveFile("D:/RptTbAge.txt", exContent.get("RptTbAge"));
				//					FileIOUtil.saveFile("D:/RptTbGrade.txt", exContent.get("RptTbGrade"));
				//					FileIOUtil.saveFile("D:/RptZbAge.txt", exContent.get("RptZbAge"));
				//					FileIOUtil.saveFile("D:/RptZbGrade.txt", exContent.get("RptZbGrade"));
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				}
				Gson gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				Type typeOfT = null;
				Map<String, String> classNoMap = new HashMap<String, String>();
				typeOfT = new TypeToken<List<SchoolTreeNode>>() {
				}.getType();
				List<SchoolTreeNode> schoolTreeNodeList = gsonBuilder.fromJson(exContent.get("SchoolTreeNode"), typeOfT);
				//					List<SchoolTreeNode> schoolTreeNodeList = getListFromJson(exContent, "SchoolTreeNode", new SchoolTreeNode());
				typeOfT = new TypeToken<List<Student>>() {
				}.getType();
				List<Student> studentList = gsonBuilder.fromJson(exContent.get("Student"), typeOfT);
				//					List<Student> studentList = getListFromJson(exContent, "Student", new Student());

				deleteLocalRecord(schoolBh, year, term, schoolTreeNodeList, studentList);
				updateProgressPanel(55, "删除本地数据，完成！");

				for (SchoolTreeNode node : schoolTreeNodeList) {
					classNoMap.put(node.getCode(), node.getLongNumber());
				}
				ISchoolTreeNodeService schoolTreeNodeService = AppContext.getBean("schoolTreeNodeService", ISchoolTreeNodeService.class);
				schoolTreeNodeService.save(schoolTreeNodeList);
				int schoolTreeNodeCount = schoolTreeNodeList.size();
				schoolTreeNodeList = null;
				updateProgressPanel(60, "保存学校班级，完成！");

				IStudentService studentService = AppContext.getBean("studentService", IStudentService.class);
				studentService.save(studentList);
				int studentCount = studentList.size();
				studentList = null;
				updateProgressPanel(65, "保存学生，完成！");

				typeOfT = new TypeToken<List<CheckResult>>() {
				}.getType();
				List<CheckResult> checkResultList = gsonBuilder.fromJson(exContent.get("CheckResult"), typeOfT);
				//					List<CheckResult> checkResultList = getListFromJson(exContent, "CheckResult", new CheckResult());
				for (CheckResult result : checkResultList) {
					result.setTjnd(year);
					result.setClassLongNo(classNoMap.get(result.getClassBh()));
					result.setStudentCode(result.getXh());
				}
				ICheckResultService chkSrv = AppContext.getBean("checkResultService", ICheckResultService.class);
				chkSrv.save(checkResultList);
				//				chkSrv.save(checkResultList, year);
				int checkResultCount = checkResultList.size();
				checkResultList = null;
				updateProgressPanel(80, "保存体检结果，完成！");

				typeOfT = new TypeToken<List<RptTbAge>>() {
				}.getType();
				List<RptTbAge> rptTbAgeList = gsonBuilder.fromJson(exContent.get("RptTbAge"), typeOfT);
				//					List<RptTbAge> rptTbAgeList = getListFromJson(exContent, "RptTbAge", new RptTbAge());
				IRptTbAgeService tbAgeSrv = AppContext.getBean("rptTbAgeService", IRptTbAgeService.class);
				tbAgeSrv.save(rptTbAgeList);
				int rptTbAgeCount = rptTbAgeList.size();
				rptTbAgeList = null;
				updateProgressPanel(85, "保存统计表1，完成！");

				typeOfT = new TypeToken<List<RptTbGrade>>() {
				}.getType();
				List<RptTbGrade> rptTbGradeList = gsonBuilder.fromJson(exContent.get("RptTbGrade"), typeOfT);
				//					List<RptTbGrade> rptTbGradeList = getListFromJson(exContent, "RptTbGrade", new RptTbGrade());
				IRptTbGradeService tbGradeSrv = AppContext.getBean("rptTbGradeService", IRptTbGradeService.class);
				tbGradeSrv.save(rptTbGradeList);
				int rptTbGradeCount = rptTbGradeList.size();
				rptTbGradeList = null;
				updateProgressPanel(90, "保存统计表2，完成！");

				typeOfT = new TypeToken<List<RptZbAge>>() {
				}.getType();
				List<RptZbAge> rptZbAgeList = gsonBuilder.fromJson(exContent.get("RptZbAge"), typeOfT);
				//					List<RptZbAge> rptZbAgeList = getListFromJson(exContent, "RptZbAge", new RptZbAge());
				IRptZbAgeService zbAgeSrv = AppContext.getBean("rptZbAgeService", IRptZbAgeService.class);
				zbAgeSrv.save(rptZbAgeList);
				int rptZbAgeCount = rptZbAgeList.size();
				rptZbAgeList = null;
				updateProgressPanel(95, "保存统计表3，完成！");

				typeOfT = new TypeToken<List<RptZbGrade>>() {
				}.getType();
				List<RptZbGrade> rptZbGradeList = gsonBuilder.fromJson(exContent.get("RptZbGrade"), typeOfT);
				//					List<RptZbGrade> rptZbGradeList = getListFromJson(exContent, "RptZbGrade", new RptZbGrade());
				IRptZbGradeService zbGradeService = AppContext.getBean("rptZbGradeService", IRptZbGradeService.class);
				zbGradeService.save(rptZbGradeList);
				int rptZbGradeCount = rptZbGradeList.size();
				rptZbGradeList = null;
				updateProgressPanel(100, "保存统计表4，完成！");

				StringBuilder msg = new StringBuilder();
				msg.append("成功下载 ");
				msg.append(schoolTreeNodeCount).append(" 条学校班级记录； ");
				msg.append(studentCount).append(" 条学生记录； ");
				msg.append(checkResultCount).append(" 条体检结果记录； ");
				msg.append(rptTbAgeCount + rptTbGradeCount + rptZbAgeCount + rptZbGradeCount).append(" 条统计记录！");

				return msg.toString();
			}

			private void updateProgressPanel(final int progress, final String message) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						logger.info(message);
						progressPanel.getProgressBar().setValue(progress);
						progressPanel.getLblDescription().setText(message);
					}
				});
			}
		});

		progressPanel.setVisible(true);
	}

	protected void deleteLocalRecord(String schoolBh, String year, String term, List<SchoolTreeNode> schoolTreeNodeList, List<Student> studentList) {
		//删除学校和学生
		int deletedRowCount;
		StringBuilder whereSql = new StringBuilder();
		whereSql.append("where longNumber like '%").append(schoolBh).append("%'");
		if (schoolTreeNodeList != null && schoolTreeNodeList.size() > 0) {
			ISchoolTreeNodeService schoolTreeNodeService = AppContext.getBean("schoolTreeNodeService", ISchoolTreeNodeService.class);
			//		schoolTreeNodeService.removeByCode(schoolBh);
			deletedRowCount = schoolTreeNodeService.removeBySql(whereSql.toString());
			logger.info("Deleted schoolTreeNodeService " + deletedRowCount + " Rows.");
		}

		whereSql.setLength(0);
		whereSql.append("where (status is null or status = 'T') and schoolNo = '").append(schoolBh).append("'");
		if (studentList != null && studentList.size() > 0) {
			IStudentService studentService = AppContext.getBean("studentService", IStudentService.class);
			deletedRowCount = studentService.removeBySql(whereSql.toString());
			logger.info("Deleted studentService " + deletedRowCount + " Rows.");
		}

		whereSql.setLength(0);
		whereSql.append(" WHERE schoolBh = '").append(schoolBh).append("' ");
		whereSql.append(" AND   tjnd = '").append(year).append("' ");
		whereSql.append(" AND   term = '").append(term).append("' \n");
		ICheckResultService chkSrv = AppContext.getBean("checkResultService", ICheckResultService.class);
		deletedRowCount = chkSrv.removeBySql(whereSql.toString());
		logger.info("Deleted checkResult " + deletedRowCount + " Rows.");

		//重用上面的whereSql
		IRptTbAgeService tbAgeSrv = AppContext.getBean("rptTbAgeService", IRptTbAgeService.class);
		deletedRowCount = tbAgeSrv.removeBySql(whereSql.toString());
		logger.info("Deleted rptTbAgeService " + deletedRowCount + " Rows.");

		IRptTbGradeService tbGradeSrv = AppContext.getBean("rptTbGradeService", IRptTbGradeService.class);
		deletedRowCount = tbGradeSrv.removeBySql(whereSql.toString());
		logger.info("Deleted rptTbGradeService " + deletedRowCount + " Rows.");

		IRptZbAgeService zbAgeSrv = AppContext.getBean("rptZbAgeService", IRptZbAgeService.class);
		deletedRowCount = zbAgeSrv.removeBySql(whereSql.toString());
		logger.info("Deleted rptZbAgeService " + deletedRowCount + " Rows.");

		IRptZbGradeService zbGradeService = AppContext.getBean("rptZbGradeService", IRptZbGradeService.class);
		deletedRowCount = zbGradeService.removeBySql(whereSql.toString());
		logger.info("Deleted rptZbGradeService " + deletedRowCount + " Rows.");
	}

	protected void writeUploadLog(String year, String term, String schoolCode, String user, Date date) {
		IUploadLogService uploadLogSrv = AppContext.getBean("uploadLogService", IUploadLogService.class);
		UploadLog log = new UploadLog();
		log.setYear(year);
		log.setTerm(term);
		log.setSchoolCode(schoolCode);
		log.setUploadUser(user);
		log.setUploadDate(new java.sql.Date(date.getTime()));
		uploadLogSrv.save(log);
	}

	public int uploadData(List<? extends CoreEntity> dataLs, String url, final String message) throws ConnectionException, RequestException {
		String json = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(dataLs);
		try {
			FileIOUtil.saveFile("D:" + url + ".txt", json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int count = dataLs.size();
		return count;
	}
}