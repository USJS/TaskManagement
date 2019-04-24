package com.jaza.taskMgmt.db.access;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.dao.GenDBOperationDAO;

@Component
public class DbOperation implements GenDBOperationDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public <T> boolean save(List<T> objLst) {
		if (objLst == null || objLst.isEmpty())
			return false;
		try {
			for (Object obj : objLst) {
				entityManager.merge(obj);
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Transactional
	@Override
	public <T> boolean removeObjects(List<T> objLst) {
		if (objLst == null || objLst.isEmpty())
			return false;
		try {
			for (Object obj : objLst) {
				entityManager.remove(obj);
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Transactional
	@Override
	public <T> boolean removeObjectWithFilters(FilterModel filterModel) {
		List<?> objLst = getAllObjects(filterModel, filterModel.getClassType());
		return removeObjects(objLst);
	}
	
	@Override
	public <T> T getObject(FilterModel filterModel, Class<T> collectionName) {
		TypedQuery<T> searchQuery = QueryProcHelper.getQuery(entityManager,collectionName, filterModel);
		try {
			return findOne(searchQuery);
		} catch (Exception ex) {
			return findOne(searchQuery);
		}
	}
	
	
	@Override
	public <T> List<T> getAllObjects(FilterModel filterModel, Class<T> collectionName) {
		TypedQuery<T> searchQuery = QueryProcHelper.getQuery(entityManager,collectionName, filterModel);
		List<T> returnVal;
		try {
			returnVal = findAll(searchQuery);
		} catch (Exception ex) {
			returnVal = findAll(searchQuery);
		}
		return returnVal;
	}

	
	private static <T> T findOne(TypedQuery<T> query) {
		try {
			return query.getSingleResult();
		}catch(Exception exception) {
			throw(exception);
		}
	}

	private static <T> List<T> findAll(TypedQuery<T> query) {
		return query.getResultList();
	}


}
