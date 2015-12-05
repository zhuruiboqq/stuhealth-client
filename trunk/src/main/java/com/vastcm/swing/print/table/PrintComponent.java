package com.vastcm.swing.print.table;

import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;

import javax.swing.*;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PrintComponent extends JPanel {
	private static final long serialVersionUID = 1L;

	/* 
	 *The item to be printed 
	 */
	protected Printable printable;

	/* 
	 *PageFormat to use when printing 
	 */
	protected PageFormat pageFormat;

	/* 
	 *The page that is currently displayed 
	 */
	protected int displayPage;

	/* 
	 *The scale factor(1.0 = 100%) 
	 */
	protected double scaleFactor;

	//init the component  
	public PrintComponent(Printable newPrintable, PageFormat newPageFormat) {
		this.setPrintabel(newPrintable);
		this.setPageFormat(newPageFormat);
		this.setDisplayPage(0);
		this.setScaleFactor(100);
		this.setBackground(Color.gray);
	}

	//get and set some variable  
	public void setPrintabel(Printable newPrintable) {
		printable = newPrintable;
		revalidate();
	}

	public void setPageFormat(PageFormat newPageFormat) {
		pageFormat = newPageFormat;
		revalidate();
	}

	public void setDisplayPage(int page) {
		displayPage = page;
		revalidate();
	}

	public void setScaleFactor(double scale) {
		scaleFactor = scale;
		revalidate();
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	//get the print size  
	public Dimension getSizeWithScale(double scale) {
		Paper paper = pageFormat.getPaper();
		int width;
		int height;
		if (pageFormat.getOrientation() == 1) {
			width = ((int) (paper.getWidth() * scale / 100));
			height = ((int) (paper.getHeight() * scale / 100));
		} else {
			height = ((int) (paper.getWidth() * scale / 100));
			width = ((int) (paper.getHeight() * scale / 100));
		}

		return new Dimension(width, height);
	}

	public Dimension getPreferredSize() {
		return getSizeWithScale(scaleFactor);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public void paintComponent(Graphics newGraphics) {
		super.paintComponent(newGraphics);
		Graphics2D newGraphics2D = (Graphics2D) newGraphics;

		int allOffsetX = 0;
		int allOffsetY = 0;

		//save infomation of the Graphics  
		AffineTransform newAffineTransform = newGraphics2D.getTransform();

		//set the paper in the mid of the printComponent Panel  
		int thisWidth = this.getWidth();
		int thisHeight = this.getHeight();

		Dimension size = this.getSizeWithScale(scaleFactor);

		int offsetX = (thisWidth - size.width) / 2;
		int offsetY = (thisHeight - size.height) / 2;

		newGraphics2D.translate(offsetX, offsetY);
		allOffsetX += offsetX;
		allOffsetY += offsetY;

		newGraphics2D.scale(scaleFactor / 100, scaleFactor / 100);
		try {
			printable.print(newGraphics, pageFormat, displayPage);
		} catch (PrinterException e) {
		}

		newGraphics2D.translate(-allOffsetX, -allOffsetY);

		newGraphics2D.setTransform(newAffineTransform);
	}
}