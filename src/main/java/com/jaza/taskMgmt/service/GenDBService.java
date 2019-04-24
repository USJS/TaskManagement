package com.jaza.taskMgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.dao.GenDBOperationDAO;

@Service
public class GenDBService {
	
	@Autowired
	private GenDBOperationDAO genDBDAO;
	
	public <T> boolean save(List<T> objects) {
		return genDBDAO.save(objects);
	}
	
	public <T> boolean delete(List<T> objects) {
		return genDBDAO.removeObjects(objects);
	}
	
	public <T> boolean deleteWithFilters(FilterModel filterModel) {
		return genDBDAO.removeObjectWithFilters(filterModel);
	}

	public <T> List<T> getAllObjects(FilterModel filterModel, Class<T> collectionName) {
		return genDBDAO.getAllObjects(filterModel, collectionName);
	}
	
	
}
