package twins.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.boundaries.ItemBoundary;
import twins.data.ItemDao;
import twins.data.ItemEntity;

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

	// TODO make sure race conditions are handled
	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		Iterable<ItemEntity> allEntities = this.itemDao.findAll();
		List<ItemBoundary> rv = new ArrayList<>();
		for (ItemEntity entity : allEntities) {
			// TODO create a generic converter from entity to boundary
			ItemBoundary boundary = new ItemBoundary();
			
			boundary.setItemId(entity.getItemId());
			boundary.setType(entity.getType());
			boundary.setName(entity.getName());
			boundary.setActive(entity.getActive());
			boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
			boundary.setLocation(entity.getLocation());
			boundary.setItemAttributes(entity.getItemAttributes());
			boundary.setCreatedBy(entity.getCreatedBy());
			
			rv.add(boundary);
		}		
		return rv;
	}

	@Override
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		//Optional<ItemEntity> entity = this.itemDao.findById(itemId); //check how to get specific item. 
		ItemEntity entity = new ItemEntity();
		ItemBoundary boundary = new ItemBoundary();
		boundary.setItemId(entity.getItemId());
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setLocation(entity.getLocation());
		boundary.setItemAttributes(entity.getItemAttributes());
		boundary.setCreatedBy(entity.getCreatedBy());
		return boundary;
	}

	
	@Override
	public void deleteAllItems(String adminSpace, String adminEmail) {
		itemDao.deleteAll();
	}

}
