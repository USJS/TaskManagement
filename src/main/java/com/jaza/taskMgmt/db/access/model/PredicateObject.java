package com.jaza.taskMgmt.db.access.model;

import java.util.ArrayList;
import java.util.List;

public class PredicateObject {

	private Object enumProperty;
	private Object value;
	// Time Between calculation
	private String fromFilterType;
	private String toFilterType;
	private long fromTime = 0L;
	private long toTime = 0L;
	private boolean isTimeBetween = false;

	private List<Filter> filLst = new ArrayList<Filter>();

	public PredicateObject() {

	}

	public PredicateObject(Object enumProperty, Object value) {
		this.enumProperty = enumProperty;
		this.value = value;
	}

	public PredicateObject(long fromTime, long toTime, String fromFilterType, String toFilterType) {
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.setFromFilterType(fromFilterType);
		this.setToFilterType(toFilterType);
	}

	public PredicateObject(long fromTime, long toTime, boolean isTimeBetween) {
		this.fromTime = fromTime;
		this.toTime = toTime;
		this.isTimeBetween = isTimeBetween;
	}

	public Object getEnumProperty() {
		return enumProperty;
	}

	public Object getValue() {
		return value;
	}

	/**
	 * @return the fromTime
	 */
	public long getFromTime() {
		return fromTime;
	}

	/**
	 * @param fromTime
	 *            the fromTime to set
	 */
	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	/**
	 * @return the toTime
	 */
	public long getToTime() {
		return toTime;
	}

	/**
	 * @param toTime
	 *            the toTime to set
	 */
	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	/**
	 * @return the fromFilterType
	 */
	public String getFromFilterType() {
		return fromFilterType;
	}

	/**
	 * @param fromFilterType
	 *            the fromFilterType to set
	 */
	public void setFromFilterType(String fromFilterType) {
		this.fromFilterType = fromFilterType;
	}

	/**
	 * @return the toFilterType
	 */
	public String getToFilterType() {
		return toFilterType;
	}

	/**
	 * @param toFilterType
	 *            the toFilterType to set
	 */
	public void setToFilterType(String toFilterType) {
		this.toFilterType = toFilterType;
	}

	public List<Filter> getFilLst() {
		return filLst;
	}

	public void setFilLst(List<Filter> filLst) {
		this.filLst = filLst;
	}

	public void addFilVal(String filterType, Object val) {
		filLst.add(new Filter(filterType, val));
	}
	
	@Override
	public String toString() {
		String toString = "[ FromTime = " + fromTime + ", ToTime = " + toTime + ", EnumProp = " + enumProperty
				+ ", Value = " + value + ", Dur = " + (toTime - fromTime) + " ]";
		return toString;
	}

	public boolean isTimeBetween() {
		return isTimeBetween;
	}

	public void setTimeBetween(boolean isTimeBetween) {
		this.isTimeBetween = isTimeBetween;
	}

}
