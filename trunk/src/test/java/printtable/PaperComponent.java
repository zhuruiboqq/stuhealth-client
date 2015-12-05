package printtable;
  
import java.awt.*;  
import java.awt.geom.*;  
import java.awt.print.*;  
  
import javax.swing.*;  
import javax.swing.border.*;  
  
/** 
 * Title:        PrintTable 
 * Description:  A java jTable Print Programme. 
 * Enable set the wighth and highth. 
 * Copyright:    Copyright (c) 2002 
 * Company:      TopShine 
 * @author ghostliang 
 * @version 1.0 
 */  
  
public class PaperComponent extends JPanel{  
  /* 
   *The page that is currently displayed 
   */  
  protected ExtPageFormat pageFormat;  
  
  /* 
   *The scale factor(1.0 = 100%) 
   */  
  double scaleFactor;  
  
  public PaperComponent(ExtPageFormat newPageFormat)  
  {  
    this.setPageFormat(newPageFormat);  
    this.setScaleFactor(20);  
    this.setBackground(Color.gray);  
  }  
  
  public void setPaper(Paper newPaper)  
  {  
    this.pageFormat.setPaper(newPaper);  
    revalidate();  
  }  
  
  public Paper getPaper()  
  {  
    return this.pageFormat.getPaper();  
  }  
  
  public void setPageFormat(ExtPageFormat newPageFormat)  
  {  
    this.pageFormat = newPageFormat;  
    revalidate();  
  }  
  
  public ExtPageFormat getPageFormat()  
  {  
    return this.pageFormat;  
  }  
  
  public double getScaleFactor()  
  {  
    return scaleFactor;  
  }  
  
  public void setScaleFactor(int newScaleFactor)  
  {  
    this.scaleFactor = newScaleFactor;  
    revalidate();  
  }  
  
  public Dimension getSizeWithScale(double scale)  
  {  
    int width = ((int)(pageFormat.getFullWidth() * scale / 100 ));  
    int height = ((int)(pageFormat.getFullHeight() * scale / 100 ));  
  
    return new Dimension(width,height);  
  }  
  
  public Dimension getPreferredSize()  
  {  
    return getSizeWithScale(scaleFactor);  
  }  
  
  public Dimension getMinimumSize()  
  {  
    return getPreferredSize();  
  }  
  
  public Dimension getImageableSize()  
  {  
    Insets insets = getInsets();  
    int width = ((int)(pageFormat.getImageableWidth()));  
    int height = ((int)(pageFormat.getImageableHeight()));  
  
    return new Dimension(width,height);  
  }  
  
