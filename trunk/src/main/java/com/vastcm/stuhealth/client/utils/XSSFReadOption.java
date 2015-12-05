package com.vastcm.stuhealth.client.utils;

public class XSSFReadOption {

	private int minColumns = -1;
	private int igronKeyRowCount = -1;
	private int keyColumnIndex = -1;

	public int getMinColumns() {
		return minColumns;
	}

	public void setMinColumns(int minColumns) {
		this.minColumns = minColumns;
	}

	public int getIgronKeyRowCount() {
		return igronKeyRowCount;
	}

	public void setIgronKeyRowCount(int igronKeyRowCount) {
		this.igronKeyRowCount = igronKeyRowCount;
	}

	public int getKeyColumnIndex() {
		return keyColumnIndex;
	}

	public void setKeyColumnIndex(int keyColumnIndex) {
		this.keyColumnIndex = keyColumnIndex;
	}

}
