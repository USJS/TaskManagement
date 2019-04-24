package com.jaza.taskMgmt.db.access.model;

import java.io.Serializable;
import java.util.Calendar;
public enum Period implements Serializable {
	RECENT_DATA("Recent Data"),
	TODAY("Today"),
	YESTERDAY("Yesterday"), 
	LAST_7_DAYS("Last 7 Days"), 
	NEXT_2_DAYS("Next 2 Days"),
	NEXT_7_DAYS("Next 7 Days"),
	;
	private String latestPeriod;

	Period(String latestPeriod) {
		this.latestPeriod = latestPeriod;
	}

	public String getLatest() {
		return latestPeriod;
	}

	static public Period GetLatestPeriod(String latest) {
		for (Period latstperiod : Period.values()) {
			if (latstperiod.getLatest().equalsIgnoreCase(latest))
				return latstperiod;
		}
		return null;
	}
	public Calendar fromDate(long time){
		Calendar fromCal = Calendar.getInstance();
		if (time != 0l)
			fromCal.setTimeInMillis(time);
		switch (this) {
		case LAST_7_DAYS:
			fromCal.add(Calendar.DAY_OF_YEAR, -7);
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		case TODAY:
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		case YESTERDAY:
			fromCal.add(Calendar.DAY_OF_YEAR, -1);
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		case RECENT_DATA:
			fromCal.add(Calendar.HOUR, -6);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		case NEXT_2_DAYS:
			fromCal.add(Calendar.DAY_OF_YEAR, 1);
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		case NEXT_7_DAYS:
			fromCal.add(Calendar.DAY_OF_YEAR, 1);
			fromCal.set(Calendar.HOUR_OF_DAY, 0);
			fromCal.set(Calendar.MINUTE, 0);
			fromCal.set(Calendar.SECOND, 0);
			fromCal.set(Calendar.MILLISECOND, 0);
			break;
		default:
			break;
		}
		
		return fromCal;
	}
	
	public Calendar fromDate() {
	return fromDate(0l);
	}
	public Calendar toDate() {
		return toDate(0l);
	}
	public Calendar toDate(long time) {
		Calendar toCal = Calendar.getInstance();
		if(time!=0l){
			toCal.setTimeInMillis(time);
			return toCal;
		}
		switch (this) {
		case RECENT_DATA:
			toCal.set(Calendar.MINUTE, toCal.get(Calendar.MINUTE)-1);
			toCal.set(Calendar.SECOND, 0);
			toCal.set(Calendar.MILLISECOND, 0);
			break;
		case YESTERDAY:
			toCal.add(Calendar.DAY_OF_YEAR, -1);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			break;
		case TODAY:
			toCal.add(Calendar.DAY_OF_YEAR, 0);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			break;
		case LAST_7_DAYS:
			toCal.add(Calendar.DAY_OF_YEAR, -1);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			break;
		case NEXT_2_DAYS:
			toCal.add(Calendar.DAY_OF_YEAR, 2);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			break;
		case NEXT_7_DAYS:
			toCal.add(Calendar.DAY_OF_YEAR, 7);
			toCal.set(Calendar.HOUR_OF_DAY, 23);
			toCal.set(Calendar.MINUTE, 59);
			toCal.set(Calendar.SECOND, 59);
			toCal.set(Calendar.MILLISECOND, 999);
			break;
		default:
			break;
		}
		return toCal;
	}

	public static void main(String[] args) {
		long time = 0l;
		Period test = Period.YESTERDAY;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
		test = Period.TODAY;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
		test = Period.NEXT_2_DAYS;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
		test = Period.LAST_7_DAYS;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
		test = Period.RECENT_DATA;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
		test = Period.NEXT_7_DAYS;
		System.out.println(test+"-From :"+test.fromDate(time).getTime());
		System.out.println(test+"-To :"+test.toDate(time).getTime()+"\n");
	}
}