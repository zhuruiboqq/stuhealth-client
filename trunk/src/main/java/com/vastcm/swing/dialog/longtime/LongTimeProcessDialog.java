package com.vastcm.swing.dialog.longtime;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

import com.vastcm.stuhealth.client.framework.ui.UIHandler;

public class LongTimeProcessDialog extends JDialog {
	private static final String RESOURCE = "com.kingdee.eas.base.permission.PermissionResource";
	private ILongTimeTask longTimeTask = null;

	private static final Color bottomBgColor = new Color(230, 230, 230);

	private Object result = null;

	private Window parent = null;

	public LongTimeProcessDialog(Frame frame) {
		super(frame, "任务进行中...", true);
		this.parent = frame;
	}

	public LongTimeProcessDialog(Dialog dialog) {
		super(dialog, "任务进行中...", true);
		this.parent = dialog;
	}

	public void setLongTimeTask(ILongTimeTask longTimeTask) {
		this.longTimeTask = longTimeTask;
	}

	protected void dialogInit() {
		super.dialogInit();
		setResizable(false);

		setDefaultCloseOperation(0);
		setLocationRelativeTo(null);

		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		Dimension bgPanelDimension = new Dimension(363, 96);
		int normalBottomHeight = 42;
		int advanceBottomHeight = 202;
		contentPane.setPreferredSize(new Dimension(bgPanelDimension.width, bgPanelDimension.height + 42));

		contentPane.setBackground(new Color(16777215));

		JPanel pnlBottom = new JPanel();
		pnlBottom.setLayout(null);
		pnlBottom.setBounds(0, bgPanelDimension.height, bgPanelDimension.width, 202);

		contentPane.add(pnlBottom);
		pnlBottom.setBackground(bottomBgColor);
		Graphics2D g2d = (Graphics2D) pnlBottom.getGraphics();
		if (g2d != null) {
			GradientPaint gradient = new GradientPaint(0.0F, 0.0F, new Color(16777215), 363.0F, 96.0F, new Color(11842740));
			g2d.setPaint(gradient);
		}

		JSeparator sp = new JSeparator();
		sp.setBounds(0, 0, bgPanelDimension.width, 2);
		pnlBottom.add(sp);

		JLabel text = new JLabel("请等待...");
		text.setBounds(44, 29, 279, 20);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(false);
		progressBar.setIndeterminate(true);
		getContentPane().add(text, null);
		getContentPane().add(progressBar, null);
		progressBar.setBounds(new Rectangle(42, 50, 279, 12));
		pack();
		setLocationRelativeTo(null);
	}

	public void show() {
		SwingWorker longTimeTaskWorker = new SwingWorker() {
			public Object construct() {
				try {
					LongTimeProcessDialog.this.longTimeTask.exec();
					LongTimeProcessDialog.this.setVisible(false);
					LongTimeProcessDialog.this.dispose();
					return LongTimeProcessDialog.this.result;
				} catch (Exception e) {
					LongTimeProcessDialog.this.setVisible(false);
					LongTimeProcessDialog.this.dispose();
					UIHandler.handleException(LongTimeProcessDialog.this.parent, e);
				}
				return null;
			}

			public void finished() {
				if (LongTimeProcessDialog.this.result == null)
					return;
				try {
					Thread.sleep(500L);
					LongTimeProcessDialog.this.longTimeTask.afterExec(LongTimeProcessDialog.this.result);
					LongTimeProcessDialog.this.setVisible(false);
					LongTimeProcessDialog.this.dispose();
				} catch (Exception e) {
					UIHandler.handleException(LongTimeProcessDialog.this.parent, e);
				}
			}
		};
		longTimeTaskWorker.start();

		setCursor(this, Cursor.getPredefinedCursor(3));
		super.show();
		setCursor(this, Cursor.getPredefinedCursor(0));
	}

	private void setCursor(Component cp, Cursor cursor) {
		cp.setCursor(cursor);
		if (!(cp instanceof Container))
			return;
		Container cc = (Container) cp;

		int i = 0;
		for (int n = cc.getComponentCount(); i < n; ++i) {
			Component curComponent = cc.getComponent(i);
			setCursor(curComponent, cursor);
		}
	}
}