package printtable;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

import javax.swing.*;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PrintPreview extends JPanel {
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

	JButton bPreviousPage = new JButton();
	JButton bNextPage = new JButton();
	JTextField tScale = new JTextField(3);
	JButton bFitToPage = new JButton();
	JButton bPrint = new JButton();

	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JLabel lPage = new JLabel();

	//*************************************************************  

	//init PrintPreview with pageable and page  
	public PrintPreview(Pageable newPageable, int page) {
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

		lPage.setText("0");
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);
		topPanel.add(lPage, null);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);

		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		mainPanel.add(previewPanel, BorderLayout.CENTER);

		previewPanel.setLayout(previewPanelLayout);
		previewPanel.add(previewScroll, BorderLayout.CENTER);

		previewScroll.getViewport().add(printComponent, null);

		controlPanel.setLayout(controlPanelLayout);

		controlPanel.add(bPrint, null);
		controlPanel.add(bPreviousPage, null);
		controlPanel.add(bNextPage, null);
		controlPanel.add(tScale, null);
		controlPanel.add(bFitToPage, null);

		bPreviousPage.setText("Previous");
		bPreviousPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bPreviousPage_actionPerformed(e);
			}
		});

		//actions fot the component  
		bNextPage.setText("Next");
		bNextPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bNextPage_actionPerformed(e);
			}
		});

		tScale.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tScale_keyReleased(e);
			}
		});

		bFitToPage.setText("Fit To Page");
		bFitToPage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bFitToPage_actionPerformed(e);
			}
		});

		bPrint.setText("PrintAll");
		bPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bPrint_actionPerformed(e);
			}
		});
	}

	void bPreviousPage_actionPerformed(ActionEvent e) {
		displayPage(pageIndex - 1);
	}

	void bNextPage_actionPerformed(ActionEvent e) {
		displayPage(pageIndex + 1);
	}

	void tScale_keyReleased(KeyEvent e) {
		try {
			int scale = Integer.parseInt(tScale.getText());
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
		bPreviousPage.setEnabled(pageIndex > 0);
		bNextPage.setEnabled(pageIndex < (pageable.getNumberOfPages() - 1));

		lPage.setText("NO: " + (index + 1) + " of Total " + pageable.getNumberOfPages());
		repaint();
	}

	void bFitToPage_actionPerformed(ActionEvent e) {
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
		tScale.setText(Integer.toString(newScaleFactor));
		repaint();
	}

	void bPrint_actionPerformed(ActionEvent e) {
		PrintMonitor printMonitor = new PrintMonitor(pageable, "ghostliang");
		try {
			printMonitor.performPrint(false);
		} catch (PrinterException pe) {
			JOptionPane.showMessageDialog(null, "Print Error:" + pe.getMessage());
		}
	}
}