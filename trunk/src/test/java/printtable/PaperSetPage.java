package printtable;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PaperSetPage extends JPanel {
	//declare if the combo is clicked  
	boolean isDirectClicked = false;
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

	JLabel lTopMargin = new JLabel();
	JTextField tTopMargin = new JTextField(5);

	JLabel lBottomMargin = new JLabel();
	JTextField tBottomMargin = new JTextField(5);

	JLabel lLeftMargin = new JLabel();
	JTextField tLeftMargin = new JTextField(5);

	JLabel lRightMargin = new JLabel();
	JTextField tRightMargin = new JTextField(5);

	//declare sizePanel where to set the size of the paper  
	JPanel sizePanel = new JPanel();

	JComboBox paperType = new JComboBox();

	JLabel lPaperWidth = new JLabel();
	JTextField tPaperWidth = new JTextField(3);

	JLabel lPaperHeight = new JLabel();
	JTextField tPaperHeight = new JTextField(3);

	//declare directPanel where to set the direct of the paper  

	ButtonGroup directChoiceGroup = new ButtonGroup();

	//declare the label with millimeter  
	JLabel mm1 = new JLabel();
	JLabel mm2 = new JLabel();
	JLabel mm3 = new JLabel();
	JLabel mm4 = new JLabel();
	JLabel mm5 = new JLabel();
	JLabel mm6 = new JLabel();
	JLabel lSettingMargin = new JLabel();
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
	JTextField tHeadRightContent = new JTextField(5);
	JTextField tHeadMidContent = new JTextField(5);
	JTextField tHeadLeftContent = new JTextField(5);
	JTextField tHeadHeight = new JTextField(5);
	JTextField tHeadRightMargin = new JTextField(5);
	FlowLayout flowLayout1 = new FlowLayout();
	JLabel lHeadHeight = new JLabel();
	JTextField tHeadBottomMargin = new JTextField(5);
	JCheckBox cSetHead = new JCheckBox();
	JLabel lHeadTopMargin = new JLabel();
	JLabel lHeadMidContent = new JLabel();
	JTextField tHeadLeftMargin = new JTextField(5);
	JLabel lHeadBottomMargin = new JLabel();
	JTextField tHeadTopMargin = new JTextField(5);
	JPanel showHeadPanel = new JPanel();
	JPanel headPanel = new JPanel();
	JPanel headInfoPanel = new JPanel();
	JComboBox sHeadRightContent = new JComboBox();
	JComboBox sHeadMidContent = new JComboBox();
	BorderLayout borderLayout1 = new BorderLayout();
	JComboBox sHeadLeftContent = new JComboBox();
	JLabel mm11 = new JLabel();
	JLabel lFootRightContent = new JLabel();
	JLabel lFootRightMargin = new JLabel();
	JLabel mm8 = new JLabel();
	JLabel mm7 = new JLabel();
	JLabel mm9 = new JLabel();
	JLabel mm10 = new JLabel();
	JLabel lFootLeftContent = new JLabel();
	JLabel lFootLeftMargin = new JLabel();
	JTextField tFootRightContent = new JTextField(5);
	JTextField tFootMidContent = new JTextField(5);
	JTextField tFootLeftContent = new JTextField(5);
	JTextField tFootHeight = new JTextField(5);
	JTextField tFootLeftMargin = new JTextField(5);
	JLabel lFootHeight = new JLabel();
	FlowLayout showFootPanelLayout = new FlowLayout();
	JTextField tFootRightMargin = new JTextField(5);
	JLabel lFootMidContent = new JLabel();
	JLabel lFootTopMargin = new JLabel();
	JCheckBox cSetFoot = new JCheckBox();
	JTextField tFootBottomMargin = new JTextField(5);
	JLabel lFootBottomMargin = new JLabel();
	JTextField tFootTopMargin = new JTextField(5);
	JPanel showFootPanel = new JPanel();
	JPanel footPanel = new JPanel();
	JPanel footInfoPanel = new JPanel();
	JComboBox sFootRightContent = new JComboBox();
	JComboBox sFootMidContent = new JComboBox();
	BorderLayout footInfoPanelLayout = new BorderLayout();
	JComboBox sFootLeftContent = new JComboBox();
	JLabel lTableHeadModel = new JLabel();
	JLabel lTableAlign = new JLabel();
	JLabel lTableScale = new JLabel();
	JTextField tTableScale = new JTextField();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	ButtonGroup tableHeadGroup = new ButtonGroup();
	ButtonGroup tableAlignGroup = new ButtonGroup();
	JComboBox sTableHead = new JComboBox();
	JComboBox sTableAlign = new JComboBox();
	JLabel lTableHeadFootBorder = new JLabel();
	JComboBox sTableHeadBorder = new JComboBox();
	JComboBox sTableFootBorder = new JComboBox();
	JLabel lTableHeadBorder = new JLabel();
	JLabel lTableFootBorder = new JLabel();
	JLabel lJobName = new JLabel();
	JTextField tJobName = new JTextField();

	public PaperSetPage(ExtPageFormat newPageFormat) {
		try {
			Paper paper = newPageFormat.getPaper();
			pageFormat = newPageFormat;

			paperComponent = new PaperComponent(pageFormat);
			paperComponent.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			jbInit();

			paperType.setSelectedIndex(PaperSetting.getIndex(paper.getWidth(), paper.getHeight()));

			//set paper margin  
			tTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
			tBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
					- pageFormat.getImageableHeight())) + 1));
			tLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
			tRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
					- pageFormat.getImageableWidth())) + 1));

			//set paper size  
			tPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
			tPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

			//set head margin  
			tHeadTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeadImageableY())) + 1));
			tHeadBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeadHeight() - pageFormat.getHeadImageableY()
					- pageFormat.getHeadImageableHeight())) + 1));
			tHeadLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableX())) + 1));
			tHeadRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeadWidth() - pageFormat.getHeadImageableX()
					- pageFormat.getHeadImageableWidth())) + 1));

			//set head height  
			tHeadHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeadHeight())) + 1));

			//set head content  
			tHeadLeftContent.setText(pageFormat.getHeadLeftContent());
			tHeadMidContent.setText(pageFormat.getHeadMidContent());
			tHeadRightContent.setText(pageFormat.getHeadRightContent());

			//set foot margin  
			tFootTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableY())) + 1));
			tFootBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootHeight() - pageFormat.getFootImageableY()
					- pageFormat.getFootImageableHeight())) + 1));
			tFootLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootImageableX())) + 1));
			tFootRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootWidth() - pageFormat.getFootImageableX()
					- pageFormat.getFootImageableWidth())) + 1));

			//set foot height  
			tFootHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFootHeight())) + 1));

			//set foot content  
			tFootLeftContent.setText(pageFormat.getFootLeftContent());
			tFootMidContent.setText(pageFormat.getFootMidContent());
			tFootRightContent.setText(pageFormat.getFootRightContent());

			//Set Table Head  
			if (pageFormat.getHeaderType() == 0)
				sTableHead.setSelectedIndex(0);
			else if (pageFormat.getHeaderType() == 1)
				sTableHead.setSelectedIndex(1);
			else
				sTableHead.setSelectedIndex(2);

			//set Table Align  
			if (pageFormat.getTableAlignment() == 0)
				sTableAlign.setSelectedIndex(0);
			else if (pageFormat.getTableAlignment() == 1)
				sTableAlign.setSelectedIndex(1);
			else
				sTableAlign.setSelectedIndex(2);

			//set table head border  
			if (pageFormat.getHeadBorderType() == 0)
				sTableHeadBorder.setSelectedIndex(0);
			else if (pageFormat.getHeadBorderType() == 1)
				sTableHeadBorder.setSelectedIndex(1);
			else
				sTableHeadBorder.setSelectedIndex(2);

			//set table foot border  
			if (pageFormat.getFootBorderType() == 0)
				sTableFootBorder.setSelectedIndex(0);
			else if (pageFormat.getFootBorderType() == 1)
				sTableFootBorder.setSelectedIndex(1);
			else
				sTableFootBorder.setSelectedIndex(2);

			this.directChoiceGroup.add(this.portrait);
			this.directChoiceGroup.add(this.landscape);

			//set paper direct  
			if (pageFormat.getOrientation() == 0)
				landscape.setSelected(true);
			else
				portrait.setSelected(true);

			tTableScale.setText("" + pageFormat.getTableScale());

			if (pageFormat.getShowHead())
				cSetHead.setSelected(true);
			else
				cSetHead.setSelected(false);

			if (pageFormat.getShowFoot())
				cSetFoot.setSelected(true);
			else
				cSetFoot.setSelected(false);

			tJobName.setText(pageFormat.getJobName());

			repaint();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		sFootLeftContent.addItem("selfdefine");
		sFootLeftContent.addItem("Date");
		sFootLeftContent.addItem("Page");
		sFootLeftContent.addItem("TotalPage");

		sFootMidContent.addItem("selfdefine");
		sFootMidContent.addItem("Date");
		sFootMidContent.addItem("Page");
		sFootMidContent.addItem("TotalPage");

		sFootRightContent.addItem("selfdefine");
		sFootRightContent.addItem("Date");
		sFootRightContent.addItem("Page");
		sFootRightContent.addItem("TotalPage");

		sHeadLeftContent.addItem("selfdefine");
		sHeadLeftContent.addItem("Date");
		sHeadLeftContent.addItem("Page");
		sHeadLeftContent.addItem("TotalPage");

		sHeadMidContent.addItem("selfdefine");
		sHeadMidContent.addItem("Date");
		sHeadMidContent.addItem("Page");
		sHeadMidContent.addItem("TotalPage");

		sHeadRightContent.addItem("selfdefine");
		sHeadRightContent.addItem("Date");
		sHeadRightContent.addItem("Page");
		sHeadRightContent.addItem("TotalPage");

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

		tJobName.addKeyListener(new java.awt.event.KeyAdapter() {
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

		lSettingMargin.setText("Set Margin:");
		lSettingMargin.setBounds(new Rectangle(37, 13, 102, 25));
		tTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tTopMargin_keyReleased(e);
			}
		});

		tBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tBottomMargin_keyReleased(e);
			}
		});

		tLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tLeftMargin_keyReleased(e);
			}
		});

		tRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tRightMargin_keyReleased(e);
			}
		});

		tPaperWidth.setEnabled(false);
		tPaperWidth.setBounds(new Rectangle(48, 50, 33, 19));
		tPaperWidth.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tPaperWidth_keyReleased(e);
			}
		});

		tPaperHeight.setEnabled(false);
		tPaperHeight.setBounds(new Rectangle(138, 50, 33, 19));
		tPaperHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tPaperHeightFootLeftContentHeadHeight_keyReleased(e);
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
		tHeadRightContent.setEnabled(false);
		tHeadRightContent.setBounds(new Rectangle(125, 136, 69, 20));
		tHeadRightContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadRightContent_keyReleased(e);
			}
		});
		tHeadMidContent.setEnabled(false);
		tHeadMidContent.setBounds(new Rectangle(125, 117, 69, 20));
		tHeadMidContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadMidContent_keyReleased(e);
			}
		});
		tHeadLeftContent.setEnabled(false);
		tHeadLeftContent.setBounds(new Rectangle(125, 98, 69, 20));
		tHeadLeftContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadLeftContent_keyReleased(e);
			}
		});
		tHeadHeight.setEnabled(false);
		tHeadHeight.setBounds(new Rectangle(59, 68, 33, 20));
		tHeadHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadHeight_keyReleased(e);
			}
		});
		tHeadRightMargin.setBounds(new Rectangle(138, 8, 33, 20));
		tHeadRightMargin.setEnabled(false);
		tHeadRightMargin.setBounds(new Rectangle(130, 40, 33, 20));
		tHeadRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadRightMargin_keyReleased(e);
			}
		});
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		lHeadHeight.setBounds(new Rectangle(14, 68, 47, 18));
		lHeadHeight.setEnabled(false);
		lHeadHeight.setText("Height:");
		tHeadBottomMargin.setBounds(new Rectangle(138, 38, 33, 20));
		tHeadBottomMargin.setEnabled(false);
		tHeadBottomMargin.setBounds(new Rectangle(30, 37, 33, 20));
		tHeadBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadBottomMargin_keyReleased(e);
			}
		});
		cSetHead.setActionMap(null);
		cSetHead.setHorizontalAlignment(SwingConstants.LEADING);
		cSetHead.setText("Enable Head");
		cSetHead.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cSetHead_stateChanged(e);
			}
		});
		cSetFoot.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cSetFoot_stateChanged(e);
			}
		});
		lHeadTopMargin.setEnabled(false);
		lHeadTopMargin.setText("T:");
		lHeadTopMargin.setBounds(new Rectangle(14, 11, 24, 18));
		lHeadMidContent.setBounds(new Rectangle(14, 118, 66, 18));
		lHeadMidContent.setEnabled(false);
		lHeadMidContent.setText("M Content:");
		tHeadLeftMargin.setBounds(new Rectangle(31, 42, 33, 20));
		tHeadLeftMargin.setEnabled(false);
		tHeadLeftMargin.setBounds(new Rectangle(131, 11, 33, 20));
		tHeadLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadLeftMargin_keyReleased(e);
			}
		});
		lHeadBottomMargin.setEnabled(false);
		lHeadBottomMargin.setText("B:");
		lHeadBottomMargin.setBounds(new Rectangle(14, 41, 22, 18));
		tHeadTopMargin.setEnabled(false);
		tHeadTopMargin.setBounds(new Rectangle(31, 10, 33, 20));
		tHeadTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tHeadTopMargin_keyReleased(e);
			}
		});
		showHeadPanel.setLayout(flowLayout1);
		headPanel.setLayout(null);
		headInfoPanel.setBorder(BorderFactory.createEtchedBorder());
		headInfoPanel.setLayout(borderLayout1);
		sHeadRightContent.setEnabled(false);
		sHeadRightContent.setBounds(new Rectangle(75, 138, 51, 20));
		sHeadRightContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sHeadRightContent_actionPerformed(e);
			}
		});
		sHeadMidContent.setEnabled(false);
		sHeadMidContent.setBounds(new Rectangle(75, 117, 51, 20));
		sHeadMidContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sHeadMidContent_actionPerformed(e);
			}
		});
		sHeadLeftContent.setEnabled(false);
		sHeadLeftContent.setBounds(new Rectangle(75, 98, 51, 20));
		sHeadLeftContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sHeadLeftContent_actionPerformed(e);
			}
		});
		mm11.setEnabled(false);
		mm11.setText("mm");
		mm11.setBounds(new Rectangle(100, 69, 22, 18));
		lFootRightContent.setEnabled(false);
		lFootRightContent.setText("R Content:");
		lFootRightContent.setBounds(new Rectangle(14, 136, 67, 18));
		lFootRightMargin.setBounds(new Rectangle(115, 40, 27, 18));
		lFootRightMargin.setEnabled(false);
		lFootRightMargin.setText("R:");
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
		lFootLeftContent.setEnabled(false);
		lFootLeftContent.setText("L Content:");
		lFootLeftContent.setBounds(new Rectangle(14, 98, 63, 18));
		lFootLeftMargin.setBounds(new Rectangle(115, 10, 19, 18));
		lFootLeftMargin.setEnabled(false);
		lFootLeftMargin.setText("L:");
		tFootRightContent.setEnabled(false);
		tFootRightContent.setBounds(new Rectangle(127, 136, 69, 20));
		tFootRightContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootRightContent_keyReleased(e);
			}
		});
		tFootMidContent.setEnabled(false);
		tFootMidContent.setBounds(new Rectangle(127, 117, 69, 20));
		tFootMidContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootMidContent_keyReleased(e);
			}
		});
		tFootLeftContent.setEnabled(false);
		tFootLeftContent.setBounds(new Rectangle(127, 98, 69, 20));
		tFootLeftContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootLeftContent_keyReleased(e);
			}
		});
		tFootHeight.setEnabled(false);
		tFootHeight.setBounds(new Rectangle(59, 68, 33, 20));
		tFootHeight.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootHeight_keyReleased(e);
			}
		});
		tFootLeftMargin.setBounds(new Rectangle(130, 40, 33, 20));
		tFootLeftMargin.setEnabled(false);
		tFootLeftMargin.setBounds(new Rectangle(138, 8, 33, 20));
		tFootLeftMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootLeftMargin_keyReleased(e);
			}
		});
		lFootHeight.setEnabled(false);
		lFootHeight.setText("Height:");
		lFootHeight.setBounds(new Rectangle(14, 68, 47, 18));
		showFootPanelLayout.setVgap(0);
		showFootPanelLayout.setHgap(0);
		tFootRightMargin.setBounds(new Rectangle(30, 37, 33, 20));
		tFootRightMargin.setEnabled(false);
		tFootRightMargin.setBounds(new Rectangle(138, 38, 33, 20));
		tFootRightMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootRightMargin_keyReleased(e);
			}
		});
		lFootMidContent.setEnabled(false);
		lFootMidContent.setText("M Content:");
		lFootMidContent.setBounds(new Rectangle(14, 117, 66, 18));
		lFootTopMargin.setBounds(new Rectangle(14, 11, 24, 18));
		lFootTopMargin.setEnabled(false);
		lFootTopMargin.setText("T:");
		cSetFoot.setText("Enable Foot");
		cSetFoot.setHorizontalAlignment(SwingConstants.LEADING);
		cSetFoot.setActionMap(null);
		tFootBottomMargin.setBounds(new Rectangle(131, 11, 33, 20));
		tFootBottomMargin.setEnabled(false);
		tFootBottomMargin.setBounds(new Rectangle(31, 42, 33, 20));
		tFootBottomMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootBottomMargin_keyReleased(e);
			}
		});
		lFootBottomMargin.setBounds(new Rectangle(14, 41, 22, 18));
		lFootBottomMargin.setEnabled(false);
		lFootBottomMargin.setText("B:");
		tFootTopMargin.setEnabled(false);
		tFootTopMargin.setBounds(new Rectangle(31, 10, 33, 20));
		tFootTopMargin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tFootTopMargin_keyReleased(e);
			}
		});
		showFootPanel.setLayout(showFootPanelLayout);
		footPanel.setLayout(null);
		footInfoPanel.setBorder(BorderFactory.createEtchedBorder());
		footInfoPanel.setLayout(footInfoPanelLayout);
		sFootRightContent.setEnabled(false);
		sFootRightContent.setBounds(new Rectangle(76, 136, 51, 20));
		sFootRightContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sFootRightContent_actionPerformed(e);
			}
		});
		sFootMidContent.setEnabled(false);
		sFootMidContent.setBounds(new Rectangle(76, 117, 51, 20));
		sFootMidContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sFootMidContent_actionPerformed(e);
			}
		});
		sFootLeftContent.setEnabled(false);
		sFootLeftContent.setBounds(new Rectangle(76, 98, 51, 20));
		sFootLeftContent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sFootLeftContent_actionPerformed(e);
			}
		});
		otherPanel.setBorder(BorderFactory.createEtchedBorder());
		tRightMargin.setBounds(new Rectangle(87, 143, 55, 22));
		mm4.setBounds(new Rectangle(147, 143, 22, 18));
		lRightMargin.setBounds(new Rectangle(50, 143, 27, 18));
		tLeftMargin.setBounds(new Rectangle(87, 113, 55, 22));
		mm3.setBounds(new Rectangle(147, 113, 22, 18));
		lLeftMargin.setBounds(new Rectangle(58, 113, 19, 18));
		tBottomMargin.setBounds(new Rectangle(87, 82, 55, 22));
		mm2.setBounds(new Rectangle(147, 82, 22, 18));
		lBottomMargin.setBounds(new Rectangle(35, 82, 42, 18));
		mm1.setBounds(new Rectangle(147, 52, 22, 18));
		tTopMargin.setBounds(new Rectangle(87, 50, 55, 22));
		lTopMargin.setBounds(new Rectangle(53, 52, 24, 18));
		mm6.setBounds(new Rectangle(172, 50, 22, 18));
		lPaperHeight.setBounds(new Rectangle(120, 50, 33, 18));
		mm5.setBounds(new Rectangle(84, 50, 22, 18));
		lPaperWidth.setBounds(new Rectangle(29, 50, 33, 18));
		paperType.setBounds(new Rectangle(28, 20, 166, 22));
		lTableHeadModel.setText("Table Head:");
		lTableHeadModel.setBounds(new Rectangle(20, 12, 79, 24));
		lTableAlign.setBounds(new Rectangle(114, 13, 97, 24));
		lTableAlign.setText("Table Alignment:");
		lTableScale.setText("Table Scale:");
		lTableScale.setBounds(new Rectangle(22, 158, 68, 27));

		tTableScale.setBounds(new Rectangle(97, 160, 55, 20));
		tTableScale.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tTableScale_keyReleased(e);
			}
		});

		sTableHead.setBounds(new Rectangle(17, 42, 71, 22));
		sTableHead.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				sTableHead_mouseClicked(e);
			}
		});
		sTableHead.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTableHead_actionPerformed(e);
			}
		});

		sTableAlign.setBounds(new Rectangle(116, 43, 78, 22));
		sTableAlign.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				sTableAlign_mouseClicked(e);
			}
		});
		sTableAlign.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTableAlign_actionPerformed(e);
			}
		});

		lTableHeadFootBorder.setBounds(new Rectangle(16, 71, 147, 24));
		lTableHeadFootBorder.setText("Table Head/Foot Border:");

		sTableHeadBorder.setBounds(new Rectangle(128, 100, 71, 22));
		sTableHeadBorder.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				sTableHeadBorder_mouseClicked(e);
			}
		});
		sTableHeadBorder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTableHeadBorder_actionPerformed(e);
			}
		});

		sTableFootBorder.setBounds(new Rectangle(128, 132, 71, 22));
		sTableFootBorder.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				sTableFootBorder_mouseClicked(e);
			}
		});
		sTableFootBorder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sTableFootBorder_actionPerformed(e);
			}
		});

		lTableHeadBorder.setText("Table Head Border:");
		lTableHeadBorder.setBounds(new Rectangle(16, 99, 113, 24));
		lTableFootBorder.setBounds(new Rectangle(16, 130, 113, 24));
		lTableFootBorder.setText("Table Foot Border:");
		lJobName.setText("Job Name:");
		lJobName.setBounds(new Rectangle(25, 155, 63, 25));
		tJobName.setBounds(new Rectangle(94, 156, 97, 24));

		footInfoPanel.add(showFootPanel, BorderLayout.NORTH);
		showFootPanel.add(cSetFoot, null);
		footInfoPanel.add(footPanel, BorderLayout.CENTER);
		footPanel.add(lFootTopMargin, null);
		footPanel.add(lFootHeight, null);
		footPanel.add(lFootBottomMargin, null);
		footPanel.add(tFootTopMargin, null);
		footPanel.add(mm7, null);
		footPanel.add(mm8, null);
		footPanel.add(sFootLeftContent, null);
		footPanel.add(lFootLeftContent, null);
		footPanel.add(tFootRightMargin, null);
		footPanel.add(tFootHeight, null);
		footPanel.add(mm11, null);
		footPanel.add(lFootLeftMargin, null);
		footPanel.add(tFootBottomMargin, null);
		footPanel.add(tFootLeftMargin, null);
		footPanel.add(lFootRightMargin, null);
		footPanel.add(mm10, null);
		footPanel.add(mm9, null);
		footPanel.add(tFootLeftContent, null);
		footPanel.add(lFootMidContent, null);
		footPanel.add(lFootRightContent, null);
		footPanel.add(sFootMidContent, null);
		footPanel.add(tFootMidContent, null);
		footPanel.add(tFootRightContent, null);
		footPanel.add(sFootRightContent, null);

		headInfoPanel.add(showHeadPanel, BorderLayout.NORTH);
		showHeadPanel.add(cSetHead, null);
		headInfoPanel.add(headPanel, BorderLayout.CENTER);
		headPanel.add(lHeadTopMargin, null);
		headPanel.add(lHeadHeight, null);
		headPanel.add(lHeadBottomMargin, null);
		headPanel.add(tHeadTopMargin, null);
		headPanel.add(mm12, null);
		headPanel.add(mm13, null);
		headPanel.add(sHeadLeftContent, null);
		headPanel.add(lHeadLeftContent, null);
		headPanel.add(tHeadBottomMargin, null);
		headPanel.add(tHeadHeight, null);
		headPanel.add(mm16, null);
		headPanel.add(tHeadLeftContent, null);
		headPanel.add(lHeadLeftMargin, null);
		headPanel.add(tHeadLeftMargin, null);
		headPanel.add(mm14, null);
		headPanel.add(mm15, null);
		headPanel.add(tHeadRightMargin, null);
		headPanel.add(lHeadRightMargin, null);
		headPanel.add(tHeadMidContent, null);
		headPanel.add(sHeadMidContent, null);
		headPanel.add(lHeadMidContent, null);
		headPanel.add(lHeadRightContent, null);
		headPanel.add(sHeadRightContent, null);
		headPanel.add(tHeadRightContent, null);
		this.add(jPanel1, BorderLayout.SOUTH);
		this.add(jPanel2, BorderLayout.WEST);
		this.add(jPanel3, BorderLayout.EAST);
		this.add(jPanel4, BorderLayout.NORTH);

		//Layout  
		marginPanel.setBorder(BorderFactory.createEtchedBorder());
		marginPanel.setLayout(null);

		lTopMargin.setText("Top:");
		mm1.setText("mm");

		lBottomMargin.setText("Bottom:");
		mm2.setText("mm");

		lLeftMargin.setText("left:");
		mm3.setText("mm");

		lRightMargin.setText("right:");
		mm4.setText("mm");
		marginPanel.add(mm1, null);
		marginPanel.add(mm4, null);
		marginPanel.add(tRightMargin, null);
		marginPanel.add(lRightMargin, null);
		marginPanel.add(lLeftMargin, null);
		marginPanel.add(tLeftMargin, null);
		marginPanel.add(mm3, null);
		marginPanel.add(mm2, null);
		marginPanel.add(tBottomMargin, null);
		marginPanel.add(lBottomMargin, null);
		marginPanel.add(lTopMargin, null);
		marginPanel.add(tTopMargin, null);
		marginPanel.add(lSettingMargin, null);

		//Layout  
		sizePanel.setBorder(BorderFactory.createEtchedBorder());
		sizePanel.setLayout(null);

		lPaperWidth.setText("W:");
		mm5.setText("mm");

		lPaperHeight.setText("H:");
		mm6.setText("mm");
		sizePanel.add(paperType, null);
		sizePanel.add(lPaperWidth, null);
		sizePanel.add(tPaperWidth, null);
		sizePanel.add(tPaperHeight, null);
		sizePanel.add(lPaperHeight, null);
		sizePanel.add(mm5, null);
		sizePanel.add(mm6, null);
		sizePanel.add(landscape, null);
		sizePanel.add(portrait, null);
		sizePanel.add(lJobName, null);

		//Layout  

		//Layout  
		sTableHead.addItem("All Page");
		sTableHead.addItem("First Page");
		sTableHead.addItem("None");

		//Layout  
		sTableAlign.addItem("Left");
		sTableAlign.addItem("Middle");
		sTableAlign.addItem("Right");

		//Layout  
		sTableHeadBorder.addItem("Line");
		sTableHeadBorder.addItem("Box");
		sTableHeadBorder.addItem("None");

		//Layout  
		sTableFootBorder.addItem("Line");
		sTableFootBorder.addItem("Box");
		sTableFootBorder.addItem("None");

		//Layout  
		otherPanel.add(sTableHead, null);
		otherPanel.add(lTableHeadModel, null);
		otherPanel.add(tTableScale, null);
		otherPanel.add(lTableScale, null);
		otherPanel.add(lTableHeadFootBorder, null);
		otherPanel.add(lTableHeadBorder, null);
		otherPanel.add(lTableFootBorder, null);
		otherPanel.add(sTableFootBorder, null);
		otherPanel.add(lTableAlign, null);
		otherPanel.add(sTableAlign, null);
		otherPanel.add(sTableHeadBorder, null);
		sizePanel.add(tJobName, null);
	}

	void landscape_actionPerformed(ActionEvent e) {
		pageFormat.setOrientation(0);

		tTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		tBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		tLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		tRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		tPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		tPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

		repaint();
	}

	void portraitFootLeftContentHeadHeight_actionPerformed(ActionEvent e) {
		pageFormat.setOrientation(1);

		tTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		tBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		tLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		tRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		tPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		tPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));

		repaint();
	}

	void tTopMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((tTopMargin.getText().equals("") ? 0.0 : Double.parseDouble(tTopMargin.getText())));
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

	void tBottomMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((tBottomMargin.getText().equals("") ? 0.0 : Double.parseDouble(tBottomMargin.getText())));
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

	void tLeftMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((tLeftMargin.getText().equals("") ? 0.0 : Double.parseDouble(tLeftMargin.getText())));
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

	void tRightMargin_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((tRightMargin.getText().equals("") ? 0.0 : Double.parseDouble(tRightMargin.getText())));
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
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getA4());
			repaint();
			break;
		case 1:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getA5());
			repaint();
			break;
		case 2:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getB5());
			repaint();
			break;
		case 3:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopC5());
			repaint();
			break;
		case 4:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopDl());
			repaint();
			break;
		case 5:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopB5());
			repaint();
			break;
		case 6:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelopMonarch());
			repaint();
			break;
		case 7:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelop9());
			repaint();
			break;
		case 8:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getDevelop10());
			repaint();
			break;
		case 9:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getLetter());
			repaint();
			break;
		case 10:
			tPaperWidth.setEnabled(false);
			tPaperHeight.setEnabled(false);
			pageFormat.setPaper(PaperSetting.getLegal());
			repaint();
			break;
		default:
			tPaperWidth.setEnabled(true);
			tPaperHeight.setEnabled(true);
			repaint();
			break;
		}
		tTopMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableY())) + 1));
		tBottomMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getHeight() - pageFormat.getImageableY()
				- pageFormat.getImageableHeight())) + 1));
		tLeftMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getImageableX())) + 1));
		tRightMargin.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getWidth() - pageFormat.getImageableX()
				- pageFormat.getImageableWidth())) + 1));

		tPaperWidth.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullWidth())) + 1));
		tPaperHeight.setText(String.valueOf((int) (Utility.dotToMillimeter(pageFormat.getFullHeight())) + 1));
	}

	void tPaperWidth_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double pw, ph;
		double x, y, w, h;
		double text;
		double newIn;

		try {
			text = ((tPaperWidth.getText().equals("") ? 0.0 : Double.parseDouble(tPaperWidth.getText())));
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

	void tPaperHeightFootLeftContentHeadHeight_keyReleased(KeyEvent e) {
		Paper newPaper = pageFormat.getPaper();

		double ph, pw;
		double text;
		double newIn;

		try {
			text = ((tPaperHeight.getText().equals("") ? 0.0 : Double.parseDouble(tPaperHeight.getText())));
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

	void cSetFoot_stateChanged(ChangeEvent e) {
		if (cSetFoot.isSelected()) {
			pageFormat.setShowFoot(true);

			Component[] component = this.footPanel.getComponents();

			for (int index = 0; index < component.length; index++)
				component[index].setEnabled(true);

			repaint();
		} else {
			pageFormat.setShowFoot(false);

			Component[] component = this.footPanel.getComponents();

			for (int index = 0; index < component.length; index++)
				component[index].setEnabled(false);

			repaint();
		}
	}

	void cSetHead_stateChanged(ChangeEvent e) {
		if (cSetHead.isSelected()) {
			pageFormat.setShowHead(true);

			Component[] component = this.headPanel.getComponents();

			for (int index = 0; index < component.length; index++)
				component[index].setEnabled(true);

			repaint();
		} else {
			pageFormat.setShowHead(false);

			Component[] component = this.headPanel.getComponents();

			for (int index = 0; index < component.length; index++)
				component[index].setEnabled(false);

			repaint();
		}
	}

	void tTableScale_keyReleased(KeyEvent e) {
		try {
			pageFormat.setTableScale(Integer.parseInt(tTableScale.getText()));
		} catch (NumberFormatException nfe) {
			return;
		}
	}

	//set foot infomation  
	void tFootTopMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tFootTopMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tFootTopMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootImageableHeight() + pageFormat.getFootImageableY() - newIn);
		pageFormat.setFootImageableY(newIn);

		repaint();
	}

	void tFootBottomMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tFootBottomMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tFootBottomMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootHeight() - pageFormat.getFootImageableY() - newIn);

		repaint();
	}

	void tFootLeftMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tFootLeftMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tFootLeftMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableWidth(pageFormat.getFootImageableWidth() + pageFormat.getFootImageableX() - newIn);
		pageFormat.setFootImageableX(newIn);

		repaint();
	}

	void tFootRightMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tFootRightMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tFootRightMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableWidth(pageFormat.getFootWidth() - pageFormat.getFootImageableX() - newIn);

		repaint();
	}

	void tFootHeight_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tFootHeight.getText().equals("")) ? 0.0 : Double.parseDouble(tFootHeight.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setFootImageableHeight(pageFormat.getFootImageableHeight() - pageFormat.getFootHeight() + newIn);
		pageFormat.setFootHeight(newIn);

		repaint();
	}

	void tFootLeftContent_keyReleased(KeyEvent e) {
		pageFormat.setFootLeftContent(tFootLeftContent.getText());
	}

	void sFootLeftContent_actionPerformed(ActionEvent e) {
		if (sFootLeftContent.getSelectedIndex() == 0)
			return;
		else if (sFootLeftContent.getSelectedIndex() == 1)
			tFootLeftContent.setText(tFootLeftContent.getText() + "@d");
		else if (sFootLeftContent.getSelectedIndex() == 2)
			tFootLeftContent.setText(tFootLeftContent.getText() + "@p");
		else
			tFootLeftContent.setText(tFootLeftContent.getText() + "@t");

		pageFormat.setFootLeftContent(tFootLeftContent.getText());
	}

	void tFootMidContent_keyReleased(KeyEvent e) {
		pageFormat.setFootMidContent(tFootMidContent.getText());
	}

	void sFootMidContent_actionPerformed(ActionEvent e) {
		if (sFootMidContent.getSelectedIndex() == 0)
			return;
		else if (sFootMidContent.getSelectedIndex() == 1)
			tFootMidContent.setText(tFootMidContent.getText() + "@d");
		else if (sFootMidContent.getSelectedIndex() == 2)
			tFootMidContent.setText(tFootMidContent.getText() + "@p");
		else
			tFootMidContent.setText(tFootMidContent.getText() + "@t");

		pageFormat.setFootMidContent(tFootMidContent.getText());
	}

	void tFootRightContent_keyReleased(KeyEvent e) {
		pageFormat.setFootRightContent(tFootRightContent.getText());
	}

	void sFootRightContent_actionPerformed(ActionEvent e) {
		if (sFootRightContent.getSelectedIndex() == 0)
			return;
		else if (sFootRightContent.getSelectedIndex() == 1)
			tFootRightContent.setText(tFootRightContent.getText() + "@d");
		else if (sFootRightContent.getSelectedIndex() == 2)
			tFootRightContent.setText(tFootRightContent.getText() + "@p");
		else
			tFootRightContent.setText(tFootRightContent.getText() + "@t");

		pageFormat.setFootRightContent(tFootRightContent.getText());
	}

	//set head infomation  
	void tHeadTopMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tHeadTopMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tHeadTopMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setHeadImageableHeight(pageFormat.getHeadImageableHeight() + pageFormat.getHeadImageableY() - newIn);
		pageFormat.setHeadImageableY(newIn);

		repaint();
	}

	void tHeadBottomMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tHeadBottomMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tHeadBottomMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setHeadImageableHeight(pageFormat.getHeadHeight() - pageFormat.getHeadImageableY() - newIn);

		repaint();
	}

	void tHeadLeftMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tHeadLeftMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tHeadLeftMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setHeadImageableWidth(pageFormat.getHeadImageableWidth() + pageFormat.getHeadImageableX() - newIn);
		pageFormat.setHeadImageableX(newIn);

		repaint();
	}

	void tHeadRightMargin_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tHeadRightMargin.getText().equals("")) ? 0.0 : Double.parseDouble(tHeadRightMargin.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setHeadImageableWidth(pageFormat.getHeadWidth() - pageFormat.getHeadImageableX() - newIn);

		repaint();
	}

	void tHeadHeight_keyReleased(KeyEvent e) {
		double text;
		double newIn;

		try {
			text = (tHeadHeight.getText().equals("")) ? 0.0 : Double.parseDouble(tHeadHeight.getText());
			newIn = Utility.millimeterToDot(text);
		} catch (NumberFormatException nfe) {
			return;
		}

		pageFormat.setHeadImageableHeight(pageFormat.getHeadImageableHeight() - pageFormat.getHeadHeight() + newIn);
		pageFormat.setHeadHeight(newIn);

		repaint();
	}

	void tHeadLeftContent_keyReleased(KeyEvent e) {
		pageFormat.setHeadLeftContent(tHeadLeftContent.getText());
	}

	void sHeadLeftContent_actionPerformed(ActionEvent e) {
		if (sHeadLeftContent.getSelectedIndex() == 0)
			return;
		else if (sHeadLeftContent.getSelectedIndex() == 1)
			tHeadLeftContent.setText(tHeadLeftContent.getText() + "@d");
		else if (sHeadLeftContent.getSelectedIndex() == 2)
			tHeadLeftContent.setText(tHeadLeftContent.getText() + "@p");
		else
			tHeadLeftContent.setText(tHeadLeftContent.getText() + "@t");

		pageFormat.setHeadLeftContent(tHeadLeftContent.getText());
	}

	void tHeadMidContent_keyReleased(KeyEvent e) {
		pageFormat.setHeadMidContent(tHeadMidContent.getText());
	}

	void sHeadMidContent_actionPerformed(ActionEvent e) {
		if (sHeadMidContent.getSelectedIndex() == 0)
			return;
		else if (sHeadMidContent.getSelectedIndex() == 1)
			tHeadMidContent.setText(tHeadMidContent.getText() + "@d");
		else if (sHeadMidContent.getSelectedIndex() == 2)
			tHeadMidContent.setText(tHeadMidContent.getText() + "@p");
		else
			tHeadMidContent.setText(tHeadMidContent.getText() + "@t");

		pageFormat.setHeadMidContent(tHeadMidContent.getText());
	}

	void tHeadRightContent_keyReleased(KeyEvent e) {
		pageFormat.setHeadRightContent(tHeadRightContent.getText());
	}

	void sHeadRightContent_actionPerformed(ActionEvent e) {
		if (sHeadRightContent.getSelectedIndex() == 0)
			return;
		else if (sHeadRightContent.getSelectedIndex() == 1)
			tHeadRightContent.setText(tHeadRightContent.getText() + "@d");
		else if (sHeadRightContent.getSelectedIndex() == 2)
			tHeadRightContent.setText(tHeadRightContent.getText() + "@p");
		else
			tHeadRightContent.setText(tHeadRightContent.getText() + "@t");

		pageFormat.setHeadRightContent(tHeadRightContent.getText());
	}

	void sTableHead_actionPerformed(ActionEvent e) {
		if (!isTableHeadClicked)
			return;

		int index = sTableHead.getSelectedIndex();

		switch (index) {
		case 0:
			pageFormat.setHeaderType(0);
			break;
		case 1:
			pageFormat.setHeaderType(1);
			break;
		case 2:
			pageFormat.setHeaderType(2);
			break;
		default:
			break;
		}
	}

	void sTableAlign_actionPerformed(ActionEvent e) {
		if (!isTableAlignClicked)
			return;

		int index = sTableAlign.getSelectedIndex();

		switch (index) {
		case 0:
			pageFormat.setTableAlignment(0);
			break;
		case 1:
			pageFormat.setTableAlignment(1);
			break;
		case 2:
			pageFormat.setTableAlignment(2);
			break;
		default:
			break;
		}
	}

	void sTableHeadBorder_actionPerformed(ActionEvent e) {
		if (!isTableHeadBorderClicked)
			return;

		int index = sTableHeadBorder.getSelectedIndex();

		switch (index) {
		case 0:
			pageFormat.setHeadBorderType(0);
			break;
		case 1:
			pageFormat.setHeadBorderType(1);
			break;
		case 2:
			pageFormat.setHeadBorderType(2);
			break;
		default:
			break;
		}
	}

	void sTableFootBorder_actionPerformed(ActionEvent e) {
		if (!isTableFootBorderClicked)
			return;

		int index = sTableFootBorder.getSelectedIndex();

		switch (index) {
		case 0:
			pageFormat.setFootBorderType(0);
			break;
		case 1:
			pageFormat.setFootBorderType(1);
			break;
		case 2:
			pageFormat.setFootBorderType(2);
			break;
		default:
			break;
		}
	}

	void sTableHead_mouseClicked(MouseEvent e) {
		isTableHeadClicked = true;
	}

	void sTableAlign_mouseClicked(MouseEvent e) {
		isTableAlignClicked = true;
	}

	void sTableHeadBorder_mouseClicked(MouseEvent e) {
		isTableHeadBorderClicked = true;
	}

	void sTableFootBorder_mouseClicked(MouseEvent e) {
		isTableFootBorderClicked = true;
	}

	void tJobName_keyReleased(KeyEvent e) {
		pageFormat.setJobName(tJobName.getText());
	}
}