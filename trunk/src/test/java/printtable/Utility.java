package printtable;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class Utility {

	public Utility() {
	}

	public static double inchToMillimeter(double inch) {
		double millimeter;
		millimeter = inch / 0.0394;
		return millimeter;
	}

	public static int inchToMillimeter(int inch) {
		int millimeter;
		millimeter = (int) ((double) inch / 0.0394);
		return millimeter;
	}

	public static double millimeterToInch(double millimeter) {
		double inch;
		inch = millimeter * 0.0394;
		return inch;
	}

	public static int millimeterToInch(int millimeter) {
		int inch;
		inch = (int) ((double) millimeter * 0.0394);
		return inch;
	}

	public static int dotToInch(int dot) {
		int inch;
		inch = (int) ((double) dot / 72);
		return inch;
	}

	public static double dotToInch(double dot) {
		double inch;
		inch = dot / 72;
		return inch;
	}

	public static int inchToDot(int inch) {
		int dot;
		dot = inch * 72;
		return dot;
	}

	public static double inchToDot(double inch) {
		double dot;
		dot = inch * 72;
		return dot;
	}

	public static int millimeterToDot(int millimeter) {
		int dot;
		double inch;
		inch = Utility.millimeterToInch((double) millimeter);
		dot = (int) Utility.inchToDot(inch);
		return dot;
	}

	public static double millimeterToDot(double millimeter) {
		double dot;
		double inch;
		inch = Utility.millimeterToInch(millimeter);
		dot = Utility.inchToDot(inch);
		return dot;
	}

	public static int dotToMillimeter(int dot) {
		int millimeter;
		double inch;
		inch = Utility.dotToInch((double) dot);
		millimeter = (int) Utility.inchToMillimeter(inch);
		return millimeter;
	}

	public static double dotToMillimeter(double dot) {
		double millimeter;
		double inch;
		inch = Utility.dotToInch(dot);
		millimeter = Utility.inchToMillimeter(inch);
		return millimeter;
	}
}