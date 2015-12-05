package printtable;

import java.awt.print.*;
import javax.swing.*;

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
		String[] options = { "Cancel" };
		optionPane = new JOptionPane("", JOptionPane.INFORMATION_MESSAGE, JOptionPane.CANCEL_OPTION, null, options);
		statusDialog = optionPane.createDialog(null, "Printing Status");
	}

	public void performPrint(boolean showDialog) throws PrinterException {
		printerJob.setPageable(this);
		if (showDialog) {
			boolean isOK = printerJob.printDialog();
			if (!isOK)
				return;
		}
		optionPane.setMessage("Initiating Printing ...");
		Thread newThread = new Thread(new Runnable() {
			public void run() {
				statusDialog.setVisible(true);
				if (optionPane.getValue() != JOptionPane.UNINITIALIZED_VALUE)
					printerJob.cancel();
			}
		});
		newThread.start();
		printerJob.print();
		statusDialog.setVisible(false);
	}

	public int getNumberOfPages() {
		return pageable.getNumberOfPages();
	}

	public Printable getPrintable(int index) {
		optionPane.setMessage("Printing Page:" + (index + 1) + "/" + pageable.getNumberOfPages());
		return pageable.getPrintable(index);
	}

	public PageFormat getPageFormat(int index) {
		return pageable.getPageFormat(index);
	}
}