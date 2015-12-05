package com.vastcm.stuhealth.client.framework.report.ui;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;
import com.vastcm.stuhealth.client.utils.BigDecimalUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptDefaultTableModel extends DefaultTableModel implements StyleModel {

	public static final Color[] DEFAULT_GROUP_ROW_COLOR = new Color[] { new Color(253, 253, 244), new Color(230, 230, 255), new Color(210, 255, 210),
			new Color(250, 222, 188), new Color(252, 216, 173), new Color(85, 90, 205), new Color(191, 219, 255), new Color(240, 240, 240),
			new Color(0, 128, 0) };
	protected Map<Integer, Object> statRowIndexMap = new LinkedHashMap<Integer, Object>();
	private boolean isCellStyleOn = true;
	protected CellStyle cellStyle = new CellStyle();

	public RptDefaultTableModel() {
		this(0, 0);
	}

	public RptDefaultTableModel(int paramInt1, int paramInt2) {
		super(paramInt1, paramInt2);
	}

	public RptDefaultTableModel(Vector paramVector, int paramInt) {
		super(paramVector, paramInt);
	}

	public RptDefaultTableModel(Object[] paramArrayOfObject, int paramInt) {
		super(paramArrayOfObject, paramInt);
	}

	public RptDefaultTableModel(Vector paramVector1, Vector paramVector2) {
		super(paramVector1, paramVector2);
	}

	public RptDefaultTableModel(Object[][] paramArrayOfObject, Object[] paramArrayOfObject1) {
		super(paramArrayOfObject, paramArrayOfObject1);
	}

	@Override
	public Class<?> getColumnClass(int paramInt) {
//		return Double.TYPE;
				return super.getColumnClass(paramInt);
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	/**
	 * 根据groupColumnIndex分组小计(本列的内容必须是排序)，合计行放到相应行的后面。根据sumGroupColumnIndex列中的内容
	 * ，汇总统计，结果放到最后 ，按倒序排序。
	 * @param groupColumnIndex
	 * @param sumGroupColumnIndex
	 * @param sumColumnIndexs
	 */
	public void calculatGroupSum2(int groupColumnIndex, int sumGroupColumnIndex, int[] sumColumnIndexs) {
		if (dataVector == null || dataVector.isEmpty())
			return;

		//先按分组内容，统计合计的分类
		Map<String, Vector> sumDatas = new HashMap<String, Vector>();
		String splitor = "_0xx1xx";
		if (sumGroupColumnIndex != -1) {
			Vector sumRowData;
			String sumKey;
			for (int r = 0, rSize = dataVector.size(); r < rSize; r++) {
				Vector rowData = (Vector) dataVector.get(r);
				sumKey = String.valueOf(rowData.get(sumGroupColumnIndex)) + splitor;
				sumRowData = sumDatas.get(sumKey);
				if (sumRowData == null) {
					sumRowData = getEmptyRow(rowData.size());
					sumRowData.set(sumGroupColumnIndex, rowData.get(sumGroupColumnIndex));
					if (sumGroupColumnIndex != 0)
						sumRowData.set(0, "小计");
					else
						sumRowData.set(1, "小计");
					sumDatas.put(sumKey, sumRowData);
				}
				for (int s = 0; s < sumColumnIndexs.length; s++) {
					sumRowData.set(sumColumnIndexs[s], BigDecimalUtil.add(sumRowData.get(sumColumnIndexs[s]), rowData.get(sumColumnIndexs[s])));
				}
			}
		}

		Vector totalRowData = getEmptyRow(((Vector) dataVector.get(0)).size());
		totalRowData.set(0, "合计");
		for (int r = 0, rSize = dataVector.size(); r < rSize; r++) {
			Vector rowData = (Vector) dataVector.get(r);
			for (int s = 0; s < sumColumnIndexs.length; s++) {
				totalRowData.set(sumColumnIndexs[s], BigDecimalUtil.add(totalRowData.get(sumColumnIndexs[s]), rowData.get(sumColumnIndexs[s])));
			}
		}
		if (groupColumnIndex != -1) {
			List<Object> groupRowIndex = new LinkedList<Object>();
			Vector groupRowData = null;
			String groupKey = "";
			for (int r = 0, rSize = dataVector.size(); r < rSize; r++) {
				Vector rowData = (Vector) dataVector.get(r);
				if (!groupKey.equals(String.valueOf(rowData.get(groupColumnIndex)) + splitor)) {//新的分组
					if (groupRowData != null) {//保存当前的位置和合计行数据
						groupRowIndex.add(r);
						groupRowIndex.add(groupRowData);
					}
					groupRowData = getEmptyRow(rowData.size());
					groupRowData.set(groupColumnIndex, rowData.get(groupColumnIndex));
					if (groupColumnIndex != 0)
						groupRowData.set(0, "小计");
					else
						groupRowData.set(1, "小计");
					groupKey = String.valueOf(rowData.get(groupColumnIndex)) + splitor;
				}
				for (int s = 0; s < sumColumnIndexs.length; s++) {
					groupRowData.set(sumColumnIndexs[s], BigDecimalUtil.add(groupRowData.get(sumColumnIndexs[s]), rowData.get(sumColumnIndexs[s])));
				}
			}
			//先加上最后的小计行
			statRowIndexMap.put(dataVector.size() + groupRowIndex.size() / 2, 0);
			dataVector.add(groupRowData);
			//从后向前再插入中间的小计行
			for (int i = groupRowIndex.size() - 1; i > -1;) {
				statRowIndexMap.put((Integer) groupRowIndex.get(i - 1) + (i - 1) / 2, 0);
				dataVector.insertElementAt(groupRowIndex.get(i--), (Integer) groupRowIndex.get(i--));
			}
		}

		if (sumGroupColumnIndex != -1) {
			List<String> list = new LinkedList<String>();
			list.addAll(sumDatas.keySet());
			Collections.sort(list, new Comparator() {
				@Override
				public int compare(Object param1, Object param2) {
					if (param1 == null) {
						if (param2 == null)
							return 0;
						else
							return 1;
					}
					int result = ((String) param1).compareTo((String) param2);
					return -result;
				}
			});
			for (int i = 0; i < list.size(); i++) {
				statRowIndexMap.put(dataVector.size(), 1);
				dataVector.add(sumDatas.get(list.get(i)));
			}
		}
		statRowIndexMap.put(dataVector.size(), 2);
		dataVector.add(totalRowData);
	}

	public void calculatGroupSum2(int[] groupColumnIndexs, int[] sumColumnIndexs, boolean isShowGroupRow) {
		if (dataVector == null || dataVector.isEmpty())
			return;

		//先按分组内容，统计合计的分类
		Map<String, Vector> sumDatas = new HashMap<String, Vector>();
		Vector sumRowData;
		String sumKey;
		String splitor = "_0xx1xx";
		for (int r = 0, rSize = dataVector.size(); r < rSize; r++) {
			Vector rowData = (Vector) dataVector.get(r);
			sumKey = "";
			for (int g = 0; g < groupColumnIndexs.length; g++) {
				sumKey += String.valueOf(rowData.get(groupColumnIndexs[g])) + splitor;
			}
			sumRowData = sumDatas.get(sumKey);
			if (sumRowData == null) {
				sumRowData = getEmptyRow(rowData.size());
				sumDatas.put(sumKey, sumRowData);
			}
			for (int s = 0; s < sumColumnIndexs.length; s++) {
				sumRowData.set(sumColumnIndexs[s], BigDecimalUtil.add(sumRowData.get(sumColumnIndexs[s]), rowData.get(sumColumnIndexs[s])));
			}
		}
	}

	private Vector<Object> getEmptyRow(int size) {
		Vector row = new Vector(size);
		row.setSize(size);
		return row;
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		cellStyle.setText(null);
		cellStyle.setToolTipText(null);
		cellStyle.setBorder(null);
		cellStyle.setIcon(null);
		cellStyle.setBackground(null);
		cellStyle.setForeground(null);
		cellStyle.setFontStyle(-1);
		Integer colorIndex = (Integer) statRowIndexMap.get(row);
		if (colorIndex != null) {
			cellStyle.setBackground(DEFAULT_GROUP_ROW_COLOR[colorIndex]);
		} else {
			cellStyle.setBackground(Color.WHITE);
		}
		return cellStyle;
	}

	public void setCellStyleOn(boolean isCellStyleOn) {
		this.isCellStyleOn = isCellStyleOn;
	}

	@Override
	public boolean isCellStyleOn() {
		return isCellStyleOn;
	}

	public Map<Integer, Object> getStatRowIndexMap() {
		return statRowIndexMap;
	}
}