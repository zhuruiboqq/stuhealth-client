package com.vastcm.stuhealth.client.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PathUtil {
	private static String ClassRootPath;

	/**
	 * 根据jar包或class所在的根目录(如D:\workspace\project\classes\)，找到相对的路径。<br/>
	 * 如果是路径在最后没有File .separator
	 * @param relativePath
	 * @return
	 */
	public static String getFilePathByClassRoot(String relativePath) {
		if (ClassRootPath == null) {
			ClassRootPath = PathUtil.class.getName();
			String temp = "";
			int index = ClassRootPath.indexOf(".");
			while (index != -1) {
				temp += "../";
				index = ClassRootPath.indexOf(".", index + 1);
			}
			ClassRootPath = getFilePathByClass(PathUtil.class, temp) + File.separator;
		}
		String filePath = null;
		File file = new File(ClassRootPath + relativePath);
		try {
			filePath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/**
	 * 获取一个Class的绝对路径
	 * @param clazz Class对象
	 * @return Class的绝对路径
	 */
	public static String getPathByClass(Class clazz) {
		String path = null;
		try {
			URI uri = clazz.getResource("").toURI();
			File file = new File(uri);
			path = file.getCanonicalPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 获取一个文件相对于一个Class相对的绝对路径
	 * @param clazz Class对象
	 * @param relativePath Class对象的相对路径
	 * @return 文件绝对路径
	 */
	public static String getFilePathByClass(Class clazz, String relativePath) {
		String filePath = null;
		String clazzPath = getPathByClass(clazz);
		StringBuffer sbPath = new StringBuffer(clazzPath);
		sbPath.append(File.separator);
		sbPath.append(relativePath);
		File file = new File(sbPath.toString());
		try {
			filePath = file.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public static void main(String[] args) {
		try {
			System.out.println(getPathByClass(PathUtil.class));
			System.out.println(getFilePathByClass(PathUtil.class, "../../images/logo.gif"));
			System.out.println(getFilePathByClass(PathUtil.class, "../../../"));
			System.out.println(getFilePathByClassRoot("jasper_report"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}