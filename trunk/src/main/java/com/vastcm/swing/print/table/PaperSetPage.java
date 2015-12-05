package com.vastcm.swing.print.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.Paper;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PaperSetPage extends JPanel {
	private static final long serialVersionUID = 1L;
	//declare if the combo is clicked  
	boolean isDirectClicked = false;
	boolean isPageHeadClicked = false;
	boolean isTableHeadClicked = false;
	boolean isTableAlignClicked = false;
	boolean isTableHeadBorderClicked = false;
	boolean isTableFootBorderClicked = false;

	//declare the pageFormat  
	ExtPageFormat pageFormat;

	//declare the buffer for the text  

	//declare thisLayout which include just mainPanel  
	BorderLayout thisLayout = new BorderLayout();

	//declare mainPanel which include paperPanel and controlPanel  
	JPanel mainPanel = new JPanel();
	GridLayout mainPanelLayout = new GridLayout();

	//declare paperPanel where to draw the graphics  
	JPanel paperPanel = new JPanel();
	BorderLayout paperPanelLayout = new BorderLayout();
	PaperComponent paperComponent;

	//declare marginPanel where to edit the margin of the paper  
	JPanel marginPanel = new JPanel();

	JLabel lblTopMargin = new JLabel();
	JTextField txtTopMargin = new JTextField(5);

	JLabel lblBottomMargin = new JLabel();
	JTextField txtBottomMargin = new JTextField(5);

	JLabel lblLeftMargin = new JLabel();
	JTextField txtLeftMargin = new JTextField(5);

	JLabel lblRightMargin = new JLabel();
	JTextField txtRightMargin = new JTextField(5);

	//declare sizePanel where to set the size of the paper  
	JPanel sizePanel = new JPanel();

	JComboBox paperType = new JComboBox();

	JLabel lblPaperWidth = new JLabel();
	JTextField txtPaperWidth = new JTextField(3);

	JLabel lblPaperHeight = new JLabel();
	JTextField txtPaperHeight = new JTextField(3);

	//declare directPanel where to set the direct of the paper  

	ButtonGroup directChoiceGroup = new ButtonGroup();

	//declare the label with millimeter  
	JLabel mm1 = new JLabel();
	JLabel mm2 = new JLabel();
	JLabel mm3 = new JLabel();
	JLabel mm4 = new JLabel();
	JLabel mm5 = new JLabel();
	JLabel mm6 = new JLabel();
	JLabel lblSettingMargin = new JLabel();
	JPanel otherPanel = new JPanel();
	JRadioButton landscape = new JRadioButton();
	JRadioButton portrait = new JRadioButton();
	JLabel mm16 = new JLabel();
	JLabel lHeadRightContent = new JLabel();
	JLabel lHeadRightMargin = new JLabel();
	JLabel mm13 = new JLabel();
	JLabel mm12 = new JLabel();
	JLabel mm14 = new JLabel();
	JLabel mm15 = new JLabel();
	JLabel lHeadLeftContent = new JLabel();
	JLabel lHeadLeftMargin = new JLabel();
	JTextField txtHeadRightContent = new JTextField(5);
	JTextField txtHeadMidContent = new JTextField(5);
	JTextField txtHeadLeftContent = new JTextField(5);
	JTextField txtHeadHeight = new JTextField(5);
	JTextField txtHeadRightMargin = new JTextField(5);
	FlowLayout flowLayout1 = new FlowLayout();
	JLabel lHeadHeight = new JLabel();
	JTextField txtHeadBottomMargin = new JTextField(5);
	JLabel lblHeadTopMargin = new JLabel();
	JLabel lblHeadMidContent = new JLabel();
	JTextField txtHeadLeftMargin = new JTextField(5);
	JLabel lHeadBottomMargin = new JLabel();
	JTextField txtHeadTopMargin = new JTextField(5);
	JPanel showHeadPanel = new JPanel();
	JPanel headPanel = new JPanel();
	JPanel headInfoPanel = new JPanel();
	JComboBox comHeadRightContent = new JComboBox();
	JComboBox comHeadMidContent = new JComboBox();
	BorderLayout borderLayout1 = new BorderLayout();
	JComboBox comHeadLeftContent = new JComboBox();
	JLabel mm11 = new JLabel();
	JLabel lblFootRightContent = new JLabel();
	JLabel lblFootRightMargin = new JLabel();
	JLabel mm8 = new JLabel();
	JLabel mm7 = new JLabel();
	JLabel mm9 = new JLabel();
	JLabel mm10 = new JLabel();
	JLabel lblFootLeftContent = new JLabel();
	JLabel lblFootLeftMargin = new JLabel();
	JTextField txtFootRightContent = new JTextField(5);
	JTextField txtFootMidContent = new JTextField(5);
	JTextField txtFootLeftContent = new JTextField(5);
	JTextField txtFootHeight = new JTextField(5);
	JTextField txtFootLeftMargin = new JTextField(5);
	JLabel lblFootHeight = new JLabel();
	FlowLayout showFootPanelLayout = new FlowLayout();
	JTextField txtFootRightMargin = new JTextField(5);
	JLabel lblFootMidContent = new JLabel();
	JLabel lblFootTopMargin = new JLabel();
	JCheckBox checkBoxSetFoot = new JCheckBox();
	JTextField txtFootBottomMargin = new JTextField(5);
	JLabel lblFootBottomMargin = new JLabel();
	JTextField txtFootTopMargin = new JTextField(5);
	JPanel showFootPanel = new JPanel();
	JPanel footPanel = new JPanel();
	JPanel footInfoPanel = new JPanel();
	JComboBox comFootRightContent = new JComboBox();
	JComboBox comFootMidContent = new JComboBox();
	BorderLayout footInfoPanelLayout = new BorderLayout();
	JComboBox comFootLeftContent = new JComboBox();
	JLabel lblTableHeadModel = new JLabel();
	JLabel lblTableAlign = new JLabel();
	JLabel lblTableScale = new JLabel();
	JTextField txtTableScale = new JTextField();
	JPanel southPanel = new JPanel();
	JPanel westPanel = new JPanel();
	JPanel eastPanel = new JPanel();
	JPanel northPanel = new JPanel();
	ButtonGroup tableHeadGroup = new ButtonGroup();
	ButtonGroup tableAlignGroup = new ButtonGroup();
	JComboBox comPageHead = new JComboBox();
	JComboBox comTableHead = new JComboBox();
	JComboBox comTableAlign = new JComboBox();
	JLabel lblTableHeadFootBorder = new JLabel();
	JComboBox comTableHeadBorder = new JComboBox();
	JComboBox comTableFootBorder = new JComboBox();
	JLabel lblTableHeadBorder = new JLabel();
	JLabel lblTableFootBorder = new JLabel();
	JLabel lblJobName = new JLabel();
	JTextField txtJobName = new JTextField();

	void setSelectedBorderType(JComboBox comHeadBorder, int borderType) {
		if (borderType == PageBorder.BORDER_TOP_LINE)
			comHeadBorder.setSelectedIndex(0);
		else if (borderType == PageBorder.BORDER_BOX)
			comHeadBorder.setSelectedIndex(1);
		else
			comHeadBorder.setSelectedIndex(2);
	}

	void setSelectedHeadDisplayType(JComboBox comHeadDisplayType, int headDisplayType) {
		if (headDisplayType == ExtPageFormat.HEADER_DISPLAY_ALL_PAGE)
			comHeadDisplayType.setSelectedIndex(0);
		else if (headDisplayType == ExtPageFormat.HEADER_DISPLAY_FIRST_PAGE)
			comHeadDisplayType.setSelectedIndex(1);
		else
			comHeadDisplayType.setSelectedIndex(2);
	}

	public PaperSetPage(ExtPageFormat newPageFormat) {
		try {
			Paper paper = newPageFormat.getPaper();
			pageFormat = newPageFormat;
			//TODO zrb 设置head
			paperComponent = new PaperComponent(pageFormat);
			paperComponent.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			jbInit();

			paperType.setSelectedIndex(PaperSetting.getIndex(paper.getWidth(), paper.getHeight()));

			//set paper margin  
			txtTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
			txtBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
					- pageFormat.getImageableHeight())) + 1));
			txtLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
			txtRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
					- pageFormat.getImageableWidth())) + 1));

			//set paper size  
			txtPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
			txtPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

			//set head margin  
			txtHeadTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHead().getImageableY())) + 1));
			txtHeadBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHead().getHeight()
					- pageFormat.getHead().getImageableY() - pageFormat.getHead().getImageableHeight())) + 1));
			txtHeadLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableX())) + 1));
			txtHeadRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHead().getWidth()
					- pageFormat.getHead().getImageableX() - pageFormat.getHead().getImageableWidth())) + 1));

			//set head height  
			txtHeadHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHead().getHeight())) + 1));

			//set head content  
			txtHeadLeftContent.setText(pageFormat.getHead().getLeftContent());
			txtHeadMidContent.setText(pageFormat.getHead().getMidContent());
			txtHeadRightContent.setText(pageFormat.getHead().getRightContent());

			//set foot margin  
			txtFootTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableY())) + 1));
			txtFootBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootHeight() - pageFormat.getFootImageableY()
					- pageFormat.getFootImageableHeight())) + 1));
			txtFootLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableX())) + 1));
			txtFootRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootWidth() - pageFormat.getFootImageableX()
					- pageFormat.getFootImageableWidth())) + 1));

			//set foot height  
			txtFootHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootHeight())) + 1));

			//set foot content  
			txtFootLeftContent.setText(pageFormat.getFootLeftContent());
			txtFootMidContent.setText(pageFormat.getFootMidContent());
			txtFootRightContent.setText(pageFormat.getFootRightContent());

			//Set Table Head  
			setSelectedHeadDisplayType(comTableHead, pageFormat.getTableHeadDisplayType());

			//set Table Align  
			if (pageFormat.getTableAlignment() == ExtPageFormat.TABLE_ALIGN_LEFT)
				comTableAlign.setSelectedIndex(0);
			else if (pageFormat.getTableAlignment() == ExtPageFormat.TABLE_ALIGN_MID)
				comTableAlign.setSelectedIndex(1);
			else
				comTableAlign.setSelectedIndex(2);

			//set table head border  
			setSelectedBorderType(comTableHeadBorder, pageFormat.getHead().getBorderType());
			//set table foot border  
			setSelectedBorderType(comTableFootBorder, pageFormat.getFootBorderType());

			this.directChoiceGroup.add(this.portrait);
			this.directChoiceGroup.add(this.landscape);

			//set paper direct  
			if (pageFormat.getOrientation() == 0)
				landscape.setSelected(true);
			else
				portrait.setSelected(true);

			txtTableScale.setText("" + pageFormat.getTableScale());

			//set page head display type
			setSelectedHeadDisplayType(comPageHead, pageFormat.getPageHeadDisplayType());

			checkBoxSetFoot.setSelected(pageFormat.getShowFoot());

			txtJobName.setText(pageFormat.getJobName());

			repaint();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		comFootLeftContent.addItem("selfdefine");
		comFootLeftContent.addItem("Date");
		comFootLeftContent.addItem("Page");
		comFootLeftContent.addItem("TotalPage");

		comFootMidContent.addItem("selfdefine");
		comFootMidContent.addItem("Date");
		comFootMidContent.addItem("Page");
		comFootMidContent.addItem("TotalPage");

		comFootRightContent.addItem("selfdefine");
		comFootRightContent.addItem("Date");
		comFootRightContent.addItem("Page");
		comFootRightContent.addItem("TotalPage");

		comHeadLeftContent.addItem("selfdefine");
		comHeadLeftContent.addItem("Date");
		comHeadLeftContent.addItem("Page");
		comHeadLeftContent.addItem("TotalPage");

		comHeadMidContent.addItem("selfdefine");
		comHeadMidContent.addItem("Date");
		comHeadMidContent.addItem("Page");
		comHeadMidContent.addItem("TotalPage");

		comHeadRightContent.addItem("selfdefine");
		comHeadRightContent.addItem("Date");
		comHeadRightContent.addItem("Page");
		comHeadRightContent.addItem("TotalPage");

		paperType.addItem("A4:210mm X 297mm");
		paperType.addItem("A5:148mm X 210mm");
		paperType.addItem("B5:182mm X 257mm");
		paperType.addItem("Devolop C5:162mm X 229mm");
		paperType.addItem("Devolop DL:110mm X 220mm");
		paperType.addItem("Devolop B5:176mm X 250mm");
		paperType.addItem("Devolop Monarch:3.875inch X 7.5inch");
		paperType.addItem("Devolop 9:3.875inch X 8.875inch");
		paperType.addItem("Devolop 10:4.125inch X 9.5inch");
		paperType.addItem("Letter:8.5inch X 11inch");
		paperType.addItem("Legal:8.5inch X 14inch");
		paperType.addItem("self define ...");

		//Layout  
		this.setLayout(thisLayout);

		txtJobName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tJobName_keyReleased(e);
			}
		});
		this.add(mainPanel, BorderLayout.CENTER);

		mainPanelLayout.setHgap(10);
		mainPanelLayout.setVgap(10);
		mainPanelLayout.setRows(2);
		mainPanelLayout.setColumns(3);

		//Layout  
		mainPanel.setLayout(mainPanelLayout);

		//Layout  
		mainPanel.add(paperPanel, null);
		mainPanel.add(sizePanel, null);
		mainPanel.add(otherPanel, null);
		mainPanel.add(marginPanel, null);
		mainPanel.add(headInfoPanel, null);
		mainPanel.add(footInfoPanel, null);

		//Layout  
		paperPanel.setLayout(paperPanelLayout);
		paperPanel.add(paperComponent, BorderLayout.CENTER);

		lblSettingMargin.setText("Set Margin:");
		lblSettingMargin.setBounds(new Rectangle(37, 13, 102, 25));
		txtTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtTopMargin_keyReleased(e);
			}
		});

		txtBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtBottomMargin_keyReleased(e);
			}
		});

		txtLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtLeftMargin_keyReleased(e);
			}
		});

		txtRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtRightMargin_keyReleased(e);
			}
		});

		txtPaperWidth.setEnabled(false);
		txtPaperWidth.setBounds(new Rectangle(48, 50, 33, 19));
		txtPaperWidth.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtPaperWidth_keyReleased(e);
			}
		});

		txtPaperHeight.setEnabled(false);
		txtPaperHeight.setBounds(new Rectangle(138, 50, 33, 19));
		txtPaperHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtPaperHeightFootLeftContentHeadHeight_keyReleased(e);
			}
		});

		paperType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paperType_actionPerformed(e);
			}
		});
		paperType.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				paperType_mouseClicked(e);
			}
		});

		otherPanel.setLayout(null);

		landscape.setText("landscape orientation ");
		landscape.setBounds(new Rectangle(30, 80, 147, 26));
		landscape.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				landscape_actionPerformed(e);
			}
		});

		portrait.setText("portrait orientation ");
		portrait.setBounds(new Rectangle(30, 112, 127, 26));
		portrait.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portraitFootLeftContentHeadHeight_actionPerformed(e);
			}
		});

		mm16.setBounds(new Rectangle(100, 69, 22, 18));
		mm16.setEnabled(false);
		mm16.setText("mm");
		lHeadRightContent.setBounds(new Rectangle(14, 139, 67, 18));
		lHeadRightContent.setEnabled(false);
		lHeadRightContent.setText("R Content:");
		lHeadRightMargin.setEnabled(false);
		lHeadRightMargin.setText("R:");
		lHeadRightMargin.setBounds(new Rectangle(115, 40, 27, 18));
		mm13.setEnabled(false);
		mm13.setText("mm");
		mm13.setBounds(new Rectangle(67, 40, 22, 18));
		mm12.setEnabled(false);
		mm12.setText("mm");
		mm12.setBounds(new Rectangle(67, 10, 22, 18));
		mm14.setEnabled(false);
		mm14.setText("mm");
		mm14.setBounds(new Rectangle(170, 10, 22, 18));
		mm15.setEnabled(false);
		mm15.setText("mm");
		mm15.setBounds(new Rectangle(170, 41, 22, 18));
		lHeadLeftContent.setBounds(new Rectangle(14, 98, 63, 18));
		lHeadLeftContent.setEnabled(false);
		lHeadLeftContent.setText("L Content:");
		lHeadLeftMargin.setEnabled(false);
		lHeadLeftMargin.setText("L:");
		lHeadLeftMargin.setBounds(new Rectangle(115, 10, 19, 18));
		txtHeadRightContent.setEnabled(false);
		txtHeadRightContent.setBounds(new Rectangle(125, 136, 69, 20));
		txtHeadRightContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadRightContent_keyReleased(e);
			}
		});
		txtHeadMidContent.setEnabled(false);
		txtHeadMidContent.setBounds(new Rectangle(125, 117, 69, 20));
		txtHeadMidContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadMidContent_keyReleased(e);
			}
		});
		txtHeadLeftContent.setEnabled(false);
		txtHeadLeftContent.setBounds(new Rectangle(125, 98, 69, 20));
		txtHeadLeftContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadLeftContent_keyReleased(e);
			}
		});
		txtHeadHeight.setEnabled(false);
		txtHeadHeight.setBounds(new Rectangle(59, 68, 33, 20));
		txtHeadHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadHeight_keyReleased(e);
			}
		});
		txtHeadRightMargin.setBounds(new Rectangle(138, 8, 33, 20));
		txtHeadRightMargin.setEnabled(false);
		txtHeadRightMargin.setBounds(new Rectangle(130, 40, 33, 20));
		txtHeadRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadRightMargin_keyReleased(e);
			}
		});
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		lHeadHeight.setBounds(new Rectangle(14, 68, 47, 18));
		lHeadHeight.setEnabled(false);
		lHeadHeight.setText("Height:");
		txtHeadBottomMargin.setBounds(new Rectangle(138, 38, 33, 20));
		txtHeadBottomMargin.setEnabled(false);
		txtHeadBottomMargin.setBounds(new Rectangle(30, 37, 33, 20));
		txtHeadBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadBottomMargin_keyReleased(e);
			}
		});
		comPageHead.addItem("All Page");
		comPageHead.addItem("First Page");
		comPageHead.addItem("None");
		comPageHead.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comPageHead_mouseClicked(e);
			}
		});
		comPageHead.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comPageHead_actionPerformed(e);
			}
		});

		checkBoxSetFoot.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				checkBoxSetFoot_stateChanged(e);
			}
		});
		lblHeadTopMargin.setEnabled(false);
		lblHeadTopMargin.setText("T:");
		lblHeadTopMargin.setBounds(new Rectangle(14, 11, 24, 18));
		lblHeadMidContent.setBounds(new Rectangle(14, 118, 66, 18));
		lblHeadMidContent.setEnabled(false);
		lblHeadMidContent.setText("M Content:");
		txtHeadLeftMargin.setBounds(new Rectangle(31, 42, 33, 20));
		txtHeadLeftMargin.setEnabled(false);
		txtHeadLeftMargin.setBounds(new Rectangle(131, 11, 33, 20));
		txtHeadLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadLeftMargin_keyReleased(e);
			}
		});
		lHeadBottomMargin.setEnabled(false);
		lHeadBottomMargin.setText("B:");
		lHeadBottomMargin.setBounds(new Rectangle(14, 41, 22, 18));
		txtHeadTopMargin.setEnabled(false);
		txtHeadTopMargin.setBounds(new Rectangle(31, 10, 33, 20));
		txtHeadTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtHeadTopMargin_keyReleased(e);
			}
		});
		showHeadPanel.setLayout(flowLayout1);
		headPanel.setLayout(null);
		headInfoPanel.setBorder(BorderFactory.createEtchedBorder());
		headInfoPanel.setLayout(borderLayout1);
		comHeadRightContent.setEnabled(false);
		comHeadRightContent.setBounds(new Rectangle(75, 138, 51, 20));
		comHeadRightContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comHeadRightContent_actionPerformed(e);
			}
		});
		comHeadMidContent.setEnabled(false);
		comHeadMidContent.setBounds(new Rectangle(75, 117, 51, 20));
		comHeadMidContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comHeadMidContent_actionPerformed(e);
			}
		});
		comHeadLeftContent.setEnabled(false);
		comHeadLeftContent.setBounds(new Rectangle(75, 98, 51, 20));
		comHeadLeftContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comHeadLeftContent_actionPerformed(e);
			}
		});
		mm11.setEnabled(false);
		mm11.setText("mm");
		mm11.setBounds(new Rectangle(100, 69, 22, 18));
		lblFootRightContent.setEnabled(false);
		lblFootRightContent.setText("R Content:");
		lblFootRightContent.setBounds(new Rectangle(14, 136, 67, 18));
		lblFootRightMargin.setBounds(new Rectangle(115, 40, 27, 18));
		lblFootRightMargin.setEnabled(false);
		lblFootRightMargin.setText("R:");
		mm8.setBounds(new Rectangle(67, 40, 22, 18));
		mm8.setEnabled(false);
		mm8.setText("mm");
		mm7.setBounds(new Rectangle(67, 10, 22, 18));
		mm7.setEnabled(false);
		mm7.setText("mm");
		mm9.setBounds(new Rectangle(178, 10, 22, 18));
		mm9.setEnabled(false);
		mm9.setText("mm");
		mm10.setBounds(new Rectangle(178, 41, 22, 18));
		mm10.setEnabled(false);
		mm10.setText("mm");
		lblFootLeftContent.setEnabled(false);
		lblFootLeftContent.setText("L Content:");
		lblFootLeftContent.setBounds(new Rectangle(14, 98, 63, 18));
		lblFootLeftMargin.setBounds(new Rectangle(115, 10, 19, 18));
		lblFootLeftMargin.setEnabled(false);
		lblFootLeftMargin.setText("L:");
		txtFootRightContent.setEnabled(false);
		txtFootRightContent.setBounds(new Rectangle(127, 136, 69, 20));
		txtFootRightContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootRightContent_keyReleased(e);
			}
		});
		txtFootMidContent.setEnabled(false);
		txtFootMidContent.setBounds(new Rectangle(127, 117, 69, 20));
		txtFootMidContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootMidContent_keyReleased(e);
			}
		});
		txtFootLeftContent.setEnabled(false);
		txtFootLeftContent.setBounds(new Rectangle(127, 98, 69, 20));
		txtFootLeftContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootLeftContent_keyReleased(e);
			}
		});
		txtFootHeight.setEnabled(false);
		txtFootHeight.setBounds(new Rectangle(59, 68, 33, 20));
		txtFootHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootHeight_keyReleased(e);
			}
		});
		txtFootLeftMargin.setBounds(new Rectangle(130, 40, 33, 20));
		txtFootLeftMargin.setEnabled(false);
		txtFootLeftMargin.setBounds(new Rectangle(138, 8, 33, 20));
		txtFootLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootLeftMargin_keyReleased(e);
			}
		});
		lblFootHeight.setEnabled(false);
		lblFootHeight.setText("Height:");
		lblFootHeight.setBounds(new Rectangle(14, 68, 47, 18));
		showFootPanelLayout.setVgap(0);
		showFootPanelLayout.setHgap(0);
		txtFootRightMargin.setBounds(new Rectangle(30, 37, 33, 20));
		txtFootRightMargin.setEnabled(false);
		txtFootRightMargin.setBounds(new Rectangle(138, 38, 33, 20));
		txtFootRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootRightMargin_keyReleased(e);
			}
		});
		lblFootMidContent.setEnabled(false);
		lblFootMidContent.setText("M Content:");
		lblFootMidContent.setBounds(new Rectangle(14, 117, 66, 18));
		lblFootTopMargin.setBounds(new Rectangle(14, 11, 24, 18));
		lblFootTopMargin.setEnabled(false);
		lblFootTopMargin.setText("T:");
		checkBoxSetFoot.setText("Enable Foot");
		checkBoxSetFoot.setHorizontalAlignment(SwingConstants.LEADING);
		checkBoxSetFoot.setActionMap(null);
		txtFootBottomMargin.setBounds(new Rectangle(131, 11, 33, 20));
		txtFootBottomMargin.setEnabled(false);
		txtFootBottomMargin.setBounds(new Rectangle(31, 42, 33, 20));
		txtFootBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootBottomMargin_keyReleased(e);
			}
		});
		lblFootBottomMargin.setBounds(new Rectangle(14, 41, 22, 18));
		lblFootBottomMargin.setEnabled(false);
		lblFootBottomMargin.setText("B:");
		txtFootTopMargin.setEnabled(false);
		txtFootTopMargin.setBounds(new Rectangle(31, 10, 33, 20));
		txtFootTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtFootTopMargin_keyReleased(e);
			}
		});
		showFootPanel.setLayout(showFootPanelLayout);
		footPanel.setLayout(null);
		footInfoPanel.setBorder(BorderFactory.createEtchedBorder());
		footInfoPanel.setLayout(footInfoPanelLayout);
		comFootRightContent.setEnabled(false);
		comFootRightContent.setBounds(new Rectangle(76, 136, 51, 20));
		comFootRightContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comFootRightContent_actionPerformed(e);
			}
		});
		comFootMidContent.setEnabled(false);
		comFootMidContent.setBounds(new Rectangle(76, 117, 51, 20));
		comFootMidContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comFootMidContent_actionPerformed(e);
			}
		});
		comFootLeftContent.setEnabled(false);
		comFootLeftContent.setBounds(new Rectangle(76, 98, 51, 20));
		comFootLeftContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comFootLeftContent_actionPerformed(e);
			}
		});
		otherPanel.setBorder(BorderFactory.createEtchedBorder());
		txtRightMargin.setBounds(new Rectangle(87, 143, 55, 22));
		mm4.setBounds(new Rectangle(147, 143, 22, 18));
		lblRightMargin.setBounds(new Rectangle(50, 143, 27, 18));
		txtLeftMargin.setBounds(new Rectangle(87, 113, 55, 22));
		mm3.setBounds(new Rectangle(147, 113, 22, 18));
		lblLeftMargin.setBounds(new Rectangle(58, 113, 19, 18));
		txtBottomMargin.setBounds(new Rectangle(87, 82, 55, 22));
		mm2.setBounds(new Rectangle(147, 82, 22, 18));
		lblBottomMargin.setBounds(new Rectangle(35, 82, 42, 18));
		mm1.setBounds(new Rectangle(147, 52, 22, 18));
		txtTopMargin.setBounds(new Rectangle(87, 50, 55, 22));
		lblTopMargin.setBounds(new Rectangle(53, 52, 24, 18));
		mm6.setBounds(new Rectangle(172, 50, 22, 18));
		lblPaperHeight.setBounds(new Rectangle(120, 50, 33, 18));
		mm5.setBounds(new Rectangle(84, 50, 22, 18));
		lblPaperWidth.setBounds(new Rectangle(29, 50, 33, 18));
		paperType.setBounds(new Rectangle(28, 20, 166, 22));
		lblTableHeadModel.setText("Table Head:");
		lblTableHeadModel.setBounds(new Rectangle(20, 12, 79, 24));
		lblTableAlign.setBounds(new Rectangle(114, 13, 97, 24));
		lblTableAlign.setText("Table Alignment:");
		lblTableScale.setText("Table Scale:");
		lblTableScale.setBounds(new Rectangle(22, 158, 68, 27));

		txtTableScale.setBounds(new Rectangle(97, 160, 55, 20));
		txtTableScale.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				txtTableScale_keyReleased(e);
			}
		});

		comTableHead.setBounds(new Rectangle(17, 42, 71, 22));
		comTableHead.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comTableHead_mouseClicked(e);
			}
		});
		comTableHead.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comTableHead_actionPerformed(e);
			}
		});

		comTableAlign.setBounds(new Rectangle(116, 43, 78, 22));
		comTableAlign.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comTableAlign_mouseClicked(e);
			}
		});
		comTableAlign.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comTableAlign_actionPerformed(e);
			}
		});

		lblTableHeadFootBorder.setBounds(new Rectangle(16, 71, 147, 24));
		lblTableHeadFootBorder.setText("Table Head/Foot Border:");

		comTableHeadBorder.setBounds(new Rectangle(128, 100, 71, 22));
		comTableHeadBorder.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comTableHeadBorder_mouseClicked(e);
			}
		});
		comTableHeadBorder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comTableHeadBorder_actionPerformed(e);
			}
		});

		comTableFootBorder.setBounds(new Rectangle(128, 132, 71, 22));
		comTableFootBorder.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				comTableFootBorder_mouseClicked(e);
			}
		});
		comTableFootBorder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comTableFootBorder_actionPerformed(e);
			}
		});

		lblTableHeadBorder.setText("Table Head Border:");
		lblTableHeadBorder.setBounds(new Rectangle(16, 99, 113, 24));
		lblTableFootBorder.setBounds(new Rectangle(16, 130, 113, 24));
		lblTableFootBorder.setText("Table Foot Border:");
		lblJobName.setText("Job Name:");
		lblJobName.setBounds(new Rectangle(25, 155, 63, 25));
		txtJobName.setBounds(new Rectangle(94, 156, 97, 24));

		footInfoPanel.add(showFootPanel, BorderLayout.NORTH);
		showFootPanel.add(checkBoxSetFoot, null);
		footInfoPanel.add(footPanel, BorderLayout.CENTER);
		footPanel.add(lblFootTopMargin, null);
		footPanel.add(lblFootHeight, null);
		footPanel.add(lblFootBottomMargin, null);
		footPanel.add(txtFootTopMargin, null);
		footPanel.add(mm7, null);
		footPanel.add(mm8, null);
		footPanel.add(comFootLeftContent, null);
		footPanel.add(lblFootLeftContent, null);
		footPanel.add(txtFootRightMargin, null);
		footPanel.add(txtFootHeight, null);
		footPanel.add(mm11, null);
		footPanel.add(lblFootLeftMargin, null);
		footPanel.add(txtFootBottomMargin, null);
		footPanel.add(txtFootLeftMargin, null);
		footPanel.add(lblFootRightMargin, null);
		footPanel.add(mm10, null);
		footPanel.add(mm9, null);
		footPanel.add(txtFootLeftContent, null);
		footPanel.add(lblFootMidContent, null);
		footPanel.add(lblFootRightContent, null);
		footPanel.add(comFootMidContent, null);
		footPanel.add(txtFootMidContent, null);
		footPanel.add(txtFootRightContent, null);
		footPanel.add(comFootRightContent, null);

		headInfoPanel.add(showHeadPanel, BorderLayout.NORTH);
		showHeadPanel.add(comPageHead, null);
		headInfoPanel.add(headPanel, BorderLayout.CENTER);
		headPanel.add(lblHeadTopMargin, null);
		headPanel.add(lHeadHeight, null);
		headPanel.add(lHeadBottomMargin, null);
		headPanel.add(txtHeadTopMargin, null);
		headPanel.add(mm12, null);
		headPanel.add(mm13, null);
		headPanel.add(comHeadLeftContent, null);
		headPanel.add(lHeadLeftContent, null);
		headPanel.add(txtHeadBottomMargin, null);
		headPanel.add(txtHeadHeight, null);
		headPanel.add(mm16, null);
		headPanel.add(txtHeadLeftContent, null);
		headPanel.add(lHeadLeftMargin, null);
		headPanel.add(txtHeadLeftMargin, null);
		headPanel.add(mm14, null);
		headPanel.add(mm15, null);
		headPanel.add(txtHeadRightMargin, null);
		headPanel.add(lHeadRightMargin, null);
		headPanel.add(txtHeadMidContent, null);
		headPanel.add(comHeadMidContent, null);
		headPanel.add(lblHeadMidContent, null);
		headPanel.add(lHeadRightContent, null);
		headPanel.add(comHeadRightContent, null);
		headPanel.add(txtHeadRightContent, null);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(northPanel, BorderLayout.NORTH);

		//Layout  
		marginPanel.setBorder(BorderFactory.createEtchedBorder());
		marginPanel.setLayout(null);

		lblTopMargin.setText("Top:");
		mm1.setText("mm");

		lblBottomMargin.setText("Bottom:");
		mm2.setText("mm");

		lblLeftMargin.setText("left:");
		mm3.setText("mm");

		lblRightMargin.setText("right:");
		mm4.setText("mm");
		marginPanel.add(mm1, null);
		marginPanel.add(mm4, null);
		marginPanel.add(txtRightMargin, null);
		marginPanel.add(lblRightMargin, null);
		marginPanel.add(lblLeftMargin, null);
		marginPanel.add(txtLeftMargin, null);
		marginPanel.add(mm3, null);
		marginPanel.add(mm2, null);
		marginPanel.add(txtBottomMargin, null);
		marginPanel.add(lblBottomMargin, null);
		marginPanel.add(lblTopMargin, null);
		marginPanel.add(txtTopMargin, null);
		marginPanel.add(lblSettingMargin, null);

		//Layout  
		sizePanel.setBorder(BorderFactory.createEtchedBorder());
		sizePanel.setLayout(null);

		lblPaperWidth.setText("W:");
		mm5.setText("mm");

		lblPaperHeight.setText("H:");
		mm6.setText("mm");
		sizePanel.add(paperType, null);
		sizePanel.add(lblPaperWidth, null);
		sizePanel.add(txtPaperWidth, null);
		sizePanel.add(txtPaperHeight, null);
		sizePanel.add(lblPaperHeight, null);
		sizePanel.add(mm5, null);
		sizePanel.add(mm6, null);
		sizePanel.add(landscape, null);
		sizePanel.add(portrait, null);
		sizePanel.add(lblJobName, null);

		//Layout  
		comTableHead.addItem("All Page");
		comTableHead.addItem("First Page");
		comTableHead.addItem("None");

		//Layout  
		comTableAlign.addItem("Left");
		comTableAlign.addItem("Middle");
		comTableAlign.addItem("Right");

		//Layout  
		comTableHeadBorder.addItem("Line");
		comTableHeadBorder.addItem("Box");
		comTableHeadBorder.addItem("None");

		//Layout  
		comTableFootBorder.addItem("Line");
		comTableFootBorder.addItem("Box");
		comTableFootBorder.addItem("None");

		//Layout  
		otherPanel.add(comTableHead, null);
		otherPanel.add(lblTableHeadModel, null);
		otherPanel.add(txtTableScale, null);
		otherPanel.add(lblTableScale, null);
		otherPanel.add(lblTableHeadFootBorder, null);
		otherPanel.add(lblTableHeadBorder, null);
		otherPanel.add(lblTableFootBorder, null);
		otherPanel.add(comTableFootBorder, null);
		otherPanel.add(lblTableAlign, null);
		otherPanel.add(comTableAlign, null);
		otherPanel.add(comTableHeadBorder, null);
		sizePanel.add(txtJobName, null);
	}

	void landscape_actionPerformed(ActionEvent e) {
		pageFormat.setOrientation(0);

		txtTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		txtBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		txtLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		txtRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		txtPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		txtPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

		repaint();
	}

	void portraitFootLeftContentHeadHeight_actionPerformed(ActionEvent e) {
		pageFormat.setOrientation(1);

		txtTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		txtBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		txtLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		txtRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		txtPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		txtPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

		repaint();
	}

	void txtTopMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((txtTopMargin.getText().equals("") ? 0.0 : Double.parseDouble(txtTopMargin.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (landscape.isSelected()) {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			w = w + x - newIn;
			x = newIn;

			newPaper.setImageableArea(x, y, w, h);
		} else {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			h = h + y - newIn;
			y = newIn;

			newPaper.setImageableArea(x, y, w, h);
		}
		pageFormat.setPaper(newPaper);

		repaint();
	}

	void txtBottomMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((txtBottomMargin.getText().equals("") ? 0.0 : Double.parseDouble(txtBottomMargin.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (landscape.isSelected()) {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			w = newPaper.getWidth() - x - newIn;

			newPaper.setImageableArea(x, y, w, h);
		} else {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			h = newPaper.getHeight() - y - newIn;

			newPaper.setImageableArea(x, y, w, h);
		}
		pageFormat.setPaper(newPaper);

		repaint();
	}

	void txtLeftMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((txtLeftMargin.getText().equals("") ? 0.0 : Double.parseDouble(txtLeftMargin.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (landscape.isSelected()) {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			h = y + h - newIn;
			y = newIn;

			newPaper.setImageableArea(x, y, w, h);
		} else {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			w = x + w - newIn;
			x = newIn;

			newPaper.setImageableArea(x, y, w, h);
		}
		pageFormat.setPaper(newPaper);

		repaint();
	}

	void txtRightMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((txtRightMargin.getText().equals("") ? 0.0 : Double.parseDouble(txtRightMargin.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (landscape.isSelected()) {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			h = newPaper.getHeight() - y - newIn;

			newPaper.setImageableArea(x, y, w, h);
		} else {
			x = newPaper.getImageableX();
			y = newPaper.getImageableY();
			w = newPaper.getImageableWidth();
			h = newPaper.getImageableHeight();

			w = newPaper.getWidth() - x - newIn;

			newPaper.setImageableArea(x, y, w, h);
		}
		pageFormat.setPaper(newPaper);

		repaint();
	}

	void paperType_actionPerformed(ActionEvent e) {
		if (!isDirectClicked)
			return;

		int index = paperType.getSelectedIndex();

		switch (index) {
		case 0:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getA4());
			repaint();
			break;
		case 1:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getA5());
			repaint();
			break;
		case 2:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getB5());
			repaint();
			break;
		case 3:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopC5());
			repaint();
			break;
		case 4:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopDl());
			repaint();
			break;
		case 5:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopB5());
			repaint();
			break;
		case 6:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopMonarch());
			repaint();
			break;
		case 7:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelop9());
			repaint();
			break;
		case 8:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelop10());
			repaint();
			break;
		case 9:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getLetter());
			repaint();
			break;
		case 10:
			txtPaperWidth.setEnabled(false);
			txtPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getLegal());
			repaint();
			break;
		default:
			txtPaperWidth.setEnabled(true);
			txtPaperHeight.setEnabled(true);
			repaint();
			break;
		}
		txtTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		txtBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		txtLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		txtRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		txtPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		txtPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));
	}

	void txtPaperWidth_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double pw, ph;
		//		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((txtPaperWidth.getText().equals("") ? 0.0 : Double.parseDouble(txtPaperWidth.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (pageFormat.getOrientation() == 1) {
			pw = newIn;
			ph = newPaper.getHeight();

			newPaper.setImageableArea(newPaper.getImageableX(), newPaper.getImageableY(), newPaper.getImageableWidth() + newIn - newPaper.getWidth(),
					newPaper.getImageableHeight());

			newPaper.setSize(pw, ph);
		} else {
			ph = newIn;
			pw = newPaper.getWidth();

			newPaper.setImageableArea(newPaper.getImageableX(), newPaper.getImageableY(), newPaper.getImageableWidth(), newPaper.getImageableHeight()
					- newPaper.getHeight() + newIn);

			newPaper.setSize(pw, ph);
		}

		pageFormat.setPaper(newPaper);

		repaint();
	}

	void txtPaperHeightFootLeftContentHeadHeight_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double ph, pw;
		double text;
		double newIn;

		try {
			text = ((txtPaperHeight.getText().equals("") ? 0.0 : Double.parseDouble(txtPaperHeight.getText())));
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		if (pageFormat.getOrientation() == 1) {
			ph = newIn;
			pw = newPaper.getWidth();

			newPaper.setImageableArea(newPaper.getImageableX(), newPaper.getImageableY(), newPaper.getImageableWidth(), newPaper.getImageableHeight()
					- newPaper.getHeight() + newIn);

			newPaper.setSize(pw, ph);
		} else {
			pw = newIn;
			ph = newPaper.getHeight();

			newPaper.setImageableArea(newPaper.getImageableX(), newPaper.getImageableY(), newPaper.getImageableWidth() + newIn - newPaper.getWidth(),
					newPaper.getImageableHeight());

			newPaper.setSize(pw, ph);
		}

		pageFormat.setPaper(newPaper);

		repaint();
	}

	void paperType_mouseClicked(MouseEvent e) {
		isDirectClicked = true;
	}

	ExtPageFormat getPageFormat() {
		return this.pageFormat;
	}

	void checkBoxSetFoot_stateChanged(ChangeEvent e) {
		pageFormat.setShowFoot(checkBoxSetFoot.isSelected());

		Component[] component = this.footPanel.getComponents();
		for (int index = 0; index < component.length; index++)
			component[index].setEnabled(checkBoxSetFoot.isSelected());

		repaint();
	}

	int getHeaderDisplayType(JComboBox comHead) {
		int index = comHead.getSelectedIndex();

		switch (index) {
		case 0:
			return ExtPageFormat.HEADER_DISPLAY_ALL_PAGE;
		case 1:
			return ExtPageFormat.HEADER_DISPLAY_FIRST_PAGE;
		case 2:
			return ExtPageFormat.HEADER_DISPLAY_NONE;
		default:
			return ExtPageFormat.HEADER_DISPLAY_NONE;
		}
	}

	void comPageHead_actionPerformed(ActionEvent e) {
//		if (!isPageHeadClicked)
//			return;

		pageFormat.setPageHeadDisplayType(getHeaderDisplayType(comPageHead));

		boolean isShow = pageFormat.getPageHeadDisplayType() != ExtPageFormat.HEADER_DISPLAY_NONE;

		Component[] component = this.headPanel.getComponents();
		for (int index = 0; index < component.length; index++)
			component[index].setEnabled(isShow);

		repaint();
	}

	void txtTableScale_keyReleased(KeyEvent e) {
		try {
			pageFormat.setTableScale(Integer.parseInt(txtTableScale.getText()));
		} catch (NumberFormatException nfe) {
			return;
		}
	}

	//set foot infomation  
	void txtFootTopMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtFootTopMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtFootTopMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootImageableHeight() + pageFormat.getFootImageableY() - newIn);
		pageFormat.setFootImageableY(newIn);

		repaint();
	}

	void txtFootBottomMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtFootBottomMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtFootBottomMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootHeight() - pageFormat.getFootImageableY() - newIn);

		repaint();
	}

	void txtFootLeftMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtFootLeftMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtFootLeftMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableWidth(pageFormat.getFootImageableWidth() + pageFormat.getFootImageableX() - newIn);
		pageFormat.setFootImageableX(newIn);

		repaint();
	}

	void txtFootRightMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtFootRightMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtFootRightMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableWidth(pageFormat.getFootWidth() - pageFormat.getFootImageableX() - newIn);

		repaint();
	}

	void txtFootHeight_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtFootHeight.getText().equals("")) ? 0.0 : Double.parseDouble(txtFootHeight.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootImageableHeight() - pageFormat.getFootHeight() + newIn);
		pageFormat.setFootHeight(newIn);

		repaint();
	}

	void txtFootLeftContent_keyReleased(KeyEvent e) {
		pageFormat.setFootLeftContent(txtFootLeftContent.getText());
	}

	void comFootLeftContent_actionPerformed(ActionEvent e) {
		pageFormat.setFootLeftContent(comContent_actionPerformed(e, comFootLeftContent, txtFootLeftContent));
	}

	void txtFootMidContent_keyReleased(KeyEvent e) {
		pageFormat.setFootMidContent(txtFootMidContent.getText());
	}

	void comFootMidContent_actionPerformed(ActionEvent e) {
		pageFormat.setFootMidContent(comContent_actionPerformed(e, comFootMidContent, txtFootMidContent));
	}

	void txtFootRightContent_keyReleased(KeyEvent e) {
		pageFormat.setFootRightContent(txtFootRightContent.getText());
	}

	void comFootRightContent_actionPerformed(ActionEvent e) {
		pageFormat.setFootRightContent(comContent_actionPerformed(e, comFootRightContent, txtFootRightContent));
	}

	//set head infomation  
	void txtHeadTopMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtHeadTopMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtHeadTopMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.getHead().setImageableHeight(pageFormat.getHead().getImageableHeight() + pageFormat.getHead().getImageableY() - newIn);
		pageFormat.getHead().setImageableY(newIn);

		repaint();
	}

	void txtHeadBottomMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtHeadBottomMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtHeadBottomMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.getHead().setImageableHeight(pageFormat.getHead().getHeight() - pageFormat.getHead().getImageableY() - newIn);

		repaint();
	}

	void txtHeadLeftMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtHeadLeftMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtHeadLeftMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.getHead().setImageableWidth(pageFormat.getHead().getImageableWidth() + pageFormat.getHead().getImageableX() - newIn);
		pageFormat.getHead().setImageableX(newIn);

		repaint();
	}

	void txtHeadRightMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtHeadRightMargin.getText().equals("")) ? 0.0 : Double.parseDouble(txtHeadRightMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.getHead().setImageableWidth(pageFormat.getHead().getWidth() - pageFormat.getHead().getImageableX() - newIn);

		repaint();
	}

	void txtHeadHeight_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (txtHeadHeight.getText().equals("")) ? 0.0 : Double.parseDouble(txtHeadHeight.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.getHead().setImageableHeight(pageFormat.getHead().getImageableHeight() - pageFormat.getHead().getHeight() + newIn);
		pageFormat.getHead().setHeight(newIn);

		repaint();
	}

	void tHeadLeftContent_keyReleased(KeyEvent e) {
		pageFormat.getHead().setLeftContent(txtHeadLeftContent.getText());
	}

	void comHeadLeftContent_actionPerformed(ActionEvent e) {
		pageFormat.getHead().setLeftContent(comContent_actionPerformed(e, comHeadLeftContent, txtHeadLeftContent));
	}

	void txtHeadMidContent_keyReleased(KeyEvent e) {
		pageFormat.getHead().setMidContent(txtHeadMidContent.getText());
	}

	String comContent_actionPerformed(ActionEvent e, JComboBox comContent, JTextField txtMidContent) {
		if (comContent.getSelectedIndex() == 0)
			return txtMidContent.getText();
		else if (comContent.getSelectedIndex() == 1)
			txtMidContent.setText(txtMidContent.getText() + PageBorder.CONTENT_DATE);
		else if (comContent.getSelectedIndex() == 2)
			txtMidContent.setText(txtMidContent.getText() + PageBorder.CONTENT_CURRENT_PAGE);
		else
			txtMidContent.setText(txtMidContent.getText() + PageBorder.CONTENT_TOTAL_PAGE);

		return txtMidContent.getText();
	}

	void comHeadMidContent_actionPerformed(ActionEvent e) {
		pageFormat.getHead().setMidContent(comContent_actionPerformed(e, comHeadMidContent, txtHeadMidContent));
	}

	void txtHeadRightContent_keyReleased(KeyEvent e) {
		pageFormat.getHead().setRightContent(txtHeadRightContent.getText());
	}

	void comHeadRightContent_actionPerformed(ActionEvent e) {
		pageFormat.getHead().setRightContent(comContent_actionPerformed(e, comHeadRightContent, txtHeadRightContent));
	}

	void comTableHead_actionPerformed(ActionEvent e) {
		if (!isTableHeadClicked)
			return;
		pageFormat.setTableHeadDisplayType(getHeaderDisplayType(comTableHead));
	}

	void comTableAlign_actionPerformed(ActionEvent e) {
		if (!isTableAlignClicked)
			return;

		int index = comTableAlign.getSelectedIndex();

		switch (index) {
		case 0:
			pageFormat.setTableAlignment(ExtPageFormat.TABLE_ALIGN_LEFT);
			break;
		case 1:
			pageFormat.setTableAlignment(ExtPageFormat.TABLE_ALIGN_MID);
			break;
		case 2:
			pageFormat.setTableAlignment(ExtPageFormat.TABLE_ALIGN_RIGHT);
			break;
		default:
			break;
		}
	}

	int getBorderType(JComboBox comBorderType) {
		int index = comBorderType.getSelectedIndex();
		switch (index) {
		case 0:
			return PageBorder.BORDER_TOP_LINE;
		case 1:
			return PageBorder.BORDER_BOX;
		case 2:
			return PageBorder.BORDER_NONE;
		default:
			return PageBorder.BORDER_NONE;
		}
	}

	void comTableHeadBorder_actionPerformed(ActionEvent e) {
		if (!isTableHeadBorderClicked)
			return;
		pageFormat.getHead().setBorderType(getBorderType(comTableHeadBorder));
	}

	void comTableFootBorder_actionPerformed(ActionEvent e) {
		if (!isTableFootBorderClicked)
			return;

		pageFormat.setFootBorderType(getBorderType(comTableFootBorder));
	}

	void comPageHead_mouseClicked(MouseEvent e) {
		isPageHeadClicked = true;
	}

	void comTableHead_mouseClicked(MouseEvent e) {
		isTableHeadClicked = true;
	}

	void comTableAlign_mouseClicked(MouseEvent e) {
		isTableAlignClicked = true;
	}

	void comTableHeadBorder_mouseClicked(MouseEvent e) {
		isTableHeadBorderClicked = true;
	}

	void comTableFootBorder_mouseClicked(MouseEvent e) {
		isTableFootBorderClicked = true;
	}

	void tJobName_keyReleased(KeyEvent e) {
		pageFormat.setJobName(txtJobName.getText());
	}
}