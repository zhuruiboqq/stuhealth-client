/**
 * 
 */
package com.vastcm.swing.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 18, 2013
 */
public class PopWin extends JDialog {
	
	private JComponent com;
	
	public PopWin(JComponent contentComponent) {
		super();
		this.com = contentComponent;
		init();
	}
	
	protected void init() {
		 setUndecorated(true);//不显示窗口边框和标题
         setSize(300, 100);
         setLayout(null);
         final JLabel jLabel=new JLabel("X");//放在右上角做关闭按钮
         jLabel.setFont(new Font("宋体", 0, 14));
         getContentPane().setBackground(new Color(255, 255, 255));
         JPanel p=((JPanel)getContentPane());
         p.setBorder(new LineBorder(new java.awt.Color(10,110,10), 1, false));
         setBounds(Toolkit.getDefaultToolkit().getScreenSize().width-305, 
         Toolkit.getDefaultToolkit().getScreenSize().height-135, 300, 100);
         getContentPane().add(jLabel);
         jLabel.setBounds(280, 0, 20, 20);
         com.setBounds(20, 30, 280, 30);
         p.add(com);
         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        jLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					dispose();
				}
				public void mouseEntered(MouseEvent e) {     
					super.mouseEntered(e);
					jLabel.setForeground(Color.red);
				}
				public void mouseExited(MouseEvent e) {
					super.mouseExited(e);
					jLabel.setForeground(Color.BLACK);
				}
        });
	}
	
}
