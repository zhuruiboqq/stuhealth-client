package com.vastcm.stuhealth.client;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.stuhealth.client.framework.ui.ScreenUtils;
import com.vastcm.stuhealth.client.framework.ui.UIFrameworkUtils;
import com.vastcm.swing.selector.ISelectorPopupUI;
import com.vastcm.swing.selector.SelectorParam;

public class SchoolTreeSelectorPopupUI extends SchoolTreePanel implements ISelectorPopupUI {

	private static final long serialVersionUID = 1L;
	protected javax.swing.JButton btnOK;
	protected javax.swing.JButton btnCancel;
	protected Dialog dialog;
	private boolean isCancel;
	private SelectorParam selectorParam;

	public SchoolTreeSelectorPopupUI() {
		super();
		btnOK = new javax.swing.JButton();
		btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/system_save.gif")));
		btnOK.setText("确定");
		btnOK.setFocusable(true);
		btnOK.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnOK.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOKActionPerformed(evt);
			}
		});

		btnCancel = new javax.swing.JButton();
		btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/door_out.png")));
		btnCancel.setText("取消");
		btnCancel.setFocusable(true);
		btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		jToolBar.remove(btnImportAuthFile);
		jToolBar.remove(btnSchoolEdit);
		jToolBar.remove(btnRemoveSchool);
		jToolBar.add(btnOK, 0);
		jToolBar.add(btnCancel, 1);

		isCancel = true;
	}

	private void btnCancelActionPerformed(AWTEvent evt) {
		isCancel = true;
		close();
	}

	private void btnOKActionPerformed(AWTEvent evt) {
		//		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) getValue();
		//		SchoolTreeNode schoolNode = (SchoolTreeNode)treeNode.getUserObject();
		//		if(schoolNode.getType()!=SchoolTreeNode.TYPE_CLASS){
		//			JOptionPane.showMessageDialog(dialog, "仅允许选择班级");
		//		}
		isCancel = false;
		close();
	}

	private void close() {
		dialog.setVisible(false);
	}

	@Override
	protected void addTreeListener() {
		super.addTreeListener();
		getTree().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() >= 2) {
					btnOKActionPerformed(evt);
				}
			}
		});
	}

	@Override
	public Object getValue() {//覆盖父类的取数方法，以便取得上级节点的内容
		TreePath[] pathArray = jTree1.getSelectionPaths();
		if (pathArray == null || pathArray.length == 0)
			return null;
		if (pathArray.length == 1)
			return pathArray[0].getLastPathComponent();
		Object[] values = new Object[pathArray.length];
		for (int i = 0; i < pathArray.length; i++) {
			values[i] = pathArray[i].getLastPathComponent();
		}
		return values;
		//		return jTree1.getLastSelectedPathComponent();
	}

	@Override
	public void show() {
		if (dialog == null) {
			Window parent = SwingUtilities.getWindowAncestor(this);
			if (parent != null) {
				if (parent instanceof Frame) {
					dialog = new Dialog((Frame) parent);
				} else if (parent instanceof Dialog) {
					dialog = new Dialog((Dialog) parent);
				} else {
					dialog = new Dialog(parent);
				}
			} else {
				dialog = new Dialog(UIFrameworkUtils.getMainUI());
			}
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent paramWindowEvent) {
					btnCancelActionPerformed(paramWindowEvent);
				}
			});
			dialog.setSize(300, 500);
			dialog.add(this);
			ScreenUtils.center(dialog);
			dialog.setModal(true);
		}

		dialog.setVisible(true);
	}

	@Override
	public void setValue(Object value) {

	}

	@Override
	public void setRptParamInfo(RptParamInfo rptParamInfo) {

	}

	@Override
	public RptParamInfo getRptParamInfo() {
		return null;
	}

	@Override
	public boolean isCancel() {
		return isCancel;
	}

	@Override
	public void setSelectorParam(SelectorParam selectorParam) {
		this.selectorParam = selectorParam;
		if (selectorParam.isEnableMultiSelect())
			getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		else
			getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	@Override
	public SelectorParam getSelectorParam() {
		return selectorParam;
	}

}
