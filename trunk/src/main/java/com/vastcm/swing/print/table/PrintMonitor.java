package com.vastcm.swing.print.table;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Title: PrintTable Description: A java jTable Print Programme. Enable set the wighth and highth. Copyright: Copyright
 * (c) 2002 Company: TopShine
 * @author ghostliang
 * @version 1.0
 */

public class PrintMonitor implements Pageable {

	private PrinterJob printerJob;
	private Pageable pageable;
	private JOptionPane optionPane;
	private JDialog statusDialog;

	public PrintMonitor(Pageable newPageable, String jobName) {
		pageable = newPageable;
		printerJob = PrinterJob.getPrinterJob();
		printerJob.setJobName(jobName);
		String[] options = { "取消" };
		optionPane = new JOptionPane("", JOptionPane.INFORMATION_MESSAGE, JOptionPane.CANCEL_OPTION, null, options);
		statusDialog = optionPane.createDialog(null, "打印状态");
	}

	public void performPrint(boolean showDialog) throws PrinterException {
		printerJob.setPageable(this);
		if (showDialog) {
			boolean isOK = printerJob.printDialog();
			if (!isOK)
				return;
		}
		optionPane.setMessage("初始化打印 ...");
		Thread newThread = new Thread(new Runnable() {
			public void run() {
				try {
					printerJob.print();
				} catch (PrinterException e) {
					e.printStackTrace();
				}
				statusDialog.setVisible(false);
			}
		});
		newThread.start();

		statusDialog.setVisible(true);
	}

	public int getNumberOfPages() {
		return pageable.getNumberOfPages();
	}

	public Printable getPrintable(int index) {
		optionPane.setMessage("正在打印：" + (index + 1) + "/" + pageable.getNumberOfPages());
		if (optionPane.getValue() != JOptionPane.UNINITIALIZED_VALUE)
			printerJob.cancel();
		return pageable.getPrintable(index);
	}

	public PageFormat getPageFormat(int index) {
		PageFormat pageFormat = pageable.getPageFormat(index);
		if (pageFormat instanceof ExtPageFormat) {
			((ExtPageFormat) pageFormat).setPrinting(true);
		}
		return pageFormat;
	}
}