package com.vastcm.swing.print.table;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class ExtPageFormat extends PageFormat {
	private PageBorder head = new PageBorder(this.getPaper().getWidth(), this.getPaper().getHeight() * 15 / 100);
	private List<PageBorder> headSubTitle = new ArrayList<PageBorder>();

	private PageBorder foot = new PageBorder(this.getPaper().getWidth(), this.getPaper().getHeight() * 15 / 100);
	private List<PageBorder> footSubTitle = new ArrayList<PageBorder>();

	private boolean showFoot = false;
	//控制页头的显示方式，与showHead冲突
	private int pageHeadDisplayType = HEADER_DISPLAY_ALL_PAGE;
	//控制表格的表头显示方式
	private int tableHeadDisplayType = HEADER_DISPLAY_ALL_PAGE;
	private int pageFootSubTitleDisplayType = HEADER_DISPLAY_ALL_PAGE;
	public static int HEADER_DISPLAY_ALL_PAGE = 0;
	public static int HEADER_DISPLAY_FIRST_PAGE = 1;
	public static int HEADER_DISPLAY_NONE = 2;

	private int tableAlignment = TABLE_ALIGN_LEFT;
	public static int TABLE_ALIGN_LEFT = 0;
	public static int TABLE_ALIGN_MID = 1;
	public static int TABLE_ALIGN_RIGHT = 2;

	private int tableScale = 100;

	private String jobName = "ghostliang";
	private boolean isPrinting = false;

	public ExtPageFormat() {
		super();
	}

	public void setFootHeight(double newHeight) {
		foot.height = newHeight;
	}

	public void setFootHeight(int newHeight) {
		foot.height = (double) newHeight;
	}

	public void setFootImageableArea(double x, double y, double width, double height) {
		foot.imageableX = x;
		foot.imageableY = y;
		foot.imageableWidth = width;
		foot.imageableHeight = height;
	}

	public void setFootImageableX(double x) {
		foot.imageableX = x;
	}

	public void setFootImageableY(double y) {
		foot.imageableY = y;
	}

	public void setFootImageableWidth(double width) {
		foot.imageableWidth = width;
	}

	public void setFootImageableHeight(double height) {
		foot.imageableHeight = height;
	}

	public void setFootBorderType(int borderType) {
		foot.borderType = borderType;
	}

	public void setFootLeftContent(String newContent) {
		foot.leftContent = newContent;
	}

	public void setFootMidContent(String newContent) {
		foot.midContent = newContent;
	}

	public void setFootRightContent(String newContent) {
		foot.rightContent = newContent;
	}

	public double getFootWidth() {
		return foot.width;
	}

	public double getFootHeight() {
		return foot.height;
	}

	public double getFootImageableX() {
		return foot.imageableX;
	}

	public double getFootImageableY() {
		return foot.imageableY;
	}

	public double getFootImageableWidth() {
		return foot.imageableWidth;
	}

	public double getFootImageableHeight() {
		return foot.imageableHeight;
	}

	public int getFootBorderType() {
		return foot.borderType;
	}

	public String getFootLeftContent() {
		return foot.leftContent;
	}

	public String getFootMidContent() {
		return foot.midContent;
	}

	public String getFootRightContent() {
		return foot.rightContent;
	}

	public void setPageHeadDisplayType(int newHeaderType) {
		this.pageHeadDisplayType = newHeaderType;
	}

	public int getPageHeadDisplayType() {
		return this.pageHeadDisplayType;
	}

	public void setPageFootSubTitleDisplayType(int newHeaderType) {
		this.pageFootSubTitleDisplayType = newHeaderType;
	}

	public int getPageFootSubTitleDisplayType() {
		return this.pageFootSubTitleDisplayType;
	}

	public void setTableHeadDisplayType(int newHeaderType) {
		this.tableHeadDisplayType = newHeaderType;
	}

	public int getTableHeadDisplayType() {
		return this.tableHeadDisplayType;
	}

	public void setTableAlignment(int newTableAlignment) {
		this.tableAlignment = newTableAlignment;
	}

	public int getTableAlignment() {
		return this.tableAlignment;
	}

	public void setTableScale(int newScale) {
		this.tableScale = newScale;
	}

	public int getTableScale() {
		return this.tableScale;
	}

	public void setShowFoot(boolean isShow) {
		if (showFoot == isShow)
			return;

		showFoot = isShow;
	}

	public boolean getShowFoot() {
		return this.showFoot;
	}

	public void setOrientation(int orientation) {
		if (this.getOrientation() == orientation)
			return;

		double pw = this.getPaper().getWidth();
		double ph = this.getPaper().getHeight();

		super.setOrientation(orientation);

		if (orientation == 1) {
			head.width = pw;
			foot.width = pw;

			head.imageableWidth = head.imageableWidth - ph + pw;
			foot.imageableWidth = foot.imageableWidth - ph + pw;
		} else {
			head.width = ph;
			foot.width = ph;

			head.imageableWidth = head.imageableWidth + ph - pw;
			foot.imageableWidth = foot.imageableWidth + ph - pw;
		}
	}

	public double getImageableX() {
		//		if (isPrinting)
		//			return 0;

		Paper paper = this.getPaper();

		double x = paper.getImageableX();
		double y = paper.getImageableY();
		double h = paper.getImageableHeight();

		double ph = paper.getHeight();

		if (this.getOrientation() == PageFormat.PORTRAIT)
			return x;
		else
			return (ph - y - h);
	}

	public double getImageableY() {
		//		if (isPrinting)
		//			return 0;

		Paper paper = this.getPaper();

		double x = paper.getImageableX();
		double y = paper.getImageableY();

		//		double hh = ((showHead) ? head.height : 0);

		if (this.getOrientation() == PageFormat.PORTRAIT)
			return y;
		else
			return x;
	}

	public double getImageableWidth() {
		Paper paper = this.getPaper();

		double w = paper.getImageableWidth();
		double h = paper.getImageableHeight();

		if (this.getOrientation() == PageFormat.PORTRAIT)
			return w;
		else
			return h;
	}

	public double getImageableHeight() {
		Paper paper = this.getPaper();

		double w = paper.getImageableWidth();
		double h = paper.getImageableHeight();
		//		if (isPrinting) {
		if (this.getOrientation() == PageFormat.PORTRAIT)
			return h;
		else
			return w;
		//		}

		//		double hh = ((showHead) ? head.height : 0);
		//		double fh = ((showFoot) ? foot.height : 0);
		//
		//		if (this.getOrientation() == PageFormat.PORTRAIT)
		//			return (h - hh - fh);
		//		else
		//			return (w - hh - fh);
	}

	public double getHeight() {
		Paper paper = this.getPaper();

		double pw = paper.getWidth();
		double ph = paper.getHeight();

		//		double hh = ((showHead) ? head.height : 0);
		//		double fh = ((showFoot) ? foot.height : 0);
		double hh = getHeadTotalHeight();
		double fh = getFootTotalHeight();

		if (this.getOrientation() == PageFormat.PORTRAIT)
			return (ph - hh - fh);
		else
			return (pw - hh - fh);
	}

	public double getWidth() {
		Paper paper = this.getPaper();

		double pw = paper.getWidth();
		double ph = paper.getHeight();

		if (this.getOrientation() == PageFormat.PORTRAIT)
			return pw;
		else
			return ph;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String newJobName) {
		this.jobName = newJobName;
	}

	public void setPaper(Paper newPaper) {
		//		if (isPrinting) {
		//
		//			return;
		//		}
		System.out.println("another paper");
		System.out.println(getWidth() + " " + getHeight() + " " + getImageableX() + " " + getImageableY() + " " + getImageableWidth() + " "
				+ getImageableHeight());
		System.out.println("head:" + head.getWidth() + " " + head.getHeight() + " " + head.getImageableX() + " " + head.getImageableY() + " "
				+ head.getImageableWidth() + " " + head.getImageableHeight());
		if (this.getOrientation() == PageFormat.PORTRAIT) {
			head.width = newPaper.getWidth();
			foot.width = newPaper.getWidth();

			head.imageableWidth = head.imageableWidth - getPaper().getWidth() + newPaper.getWidth();
			foot.imageableWidth = foot.imageableWidth - getPaper().getWidth() + newPaper.getWidth();
		} else {
			head.width = newPaper.getHeight();
			foot.width = newPaper.getHeight();

			head.imageableWidth = head.imageableWidth - getPaper().getHeight() + newPaper.getHeight();
			foot.imageableWidth = foot.imageableWidth - getPaper().getHeight() + newPaper.getHeight();
		}
		super.setPaper(newPaper);
		System.out.println(getWidth() + " " + getHeight() + " " + getImageableX() + " " + getImageableY() + " " + getImageableWidth() + " "
				+ getImageableHeight());
		System.out.println("head:" + head.getWidth() + " " + head.getHeight() + " " + head.getImageableX() + " " + head.getImageableY() + " "
				+ head.getImageableWidth() + " " + head.getImageableHeight());
	}

	public double getFullWidth() {
		if (this.getOrientation() == PageFormat.PORTRAIT)
			return this.getPaper().getWidth();
		else
			return this.getPaper().getHeight();
	}

	public double getFullHeight() {
		if (this.getOrientation() == PageFormat.PORTRAIT)
			return this.getPaper().getHeight();
		else
			return this.getPaper().getWidth();
	}

	public PageBorder getHead() {
		return this.head;
	}

	public double getHeadTotalHeight() {
		double subTitleHeight = 0;
		for (PageBorder pb : headSubTitle)
			subTitleHeight += pb.height;
		return head.height + subTitleHeight;
	}

	public PageBorder getHeadSubTitle(int index) {
		return headSubTitle.get(index);
	}

	public List<PageBorder> getHeadSubTitle() {
		return headSubTitle;
	}

	public PageBorder getFoot() {
		return this.foot;
	}

	public double getFootTotalHeight() {
		double subTitleHeight = 0;
		for (PageBorder pb : footSubTitle)
			subTitleHeight += pb.height;
		return foot.height + subTitleHeight;
	}

	public PageBorder getFootSubTitle(int index) {
		return footSubTitle.get(index);
	}

	public List<PageBorder> getFootSubTitle() {
		return footSubTitle;
	}

	/**
	 * Makes a copy of this <code>PageFormat</code> with the same contents as this <code>PageFormat</code>.
	 * @return a copy of this <code>PageFormat</code>.
	 */
	public Object clone() {
		ExtPageFormat newPage;

		try {
			newPage = (ExtPageFormat) super.clone();
			newPage.head = (PageBorder) head.clone();
			List<PageBorder> newHeadSubTitle = new ArrayList<PageBorder>();
			for (PageBorder pb : headSubTitle) {
				newHeadSubTitle.add((PageBorder) pb.clone());
			}
			newPage.headSubTitle = newHeadSubTitle;

			newPage.foot = (PageBorder) foot.clone();
			List<PageBorder> newFootSubTitle = new ArrayList<PageBorder>();
			for (PageBorder pb : footSubTitle) {
				newFootSubTitle.add((PageBorder) pb.clone());
			}
			newPage.footSubTitle = newFootSubTitle;
		} catch (Exception e) {
			e.printStackTrace();
			newPage = null; // should never happen.
		}

		return newPage;
	}

	public boolean isPrinting() {
		return isPrinting;
	}

	public void setPrinting(boolean isPrinting) {
		this.isPrinting = isPrinting;
	}

}