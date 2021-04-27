package twins.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.logic.ItemsServiceImplementation;
import twins.logic.OperationsServiceImplementation;
import twins.logic.UsersServiceImplementation;

@RestController
public class AdminController {
	private ItemsServiceImplementation itemsServiceImplementation;
	private UsersServiceImplementation usersServiceImplementation;
	private OperationsServiceImplementation operationsServiceImplementation;
	
	@Autowired
	public void setItemsServiceImplementation(ItemsServiceImplementation itemsServiceImplementation) {
		this.itemsServiceImplementation = itemsServiceImplementation;
	}
	
	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}
	
	@Autowired
	public void setOperationsServiceImplementation(OperationsServiceImplementation operationsServiceImplementation) {
		this.operationsServiceImplementation = operationsServiceImplementation;
	}
	
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.usersServiceImplementation.deleteAllUsers(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItemsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.itemsServiceImplementation.deleteAllItems(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperationsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		this.operationsServiceImplementation.deleteAllOperations(userSpace, userEmail);
	}
	
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail) {
			
		List<UserBoundary> rv = this.usersServiceImplementation.getAllUsers(userSpace, userEmail);
		
		return rv.toArray(new UserBoundary[0]);
	}


	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {

		List<OperationBoundary> rv = this.operationsServiceImplementation.getAllOperations(userSpace, userEmail);

		return rv.toArray(new OperationBoundary[0]);
	}
	
}
