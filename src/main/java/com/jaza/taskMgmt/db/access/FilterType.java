
package com.jaza.taskMgmt.db.access;

import com.jaza.taskMgmt.db.access.model.PredicateOperator;


public enum FilterType {
	/**
	 * Common TO All
	 */
	ID("id",PredicateOperator.EQUAL),
	TIME_BETWEEN("creationTime",PredicateOperator.TIME_BETWEEN),
	
	
	//Task
	TASK_ID("taskId",PredicateOperator.EQUAL),
	PRIORITY("priority",PredicateOperator.EQUAL),
	DUE_DATE_BETWEEN("dueDate",PredicateOperator.TIME_BETWEEN),
	
	TASK_ID_IN("taskId",PredicateOperator.LIST_STR_IN),
	PRIORITY_IN("priority",PredicateOperator.LIST_STR_IN),
	
	LABEL("category.labelName",PredicateOperator.EQUAL),
	LABEL_IN("category.labelName",PredicateOperator.LIST_STR_IN),
	
	//Label
	LABEL_NAME("labelName",PredicateOperator.EQUAL),
	LABEL_ID("labelId",PredicateOperator.EQUAL),
	
	LABEL_NAME_IN("labelName",PredicateOperator.LIST_STR_IN),
	LABEL_ID_IN("labelId",PredicateOperator.LIST_STR_IN),
	
	
	
	
	
	
	;
	private String name;
	private PredicateOperator predicateOperator;
	
	private FilterType(String name,PredicateOperator operator){
		if(name.endsWith("."))
			throw new RuntimeException("Filter Type Should not ends with '.'");
		this.name = name;
		predicateOperator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PredicateOperator getPredicateOperator() {
		return predicateOperator;
	}

}
