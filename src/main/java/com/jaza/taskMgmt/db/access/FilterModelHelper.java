package com.jaza.taskMgmt.db.access;

import java.util.ArrayList;
import java.util.List;

import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.access.model.Period;
import com.jaza.taskMgmt.db.access.model.PredicateObject;
import com.jaza.taskMgmt.db.models.Task;

public class FilterModelHelper {


	public static FilterModel getFilterModelForTask(String taskId, String labelName, List<String> priority, String period,
			Long fromTime, Long toTime) {
		List<String> taskIds = new ArrayList<>();
		if(taskId!=null && !taskId.isEmpty())
			taskIds.add(taskId);
		List<String> labelIds = new ArrayList<>();
		if(labelName!=null && !labelName.isEmpty())
			labelIds.add(labelName);
		return getFilterModelForTaskWithLst(taskIds, labelIds, priority, period, fromTime, toTime);
	}
	
	public static FilterModel getFilterModelForTaskWithLst(List<String> taskIds, List<String> labelName, List<String> priority, String period,
			Long fromTime, Long toTime) {
		FilterModel filterModel = null;
		try {
			List<FilterValue> filterValue = new ArrayList<FilterValue>();
			if (taskIds != null && !taskIds.isEmpty()) {
				if(taskIds.size()>1)
					filterValue.add(new FilterValue(FilterType.TASK_ID_IN, taskIds));
				else
					filterValue.add(new FilterValue(FilterType.TASK_ID,taskIds.get(0)));
			}
			if (labelName != null && !labelName.isEmpty()) {
				if (labelName != null && !labelName.isEmpty()) {
					if(labelName.size()>1)
						filterValue.add(new FilterValue(FilterType.LABEL_IN, labelName));
					else
						filterValue.add(new FilterValue(FilterType.LABEL,labelName.get(0)));
				}
			}
			if(period!=null && !period.isEmpty()) {
				try {
					Period periodEnm = Period.valueOf(period);
					if(periodEnm!=null) {
						fromTime = periodEnm.fromDate().getTimeInMillis();
						toTime = periodEnm.toDate().getTimeInMillis();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			if (fromTime != null && toTime != null) {
				PredicateObject predicateObject = new PredicateObject(fromTime, toTime, true);
				filterValue.add(new FilterValue(FilterType.DUE_DATE_BETWEEN, predicateObject));
			}
			if (priority != null && !priority.isEmpty()) {
				if (priority != null && !priority.isEmpty()) {
					if(priority.size()>1)
						filterValue.add(new FilterValue(FilterType.PRIORITY_IN, priority));
					else
						filterValue.add(new FilterValue(FilterType.PRIORITY,priority.get(0)));
				}
			}
			filterModel = FilterHandler.getFilterModel(filterValue, Task.class);
		} catch (Exception e) {
			// TODO add log
			e.printStackTrace();
		}
		return filterModel;
	}
}
