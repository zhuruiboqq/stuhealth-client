package com.vastcm.io.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExcelFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "*.xls;*.xlsx";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String tempFileName = f.getName().toLowerCase();
		if (tempFileName.endsWith(".xls") || tempFileName.endsWith(".xlsx"))
			return true;
		return false;
	}
}