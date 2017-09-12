package com.school.combobox.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.hibernate.HibernateException;

import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;

public class SessionComboBoxModel extends AbstractListModel implements
		ComboBoxModel {
	private List<SessionDetails> list;
	Object selection = null;

	public SessionComboBoxModel(List<SessionDetails> list) {
		this.list = list;
		setCurrentDefaultSelection();
	}

	public SessionComboBoxModel() {
		try {
			this.list = HiebernateDboUtil.getSessionDetails();
			Logger.DEBUG.info("Session Details List is loaded successfully");
		} catch (HibernateException e) {
			list = new ArrayList<SessionDetails>();
			Logger.EXCEPTION.log(Level.WARNING,
					"Error Occured while getting Session Details from DB", e);
		}
		setCurrentDefaultSelection();
	}

	@Override
	public Object getElementAt(int arg0) {
		return list.get(arg0).getDisplayString();
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Object getSelectedItem() {
		if (list == null || list.size() == 0) {
			return null;
		}
		if (selection == null) {
			return list.get(0).getDisplayString();
		}
		return selection;
	}

	public void addDefaultSession() {
		SessionDetails session = new SessionDetails();
		session.setDisplayString(ResourcesUtils.getString("ALL_SESSION"));
		session.setSessionId(0);
		session.setStartDate(new Date(1990, 1,1));
		session.setEndDate(new Date(1990, 2,1));
		list.add(0, session);
	}

	public void setCurrentDefaultSelection() {
		if (list != null) {
			Date current = new Date();
			for (Iterator<SessionDetails> iterator = list.iterator(); iterator
					.hasNext();) {
				SessionDetails s = iterator.next();
				if (current.after(s.getStartDate())
						&& current.before(s.getEndDate())) {
					setSelectedItem(s.getDisplayString());
					return;
				}

			}
		}
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selection = anItem;
	}

	public SessionDetails getSelectedObject(int index) {
		return list.get(index);
	}

}
