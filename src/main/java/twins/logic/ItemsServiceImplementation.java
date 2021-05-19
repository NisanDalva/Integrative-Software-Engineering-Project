package twins.logic;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.CreatedBy;
import twins.ItemId;
import twins.Location;
import twins.UserId;
import twins.boundaries.ItemBoundary;
import twins.boundaries.UserBoundary;
import twins.data.ItemDao;
import twins.data.ItemEntity;

@Service
public class ItemsServiceImplementation implements AdvancedItemService {
	private ItemDao itemDao;
	private ObjectMapper jackson;
	private AtomicLong atomicLong; // TODO DO NOT RELY ON ATOMIC LONG IN PRODUCTION!!!!
	private String space;
	private UsersServiceImplementation usersServiceImplementation;

	@Autowired
	public ItemsServiceImplementation(ItemDao itemDao) {
		super();
		this.itemDao = itemDao;
		this.jackson = new ObjectMapper();
		this.atomicLong = new AtomicLong(1L);
	}

	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}

	@Value("${spring.application.name}")
	public void setSpace(String space) {
		this.space = space;
	}


	@Override
	@Transactional
	public ItemBoundary createItem(String userSpace, String userEmail, ItemBoundary item) {
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		if(user.getRole().equals("MANAGER")) {
			ItemEntity entity = this.boundaryToEntity(item);

			entity.setId("" + this.atomicLong.getAndIncrement() + "__" + this.space);
			entity.setUserSpace(userSpace);
			entity.setEmail(userEmail);
			entity.setCreatedTimestamp(new Date());

			entity = this.itemDao.save(entity);


			return this.entityToBoundary(entity);

		}
		else
			throw new RuntimeException("Only manager can create items!");

	}

	@Override
	@Transactional
	public ItemBoundary updateItem(String userSpace, String userEmail, String itemSpace, String itemId,
			ItemBoundary update) {
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		if(user.getRole().equals("MANAGER")) {
			Optional<ItemEntity> op = this.itemDao.findById(itemId + "__" + itemSpace);
			ItemEntity updated = this.boundaryToEntity(update);
			ItemEntity existing;
			if(op.isPresent()) {
				existing = op.get();

				updated.setId(itemId + "__" + itemSpace);
				updated.setUserSpace(existing.getUserSpace());
				updated.setEmail(existing.getEmail());
				updated.setCreatedTimestamp(existing.getCreatedTimestamp());

				this.itemDao.save(updated);

			}else {
				throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
			}
			return this.entityToBoundary(updated);
		}
		else
			throw new RuntimeException("Only manager can update item!");
	}

	// TODO make sure race conditions are handled
	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail) {
		//		Iterable<ItemEntity> allEntities = this.itemDao.findAll();
		//		List<ItemBoundary> rv = new ArrayList<>();
		//		for (ItemEntity entity : allEntities) {
		//			// TODO create a generic converter from entity to boundary
		//			ItemBoundary boundary = entityToBoundary(entity);		
		//			rv.add(boundary);
		//		}		
		//		return rv;
		throw new RuntimeException("deprecated operation - use the new API getAllItems(userSpace, userEmail, size, page)");
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getSpecificItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		String userRole=user.getRole();
		if(userRole.equals("MANAGER")||userRole.equals("Player")) {
			Optional<ItemEntity> op = this.itemDao.findById(itemId + "__" + itemSpace); //check how to get specific item. 
			if (op.isPresent()) {
				ItemEntity entity = op.get();
				boolean active=entity.getActive();
				if(active==true||(active==false&&userRole.equals("MANAGER")))
					return this.entityToBoundary(entity);
				else
					throw new RuntimeException("Only manager can get items that not activate!");

			} else {
				throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
			}
		}
		else
			throw new RuntimeException("Admin cant get specific items!");


	}

	private ItemEntity boundaryToEntity(ItemBoundary boundary) {
		ItemEntity entity = new ItemEntity();

		if (boundary.getItemId() != null) {
			entity.setId(boundary.getItemId().getId());
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
		String tmp[] = entity.getId().split("__");
		boundary.setItemId(new ItemId(tmp[1],tmp[0]));
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
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN"))
			this.itemDao.deleteAll(); //DELETE BY SPACE ??
		else
			throw new RuntimeException("Only admin delete all items!");
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

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page) {
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		String userRole=user.getRole();
		Iterable<ItemEntity> allEntities = this.itemDao.findAll(PageRequest.of(page, size, Direction.ASC, "name", "id"));

		List<ItemBoundary> rv = new ArrayList<>();
		

		for (ItemEntity entity : allEntities) {
			// TODO create a generic converter from entity to boundary
			ItemBoundary boundary = entityToBoundary(entity);		
			rv.add(boundary);
		}
		if(userRole.equals("MANAGER"))
			return rv;
		else if(userRole.equals("PLAYER")) {
			rv.removeIf(n -> n.getActive().equals(false));
			//rv.stream().filter(n->n.getActive().equals(false)) //plan B
			return rv;
			
		}
		else
			throw new RuntimeException("admin cant get all users!");
	}



}