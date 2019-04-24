package com.jaza.taskMgmt.db.access;

import java.util.ArrayList;
import java.util.List;

import com.jaza.taskMgmt.db.access.model.Filter;
import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.access.model.PredicateOperator;

public class FilterHandler {
	
	public static FilterModel getFilterModel(List<FilterValue> filterValues, Class<?> collType) {
		FilterModel filterModel=new FilterModel(collType);
		if(filterValues != null && !filterValues.isEmpty())
			filterModel.addFilterLst(FilterHandler.convertFilterValues(filterValues));
		filterModel.setColumnsNeeded(null);
		return filterModel;
	}

	public static List<Filter> convertFilterValues(List<FilterValue> filterValues) {
		List<Filter> convertedFilterValues = new ArrayList<Filter>();
		for (FilterValue filterValue : filterValues) {
			convertedFilterValues.add(convertFilterValues(filterValue));
		}
		return convertedFilterValues;
	}

	public static Filter convertFilterValues(FilterValue filterValue) {
		Filter filter = null;
		PredicateOperator predicateOperator = getPredicateType(filterValue);
		filter = new Filter(getFilterType(filterValue), filterValue.getFilterValue(), predicateOperator);
		return filter;
	}

	public static String getFilterType(FilterValue filterValue) {
		String filterField = filterValue.getFilterType().getName();
		return filterField;
	}


	public static PredicateOperator getPredicateType(FilterValue filterValue) {
		PredicateOperator predicateOperator = filterValue.getFilterType().getPredicateOperator();
		if(filterValue.getPredicateOperator()!=null)
			predicateOperator=filterValue.getPredicateOperator();
		return predicateOperator;
	}



}
