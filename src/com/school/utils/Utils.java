package com.school.utils;

import java.util.Calendar;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;

public class Utils {

	public static String generateRegistrationNumber(
			SessionDetails sessionDetails, ClassDetails classDetails,
			String firstName) {
		StringBuffer sb = new StringBuffer();
		sb.append(classDetails.getClassName());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(sessionDetails.getStartDate().getTime());
		int i = cal.get(Calendar.YEAR);
		sb.append(i % 100);
		String s = firstName.length() > 3 ? firstName.substring(0, 3)
				: firstName;
		sb.append(s);
		return sb.toString();
	}

	/**
	 * 
	 * @param index
	 *            start from 0-11
	 * @return month string
	 */
	public static String getMonthString(int index) {
		switch (index) {
		case 0:
			return "January";
		case 1:
			return "Februry";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		}
		return "";
	}

	public static PeriodDetails getTodaysPeriod() {
		PeriodDetails details = new PeriodDetails();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		details.setPeriod("Todays");
		details.setStartDate(cal.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		details.setEndDate(cal.getTime());
		return details;
	}

	public static PeriodDetails getYesterdayPeriod() {
		PeriodDetails details = new PeriodDetails();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		details.setPeriod("Yesterday");
		details.setStartDate(cal.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		details.setEndDate(cal.getTime());
		return details;
	}

	// Week Mon - Sun
	// Sun 1, sat 7
	public static PeriodDetails getThisWeekPeriod() {
		PeriodDetails details = new PeriodDetails();
		Calendar cal = Calendar.getInstance();

		details.setPeriod("This Week");
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		details.setEndDate(cal.getTime());
		int i = cal.get(Calendar.DAY_OF_WEEK);
		i = i - 2 >= 0 ? i - 2 : 7 - i;
		cal.add(Calendar.DATE, -i);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		details.setStartDate(cal.getTime());
		return details;
	}

	// Week Mon - Sun
	// Sun 1, sat 7
	public static PeriodDetails getLastWeekPeriod() {
		PeriodDetails details = new PeriodDetails();
		Calendar cal = Calendar.getInstance();

		details.setPeriod("Last Week");
		int i = cal.get(Calendar.DAY_OF_WEEK);
		i = i - 2 >= 0 ? i - 2 : 7 - i;
		i+=1;
		cal.add(Calendar.DATE, -i);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		details.setEndDate(cal.getTime());
		cal.add(Calendar.DATE, -6);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		details.setStartDate(cal.getTime());
		return details;
	}

	public static void main(String[] args) {
		PeriodDetails details = getThisWeekPeriod();
		System.out.println("Period - " + details.getPeriod());
		System.out.println("start - " + details.getStartDate().toString());
		System.out.println("End - " + details.getEndDate().toString());
		details = getLastWeekPeriod();
		System.out.println("Period - " + details.getPeriod());
		System.out.println("start - " + details.getStartDate().toString());
		System.out.println("End - " +
				"" +
				"" + details.getEndDate().toString());
		details =getTodaysPeriod();
		System.out.println("Period - " + details.getPeriod());
		System.out.println("start - " + details.getStartDate().toString());
		System.out.println("End - " +
				"" +
				"" + details.getEndDate().toString());
		details = getYesterdayPeriod();
		System.out.println("Period - " + details.getPeriod());
		System.out.println("start - " + details.getStartDate().toString());
		System.out.println("End - " +
				"" +
				"" + details.getEndDate().toString());
	}
}
