package twins.rest_api.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.OperationId;
import twins.business_logic.boundaries.OperationBoundary;


@RestController
public class OperationController {
	
	public OperationController() {
	}
	
	@RequestMapping(
			path = "/twins/operations",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperation (@RequestBody OperationBoundary input) {
		//STUB implementation
		input.setOperationId(new OperationId("demo_space", "demo_id"));
		return input;
	}
	
	@RequestMapping(
			path = "/twins/operations/async",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary asyncOperation (@RequestBody OperationBoundary input) {
		//STUB implementation
		input.setOperationId(new OperationId("demo_space", "demo_id"));
		return input;
	}
}