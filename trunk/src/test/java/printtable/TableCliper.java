package printtable;

import java.awt.*;
import java.awt.print.*;

import java.util.*;

import java.text.*;

import javax.swing.*;
import javax.swing.table.*;

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
	private boolean showHead;
	private PageBorder foot;
	private boolean showFoot;

	private int headerStatus;
	private int tableAlignment;
	private int tableScale;

	private ExtPageFormat pageFormat;

	//init the class  
	public TableCliper(JTable newTable, ExtPageFormat newPageFormat) {
		table = newTable;

		tableAlignment = newPageFormat.getTableAlignment();
		headerStatus = newPageFormat.getHeaderType();
		tableScale = newPageFormat.getTableScale();

		head = newPageFormat.getHead();
		foot = newPageFormat.getFoot();

		showHead = newPageFormat.getShowHead();
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
				size = getPrintSize(positionX, positionY);
				if (pageIndex == index) {
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
	protected Dimension getPrintSize(int positionX, int positionY) {
		Rectangle rect;

		int printWidth;
		int printHeight;

		int firstCol = table.columnAtPoint(new Point(positionX, positionY));
		int firstRow = table.columnAtPoint(new Point(positionX, positionY));

		int maxWidth = (int) (pageFormat.getImageableWidth() * 100 / tableScale);
		int maxHeight = (int) (pageFormat.getImageableHeight() * 100 / tableScale);

		if (displayHeaderOnPage(positionY)) {
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
		Graphics2D newGraphics2D = (Graphics2D) newGraphics;

		int allOffsetX = 0;
		int allOffsetY = 0;

		//if head is exist,show it on the paper  
		if (showHead) {
			int headWidth = (int) head.getWidth();
			int headHeight = (int) head.getHeight();

			int headImageableX = (int) head.getImageableX();
			int headImageableY = (int) head.getImageableY();
			int headImageableWidth = (int) head.getImageableWidth();
			int headImageableHeight = (int) head.getImageableHeight();

			newGraphics.setColor(Color.white);
			newGraphics.fillRect(0, 0, headWidth, headHeight);

			//print the border of the head  
			if (head.getBorderType() == 0) {
				newGraphics.setColor(Color.black);
				newGraphics.drawLine(headImageableX, headImageableY + headImageableHeight, headImageableWidth + headImageableX, headImageableY
						+ headImageableHeight);
			} else if (head.getBorderType() == 1) {
				newGraphics.setColor(Color.black);
				newGraphics.drawRect(headImageableX, headImageableY, headImageableWidth, headImageableHeight);
			}

			//print string of head  
			StringBuffer content;
			StringBuffer bakContent;

			Date localDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String localNowDate = sdf.format(localDate);

			if (head.leftContent != "") {
				content = new StringBuffer(head.leftContent);
				bakContent = new StringBuffer(head.leftContent);

				while (head.leftContent.indexOf('@') != -1 && head.leftContent.indexOf('@') != head.leftContent.length() - 1) {
					int flag = head.leftContent.indexOf('@');
					switch (head.leftContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						head.leftContent = content.toString();
						break;
					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						head.leftContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						head.leftContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						head.leftContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				head.leftContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), headImageableX + 10, headImageableY + (headImageableHeight / 2 + 6));
			}

			if (head.midContent != "") {
				content = new StringBuffer(head.midContent);
				bakContent = new StringBuffer(head.midContent);

				while (head.midContent.indexOf('@') != -1 && head.midContent.indexOf('@') != head.midContent.length() - 1) {
					int flag = head.midContent.indexOf('@');
					switch (head.midContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						head.midContent = content.toString();
						break;
					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						head.midContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						head.midContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						head.midContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				head.midContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), headImageableX + (headImageableWidth / 2) - content.toString().length() * 4,
						headImageableY + (headImageableHeight / 2 + 6));
			}

			if (head.rightContent != "") {
				content = new StringBuffer(head.rightContent);
				bakContent = new StringBuffer(head.rightContent);

				while (head.rightContent.indexOf('@') != -1 && head.rightContent.indexOf('@') != head.rightContent.length() - 1) {
					int flag = head.rightContent.indexOf('@');
					switch (head.rightContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						head.rightContent = content.toString();
						break;
					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						head.rightContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						head.rightContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						head.rightContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				head.rightContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), headImageableX + headImageableWidth - content.toString().length() * 8, headImageableY
						+ (headImageableHeight / 2 + 6));
			}

			newGraphics.translate(0, headHeight);
			allOffsetX += 0;
			allOffsetY += headHeight;
		}

		//print the main paper  
		//a offset from the border of the paper  
		int offsetX = (int) (pageFormat.getImageableX());
		int offsetY = (int) (pageFormat.getImageableY());

		//the size of the paper  
		int paperWidth = (int) (pageFormat.getWidth());
		int paperHeight = (int) (pageFormat.getHeight());

		//draw main paper  
		newGraphics.setColor(Color.white);
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
		if (displayHeaderOnPage(positionY)) {
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

			table.paint(newGraphics);

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

			table.paint(newGraphics);

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
		allOffsetX += 0;
		allOffsetY += paperHeight;

		//if foot is exist,show it on the paper  
		if (showFoot) {
			int footWidth = (int) foot.getWidth();
			int footHeight = (int) foot.getHeight();

			int footImageableX = (int) foot.getImageableX();
			int footImageableY = (int) foot.getImageableY();
			int footImageableWidth = (int) foot.getImageableWidth();
			int footImageableHeight = (int) foot.getImageableHeight();

			newGraphics.setColor(Color.white);
			newGraphics.fillRect(0, 0, footWidth, footHeight);

			//print the border of the foot  
			if (foot.getBorderType() == 0) {
				newGraphics.setColor(Color.black);
				newGraphics.drawLine(footImageableX, footImageableY, footImageableWidth + footImageableX, footImageableY);
			} else if (foot.getBorderType() == 1) {
				newGraphics.setColor(Color.black);
				newGraphics.drawRect(footImageableX, footImageableY, footImageableWidth, footImageableHeight);
			}

			//print string of foot  
			StringBuffer content;
			StringBuffer bakContent;

			Date localDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String localNowDate = sdf.format(localDate);

			if (foot.leftContent != "") {
				content = new StringBuffer(foot.leftContent);
				bakContent = new StringBuffer(foot.leftContent);

				while (foot.leftContent.indexOf('@') != -1 && foot.leftContent.indexOf('@') != foot.leftContent.length() - 1) {
					int flag = foot.leftContent.indexOf('@');
					switch (foot.leftContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						foot.leftContent = content.toString();
						break;

					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						foot.leftContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						foot.leftContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						foot.leftContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				foot.leftContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), footImageableX + 10, footImageableY + (footImageableHeight / 2 + 6));
			}

			if (foot.midContent != "") {
				content = new StringBuffer(foot.midContent);
				bakContent = new StringBuffer(foot.midContent);

				while (foot.midContent.indexOf('@') != -1 && foot.midContent.indexOf('@') != foot.midContent.length() - 1) {
					int flag = foot.midContent.indexOf('@');
					switch (foot.midContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						foot.midContent = content.toString();
						break;
					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						foot.midContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						foot.midContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						foot.midContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				foot.midContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), footImageableX + (footImageableWidth / 2) - content.toString().length() * 4,
						footImageableY + (footImageableHeight / 2 + 6));
			}

			if (foot.rightContent != "") {
				content = new StringBuffer(foot.rightContent);
				bakContent = new StringBuffer(foot.rightContent);

				while (foot.rightContent.indexOf('@') != -1 && foot.rightContent.indexOf('@') != foot.rightContent.length() - 1) {
					int flag = foot.rightContent.indexOf('@');
					switch (foot.rightContent.charAt(flag + 1)) {
					case 'd':
						content.replace(flag, flag + 2, localNowDate);
						foot.rightContent = content.toString();
						break;
					case 'p':
						content.replace(flag, flag + 2, "" + this.getCurrentNumberOfPages(positionX, positionY));
						foot.rightContent = content.toString();
						break;
					case 't':
						content.replace(flag, flag + 2, "" + this.getNumberOfPages());
						foot.rightContent = content.toString();
						break;
					default:
						content.replace(flag, flag + 1, "#$");
						foot.rightContent = content.toString();
						break;
					}
				}

				for (int index = 0; index < content.length() - 1; index++)
					if (content.charAt(index) == '#' && content.charAt(index + 1) == '$')
						content.replace(index, index + 2, "@");

				foot.rightContent = bakContent.toString();

				newGraphics.setColor(Color.black);
				newGraphics.drawString(content.toString(), footImageableX + footImageableWidth - content.toString().length() * 8, footImageableY
						+ (footImageableHeight / 2 + 6));
			}

			newGraphics.translate(0, footHeight);
			allOffsetX += 0;
			allOffsetY += footHeight;
		}

		//restore all offset  
		newGraphics.translate(-allOffsetX, -allOffsetY);
	}

	//decide whether display the header or not  
	protected boolean displayHeaderOnPage(int positionY) {
		if ((headerStatus == 0) || ((headerStatus == 1) && positionY == 0))
			return true;
		else
			return false;
	}

	//get the total page numbers  
	public int getNumberOfPages() {
		Dimension size = null;
		int tableWidth = table.getWidth();
		int tableHeight = table.getHeight();
		int positionX = 0;
		int positionY = 0;

		int pageIndex = 0;
		while (positionY < tableHeight) {
			positionX = 0;
			while (positionX < tableWidth) {
				size = getPrintSize(positionX, positionY);
				positionX += size.width;
				pageIndex++;
			}
			positionY += size.height;
		}
		return pageIndex;
	}

	//get current page number  
	public int getCurrentNumberOfPages(int currentPositionX, int currentPositionY) {
		Dimension size = null;
		int tableWidth = table.getWidth();
		int tableHeight = table.getHeight();
		int positionX = 0;
		int positionY = 0;

		int pageIndex = 0;
		while (positionY < tableHeight) {
			positionX = 0;
			while (positionX < tableWidth) {
				size = getPrintSize(positionX, positionY);
				pageIndex++;
				System.out.println(currentPositionX);
				System.out.println(currentPositionY);
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