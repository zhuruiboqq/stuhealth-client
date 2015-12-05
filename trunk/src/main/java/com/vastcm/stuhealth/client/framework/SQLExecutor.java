/**
 * 
 */
package com.vastcm.stuhealth.client.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vastcm.stuhealth.client.AppContext;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jul 24, 2013
 */
public class SQLExecutor {
	
	private Logger logger = LoggerFactory.getLogger(SQLExecutor.class);
	private File sqlDir_to_update = null;
	private File sqlDir_updated   = null;
	
	public SQLExecutor() {
		sqlDir_to_update = new File(System.getProperty("appHome") + File.separator + "sql/to_update");
		logger.info("sqlDir_to_update=" + sqlDir_to_update.getAbsolutePath());
		sqlDir_updated   = new File(System.getProperty("appHome") + File.separator + "sql/updated");
		if(!sqlDir_to_update.exists()) {
			sqlDir_to_update.mkdirs();
		}
		if(!sqlDir_updated.exists()) {
			sqlDir_updated.mkdirs();
		}
	}
	
	public void executeSQL() {
		logger.info("Start executing update SQL...");
		File[] fs = sqlDir_to_update.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".sql")) {
					return true;
				}
				return false;
			}
		});
		if(fs.length == 0) {
			logger.info("No SQL file to update.");
			return;
		}
		DataSource ds = AppContext.getBean("dataSource", DataSource.class);
		Connection conn = null;
		Statement  stmt = null;
		String sql = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			for(File f : fs) {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
					while((sql = br.readLine()) != null) {
						logger.info("sql:  " + sql);
						if(sql == null || sql.trim().startsWith("--")) {
							continue;
						}
						stmt.addBatch(sql);
					}
					br.close();
					stmt.executeBatch();
					f.renameTo(new File(sqlDir_updated, f.getName()));
					f.delete();
				}
				catch(Exception e) {
					ExceptionUtils.writeExceptionLog(logger, e);
					continue;
				}
			}
			
		}
		catch (SQLException e) {
			ExceptionUtils.writeExceptionLog(logger, e);
			return;
		}
		finally {
			try {
				stmt.close();
				conn.close();
			} 
			catch (SQLException e) {
				ExceptionUtils.writeExceptionLog(logger, e);
			}
		}
		logger.info("Update SQL Finished...");
	}
	
	
}
