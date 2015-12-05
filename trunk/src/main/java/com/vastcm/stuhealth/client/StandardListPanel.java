/**
 * 
 */
package com.vastcm.stuhealth.client;


import com.vastcm.stuhealth.client.entity.BmiStandard;
import com.vastcm.stuhealth.client.entity.ChestStandard;
import com.vastcm.stuhealth.client.entity.HeightStandard;
import com.vastcm.stuhealth.client.entity.LungsStandard;
import com.vastcm.stuhealth.client.entity.NutritionStandard;
import com.vastcm.stuhealth.client.entity.PulsationStandard;
import com.vastcm.stuhealth.client.entity.SchoolTreeNode;
import com.vastcm.stuhealth.client.entity.WeightStandard;
import com.vastcm.stuhealth.client.entity.service.IBmiStandardService;
import com.vastcm.stuhealth.client.entity.service.IChestStandardService;
import com.vastcm.stuhealth.client.entity.service.IHeightStandardService;
import com.vastcm.stuhealth.client.entity.service.ILungsStandardService;
import com.vastcm.stuhealth.client.entity.service.INutritionStandardService;
import com.vastcm.stuhealth.client.entity.service.IPulsationStandardService;
import com.vastcm.stuhealth.client.entity.service.IWeightStandardService;
import com.vastcm.stuhealth.client.framework.ui.KernelUI;
import com.vastcm.swing.jtable.MyRow;
import com.vastcm.swing.jtable.MyTable;

import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author house
 * @email  yyh2001@gmail.com
 * @since  Jun 8, 2013
 */
public class StandardListPanel extends KernelUI {
	
	private Logger logger = LoggerFactory.getLogger(StandardListPanel.class);
	private MyTable tblMain;
	private JTree treeMain;
	private JComboBox cbVersion;
	

	/**
	 * Create the panel.
	 */
	public StandardListPanel() {
		initComponents();
	}
	
	public void initComponentsEx() {
		
	}
	
	public void initTree() {
		
	}
	
	public void treeNodeSelectChanged(TreeSelectionEvent e) {
		 TreePath path = treeMain.getSelectionPath();
	     DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
	     String nodeName = node.getUserObject().toString();
	     if("身高".equals(nodeName)) {
	    	 makeHeightTable();
	     }
	     if("体重".equals(nodeName)) {
	    	 makeWeightTable();
	     }
	     if("胸围".equals(nodeName)) {
	    	 makeChestTable();
	     }
	     if("脉博".equals(nodeName)) {
			 makePulsationTable();
	     }
	     if("肺活量".equals(nodeName)) {
			 makeLungsTable();
	     }
	     if("营养评价".equals(nodeName)) {
			 makeNutritionTable();
	     }
	     if("BMI评价".equals(nodeName)) {
			 makeBmiTable();
	     }
	}
	
	private void rebuildTable(String[] colNames) {
		tblMain.setModel(new javax.swing.table.DefaultTableModel(
	            new Object [][] {

	            },
	            colNames
	        ) {
	            public Class getColumnClass(int columnIndex) {
	                return String.class;
	            }
	        });
	}
	
	private void rebuildVersionComboBox(Object[] version) {
		cbVersion.removeAllItems();
		cbVersion.addItem("ALL");
		for(Object v : version) {
			cbVersion.addItem(v);
		}
	}
	
