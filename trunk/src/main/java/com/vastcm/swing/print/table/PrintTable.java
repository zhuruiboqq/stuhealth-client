package com.vastcm.swing.print.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.vastcm.stuhealth.client.framework.ui.ScreenUtils;

public class PrintTable extends JPanel {
	private static final long serialVersionUID = 1L;

	//the target table  
	JTable table;

	//use as the printable for print  
	TableCliper tableCliper;

	//use as the pageformat for print  
	ExtPageFormat pageFormat;
	JPanel mainPanel = new JPanel();
	BorderLayout thisPanelLayout = new BorderLayout();
	FlowLayout mainPanelLayout = new FlowLayout();
	JButton btnPrint = new JButton();
	JButton btnPrintPreview = new JButton();
	JButton btnSetPaper = new JButton();

	//init PrintTable class with only table  
	public PrintTable(JTable targetTable) {
		//super();  
		//super.repaint();  
		table = targetTable;
		pageFormat = new ExtPageFormat();
		pageFormat.setPaper(PaperSetting.getA4());
	}

	//init PrintTable class with both table and pageformat  
	public PrintTable(JTable targetTable, PageFormat newPageFormat) {
		//super();  
		//super.repaint();  
		table = targetTable;
		pageFormat = (ExtPageFormat) newPageFormat;
	}

	//get PageFormat  
	public PageFormat getPageFormat() {
		return pageFormat;
	}

	//set or get table  
	public void setTable(JTable newTable) {
		table = newTable;
	}

	public JTable getTable() {
		return table;
	}

	//show print preview dialog  
	public void printPreview() {
		//get printable from table  
		tableCliper = new TableCliper(table, pageFormat);

		//build a printPreview dialog to show the paper  
		PrintPreview2 printPreview = new PrintPreview2(tableCliper, 0);

		JDialog newDialog = new JDialog();
		newDialog.setTitle("打印预览");
		newDialog.setModal(true);
		newDialog.getContentPane().add(printPreview);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		newDialog.setSize((int) scrSize.getWidth() - 100, (int) scrSize.getHeight() - 100);
		ScreenUtils.center(newDialog);

		newDialog.setVisible(true);
	}

	//show a dialog for set paper  
	public void paperSetPage() {
		//send the ExtPageFormat to it and build a dialog  
		PaperSetPage paperSetPage = new PaperSetPage(pageFormat);

		JDialog newDialog = new JDialog();
		newDialog.setTitle("打印设置");
		newDialog.setModal(true);
		newDialog.getContentPane().add(paperSetPage);
		newDialog.setSize(675, 450);

		newDialog.setVisible(true);
	}

	//printing  
	public void printWithDialog() {
		tableCliper = new TableCliper(table, pageFormat);

		PrintMonitor printMonitor = new PrintMonitor(tableCliper, pageFormat.getJobName());
		try {
			printMonitor.performPrint(true);
		} catch (PrinterException pe) {
			pe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Print Error:" + pe.getMessage());
		}
	}

	public void printWithoutDialog() {
		tableCliper = new TableCliper(table, pageFormat);

		PrintMonitor printMonitor = new PrintMonitor(tableCliper, pageFormat.getJobName());
		try {
			printMonitor.performPrint(false);
		} catch (PrinterException pe) {
			pe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Print Error:" + pe.getMessage());
		}
	}

	public PrintTable() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		mainPanel.setLayout(mainPanelLayout);
		this.setLayout(thisPanelLayout);
		this.setBorder(BorderFactory.createEtchedBorder());
		btnPrint.setText("Print ...");
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrint_actionPerformed(e);
			}
		});
		btnPrintPreview.setText("Print With Preview");
		btnPrintPreview.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrintPreview_actionPerformed(e);
			}
		});
		btnSetPaper.setText("SetPaper");
		btnSetPaper.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSetPaper_actionPerformed(e);
			}
		});
		this.add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(btnPrint, null);
		mainPanel.add(btnPrintPreview, null);
		mainPanel.add(btnSetPaper, null);
	}

	void btnPrint_actionPerformed(ActionEvent e) {
		this.printWithDialog();
	}

	void btnPrintPreview_actionPerformed(ActionEvent e) {
		this.printPreview();
	}

	void btnSetPaper_actionPerformed(ActionEvent e) {
		this.paperSetPage();
	}
}