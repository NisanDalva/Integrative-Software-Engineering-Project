package twins.controllers;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.InvokedBy;
import twins.Item;
import twins.ItemId;
import twins.OperationBoundary;
import twins.OperationId;
import twins.UserBoundary;
import twins.UserId;

@RestController
public class AdminController {
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("all users were deleted successfully");
	}
	
	
	@RequestMapping(
			path = "/twins/admin/items/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllItemsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("all items were deleted successfully");
	}
	
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllOperationsBySpace (@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("all operations were deleted successfully");
	}
	
	
	@RequestMapping(
			path = "/twins/admin/users/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] exportAllUsers(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail) {
		
		// STUB implementation
		UserBoundary ub = new UserBoundary(new UserId(userSpace, userEmail), "demo_role", "demo_username", "demo_avatar");
		return new UserBoundary[] {ub, ub, ub};
	}
	
	
	@RequestMapping(
			path = "/twins/admin/operations/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public OperationBoundary[] exportAllOperations(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail) {
		
		// STUB implementation
		OperationBoundary ob = new OperationBoundary(new OperationId("demo_space", "demo_id"), "operation_type",
				new Item(new ItemId("item_space", "idem_id")), new InvokedBy(new UserId(userSpace, userEmail)));
		
		return new OperationBoundary[] {ob, ob, ob};
	}
	
}
