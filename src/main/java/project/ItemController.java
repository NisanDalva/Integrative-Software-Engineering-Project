package project;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
	
	public enum eTypes {MEAL, DRINK, SOUCE};
	
	public ItemController() {
	}
	
	@RequestMapping(
		path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary retrieveItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		
		// STUB implementation
		ItemId id = new ItemId(itemSpace, itemId);
		CreatedBy createdBy = new CreatedBy(new UserId(userSpace, userEmail));
		
		ItemBoundary rv = new ItemBoundary(id, "product type", "product name", createdBy);
		
		rv.getItemAttributes().put("type", eTypes.MEAL.name());
		rv.getItemAttributes().put("price", 53.5);
		rv.getItemAttributes().put("ingredients", new String[] {"rice", "meat", "tomato", "onion"});
		rv.getItemAttributes().put("rank", 5);
		
		System.err.println(rv);
		
		return rv;
	}
	
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ItemBoundary[] getAllItems(@PathVariable("userSpace") String userSpace,
				@PathVariable("userEmail") String userEmail) {
		
		// STUB implementation
		ItemBoundary it = new ItemBoundary(new ItemId("demo_item_space", "demo_item_id"), "demo_type", "demo_name", new CreatedBy(new UserId(userSpace, userEmail)));
		return new ItemBoundary[] {it, it, it};
	}
	
	
	@RequestMapping(
			path = "/twins/items/{userSpace}/{userEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary CreateNewItem(@RequestBody ItemBoundary input,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		// STUB implementation
		return input;
	}
	
	
	@RequestMapping(
			path="/twins/users/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void UpdateItem(@RequestBody UserBoundary update,
			@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		
		// STUB implementation
	}
	
	
}






