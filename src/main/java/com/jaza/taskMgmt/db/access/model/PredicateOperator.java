package com.jaza.taskMgmt.db.access.model;

public enum PredicateOperator {


	EQUAL("Equals"), 
	NOT_EQUAL("Does not equal"), 
	LIKE("Contains"), 
	NOT_LIKE("Does not contain"),
	GREATER_THAN("Greater than"), 
	LESS_THAN("Less than"), 
	GREATER_THAN_OR_EQ("Greater than or equal"), 
	LESS_THAN_OR_EQ("Less than or equal"),
	TIME_BETWEEN("Time between"),
	LIST_STR_IN("List of String in"),
	LIST_STR_NOT_IN("List of String not in")
	
	;
	private String name;
	PredicateOperator(String name){
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}



}
