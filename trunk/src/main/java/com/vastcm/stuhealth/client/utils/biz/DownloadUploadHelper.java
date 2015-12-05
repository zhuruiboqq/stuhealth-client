package com.vastcm.stuhealth.client.utils.biz;

import javax.swing.JOptionPane;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.School;
import com.vastcm.stuhealth.client.entity.service.ISchoolService;
import com.vastcm.stuhealth.client.framework.AppCache;
import com.vastcm.stuhealth.client.framework.Request;
import com.vastcm.stuhealth.client.framework.exception.ConnectionException;
import com.vastcm.stuhealth.client.framework.exception.RequestException;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.utils.SecurityUtils;
import com.vastcm.stuhealth.framework.Response;
import com.vastcm.stuhealth.framework.ResponseUtils;

public class DownloadUploadHelper {

	public static String checkAuth(KernelUI pancel, String username, char[] password, String schoolBh) throws ConnectionException, RequestException {
		School school = AppContext.getBean("schoolService", ISchoolService.class).getByCode(schoolBh);
		Request reqAuth = new Request("/auth");
		reqAuth.setParam("username", username);
		reqAuth.setParam("password", SecurityUtils.cryptPassword(String.valueOf(password)));
		reqAuth.setParam("isSchoolEdition", String.valueOf(AppCache.getInstance().isSchoolEdition()));
		reqAuth.setParam("schoolCode", schoolBh);
		reqAuth.setParam("schoolSecCode", school.getSecCode());
		Response respAuth = reqAuth.send();
		if (respAuth.getRetCode() != ResponseUtils.RETCODE_OK) {
			String msg = "ERROR [" + respAuth.getRetCode() + "]-" + respAuth.getRetMsg();
			//			logger.error(msg);
			JOptionPane.showMessageDialog(pancel, msg);
			return null;
		}
		return respAuth.getSessionId();
	}

	public static boolean isCheckResultExistence(String schoolBh, String year) throws ConnectionException, RequestException {
		Request req1 = new Request("/CheckResultExistence");
		//            req1.setParam("sessionId", sessionId);
		req1.setParam("year", year);
		req1.setParam("schoolBh", schoolBh);
		Response res1 = req1.send();
		String isExist = res1.getExContent().get("isExist");
		return isExist != null && isExist.equalsIgnoreCase("true");
	}
}
