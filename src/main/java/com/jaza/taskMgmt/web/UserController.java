/*package com.jaza.taskMgmt.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jaza.taskMgmt.db.access.FilterHandler;
import com.jaza.taskMgmt.db.access.FilterType;
import com.jaza.taskMgmt.db.access.FilterValue;
import com.jaza.taskMgmt.db.access.model.FilterModel;
import com.jaza.taskMgmt.db.models.User;
import com.jaza.taskMgmt.service.GenDBService;
import com.jaza.taskMgmt.service.UserService;

*//**
 * Handles requests for the application home page.
 *//*
@Controller
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private GenDBService genDBService;
	
	*//**
	 * Simply selects the home view to render by returning its name.
	 *//*
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		
//		model.addAttribute("users", userService.getAllUsers() );
		
		FilterModel filterModel = FilterHandler.getFilterModel(new ArrayList<FilterValue>(), User.class);
		List<User> users = genDBService.getAllObjects(filterModel, User.class);
		model.addAttribute("users", users);
//		model.addAttribute("users", genDBService.getAllObjects(filterModel, User.class);
		return "home";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addUser() {
		
//		userService.save(new User("username_" + new Random().nextInt(1000)));
		List<User> users= new ArrayList<>();
		users.add(new User("username_" + new Random().nextInt(1000)));
		genDBService.save(users);
		
		return "redirect:/";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String removeUser(@PathVariable("id") int userId) {
		
//		userService.delete(userId);
		
		List<FilterValue> userFilterValues = new ArrayList<FilterValue>();
		userFilterValues.add(new FilterValue(FilterType.ID, userId));
		FilterModel filterModel = FilterHandler.getFilterModel(userFilterValues, User.class);
		genDBService.deleteWithFilters(filterModel);
		return "redirect:/";
	}
	
}
*/