  public void paintComponent(Graphics newGraphics)  
  {  
    super.paintComponent(newGraphics);  
    Graphics2D newGraphics2D = (Graphics2D)newGraphics;  
  
    int allOffsetX = 0;  
    int allOffsetY = 0;  
  
    int headHeight = (int)((pageFormat.getShowHead())?(pageFormat.getHeadHeight()  * scaleFactor / 100):0.0);  
    int footHeight = (int)((pageFormat.getShowFoot())?(pageFormat.getFootHeight()  * scaleFactor / 100):0.0);  
  
    //get paperSize  
    int paperWidth = (int)(pageFormat.getWidth() * scaleFactor / 100);  
    int fullPaperHeight = (int)((pageFormat.getFullHeight()) * scaleFactor / 100);  
    int paperHeight = (int)(pageFormat.getHeight() * scaleFactor / 100);  
  
    //get head/foot infomation  
    int headWidth = (int)(pageFormat.getShowHead()?paperWidth:0.0);  
    int footWidth = (int)(pageFormat.getShowFoot()?paperWidth:0.0);  
  
    //set the paper in the mid of the printComponent Panel  
    int thisWidth = this.getWidth();  
    int thisHeight = this.getHeight();  
  
    int offsetX = (thisWidth - paperWidth) / 2;  
    int offsetY = (thisHeight - fullPaperHeight) / 2;  
  
    //offset to enable area  
    newGraphics2D.translate(offsetX,offsetY);  
    allOffsetX += offsetX;  
    allOffsetY += offsetY;  
  
    //print head area  
    if(pageFormat.getShowHead())  
    {  
        int headImageableX = (int)(pageFormat.getHeadImageableX()  * scaleFactor / 100);  
        int headImageableY = (int)(pageFormat.getHeadImageableY()  * scaleFactor / 100);  
        int headImageableWidth = (int)(pageFormat.getHeadImageableWidth()  * scaleFactor / 100);  
        int headImageableHeight = (int)(pageFormat.getHeadImageableHeight()  * scaleFactor / 100);  
  
        newGraphics2D.setColor(Color.black);  
        newGraphics2D.drawRect(-1,-1,headWidth + 1,headHeight + 1);  
  
        newGraphics2D.setColor(Color.white);  
        newGraphics2D.fillRect(0,0,headWidth,headHeight);  
  
        newGraphics2D.setColor(Color.black);  
        newGraphics2D.drawRect(headImageableX - 1,headImageableY - 1,headImageableWidth + 1,headImageableHeight + 1);  
  
        newGraphics2D.setColor(Color.blue);  
        newGraphics2D.fillRect(headImageableX,headImageableY,headImageableWidth ,headImageableHeight);  
  
        newGraphics2D.translate(0,headHeight);  
        allOffsetX += 0;  
        allOffsetY += headHeight;  
    }  
  
    //draw the area of the paper  
    newGraphics2D.setColor(Color.white);  
    newGraphics2D.fillRect(0,0,paperWidth,paperHeight);  
  
    //draw the border of the paper  
    newGraphics2D.setColor(Color.black);  
    newGraphics2D.drawRect(-1,-1,paperWidth + 1,paperHeight + 1);  
  
    //get offset infomation  
    int imageableX = (int)(pageFormat.getImageableX() * scaleFactor / 100d);  
    int imageableY = (int)(pageFormat.getImageableY() * scaleFactor / 100d);  
    int imageableWidth = (int)(pageFormat.getImageableWidth() * scaleFactor / 100d);  
    int imageableHeight = (int)(pageFormat.getImageableHeight() * scaleFactor / 100d);  
  
    //draw the area of the imageable  
  
    newGraphics2D.setColor(Color.lightGray);  
    newGraphics2D.fillRect(imageableX,imageableY,imageableWidth,imageableHeight);  
  
    //draw the border of the imageable  
    newGraphics2D.setColor(Color.black);  
    newGraphics2D.drawRect(imageableX - 1,imageableY - 1,imageableWidth + 1,imageableHeight +1);  
  
    newGraphics2D.translate(0,paperHeight);  
    allOffsetX += 0;  
    allOffsetY += paperHeight;  
  
    //print foot area  
    if(pageFormat.getShowFoot())  
    {  
        int footImageableX = (int)(pageFormat.getFootImageableX()  * scaleFactor / 100);  
        int footImageableY = (int)(pageFormat.getFootImageableY()  * scaleFactor / 100);  
        int footImageableWidth = (int)(pageFormat.getFootImageableWidth()  * scaleFactor / 100);  
        int footImageableHeight = (int)(pageFormat.getFootImageableHeight()  * scaleFactor / 100);  
  
        newGraphics2D.setColor(Color.black);  
        newGraphics2D.drawRect(-1,-1,footWidth + 1,footHeight + 1);  
  
        newGraphics2D.setColor(Color.white);  
        newGraphics2D.fillRect(0,0,footWidth,footHeight);  
  
        newGraphics2D.setColor(Color.black);  
        newGraphics2D.drawRect(footImageableX - 1,footImageableY - 1,footImageableWidth + 1,footImageableHeight + 1);  
  
        newGraphics2D.setColor(Color.blue);  
        newGraphics2D.fillRect(footImageableX,footImageableY,footImageableWidth ,footImageableHeight);  
  
        newGraphics2D.translate(0,footHeight);  
        allOffsetX += 0;  
        allOffsetY += footHeight;  
    }  
  
    newGraphics2D.translate(-allOffsetX,-allOffsetY);  
  }  
}