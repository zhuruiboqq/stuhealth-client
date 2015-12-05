package com.vastcm.io.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.springframework.util.StringUtils;

public class CommonFileFilter extends FileFilter {
	private String description;
	private String[] extFileNames;

	public CommonFileFilter(String description, String... extFileNames) {
		if (!StringUtils.hasText(description)) {
			throw new IllegalArgumentException("文件描述信息不能为空。");
		}
		if (extFileNames == null || extFileNames.length == 0 || !StringUtils.hasText(extFileNames[0])) {
			throw new IllegalArgumentException("文件描述信息不能为空。");
		}
		this.description = description;
		this.extFileNames = extFileNames;
		for (int i = extFileNames.length - 1; i >= 0; i--) {
			extFileNames[i] = extFileNames[i].toLowerCase();
		}
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String tempFileName = f.getName().toLowerCase();
		for (String extFileName : extFileNames) {
			if (tempFileName.endsWith(extFileName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
