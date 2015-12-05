/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.swing.jtable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author house
 */
public class MyTable extends JTable {
    
    private Logger logger = LoggerFactory.getLogger(MyTable.class);
    private boolean isLocked = false;
    private Map<Integer, String> columnIndexMap = new HashMap<Integer, String>();
    private Map<String, Integer> columnNameMap = new HashMap<String, Integer>();
    // 存放外部监听器
    private CellValueChangeSupport cellValueChangeSupport = new CellValueChangeSupport(this);
    // 存放MyRow监听器
    private CellValueChangeSupport cellValueChange4Row = new CellValueChangeSupport(this);
    private boolean isNoCellValueChangeEvent;
    private List<MyRow> rows = new LinkedList<MyRow>();


    public MyTable() {
        setRowHeight(25);
    } 
    
    public boolean isIsNoCellValueChangeEvent() {
        return isNoCellValueChangeEvent;
    }

    public void setIsNoCellValueChangeEvent(boolean isNoCellValueChangeEvent) {
        this.isNoCellValueChangeEvent = isNoCellValueChangeEvent;
    }
    
    public void addCellValueChangeListener(CellValueChangeListener lsnr) {
        cellValueChangeSupport.addCellValueChangeListener(lsnr);
    }
    
    public void removeCellValueChangeListener(CellValueChangeListener lsnr) {
        cellValueChangeSupport.removeCellValueChangeListener(lsnr);
    }
    
    protected void addCellValueChangeListener4Row(CellValueChangeListener lsnr) {
        cellValueChange4Row.addCellValueChangeListener(lsnr);
    }
    
    protected void removeCellValueChangeListener4Row(CellValueChangeListener lsnr) {
        cellValueChange4Row.removeCellValueChangeListener(lsnr);
    }

    @Override
    public void tableChanged(TableModelEvent tme) {
        super.tableChanged(tme); //To change body of generated methods, choose Tools | Templates.
        updateColumnMap();
        int rowIndex = tme.getFirstRow();
        int colIndex = tme.getColumn();
        if(rowIndex == -1 || colIndex == -1) {
            return;
        }
        logger.debug("tableChanged [" + rowIndex + ", " + colIndex +"] " + tme.getType());
        if(TableModelEvent.UPDATE == tme.getType()) {
            cellValueChangeSupport.fireCellValueChange(null, getValueAt(rowIndex, colIndex), rowIndex, colIndex);
        }
        
        if(!isNoCellValueChangeEvent) {
            cellValueChange4Row.fireCellValueChange(null, getValueAt(rowIndex, colIndex), rowIndex, colIndex);
        }
        
    }
    
    protected void updateColumnMap() {
//        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) getTableHeader().getColumnModel();
        int colCount = columnModel.getColumnCount();
        for(int i = 0; i < colCount; i++) {
            String colName = getModel().getColumnName(i); //columnModel.getColumn(i).getHeaderValue().toString();
            columnIndexMap.put(i, colName);
            columnNameMap.put(colName, i);
        }
    }
    
    public int getColumnIndex(String colName) {
        return columnNameMap.get(colName);
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        if(isLocked) {
            return false;
        }
        return super.isCellEditable(i, i1); //To change body of generated methods, choose Tools | Templates.
    }
    
    public MyRow addRow() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(new Object[]{});
        MyRow row = new MyRow(model.getRowCount()-1, this);
        rows.add(row);
        return row;
    }
    
    public MyRow getRow(int i) {
        return rows.get(i);
    }

    public void cellValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
        logger.debug("table cellValueChange: " + newValue + " " + rowIndex + " " + colIndex);
        setValueAt(newValue, rowIndex, colIndex);
        
    }
}
