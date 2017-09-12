package com.school.combobox.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.hibernate.HibernateException;

import com.school.hiebernate.HiebernateDboUtil;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.resource.ResourcesUtils;
import com.school.utils.Logger;

public class ClassComboBoxModel extends AbstractListModel implements
		ComboBoxModel {
	private List<ClassDetails> list;
	Object selection = null;

	public ClassComboBoxModel(List<ClassDetails> list) {
		this.list = list;
	}

	public void addDefaultCLASS() {
		ClassDetails classDetails = new ClassDetails();
		classDetails.setClassName(ResourcesUtils.getString("ALL_CLASSES"));
		classDetails.setClassId(0);
		list.add(0, classDetails);
	}

	public ClassComboBoxModel() {
		try {
			this.list = HiebernateDboUtil.getClassDetails();
			Logger.DEBUG.info("Class Details List is loaded successfully");
		} catch (HibernateException e) {
			list = new ArrayList<ClassDetails>();
			Logger.EXCEPTION.log(Level.WARNING,
					"Error Occured while getting Class Details from DB", e);
		}
	}

	@Override
	public Object getElementAt(int arg0) {
		return list.get(arg0).getClassName();
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
			return list.get(0).getClassName();
		}
		return selection;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selection = anItem;
	}

	public ClassDetails getSelectedObject(int index) {
		return list.get(index);
	}

}
