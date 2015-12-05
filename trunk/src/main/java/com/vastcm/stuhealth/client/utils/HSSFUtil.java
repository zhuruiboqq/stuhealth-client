package com.vastcm.stuhealth.client.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class HSSFUtil {

	public static Object getCellValue(HSSFCell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return null;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case HSSFCell.CELL_TYPE_NUMERIC:
			double dValue = cell.getNumericCellValue();
			if (HSSFDateUtil.isCellDateFormatted(cell)) { //日期
				//DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(cell.getDateCellValue())
				return cell.getDateCellValue();
			} else if (doubleIsInteger(dValue)) {
				return (int) dValue;
			} else {
				return cell.getNumericCellValue();
			}
		case HSSFCell.CELL_TYPE_ERROR:
		case HSSFCell.CELL_TYPE_FORMULA:
		case HSSFCell.CELL_TYPE_STRING:
		default:
			return cell.toString();
		}
	}

	public static boolean doubleIsInteger(double dValue) {
		return (dValue - (int) dValue) == 0;
	}
}