	protected Set<String> _makeChestTable(List<ChestStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(ChestStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP10());
			r.setValue(4, c.getP25());
			r.setValue(5, c.getP75());
			r.setValue(6, c.getP90());
		}
		return versionSet;
		
	}
	
	public List<String> orderVersion(Set<String> versions) {
		List<String> orderedVersions = new ArrayList<String>(versions);
		Collections.sort(orderedVersions, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		});
		
		return orderedVersions;
	}
	
	protected void makeChestTable() {
		final IChestStandardService srv = AppContext.getBean("chestStandardService", IChestStandardService.class);
		List<ChestStandard> ls = srv.getAll();
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions = _makeChestTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeChestTable(srv.getAll());
				}
				else {
					_makeChestTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeChestTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected Set<String> _makeHeightTable(List<HeightStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(HeightStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP10());
			r.setValue(4, c.getP25());
			r.setValue(5, c.getP75());
			r.setValue(6, c.getP90());
		}
		return versionSet;
		
	}
	
	protected void makeHeightTable() {
		final IHeightStandardService srv = AppContext.getBean("heightStandardService", IHeightStandardService.class);
		List<HeightStandard> ls = srv.getAll();
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions =_makeHeightTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeHeightTable(srv.getAll());
				}
				else {
					_makeHeightTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeHeightTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected Set<String> _makeLungsTable(List<LungsStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(LungsStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP10());
			r.setValue(4, c.getP25());
			r.setValue(5, c.getP75());
			r.setValue(6, c.getP90());
		}
		return versionSet;
		
	}
	
	protected void makeLungsTable() {
		final ILungsStandardService srv = AppContext.getBean("lungsStandardService", ILungsStandardService.class);
		List<LungsStandard> ls = srv.getAll();
		
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions = _makeLungsTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeLungsTable(srv.getAll());
				}
				else {
					_makeLungsTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeLungsTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected Set<String> _makeNutritionTable(List<NutritionStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年级", "身高", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(NutritionStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getGrade());
			r.setValue(3, c.getSg());
			r.setValue(4, c.getP10());
			r.setValue(5, c.getP25());
			r.setValue(6, c.getP75());
			r.setValue(7, c.getP90());
		}
		return versionSet;
		
	}
	
	protected void makeNutritionTable() {
		final INutritionStandardService srv = AppContext.getBean("nutritionStandardService", INutritionStandardService.class);
		List<NutritionStandard> ls = srv.getAll();
		
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		
		Set<String> versions = _makeNutritionTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeNutritionTable(srv.getAll());
				}
				else {
					_makeNutritionTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeNutritionTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected Set<String> _makePulsationTable(List<PulsationStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(PulsationStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP10());
			r.setValue(4, c.getP25());
			r.setValue(5, c.getP75());
			r.setValue(6, c.getP90());
		}
		return versionSet;
		
	}
	
	protected void makePulsationTable() {
		final IPulsationStandardService srv = AppContext.getBean("pulsationStandardService", IPulsationStandardService.class);
		List<PulsationStandard> ls = srv.getAll();
		
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions = _makePulsationTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makePulsationTable(srv.getAll());
				}
				else {
					_makePulsationTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makePulsationTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected Set<String> _makeWeightTable(List<WeightStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P10", "P25", "P75", "P90" };
		rebuildTable(colNames);
		for(WeightStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP10());
			r.setValue(4, c.getP25());
			r.setValue(5, c.getP75());
			r.setValue(6, c.getP90());
		}
		return versionSet;
	}
	
	protected Set<String> _makeBmiTable(List<BmiStandard> ls) {
		Set<String> versionSet = new TreeSet<String>();
		String[] colNames = new String[] { "版本", "性别", "年龄", "P1", "P2" };
		rebuildTable(colNames);
		for(BmiStandard c : ls) {
			versionSet.add(c.getVersion());
			MyRow r = tblMain.addRow();
			r.setValue(0, c.getVersion());
			r.setValue(1, c.getSex());
			r.setValue(2, c.getAge());
			r.setValue(3, c.getP1());
			r.setValue(4, c.getP2());
		}
		return versionSet;
		
	}
	
	protected void makeWeightTable() {
		final IWeightStandardService srv = AppContext.getBean("weightStandardService", IWeightStandardService.class);
		List<WeightStandard> ls = srv.getAll();
		
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions = _makeWeightTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeWeightTable(srv.getAll());
				}
				else {
					_makeWeightTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeWeightTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	protected void makeBmiTable() {
		final IBmiStandardService srv = AppContext.getBean("bmiStandardService", IBmiStandardService.class);
		List<BmiStandard> ls = srv.getAll();
		
		ItemListener[] ils = cbVersion.getItemListeners();
		for(ItemListener il : ils) {
			cbVersion.removeItemListener(il);
		}
		Set<String> versions = _makeBmiTable(ls);
		cbVersion.removeAllItems();
		for(String v : orderVersion(versions)) {
			cbVersion.addItem(v);
		}
		cbVersion.addItem("ALL");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String ver = (String) e.getItem();
				if("ALL".equals(ver)) {
					_makeBmiTable(srv.getAll());
				}
				else {
					_makeBmiTable(srv.getStandardByVersion(ver));
				}
			}
		});
		_makeBmiTable(srv.getStandardByVersion(cbVersion.getSelectedItem().toString()));
	}
	
	public void initComponents() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		treeMain = new JTree();
		treeMain.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				treeNodeSelectChanged(e);
			}
		});
		treeMain.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("体检标准") {
				{
					add(new DefaultMutableTreeNode("身高"));
					add(new DefaultMutableTreeNode("体重"));
					add(new DefaultMutableTreeNode("胸围"));
					add(new DefaultMutableTreeNode("脉博"));
					add(new DefaultMutableTreeNode("肺活量"));
					add(new DefaultMutableTreeNode("营养评价"));
					add(new DefaultMutableTreeNode("BMI评价"));
				}
			}
		));
		scrollPane.setViewportView(treeMain);
		
		JScrollPane spTable = new JScrollPane();
		splitPane.setRightComponent(spTable);
		
		tblMain = new MyTable();
		spTable.setViewportView(tblMain);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setPreferredSize(new Dimension(800, 35));
		panel.setLayout(null);
		
		cbVersion = new JComboBox();
		cbVersion.setBounds(92, 6, 120, 25);
		panel.add(cbVersion);
		
		JLabel lblVersion = new JLabel("版本");
		lblVersion.setBounds(6, 6, 80, 25);
		panel.add(lblVersion);
		
		initComponentsEx();
	}
	
}
