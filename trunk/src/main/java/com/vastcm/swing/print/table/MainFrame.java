package com.vastcm.swing.print.table;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	//declear mainPanel  
	PrintTable pt;
	JPanel mainPanel;
	BorderLayout mainPanelLayout = new BorderLayout();

	//declear bottomPanel  
	JPanel bottomPanel = new JPanel();

	//declear tablePanel  
	PageFormat pageFormat = new PageFormat();
	JPanel tablePanel = new JPanel();
	BorderLayout tablePanelLayout = new BorderLayout();
	JScrollPane tableScroll = new JScrollPane();
	JTable mainTable = new JTable();

	//declear statePanel  
	JPanel statePanel = new JPanel();

	//declear tableInfoPanel  
	JPanel tableInfoPanel = new JPanel();

	JLabel lColumnNum = new JLabel();
	JTextField col = new JTextField(5);
	JLabel lRowNum = new JLabel();
	JTextField row = new JTextField(5);
	JButton okSetRCNum = new JButton();

	//declear cellInfoPanel  

	//declear menu  
	JMenuBar mainMenu = new JMenuBar();
	JMenu mFile = new JMenu();
	JMenuItem mPageSetting = new JMenuItem();
	JMenuItem mExit = new JMenuItem();
	JMenuItem mPrintPreview = new JMenuItem();
	JMenuItem mPrint = new JMenuItem();
	JMenu mHelp = new JMenu();
	JMenuItem mHelpTopics = new JMenuItem();
	JMenuItem mAbout = new JMenuItem();
	BorderLayout borderLayout1 = new BorderLayout();
	FlowLayout flowLayout2 = new FlowLayout();
	BorderLayout borderLayout2 = new BorderLayout();

	/** Main method */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}

	/** Construct the frame */
	public MainFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			((DefaultTableModel) mainTable.getModel()).setColumnCount(3);
			((DefaultTableModel) mainTable.getModel()).setRowCount(3);
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					mainTable.setValueAt("" + i + "/" + j, i, j);
			pt = new PrintTable(mainTable);
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Component initialization */
	private void jbInit() throws Exception {
		//setIconImage(Toolkit.getDefaultToolkit().createImage(MainFrame.class.getResource("[Your Icon]")));  

		//Layout mainPanel  
		mainPanel = (JPanel) this.getContentPane();
		mainPanel.setLayout(mainPanelLayout);

		this.setJMenuBar(mainMenu);
		this.setSize(new Dimension(450, 550));
		this.setTitle("Print Table Alpha 1.0");

		okSetRCNum.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okSetRCNum_actionPerformed(e);
			}
		});
		mainPanel.add(bottomPanel, BorderLayout.CENTER);
		//mainPanel.add(pt,BorderLayout.NORTH);  

		//Layout bottomPanel  
		bottomPanel.setLayout(borderLayout1);

		bottomPanel.add(tablePanel, BorderLayout.CENTER);
		bottomPanel.add(statePanel, BorderLayout.SOUTH);

		//Layout tablePanel  
		tablePanel.setLayout(tablePanelLayout);
		mainTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.gray));

		mainTable.setAutoResizeMode(0);
		mainTable.setSelectionMode(0);
		tablePanel.add(tableScroll, null);
		tableScroll.getViewport().add(mainTable, null);

		//Layout statePanel  
		statePanel.setLayout(borderLayout2);

		statePanel.add(tableInfoPanel, BorderLayout.CENTER);

		//LayoutTableInfoPanel  
		tableInfoPanel.setLayout(flowLayout2);
		tableInfoPanel.setBorder(BorderFactory.createEtchedBorder());

		lRowNum.setText("Rows:");
		tableInfoPanel.add(lRowNum, null);
		tableInfoPanel.add(row, null);

		row.setText("3");

		lColumnNum.setText("Columns:");
		tableInfoPanel.add(lColumnNum, null);
		tableInfoPanel.add(col, null);

		col.setText("3");

		okSetRCNum.setText("OK");
		tableInfoPanel.add(okSetRCNum, null);

		//Layout Menu  
		//File  
		mainMenu.add(mFile);
		mFile.setText("File");

		mFile.add(mPrint);
		mPrint.setText("Print ...");
		mPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doPrint();
			}
		});

		mFile.add(mPrintPreview);
		mPrintPreview.setText("Print With Preview");
		mPrintPreview.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doPrintPreview();
			}
		});

		mFile.add(mPageSetting);
		mPageSetting.setText("Page Setting ...");
		mPageSetting.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				doPageSetting();
			}
		});

		mFile.addSeparator();

		mExit.setText("Exit");
		mFile.add(mExit);

		//Help  
		mainMenu.add(mHelp);
		mHelp.setText("Help");

		mHelp.add(mHelpTopics);
		mHelpTopics.setText("Help Topics");

		mHelp.add(mAbout);
		mAbout.setText("About ...");
	}

	/** Overridden so we can exit when window is closed */
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		}
	}

	void doPrint() {
		pt.printWithDialog();
	}

	void doPrintPreview() {
		pt.printPreview();
	}

	void doPageSetting() {
		pt.paperSetPage();
	}

	void okSetRCNum_actionPerformed(ActionEvent e) {
		try {
			((DefaultTableModel) mainTable.getModel()).setColumnCount(Integer.parseInt(col.getText()));
			((DefaultTableModel) mainTable.getModel()).setRowCount(Integer.parseInt(row.getText()));
			for (int i = 0; i < Integer.parseInt(row.getText()); i++)
				for (int j = 0; j < Integer.parseInt(col.getText()); j++)
					mainTable.setValueAt("" + i + "/" + j, i, j);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Error Input", "Caution!", JOptionPane.ERROR_MESSAGE);
		}
	}
}