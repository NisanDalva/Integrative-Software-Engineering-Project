package DanielHays.Team;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
//	private int counter; // ERROR!! DO NOT USE SUCH IMPLEMENTATION IN YOUR CODE
	
	private AtomicInteger counter;
	
	public HelloController() {
		this.counter = new AtomicInteger(1);
	}
	
	@RequestMapping(
		path = "/hello",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public HelloBoundary hello() {
		// STUB implementation
		
		HelloBoundary rv = new HelloBoundary(
				"Hello world!", 
				counter.getAndIncrement(),
				new UserDetailsBoundary("Jill", "James"));
		System.err.println(rv);
		return rv;
	}
}





















