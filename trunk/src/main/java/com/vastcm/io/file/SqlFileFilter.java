package com.vastcm.io.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SqlFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "*.sql";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String tempFileName = f.getName().toLowerCase();
		if (tempFileName.endsWith(".sql"))
			return true;
		return false;
	}
}