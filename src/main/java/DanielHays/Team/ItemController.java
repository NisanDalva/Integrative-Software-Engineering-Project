package DanielHays.Team;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
	
	private enum eTypes {MEAL, DRINK, SOUCE};
	
	public ItemController() {
	}
	
	@RequestMapping(
		path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductBoundary retrieveProduct(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {
		
		// STUB implementation
		ItemId id = new ItemId(itemSpace, itemId);
		ProductBoundary rv = new ProductBoundary(id, "product type", "product name");
		
		rv.getItemAttributes().put("type", eTypes.MEAL.name());
		rv.getItemAttributes().put("price", 53.5);
		rv.getItemAttributes().put("ingredients", new String[] {"rice", "meat", "tomato", "onion"});
		rv.getItemAttributes().put("rank", 5);
		
		System.err.println(rv);
		
		return rv;
	}
}





















