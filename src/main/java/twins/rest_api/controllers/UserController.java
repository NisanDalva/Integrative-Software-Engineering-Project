package twins.rest_api.controllers;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.NewUserDetails;
import twins.UserId;
import twins.business_logic.boundaries.UserBoundary;


@RestController
public class UserController {
	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary CreateNewUser(@RequestBody NewUserDetails input) {
		UserBoundary user = new UserBoundary(new UserId("demo_userSpace","demo_userEmail"), input.getRole(), input.getUsername(), input.getAvatar());
		return user;
	}
	
	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary LoginAndRetrieve(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		UserBoundary user = new UserBoundary(new UserId(userSpace,userEmail), "manager", "dolev", "12345");
		return user;
	}
	
	@RequestMapping(
			path="/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateUser(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {
		
		System.err.println("updated successfully");
	}
	
}
