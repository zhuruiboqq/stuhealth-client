package com.vastcm.io.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GZFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "*.gz";
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String tempFileName = f.getName().toLowerCase();
		if (tempFileName.endsWith(".gz"))
			return true;
		return false;
	}
}