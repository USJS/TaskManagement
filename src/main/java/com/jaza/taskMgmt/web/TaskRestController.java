package com.jaza.taskMgmt.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jaza.taskMgmt.db.access.FilterHandler;
import com.jaza.taskMgmt.db.access.FilterModelHelper;
import com.jaza.taskMgmt.db.access.FilterType;
import com.jaza.taskMgmt.db.access.FilterValue;
import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.models.Category;
import com.jaza.taskMgmt.db.models.Task;
import com.jaza.taskMgmt.service.GenDBService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskRestController {

	@Autowired
	private GenDBService genDBService;

	@RequestMapping(value="/insertList",method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> addTasks(@RequestBody List<Task> tasks) {
		return addTask(tasks);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> addTask(@RequestBody Task task) {
		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
		return addTask(tasks);
	}

	private ResponseEntity<String> addTask(List<Task> tasks){
		try {
			for(Task task : tasks) {
				Category category = getCategory(task.getCategory().getLabelName());
				if(category!=null)
					task.setCategory(category);
			}
			boolean isInserted = genDBService.save(tasks);
			if (isInserted)
				return new ResponseEntity<String>("Tasks inserted successfully", HttpStatus.CREATED);
			else
				return new ResponseEntity<String>("Tasks not inserted", HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Task>> getTasks() {
		List<Task> tasks = null;
		try {
			FilterModel filterModel = FilterHandler.getFilterModel(new ArrayList<FilterValue>(), Task.class);
			tasks = genDBService.getAllObjects(filterModel, Task.class);
			if (tasks != null && !tasks.isEmpty()) {
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO add log
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<List<Task>> updateTask(@RequestBody Task task) {
		List<Task> tasks = new ArrayList<>();
		try {
			List<FilterValue> filterValue= new ArrayList<FilterValue>();
			filterValue.add(new FilterValue(FilterType.TASK_ID, task.getTaskId()));
			FilterModel filterModel = FilterHandler.getFilterModel(filterValue, Task.class);
			tasks=genDBService.getAllObjects(filterModel, Task.class);
			if(tasks!=null && !tasks.isEmpty() && !(tasks.size()>1)) {
				Category category = getCategory(task.getCategory().getLabelName());
				if(category!=null)
					task.setCategory(category);
				List<Task> upDatedtasks = new ArrayList<>();
				upDatedtasks.add(task);
				boolean isInserted = genDBService.save(upDatedtasks);
				if (isInserted)
					return new ResponseEntity<List<Task>>(tasks, HttpStatus.CREATED);
				else
					return new ResponseEntity<List<Task>>(tasks, HttpStatus.CONFLICT);
			}else
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deleteTask/{taskId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> deleteTaskWithId(@PathVariable("taskId") String taskId) {
		try {
			FilterModel filterModel = FilterModelHelper.getFilterModelForTask(taskId, null, null, null,null,null);
			if(filterModel!=null) {
				boolean isInserted = genDBService.deleteWithFilters(filterModel);
				if (isInserted)
					return new ResponseEntity<String>("Tasks Deleted successfully", HttpStatus.CREATED);
				else
					return new ResponseEntity<String>("Tasks not Deleted", HttpStatus.CONFLICT);
			}else
				return new ResponseEntity<String>("Tasks not Deleted due to filter issue",HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/findTask" ,method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Task>> findTasks(
			@RequestParam(value="task", required=false) List<String> taskIds, 
			@RequestParam(value="label", required=false) List<String> labelIds, 
			@RequestParam(value="priority", required=false) List<String> priorities, 
			@RequestParam(value="period", required=false) String period,
			@RequestParam(value="fromTime", required=false) Long fromTime,
			@RequestParam(value="toTime", required=false) Long toTime) {
		List<Task> tasks = null;
		try {
			FilterModel filterModel = FilterModelHelper.getFilterModelForTaskWithLst(taskIds, labelIds, priorities,period,fromTime, toTime);
			tasks = genDBService.getAllObjects(filterModel, Task.class);
			if (tasks != null && !tasks.isEmpty()) {
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO add log
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
		
	private Category getCategory(String labelName) {
//		String labelName = task.getCategory().getLabelName();
		List<FilterValue> filterValue = new ArrayList<FilterValue>();
		filterValue.add(new FilterValue(FilterType.LABEL_NAME, labelName));
		FilterModel filterModel = FilterHandler.getFilterModel(filterValue, Category.class);
		List<Category> labelObjs = genDBService.getAllObjects(filterModel, Category.class);
		if(labelObjs!=null && !labelObjs.isEmpty() && !(labelObjs.size()>1))
			return labelObjs.get(0);
//			task.setCategory(labelObjs.get(0));
		return null;
	}
}

