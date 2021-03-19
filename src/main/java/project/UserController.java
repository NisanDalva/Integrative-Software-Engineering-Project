package project;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
	ArrayList<UserBoundary> users=new ArrayList<>();
	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary CreateNewUser(@RequestBody NewUserDetails input,@PathVariable("userSpace") String userSpace,@PathVariable("userEmail") String userEmail) {
		UserBoundary user=new UserBoundary(input.getRole(),input.getUsername(),input.getAvatar(),userSpace,userEmail);
		//users.add(user);
		
		return user;
		
		
	}
	
	@RequestMapping(
			path = "/twins/users/login/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary LoginAndRetrieve(@PathVariable("userSpace") String userSpace,@PathVariable("userEmail") String userEmail) {
		UserBoundary user=new UserBoundary("manager","dolev","12345",userSpace,userEmail);///////need to improve!!!
		return user;
		
	}
	
	@RequestMapping(
			path="/twins/users/{userSpace}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateUser(@PathVariable("userSpace") String userSpace,@PathVariable("userEmail") String userEmail,@RequestBody UserBoundary update) {
		
	}
}
