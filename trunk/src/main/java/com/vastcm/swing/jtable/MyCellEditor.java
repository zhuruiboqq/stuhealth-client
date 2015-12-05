/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.swing.jtable;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author house
 */
public class MyCellEditor extends DefaultCellEditor {

    private JComboBox cb;
    private JTextField txt;
    private Map<String, List> valueMap;
    private Component currentEditor = null;
    private Class<?> txtClass;

    /**
     * CellEditor contains JComboBox and JTextField on one same column of a table.
     * @param jcb 		JComboBox
     * @param valueMap	values for JComboBox
     * @param clazz		a class determine the content type of the JTextField, such as BigDecimal, String.
     */
    public MyCellEditor(JComboBox jcb, Map<String, List> valueMap, Class<?> txtClass) {
        super(new JTextField());
        this.cb = jcb;
        this.txt = new JTextField();
        this.valueMap = valueMap;
        this.txtClass = txtClass;
        initComponentsEx();
    }
    
    protected void initComponentsEx() {
    	txt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				txt.selectAll();
			}
		});
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object value, boolean bln, int rowIndex, int columnIndex) {
        MyTable table = (MyTable) jtable;
        MyRow row = table.getRow(rowIndex);
        String id = (String) row.getValue("ID");
        List valueLs = valueMap.get(id);
        if(valueLs != null && valueLs.size() > 0) {
            cb.setModel(new DefaultComboBoxModel(valueLs.toArray())); 
            cb.setSelectedItem(value);
            currentEditor = cb;
            return cb; 
        }
        else {
        	if(value != null && value.toString().trim().length() > 0) {
        		txt.setText(value.toString());
        	}
        	else {
        		txt.setText(null);
//        		if(txtClass.isAssignableFrom(String.class)) {
//        			txt.setText("");
//        		}
//        		if(txtClass.isAssignableFrom(BigDecimal.class)) {
//        			txt.setText("0");
//        		}
        	}
            currentEditor = txt;
            return txt;
        }
    }

    @Override
    public Object getCellEditorValue() {
        if(currentEditor == cb) {
            return cb.getSelectedItem();
        }
        if(currentEditor == txt) {
            return txt.getText();
        }
        return null;
        
    }
    
    

}
