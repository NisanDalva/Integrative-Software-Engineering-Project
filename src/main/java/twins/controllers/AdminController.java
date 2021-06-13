package twins.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.logic.AdvancedItemService;
import twins.logic.AdvancedOperationsService;
import twins.logic.AdvancedUserService;

@RestController
public class AdminController {
	private AdvancedItemService advancedItemsService;
	private AdvancedUserService advancedUserService;
	private AdvancedOperationsService advancedOperationsService;
	
	@Autowired
	public void setItemsServiceImplementation(AdvancedItemService advancedItemsServiceImp) {
		this.advancedItemsService = advancedItemsServiceImp;
	}
	
	@Autowired
	public void setUsersServiceImplementation(AdvancedUserService advancedUserService) {
		this.advancedUserService = advancedUserService;
	}
	
	@Autowired
	public void setOperationsServiceImplementation(AdvancedOperationsService advancedOperationsService) {
		this.advancedOperationsService = advancedOperationsService;
	}
	
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.advancedUserService.deleteAllUsers(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItemsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.advancedItemsService.deleteAllItems(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperationsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.advancedOperationsService.deleteAllOperations(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail,
				@RequestParam(name="size", required = false, defaultValue = "5") int size,
				@RequestParam(name="page", required = false, defaultValue = "0")  int page) {
			
		List<UserBoundary> rv = this.advancedUserService.getAllUsers(userSpace, userEmail, size, page);
		
		return rv.toArray(new UserBoundary[0]);
	}


	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "5") int size,
			@RequestParam(name="page", required = false, defaultValue = "0")  int page) {

		List<OperationBoundary> rv = this.advancedOperationsService.getAllOperations(userSpace, userEmail,size, page );

		return rv.toArray(new OperationBoundary[0]);
	}
	
}
