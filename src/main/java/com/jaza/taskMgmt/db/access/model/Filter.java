package com.jaza.taskMgmt.db.access.model;

import java.util.ArrayList;
import java.util.List;

public class Filter {

	private static final long serialVersionUID = 1L;
	private String filterType;
	private Object filterValue;
	private PredicateOperator predicateOperator = PredicateOperator.EQUAL;
	private List<Filter> addFilters = new ArrayList<Filter>();
	private CriteriaType criteriaType = CriteriaType.AND;

	public enum CriteriaType {
		OR, AND;
	}

	public Filter(String filterType, Object filterValue) {
		this.filterType = filterType;
		this.filterValue = filterValue;
	}

	public Filter(String filterType, Object filterValue, PredicateOperator predicateOperator) {
		this.filterType = filterType;
		this.filterValue = filterValue;
		if(predicateOperator != null)
			this.predicateOperator = predicateOperator;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public Object getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}

	public PredicateOperator getPredicateOperator() {
		return predicateOperator;
	}

	public void setPredicateOperator(PredicateOperator predicateOperator) {
		if(predicateOperator != null)
			this.predicateOperator = predicateOperator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getStringValue() {
		return filterValue.toString();
	}

	public List<Filter> getAddFilters() {
		return addFilters;
	}

	public void setAddFilters(List<Filter> addFilters) {
		this.addFilters = addFilters;
	}

	public void addAddFilters(List<Filter> addFilters) {
		this.addFilters.addAll(addFilters);
	}

	public void addAddFilter(Filter addFilter) {
		this.addFilters.add(addFilter);
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(CriteriaType criteriaType) {
		if(criteriaType != null)
			this.criteriaType = criteriaType;
	}
	
	@Override
	public String toString() {
		String str = "FilterType: "+filterType+" FilterValue: "+filterValue+" Oper: "+predicateOperator.getName()+" CriteriaType: "+criteriaType+ "addFilter: "+addFilters;
		return str;
	}
}
