package com.vastcm.stuhealth.client.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class FileChooseUtil {

	public static File chooseFile4Open(Component parent, FileFilter fileFilter) {
		return chooseFile4Open(parent, fileFilter, null);
	}

	public static File chooseFile4Open(Component parent, FileFilter fileFilter, File defaultFilePath) {
		JFileChooser fileChooser = new JFileChooser();
		if (defaultFilePath != null)
			fileChooser.setSelectedFile(defaultFilePath);
		fileChooser.setFileFilter(fileFilter);
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectFile = fileChooser.getSelectedFile();
			return selectFile;
		}
		return null;
	}

	public static File chooseFile4Save(Component parent, FileFilter fileFilter, String defaultFileExt) {
		return chooseFile4Save(parent, fileFilter, defaultFileExt, null);
	}

	public static File chooseFile4Save(Component parent, FileFilter fileFilter, String defaultFileExt, File defaultFilePath) {
		JFileChooser fileChooser = new JFileChooser();
		if (defaultFilePath != null)
			fileChooser.setSelectedFile(defaultFilePath);
		fileChooser.setFileFilter(fileFilter);
		int result = fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectFile = fileChooser.getSelectedFile();
			if (!fileFilter.accept(selectFile)) {
				selectFile = new File(selectFile.getAbsolutePath() + defaultFileExt);
			}
			if (selectFile.exists()) {
				int resultOption = JOptionPane.showConfirmDialog(parent, "文件已存在，是否覆盖？", "是否覆盖？", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION != resultOption) {
					return null;
				}
			}
			return selectFile;
		}
		return null;
	}
}
