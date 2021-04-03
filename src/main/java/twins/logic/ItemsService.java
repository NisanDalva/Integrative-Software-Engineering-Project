package twins.logic;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import twins.boundaries.ItemBoundary;

public interface ItemsService {
	public ItemBoundary createItem(String userSpace, String userEmail, Item itemBoundary);
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,ItemBoundary update);
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail );
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId);
	public void deleteAllItems(String adminSpace, String adminEmail);
}
