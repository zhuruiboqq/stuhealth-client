package printtable;

import java.awt.print.*;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class ExtPageFormat extends PageFormat {
	private PageBorder head = new PageBorder(this.getPaper().getWidth(), this.getPaper().getHeight() * 15 / 100);

	private PageBorder foot = new PageBorder(this.getPaper().getWidth(), this.getPaper().getHeight() * 15 / 100);

	private boolean showHead = false;
	private boolean showFoot = false;

	private int headerType = ALLPAGE;
	public static int ALLPAGE = 0;
	public static int TOPPAGE = 1;
	public static int NONE = 2;

	private int tableAlignment = LEFT;
	public static int LEFT = 0;
	public static int MID = 1;
	public static int RIGHT = 2;

	private int tableScale = 100;

	private String jobName = "ghostliang";

	public ExtPageFormat() {
		super();
	}

	public void setHeadHeight(double newHeight) {
		head.height = newHeight;
	}

	public void setHeadHeight(int newHeight) {
		head.height = (double) newHeight;
	}

	public void setHeadImageableArea(double x, double y, double width, double height) {
		head.imageableX = x;
		head.imageableY = y;
		head.imageableWidth = width;
		head.imageableHeight = height;
	}

	public void setHeadImageableX(double x) {
		head.imageableX = x;
	}

	public void setHeadImageableY(double y) {
		head.imageableY = y;
	}

	public void setHeadImageableWidth(double width) {
		head.imageableWidth = width;
	}

	public void setHeadImageableHeight(double height) {
		head.imageableHeight = height;
	}

	public void setHeadBorderType(int borderType) {
		head.borderType = borderType;
	}

	public void setHeadLeftContent(String newContent) {
		head.leftContent = newContent;
	}

	public void setHeadMidContent(String newContent) {
		head.midContent = newContent;
	}

	public void setHeadRightContent(String newContent) {
		head.rightContent = newContent;
	}

	public double getHeadWidth() {
		return head.width;
	}

	public double getHeadHeight() {
		return head.height;
	}

	public double getHeadImageableX() {
		return head.imageableX;
	}

	public double getHeadImageableY() {
		return head.imageableY;
	}

	public double getHeadImageableWidth() {
		return head.imageableWidth;
	}

	public double getHeadImageableHeight() {
		return head.imageableHeight;
	}

	public int getHeadBorderType() {
		return head.borderType;
	}

	public String getHeadLeftContent() {
		return head.leftContent;
	}

	public String getHeadMidContent() {
		return head.midContent;
	}

	public String getHeadRightContent() {
		return head.rightContent;
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

	public void setHeaderType(int newHeaderType) {
		this.headerType = newHeaderType;
	}

	public int getHeaderType() {
		return this.headerType;
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

	public void setShowHead(boolean isShow) {
		if (showHead == isShow)
			return;

		showHead = isShow;
	}

	public boolean getShowHead() {
		return this.showHead;
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
		Paper paper = this.getPaper();

		double x = paper.getImageableX();
		double y = paper.getImageableY();
		double h = paper.getImageableHeight();

		double ph = paper.getHeight();

		if (this.getOrientation() == 1)
			return x;
		else
			return (ph - y - h);
	}

	public double getImageableY() {
		Paper paper = this.getPaper();

		double x = paper.getImageableX();
		double y = paper.getImageableY();

		double hh = ((showHead) ? head.height : 0);

		if (this.getOrientation() == 1)
			return y;
		else
			return x;
	}

	public double getImageableWidth() {
		Paper paper = this.getPaper();

		double w = paper.getImageableWidth();
		double h = paper.getImageableHeight();

		if (this.getOrientation() == 1)
			return w;
		else
			return h;
	}

	public double getImageableHeight() {
		Paper paper = this.getPaper();

		double w = paper.getImageableWidth();
		double h = paper.getImageableHeight();

		double hh = ((showHead) ? head.height : 0);
		double fh = ((showFoot) ? foot.height : 0);

		if (this.getOrientation() == 1)
			return (h - hh - fh);
		else
			return (w - hh - fh);
	}

	public double getHeight() {
		Paper paper = this.getPaper();

		double pw = paper.getWidth();
		double ph = paper.getHeight();

		double hh = ((showHead) ? head.height : 0);
		double fh = ((showFoot) ? foot.height : 0);

		if (this.getOrientation() == 1)
			return (ph - hh - fh);
		else
			return (pw - hh - fh);
	}

	public double getWidth() {
		Paper paper = this.getPaper();

		double pw = paper.getWidth();
		double ph = paper.getHeight();

		if (this.getOrientation() == 1)
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
		if (this.getOrientation() == 1) {
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
	}

	public double getFullWidth() {
		if (this.getOrientation() == 1)
			return this.getPaper().getWidth();
		else
			return this.getPaper().getHeight();
	}

	public double getFullHeight() {
		if (this.getOrientation() == 1)
			return this.getPaper().getHeight();
		else
			return this.getPaper().getWidth();
	}

	public PageBorder getHead() {
		return this.head;
	}

	public PageBorder getFoot() {
		return this.foot;
	}
}