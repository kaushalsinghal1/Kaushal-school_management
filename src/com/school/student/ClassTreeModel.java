package com.school.student;

import java.util.List;

import javax.swing.event.TreeSelectionListener;

import com.banti.framework.tree.AbstractTreeModel;
import com.school.hiebernate.dbo.ClassDetails;
import com.school.resource.ResourcesUtils;

public class ClassTreeModel extends AbstractTreeModel {

	public ClassTreeModel(List classList,
			TreeSelectionListener listener) {
		super(classList, listener);
	}

	@Override
	public Object defaultObject() {
		ClassDetails classDetails = new ClassDetails();
		classDetails.setClassName(ResourcesUtils.getString("ALL_CLASSES"));
		classDetails.setClassId(0);
		return classDetails;
	}

}
