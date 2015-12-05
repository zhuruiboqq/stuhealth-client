/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.utils;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.NestedTableHeader;
import com.jidesoft.grid.TableColumnGroup;

/**
 * 
 * @author bob
 */
public class MyTableUtils {

	public static JLabel getTableHeaderCellComponent(JTable table) {
		TableCellRenderer tableCellRendererDef = table.getTableHeader().getDefaultRenderer();
		TableColumnModel tempColumnModel = table.getColumnModel();
		TableCellRenderer tableCellRenderer = tempColumnModel.getColumn(0).getHeaderRenderer();
		if (tableCellRenderer == null)
			tableCellRenderer = tableCellRendererDef;
		Component cellComponent = null;
		if ((tableCellRenderer != null)) {
			cellComponent = tableCellRenderer.getTableCellRendererComponent(table, "table", false, false, -1, 0);
		}
		if (cellComponent instanceof JLabel)
			return (JLabel) cellComponent;

		return null;
	}

	/**
	 * 添加列头的合并组
	 * @param table
	 * @param columnLable
	 * @param columnIndexs
	 * @return
	 */
	public static TableColumnGroup addColumnGroup2Table(JideTable table, String columnLable, int... columnIndexs) {
		if (!(table.getTableHeader() instanceof NestedTableHeader)) {
			return null;
		}
		TableColumnGroup tempGroup = new TableColumnGroup(columnLable);
		for (int index : columnIndexs) {
			tempGroup.add(table.getColumnModel().getColumn(index));
		}
		((NestedTableHeader) table.getTableHeader()).addColumnGroup(tempGroup);
		return tempGroup;
	}

	/**
	 * 新增表格头的合并单元格，并把合并后的TableColumnGroup添加到指定的上级合并组group中
	 * @param table
	 * @param group
	 * @param columnLable
	 * @param columnIndexs
	 * @return
	 */
	public static TableColumnGroup addColumnGroup2Table(JideTable table, TableColumnGroup group, String columnLable, int... columnIndexs) {
		TableColumnGroup tempGroup = new TableColumnGroup(columnLable);
		for (int index : columnIndexs) {
			tempGroup.add(table.getColumnModel().getColumn(index));
		}
		group.add(tempGroup);

		return tempGroup;
	}

	/**
	 * 隐藏指定JTable的指定列
	 * @param table 指定JTable
	 * @param column 指定列
	 */
	public static void hiddenColumn(JTable table, int column) {
		TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
		//设置顺序必须如下
		tc.setMinWidth(0);
		tc.setMaxWidth(0);
		tc.setPreferredWidth(0);
		tc.setWidth(0);
		//		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
		//		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
	}

	/**
	 * 隐藏指定JTable的指定列
	 * @param table 指定JTable
	 * @param column 指定列
	 */
	public static void hiddenColumn2(JTable table, int column) {
		TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
		//设置顺序必须如下
		tc.setMinWidth(1);
		tc.setMaxWidth(1);
		tc.setPreferredWidth(1);
		tc.setWidth(1);
		//		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
		//		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
	}

	/**
	 * 显示指定JTable的指定列
	 * @param table 指定JTable
	 * @param column 指定列
	 * @param width 指定列显示宽度
	 */
	public static void showColumn(JTable table, int column, int width) {
		TableColumn tc = table.getColumnModel().getColumn(column);
		tc.setMaxWidth(width);
		tc.setPreferredWidth(width);
		tc.setWidth(width);
		tc.setMinWidth(width);
		table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(width);
		table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(width);
	}
}