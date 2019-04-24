package com.jaza.taskMgmt.db.access;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.jaza.taskMgmt.db.access.model.Filter;
import com.jaza.taskMgmt.db.access.model.Filter.CriteriaType;
import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.access.model.PredicateObject;
import com.jaza.taskMgmt.db.access.model.PredicateOperator;

public class QueryProcHelper {
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static <T> TypedQuery<T> getQuery(EntityManager entityManager,Class<T> classType, FilterModel filterModel) {
		CriteriaBuilder criBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = criBuilder.createQuery(classType);
		Root<?> rootTable = query.from(filterModel.getClassType());
		List<Selection<?>> selectionList = null;
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate filterPredicate = getPredicate(filterModel.getFilter(), criBuilder, rootTable);
		if(filterPredicate != null)
			predicates.add(filterPredicate);
		List<Order> orders = new ArrayList<Order>();
		if (!orders.isEmpty()) {
			query.orderBy(orders);
		}

		Predicate predicate = null;
		if(!predicates.isEmpty())
			predicate = createAndPredicate(predicates, criBuilder);
		
		// add where condition
		if (predicate != null)
			query.where(predicate);


		// Decide selection data based on QueryType
		switch (filterModel.getQueryType()) {
		case SELECT:
			 if (selectionList != null && !selectionList.isEmpty()) {
				 query.multiselect(selectionList);
			 } else {
				 query.select((Selection<? extends T>) rootTable);
			 }
			break;
		default:
			break;
		}

		// Create TypedQuery
		TypedQuery<T> typeQuery = entityManager.createQuery(query);

		// Skip rows
		if (filterModel.getSkipUpto() > 0) {
			typeQuery.setFirstResult(filterModel.getSkipUpto());
		}
		// Limit rows
		if (filterModel.getLimit() > 0) {
			typeQuery.setMaxResults(filterModel.getLimit());
		}
		return typeQuery;
	}


	private static Predicate createAndPredicate(List<Predicate> predicateLst, CriteriaBuilder criBuilder) {
		Predicate andPredicate = null;
		for (Predicate predicate : predicateLst) {
			if (andPredicate == null)
				andPredicate = criBuilder.and(predicate);
			else
				andPredicate = criBuilder.and(predicate, andPredicate);
		}
		return andPredicate;
	}
	
	private static Predicate createOrPredicate(List<Predicate> predicateLst, CriteriaBuilder criBuilder) {
		Predicate orPredicate = null;
		for (Predicate predicate : predicateLst) {
			if (orPredicate == null)
				orPredicate = criBuilder.or(predicate);
			else
				orPredicate = criBuilder.or(predicate, orPredicate);
		}
		return orPredicate;
	}

