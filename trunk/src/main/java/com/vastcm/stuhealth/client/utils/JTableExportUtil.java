package com.vastcm.stuhealth.client.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.hssf.HssfTableFormat;
import com.jidesoft.hssf.HssfTableUtils;

public class JTableExportUtil {

	public static void exportToSheet(File saveFile, JTableExportParam param) throws IOException {
		OutputStream output = new FileOutputStream(saveFile);
		exportToSheet(output, param);
		output.close();
	}

	public static void exportToSheet(OutputStream outputStream, JTableExportParam param) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		exportToSheet(workbook, param);
		workbook.write(outputStream);
	}

	public static void exportToSheet(File saveFile, JTableExportParam... params) throws IOException {
		OutputStream output = new FileOutputStream(saveFile);
		exportToSheet(output, params);
		output.close();
	}

	public static void exportToSheet(OutputStream outputStream, JTableExportParam... params) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (JTableExportParam param : params)
			exportToSheet(workbook, param);
		workbook.write(outputStream);
	}

	private static void exportToSheet(HSSFWorkbook workbook, JTableExportParam param) throws IOException {
		HSSFSheet sheet = workbook.createSheet(param.getTabTitle());

		JTable paramJTable = param.getExportTable();
		List<Integer> hideColumnIndex = new ArrayList<Integer>();
		List<Integer> notHideColumnIndex = new ArrayList<Integer>();
		TableColumnModel tempColumnModel = paramJTable.getColumnModel();
		int columnCount = paramJTable.getColumnCount();
		int tempColumnIndex = 0;
		int tempWidth = 0;
		int sortTableAddWidth = (paramJTable instanceof SortableTable) ? 5 : 0;
		JLabel cellComponent = MyTableUtils.getTableHeaderCellComponent(paramJTable);
		while (tempColumnIndex < columnCount) {
			TableColumn tableColumn = tempColumnModel.getColumn(tempColumnIndex);
			if ((tableColumn.getWidth() == 0)) {//隐藏的列，暂时放开可见
				hideColumnIndex.add(tempColumnIndex);

				tempWidth = -1;
				if ((cellComponent != null)) {
					cellComponent.setText(String.valueOf(tableColumn.getHeaderValue()));
					tempWidth = Math.max(tempWidth, cellComponent.getPreferredSize().width + paramJTable.getIntercellSpacing().width)
							+ sortTableAddWidth;
				}
				tempWidth = Math.max(tempWidth, tableColumn.getMinWidth());

				tableColumn.setMinWidth(tempWidth);
				tableColumn.setMaxWidth(tempWidth);
				tableColumn.setPreferredWidth(tempWidth);
				tableColumn.setWidth(tempWidth);
			} else {
				notHideColumnIndex.add(tempColumnIndex);
			}
			tempColumnIndex++;
		}

		int startExcelRow = 0;
		HSSFRow row;
		HSSFCell cell;
		{//构建标题
			// 生成一个样式
			HSSFCellStyle titleStyle = getTitleStyle(workbook);
			//标题行
			row = sheet.createRow(startExcelRow++);
			row.setHeight((short) (15 * 36));
			cell = row.createCell(0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(new HSSFRichTextString(param.getReportTitle()));
			sheet.addMergedRegion(new CellRangeAddress(startExcelRow - 1, startExcelRow - 1, 0, columnCount - 1));
			sheet.createRow(startExcelRow++);//增加一行空行
		}

		// 生成一个样式，自动添加到工作簿当中
		HSSFCellStyle subTitleStyle = getSubTitleStyle(workbook);
		int[] msgIndexArray;
		if (param.getHeaderMsgFmtList() != null) {//构建单据头的副标题行
			msgIndexArray = initMessageIndex(param, notHideColumnIndex);
			for (MessageFormat[] msgs : param.getHeaderMsgFmtList()) {
				row = sheet.createRow(startExcelRow++);
				for (int i = 0; i < msgs.length; i++) {
					if (msgs[i] != null) {
						cell = row.createCell(msgIndexArray[i]);
						cell.setCellStyle(subTitleStyle);
						cell.setCellValue(new HSSFRichTextString(msgs[i].toPattern()));
					}
				}
			}
		}
		//导出表格内容到Excel
		//		System.out.println(workbook.getNumCellStyles());
		int firstTableRow = 0, firstTableColumn = 0, startExcelColumn = 0, exportRows = -1, exportColumns = -1;
		HssfTableFormat localHssfTableFormat = new HssfTableFormat();
		localHssfTableFormat.setFirstRow(firstTableRow);
		localHssfTableFormat.setFirstColumn(firstTableColumn);
		localHssfTableFormat.setNumberOfRows(exportRows);
		localHssfTableFormat.setNumberOfColumns(exportColumns);
		localHssfTableFormat.setStartRow(startExcelRow);//导出的Excel，从这一行开始写入表格中的内容
		localHssfTableFormat.setStartColumn(startExcelColumn);
		localHssfTableFormat.setIncludeTableHeader(true);
		localHssfTableFormat.setCellValueConverter(null);
		localHssfTableFormat.setColumnNameConverter(param.new DefaultStringConverter());//表头列名的转换
		//表体内容的样式，没生效。要取得最后的样式
		//		localHssfTableFormat.setBottomBorder(CellStyle.BORDER_THIN);
		//		localHssfTableFormat.setTopBorder(CellStyle.BORDER_THIN);
		//		localHssfTableFormat.setLeftBorder(CellStyle.BORDER_THIN);
		//		localHssfTableFormat.setRightBorder(CellStyle.BORDER_THIN);
		HssfTableUtils.exportToSheet(param.getExportTable(), workbook, sheet, localHssfTableFormat);
		//设置表格体导出内容的样式，不包括表格头的
		HSSFCellStyle tableHeaderCellStyle = workbook.getCellStyleAt((short) (workbook.getNumCellStyles() - 2));
		//		tableHeaderCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//		tableHeaderCellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle contentCellStyle = workbook.getCellStyleAt((short) (workbook.getNumCellStyles() - 1));
		contentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		contentCellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);

		if (JTableExportParam.TableStyle.All_Border.equals(param.getTableStyle())) {
			short border = CellStyle.BORDER_THIN;
			contentCellStyle.setBorderTop(border);
			contentCellStyle.setBorderBottom(border);
			contentCellStyle.setBorderLeft(border);
			contentCellStyle.setBorderRight(border);
		}

		//处理表头的合并
		if (exportColumns == -1)
			exportColumns = columnCount;
		if (exportRows == -1)
			exportRows = paramJTable.getRowCount();
		NestedTableHeader nestedTableHeader = (NestedTableHeader) paramJTable.getTableHeader();
		int acutalHeaderRowCount = ((NestedTableHeader) nestedTableHeader).getRowCount();
		int headerRowNum = ((NestedTableHeader) nestedTableHeader).getRowCount();

		//在表头行的合并先删除
		for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
			CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
			//			System.out.println(mergedRegion);
			if (mergedRegion.getFirstRow() >= startExcelRow && mergedRegion.getFirstRow() < startExcelRow + headerRowNum) {
				sheet.removeMergedRegion(i);
			}
		}
		//添加表头行的合并单元格
		if (headerRowNum > 1) {
			if ((paramJTable.getTableHeader() instanceof NestedTableHeader)) {
				int tableColumnIndex;
				HashSet<CellSpan> blockCellSpanSet = new HashSet<CellSpan>();
				int rowCount = 0;
				do {
					if (rowCount >= headerRowNum)
						break;
					tableColumnIndex = 0;
					do {
						if (tableColumnIndex >= exportColumns)
							break;
						if (firstTableColumn + tableColumnIndex >= columnCount)
							break;
						CellSpan tableCellSpan = ((NestedTableHeader) nestedTableHeader).getCellSpanAt(firstTableRow + rowCount, firstTableColumn
								+ tableColumnIndex);
						if (((rowCount == headerRowNum - 1) && (rowCount < acutalHeaderRowCount - 1)))
							(tableCellSpan).setRowSpan((tableCellSpan).getRowSpan() + acutalHeaderRowCount - headerRowNum);
						if ((tableCellSpan != null) && (((!(blockCellSpanSet.contains(tableCellSpan)))))) {
							if ((tableCellSpan).getRowSpan() == 1) {
								if ((tableCellSpan).getColumnSpan() == 1)
									break;
							}
							blockCellSpanSet.add(tableCellSpan);
							addMergedRegion(sheet, tableCellSpan, firstTableRow, firstTableColumn, rowCount, tableColumnIndex, headerRowNum,
									exportColumns, startExcelRow, startExcelColumn, -1, -1);
						}
						++tableColumnIndex;
					} while (true);
					++rowCount;
				} while (true);
			}
		}
		//		for (int i = 0, size = paramSheet.getNumMergedRegions(); i < size; i++) {
		//			System.out.println(paramSheet.getMergedRegion(i));
		//		}
		//处理表头的样式
		HSSFCellStyle tableHeaderStyle = getTableHeaderStyle(workbook, param);
		if (JTableExportParam.TableStyle.All_Border.equals(param.getTableStyle())) {
			for (int i = startExcelRow + headerRowNum - 1; i >= startExcelRow; i--) {
				for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
					sheet.getRow(i).getCell(j).setCellStyle(tableHeaderStyle);
				}
			}
		} else if (JTableExportParam.TableStyle.Three_Line.equals(param.getTableStyle())
				|| JTableExportParam.TableStyle.Tow_Line.equals(param.getTableStyle())) {
			if (headerRowNum == 1) {
				tableHeaderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				tableHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
					sheet.getRow(startExcelRow).getCell(j).setCellStyle(tableHeaderStyle);
				}
			} else {
				tableHeaderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
					sheet.getRow(startExcelRow).getCell(j).setCellStyle(tableHeaderStyle);
				}
				if (headerRowNum > 2) {
					tableHeaderStyle = getTableHeaderStyle(workbook, param);
					for (int i = 1; i < headerRowNum - 1; i++) {
						for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
							sheet.getRow(startExcelRow + i).getCell(j).setCellStyle(tableHeaderStyle);
						}
					}
				}
				tableHeaderStyle = getTableHeaderStyle(workbook, param);
				tableHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
					sheet.getRow(startExcelRow + headerRowNum - 1).getCell(j).setCellStyle(tableHeaderStyle);
				}
			}
		}
		int lastExcelRow = sheet.getLastRowNum();
		//处理最后合计行的样式
		if (JTableExportParam.TableStyle.Tow_Line.equals(param.getTableStyle())
				|| JTableExportParam.TableStyle.Three_Line.equals(param.getTableStyle())) {
			//			HSSFCellStyle tableTotalRowStyle = getTableHeaderStyle(workbook);
			HSSFCellStyle tableTotalRowStyle = workbook.createCellStyle();
			tableTotalRowStyle.cloneStyleFrom(contentCellStyle);

			if (JTableExportParam.TableStyle.Three_Line.equals(param.getTableStyle())) {
				tableTotalRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			} else {
				tableTotalRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			}
			for (int j = startExcelColumn; j < startExcelColumn + exportColumns; j++) {
				sheet.getRow(lastExcelRow).getCell(j).setCellStyle(tableTotalRowStyle);
			}
		}

		if (param.getFooterMsgFmtList() != null) {//构建最后备注
			lastExcelRow++;
			sheet.createRow(lastExcelRow++);//增加一行空行
			msgIndexArray = initMessageIndex(param, notHideColumnIndex);
			for (MessageFormat[] msgs : param.getFooterMsgFmtList()) {
				row = sheet.createRow(lastExcelRow++);
				for (int i = 0; i < msgs.length; i++) {
					if (msgs[i] != null) {
						cell = row.createCell(msgIndexArray[i]);
						cell.setCellStyle(subTitleStyle);
						cell.setCellValue(new HSSFRichTextString(msgs[i].toPattern()));
					}
				}
			}
		}
		//隐藏列
		Iterator<Integer> iterator = hideColumnIndex.iterator();
		while (iterator.hasNext()) {
			tempColumnIndex = iterator.next();
			MyTableUtils.hiddenColumn(paramJTable, tempColumnIndex);
			sheet.setColumnHidden(tempColumnIndex, true);
		}
	}

	private static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		// 设置这些样式
		//			titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//			titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont titleFont = workbook.createFont();
		//			titleFont.setColor(HSSFColor.VIOLET.index);
		titleFont.setFontHeightInPoints((short) 20);
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		titleStyle.setFont(titleFont);
		return titleStyle;
	}

	private static HSSFCellStyle getSubTitleStyle(HSSFWorkbook workbook) {
		HSSFCellStyle subTitleStyle = workbook.createCellStyle();
		// 设置这些样式
		//		subTitleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//		subTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//		subTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//		subTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//		subTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//		subTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		subTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 生成一个字体
		HSSFFont subTitleFont = workbook.createFont();
		//		subTitleFont.setColor(HSSFColor.VIOLET.index);
		subTitleFont.setFontHeightInPoints((short) 12);
		//		subTitleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		subTitleStyle.setFont(subTitleFont);
		return subTitleStyle;
	}

	private static HSSFCellStyle getTableHeaderStyle(HSSFWorkbook workbook, JTableExportParam param) {
		HSSFCellStyle tableHeaderStyle = workbook.createCellStyle();
		// 设置这些样式
		//		subTitleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		//		subTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		tableHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		tableHeaderStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		tableHeaderStyle.setWrapText(true);

		if (JTableExportParam.TableStyle.All_Border.equals(param.getTableStyle())) {
			tableHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			tableHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			tableHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			tableHeaderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 生成一个字体
			HSSFFont tableHeaderFont = workbook.createFont();
			//		subTitleFont.setColor(HSSFColor.VIOLET.index);
			tableHeaderFont.setFontHeightInPoints((short) 10);
			tableHeaderFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			tableHeaderStyle.setFont(tableHeaderFont);
		}
		return tableHeaderStyle;
	}

	static void addMergedRegion(Sheet paramSheet, CellSpan tableCellSpan, int firstTableRow, int firstTableColumn, int rowCount, int columnCount,
			int headerRowNum, int exportColumns, int startExcelRow, int startExcelColumn, int paramInt9, int paramInt10) {
		//		System.out.println("" + firstTableRow + " " + firstTableColumn + " rowCount" + rowCount + " columnCount" + columnCount + " headerRowNum"
		//				+ headerRowNum + " exportColumns" + exportColumns + " startExcelRow" + startExcelRow + " startExcelColumn" + startExcelColumn);
		//		System.out.println("CellSpan r" + tableCellSpan.getRow() + " rs" + tableCellSpan.getRowSpan() + " c" + tableCellSpan.getColumn() + " cs"
		//				+ tableCellSpan.getColumnSpan());
		paramInt9 = (paramInt9 < 0) ? columnCount : paramInt9;
		int lastRowIndex = startExcelRow + rowCount + tableCellSpan.getRowSpan() - 1;
		if (tableCellSpan.getRow() < firstTableRow + rowCount)
			lastRowIndex -= firstTableRow + rowCount - tableCellSpan.getRow();
		if (tableCellSpan.getRow() + tableCellSpan.getRowSpan() >= firstTableRow + headerRowNum)
			lastRowIndex -= tableCellSpan.getRow() + tableCellSpan.getRowSpan() - (firstTableRow + headerRowNum);
		int lastColIndex = (short) (startExcelColumn + paramInt9 + tableCellSpan.getColumnSpan() - 1);
		if (tableCellSpan.getColumn() < firstTableColumn + columnCount)
			lastColIndex -= firstTableColumn + columnCount - tableCellSpan.getColumn();
		if (tableCellSpan.getColumn() + tableCellSpan.getColumnSpan() >= firstTableColumn + exportColumns)
			lastColIndex -= tableCellSpan.getColumn() + tableCellSpan.getColumnSpan() - (firstTableColumn + exportColumns);
		if (paramInt10 > 0)
			lastColIndex -= paramInt10;
		if ((lastColIndex < startExcelColumn + paramInt9))
			lastColIndex = startExcelColumn + paramInt9;
		CellRangeAddress cellRange = new CellRangeAddress(startExcelRow + rowCount, lastRowIndex, startExcelColumn + paramInt9, lastColIndex);

		//		System.out.println("fr:" + cellRange.getFirstRow() + " fc:" + cellRange.getFirstColumn() + "lr:" + cellRange.getLastRow() + " lc:"
		//				+ cellRange.getLastColumn());
		paramSheet.addMergedRegion(cellRange);
	}

	private static int[] initMessageIndex(JTableExportParam param, List<Integer> notHideColumnIndex) {
		//		JTable table = param.getExportTable();
		int columnCount = notHideColumnIndex.size();
		int[] msgIndexArray = new int[3];
		if (columnCount == 1) {
			msgIndexArray[0] = notHideColumnIndex.get(0);
			msgIndexArray[1] = notHideColumnIndex.get(0);
			msgIndexArray[2] = notHideColumnIndex.get(0);
		} else if (columnCount == 2) {
			msgIndexArray[0] = notHideColumnIndex.get(0);
			msgIndexArray[1] = notHideColumnIndex.get(1);
			msgIndexArray[2] = notHideColumnIndex.get(1);
		} else if (columnCount == 3) {
			msgIndexArray[0] = notHideColumnIndex.get(0);
			msgIndexArray[1] = notHideColumnIndex.get(1);
			msgIndexArray[2] = notHideColumnIndex.get(2);
		} else {
			int index = 0;
			msgIndexArray[0] = notHideColumnIndex.get(index);
			index = columnCount / 3 + 1;
			msgIndexArray[1] = notHideColumnIndex.get(index);
			index = columnCount * 2 / 3;
			msgIndexArray[2] = notHideColumnIndex.get(index);
		}
		return msgIndexArray;
	}
}