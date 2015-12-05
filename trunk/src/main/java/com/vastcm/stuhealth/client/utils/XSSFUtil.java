package com.vastcm.stuhealth.client.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class XSSFUtil {

	public static Object getCellValue(XSSFCell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_BLANK:
			return null;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case XSSFCell.CELL_TYPE_NUMERIC:
			double dValue = cell.getNumericCellValue();
			if (HSSFDateUtil.isCellDateFormatted(cell)) { //日期
				//DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cell.getDateCellValue())
				return cell.getDateCellValue();
			} else if (doubleIsInteger(dValue)) {
				return (int) dValue;
			} else {
				return cell.getNumericCellValue();
			}
		case XSSFCell.CELL_TYPE_ERROR:
		case XSSFCell.CELL_TYPE_FORMULA:
		case XSSFCell.CELL_TYPE_STRING:
		default:
			return cell.toString();
		}
	}

	public static boolean doubleIsInteger(double dValue) {
		return (dValue - (int) dValue) == 0;
	}
}