package com.vastcm.swing.selector;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.util.StringUtils;

import com.vastcm.stuhealth.client.framework.report.RptParamInfo;
import com.vastcm.swing.selector.event.DataChangeEvent;
import com.vastcm.swing.selector.event.DataChangeListener;
import com.vastcm.swing.selector.event.PreChangeEvent;
import com.vastcm.swing.selector.event.PreChangeListener;
import com.vastcm.swing.selector.event.PreShowEvent;
import com.vastcm.swing.selector.event.PreShowListener;

public class JSelectorBox extends JPanel implements ISelectorPopupUI {

	private static final long serialVersionUID = 2814777654384974503L;

	private boolean isSelectorPopupUIChange;
	private boolean isSelectorPopupUIShow;
	private JFormattedTextField textField;
	private JButton button;

	private ISelectorPopupUI selectorPopupUI;
	private InternalEventHandler internalEventHandler;

	private RptParamInfo rptParamInfo;
	private SelectorParam selectorParam;
	private HashSet<ActionListener> actionListeners;
	private HashSet<DataChangeListener> dataChangeListeners;

	public JSelectorBox() {
		this(null);
	}

	public JSelectorBox(ISelectorPopupUI selectorPopupUI) {
		this.selectorPopupUI = selectorPopupUI;

		//Initialise Variables
		isSelectorPopupUIChange = true;
		isSelectorPopupUIShow = false;
		internalEventHandler = new InternalEventHandler();
		actionListeners = new LinkedHashSet<ActionListener>();
		dataChangeListeners = new LinkedHashSet<DataChangeListener>();

		//Create Layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		//Create and Add Components
		//Add and Configure TextField
		textField = new JFormattedTextField(createDefaultFormatter());
		if (selectorPopupUI != null) {
			textField.setValue(selectorPopupUI.getValue());
		}
		textField.setEditable(false);
		add(textField);
		layout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, textField);

