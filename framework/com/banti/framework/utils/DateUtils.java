
package com.banti.framework.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DateUtils {

	private static int year;
	private static int month;
	private static int date;

	private static Timer timer = null;
	private static Task task = null;
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");

	static {
		startTimer();
	}

	public static void updateCurrentDate(boolean schedule) {
		Date currentTime_1 = new Date();
		String dateString = formatter.format(currentTime_1);
		try {
			year = Integer.parseInt(dateString.substring(0, dateString.indexOf(" ")));
			month =
				Integer.parseInt(
					dateString.substring(
						dateString.indexOf(" ") + 1,
						dateString.lastIndexOf(" ")));
			date =
				Integer.parseInt(dateString.substring(dateString.lastIndexOf(" ") + 1));
		} catch (NumberFormatException exp) {
		}
		if ( schedule ) {
		  timer.schedule(new Task(), ((1000 * 60 * 64 * 24) - 5));
		}
	}

	private static void startTimer() {
		timer = new Timer();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);

		long val =
			(cal.getTimeInMillis() - (Calendar.getInstance()).getTimeInMillis());

		String DATE_FORMAT = "yyyy MM dd";
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date currentTime_1 = new Date();
		String dateString = formatter.format(currentTime_1);

		try {
			year = Integer.parseInt(dateString.substring(0, dateString.indexOf(" ")));
			month =
				Integer.parseInt(
					dateString.substring(
						dateString.indexOf(" ") + 1,
						dateString.lastIndexOf(" ")));
			date =
				Integer.parseInt(dateString.substring(dateString.lastIndexOf(" ") + 1));
		} catch (NumberFormatException exp) {
		}

		task = new Task();
		timer.schedule(task, val);
	}

	public static String getSystemTime() {
		String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		java.util.Date currentTime = new java.util.Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getSystemDate() {
		String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		java.util.Date currentTime = new java.util.Date();
		String dateString = formatter.format(currentTime);
		int index = dateString.lastIndexOf(":");
		String temp = dateString.substring(0, index);
		temp = temp + ":00";
		//return dateString;
    return temp;
	}

	public static String getYearFromMMDD(String mm_dd) {
		int mm = Integer.parseInt(mm_dd.substring(0, mm_dd.indexOf("/")));
		int dd = Integer.parseInt(mm_dd.substring(mm_dd.indexOf("/") + 1));

		int tmpYear = getYear(mm);

		if (dd > date) {
			if (tmpYear == year) {
				tmpYear--;
			}
		}
		String tmp = (mm < 10) ? "0" + mm : "" + mm;
		return "" + tmpYear;
	}

	public static String getYearFromMMMDD(String mmm_dd) {
		updateCurrentDate(false);
		String monthName = mmm_dd.substring(0, mmm_dd.indexOf(" "));
		int dd = Integer.parseInt(mmm_dd.substring(mmm_dd.indexOf(" ") + 1));
		int mm = 0;
		if (monthName.equalsIgnoreCase("Jan")) {
			mm = 1;
		} else if (monthName.equalsIgnoreCase("Feb")) {
			mm = 2;
		} else if (monthName.equalsIgnoreCase("Mar")) {
			mm = 3;
		} else if (monthName.equalsIgnoreCase("Apr")) {
			mm = 4;
		} else if (monthName.equalsIgnoreCase("May")) {
			mm = 5;
		} else if (monthName.equalsIgnoreCase("Jun")) {
			mm = 6;
		} else if (monthName.equalsIgnoreCase("Jul")) {
			mm = 7;
		} else if (monthName.equalsIgnoreCase("Aug")) {
			mm = 8;
		} else if (monthName.equalsIgnoreCase("Sep")) {
			mm = 9;
		} else if (monthName.equalsIgnoreCase("Oct")) {
			mm = 10;
		} else if (monthName.equalsIgnoreCase("Nov")) {
			mm = 11;
		} else if (monthName.equalsIgnoreCase("Dec")) {
			mm = 12;
		}
		int tmpYear = getYear(mm);
		if (dd > date && mm >= month) {
			if (tmpYear == year) {
				tmpYear--;
			}
		}
		String tmp = (mm < 10) ? "0" + mm : "" + mm;
		return "" + tmpYear + "/" + tmp + "/" + ((dd < 10) ? "0" + dd : "" + dd);
	}

	public static int getYear(int monthNo) {
		if (monthNo <= month) {
			return year;
		} else {
			return year - 1;
		}
	}

	public static String getDateStringFormat(Date dt) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = sd.format(dt);
		return date;
	}

	/**
	 * Gets the long value from the string of <yyyy>-<MM>-<dd> <HH>:<mm>:<ss>
	 * format.
	 * @param time - time value to be converted to long
	 */
	public static long getTimeInMilliSeconds(String time) {
		if (time == null) {
			return 0;
		}

		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		if (time.indexOf("/") > 0) {
			DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Calendar cal = Calendar.getInstance();

		cal.setTime(formatter.parse(time, new ParsePosition(0)));
		return cal.getTimeInMillis();
	}
    
    public static String getTimeFromMillsForDB(long millis){
        
        String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

                Calendar cal = Calendar.getInstance();
                long ltime = 0;
                cal.setTimeInMillis(millis);
                String time = formatter.format(cal.getTime());

                return time;
    }

	public static long getTimeFromMillis(long millis) {

		String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

		Calendar cal = Calendar.getInstance();
		long ltime = 0;
		cal.setTimeInMillis(millis);
		String time = formatter.format(cal.getTime());
		try {
			ltime = Long.parseLong(time);
		} catch (NumberFormatException e) {

		}
		return ltime;
	}

	public static boolean isValidDate(String date) {
		String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setLenient(false);
		try {

			Date dateStr = sdf.parse(date);
		} catch (ParseException e) {

			return false;
		}
		return true;
	}

	/**
		 * @param startTimeStamp
		 */
	public static String formatTime(String time) {
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String date = time.substring(6, 8);
		String hr = time.substring(8, 10);
		String min = time.substring(10, 12);
		String sec = time.substring(12, 14);
		time =
			year + "-" + month + "-" + date + " " + hr + ":" + min + ":" + sec;
		return time;
	}

    /**
     * This method returns the following String array of the formatted date.
     * String[] { "now - 1hour", "now"} 
     * @return String[]
     */
    public static String[] getSearchPeriod() {
        String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        java.util.Date currentTime = new java.util.Date();
        String[] period = new String[2];

        String dateString = formatter.format(new Date(currentTime.getTime() - 60 * 60 * 1000));
        period[0] = dateString.substring(0, dateString.lastIndexOf(":")) + ":00";
        dateString = formatter.format(currentTime);
        period[1] = dateString.substring(0, dateString.lastIndexOf(":")) + ":00";
        return period;
    }

//	public static void main(String args[]) {
//		new DateUtils();
//		System.out.println(DateUtils.getYearFromMMMDD("Jan 31"));
//		System.out.println(DateUtils.getYearFromMMMDD("Oct 22"));
//		System.out.println(DateUtils.getYearFromMMMDD("Oct 31"));
//		System.out.println(DateUtils.getYearFromMMMDD("Nov 31"));
//		System.out.println(DateUtils.getYearFromMMMDD("Dec 31"));
//		for (int i = 0; i < 28; i++) {
//			try {
//				Thread.sleep(3500);
//				System.out.println(
//					"===========>" + DateUtils.getYearFromMMMDD("Feb " + i));
//			} catch (Exception exc) {
//				exc.printStackTrace();
//			}
//		}
//		System.out.println(
//			DateUtils.getTimeFromMillis(System.currentTimeMillis()));
//		System.out.println(
//			DateUtils.getTimeInMilliSeconds("2002-12-10 15:45:56"));
//		System.out.println(
//			DateUtils.getTimeFromMillis(
//				DateUtils.getTimeInMilliSeconds("2002-12-10 15:45:56")));
//		String str = "\"ISS\"";
//		str = DateUtils.replaceControlCharacters(str);
//		System.out.println("Replaced str in main = " + str);
//
//		String str1 = "ISS\\ISS";
//		System.out.println("str1 = " + str1);
//		System.out.println("str1 's length = " + str1.length());
//		str = DateUtils.replaceBackSlash(str1);
//		System.out.println("Next Replaced str in main = " + str1);
//
//	}

}

class Task extends TimerTask {

    public long scheduledExecutionTime() {
        return 0;
   	}

    public boolean cancel() {
        return true;
    }

    public void run() {
        DateUtils.updateCurrentDate(true);
    }
}

