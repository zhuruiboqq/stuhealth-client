/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.jidesoft.hssf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.CellSpan;
import com.jidesoft.grid.CellSpanTable;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.Expandable;
import com.jidesoft.grid.ITreeTableModel;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableScrollPane;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.grid.TreeTableUtils;
import com.jidesoft.grid.ValueStringAdjustProvider;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.StringConverter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HssfTableUtils {
	private static final Logger a = Logger.getLogger(HssfTableUtils.class.getName());
	public static final String CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT = "ExcelOutputFormat:";
	public static final String EXCEL_OUTPUT_FORMAT_2003 = "2003";
	public static final String EXCEL_OUTPUT_FORMAT_2007 = "2007";
	static com.jidesoft.grid.CellStyle b = new com.jidesoft.grid.CellStyle();
	static int c = 0;

	public static boolean isHssfInstalled() {
		try {
			Class.forName("org.apache.poi.hssf.usermodel.HSSFWorkbook");
			return true;
		} catch (Throwable localThrowable) {
			System.err.println("Error loading org.apache.poi.hssf.usermodel.HSSFWorkbook. The reason is that: " + localThrowable.getMessage());
			System.err
					.println("You must have POI-HSSF related jars in the classpath in order to use Excel HSSF exporting feature. Please download from http://poi.apache.org/spreadsheet/index.html and include the all the dependency jar files in the classpath.");
		}
		return false;
	}

	public static boolean isXssfInstalled() {
		try {
			Class.forName("org.apache.poi.xssf.usermodel.XSSFWorkbook");
			return true;
		} catch (Throwable localThrowable) {
			System.err.println("Error loading org.apache.poi.xssf.usermodel.XSSFWorkbook. The reason is that: " + localThrowable.getMessage());
			System.err
					.println("You must have POI-XSSF related jars (version 3.5 and after) in the classpath in order to use Excel XSSF exporting feature. Please download from http://poi.apache.org/spreadsheet/index.html and include the all the dependency jar files in the classpath.");
		}
		return false;
	}

	public static boolean export(JTable paramJTable, String paramString1, String paramString2, boolean paramBoolean) throws IOException {
		return export(paramJTable, paramString1, paramString2, paramBoolean, true, null);
	}

	public static boolean export(JTable paramJTable, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
			throws IOException {
		return export(paramJTable, paramString1, paramString2, paramBoolean1, paramBoolean2, null);
	}

	public static boolean export(JTable paramJTable, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2,
			CellValueConverter paramCellValueConverter) throws IOException {
		return export(paramJTable, 0, 0, -1, -1, paramString1, paramString2, paramBoolean1, paramBoolean2, paramCellValueConverter);
	}

	public static boolean export(JTable paramJTable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1,
			String paramString2, boolean paramBoolean1, boolean paramBoolean2, CellValueConverter paramCellValueConverter) throws IOException {
		if (!(isHssfInstalled()))
			return false;
		if (!(new File(paramString1).exists()))
			paramBoolean1 = false;
		Object localObject = new HSSFWorkbook();
		if ("2007".equals(paramJTable.getClientProperty("ExcelOutputFormat:"))) {
			localObject = new XSSFWorkbook();
		}
		int i = (((Workbook) localObject).getSheet(paramString2) != null) ? 1 : 0;
		Sheet localSheet = (i != 0) ? ((Workbook) localObject).getSheet(paramString2) : ((Workbook) localObject).createSheet(paramString2);
		exportToSheet(paramJTable, paramInt1, paramInt2, paramInt3, paramInt4, (Workbook) localObject, localSheet, 0, 0, paramBoolean2,
				paramCellValueConverter);
		FileOutputStream localFileOutputStream = new FileOutputStream(paramString1);
		((Workbook) localObject).write(localFileOutputStream);
		localFileOutputStream.close();
		return true;
	}

	public static boolean export(JTable paramJTable, OutputStream paramOutputStream, String paramString) throws IOException {
		return export(paramJTable, paramOutputStream, paramString, true, null);
	}

	public static boolean export(JTable paramJTable, OutputStream paramOutputStream, String paramString, boolean paramBoolean) throws IOException {
		return export(paramJTable, paramOutputStream, paramString, paramBoolean, null);
	}

	public static boolean export(JTable paramJTable, OutputStream paramOutputStream, String paramString, boolean paramBoolean,
			CellValueConverter paramCellValueConverter) throws IOException {
		return export(paramJTable, paramOutputStream, 0, 0, -1, -1, paramString, paramBoolean, paramCellValueConverter);
	}

	public static boolean export(JTable paramJTable, OutputStream paramOutputStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4,
			String paramString, boolean paramBoolean, CellValueConverter paramCellValueConverter) throws IOException {
		return export(paramJTable, paramOutputStream, paramInt1, paramInt2, paramInt3, paramInt4, paramString, paramBoolean, paramCellValueConverter,
				null);
	}

	public static boolean export(JTable paramJTable, OutputStream paramOutputStream, int paramInt1, int paramInt2, int paramInt3, int paramInt4,
			String paramString, boolean paramBoolean, CellValueConverter paramCellValueConverter, StringConverter paramStringConverter)
			throws IOException {
		if (!(isHssfInstalled()))
			return false;
		Object localObject = new HSSFWorkbook();
		if ("2007".equals(paramJTable.getClientProperty("ExcelOutputFormat:"))) {
			localObject = new XSSFWorkbook();
		}
		int i = (((Workbook) localObject).getSheet(paramString) != null) ? 1 : 0;
		Sheet localSheet = (i != 0) ? ((Workbook) localObject).getSheet(paramString) : ((Workbook) localObject).createSheet(paramString);
		exportToSheet(paramJTable, paramInt1, paramInt2, paramInt3, paramInt4, (Workbook) localObject, localSheet, 0, 0, paramBoolean,
				paramCellValueConverter, paramStringConverter);
		((Workbook) localObject).write(paramOutputStream);
		paramOutputStream.close();
		return true;
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, int paramInt1, int paramInt2) {
		exportToSheet(paramJTable, paramWorkbook, paramSheet, paramInt1, paramInt2, null);
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, int paramInt1, int paramInt2,
			CellValueConverter paramCellValueConverter) {
		exportToSheet(paramJTable, paramWorkbook, paramSheet, paramInt1, paramInt2, true, paramCellValueConverter);
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, int paramInt1, int paramInt2, boolean paramBoolean) {
		exportToSheet(paramJTable, paramWorkbook, paramSheet, paramInt1, paramInt2, paramBoolean, null);
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, int paramInt1, int paramInt2,
			boolean paramBoolean, CellValueConverter paramCellValueConverter) {
		exportToSheet(paramJTable, 0, 0, -1, -1, paramWorkbook, paramSheet, paramInt1, paramInt2, paramBoolean, paramCellValueConverter);
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, int paramInt1, int paramInt2,
			boolean paramBoolean, CellValueConverter paramCellValueConverter, StringConverter paramStringConverter) {
		exportToSheet(paramJTable, 0, 0, -1, -1, paramWorkbook, paramSheet, paramInt1, paramInt2, paramBoolean, paramCellValueConverter,
				paramStringConverter);
	}

	public static void exportToSheet(JTable paramJTable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Workbook paramWorkbook,
			Sheet paramSheet, int paramInt5, int paramInt6, boolean paramBoolean, CellValueConverter paramCellValueConverter) {
		exportToSheet(paramJTable, paramInt1, paramInt2, paramInt3, paramInt4, paramWorkbook, paramSheet, paramInt5, paramInt6, paramBoolean,
				paramCellValueConverter, null);
	}

	public static void exportToSheet(JTable paramJTable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Workbook paramWorkbook,
			Sheet paramSheet, int paramInt5, int paramInt6, boolean paramBoolean, CellValueConverter paramCellValueConverter,
			StringConverter paramStringConverter) {
		HssfTableFormat localHssfTableFormat = new HssfTableFormat();
		localHssfTableFormat.setFirstRow(paramInt1);
		localHssfTableFormat.setFirstColumn(paramInt2);
		localHssfTableFormat.setNumberOfRows(paramInt3);
		localHssfTableFormat.setNumberOfColumns(paramInt4);
		localHssfTableFormat.setStartRow(paramInt5);
		localHssfTableFormat.setStartColumn(paramInt6);
		localHssfTableFormat.setIncludeTableHeader(paramBoolean);
		localHssfTableFormat.setCellValueConverter(paramCellValueConverter);
		localHssfTableFormat.setColumnNameConverter(paramStringConverter);
		exportToSheet(paramJTable, paramWorkbook, paramSheet, localHssfTableFormat);
	}

	public static void exportToSheet(JTable paramJTable, Workbook paramWorkbook, Sheet paramSheet, HssfTableFormat paramHssfTableFormat) {
		if (paramHssfTableFormat == null)
			return;
		int exportColumns = paramHssfTableFormat.getNumberOfColumns();
		int exportRows = paramHssfTableFormat.getNumberOfRows();
		boolean isIncludeTableHeader = paramHssfTableFormat.isIncludeTableHeader();
		int startExcelRow = paramHssfTableFormat.getStartRow();
		int startExcelColumn = paramHssfTableFormat.getStartColumn();
		int firstTableColumn = paramHssfTableFormat.getFirstColumn();
		int firstTableRow = paramHssfTableFormat.getFirstRow();
		StringConverter columnNameConverter = paramHssfTableFormat.getColumnNameConverter();
		CellValueConverter cellValueConverter = paramHssfTableFormat.getCellValueConverter();
		HashMap localHashMap1 = new HashMap();
		HashMap localHashMap2 = new HashMap();
		paramJTable.putClientProperty("HssfTableUtils.HSSFWorkbook", paramWorkbook);
		TreeTableModel localTreeTableModel = null;
		Map localMap = null;
		HashMap localHashMap3 = null;
		HashSet hideColumnIndexSet = new HashSet();
		TableColumnModel localTableColumnModel1 = paramJTable.getColumnModel();
		int i3 = paramJTable.getColumnCount() - 1;
		do {
			if (i3 < 0)
				break;
			if ((localTableColumnModel1.getColumn(i3).getWidth() == 0))
				hideColumnIndexSet.add(Integer.valueOf(i3));
			--i3;
		} while (true);
		Object tableModel;
		label112: if (((paramJTable instanceof TreeTable) && (((((TreeTable) paramJTable).isExportCollapsedRowsToExcel()))))) {
			localTreeTableModel = (TreeTableModel) TableModelWrapperUtils.getActualTableModel(paramJTable.getModel(), TreeTableModel.class);
			if (((paramJTable.getModel() instanceof ITreeTableModel) && (localTreeTableModel instanceof TreeTableModel))) {
				tableModel = ((TreeTable) paramJTable).getRowAt(firstTableRow);
				com.jidesoft.grid.Row localRow1 = null;
				if (exportRows >= 0)
					localRow1 = ((TreeTable) paramJTable).getRowAt(firstTableRow + exportRows - 1);
				localMap = localTreeTableModel.getExpansionState();
				localTreeTableModel.expandAll();
				if (tableModel == null)
					break label112;
				firstTableRow = ((ITreeTableModel) paramJTable.getModel()).getRowIndex((com.jidesoft.grid.Row) tableModel);
				if (firstTableRow < 0)
					firstTableRow = 0;
				int i6 = -1;
				if (localRow1 != null)
					i6 = ((ITreeTableModel) paramJTable.getModel()).getRowIndex(localRow1);
				if (i6 < 0)
					i6 = paramJTable.getModel().getRowCount() - 1;
				if ((firstTableRow <= i6))
					exportRows = i6 - firstTableRow + 1;
			}
		}

		try {
			tableModel = paramJTable.getModel();
			if (exportColumns == -1)
				exportColumns = paramJTable.getColumnCount();
			if (exportRows == -1)
				exportRows = paramJTable.getRowCount();
			if (isIncludeTableHeader) {
				int acutalHeaderRowCount = getActualHeaderRowCount(paramJTable);
				if ((paramJTable.getTableHeader() instanceof NestedTableHeader)) {
					NestedTableHeader nestedTableHeader = (NestedTableHeader) paramJTable.getTableHeader();
					int headerRowNum = ((NestedTableHeader) nestedTableHeader).getRowCount();
					int i11 = 0;
					int tableColumnIndex;
					do {
						if (i11 >= headerRowNum)
							break;
						org.apache.poi.ss.usermodel.Row sheetRow = paramSheet.getRow(startExcelRow + i11);
						if ((sheetRow == null))
							sheetRow = paramSheet.createRow(startExcelRow + i11);
						tableColumnIndex = 0;
						do {
							if (tableColumnIndex >= exportColumns)
								break;
							if (firstTableColumn + tableColumnIndex >= paramJTable.getColumnCount())
								break;
							Cell sheetCell = sheetRow.getCell((short) (tableColumnIndex + startExcelColumn));
							if ((sheetCell == null))
								sheetCell = sheetRow.createCell((short) (tableColumnIndex + startExcelColumn));
							String str2 = ""
									+ ((NestedTableHeader) nestedTableHeader).getHeaderValueAt(firstTableRow + i11, firstTableColumn
											+ tableColumnIndex);
							((Cell) sheetCell).setCellValue(((columnNameConverter != null)) ? columnNameConverter.convert(str2) : str2);
							++tableColumnIndex;
						} while (true);
						++i11;
					} while (true);
					HashSet blockCellSpanSet = new HashSet();
					int rowCount = 0;
					do {
						if (rowCount >= headerRowNum)
							break;
						tableColumnIndex = 0;
						do {
							if (tableColumnIndex >= exportColumns)
								break;
							if (firstTableColumn + tableColumnIndex >= paramJTable.getColumnCount())
								break;
							//此处有错，应该是firstTableRow + rowCount所有的单元格
							CellSpan tableCellSpan = ((NestedTableHeader) nestedTableHeader).getCellSpanAt(startExcelRow + rowCount, firstTableColumn
									+ tableColumnIndex);
							if (((rowCount == headerRowNum - 1) && (rowCount < acutalHeaderRowCount - 1)))
								((CellSpan) tableCellSpan).setRowSpan(((CellSpan) tableCellSpan).getRowSpan() + acutalHeaderRowCount - headerRowNum);
							if ((tableCellSpan != null) && (((!(((Set) blockCellSpanSet).contains(tableCellSpan)))))) {
								if (((CellSpan) tableCellSpan).getRowSpan() == 1) {
									if (((CellSpan) tableCellSpan).getColumnSpan() == 1)
										break;
								}
								((Set) blockCellSpanSet).add(tableCellSpan);
								addMergedRegion(paramSheet, (CellSpan) tableCellSpan, firstTableRow, firstTableColumn, rowCount, tableColumnIndex,
										headerRowNum, exportColumns, startExcelRow, startExcelColumn, -1, -1);
							}
							++tableColumnIndex;
						} while (true);
						++rowCount;
					} while (true);
				}
				//列的纵向合并
				org.apache.poi.ss.usermodel.Row sheetRow = paramSheet.getRow(startExcelRow);
				if ((sheetRow == null))
					sheetRow = paramSheet.createRow(startExcelRow);
				int columnCount = 0;
				do {
					if (columnCount >= exportColumns)
						break;
					if (firstTableColumn + columnCount < paramJTable.getColumnCount()) {
						Cell sheetCell = ((org.apache.poi.ss.usermodel.Row) sheetRow).getCell((short) (columnCount + startExcelColumn));
						if ((sheetCell == null))
							sheetCell = ((org.apache.poi.ss.usermodel.Row) sheetRow).createCell((short) (columnCount + startExcelColumn));
						String str1 = paramJTable.getColumnName(firstTableColumn + columnCount);
						((Cell) sheetCell).setCellValue(((columnNameConverter != null)) ? columnNameConverter.convert(str1) : str1);
						if (acutalHeaderRowCount > 1) {
							CellSpan newCellSpan = new CellSpan(0, columnCount, acutalHeaderRowCount, 1);
							addMergedRegion(paramSheet, (CellSpan) newCellSpan, firstTableRow, firstTableColumn, 0, columnCount,
									acutalHeaderRowCount, exportColumns, startExcelRow, startExcelColumn, -1, -1);
						}
					}
					++columnCount;
				} while (true);
				startExcelRow += acutalHeaderRowCount;
			}
			int i4 = 0;
			int i10;
			int i12;
			do {
				if (i4 >= exportRows)
					break;
				if (firstTableRow + i4 >= paramJTable.getRowCount())
					break;
				org.apache.poi.ss.usermodel.Row sheetRow;
				try {
					sheetRow = paramSheet.getRow(i4 + startExcelRow);
				} catch (IndexOutOfBoundsException localIndexOutOfBoundsException1) {
					a.warning("Please try use XssfTableutils.export() to export to excel 2007 format if you have more than 65536 rows per sheet.");
					return;
				}
				if ((sheetRow == null))
					sheetRow = paramSheet.createRow(i4 + startExcelRow);
				i10 = 0;
				i12 = -1;
				do {
					if (i10 >= exportColumns)
						break;
					if (firstTableColumn + i10 >= paramJTable.getColumnCount())
						break;
					int i14 = paramJTable.convertColumnIndexToModel(firstTableColumn + i10);
					if (i14 == -1)
						break;
					if (!(hideColumnIndexSet.contains(Integer.valueOf(i10)))) {
						Object cellValue;
						Cell curSheetCell = ((org.apache.poi.ss.usermodel.Row) sheetRow).getCell((short) (++i12 + startExcelColumn));
						if ((curSheetCell == null))
							curSheetCell = ((org.apache.poi.ss.usermodel.Row) sheetRow).createCell((short) (i12 + startExcelColumn));
						if (paramJTable instanceof ValueStringAdjustProvider) {
							if (((ValueStringAdjustProvider) paramJTable).needAdjustCellValueString(firstTableRow + i4, firstTableColumn + i10)) {
								cellValue = ((ValueStringAdjustProvider) paramJTable).getValueAtInString(firstTableRow + i4, firstTableColumn + i10,
										cellValueConverter);
							}
						}
						cellValue = ((TableModel) tableModel).getValueAt(firstTableRow + i4, i14);
						if (cellValueConverter != null) {
							cellValue = cellValueConverter.convert(paramJTable, cellValue, firstTableRow + i4, firstTableColumn + i10);
							if (cellValueConverter.getDataFormat(paramJTable, cellValue, firstTableRow + i4, firstTableColumn + i10) == -1) {
								if (paramJTable.getModel() instanceof ContextSensitiveTableModel) {
									cellValue = ObjectConverterManager.toString(cellValue,
											((ContextSensitiveTableModel) paramJTable.getModel()).getCellClassAt(firstTableRow + i4, i14),
											((ContextSensitiveTableModel) paramJTable.getModel()).getConverterContextAt(firstTableRow + i4, i14));
								}
								cellValue = (cellValue == null) ? "" : cellValue.toString();
							}
						}
						setCellValue((Cell) curSheetCell, cellValue);
						((Cell) curSheetCell).setCellStyle(getContentCellStyle(paramWorkbook, paramJTable, i4, i14, cellValue, cellValueConverter,
								localHashMap1, localHashMap2, paramHssfTableFormat));
					}
					++i10;
				} while (true);
				++i4;
			} while (true);
			if (paramJTable instanceof CellSpanTable) {
				if (((CellSpanTable) paramJTable).isCellSpanOn()) {
					HashSet localHashSet2 = new HashSet();
					int i7 = 0;
					do {
						if (i7 >= exportRows)
							break;
						if (firstTableRow + i7 >= paramJTable.getRowCount())
							break;
						i10 = 0;
						i12 = -1;
						do {
							if (i10 >= exportColumns)
								break;
							if (firstTableColumn + i10 >= paramJTable.getColumnCount())
								break;
							if (!(hideColumnIndexSet.contains(Integer.valueOf(i10)))) {
								++i12;
								CellSpan localCellSpan = ((CellSpanTable) paramJTable).getCellSpanAt(firstTableRow + i7, i10);
								if ((localCellSpan != null) && (((!(localHashSet2.contains(localCellSpan)))))) {
									if (localCellSpan.getRowSpan() == 1) {
										if (localCellSpan.getColumnSpan() == 1)
											break;
									}
									localHashSet2.add(localCellSpan);
									int i16 = 0;
									int i17 = localCellSpan.getColumnSpan() - 1;
									int i18 = localCellSpan.getColumn();
									do {
										if (i17 < 0)
											break;
										if (hideColumnIndexSet.contains(Integer.valueOf(i17 + i18)))
											++i16;
										--i17;
									} while (true);
									org.apache.poi.ss.usermodel.Row localRow4;
									try {
										localRow4 = paramSheet.getRow(i7 + startExcelRow);
									} catch (IndexOutOfBoundsException localIndexOutOfBoundsException2) {
										a.warning("Please try use XssfTableutils.export() to export to excel 2007 format if you have more than 65536 rows per sheet.");
										return;
									}
									Cell localCell = localRow4.getCell((short) (i12 + startExcelColumn));
									int i19 = paramJTable.convertColumnIndexToModel(localCellSpan.getColumn());
									if (i19 == -1)
										break;
									Object localObject6 = null;
									if (paramJTable instanceof ValueStringAdjustProvider) {
										localObject6 = ((TableModel) tableModel).getValueAt(localCellSpan.getRow(), i19);
										if (((ValueStringAdjustProvider) paramJTable).needAdjustCellValueString(firstTableRow + i7,
												localCellSpan.getColumn())) {
											localObject6 = ((ValueStringAdjustProvider) paramJTable).getValueAtInString(localCellSpan.getRow(),
													localCellSpan.getColumn(), cellValueConverter);
										}
									}
									if (cellValueConverter != null) {
										localObject6 = cellValueConverter.convert(paramJTable, localObject6, localCellSpan.getRow(),
												localCellSpan.getColumn());
										if (cellValueConverter.getDataFormat(paramJTable, localObject6, localCellSpan.getRow(),
												localCellSpan.getColumn()) == -1) {
											if (localObject6 instanceof String)
												break;
											if (paramJTable.getModel() instanceof ContextSensitiveTableModel) {
												localObject6 = ObjectConverterManager.toString(localObject6,
														((ContextSensitiveTableModel) paramJTable.getModel()).getCellClassAt(localCellSpan.getRow(),
																i19), ((ContextSensitiveTableModel) paramJTable.getModel()).getConverterContextAt(
																localCellSpan.getRow(), i19));
											}
											localObject6 = (localObject6 == null) ? "" : localObject6.toString();
										}
									}
									setCellValue(localCell, localObject6);
									localCell.setCellStyle(getContentCellStyle(paramWorkbook, paramJTable, localCellSpan.getRow(), i19, localObject6,
											cellValueConverter, localHashMap1, localHashMap2, paramHssfTableFormat));
									addMergedRegion(paramSheet, localCellSpan, firstTableRow, firstTableColumn, i7, i10, exportRows, exportColumns,
											startExcelRow, startExcelColumn, i12, i16);
								}
							}
							++i10;
						} while (true);
						++i7;
					} while (true);
				}
			}
			int i5;
			if (((paramJTable instanceof TreeTable) && (paramHssfTableFormat.isGroupExpandable()))) {
				localHashMap3 = new HashMap();
				i5 = 0;
				do {
					if (i5 >= exportRows)
						break;
					if (firstTableRow + i5 >= paramJTable.getRowCount())
						break;
					com.jidesoft.grid.Row localRow2 = ((TreeTable) paramJTable).getRowAt(firstTableRow + i5);
					if ((localRow2 instanceof Expandable) && (((((Expandable) localRow2).hasChildren())))) {
						i10 = TreeTableUtils.getDescendantCount(paramJTable.getModel(), localRow2, false, false);
						i12 = Math.min(firstTableRow + i5 + i10, firstTableRow + exportRows - 1);
						paramSheet.groupRow(startExcelRow + i5 + 1, startExcelRow + i12 - firstTableRow - 1);
						localHashMap3.put((Expandable) localRow2, Integer.valueOf(startExcelRow + i5 + 1));
					}
					++i5;
				} while (true);
			}
			if ((paramHssfTableFormat.isAutoSizeColumns())) {
				i5 = 0;
				do {
					if (i5 >= exportColumns)
						break;
					paramSheet.autoSizeColumn((short) (i5 + startExcelColumn));
					i5 = (short) (i5 + 1);
				} while (true);
			}
			TableColumnModel localTableColumnModel2 = paramJTable.getColumnModel();
			int i8 = 0;
			do {
				if (i8 >= exportColumns)
					break;
				i10 = localTableColumnModel2.getColumn(firstTableColumn + i8).getWidth();
				paramSheet.setColumnWidth(i8 + startExcelColumn, Math.min(i10 * 40, 65280));
				i8 = (short) (i8 + 1);
			} while (true);
		} finally {
			localHashMap1.clear();
			localHashMap2.clear();
			paramJTable.putClientProperty("HssfTableUtils.HSSFWorkbook", null);
			if ((localTreeTableModel != null) && (localMap != null)) {
				localTreeTableModel.setExpansionState(localMap);
				if ((localHashMap3 != null)) {
					Set localSet = localHashMap3.keySet();
					Iterator localIterator = localSet.iterator();
					do {
						if (!(localIterator.hasNext()))
							break;
						Expandable localExpandable = (Expandable) localIterator.next();
						if (((!(localExpandable.isExpandable()))) || ((localExpandable.isExpanded())))
							continue;
						int i20 = ((Integer) localHashMap3.get(localExpandable)).intValue();
						paramSheet.setRowGroupCollapsed(i20, true);
					} while (true);
				}
			}
		}
	}

	static int getActualHeaderRowCount(JTable paramJTable) {
		int i = 1;
		if (paramJTable.getTableHeader() instanceof NestedTableHeader)
			i = ((NestedTableHeader) paramJTable.getTableHeader()).getRowCount();
		Object localObject = paramJTable.getClientProperty("TableScrollPane.Parent");
		if ((localObject != null)) {
			TableScrollPane localTableScrollPane = (TableScrollPane) localObject;
			if (((localTableScrollPane.getRowHeaderTable() != paramJTable) && ((((localTableScrollPane.getMainTable() != paramJTable) && (((localTableScrollPane
					.getRowFooterTable() == paramJTable)))))))) {
				int j;
				if (localTableScrollPane.getRowHeaderTable() != null) {
					if (localTableScrollPane.getRowHeaderTable().getTableHeader() instanceof NestedTableHeader) {
						j = ((NestedTableHeader) localTableScrollPane.getRowHeaderTable().getTableHeader()).getRowCount();
						if (j >= i)
							i = j;
					}
				}
				if (localTableScrollPane.getMainTable() != null) {
					if (localTableScrollPane.getMainTable().getTableHeader() instanceof NestedTableHeader) {
						j = ((NestedTableHeader) localTableScrollPane.getMainTable().getTableHeader()).getRowCount();
						if (j >= i)
							i = j;
					}
				}
				if ((localTableScrollPane.getRowFooterTable() != null)) {
					if (localTableScrollPane.getRowFooterTable().getTableHeader() instanceof NestedTableHeader) {
						j = ((NestedTableHeader) localTableScrollPane.getRowFooterTable().getTableHeader()).getRowCount();
						if (j >= i)
							i = j;
					}
				}
			}
		}
		return i;
	}

	static void setCellValue(Cell paramCell, Object value) {
		if (value instanceof Double) {
			paramCell.setCellValue(((Double) value).doubleValue());
		}
		if (value instanceof Integer) {
			paramCell.setCellValue(((Integer) value).intValue());
		}
		if (value instanceof Float) {
			paramCell.setCellValue(((Float) value).floatValue());
		}
		if (value instanceof Short) {
			paramCell.setCellValue(((Short) value).shortValue());
		}
		if (value instanceof Date) {
			paramCell.setCellValue((Date) value);
		}
		if (value instanceof Calendar) {
			paramCell.setCellValue((Calendar) value);
		}
		if (value instanceof Boolean) {
			paramCell.setCellValue(((Boolean) value).booleanValue());
		}
		if (value instanceof BigDecimal) {
			paramCell.setCellValue(((BigDecimal) value).doubleValue());
		}
		if (value != null) {
			paramCell.setCellValue(value.toString());
		}
		paramCell.setCellValue("");
	}

	static void addMergedRegion(Sheet paramSheet, CellSpan paramCellSpan, int firstTableRow, int firstTableColumn, int rowCount, int columnCount,
			int headerRowNum, int exportColumns, int startExcelRow, int startExcelColumn, int paramInt9, int paramInt10) {
		//		paramSheet, (CellSpan) sheetCell, firstTableRow, firstTableColumn, rowCount, columnCount, headerRowNum, exportColumns, startExcelRow,	startExcelColumn, -1, -1
		paramInt9 = (paramInt9 < 0) ? columnCount : paramInt9;
		int lastRowIndex = startExcelRow + rowCount + paramCellSpan.getRowSpan() - 1;
		if (paramCellSpan.getRow() < firstTableRow + rowCount)
			lastRowIndex -= firstTableRow + rowCount - paramCellSpan.getRow();
		if (paramCellSpan.getRow() + paramCellSpan.getRowSpan() >= firstTableRow + headerRowNum)
			lastRowIndex -= paramCellSpan.getRow() + paramCellSpan.getRowSpan() - (firstTableRow + headerRowNum);
		int lastColIndex = (short) (startExcelColumn + paramInt9 + paramCellSpan.getColumnSpan() - 1);
		if (paramCellSpan.getColumn() < firstTableColumn + columnCount)
			lastColIndex -= firstTableColumn + columnCount - paramCellSpan.getColumn();
		if (paramCellSpan.getColumn() + paramCellSpan.getColumnSpan() >= firstTableColumn + exportColumns)
			lastColIndex -= paramCellSpan.getColumn() + paramCellSpan.getColumnSpan() - (firstTableColumn + exportColumns);
		if (paramInt10 > 0)
			lastColIndex -= paramInt10;
		if ((lastColIndex < startExcelColumn + paramInt9))
			lastColIndex = startExcelColumn + paramInt9;
		CellRangeAddress localCellRangeAddress = new CellRangeAddress(startExcelRow + rowCount, lastRowIndex, startExcelColumn + paramInt9,
				lastColIndex);
		paramSheet.addMergedRegion(localCellRangeAddress);
	}

	public static void exportToCell(Sheet paramSheet, int paramInt1, int paramInt2, Object paramObject) {
		org.apache.poi.ss.usermodel.Row localRow = paramSheet.getRow(paramInt1);
		if (localRow == null)
			localRow = paramSheet.createRow(paramInt1);
		Cell localCell = localRow.getCell((short) paramInt2);
		if (localCell == null)
			localCell = localRow.createCell((short) paramInt2);
		setCellValue(localCell, paramObject);
	}

	static org.apache.poi.ss.usermodel.CellStyle getContentCellStyle(Workbook paramWorkbook, JTable paramJTable, int paramInt1, int paramInt2,
			Object paramObject, CellValueConverter paramCellValueConverter,
			Map<com.jidesoft.grid.CellStyle, org.apache.poi.ss.usermodel.CellStyle> paramMap,
			Map<String, org.apache.poi.ss.usermodel.Font> paramMap1, HssfTableFormat paramHssfTableFormat) {
		int i = -1;
		if (paramCellValueConverter != null)
			i = paramCellValueConverter.getDataFormat(paramJTable, paramObject, paramInt1, paramInt2);
		org.apache.poi.ss.usermodel.CellStyle localCellStyle = (org.apache.poi.ss.usermodel.CellStyle) paramMap.get(b);
		if (localCellStyle != null)
			return localCellStyle;
		localCellStyle = getDefaultCellStyle(paramWorkbook);
		paramMap.put(b, localCellStyle);
		Object localObject = ((paramJTable instanceof CellStyleTable)) ? ((CellStyleTable) paramJTable).getStyleModel() : null;
		if (((localObject instanceof StyleModel) && (((((StyleModel) localObject).isCellStyleOn()))))) {
			int j = (paramJTable.getModel() == localObject) ? paramInt1 : TableModelWrapperUtils.getActualRowAt(paramJTable.getModel(), paramInt1,
					StyleModel.class);
			com.jidesoft.grid.CellStyle localCellStyle1 = ((StyleModel) localObject).getCellStyleAt(j, paramInt2);
			if (localCellStyle1 == null)
				return localCellStyle;
			localCellStyle = a(paramMap, localCellStyle1);
			if (localCellStyle != null)
				return localCellStyle;
			localCellStyle = getDefaultCellStyle(paramWorkbook);
			localCellStyle = getCellStyle(paramWorkbook, localCellStyle, localCellStyle1, null, paramMap1, paramHssfTableFormat);
			paramMap.put(localCellStyle1, localCellStyle);
		}
		if (paramCellValueConverter != null) {
			localCellStyle.setDataFormat((short) ((i < 0) ? 0 : i));
		}
		return localCellStyle;
	}

	private static org.apache.poi.ss.usermodel.CellStyle a(Map<com.jidesoft.grid.CellStyle, org.apache.poi.ss.usermodel.CellStyle> paramMap,
			com.jidesoft.grid.CellStyle paramCellStyle) {
		org.apache.poi.ss.usermodel.CellStyle localCellStyle = (org.apache.poi.ss.usermodel.CellStyle) paramMap.get(paramCellStyle);
		if (localCellStyle == null) {
			Iterator localIterator = paramMap.keySet().iterator();
			do {
				if (!(localIterator.hasNext()))
					break;
				com.jidesoft.grid.CellStyle localCellStyle1 = (com.jidesoft.grid.CellStyle) localIterator.next();
				if ((!(JideSwingUtilities.equals(localCellStyle1, paramCellStyle))))
					continue;
				localCellStyle = (org.apache.poi.ss.usermodel.CellStyle) paramMap.get(localCellStyle1);
			} while (true);
		}
		return localCellStyle;
	}

	static org.apache.poi.ss.usermodel.CellStyle getCellStyle(Workbook paramWorkbook, org.apache.poi.ss.usermodel.CellStyle paramCellStyle,
			com.jidesoft.grid.CellStyle paramCellStyle1, String paramString, Map<String, org.apache.poi.ss.usermodel.Font> paramMap,
			HssfTableFormat paramHssfTableFormat) {
		org.apache.poi.ss.usermodel.Font localFont = (org.apache.poi.ss.usermodel.Font) paramMap.get(paramString);
		int i = 0;
		if (paramCellStyle1.getFont() != null) {
			if (localFont == null) {
				localFont = getFont(paramWorkbook);
				paramMap.put("" + localFont.hashCode(), localFont);
			}
			localFont.setFontName(paramCellStyle1.getFont().getFontName());
			if (paramCellStyle1.getFont().getStyle() == 1)
				localFont.setBoldweight((short) 700);
			localFont.setItalic(paramCellStyle1.getFont().getStyle() == 2);
			i = 1;
		}
		if (paramCellStyle1.getForeground() != null) {
			if (localFont == null) {
				localFont = getFont(paramWorkbook);
				paramMap.put("" + localFont.hashCode(), localFont);
			}
			localFont.setColor(a(paramCellStyle1.getForeground()));
			i = 1;
		}
		if (paramCellStyle1.getFontStyle() != -1) {
			if (localFont == null) {
				localFont = getFont(paramWorkbook);
				paramMap.put("" + localFont.hashCode(), localFont);
			}
			int j = paramCellStyle1.getFontStyle();
			localFont.setItalic((j & 0x2) != 0);
			localFont.setBoldweight((short) (((j & 0x1) != 0) ? 700 : 400));
			i = 1;
		}
		if (i != 0)
			paramCellStyle.setFont(localFont);
		if (paramCellStyle1.getBackground() != null) {
			paramCellStyle.setFillPattern((short) 1);
			paramCellStyle.setFillForegroundColor(a(paramCellStyle1.getBackground()));
		}
		if (paramCellStyle1.getHorizontalAlignment() != -1)
			paramCellStyle.setAlignment(a(paramCellStyle1.getHorizontalAlignment()));
		if (paramCellStyle1.getVerticalAlignment() != -1)
			paramCellStyle.setVerticalAlignment(b(paramCellStyle1.getVerticalAlignment()));
		paramCellStyle.setBorderBottom(paramHssfTableFormat.getBottomBorder());
		paramCellStyle.setBorderTop(paramHssfTableFormat.getTopBorder());
		paramCellStyle.setBorderLeft(paramHssfTableFormat.getLeftBorder());
		paramCellStyle.setBorderRight(paramHssfTableFormat.getRightBorder());
		return paramCellStyle;
	}

	static short a(int paramInt) {
		switch (paramInt) {
		case 0:
			return 2;
		case 2:
		case 10:
			return 1;
		case 4:
		case 11:
			return 3;
		case 1:
		case 3:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		}
		return 0;
	}

	static short b(int paramInt) {
		switch (paramInt) {
		case 0:
			return 1;
		case 1:
			return 0;
		case 3:
			return 2;
		case 2:
		}
		return 3;
	}

	static org.apache.poi.ss.usermodel.Font getFont(Workbook paramWorkbook) {
		return paramWorkbook.createFont();
	}

	static org.apache.poi.ss.usermodel.CellStyle getDefaultCellStyle(Workbook paramWorkbook) {
		c += 1;
		org.apache.poi.ss.usermodel.CellStyle localCellStyle = paramWorkbook.createCellStyle();
		localCellStyle.setAlignment((short) 0);
		localCellStyle.setVerticalAlignment((short) 0);
		return localCellStyle;
	}

	private static short a(Color paramColor) {
		Hashtable localHashtable = (Hashtable) HSSFColor.getIndexHash();
		Enumeration localEnumeration = localHashtable.keys();
		double d1 = 1.7976931348623157E+308D;
		short i = -1;
		do {
			if (!(localEnumeration.hasMoreElements()))
				break;
			Integer localInteger = (Integer) localEnumeration.nextElement();
			HSSFColor localHSSFColor = (HSSFColor) localHashtable.get(localInteger);
			short[] arrayOfShort = localHSSFColor.getTriplet();
			double d2 = Math.sqrt(arrayOfShort[0] - paramColor.getRed()) + Math.sqrt(arrayOfShort[1] - paramColor.getGreen())
					+ Math.sqrt(arrayOfShort[2] - paramColor.getBlue());
			if (d2 >= d1)
				continue;
			d1 = d2;
			i = localInteger.shortValue();
		} while (true);
		if (i != -1)
			return i;
		return 64;
	}

	public static class ContextSensitiveCellValueConverter implements HssfTableUtils.CellValueConverter {
		public Object convert(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2) {
			ContextSensitiveTableModel localContextSensitiveTableModel = (ContextSensitiveTableModel) TableModelWrapperUtils.getActualTableModel(
					paramJTable.getModel(), ContextSensitiveTableModel.class);
			if (localContextSensitiveTableModel == null)
				return paramObject;
			ConverterContext localConverterContext = localContextSensitiveTableModel.getConverterContextAt(paramInt1,
					paramJTable.convertColumnIndexToModel(paramInt2));
			if (localConverterContext != null) {
				if (paramObject != null) {
					ObjectConverter localObjectConverter = ObjectConverterManager.getConverter(paramObject.getClass(), localConverterContext);
					if (localObjectConverter != null) {
						if (localObjectConverter.supportToString(paramObject, localConverterContext))
							return localObjectConverter.toString(paramObject, localConverterContext);
					}
				}
			}
			return paramObject;
		}

		public int getDataFormat(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2) {
			return -1;
		}
	}

	public static class DefaultCellValueConverter implements HssfTableUtils.CellValueConverter {
		public Object convert(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2) {
			return paramObject;
		}

		public int getDataFormat(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2) {
			return -1;
		}
	}

	public static abstract interface CellValueConverter {
		public abstract Object convert(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2);

		public abstract int getDataFormat(JTable paramJTable, Object paramObject, int paramInt1, int paramInt2);
	}
}