/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.swing.jtable;

/**
 *
 * @author house
 */
public interface CellValueChangeListener {
    public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex);
}
