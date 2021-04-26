package twins.logic;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.CreatedBy;
import twins.ItemId;
import twins.Location;
import twins.UserId;
import twins.boundaries.ItemBoundary;
import twins.data.ItemDao;
import twins.data.ItemEntity;

//@SuppressWarnings("unused")
@Service
public class ItemsServiceImplementation implements ItemsService {
	private ItemDao itemDao;
	private ObjectMapper jackson;
	private AtomicLong atomicLong; // TODO DO NOT RELY ON ATOMIC LONG IN PRODUCTION!!!!
	private String space;

	@Autowired
	public ItemsServiceImplementation(ItemDao itemDao) {
		super();
		this.itemDao = itemDao;
		this.jackson = new ObjectMapper();
		this.atomicLong = new AtomicLong(1L);
	}
	
	@Value("${spring.application.name}")
	public void setSpace(String space) {
		this.space = space;
	}
	
	
	@Override
	@Transactional
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {

		ItemEntity entity = this.boundaryToEntity(item);
		
		entity.setId("" + this.atomicLong.getAndIncrement());
		entity.setUserSpace(userSpace);
		entity.setEmail(userEmail);
		entity.setItemSpace(this.space);
		entity.setCreatedTimestamp(new Date());
		
		
		entity = this.itemDao.save(entity);
		
		
		return this.entityToBoundary(entity);
	}
 
	@Override
	@Transactional
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		
		Optional<ItemEntity> op = this.itemDao.findById(itemId);

		if(op.isPresent()) {
			ItemEntity existing = op.get();
			ItemEntity updated = this.boundaryToEntity(update);

			updated.setId(itemId);
			updated.setItemSpace(existing.getItemSpace());
			updated.setUserSpace(existing.getUserSpace());
			updated.setEmail(existing.getEmail());
			updated.setCreatedTimestamp(existing.getCreatedTimestamp());

			this.itemDao.save(updated);

		}else {
			throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		}
		return null;
	}

	// TODO make sure race conditions are handled
	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		Iterable<ItemEntity> allEntities = this.itemDao.findAll();
		List<ItemBoundary> rv = new ArrayList<>();
		for (ItemEntity entity : allEntities) {
			// TODO create a generic converter from entity to boundary
			ItemBoundary boundary = entityToBoundary(entity);		
			rv.add(boundary);
		}		
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		Optional<ItemEntity> op = this.itemDao.findById(itemId); //check how to get specific item. 
		if (op.isPresent()) {
			ItemEntity entity = op.get();
			return this.entityToBoundary(entity);
		} else {
			throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		}
	}

	private ItemEntity boundaryToEntity(ItemBoundary boundary) {
		ItemEntity entity = new ItemEntity();
		
		if (boundary.getItemId() != null) {
			entity.setId(boundary.getItemId().getId());
			entity.setItemSpace(boundary.getItemId().getSpace());
		}
		
		if (boundary.getCreatedBy() != null) {
			if (boundary.getCreatedBy().getUserId() != null) {
				entity.setEmail(boundary.getCreatedBy().getUserId().getEmail());
				entity.setUserSpace(boundary.getCreatedBy().getUserId().getSpace());
			}
		}
		
		if (boundary.getLocation() != null) {
			entity.setLat(boundary.getLocation().getLat());
			entity.setLng(boundary.getLocation().getLng());
		}
		
		entity.setActive(boundary.getActive());
		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		entity.setItemAttributes(this.marshal(boundary.getItemAttributes()));
		entity.setName(boundary.getName());
		entity.setType(boundary.getType());
		
		
		
	
		return entity;
	}
	
	private ItemBoundary entityToBoundary(ItemEntity entity) {
		ItemBoundary boundary = new ItemBoundary();
		boundary.setItemId(new ItemId(entity.getItemSpace(),entity.getId()));
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setLocation(new Location(entity.getLat(),entity.getLng()));
		boundary.setCreatedBy(new CreatedBy(new UserId(entity.getUserSpace(),entity.getEmail())));
		boundary.setItemAttributes(this.unmarshal(entity.getItemAttributes(), Map.class));
		return boundary;
	}
	
	@Override
	@Transactional
	public void deleteAllItems(String adminSpace, String adminEmail) {
		itemDao.deleteAll();
	}
	
	// use Jackson to convert JSON to Object
	private <T> T unmarshal(String json, Class<T> type) {
		try {
			return this.jackson
					.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String marshal(Object moreDetails) {
		try {
			return this.jackson
					.writeValueAsString(moreDetails);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}