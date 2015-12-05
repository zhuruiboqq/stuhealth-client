package com.vastcm.swing.jtree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.vastcm.stuhealth.client.framework.ui.MenuItemInfo;

public class MyTreeCellRender extends DefaultTreeCellRenderer {
	//定义图标和要显示的字符串  
	ImageIcon icon = null;
	String str = null;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

			if (node.getUserObject() instanceof MenuItemInfo) {
				MenuItemInfo menuItem = (MenuItemInfo) node.getUserObject();
				if (menuItem.getIcon() != null) {
					setIcon(menuItem.getIcon());
				}
				setText(menuItem.getUiTitle());
			}
		}
		return this;
	}
}