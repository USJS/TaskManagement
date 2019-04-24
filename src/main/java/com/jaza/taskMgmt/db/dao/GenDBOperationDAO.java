package com.jaza.taskMgmt.db.dao;

import java.util.List;

import com.jaza.taskMgmt.db.access.model.FilterModel;

public interface GenDBOperationDAO {
	
	public <T> boolean save(List<T> objLst);
	
	public <T> boolean removeObjects(List<T> objLst);
	
	public <T> boolean removeObjectWithFilters(FilterModel filterModel);
	
	public <T> T getObject(FilterModel filterModel, Class<T> collectionName);
	
	public <T> List<T> getAllObjects(FilterModel filterModel, Class<T> collectionName);
	
}
