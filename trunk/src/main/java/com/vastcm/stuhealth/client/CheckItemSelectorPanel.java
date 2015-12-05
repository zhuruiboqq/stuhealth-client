/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.entity.CheckItem;
import com.vastcm.stuhealth.client.entity.CheckItemResult;
import com.vastcm.stuhealth.client.entity.Vaccin;
import com.vastcm.stuhealth.client.entity.VaccinItem;
import com.vastcm.stuhealth.client.entity.service.ICheckItemResultService;
import com.vastcm.stuhealth.client.entity.service.ICheckItemService;
import com.vastcm.stuhealth.client.entity.service.IVaccinItemService;
import com.vastcm.stuhealth.client.entity.service.IVaccinService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.swing.jtable.CellValueChangeListener;
import com.vastcm.swing.jtable.MyCellEditor;
import com.vastcm.swing.jtable.MyRow;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JButton;

/**
 *
 * @author House
 */
public class CheckItemSelectorPanel extends KernelUI {

    private Logger logger = LoggerFactory.getLogger(CheckItemSelectorPanel.class);
    private ICheckItemService checkItemService;
    private IVaccinService vaccinService;
    private final static int colIndex_IsCustom_checkItem = 5;
    private final static int colIndex_IsCustom_vaccin = 4;
    private final static int colIndex_Flag_checkItem = 6;
    
    /**
     * Creates new form CheckItemSelectorPanel
     */
    public CheckItemSelectorPanel() {
        initComponents();
    }

    @Override
    public void onLoad() throws Exception{
        super.onLoad(); //To change body of generated methods, choose Tools | Templates.
        initData();
        initComponentsEx();
    }
    
    private void initData() {
        initData4CheckItem();
        initData4Vaccin();
    }
    
    private void initData4CheckItem() {
        tblCheckItem.setIsNoCellValueChangeEvent(true);
        
        List<CheckItem> ls = getCheckItemService().getNormalItems();
        for(CheckItem item : ls) {
            MyRow row = tblCheckItem.addRow();
            row.setValue("体检项目", item.getItemName());
            row.setValue("启用", item.isIsSelected());
            row.setValue("单位",    item.getUnit());
            row.setValue("顺序号", item.getSort());
            row.setValue("ID", item.getId());
            row.setValue("是否自定义", item.isIsCustom());
            row.setValue("flag", item.getFlag());
        }
        tblCheckItem.setIsNoCellValueChangeEvent(false);
    }
    
    private void initData4Vaccin() {
        tblVaccin.setIsNoCellValueChangeEvent(true);
        List<Vaccin> ls = getVaccinService().getNormalItems();
        for(Vaccin item : ls) {
            MyRow row = tblVaccin.addRow();
            row.setValue("疫苗名称", item.getVaccinName());
            row.setValue("启用", item.isIsSelected());
            row.setValue("顺序号", item.getSort());
            row.setValue("ID", item.getId());
            row.setValue("是否自定义", item.isIsCustom());
        }
        tblVaccin.setIsNoCellValueChangeEvent(false);
    }
    
    private void initComponentsEx() {
        initComponentsEx4CheckItem();
        initComponentsEx4Vaccin();
    }
    