		//Add and Configure Button
		button = new JButton("...");
		button.setFocusable(true);
		add(button);
		layout.putConstraint(SpringLayout.WEST, button, 1, SpringLayout.EAST, textField);
		layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, button);
		layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, button);

		//Do layout formatting
		int h = (int) button.getPreferredSize().getHeight();
		int w = (int) this.getWidth();
		button.setPreferredSize(new Dimension(h, h));
		textField.setPreferredSize(new Dimension(w - h - 1, h));

		button.addActionListener(internalEventHandler);
		textField.addPropertyChangeListener("value", internalEventHandler);

		selectorParam = new SelectorParam();
	}

	protected JFormattedTextField.AbstractFormatter createDefaultFormatter() {
		return new SelectorComponentFormatter();
	}

	public void setTextEditable(boolean editable) {
		textField.setEditable(editable);
	}

	public boolean isTextEditable() {
		return textField.isEditable();
	}

	public void setButtonFocusable(boolean focusable) {
		button.setFocusable(focusable);
	}

	public boolean getButtonFocusable() {
		return button.isFocusable();
	}

	public ISelectorPopupUI getSelectorPopupUI() {
		return selectorPopupUI;
	}

	public JFormattedTextField getJFormattedTextField() {
		return textField;
	}

	/**
	 * Called internally to popup the dates.
	 */
	private void showPopup() {
		if (!isSelectorPopupUIShow) {
			if (selectorPopupUI == null) {
				throw new IllegalArgumentException("selectorPopupUI can not be null!");
			}
			if (isSelectorPopupUIChange) {
				selectorPopupUI.setValue(textField.getValue());
				selectorPopupUI.setRptParamInfo(rptParamInfo);
				selectorPopupUI.setSelectorParam(selectorParam);
			}
			if (!(doFirePreShowEvent())) {
				return;
			}
			isSelectorPopupUIShow = true;
			selectorPopupUI.show();
			if (!selectorPopupUI.isCancel()) {
				Object newValue = selectorPopupUI.getValue();
				Object oldValue = textField.getValue();
				PreChangeEvent e = new PreChangeEvent(this, newValue, oldValue);
				if (firePreChange(e)) {
					textField.setValue(newValue);
					fireActionPerformed();
					fireDataChangedPerformed(newValue, oldValue);
				}
			}
			isSelectorPopupUIShow = false;
		}
	}

	public void addActionListener(ActionListener actionListener) {
		actionListeners.add(actionListener);
	}

	public void removeActionListener(ActionListener actionListener) {
		actionListeners.remove(actionListener);
	}

	private void fireActionPerformed() {
		for (ActionListener actionListener : actionListeners) {
			actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Data changed"));
		}
	}

	public void addDataChangedListener(DataChangeListener actionListener) {
		dataChangeListeners.add(actionListener);
	}

	public void removeDataChangedListener(DataChangeListener actionListener) {
		dataChangeListeners.remove(actionListener);
	}

	protected void fireDataChangedPerformed(Object newValue, Object oldValue) {
		DataChangeEvent evt = new DataChangeEvent(this, newValue, oldValue);
		fireDataChangedPerformed(evt);
	}

	protected void fireDataChangedPerformed(DataChangeEvent evt) {
		for (DataChangeListener actionListener : dataChangeListeners) {
			actionListener.dataChanged(evt);
		}
	}

	public void addPreChangeListener(PreChangeListener listener) {
		this.listenerList.add(PreChangeListener.class, listener);
	}

	public void removePreChangeListener(PreChangeListener listener) {
		this.listenerList.remove(PreChangeListener.class, listener);
	}

	public PreChangeListener[] getPreChangeListeners() {
		return ((PreChangeListener[]) this.listenerList.getListeners(PreChangeListener.class));
	}

	protected boolean firePreChange(PreChangeEvent e) {
		boolean ret = true;
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PreChangeListener.class) {
				((PreChangeListener) listeners[(i + 1)]).preChange(e);
				ret &= e.succeeded();
			}
			if (!(ret))
				return false;
		}
		return true;
	}

	public void addPreShowListener(PreShowListener listener) {
		this.listenerList.add(PreShowListener.class, listener);
	}

	public void removePreShowListener(PreShowListener listener) {
		this.listenerList.remove(PreShowListener.class, listener);
	}

	public PreShowListener[] getPreShowListeners() {
		return ((PreShowListener[]) this.listenerList.getListeners(PreShowListener.class));
	}

	protected boolean doFirePreShowEvent() {
		PreShowEvent PreShowEvent = new PreShowEvent(this);
		fireSelectorWillShow(PreShowEvent);

		return (!(PreShowEvent.isCanceled()));
	}

	protected void fireSelectorWillShow(PreShowEvent PreShowEvent) {
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] != PreShowListener.class)
				continue;
			((PreShowListener) listeners[(i + 1)]).preShow(PreShowEvent);
		}
	}

	/**
	 * This internal class hides the public event methods from the outside
	 */
	private class InternalEventHandler implements ActionListener, ChangeListener, PropertyChangeListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == button) {
				showPopup();
			}
		}

		public void stateChanged(ChangeEvent event) {
			//			if (event.getSource() == datePanel.getModel()) {
			//				DateModel<?> model = datePanel.getModel();
			//				setTextFieldValue(textField, model.getYear(), model.getMonth(), model.getDay(), model.isSelected());
			//			}
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (textField.isEditable()) {
				if (!StringUtils.hasText(textField.getText())) {
					Object oldValue = textField.getValue();
					textField.setValue(null);
					fireDataChangedPerformed(null, oldValue);
				} else {

				}

			}
		}

	}

	@Override
	public Object getValue() {
		return textField.getValue();
	}

	@Override
	public void setValue(Object value) {
		textField.setValue(value);
	}

	@Override
	public void setRptParamInfo(RptParamInfo rptParamInfo) {
		this.rptParamInfo = rptParamInfo;
	}

	@Override
	public RptParamInfo getRptParamInfo() {
		return rptParamInfo;
	}

	@Override
	public boolean isCancel() {
		return false;
	}

	@Override
	public void setSelectorParam(SelectorParam param) {
		this.selectorParam = param;
	}

	@Override
	public SelectorParam getSelectorParam() {
		return selectorParam;
	}
}
