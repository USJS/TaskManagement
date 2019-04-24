package com.jaza.taskMgmt.db.access.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterModel {

	public enum QueryType {
		SELECT,
		;
	}
	
	private Class<?> classType;
	private Filter filter;
	private List<String> sortBy = new ArrayList<String>();
	private List<String> columnsNeeded = new ArrayList<String>();
	private int limit = 0;
	private int skipUpto = 0;
	private QueryType queryType=QueryType.SELECT;

	public FilterModel(Class<?> classType) {
		this.classType = classType;
	}
	public FilterModel(Class<?> classType,Filter filter) {
		this.classType = classType;
		this.filter = filter;
	}
	
	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public List<String> getSortBy() {
		return sortBy;
	}

	public void setSortBy(List<String> sortBy) {
		this.sortBy = sortBy;
	}

	public List<String> getColumnsNeeded() {
		return columnsNeeded;
	}

	public void setColumnsNeeded(List<String> columnsNeeded) {
		this.columnsNeeded = columnsNeeded;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getSkipUpto() {
		return skipUpto;
	}

	public void setSkipUpto(int skipUpto) {
		this.skipUpto = skipUpto;
	}

	public String getDistinctField() {
		if (columnsNeeded == null || columnsNeeded.isEmpty())
			return null;
		return columnsNeeded.get(0);
	}

	public void setDistinctField(String distinctField) {
		this.columnsNeeded = Arrays.asList(distinctField);
	}

	public void addFilterLst(List<Filter> filterLst) {
		if (filter == null)
			filter = new Filter(null, null);
		filter.addAddFilters(filterLst);
	}
	
	public void addFilter(Filter filter) {
		if (this.filter == null)
			this.filter = new Filter(null, null);
		this.filter.addAddFilter(filter);
	}
	
	/**
	 * @return the queryType
	 */
	public QueryType getQueryType() {
		return queryType;
	}
	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	
	/**
	 * Change to next Page 
	 */
	public void updateSkipUpto() {
		this.setSkipUpto(this.getSkipUpto() + this.getLimit());
	}
	

}
