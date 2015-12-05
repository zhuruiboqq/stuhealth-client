/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.swing.jtable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author house
 */
public class MyRow {
    
	private Logger logger = LoggerFactory.getLogger(MyRow.class);
    private Map<String, Object> rowData = new HashMap<String, Object>();
    private int rowIndex;
    private MyTable table;

    public MyRow(int rowIndex, MyTable table) {
        this.rowIndex = rowIndex;
        this.table = table;
        table.addCellValueChangeListener4Row(new CellValueChangeListener() {

            public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
            	logger.debug("oldValue=" + oldValue + "; newValue=" + newValue + " [" + rowIndex + ", " + colIndex + "]");
                if(MyRow.this.rowIndex == rowIndex) {
                    tableValueChange(oldValue, newValue, rowIndex, colIndex);    
                }
            }
        });
    }
    
    public void setValue(String colName, Object value) {
        setValue(colName, value, true);
    }
    
    public void setValue(int colIndex, Object value) {
        String colName = table.getColumnName(colIndex);
        setValue(colName, value);
    }
    
    private void setValue(String colName, Object value, boolean fireTableChange) {
        rowData.put(colName, value);
        if(fireTableChange) {
            Object oldValue = rowData.get(colName);
            table.setIsNoCellValueChangeEvent(true);
            table.cellValueChange(oldValue, value, rowIndex, table.getColumnIndex(colName));
            table.setIsNoCellValueChangeEvent(false); 
        }
    }
    
    private void setValue(int colIndex, Object value, boolean fireTableChange) {
        String colName = table.getColumnName(colIndex);
        setValue(colName, value, fireTableChange);
    }
    
    public Object getValue(String colName) {
        return rowData.get(colName);
    }
    
    private void tableValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
        setValue(colIndex, newValue, true);
    }
}
