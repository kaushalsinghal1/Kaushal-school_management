package com.school.combobox.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.school.hiebernate.dbo.SessionDetails;
import com.school.utils.PeriodDetails;
import com.school.utils.Utils;

public class PeriodComboBoxModel extends AbstractListModel implements
		ComboBoxModel {
	private List<PeriodDetails> list;
	Object selection = null;

	public PeriodComboBoxModel(List<PeriodDetails> list) {
		this.list = list;
	}

	public PeriodComboBoxModel() {
		list = new ArrayList<PeriodDetails>();
		list.add(Utils.getTodaysPeriod());
		list.add(Utils.getYesterdayPeriod());
		list.add(Utils.getThisWeekPeriod());
		list.add(Utils.getLastWeekPeriod());
	}

	@Override
	public Object getElementAt(int arg0) {
		return list.get(arg0).getPeriod();
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
			return list.get(0).getPeriod();
		}
		return selection;
	}

	public void setPeriodFronSession(SessionDetails sessionDetails) {
		List<PeriodDetails> periodDetails = new ArrayList<PeriodDetails>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		if (cal.getTime().before(sessionDetails.getStartDate())) {
			this.list = periodDetails;
			fireContentsChanged(this, 0, list.size());
			return;
		}
		if (cal.getTime().after(sessionDetails.getEndDate())) {
			PeriodDetails details = new PeriodDetails();
			details.setPeriod("All Months");
			details.setStartDate(sessionDetails.getStartDate());
			details.setStartDate(sessionDetails.getEndDate());
		} else {
			periodDetails.add(Utils.getTodaysPeriod());
			periodDetails.add(Utils.getYesterdayPeriod());
			periodDetails.add(Utils.getThisWeekPeriod());
			periodDetails.add(Utils.getLastWeekPeriod());
		}

		// --
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(sessionDetails.getStartDate());
		startCal.set(Calendar.DATE, 1);
		Calendar end = Calendar.getInstance();
		end.setTime(sessionDetails.getEndDate());
		PeriodDetails pDetails = null;
		while (startCal.before(end) && startCal.before(Calendar.getInstance())) {
			int i = startCal.get(Calendar.MONTH);
			pDetails = new PeriodDetails();
			pDetails.setPeriod(Utils.getMonthString(i));
			pDetails.setStartDate(startCal.getTime());
			startCal.add(Calendar.MONTH, 1);
			startCal.add(Calendar.DATE, -1);
			pDetails.setEndDate(startCal.getTime());
			startCal.add(Calendar.DATE, 1);
			periodDetails.add(pDetails);
		}
		this.list = periodDetails;
		fireContentsChanged(this, 0, list.size());
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selection = anItem;
	}

	public PeriodDetails getSelectedObject(int index) {
		return list.get(index);
	}

}
