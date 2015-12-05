package com.vastcm.swing.print.table;

import java.awt.print.Paper;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the
 * wighth and highth. Copyright: Copyright (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PaperSetting {
	private static Paper paper = new Paper();

	protected static final double inchesPerMillimeter = 0.0394;

	protected static final int A4_WIDTH = (int) (210 * inchesPerMillimeter * 72);
	protected static final int A4_HEIGHT = (int) (297 * inchesPerMillimeter * 72);

	protected static final int A5_WIDTH = (int) (148 * inchesPerMillimeter * 72);
	protected static final int A5_HEIGHT = (int) (210 * inchesPerMillimeter * 72);

	protected static final int B5_WIDTH = (int) (182 * inchesPerMillimeter * 72);
	protected static final int B5_HEIGHT = (int) (257 * inchesPerMillimeter * 72);

	protected static final int DEVELOP_C5_WIDTH = (int) (162 * inchesPerMillimeter * 72);
	protected static final int DEVELOP_C5_HEIGHT = (int) (229 * inchesPerMillimeter * 72);

	protected static final int DEVELOP_DL_WIDTH = (int) (110 * inchesPerMillimeter * 72);
	protected static final int DEVELOP_DL_HEIGHT = (int) (220 * inchesPerMillimeter * 72);

	protected static final int DEVELOP_B5_WIDTH = (int) (176 * inchesPerMillimeter * 72);
	protected static final int DEVELOP_B5_HEIGHT = (int) (250 * inchesPerMillimeter * 72);

	protected static final int DEVELOP_MONARCH_WIDTH = (int) (3.875 * 72);
	protected static final int DEVELOP_MONARCH_HEIGHT = (int) (7.5 * 72);

	protected static final int DEVELOP_9_WIDTH = (int) (3.875 * 72);
	protected static final int DEVELOP_9_HEIGHT = (int) (8.875 * 72);

	protected static final int DEVELOP_10_WIDTH = (int) (4.125 * 72);
	protected static final int DEVELOP_10_HEIGHT = (int) (9.5 * 72);

	protected static final int LETTER_WIDTH = (int) (8.5 * 72);
	protected static final int LETTER_HEIGHT = (int) (11 * 72);

	protected static final int LEGAL_WIDTH = (int) (8.5 * 72);
	protected static final int LEGAL_HEIGHT = (int) (14 * 72);

	protected static final int margin = (int) (0.0394 * 25 * 72);

	public PaperSetting() {
	}

	public static Paper getA4() {
		paper.setSize(A4_WIDTH, A4_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	public static Paper getA5() {
		paper.setSize(A5_WIDTH, A5_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	public static Paper getB5() {
		paper.setSize(B5_WIDTH, B5_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelop9() {
		paper.setSize(DEVELOP_9_WIDTH, DEVELOP_9_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelop10() {
		paper.setSize(DEVELOP_10_WIDTH, DEVELOP_10_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelopC5() {
		paper.setSize(DEVELOP_C5_WIDTH, DEVELOP_C5_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelopMonarch() {
		paper.setSize(DEVELOP_MONARCH_WIDTH, DEVELOP_MONARCH_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelopDl() {
		paper.setSize(DEVELOP_DL_WIDTH, DEVELOP_DL_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getDevelopB5() {
		paper.setSize(DEVELOP_B5_WIDTH, DEVELOP_B5_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getLetter() {
		paper.setSize(LETTER_WIDTH, LETTER_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static Paper getLegal() {
		paper.setSize(LEGAL_WIDTH, LEGAL_HEIGHT);
		paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
		return paper;
	}

	static int getIndex(double width, double height) {
		if (PaperSetting.getA4().getWidth() == width && PaperSetting.getA4().getHeight() == height)
			return 0;

		if (PaperSetting.getA5().getWidth() == width && PaperSetting.getA5().getHeight() == height)
			return 1;

		if (PaperSetting.getB5().getWidth() == width && PaperSetting.getB5().getHeight() == height)
			return 2;

		if (PaperSetting.getDevelopC5().getWidth() == width && PaperSetting.getDevelopC5().getHeight() == height)
			return 3;

		if (PaperSetting.getDevelopDl().getWidth() == width && PaperSetting.getDevelopDl().getHeight() == height)
			return 4;

		if (PaperSetting.getDevelopB5().getWidth() == width && PaperSetting.getDevelopB5().getHeight() == height)
			return 5;

		if (PaperSetting.getDevelopMonarch().getWidth() == width && PaperSetting.getDevelopMonarch().getHeight() == height)
			return 6;

		if (PaperSetting.getDevelop9().getWidth() == width && PaperSetting.getDevelop9().getHeight() == height)
			return 7;

		if (PaperSetting.getDevelop10().getWidth() == width && PaperSetting.getDevelop10().getHeight() == height)
			return 8;

		if (PaperSetting.getLetter().getWidth() == width && PaperSetting.getLetter().getHeight() == height)
			return 9;

		if (PaperSetting.getLegal().getWidth() == width && PaperSetting.getLegal().getHeight() == height)
			return 10;

		return 11;
	}
}