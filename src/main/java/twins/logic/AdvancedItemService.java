package twins.logic;

import java.util.List;

import twins.ItemId;
import twins.boundaries.ItemBoundary;

public interface AdvancedItemService extends ItemsService {
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page);
	public void deleteSpecificItem(String userSpace, String userEmail,ItemId itemid);
	public List<ItemBoundary> getAllByActiveAndName(String userSpace, String userEmail, String name, int size, int page);
}
