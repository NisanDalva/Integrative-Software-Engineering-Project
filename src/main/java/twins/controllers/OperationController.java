package twins.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twins.OperationId;
import twins.boundaries.OperationBoundary;
import twins.logic.AdvancedOperationsService;
import twins.logic.OperationsServiceImplementation;


@RestController
public class OperationController {
	private AdvancedOperationsService advancedOperationsService;
	
	@Autowired
	public void setOperationsServiceImplementation(OperationsServiceImplementation operationsServiceImplementation) {
		this.advancedOperationsService = operationsServiceImplementation;
	}
	
	
	public OperationController() {
	}
	
	@RequestMapping(
			path = "/twins/operations",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeOperation (@RequestBody OperationBoundary input) {
		return this.advancedOperationsService.invokeOperation(input);
	}
	
	@RequestMapping(
			path = "/twins/operations/async",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary asyncOperation (@RequestBody OperationBoundary input) {
		return this.advancedOperationsService.sendAndForget(input);
	}
}