package com.vastcm.stuhealth.client.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class FileIOUtil {
	/**
	 * 追加文件：使用RandomAccessFile，可随意指定追加位置
	 * @param fileName 文件名
	 * @param content 追加的内容
	 * @param beginIndex 从指定位置追加内容，如果设置为-1或大于文件长度，则从最后追加内容
	 */
	public static void appendContent(String fileName, String content, int beginIndex) throws IOException {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式 
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数 
			long fileLength = randomFile.length();
			//将写文件指针移到指定位置
			if (beginIndex == -1 || beginIndex > fileLength)
				randomFile.seek(fileLength);
			else
				randomFile.seek(beginIndex);

			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			throw e;
		} finally {
			close(randomFile);
		}
	}

	/**
	 * 追加文件：使用FileWriter。直接追加到文件最后
	 * @param fileName
	 * @param content
	 */
	public static void appendContent(String fileName, String content) throws IOException {
		FileWriter writer = null;
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件 
			writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw e;
		} finally {
			close(writer);
		}
	}

	public static void saveFile(String filePath, String content) throws IOException {
		OutputStreamWriter output = null;
		try {
			output = new OutputStreamWriter(new FileOutputStream(new File(filePath)));
			output.write(content);
			output.flush();
		} catch (IOException ioExc) {
			throw ioExc;
		} finally {
			close(output);
		}
	}

	/**
	 * 随机读取文件内容
	 * @param fileName 文件名
	 * @throws IOException
	 */
	public static String readFileByRandomAccess(String fileName, int beginIndex, int readLength) throws IOException {
		RandomAccessFile randomFile = null;
		String content = null;
		try {
			// 打开一个随机访问文件流，按只读方式 
			randomFile = new RandomAccessFile(fileName, "r");
			// 文件长度，字节数 
			long fileLength = randomFile.length();
			// 读文件的起始位置
			if (fileLength < beginIndex) {
				throw new RuntimeException("file length:" + fileLength + ", begin index:" + beginIndex + ", out of range");
			}
			//将读文件的开始位置移到beginIndex位置。 
			randomFile.seek(beginIndex);
			byte[] bytes = new byte[readLength];
			int byteRead = 0;
			//一次读readLength个字节，如果文件内容不足readLength个字节，则读剩下的字节。 
			//将一次读取的字节数赋给byteread 
			while ((byteRead = randomFile.read(bytes)) != -1) {
				content = new String(bytes, 0, byteRead);
			}
		} catch (IOException ioExc) {
			throw ioExc;
		} finally {
			close(randomFile);
		}
		return content;
	}

	public static String readFile(String filePath) throws IOException {
		BufferedReader input = null;
		String readContent;
		StringBuilder content = new StringBuilder();
		try {
			input = new BufferedReader(new FileReader(new File(filePath)));
			while ((readContent = input.readLine()) != null) {
				content.append(readContent);
			}
		} catch (IOException ioExc) {
			throw ioExc;
		} finally {
			close(input);
		}

		return content.toString();
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}
}