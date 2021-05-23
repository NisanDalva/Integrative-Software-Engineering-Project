package twins.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.CreatedBy;
import twins.ItemId;
import twins.UserId;
import twins.boundaries.ItemBoundary;
import twins.boundaries.UserBoundary;
import twins.logic.ItemsServiceImplementation;

@RestController
public class ItemController {
	
	public enum eTypes {MEAL, DRINK, SOUCE};
	private ItemsServiceImplementation itemsServiceImplementation;
	
	@Autowired
	public ItemController(ItemsServiceImplementation ItemsServiceImplementation) {
		this.itemsServiceImplementation = ItemsServiceImplementation;
	}
	
	@RequestMapping(
		path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary retrieveItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		
		
		return itemsServiceImplementation.getSpecificItem(userSpace, userEmail, itemSpace, itemId);
	}
	
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary[] getAllItems(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail,
				@RequestParam(name="size", required = false, defaultValue = "5") int size,
				@RequestParam(name="page", required = false, defaultValue = "0")  int page) {
		
		return itemsServiceImplementation.getAllItems(userSpace, userEmail, size, page).toArray(new ItemBoundary[0]);
	}
	
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary CreateNewItem(@RequestBody ItemBoundary input,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		return itemsServiceImplementation.createItem(userSpace, userEmail, input);
	}
	
	
	@RequestMapping(
			path="/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateItem(@RequestBody ItemBoundary update,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		
		itemsServiceImplementation.updateItem(userSpace, userEmail, itemSpace, itemId, update);
	}
	
	
}