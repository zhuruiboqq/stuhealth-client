package com.vastcm.stuhealth.client.framework.report.ui;

import java.awt.Font;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import javax.swing.JTable;

import com.vastcm.swing.print.table.ExtPageFormat;
import com.vastcm.swing.print.table.PageBorder;
import com.vastcm.swing.print.table.PrintTable;
import com.vastcm.swing.print.table.Utility;

public class PrintTableUtil {

	public static PrintTable createPrintTable(JTable tblMain, int origin, int headSubTitleCount) {
		if (PageFormat.LANDSCAPE == origin) {
			PrintTable printTable = new PrintTable(tblMain);
			ExtPageFormat pageFormat = (ExtPageFormat) printTable.getPageFormat();
			//			pageFormat.setHeaderDisplayType(ExtPageFormat.HEADER_DISPLAY_FIRST_PAGE);
			pageFormat.setOrientation(PageFormat.LANDSCAPE);
			//		double x = Utility.millimeterToDot(0);
			//		double y = Utility.millimeterToDot(26);
			double x = Utility.millimeterToDot(0);
			double y = Utility.millimeterToDot(10);

			Paper newPaper = pageFormat.getPaper();
			PageBorder head = pageFormat.getHead();
			head.setHeight(newPaper.getHeight() * 0.1);
			head.setImageableArea(x, y, head.getWidth() - 2 * x, head.getHeight() - y);

			addHeadSubTitle(pageFormat, headSubTitleCount);

			PageBorder foot = pageFormat.getFoot();
			foot.setHeight(newPaper.getHeight() * 0.05);
			foot.setImageableArea(x, y, foot.getWidth() - 2 * x, foot.getHeight() - 2 * y);
			//设置中间打印区域大小
			newPaper.setImageableArea(x, y, newPaper.getWidth() - 2 * x, newPaper.getHeight() - 2 * y);
			pageFormat.setPaper(newPaper);
			pageFormat.setPageHeadDisplayType(ExtPageFormat.HEADER_DISPLAY_ALL_PAGE);

			pageFormat.setShowFoot(true);
			pageFormat.setFootMidContent("第@p/@t页");

			pageFormat.setTableScale(65);
			return printTable;
		}
		return null;
	}

	public static void addHeadSubTitle(ExtPageFormat pageFormat, int headSubTitleCount) {
		addHeadSubTitle(pageFormat, headSubTitleCount, null);
	}

	public static void addHeadSubTitle(ExtPageFormat pageFormat, int headSubTitleCount, Font font) {
		double x = Utility.millimeterToDot(0);
		double y = Utility.millimeterToDot(0);

		Paper newPaper = pageFormat.getPaper();
		PageBorder head = pageFormat.getHead();
		double height = newPaper.getHeight() * 2 / 100;
		if (font != null) {
			height = height * font.getSize() / 10;
		}
		for (int i = 0; i < headSubTitleCount; i++) {
			PageBorder headSubTitle = new PageBorder(head.getWidth(), height);
			headSubTitle.setFont(font);
			headSubTitle.setImageableArea(x, y, headSubTitle.getWidth() - 2 * x, headSubTitle.getHeight());
			//			headSubTitle.setBorderType(PageBorder.BORDER_TOP_LINE);
			pageFormat.getHeadSubTitle().add(headSubTitle);
		}
	}

	public static void addFootSubTitle(ExtPageFormat pageFormat, int headSubTitleCount) {
		addFootSubTitle(pageFormat, headSubTitleCount, null);
	}

	/**
	 * @param pageFormat
	 * @param headSubTitleCount
	 * @param height 高度，小于0即使用默认。
	 */
	public static void addFootSubTitle(ExtPageFormat pageFormat, int headSubTitleCount, Font font) {
		double x = Utility.millimeterToDot(0);
		double y = Utility.millimeterToDot(0);

		Paper newPaper = pageFormat.getPaper();
		PageBorder head = pageFormat.getHead();
		double height = newPaper.getHeight() * 2 / 100;
		if (font != null) {
			height = height * font.getSize() / 8;
		}
		for (int i = 0; i < headSubTitleCount; i++) {
			PageBorder headSubTitle = new PageBorder(head.getWidth(), height);
			headSubTitle.setFont(font);
			//			headSubTitle.setBorderType(PageBorder.BORDER_BOX);
			headSubTitle.setImageableArea(x, y, headSubTitle.getWidth() - 2 * x, headSubTitle.getHeight());
			pageFormat.getFootSubTitle().add(headSubTitle);
		}
	}
}