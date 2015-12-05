package com.vastcm.swing.dialog.longtime;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.stuhealth.client.framework.ui.UIHandler;

public class LongTimeTaskDialog extends JDialog {
	private static final String TITLE = "执行任务中...";

	private Component parent = null;

	private LongTimeTaskAdapter taskAdpter = null;

	public LongTimeTaskDialog() {
		setTitle(TITLE);
	}

	public LongTimeTaskDialog(Frame frame) {
		super(frame, TITLE, true);
		this.parent = frame;
	}

	public LongTimeTaskDialog(Frame frame, String title) {
		super(frame, title, true);
		this.parent = frame;
	}

	public LongTimeTaskDialog(Dialog dialog) {
		super(dialog, TITLE, true);
		this.parent = dialog;
	}

	public LongTimeTaskDialog(Dialog dialog, String title) {
		super(dialog, title, true);
		this.parent = dialog;
	}

	public static LongTimeTaskDialog getInstance(String title, LongTimeTaskAdapter taskAdpter) {
		LongTimeTaskDialog dlg = null;
		Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
		if (owner == null) {
			owner = UIFrameworkUtils.getMainUI();
		}
		Window ownerWindow = SwingUtilities.getWindowAncestor(owner);
		if (ownerWindow instanceof Frame)
			dlg = new LongTimeTaskDialog((Frame) ownerWindow);
		else if (ownerWindow instanceof Dialog)
			dlg = new LongTimeTaskDialog((Dialog) ownerWindow);
		else {
			dlg = new LongTimeTaskDialog((Frame) null);
		}
		if (taskAdpter != null) {
			dlg.setGetDataTask(taskAdpter);
		}
		if (title != null) {
			dlg.setTitle(title);
		}
		return dlg;
	}

	public static LongTimeTaskDialog getInstance(LongTimeTaskAdapter taskAdpter) {
		return getInstance(null, taskAdpter);
	}

	public static LongTimeTaskDialog getInstance(String title) {
		return getInstance(title, null);
	}

	public static LongTimeTaskDialog getInstance() {
		return getInstance(null, null);
	}

	public void setGetDataTask(LongTimeTaskAdapter taskAdpter) {
		this.taskAdpter = taskAdpter;
	}

	protected void dialogInit() {
		super.dialogInit();
		setResizable(false);
		setSize(200, 70);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(0);
		setLocationRelativeTo(null);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(false);
		progressBar.setIndeterminate(true);
		getContentPane().add(progressBar, null);
		progressBar.setBounds(new Rectangle(10, 10, 172, 19));
	}

	public void show() {
		Thread thread = new Thread() {
			public void run() {
				try {
					Object obj = LongTimeTaskDialog.this.taskAdpter.exec();
					LongTimeTaskDialog.this.taskAdpter.finish(obj);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							LongTimeTaskDialog.this.dispose();
						}
					});
				} catch (Exception e) {
					LongTimeTaskDialog.this.dispose();
					UIHandler.handleException(parent, e);
				}
			}
		};
		thread.start();
		super.show();
	}
}
