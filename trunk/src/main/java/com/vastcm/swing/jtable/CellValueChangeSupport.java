/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.swing.jtable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author house
 */
public class CellValueChangeSupport {
    private Set<CellValueChangeListener> lsnrs = new LinkedHashSet<CellValueChangeListener>();
    private Object host;
    
    public CellValueChangeSupport(Object host) {
        this.host = host;
    }
    
    public void addCellValueChangeListener(CellValueChangeListener lsnr) {
        lsnrs.add(lsnr);
    }
    
    public void removeCellValueChangeListener(CellValueChangeListener lsnr) {
        lsnrs.remove(lsnr);
    }
    
    public void fireCellValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
        Iterator<CellValueChangeListener> iter = lsnrs.iterator();
        while (iter.hasNext()) {
            CellValueChangeListener lsnr = iter.next();
            lsnr.valueChange(oldValue, newValue, rowIndex, colIndex);
        }
    }
}
