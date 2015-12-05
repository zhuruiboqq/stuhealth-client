package com.vastcm.stuhealth.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XSSFReadHelper {

	/**
	 * The type of the data value is indicated by an attribute on the cell. The value is usually in a "v" element within
	 * the cell.
	 */
	enum xssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, DATE, NUMBER,
	}

	/**
	 * Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
	 * <p/>
	 * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
	 * http://www.ecma-international.org/publications/standards/Ecma-376.htm
	 * <p/>
	 * A web-friendly version is http://openiso.org/Ecma/376/Part4
	 */
	@SuppressWarnings("rawtypes")
	class MyXSSFSheetHandler extends DefaultHandler {

		/**
		 * Table with styles
		 */
		private StylesTable stylesTable;

		/**
		 * Table with unique strings
		 */
		private ReadOnlySharedStringsTable sharedStringsTable;

		/**
		 * Number of columns to read starting with leftmost
		 */
		private final XSSFReadOption option;

		private Vector<Vector<Object>> dataVector;
		private Vector<Object> rowData;
		private boolean isStopReading = false;;

		// Set when V start element is seen
		private boolean vIsOpen;

		// Set when cell start element is seen;
		// used when cell close element is seen.
		private xssfDataType nextDataType;

		// Used to format numeric cell values.
		private short formatIndex;
		private String formatString;

		private int thisColumn = -1;
		// The last column printed to the output stream
		private int lastColumnNumber = -1;

		// Gathers characters as they are seen.
		private StringBuffer value;

		/**
		 * Accepts objects needed while parsing.
		 * 
		 * @param styles Table of styles
		 * @param strings Table of shared strings
		 * @param cols Minimum number of columns to show
		 * @param target Sink for output
		 */

		public MyXSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, XSSFReadOption XSSFReadOption) {
			this.stylesTable = styles;
			this.sharedStringsTable = strings;
			this.option = XSSFReadOption;
			this.value = new StringBuffer();
			this.nextDataType = xssfDataType.NUMBER;
			this.dataVector = new Vector<Vector<Object>>();
			this.rowData = new Vector<Object>();
		}

		public Vector<Vector<Object>> getDatas() {
			int maxSize = -1;
			for (Vector row : dataVector) {
				if (row.size() > maxSize) {
					maxSize = row.size();
				}
			}
			for (Vector row : dataVector) {
				row.setSize(maxSize);
			}
			return dataVector;
		}

		/*
		   * (non-Javadoc)
		   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		   */
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if (isStopReading)
				return;

			if ("inlineStr".equals(name) || "v".equals(name)) {
				vIsOpen = true;
				// Clear contents cache
				value.setLength(0);
			}
			// c => cell
			else if ("c".equals(name)) {
				// Get the cell reference
				String r = attributes.getValue("r");
				int firstDigit = -1;
				for (int c = 0; c < r.length(); ++c) {
					if (Character.isDigit(r.charAt(c))) {
						firstDigit = c;
						break;
					}
				}
				thisColumn = nameToColumn(r.substring(0, firstDigit));

				// Set up defaults.
				this.nextDataType = xssfDataType.NUMBER;
				this.formatIndex = -1;
				this.formatString = null;
				String cellType = attributes.getValue("t");
				String cellStyleStr = attributes.getValue("s");
				if ("b".equals(cellType))
					nextDataType = xssfDataType.BOOL;
				else if ("e".equals(cellType))
					nextDataType = xssfDataType.ERROR;
				else if ("inlineStr".equals(cellType))
					nextDataType = xssfDataType.INLINESTR;
				else if ("s".equals(cellType))
					nextDataType = xssfDataType.SSTINDEX;
				else if ("str".equals(cellType))
					nextDataType = xssfDataType.FORMULA;
				else if (cellStyleStr != null) {
					// It's a number, but almost certainly one
					//  with a special style or format 
					int styleIndex = Integer.parseInt(cellStyleStr);
					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
					this.formatIndex = style.getDataFormat();
					this.formatString = style.getDataFormatString();
					if (this.formatString == null) {
						this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
					}
					if (DateUtil.isADateFormat(formatIndex, formatString)) {
						this.nextDataType = xssfDataType.DATE;
					}
				}
			}

		}

		/*
		   * (non-Javadoc)
		   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		   */
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (isStopReading)
				return;

			Object objValue = null;

			// v => contents of a cell
			if ("v".equals(name)) {
				// Process the value contents as required.
				// Do now, as characters() may be called more than once
				switch (nextDataType) {

				case BOOL:
					char first = value.charAt(0);
					objValue = first != '0';
					break;

				case ERROR:
					objValue = "\"ERROR:" + value.toString() + '"';
					break;

				case FORMULA:
					// A formula could result in a string value,
					// so always add double-quote characters.
					objValue = value.toString();
					break;

				case INLINESTR:
					// TODO: have seen an example of this, so it's untested.
					XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
					objValue = rtsi.toString();
					break;

				case SSTINDEX:
					String sstIndex = value.toString();
					try {
						int idx = Integer.parseInt(sstIndex);
						XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
						objValue = rtss.toString();
					} catch (NumberFormatException ex) {
						ex.printStackTrace();
					}
					break;
				case DATE:
					Double d = Double.parseDouble(value.toString());
					if (DateUtil.isValidExcelDate(d)) {
						objValue = DateUtil.getJavaDate(d, false);
					} else {
						objValue = d;
					}
					break;
				case NUMBER:
					Double dd = Double.parseDouble(value.toString());
					if (XSSFUtil.doubleIsInteger(dd)) {
						objValue = dd.intValue();
					} else {
						objValue = dd;
					}
					break;

				default:
					objValue = null;
					//					thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
					break;
				}

				// Output after we've seen the string contents
				// Emit commas for any fields that were missing on this row
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
				//				System.out.println(lastColumnNumber + " " + thisColumn);
				for (int i = lastColumnNumber + 1; i < thisColumn; ++i) {
					rowData.add(null);
				}

				// Might be the empty string.
				rowData.add(objValue);

				// Update column
				if (thisColumn > -1)
					lastColumnNumber = thisColumn;

			} else if ("row".equals(name)) {

				// Print out any missing commas if needed
				if (option.getMinColumns() > 0) {
					// Columns are 0 based
					if (lastColumnNumber == -1) {
						lastColumnNumber = 0;
					}
					if (lastColumnNumber < option.getMinColumns()) {
						rowData.setSize(option.getMinColumns());
					}
				}
				if (option.getKeyColumnIndex() > 0 && dataVector.size() > option.getIgronKeyRowCount()) {
					isStopReading = rowData.size() < option.getKeyColumnIndex() || rowData.get(option.getKeyColumnIndex()) == null;
				}
				// We're onto a new row
				if (isStopReading)
					return;

				dataVector.add(rowData);
				rowData = new Vector<Object>();
				lastColumnNumber = -1;
			}

		}

		/**
		 * Captures characters only if a suitable element is open. Originally was just "v"; extended for inlineStr also.
		 */
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (isStopReading)
				return;

			if (vIsOpen)
				value.append(ch, start, length);
		}

		/**
		 * Converts an Excel column name like "C" to a zero-based index.
		 * 
		 * @param name
		 * @return Index corresponding to the specified name
		 */
		private int nameToColumn(String name) {
			int column = -1;
			for (int i = 0; i < name.length(); ++i) {
				int c = name.charAt(i);
				column = (column + 1) * 26 + c - 'A';
			}
			return column;
		}

	}

	///////////////////////////////////////

	private OPCPackage xlsxPackage;
	private XSSFReadOption option;

	/**
	 * Creates a new XLSX -> CSV converter
	 * 
	 * @param pkg The XLSX package to process
	 * @param output The PrintStream to output the CSV to
	 * @param minColumns The minimum number of columns to output, or -1 for no minimum
	 * @throws InvalidFormatException
	 */
	public XSSFReadHelper(File xlsxFile, XSSFReadOption option) throws InvalidFormatException {
		this.xlsxPackage = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
		this.option = option;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and shared-strings tables.
	 * 
	 * @param styles
	 * @param strings
	 * @param sheetInputStream
	 */
	public Vector<Vector<Object>> processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
			throws IOException, ParserConfigurationException, SAXException {

		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings, option);
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);
		return handler.getDatas();
	}

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 * 
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public Vector<Vector<Object>> process(int sheetIndex) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		int index = 0;
		Vector<Vector<Object>> datas = null;
		while (iter.hasNext()) {
			if (index != sheetIndex) {
				InputStream stream = iter.next();
				stream.close();
				++index;
				continue;
			}
			InputStream stream = iter.next();
			String sheetName = iter.getSheetName();
			datas = processSheet(styles, strings, stream);
			stream.close();
			++index;
			break;
		}
		xlsxPackage.close();
		return datas;
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "D:/bzhuorui/work/t体检/app_data/学生体检信息最新导入模版.xlsx", "30" };
		if (args.length < 1) {
			System.err.println("Use:");
			System.err.println("  XLSX2CSV <xlsx file> [min columns]");
			return;
		}

		File xlsxFile = new File(args[0]);
		if (!xlsxFile.exists()) {
			System.err.println("Not found or not a file: " + xlsxFile.getPath());
			return;
		}

		// The package open is instantaneous, as it should be.
		XSSFReadOption option = new XSSFReadOption();
		option.setMinColumns(30);
		option.setIgronKeyRowCount(5);
		option.setKeyColumnIndex(1);

		XSSFReadHelper xlsx2csv = new XSSFReadHelper(xlsxFile, option);
		Vector<Vector<Object>> data = xlsx2csv.process(0);
		System.out.println(data);
	}

}