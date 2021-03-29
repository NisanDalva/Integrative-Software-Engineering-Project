package twins.logic;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.ItemBoundary;
import twins.data.ItemDao;

@Service
public class ItemsServiceImplementation implements ItemsService {
	private ItemDao itemDao;
	
	@Autowired
	public ItemsServiceImplementation(ItemDao itemDao) {
		super();
		this.itemDao = itemDao;
	}

	@Override
	public ItemBoundary createItem(String userSpace, String userEmail, Item itemBoundary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