    private class RowRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable t, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
			int colIndex_custom = 4;
			if(t == tblCheckItem) {
				colIndex_custom = colIndex_IsCustom_checkItem;
			}
			if(t == tblVaccin) {
				colIndex_custom = colIndex_IsCustom_vaccin;
			}
        	boolean isCustom = (Boolean) t.getModel().getValueAt(row, colIndex_custom);
        	logger.debug("render cell (" + row + ", " + column + ") isCustom=" + isCustom);
            if(isCustom) {
            	setBackground(Color.YELLOW);
            }
            else {
            	setBackground(Color.WHITE);
            }
            return super.getTableCellRendererComponent(t, value, isSelected,
                    hasFocus, row, column);
        }
    }
    
    private void initComponentsEx4Vaccin() {
    	tblVaccin.setDefaultRenderer(Object.class, new RowRenderer());
    	
        TableColumn colId = tblVaccin.getColumn("ID");
        colId.setMinWidth(0);
        colId.setMaxWidth(0);
        colId.setPreferredWidth(0);
        colId.setWidth(0);
        
        TableColumn colIsCustom = tblVaccin.getColumn("是否自定义");
        colIsCustom.setMinWidth(0);
        colIsCustom.setMaxWidth(0);
        colIsCustom.setPreferredWidth(0);
        colIsCustom.setWidth(0);
        
      
        tblVaccin.addCellValueChangeListener(new CellValueChangeListener() {

            public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
                vaccinValueChange(oldValue, newValue, rowIndex, colIndex);
            }
        });
        
    }
    
    private void initComponentsEx4CheckItem() {
    	
    	tblCheckItem.setDefaultRenderer(Object.class, new RowRenderer());
    	
        TableColumn colId = tblCheckItem.getColumn("ID");
        colId.setMinWidth(0);
        colId.setMaxWidth(0);
        colId.setPreferredWidth(0);
        colId.setWidth(0);
        
        TableColumn colIsCustom = tblCheckItem.getColumn("是否自定义");
        colIsCustom.setMinWidth(0);
        colIsCustom.setMaxWidth(0);
        colIsCustom.setPreferredWidth(0);
        colIsCustom.setWidth(0);
        
        TableColumn colFlag = tblCheckItem.getColumn("flag");
        colFlag.setMinWidth(0);
        colFlag.setMaxWidth(0);
        colFlag.setPreferredWidth(0);
        colFlag.setWidth(0);
        
        tblCheckItem.addCellValueChangeListener(new CellValueChangeListener() {

            public void valueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
                checkItemValueChange(oldValue, newValue, rowIndex, colIndex);
            }
        });
        
    }
    
    protected void vaccinValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
        MyRow row = tblVaccin.getRow(rowIndex);
        Vaccin item = getVaccinService().getById((String) row.getValue("ID"));
        item.setIsSelected(Boolean.parseBoolean(row.getValue("启用").toString()));
        item.setChooseSort(Integer.decode(row.getValue("顺序号").toString()));
        if(item.isIsCustom()) {
           item.setVaccinName(row.getValue("疫苗名称").toString()); 
        }
        getVaccinService().save(item);
    }
    
    protected void checkItemValueChange(Object oldValue, Object newValue, int rowIndex, int colIndex) {
        logger.debug("value changed [" + rowIndex + "," + colIndex + "] " + newValue);
        logger.debug("row data: " + tblCheckItem.getRow(rowIndex).getValue("体检项目"));
        MyRow row = tblCheckItem.getRow(rowIndex);
        CheckItem item = getCheckItemService().getById((String) row.getValue("ID"));
        item.setIsSelected(Boolean.parseBoolean(row.getValue("启用").toString()));
        item.setEnCode(String.valueOf(row.getValue("顺序号")));
        if(item.isIsCustom()) {
           item.setItemName(row.getValue("体检项目").toString()); 
        }
        getCheckItemService().save(item);
    }
    
    private ICheckItemService getCheckItemService() {
        if(checkItemService == null) {
            checkItemService = AppContext.getBean("checkItemService", ICheckItemService.class);
        }
        return checkItemService;
    }
    
    private IVaccinService getVaccinService() {
        if(vaccinService == null) {
            vaccinService = AppContext.getBean("vaccinService", IVaccinService.class);
        }
        return vaccinService;
    }
    
    @Override
    public void uiClosing() {
    	if(tblCheckItem.isEditing()) {
    		tblCheckItem.getCellEditor().stopCellEditing();
    	}
    	if(tblVaccin.isEditing()) {
    		tblVaccin.getCellEditor().stopCellEditing();
    	}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        btnSelectAll = new javax.swing.JButton();
        btnDeselectAll = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCheckItem = new com.vastcm.swing.jtable.MyTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVaccin = new com.vastcm.swing.jtable.MyTable();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSelectAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/accept.png"))); // NOI18N
        btnSelectAll.setText("全部选择");
        btnSelectAll.setFocusable(false);
        btnSelectAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSelectAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectAllActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSelectAll);

        btnDeselectAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete.png"))); // NOI18N
        btnDeselectAll.setText("全部取消");
        btnDeselectAll.setFocusable(false);
        btnDeselectAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDeselectAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDeselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeselectAllActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDeselectAll);

        add(jToolBar1, java.awt.BorderLayout.NORTH);
        
        btnMust = new JButton("必测项目");
        btnMust.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/accept.png"))); // NOI18N
        btnMust.setText("必测项目");
        btnMust.setFocusable(false);
        btnMust.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMust.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMustActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMust);
        
        btnOption = new JButton("选测项目");
        btnOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/accept.png"))); // NOI18N
        btnOption.setText("选测项目");
        btnOption.setFocusable(false);
        btnOption.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOption.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOption);
        
        btnMustFirstYear = new JButton("入学第一年必测项目");
        btnMustFirstYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/accept.png"))); // NOI18N
        btnMustFirstYear.setText("入学第一年必测项目");
        btnMustFirstYear.setFocusable(false);
        btnMustFirstYear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMustFirstYear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMustFirstYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMustFirstYearActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMustFirstYear);

        jSplitPane2.setDividerLocation(800);
        jSplitPane2.setEnabled(false);

        jSplitPane1.setDividerLocation(350);

        tblCheckItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "启用", "体检项目", "单位", "顺序号", "是否自定义", "flag"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if(columnIndex == 2) {
                    Boolean isCustom = (Boolean) tblCheckItem.getModel().getValueAt(rowIndex, colIndex_IsCustom_checkItem);
                    if(isCustom) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return canEdit[columnIndex];
                }
            }
        });
        jScrollPane2.setViewportView(tblCheckItem);

        jSplitPane1.setLeftComponent(jScrollPane2);

        tblVaccin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "启用", "疫苗名称", "顺序号", "是否自定义"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                Boolean isCustom = (Boolean) tblVaccin.getModel().getValueAt(rowIndex, colIndex_IsCustom_vaccin);
                if(isCustom) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });
        jScrollPane1.setViewportView(tblVaccin);

        jSplitPane1.setRightComponent(jScrollPane1);

        jSplitPane2.setLeftComponent(jSplitPane1);
        jSplitPane2.setRightComponent(jPanel1);

        add(jSplitPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectAllActionPerformed
        int rowCount = tblCheckItem.getRowCount();
        for(int i = 0; i < rowCount; i++) {
            tblCheckItem.getRow(i).setValue("启用", Boolean.TRUE);
        }
        tblCheckItem.repaint();
    }//GEN-LAST:event_btnSelectAllActionPerformed
    
    private void btnMustActionPerformed(java.awt.event.ActionEvent evt) {
        int rowCount = tblCheckItem.getRowCount();
        for(int i = 0; i < rowCount; i++) {
        	String flag = (String) tblCheckItem.getRow(i).getValue("flag");
        	if(flag != null && flag.equals("1")) { // 1表示：必测项目
        		tblCheckItem.getRow(i).setValue("启用", Boolean.TRUE);
        	}
        }
        tblCheckItem.repaint();
    }
    
    private void btnOptionActionPerformed(java.awt.event.ActionEvent evt) {
        int rowCount = tblCheckItem.getRowCount();
        for(int i = 0; i < rowCount; i++) {
        	String flag = (String) tblCheckItem.getRow(i).getValue("flag");
        	if(flag != null && flag.equals("2")) { // 2表示：表示选测
        		tblCheckItem.getRow(i).setValue("启用", Boolean.TRUE);
        	}
        }
        tblCheckItem.repaint();
    }
    
    private void btnMustFirstYearActionPerformed(java.awt.event.ActionEvent evt) {
        int rowCount = tblCheckItem.getRowCount();
        for(int i = 0; i < rowCount; i++) {
        	String flag = (String) tblCheckItem.getRow(i).getValue("flag");
        	if(flag != null && flag.equals("3")) { // 3表示：入学第一年必测
        		tblCheckItem.getRow(i).setValue("启用", Boolean.TRUE);
        	}
        }
        tblCheckItem.repaint();
    }

    private void btnDeselectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeselectAllActionPerformed
        int rowCount = tblCheckItem.getRowCount();
        for(int i = 0; i < rowCount; i++) {
            tblCheckItem.getRow(i).setValue("启用", Boolean.FALSE);
            tblCheckItem.repaint();
        }
    }//GEN-LAST:event_btnDeselectAllActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeselectAll;
    private javax.swing.JButton btnSelectAll;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private com.vastcm.swing.jtable.MyTable tblCheckItem;
    private com.vastcm.swing.jtable.MyTable tblVaccin;
    private JButton btnMust;
    private JButton btnOption;
    private JButton btnMustFirstYear;
    // End of variables declaration//GEN-END:variables
}
