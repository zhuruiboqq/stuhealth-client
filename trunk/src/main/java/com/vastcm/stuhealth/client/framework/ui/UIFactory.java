/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vastcm.stuhealth.client.framework.ui;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author House
 */
public class UIFactory {

	private static Map<Class, UI> uiPool = new HashMap<Class, UI>();

	private UIFactory() {
	}

	public static <T> UI<T> create(Class<T> uiClass, UIType uiType, UIContext ctx, JFrame owner) throws Exception {
		UI<T> ui = null;
		if (UIType.TAB.equals(uiType)) {
			ui = UIFactory.uiPool.get(uiClass);
			if (ui != null) {
				return ui;
			}
		}

		Object obj = null;
		try {
			obj = uiClass.newInstance();
		} catch (InstantiationException ex) {
			throw new UICreationException(ex);
		} catch (IllegalAccessException ex) {
			throw new UICreationException(ex);
		}
		if (obj == null || !(obj instanceof KernelUI)) {
			throw new IllegalArgumentException("Variable uiClass must assignable from com.vastcm.stuhealth.client.framework.ui.KernelUI");
		}
		final KernelUI uiObject = (KernelUI) obj;
		if (ctx.get(UIContext.UI_STATUS) == null) {
			ctx.set(UIContext.UI_STATUS, UIStatusEnum.NEW);
		}
		uiObject.setUIContext(ctx);
		uiObject.onLoad();
		String uiTitle = (String) ctx.get(UIContext.UI_TITLE);
		Window win = null;
		if (UIType.MODAL.equals(uiType)) {
			JDialog w = new JDialog(owner, uiTitle);
			w.add(uiObject, BorderLayout.CENTER);
			w.setModal(true);
			if(ctx.get(UIContext.UI_CAN_CLOSE) != null) {
				boolean canClose = (Boolean) ctx.get(UIContext.UI_CAN_CLOSE);
				w.setUndecorated(!canClose);
			}
			w.pack();
			win = w;
		}
		if (UIType.WINDOW.equals(uiType)) {
			JFrame w = new JFrame(uiTitle);
			w.add(uiObject, BorderLayout.CENTER);
			if(ctx.get(UIContext.UI_CAN_CLOSE) != null) {
				boolean canClose = (Boolean) ctx.get(UIContext.UI_CAN_CLOSE);
				w.setUndecorated(!canClose);
			}
			w.pack();
			win = w;
		}
		if (UIType.TAB.equals(uiType)) {
			JPanel p = new JPanel();
			p.add(uiObject, BorderLayout.CENTER);
			win = null;
			UIFactory.uiPool.put(uiClass, null);
		}
		if (win != null) {
			BufferedImage bImg = ImageIO.read(UIFactory.class.getResource("/image/logo_2.png"));
			win.setIconImage(bImg);
		}
		ui = new UI<T>(uiObject, win, ctx);
		UIFactory.uiPool.put(uiClass, ui);

		return ui;
	}

	public static void disposeUI(Class uiClass) {
		uiPool.remove(uiClass);
	}

}
