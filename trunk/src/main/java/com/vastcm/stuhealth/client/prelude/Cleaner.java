/**
 * 
 */
package com.vastcm.stuhealth.client.prelude;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Aug 4, 2013
 */
public class Cleaner {
	
	private BufferedWriter logWriter = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public Cleaner() {
		
	}
	
	public String getCurrentTime() {
		return "[" + sdf.format(new Date()) + "] ";
	}
	
	public void start() {
		try {
			String appHome = System.getProperty("appHome");
			File logFile = new File(appHome + "/logs/prelude.log");
			logWriter = new BufferedWriter(new FileWriter(logFile));
			deleteFiles();
			logWriter.flush();
			logWriter.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * 启动时删除指定的文件。如升级第三方包后要删除旧版本的包
     * @author house
	 * @throws IOException 
     * @email  yyh2001@gmail.com
     * @since  Aug 4, 2013
     */
    private void deleteFiles() throws IOException {
    	String appHome = System.getProperty("appHome");
    	File dir_to_delete = new File(appHome + File.separator + "/to_delete");
    	logWriter.append(getCurrentTime()).append("dir_to_delete=" + dir_to_delete.getAbsolutePath()).append("\n");
		if(!dir_to_delete.exists()) {
			dir_to_delete.mkdirs();
			return;
		}
		
		File[] fs = dir_to_delete.listFiles();
		String line = null;
		File file2Delete = null;
		for(File f : fs) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				while((line = br.readLine()) != null) {
					logWriter.append(getCurrentTime()).append("read file: " + line).append("\n");
					if(line.startsWith("#")) {
						continue;
					}
					file2Delete = new File(line.trim());
					if(!file2Delete.isAbsolute()) {
						file2Delete = new File(appHome + File.separator + "lib/" + file2Delete.getName());
//						logWriter.append(getCurrentTime()).append("abstract path=" + file2Delete.getAbsolutePath()).append("\n");
					}
					if(file2Delete.exists()) {
						if(file2Delete.delete()) {
							logWriter.append(getCurrentTime()).append(file2Delete.getAbsolutePath() + " is deleted.").append("\n");
						}
					}
					else {
						logWriter.append(getCurrentTime()).append(file2Delete.getAbsolutePath() + " not exists, skip deletion.").append("\n");
					}
				}
				br.close();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace(new PrintWriter(logWriter));
			} 
			catch (IOException e) {
				e.printStackTrace(new PrintWriter(logWriter));
			}
		}
    }

	/**
	 * @author house
	 * @email  yyh2001@gmail.com
	 * @since  Aug 4, 2013
	 * @param args
	 */
	public static void main(String[] args) {
		Cleaner cleaner = new Cleaner();
		cleaner.start();
	}

}
