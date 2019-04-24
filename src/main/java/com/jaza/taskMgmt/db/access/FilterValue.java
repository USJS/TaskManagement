package com.jaza.taskMgmt.db.access;

import java.io.Serializable;

import com.jaza.taskMgmt.db.access.model.PredicateOperator;


public class FilterValue implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FilterType filterType;
	private Object filterValue;
	private PredicateOperator predicateOperator = null;
	private String addFilter;
	
	@SuppressWarnings("unused")
	private FilterValue(){}
	
	public FilterValue(FilterType filterType, Object filterValue) {
		this.filterType = filterType;
		this.filterValue = filterValue;
	}
	
	public FilterValue(FilterType filterType, String addFilter,
			Object filterValue, PredicateOperator operator) {
		this.filterType = filterType;
		this.filterValue = filterValue;
		this.setAddFilter(addFilter);
		this.predicateOperator = operator;
	}
	
	@Override
	public String toString() {
		return "FilterValue [filterType=" + filterType + ", filterValue="
				+ filterValue + ", predicateOperator=" + predicateOperator
				+ "]";
	}
	public static FilterValue getInstance(FilterType filterType, Object filterValue,PredicateOperator predicateOperator){
		FilterValue filterObject=new FilterValue(filterType, filterValue);
		filterObject.predicateOperator=predicateOperator;
		return filterObject;
	}
	public Object getFilterValue() {
		return filterValue;
	}


	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filterType == null) ? 0 : filterType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterValue other = (FilterValue) obj;
		if (filterType != other.filterType)
			return false;
		return true;
	}
	public PredicateOperator getPredicateOperator() {
		return predicateOperator;
	}
	public void setPredicateOperator(PredicateOperator predicateOperator) {
		this.predicateOperator = predicateOperator;
	}
	public String getAddFilter() {
		return addFilter;
	}
	public void setAddFilter(String addFilter) {
		this.addFilter = addFilter;
	}
	
}