	@SuppressWarnings("unchecked")
	private static <T, Z> Predicate getPredicate(Filter filter, CriteriaBuilder criBuilder, Root<T> rootTable) {
		List<Predicate> predicateLst = new ArrayList<Predicate>();
		Predicate predicate = null;
		if(filter == null)
			return predicate;
		List<Filter> addFilters = filter.getAddFilters();
		if (addFilters != null && !addFilters.isEmpty()) {
			for (Filter filt : addFilters) {
				Predicate newPredicate = getPredicate(filt, criBuilder, rootTable);
				if (newPredicate != null)
					predicateLst.add(newPredicate);
			}
		}
		if (filter.getFilterType() != null) {
			PathMap pathMap = null;
			Path<?> path = null;
			Join<?, ?> join = null;
			if(!filter.getFilterType().isEmpty()) {
				pathMap = getPath(rootTable, filter.getFilterType());
				 path = pathMap.getPath();
				join = pathMap.getJoin();
			}
			PredicateObject predicateObj = null;
			Predicate fromCri = null;
			Predicate toCri = null;
			PredicateOperator mapJoinPredOperator = PredicateOperator.EQUAL;
			switch (filter.getPredicateOperator()) {
			case EQUAL:
				if(filter.getFilterValue() == null) {
					if(path.getJavaType().equals(Map.class)) 
						mapJoinPredOperator = PredicateOperator.NOT_EQUAL;
					else
						predicate = path.isNull();
				} else if (path.getJavaType().equals(Map.class)) {
					MapJoin<?, ?, ?> mapJoin = (MapJoin<?, ?, ?>) join;
					predicate = criBuilder.equal((Expression<String>) mapJoin.value(), filter.getStringValue());
				}else if (path.getJavaType().equals(List.class)) {
					ListJoin<?, ?> listJoin = (ListJoin<?, ?>) join;
					if (listJoin.getAttribute().getJavaType().isEnum())
						predicate = join.in(getEnumFromString(listJoin.getJavaType(), filter.getStringValue()));
					else
						predicate = join.in( filter.getStringValue());
				} else if (path.getJavaType().equals(String.class)) {
					predicate = criBuilder.equal((Expression<String>) path, filter.getStringValue());
				} else if (path.getJavaType().isEnum()) {
					predicate = criBuilder.equal(path, getEnumFromString(path.getJavaType(), filter.getStringValue()));
				} else {
					predicate = criBuilder.equal(path, filter.getFilterValue());
				}
				break;
			case NOT_EQUAL:
				if(filter.getFilterValue() == null) {
					if(path.getJavaType().equals(Map.class)) 
						mapJoinPredOperator = PredicateOperator.EQUAL;
					else
						predicate = path.isNotNull();
				}else if (path.getJavaType().equals(Map.class)) {
					MapJoin<?, ?, ?> mapJoin = (MapJoin<?, ?, ?>) join;
					predicate = criBuilder.notEqual((Expression<String>) mapJoin.value(), filter.getStringValue());
				} else if (path.getJavaType().equals(String.class)) {
					predicate = criBuilder.notEqual((Expression<String>) path, filter.getStringValue());
				} else if (path.getJavaType().isEnum()) {
					predicate = criBuilder.notEqual(path, getEnumFromString(path.getJavaType(), filter.getStringValue()));
				} else if (path.getJavaType().equals(List.class)) {
					ListJoin<?, ?> listJoin = (ListJoin<?, ?>) join;
					if (listJoin.getAttribute().getJavaType().isEnum())
						predicate = criBuilder.not(join.in(getEnumFromString(listJoin.getJavaType(), filter.getStringValue())));
					else
						predicate = criBuilder.not(join.in(filter.getFilterValue()));
				} else {
					predicate = criBuilder.notEqual(path, filter.getFilterValue());
				}
				break;
			case LIKE:
				if (path.getJavaType().equals(List.class)) {
					predicate = join.in(criBuilder.like((Expression<String>) path, "%" + filter.getStringValue() + "%"));
				}else if (path.getJavaType().equals(Map.class)) {
					MapJoin<?, ?, ?> mapJoin = (MapJoin<?, ?, ?>) join;
					predicate = criBuilder.like((Expression<String>) mapJoin.value(), "%" + filter.getStringValue() + "%");
				}
				else
					predicate = criBuilder.like((Expression<String>) path, "%" + filter.getStringValue() + "%");
				break;
			case NOT_LIKE:
				if (path.getJavaType().equals(Map.class)) {
					MapJoin<?, ?, ?> mapJoin = (MapJoin<?, ?, ?>) join;
					predicate = criBuilder.notLike((Expression<String>) mapJoin.value(), "%" + filter.getStringValue() + "%");
				}else {
					predicate = criBuilder.notLike((Expression<String>) path,	"%" + filter.getStringValue() + "%");
				}
				break;
			case LESS_THAN:
				predicate = criBuilder.lt((Expression<? extends Number>) path, (Number) filter.getFilterValue());
				break;
			case LESS_THAN_OR_EQ:
				predicate = criBuilder.le((Expression<? extends Number>) path, (Number) filter.getFilterValue());
				break;
			case GREATER_THAN:
				predicate = criBuilder.gt((Expression<? extends Number>) path, (Number) filter.getFilterValue());
				break;
			case GREATER_THAN_OR_EQ:
				predicate = criBuilder.ge((Expression<? extends Number>) path, (Number) filter.getFilterValue());
				break;
			case TIME_BETWEEN:
				predicateObj = (PredicateObject) filter.getFilterValue(); 
				fromCri = criBuilder.ge((Expression<? extends Number>) path, predicateObj.getFromTime());
				toCri = criBuilder.le((Expression<? extends Number>) path, predicateObj.getToTime());
				predicate = criBuilder.and(fromCri, toCri);
			break;
			case LIST_STR_IN:
				if(path.getJavaType().isEnum()) {
					@SuppressWarnings("rawtypes")
					List<Enum> enumList = new ArrayList<Enum>();
					for(String val:(List<String>)filter.getFilterValue())
						enumList.add(getEnumFromString(path.getJavaType(), val));
					if(enumList.isEmpty())
						enumList.add(null);
					predicate = path.in(enumList);
				}
				else {
					List<?> list = (List<?>) filter.getFilterValue();
					if(list.isEmpty())
						list.add(null);
					predicate = path.in(list);
				}
			break;
			case LIST_STR_NOT_IN:
				List<?> list = (List<?>) filter.getFilterValue();
				if(!list.isEmpty())
					predicate = criBuilder.not(path.in(list));
				break;
			default:
				break;
			}
			if(pathMap != null) {
				Predicate keyPredicate = getPredicateFromPathKeyMap(pathMap.getJoinKeyMap(), criBuilder, mapJoinPredOperator);	// to get predicate for Map key.
				if (keyPredicate != null) {
					if (predicate == null)
						predicate = keyPredicate;
					else
						predicate = criBuilder.and(predicate, keyPredicate);
				}
			}
		}
		if (!predicateLst.isEmpty()) {
			if (predicateLst.size() == 1)
				predicate = predicateLst.get(0);
			else if (filter.getCriteriaType().equals(CriteriaType.AND)) {
				predicate = createAndPredicate(predicateLst, criBuilder);
			} else if (filter.getCriteriaType().equals(CriteriaType.OR)) {
				predicate = createOrPredicate(predicateLst, criBuilder);
			}
		}
		return predicate;
	}
	
