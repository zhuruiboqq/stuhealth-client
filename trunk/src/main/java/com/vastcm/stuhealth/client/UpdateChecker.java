/**
 * 
 */
package com.vastcm.stuhealth.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vastcm.stuhealth.client.entity.BmiStandard;
import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.LungsStandard;
import com.vastcm.stuhealth.client.entity.NutritionStandard;
import com.vastcm.stuhealth.client.entity.PulsationStandard;
import com.vastcm.stuhealth.client.entity.WeightStandard;
import com.vastcm.stuhealth.client.entity.service.IBmiStandardService;
import com.vastcm.stuhealth.client.entity.service.IChestStandardService;
import com.vastcm.stuhealth.client.entity.service.IHeightStandardService;
import com.vastcm.stuhealth.client.entity.service.ILungsStandardService;
import com.vastcm.stuhealth.client.entity.service.INutritionStandardService;
import com.vastcm.stuhealth.client.entity.service.IPulsationStandardService;
import com.vastcm.stuhealth.client.entity.service.IWeightStandardService;
import com.vastcm.stuhealth.client.entity.service.core.ICoreService;
import com.vastcm.stuhealth.client.framework.Request;
import com.vastcm.stuhealth.client.framework.exception.ConnectionException;
import com.vastcm.stuhealth.client.framework.exception.RequestException;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.stuhealth.framework.Response;
import com.vastcm.stuhealth.framework.ResponseContent;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  May 31, 2013
 */
public class UpdateChecker {
	private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);
	
	private static void updateStandard(ICoreService<?> service, Type collectionType, String clientVer, String url) {
		Request req = new Request(url);
		req.setParam("version", clientVer);
		String standardName = url.substring(url.lastIndexOf("/") + 7);
		logger.info("Client " + standardName + " version is: " + clientVer);
		try {
			Response resp = req.send();
			ResponseContent respContent = resp.getExContent();
			boolean isExistUpdate = Boolean.parseBoolean(respContent.get("isExist"));
			if(isExistUpdate) {
				String serverVer = respContent.get("version");
				logger.info("Server " + standardName + " version is: " + serverVer);
				logger.info("Start to update " + standardName + "...");
				String json = respContent.get("data");
				List standards = new Gson().fromJson(json, collectionType);
				service.save(standards);
				logger.info("Update " + standardName + " version[" + serverVer + "] completed.");
			}
			else {
				logger.info("Client " + standardName + " version is update to date.");
			}
		} 
		catch (ConnectionException e) {
			logger.error("Update " + standardName +  " excpetion.");
			ExceptionUtils.writeExceptionLog(logger, e);
		} 
		catch (RequestException e) {
			logger.error("Update " + standardName +  " excpetion.");
			ExceptionUtils.writeExceptionLog(logger, e);
		}
	}
	
	public static void startItemStandardUpdateChecker() {
		Request reqTestConnection = new Request("");
		try {
			reqTestConnection.testConnection();
		} 
		catch (Exception e) {
			logger.error("连接服务器 " + reqTestConnection.getServerIP() + " 失败！ 跳过体检标准自动更新检查程序！");
			ExceptionUtils.writeExceptionLog(logger, e);
			return;
		}
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				IChestStandardService chestStandardSrv = AppContext.getBean("chestStandardService", IChestStandardService.class);
				String ver = chestStandardSrv.getNewestVersionCode();
				Type typeChest = new TypeToken<List<ChestStandard>>(){}.getType();
				updateStandard(chestStandardSrv, typeChest, ver, "/download/GetNewChestStandard");
				
				IHeightStandardService heightStandardSrv = AppContext.getBean("heightStandardService", IHeightStandardService.class);
				ver = heightStandardSrv.getNewestVersionCode();
				Type typeHeight = new TypeToken<List<HeightStandard>>(){}.getType();
				updateStandard(heightStandardSrv, typeHeight, ver, "/download/GetNewHeightStandard");
				
				IWeightStandardService weightStandardSrv = AppContext.getBean("weightStandardService", IWeightStandardService.class);
				ver = weightStandardSrv.getNewestVersionCode();
				Type typeWeight = new TypeToken<List<WeightStandard>>(){}.getType();
				updateStandard(weightStandardSrv, typeWeight, ver, "/download/GetNewWeightStandard");
				
				ILungsStandardService lungsStandardSrv = AppContext.getBean("lungsStandardService", ILungsStandardService.class);
				ver = lungsStandardSrv.getNewestVersionCode();
				Type typeLungs = new TypeToken<List<LungsStandard>>(){}.getType();
				updateStandard(lungsStandardSrv, typeLungs, ver, "/download/GetNewLungsStandard");
				
				INutritionStandardService nutritionStandardSrv = AppContext.getBean("nutritionStandardService", INutritionStandardService.class);
				ver = nutritionStandardSrv.getNewestVersionCode();
				Type typeNutrition = new TypeToken<List<NutritionStandard>>(){}.getType();
				updateStandard(nutritionStandardSrv, typeNutrition, ver, "/download/GetNewNutritionStandard");
				
				IPulsationStandardService pulsationStandardSrv = AppContext.getBean("pulsationStandardService", IPulsationStandardService.class);
				ver = pulsationStandardSrv.getNewestVersionCode();
				Type typePulsation = new TypeToken<List<PulsationStandard>>(){}.getType();
				updateStandard(pulsationStandardSrv, typePulsation, ver, "/download/GetNewPulsationStandard");
				
				IBmiStandardService bmiStandardSrv = AppContext.getBean("bmiStandardService", IBmiStandardService.class);
				ver = bmiStandardSrv.getNewestVersionCode();
				Type typeBmi = new TypeToken<List<BmiStandard>>(){}.getType();
				updateStandard(bmiStandardSrv, typeBmi, ver, "/download/GetNewBmiStandard");
				
			}
		};
		
		SwingUtilities.invokeLater(task);
	}
}
