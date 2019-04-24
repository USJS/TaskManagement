package com.jaza.taskMgmt.db.access;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;

public class PathMap {
	private Path<?> path;
	private Join<?, ?> join;
	private List<String> filters = new ArrayList<String>();
	
	private LinkedHashMap<Join<?, ?>, String> joinKeyMap=new LinkedHashMap<Join<?, ?>, String>();
	
	public Path<?> getPath() {
		return path;
	}
	public void setPath(Path<?> path) {
		this.path = path;
	}
	public Join<?, ?> getJoin() {
		return join;
	}
	public LinkedHashMap<Join<?, ?>, String> getJoinKeyMap() {
		return joinKeyMap;
	}
	public void setJoinKeyMap(LinkedHashMap<Join<?, ?>, String> joinKeyMap) {
		this.joinKeyMap = joinKeyMap;
	}
	public void setJoin(Join<?, ?> join) {
		this.join = join;
	}
	public void setJoinKey(Join<?, ?> join, String Key) {
		
		this.joinKeyMap.put(join, Key);
	}
	
	public List<String> getFilters() {
		return filters;
	}
	
	public void setFilters(List<String> filters) {
		this.filters = filters;
	}
	
	public void addFilters(String filter) {
		if(filter != null)
			this.filters.add(filter);
	}
}
