/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.entity.ServerSetting;
import com.vastcm.stuhealth.client.entity.service.IServerSettingService;
import com.vastcm.stuhealth.client.framework.exception.ConnectionException;
import com.vastcm.stuhealth.client.framework.exception.RequestException;
import com.vastcm.stuhealth.framework.Response;
import com.vastcm.stuhealth.framework.ResponseUtils;
import com.vastcm.stuhealth.framework.SessionUtils;

/**
 * 
 * @author House
 */
public class Request {
	private static Logger logger = LoggerFactory.getLogger(Request.class);
	private static HttpClient hc = new DefaultHttpClient();
	private HttpPost req;
	private Map<String, String> params = new LinkedHashMap<String, String>();
	private String serverIP;
	private String serverPort;

	static {
		logger.info("starting http connection cleaner.");
		startConnectionCleaner();
		logger.info("start http connection cleaner successfully.");
	}

	public Request(String api) {
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000 * 60);
		req = new HttpPost("http://" + getServerIP() + ":" + getServerPort() + "/stuhealth" + api);
		//        
		Serializable sessionId = SessionClientUtils.getSessionId();
		if (sessionId != null) {
			setParam(SessionUtils.SESSION_ID, sessionId.toString());
		}
	}

	public static void startConnectionCleaner() {
		Runnable cleanTask = new Runnable() {
			public void run() {
				synchronized (hc) {
					long start = System.currentTimeMillis();
					hc.getConnectionManager().closeExpiredConnections();
					long elapse = System.currentTimeMillis() - start;
					if (elapse > 0)
						logger.info("http closeExpiredConnections: " + elapse + "ms.");
				}
			}
		};
		ScheduledExecutorService execServ = Executors.newScheduledThreadPool(1);
		execServ.scheduleWithFixedDelay(cleanTask, 3, 3, TimeUnit.SECONDS);
	}

	public IServerSettingService getServerSettingService() {
		return AppContext.getBean("serverSettingService", IServerSettingService.class);
	}

	public void setParam(String key, String value) {
		params.put(key, value);
	}

	public String getParam(String key) {
		return params.get(key);
	}

	private List<NameValuePair> getParamList() {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			paramList.add(new BasicNameValuePair(key, params.get(key)));
		}
		return paramList;
	}

	public void download(String path) throws RequestException, ConnectionException {
		File dir = new File(path);
		HttpResponse rs = null;
		try {
			req.setEntity(new UrlEncodedFormEntity(getParamList(), "UTF-8"));
			for (NameValuePair p : getParamList()) {
				req.getParams().setParameter(p.getName(), p.getValue());
				logger.info("param " + p.getName() + "=" + p.getValue());
			}
			rs = hc.execute(req);
			int reqStatus = rs.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != reqStatus) {
				throw new RequestException(reqStatus + " " + rs.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = rs.getEntity();
			if (entity != null) {
				String filename = getParam("file");
				logger.info("File " + filename + " size: " + entity.getContentLength());
				InputStream in = new BufferedInputStream(entity.getContent());
				File oFile = new File(dir, filename);
				OutputStream out = new FileOutputStream(oFile);
				byte[] b = new byte[1024];
				int i;
				int bufferSize = 0;
				long startTime = System.currentTimeMillis();
				while ((i = in.read(b)) != -1) {
					out.write(b, 0, i);
					if (bufferSize++ >= 1024) {
						out.flush();
						bufferSize = 0;
					}
				}
				out.close();
				long endTime = System.currentTimeMillis();
				logger.info("File " + filename + " downloaded used: " + (endTime - startTime) + "ms.");

			}
		} catch (Exception ex) {
			if (ex instanceof RuntimeException) {
				throw (RuntimeException) ex;
			}
			if (ex instanceof RequestException) {
				throw (RequestException) ex;
			}
			throw new ConnectionException("Connection ERROR!", ex);
		} finally {
			try {
				EntityUtils.consume(rs.getEntity());
			} catch (IOException e) {
				logger.error("consume response entity error.", e);
			}
		}
	}

	public Response send() throws ConnectionException, RequestException {
		Response res = null;
		HttpResponse rs = null;
		int reqStatus;
		try {
			req.setEntity(new UrlEncodedFormEntity(getParamList(), "UTF-8"));
			logger.info("Sending request to : " + req.getURI());
			for (NameValuePair p : getParamList()) {
				req.getParams().setParameter(p.getName(), p.getValue());
				logger.info("param " + p.getName() + "=" + p.getValue());
			}
			rs = hc.execute(req);
			reqStatus = rs.getStatusLine().getStatusCode();
			if (200 != reqStatus) {
				throw new RequestException(reqStatus + " " + rs.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = rs.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				InputStreamReader inputReader=new InputStreamReader(in,"UTF-8");
				StringBuilder sb = new StringBuilder();
				char[] b = new char[1024];
				for (int bytesRead = 0; (bytesRead = inputReader.read(b)) != -1;) {
					sb.append(b,0,bytesRead);
				}
				String json = sb.toString();
				res = ResponseUtils.fromJson(json, Response.class);
				if (ResponseUtils.RETCODE_OK == res.getRetCode() && res.getSessionId() != null) {
					SessionClientUtils.setSessionId(res.getSessionId());
				}
				if (ResponseUtils.RETCODE_ERROR == res.getRetCode()) {
					throw new RequestException(res.getRetMsg());
				}
			}
		} catch (Exception ex) {
			if (ex instanceof RuntimeException) {
				throw (RuntimeException) ex;
			}
			if (ex instanceof RequestException) {
				throw (RequestException) ex;
			}
			throw new RequestException("Request ERROR!", ex);
		} finally {
			try {
				EntityUtils.consume(rs.getEntity());
			} catch (IOException e) {
				logger.error("consume response entity error.", e);
			}
		}
		if (res == null) {
			res = new Response();
			res.setRetCode(ResponseUtils.RETCODE_ERROR);
			res.setRetMsg("Unknown Reason. Can't get response from server.");
		}
		return res;
	}

	public void testConnection() throws IOException {
		InetAddress ad = InetAddress.getByName(getServerIP());
		Socket s = new Socket();
		s.connect(new InetSocketAddress(ad, Integer.valueOf(getServerPort())), 3000);
		s.close();
		logger.info("Success to Connect to Server " + getServerIP());
	}

	public String getServerIP() {
		if (serverIP == null) {
			List<ServerSetting> serverSettingLs = getServerSettingService().getAll();
			if (serverSettingLs != null && !serverSettingLs.isEmpty()) {
				ServerSetting setting = serverSettingLs.get(0);
				serverIP = setting.getServerIp();
				serverPort = setting.getServerPort();
			}
		}
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getServerPort() {
		if (serverPort == null) {
			List<ServerSetting> serverSettingLs = getServerSettingService().getAll();
			if (serverSettingLs != null && !serverSettingLs.isEmpty()) {
				ServerSetting setting = serverSettingLs.get(0);
				serverIP = setting.getServerIp();
				serverPort = setting.getServerPort();
			}
		}
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

}
