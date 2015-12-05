/**
 * 
 */
package com.vastcm.stuhealth.client;

import com.vastcm.stuhealth.client.entity.Notice;
import com.vastcm.stuhealth.client.entity.service.INoticeService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.stuhealth.client.utils.ExceptionUtils;
import com.vastcm.swing.jtable.MyRow;
import com.vastcm.swing.jtable.MyTable;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 16, 2013
 */
public class NoticeListPanel extends KernelUI {
	private Logger logger = LoggerFactory.getLogger(NoticeListPanel.class);
 	private MyTable tblMain;
	public NoticeListPanel() {
		initComponents();
		initData();
	}
	
	public void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		tblMain = new MyTable();
		scrollPane.setViewportView(tblMain);
		
		String[] colNames = new String[] { "公告标题", "通知内容详见", "通知时间" };
		buildTable(colNames);
	}
	
	public INoticeService getService() {
		return AppContext.getBean("noticeService", INoticeService.class);
	}

	public void initData() {
		List<Notice> noticeLs = getService().getAll();
		for(Notice c : noticeLs) {
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getNoticeTitle());
			r.setValue(1, c.getUrl());
			r.setValue(2, c.getCreateDate());
		}
	}
	
	private void buildTable(String[] colNames) {
		tblMain.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {

	            },
	            colNames
	        ) {
	            public Class getColumnClass(int columnIndex) {
	                return String.class;
	            }
	            
	            @Override
	            public boolean isCellEditable(int rowIndex, int colIndex) {
	            	return false;
	            }
	        });
		tblMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2 ){
					int rowIndex = tblMain.getSelectedRow();
					String url = (String) tblMain.getValueAt(rowIndex, 1);
					if(url != null && !url.isEmpty() ) {
						try {
							Desktop.getDesktop().browse(new URI(url));
						} catch (IOException e1) {
							ExceptionUtils.writeExceptionLog(logger, e1);
						} catch (URISyntaxException e1) {
							ExceptionUtils.writeExceptionLog(logger, e1);
						}
					}
				}
			}
		});
	}
}
