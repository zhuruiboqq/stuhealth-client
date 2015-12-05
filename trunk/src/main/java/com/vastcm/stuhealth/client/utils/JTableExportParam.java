package com.vastcm.stuhealth.client.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import com.jidesoft.swing.StringConverter;

public class JTableExportParam {
	public enum TableStyle {
		All_Border, Tow_Line, Three_Line
	}

	private String reportTitle;
	private String tabTitle;
	private JTable exportTable;
	private TableStyle tableStyle = TableStyle.All_Border;
	private List<MessageFormat[]> headerMsgFmtList;
	private List<MessageFormat[]> footerMsgFmtList;

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}

	public JTable getExportTable() {
		return exportTable;
	}

	public void setExportTable(JTable exportTable) {
		this.exportTable = exportTable;
	}

	public TableStyle getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(TableStyle tableStyle) {
		this.tableStyle = tableStyle;
	}

	public List<MessageFormat[]> getHeaderMsgFmtList() {
		return headerMsgFmtList;
	}

	public List<MessageFormat[]> getFooterMsgFmtList() {
		return footerMsgFmtList;
	}

	public void addMessageFormat(boolean isheaderFormat, MessageFormat leftFormat, MessageFormat middleFormat, MessageFormat rightFormat) {
		if (isheaderFormat) {
			if (headerMsgFmtList == null) {
				headerMsgFmtList = new ArrayList<MessageFormat[]>();
			}
			headerMsgFmtList.add(new MessageFormat[] { leftFormat, middleFormat, rightFormat });
		} else {
			if (footerMsgFmtList == null) {
				footerMsgFmtList = new ArrayList<MessageFormat[]>();
			}
			footerMsgFmtList.add(new MessageFormat[] { leftFormat, middleFormat, rightFormat });
		}
	}

	public void addHeaderMessage(MessageFormat leftFormat, MessageFormat middleFormat, MessageFormat rightFormat) {
		addMessageFormat(true, leftFormat, middleFormat, rightFormat);
	}

	public void addFooterMessage(MessageFormat leftFormat, MessageFormat middleFormat, MessageFormat rightFormat) {
		addMessageFormat(false, leftFormat, middleFormat, rightFormat);
	}

	class DefaultStringConverter implements StringConverter {
		@Override
		public String convert(String orginStr) {
			if (orginStr == null)
				return null;
			//			System.out.println(orginStr);
			return orginStr.replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<br/>", "\r\n");
		}
	}
}