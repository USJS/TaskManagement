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
@RequestMapping(value = "/labels")
public class LabelRestController {

	@Autowired
	private GenDBService genDBService;

	@RequestMapping(value="/insertList",method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> addLabels(@RequestBody List<Category> labels) {
		return addLabel(labels);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> addLabel(@RequestBody Category label) {
		List<Category> labels = new ArrayList<>();
		labels.add(label);
		return addLabel(labels);
	}
	
	private ResponseEntity<String> addLabel(List<Category> labels){
		try {
			boolean isInserted = genDBService.save(labels);
			if (isInserted)
				return new ResponseEntity<String>("Labels inserted successfully", HttpStatus.CREATED);
			else
				return new ResponseEntity<String>("Labels not inserted", HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Category>> getLabels() {
		List<Category> labels = null;
		try {
			FilterModel filterModel = FilterHandler.getFilterModel(new ArrayList<FilterValue>(), Category.class);
			labels = genDBService.getAllObjects(filterModel, Category.class);
			if (labels != null && !labels.isEmpty()) {
				return new ResponseEntity<List<Category>>(labels, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Category>>(labels, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO add log
			return new ResponseEntity<List<Category>>(labels, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<List<Category>> updateLabel(@RequestBody Category label) {
		List<Category> labels = new ArrayList<>();
		try {
			List<FilterValue> filterValue = new ArrayList<FilterValue>();
			filterValue.add(new FilterValue(FilterType.LABEL_NAME, label.getLabelName()));
			FilterModel filterModel  = FilterHandler.getFilterModel(filterValue, Category.class);
			labels = genDBService.getAllObjects(filterModel, Category.class);
			if(labels!=null && !labels.isEmpty() && !(labels.size()>1)) {
				List<Category> upDatedlabels = new ArrayList<>();
				upDatedlabels.add(label);
				boolean isInserted = genDBService.save(upDatedlabels);
				if (isInserted)
					return new ResponseEntity<List<Category>>(upDatedlabels, HttpStatus.CREATED);
				else
					return new ResponseEntity<List<Category>>(labels, HttpStatus.CONFLICT);
			}else
				return new ResponseEntity<List<Category>>(labels, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<List<Category>>(labels, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/deleteLabel", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> deleteTaskWithId(@RequestParam(value="label") String label) {
		try {
			FilterModel taskFilterModel = FilterModelHelper.getFilterModelForTask(null, label, null, null,null,null);
			if(taskFilterModel!=null) {
				genDBService.deleteWithFilters(taskFilterModel);
			}
			List<FilterValue> filterValue= new ArrayList<FilterValue>();
			if(label!=null && !label.isEmpty())
				filterValue.add(new FilterValue(FilterType.LABEL_NAME, label));
			FilterModel filterModel = FilterHandler.getFilterModel(filterValue, Category.class);
			boolean isInserted = genDBService.deleteWithFilters(filterModel);
			if (isInserted)
				return new ResponseEntity<String>("Label Deleted successfully", HttpStatus.CREATED);
			else
				return new ResponseEntity<String>("Label not Deleted", HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/getAllTasks" ,method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Task>> findTasks(
			@RequestParam(value="label", required=false) String label){
		List<Task> tasks = new ArrayList<>();
		try {
			List<FilterValue> filterValue = new ArrayList<FilterValue>();
			filterValue.add(new FilterValue(FilterType.LABEL_NAME, label));
			FilterModel filterModel = FilterHandler.getFilterModel(filterValue, Category.class);
			List<Category> labels = genDBService.getAllObjects(filterModel, Category.class);
			if (labels != null && !labels.isEmpty()) {
				for(Category labelObj : labels) {
					tasks.addAll(labelObj.getTasks());
				}
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Task>>(tasks, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO add log
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
}

