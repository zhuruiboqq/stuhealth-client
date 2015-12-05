package com.vastcm.stuhealth.client.utils;

import javax.swing.JComponent;

import com.vastcm.swing.dialog.StandardDialog4Detail;

public class MessageDialogUtil {

	public static void showErrorDetail(JComponent parentComponent, String content, String detail) {
		StandardDialog4Detail example = new StandardDialog4Detail("提示信息", content, detail);
		example.pack();
		example.setLocationRelativeTo(null);
		example.setVisible(true);

		//		JideOptionPane optionPane = new JideOptionPane("点击\"详细信息\"查看，明细内容...", JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
		//		optionPane.setTitle(content);
		//		optionPane.setDetails(detail);
		//		optionPane.setDetailsVisible(true);
		//		JDialog dialog = optionPane.createDialog(SwingUtilities.getWindowAncestor(parentComponent), "错误信息");
		//		dialog.setResizable(true);
		//		dialog.pack();
		//		dialog.setVisible(true);

		//		String details = ("java.lang.Exception: Stack trace\n" +
		//                "\tat java.awt.Component.processMouseEvent(Component.java:5957)\n" +
		//                "\tat javax.swing.JComponent.processMouseEvent(JComponent.java:3284)\n" +
		//                "\tat java.awt.Component.processEvent(Component.java:5722)\n" +
		//                "\tat java.awt.Container.processEvent(Container.java:1966)\n" +
		//                "\tat java.awt.Component.dispatchEventImpl(Component.java:4365)\n" +
		//                "\tat java.awt.Container.dispatchEventImpl(Container.java:2024)\n" +
		//                "\tat java.awt.Component.dispatchEvent(Component.java:4195)\n" +
		//                "\tat java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4228)\n" +
		//                "\tat java.awt.LightweightDispatcher.processMouseEvent(Container.java:3892)\n" +
		//                "\tat java.awt.LightweightDispatcher.dispatchEvent(Container.java:3822)\n" +
		//                "\tat java.awt.Container.dispatchEventImpl(Container.java:2010)\n" +
		//                "\tat java.awt.Window.dispatchEventImpl(Window.java:2299)\n" +
		//                "\tat java.awt.Component.dispatchEvent(Component.java:4195)\n" +
		//                "\tat java.awt.EventQueue.dispatchEvent(EventQueue.java:599)\n" +
		//                "\tat java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:273)\n" +
		//                "\tat java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:183)\n" +
		//                "\tat java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:173)\n" +
		//                "\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:168)\n" +
		//                "\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:160)\n" +
		//                "\tat java.awt.EventDispatchThread.run(EventDispatchThread.java:121)");
		//        JideOptionPane optionPane = new JideOptionPane("Click \"Details\" button to see more information ... ", JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
		//        optionPane.setTitle("An exception happened during file transfers - if the title is very long, it will wrap automatically.");
		//        optionPane.setDetails(details);
		//        JDialog dialog = optionPane.createDialog(SwingUtilities.getWindowAncestor(parentComponent), "Warning");
		//        dialog.setResizable(true);
		//        dialog.pack();
		//        dialog.setVisible(true);
	}
}
