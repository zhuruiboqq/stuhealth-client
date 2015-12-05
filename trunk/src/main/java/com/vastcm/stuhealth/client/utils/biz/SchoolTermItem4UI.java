package com.vastcm.stuhealth.client.utils.biz;

public class SchoolTermItem4UI {

	public static final int First_Term = 1;
	public static final int Second_Term = 2;

	private int year;
	private int term;

	public SchoolTermItem4UI(int year, int term) {
		super();
		this.year = year;
		this.term = term;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SchoolTermItem4UI)) {
			return false;
		}
		SchoolTermItem4UI temp = (SchoolTermItem4UI) obj;
		return (this.year == temp.year) && (this.term == temp.term);
	}

	@Override
	public String toString() {
		return year + "-" + (year + 1) + "学年";//+ (term == First_Term ? "学年上学期" : "学年下学期");
	}
}