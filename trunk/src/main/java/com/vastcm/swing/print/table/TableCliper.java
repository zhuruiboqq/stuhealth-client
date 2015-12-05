package com.vastcm.swing.print.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import com.vastcm.stuhealth.client.utils.DateUtil;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class TableCliper implements Printable, Pageable {
	//variable for record the pageFormat  
	private JTable table;

	private PageBorder head;
	private List<PageBorder> headSubTitle;
	private PageBorder foot;
	private List<PageBorder> footSubTitle;
	private boolean showFoot;

	private int tableAlignment;
	private int tableScale;

	private ExtPageFormat pageFormat;

	private int currentPage = -1;
	private int totalPage = -1;

	//init the class  
	public TableCliper(JTable newTable, ExtPageFormat newPageFormat) {
		table = newTable;

		tableAlignment = newPageFormat.getTableAlignment();
		tableScale = newPageFormat.getTableScale();

		head = newPageFormat.getHead();
		headSubTitle = newPageFormat.getHeadSubTitle();
		foot = newPageFormat.getFoot();
		footSubTitle = newPageFormat.getFootSubTitle();

		showFoot = newPageFormat.getShowFoot();

		pageFormat = newPageFormat;
	}

	//overwrite the print function of the print  
	public int print(Graphics newGraphics, PageFormat newPageFormat, int index) {
		Dimension size = null;

		if ((table.getWidth() == 0) || (table.getHeight() == 0)) {
			table.setSize(table.getPreferredSize());
		}

		int tableWidth = table.getWidth();
		int tableHeight = table.getHeight();
		int positionX = 0;
		int positionY = 0;

		int pageIndex = 0;
		while (positionY < tableHeight) {
			positionX = 0;
			while (positionX < tableWidth) {
				size = getPrintSize(pageIndex, positionX, positionY);
				if (pageIndex == index) {
					currentPage = index + 1;
					paintTable(newGraphics, positionX, positionY, size);
					return Printable.PAGE_EXISTS;
				}

				pageIndex++;
				positionX += size.width;
			}
			positionY += size.height;
		}
		return Printable.NO_SUCH_PAGE;
	}

	//get how much area this page to print  
	protected Dimension getPrintSize(int pageIndex, int positionX, int positionY) {
		Rectangle rect;

		int printWidth;
		int printHeight;

		//		int firstCol = table.columnAtPoint(new Point(positionX, positionY));
		//		int firstRow = table.columnAtPoint(new Point(positionX, positionY));

		int maxWidth = (int) (pageFormat.getImageableWidth() * 100 / tableScale);
		double maxHeightD = pageFormat.getImageableHeight();
		double hh = 0;
		if (displayHeaderOnPage(pageFormat.getPageHeadDisplayType(), positionY))
			hh = pageFormat.getHeadTotalHeight();
		double fh = ((showFoot) ? foot.height : 0);
		if (displayHeaderOnPage(pageFormat.getPageFootSubTitleDisplayType(), positionY))
			fh = pageFormat.getFootTotalHeight();
		maxHeightD = (maxHeightD - hh - fh);
		int maxHeight = (int) (maxHeightD * 100 / tableScale);

		if (displayHeaderOnPage(pageFormat.getTableHeadDisplayType(), positionY)) {
			maxHeight -= table.getTableHeader().getHeight();
		}

		int lastCol = table.columnAtPoint(new Point(positionX + maxWidth, positionY));
		if (lastCol == -1)
			printWidth = table.getWidth() - positionX;
		else {
			rect = table.getCellRect(0, lastCol - 1, true);
			printWidth = rect.x + rect.width - positionX;
		}

		int lastRow = table.rowAtPoint(new Point(positionX, positionY + maxHeight));
		if (lastRow == -1)
			printHeight = table.getHeight() - positionY;
		else {
			rect = table.getCellRect(lastRow - 1, 0, true);
			printHeight = rect.y + rect.height - positionY;
		}

		return new Dimension(printWidth, printHeight);
	}

	//show the table on the PrintComponent  
	protected void paintTable(Graphics newGraphics, int positionX, int positionY, Dimension size) {
		int allOffsetX = 0;
		int allOffsetY = 0;

		if (displayHeaderOnPage(pageFormat.getPageHeadDisplayType(), positionY)) {
			//if head is exist, show it on the paper  
			paintTable_head(newGraphics, positionX, positionY);
			allOffsetX += 0;
			allOffsetY += (int) head.getHeight();

			if (headSubTitle.size() > 0) {
				for (PageBorder subTitle : headSubTitle) {
					paintTable_subTitle(newGraphics, positionX, positionY, subTitle);
					allOffsetX += 0;
					allOffsetY += (int) subTitle.getHeight();
				}
			}
		}
		paintTable_midTable(newGraphics, positionX, positionY, size);
		allOffsetX += 0;
		allOffsetY += (int) (pageFormat.getHeight());

		//if foot is exist,show it on the paper  
		if (displayHeaderOnPage(pageFormat.getPageFootSubTitleDisplayType(), positionY)) {
			if (footSubTitle.size() > 0) {
				for (PageBorder subTitle : footSubTitle) {
					paintTable_subTitle(newGraphics, positionX, positionY, subTitle);
					allOffsetX += 0;
					allOffsetY += (int) subTitle.getHeight();
				}
			}
		}
		if (showFoot) {
			paintTable_subTitle(newGraphics, positionX, positionY, foot);
			allOffsetX += 0;
			allOffsetY += (int) foot.getHeight();
		}

		//restore all offset  
		newGraphics.translate(-allOffsetX, -allOffsetY);
	}

	private String getRealContent(String oldContent, int positionX, int positionY) {
		StringBuffer content = new StringBuffer(oldContent);
		while (content.indexOf("@") != -1 && content.indexOf("@") != content.length() - 1) {
			int flag = content.indexOf("@");
			switch (content.charAt(flag + 1)) {
			case 'd':
				content.replace(flag, flag + 2, DateUtil.getDateString(new Date()));
				break;
			case 'p':
				content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
				break;
			case 't':
				content.replace(flag, flag + 2, "" + this.getNumberOfPages());
				break;
			default:
				content.replace(flag, flag + 1, "#$#$#$");
				break;
			}
		}
		String result = content.toString().replaceAll("#$#$#$", "@");

		return result;
	}

	private void paintTable_head(Graphics newGraphics, int positionX, int positionY) {
		int headWidth = (int) head.getWidth();
		int headHeight = (int) head.getHeight();

		int headImageableX = (int) head.getImageableX();
		int headImageableY = (int) head.getImageableY();
		int headImageableWidth = (int) head.getImageableWidth();
		int headImageableHeight = (int) head.getImageableHeight();

		newGraphics.setColor(Color.white);//TODO head
		newGraphics.fillRect(0, 0, headWidth, headHeight);

		//print the border of the head  
		if (head.getBorderType() == PageBorder.BORDER_TOP_LINE) {
			newGraphics.setColor(Color.black);
			newGraphics.drawLine(headImageableX, headImageableY + headImageableHeight, headImageableWidth + headImageableX, headImageableY
					+ headImageableHeight);
		} else if (head.getBorderType() == PageBorder.BORDER_BOX) {
			newGraphics.setColor(Color.black);
			newGraphics.drawRect(headImageableX, headImageableY, headImageableWidth, headImageableHeight);
		}

		//print string of head  
		String content = null;
		int y = headImageableY + (headImageableHeight / 2 + 6);
		if (head.leftContent != "") {
			content = getRealContent(head.leftContent, positionX, positionY);
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, headImageableX + 10, y);
		}

		if (head.midContent != "") {
			content = getRealContent(head.midContent, positionX, positionY);
			Font oldFont = newGraphics.getFont();
			newGraphics.setFont(new Font("宋体", Font.BOLD, 24));//TODO title old value = 4 new value = 6
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, headImageableX + (headImageableWidth / 2) - content.length() * 12, y);
			newGraphics.setFont(oldFont);
		}

		if (head.rightContent != "") {
			content = getRealContent(head.rightContent, positionX, positionY);
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, headImageableX + headImageableWidth - content.length() * 8, y);
		}

		newGraphics.translate(0, headHeight);
	}

	private void paintTable_midTable(Graphics newGraphics, int positionX, int positionY, Dimension size) {
		Graphics2D newGraphics2D = (Graphics2D) newGraphics;
		//print the main paper  
		//a offset from the border of the paper  
		int offsetX = (int) (pageFormat.getImageableX());
		int offsetY = (int) (pageFormat.getImageableY());

		//the size of the paper  
		int paperWidth = (int) (pageFormat.getWidth());
		int paperHeight = (int) (pageFormat.getHeight());

		//draw main paper  
		newGraphics.setColor(Color.white);//TODO page table
		newGraphics.fillRect(0, 0, paperWidth, paperHeight);

		//save the clip  
		Rectangle clipRect = newGraphics.getClipBounds();

		//offset for alignment  
		//size measured as table scale,so you must consider with table scale here  
		if (tableAlignment == 1)
			newGraphics.translate((int) ((pageFormat.getImageableWidth() - (double) (size.width * tableScale / 100)) / 2), 0);
		else if (tableAlignment == 2)
			newGraphics.translate((int) (pageFormat.getImageableWidth() - (double) (size.width * tableScale / 100)), 0);

		//scale the Graphics  
		newGraphics2D.scale((double) (tableScale) / 100.0, (double) (tableScale) / 100.0);

		//if print head,we should print head and table  
		//otherwise,we just need to pring the table  
		if (displayHeaderOnPage(pageFormat.getTableHeadDisplayType(), positionY)) {
			//if need to print a table head  
			JTableHeader header = table.getTableHeader();
			if ((header.getHeight() == 0) || (header.getWidth() == 0))
				header.setSize(header.getPreferredSize());

			//get the height of the header  
			//after print the head,the offsetY need to change into (offsetY + headerHeight)  
			int headerHeight = header.getHeight();

			//show the offset as the real size,so multiply the table scale with offset  
			newGraphics.translate((int) (offsetX * 100.0 / tableScale) - positionX, (int) (offsetY * 100.0 / tableScale));

			newGraphics.clipRect(positionX, 0, size.width, size.height + headerHeight);

			header.paint(newGraphics);

			//draw v(|) line with table head  
			newGraphics.setColor(Color.gray);
			newGraphics.drawLine(positionX, 0, positionX, headerHeight);

			//draw h(-) line with table head  
			newGraphics.setColor(Color.gray);
			newGraphics.drawLine(positionX, 0, positionX + size.width, 0);

			//draw the table  
			newGraphics.translate(0, headerHeight - positionY);
			newGraphics.clipRect(positionX, positionY, size.width, size.height);

			//			table.paint(newGraphics);
			table.print(newGraphics);

			//draw h-line with table head  
			newGraphics.setColor(Color.gray);
			newGraphics.drawLine(positionX, positionY, positionX, size.height + positionY);

			//restore the translate  
			newGraphics.translate(0, positionY - headerHeight);
			newGraphics.translate((int) (positionX - offsetX * (double) (100.0 / tableScale)), (int) (-offsetY * (double) (100.0 / tableScale)));
		}
		//print table without head  
		else {
			//show the offset as the real size,so multiply the table scale with offset  
			newGraphics.translate((int) (offsetX * 100.0 / tableScale - positionX), (int) (offsetY * 100.0 / tableScale - positionY));
			//as i said,size has been measured with table scale,so there needn't  
			newGraphics.clipRect(positionX, positionY, size.width, size.height);

			//			table.paint(newGraphics);
			table.print(newGraphics);

			//draw v-line without table head  
			newGraphics.setColor(Color.gray);
			newGraphics.drawLine(positionX, positionY, positionX + size.width, positionY);

			//draw h-line without table head  
			newGraphics.setColor(Color.gray);
			newGraphics.drawLine(positionX, positionY, positionX, size.height + positionY);

			//restore the translate  
			newGraphics.translate((int) (positionX - offsetX * (double) (100.0 / tableScale)), (int) (positionY - offsetY
					* (double) (100.0 / tableScale)));
		}

		//restore the scale  
		newGraphics2D.scale((double) (100.0 / tableScale), (double) (100.0 / tableScale));

		//restore the translate  
		if (tableAlignment == 1)
			newGraphics.translate((int) (((double) (size.width * tableScale / 100) - pageFormat.getImageableWidth()) / 2), 0);
		else if (tableAlignment == 2)
			newGraphics.translate((int) ((double) (size.width * tableScale / 100) - pageFormat.getImageableWidth()), 0);

		//restore the clip  
		newGraphics.setClip(clipRect);

		//translate for the main paper  
		newGraphics.translate(0, paperHeight);
	}

	private void paintTable_subTitle(Graphics newGraphics, int positionX, int positionY, PageBorder foot) {
		int footWidth = (int) foot.getWidth();
		int footHeight = (int) foot.getHeight();

		int footImageableX = (int) foot.getImageableX();
		int footImageableY = (int) foot.getImageableY();
		int footImageableWidth = (int) foot.getImageableWidth();
		int footImageableHeight = (int) foot.getImageableHeight();

		newGraphics.setColor(Color.white);//TODO foot
		newGraphics.fillRect(0, 0, footWidth, footHeight);

		//print the border of the foot  
		if (foot.getBorderType() == PageBorder.BORDER_TOP_LINE) {
			newGraphics.setColor(Color.black);
			newGraphics.drawLine(footImageableX, footImageableY, footImageableWidth + footImageableX, footImageableY);
		} else if (foot.getBorderType() == PageBorder.BORDER_BOX) {
			newGraphics.setColor(Color.black);
			newGraphics.drawRect(footImageableX, footImageableY, footImageableWidth, footImageableHeight);
		}

		Font oldFont = newGraphics.getFont();
		Font curFont = oldFont;
		if (foot.getFont() != null) {
			curFont = foot.getFont();
		}
		newGraphics.setFont(curFont);
		//print string of foot  
		String content = null;
		int y = footImageableY + (footImageableHeight / 2 + curFont.getSize() / 2);
		if (foot.leftContent != "") {
			content = getRealContent(foot.leftContent, positionX, positionY);
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, footImageableX + 10, y);
		}

		if (foot.midContent != "") {
			content = getRealContent(foot.midContent, positionX, positionY);
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, footImageableX + (footImageableWidth / 2) - content.length() * 4, y);
		}

		if (foot.rightContent != "") {
			content = getRealContent(foot.rightContent, positionX, positionY);
			newGraphics.setColor(Color.black);
			newGraphics.drawString(content, footImageableX + footImageableWidth - content.length() * 10, y);
		}
		newGraphics.setFont(oldFont);

		newGraphics.translate(0, footHeight);
	}

	//decide whether display the table header or not  
	protected boolean displayHeaderOnPage(int headerDisplayType, int positionY) {
		if ((headerDisplayType == ExtPageFormat.HEADER_DISPLAY_ALL_PAGE)
				|| ((headerDisplayType == ExtPageFormat.HEADER_DISPLAY_FIRST_PAGE) && positionY == 0))
			return true;
		else
			return false;
	}

	public JTable getJTable() {
		return table;
	}

	//get the total page numbers  
	public int getNumberOfPages() {
		if (totalPage == -1) {
			System.out.println("getNumberOfPages()");
			Dimension size = null;
			int tableWidth = table.getWidth();
			int tableHeight = table.getHeight();
			int positionX = 0;
			int positionY = 0;

			totalPage = 0;
			while (positionY < tableHeight) {
				positionX = 0;
				while (positionX < tableWidth) {
					size = getPrintSize(totalPage, positionX, positionY);
					positionX += size.width;
					totalPage++;
				}
				positionY += size.height;
			}
		}
		return totalPage;
	}

	//get current page number  
	public int getCurrentNumberOfPages(int currentPositionX, int currentPositionY) {
		if (currentPage != -1) {
			return currentPage;
		}
		System.out.println("getCurrentNumberOfPages()");
		Dimension size = null;
		int tableWidth = table.getWidth();
		int tableHeight = table.getHeight();
		int positionX = 0;
		int positionY = 0;

		int pageIndex = 0;
		while (positionY < tableHeight) {
			positionX = 0;
			while (positionX < tableWidth) {
				size = getPrintSize(pageIndex, positionX, positionY);
				pageIndex++;
				//				System.out.println(currentPositionX);
				//				System.out.println(currentPositionY);
				if ((currentPositionX == positionX) && (currentPositionY == positionY))
					return pageIndex;
				positionX += size.width;
			}
			positionY += size.height;
		}
		return 0;
	}

	//get the printable(the canvas)  
	public Printable getPrintable(int index) {
		return this;
	}

	//get the pageFormat(the layout of the paper)  
	public PageFormat getPageFormat(int index) {
		return (PageFormat) pageFormat;
	}
}