package com.banti.framework.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;

public class SchoolOpertaionAction extends AbstractAction {

	private String actionCommand;
	private List<ActionListener> listenerList;

	public SchoolOpertaionAction(String dispalyString, String actionCommand) {
		super(dispalyString);
		this.actionCommand = actionCommand;
		super.putValue(ACTION_COMMAND_KEY, actionCommand);
		listenerList = new ArrayList<ActionListener>();
	}

	public void registerActionListener(ActionListener listener) {
		listenerList.add(listener);

	}

	@Override
	public void setEnabled(boolean newValue) {
		// super.setEnabled(true);
		if (newValue && ApplicationOpertaion.isAllowed(actionCommand)) {
			super.setEnabled(true);
		} else {
			super.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (Iterator<ActionListener> iterator = listenerList.iterator(); iterator
				.hasNext();) {
			ActionListener listener = iterator.next();
			listener.actionPerformed(arg0);
		}

	}

}