	private static PathMap getPath(Root<?> rootTable, String filterType) {
		if(filterType.isEmpty())
			return null;
		PathMap pathMap = new PathMap();
		String[] filterArray = filterType.split("\\.");
		Path<?> path = null;
		Join<?, ?> join = null;
		Join<?, ?> tmpJoin = null;
		String field = null;
		for (int i = 0; i < filterArray.length; i++) {
			field = filterArray[i];
			if (join == null) {
				// tmpJoin = rootTable.join(field);
				path = rootTable.get(field);
			} else {
				// tmpJoin = join.join(field);
				path = join.get(field);
			}
			Class<? extends Object> javaType = path.getJavaType();
			if (javaType.equals(Map.class)) {
				if (join == null) {
					tmpJoin = rootTable.joinMap(field);
					// path = rootTable.get(field);
				} else {
					tmpJoin = join.joinMap(field);
					// path = join.get(field);
				}
				i++;
				if(i < filterArray.length)
					pathMap.setJoinKey(tmpJoin, filterArray[i]);
			} else if (javaType.equals(List.class)) {
				if (join == null)
					tmpJoin = rootTable.joinList(field);
				else
					tmpJoin = join.joinList(field);
			} else if (!javaType.equals(String.class) && 
					!javaType.isEnum() && 
					!javaType.equals(boolean.class) && 
					!javaType.equals(int.class) && 
					!javaType.equals(float.class) && 
					!javaType.equals(long.class) && 
					!javaType.equals(double.class) && 
					!javaType.equals(Integer.class) && 
					!javaType.equals(Float.class) && 
					!javaType.equals(Long.class) && 
					!javaType.equals(Double.class)  ) {
				if (join == null) {
					tmpJoin = rootTable.join(field);
				} else {
					tmpJoin = join.join(field);
				}
			}
			join = tmpJoin;
		}
		pathMap.setJoin(join);
		pathMap.setPath(path);
		return pathMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Enum getEnumFromString(Class<?> classType, String value) {
		Enum val = Enum.valueOf((Class<Enum>) classType, value);
		return val;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Predicate getPredicateFromPathKeyMap(LinkedHashMap<Join<?, ?>, String> map, CriteriaBuilder criBuilder, PredicateOperator predicateOperator) {
		if (map == null || map.isEmpty())
			return null;
		int counter = 0;
		Predicate predicate = null;
		if(predicateOperator == null)
			predicateOperator = PredicateOperator.EQUAL;
		Set<Entry<Join<?, ?>, String>> entrySet = map.entrySet();
		for (Entry<Join<?, ?>, String> entry : entrySet) {
			counter++;
			MapJoin<?, ?, ?> mapJoin = (MapJoin<?, ?, ?>) entry.getKey();
			Path<?> keyPath = mapJoin.key();
			Predicate tmpPredicate = null;
			Object value = entry.getValue();
			if (keyPath.getJavaType().isEnum()) {
				value = Enum.valueOf((Class<Enum>) keyPath.getJavaType(), entry.getValue());
			}
			if(entrySet.size() == counter) {
				switch(predicateOperator) {
				case EQUAL:
					tmpPredicate = criBuilder.equal(mapJoin.key(), value);
					break;
				case NOT_EQUAL:
					tmpPredicate = criBuilder.notEqual(mapJoin.key(), value);
					break;
				default:
					tmpPredicate = criBuilder.equal(mapJoin.key(), value);
					break;
				}
			}
			else
				tmpPredicate = criBuilder.equal(mapJoin.key(), value);
			if (predicate == null)
				predicate = tmpPredicate;
			else
				predicate = criBuilder.and(predicate, tmpPredicate);
		}
		return predicate;
	}

}
