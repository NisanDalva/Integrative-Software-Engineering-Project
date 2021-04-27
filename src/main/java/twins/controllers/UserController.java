package twins.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.NewUserDetails;
import twins.UserId;
import twins.boundaries.UserBoundary;
import twins.logic.UsersServiceImplementation;


@RestController
public class UserController {
	private UsersServiceImplementation usersServiceImplementation;
	private String space;
	
	@Value("${spring.application.name}")
	public void setSpace(String space) {
		this.space = space;
	}
	
	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}
	
	@RequestMapping(
			path = "/twins/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary CreateNewUser(@RequestBody NewUserDetails input) {
		UserBoundary boundary = new UserBoundary(new UserId(this.space, input.getEmail()), input.getRole(), input.getUsername(), input.getAvatar());
		return usersServiceImplementation.createUser(boundary);
	}
	
	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary LoginAndRetrieve(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		return this.usersServiceImplementation.login(userSpace, userEmail);
	}
	
	@RequestMapping(
			path="/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateUser(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {
		
		this.usersServiceImplementation.updateUser(userSpace, userEmail, update);
	}
	
}
