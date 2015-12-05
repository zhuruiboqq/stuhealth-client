package com.vastcm.swing.print.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.Pageable;
import java.awt.print.PrinterException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */
public class PrintPreview2 extends JPanel {
	private static final long serialVersionUID = 1L;

	protected Pageable pageable;
	protected int pageIndex;

	//declare component  
	//*************************************************************  
	BorderLayout thisLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	BorderLayout mainPanelLayout = new BorderLayout();

	JPanel previewPanel = new JPanel();
	BorderLayout previewPanelLayout = new BorderLayout();
	JScrollPane previewScroll = new JScrollPane();
	PrintComponent printComponent;

	JPanel controlPanel = new JPanel();
	FlowLayout controlPanelLayout = new FlowLayout();

	JButton btnPreviousPage = new JButton();
	JButton btnNextPage = new JButton();
	JTextField txtScale = new JTextField(3);
	JButton btnFitToPage = new JButton();
	JButton btnPrint = new JButton();

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JLabel lblPage = new JLabel();

	//*************************************************************  

	//init PrintPreview with pageable and page  
	public PrintPreview2(Pageable newPageable, int page) {
		try {
			pageable = newPageable;
			pageIndex = page;

			printComponent = new PrintComponent(null, null);
			jbInit();

			displayPage(pageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//layout the component  
	private void jbInit() throws Exception {
		this.setLayout(thisLayout);

		lblPage.setText("0");
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);
		topPanel.add(lblPage, null);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);

		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		mainPanel.add(previewPanel, BorderLayout.CENTER);

		previewPanel.setLayout(previewPanelLayout);
		previewPanel.add(previewScroll, BorderLayout.CENTER);

		previewScroll.getViewport().add(printComponent, null);

		controlPanel.setLayout(controlPanelLayout);

		controlPanel.add(btnPrint, null);
		controlPanel.add(btnPreviousPage, null);
		controlPanel.add(btnNextPage, null);
		controlPanel.add(txtScale, null);
		controlPanel.add(btnFitToPage, null);

		btnPreviousPage.setText("上一页<<");
		btnPreviousPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreviousPage_actionPerformed(e);
			}
		});

		//actions fot the component  
		btnNextPage.setText("下一页>>");
		btnNextPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNextPage_actionPerformed(e);
			}
		});

		txtScale.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtScale_keyReleased(e);
			}
		});

		btnFitToPage.setText("适合页面");
		btnFitToPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFitToPage_actionPerformed(e);
			}
		});

		btnPrint.setText("打印全部");
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrint_actionPerformed(e);
			}
		});
	}

	void btnPreviousPage_actionPerformed(ActionEvent e) {
		displayPage(pageIndex - 1);
	}

	void btnNextPage_actionPerformed(ActionEvent e) {
		displayPage(pageIndex + 1);
	}

	void txtScale_keyReleased(KeyEvent e) {
		try {
			int scale = Integer.parseInt(txtScale.getText());
			printComponent.setScaleFactor(scale);
			repaint();
		} catch (NumberFormatException nfe) {
		}
	}

	protected void displayPage(int index) {
		pageIndex = index;
		printComponent.setPrintabel(pageable.getPrintable(pageIndex));
		printComponent.setPageFormat(pageable.getPageFormat(pageIndex));
		printComponent.setDisplayPage(pageIndex);
		btnPreviousPage.setEnabled(pageIndex > 0);
		btnNextPage.setEnabled(pageIndex < (pageable.getNumberOfPages() - 1));

		lblPage.setText("NO: " + (index + 1) + " of Total " + pageable.getNumberOfPages());
		repaint();
	}

	void btnFitToPage_actionPerformed(ActionEvent e) {
		fitToPage();
	}

	protected void fitToPage() {
		int newScaleFactor;
		Dimension compSize = printComponent.getSizeWithScale(100);
		Dimension viewSize = previewScroll.getSize();

		int scaleX = (viewSize.width - 25) * 100 / compSize.width;
		int scaleY = (viewSize.height - 25) * 100 / compSize.height;

		newScaleFactor = Math.min(scaleX, scaleY);

		printComponent.setScaleFactor(newScaleFactor);
		txtScale.setText(Integer.toString(newScaleFactor));
		repaint();
	}

	void btnPrint_actionPerformed(ActionEvent e) {
		PrintMonitor printMonitor = new PrintMonitor(pageable, "打印");
		try {
			printMonitor.performPrint(true);//TODO is show print setting dialog
		} catch (PrinterException pe) {
			pe.printStackTrace();
			JOptionPane.showMessageDialog(null, "Print Error:" + pe.getMessage());
		}
	}
}
