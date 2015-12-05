package printtable;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PageBorder {
	protected double width;
	protected double height;

	protected double imageableX;
	protected double imageableY;
	protected double imageableWidth;
	protected double imageableHeight;

	protected int borderType;
	public static int LINE = 0;
	public static int BOX = 1;
	public static int NONE = 2;

	protected String leftContent;
	protected String midContent;
	protected String rightContent;

	public PageBorder(double width, double height) {
		this.width = width;
		this.height = height;

		this.imageableX = Utility.millimeterToDot(25);
		this.imageableY = 25;
		this.imageableHeight = height - 2 * imageableY;
		this.imageableWidth = width - 2 * imageableX;

		this.borderType = NONE;

		this.leftContent = "";
		this.midContent = "";
		this.rightContent = "";
	}

	public void setWidth(double newWidth) {
		this.width = newWidth;
	}

	public void setHeight(double newHeight) {
		this.height = newHeight;
	}

	public void setWidth(int newWidth) {
		this.width = (double) newWidth;
	}

	public void setHeight(int newHeight) {
		this.height = (double) newHeight;
	}

	public void setImageableArea(double x, double y, double width, double height) {
		this.imageableX = x;
		this.imageableY = y;
		this.imageableWidth = width;
		this.imageableHeight = height;
	}

	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}

	public void setLeftContent(String newContent) {
		this.leftContent = newContent;
	}

	public void setMidContent(String newContent) {
		this.midContent = newContent;
	}

	public void setRightContent(String newContent) {
		this.rightContent = newContent;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public double getImageableX() {
		return this.imageableX;
	}

	public double getImageableY() {
		return this.imageableY;
	}

	public double getImageableWidth() {
		return this.imageableWidth;
	}

	public double getImageableHeight() {
		return this.imageableHeight;
	}

	public int getBorderType() {
		return this.borderType;
	}

	public String getLeftContent() {
		return this.leftContent;
	}

	public String getMidContent() {
		return this.midContent;
	}

	public String getRightContent() {
		return this.rightContent;
	}
}