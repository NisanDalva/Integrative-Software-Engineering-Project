package twins.logic;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import twins.CreatedBy;
import twins.ItemId;
import twins.Location;
import twins.UserId;
import twins.Exceptions.AccessDeniedException;
import twins.Exceptions.ItemNotFoundException;
import twins.boundaries.ItemBoundary;
import twins.boundaries.UserBoundary;
import twins.data.ItemDao;
import twins.data.ItemEntity;

@Service
public class ItemsServiceImplementation implements AdvancedItemService {
	private ItemDao itemDao;
	private String space;
	private UsersServiceImplementation usersServiceImplementation;
	private Utils utils;
	
	public ItemsServiceImplementation() {
	}
	
	@Autowired
	public ItemsServiceImplementation(ItemDao itemDao) {
		super();
		this.itemDao = itemDao;
	}

	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}
	
	@Autowired
	public void setUtils(Utils utils) {
		this.utils = utils;
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

			entity.setId(UUID.randomUUID().toString() + "__" + this.space);
			entity.setUserSpace(userSpace);
			entity.setEmail(userEmail);
			entity.setCreatedTimestamp(new Date());

			entity = this.itemDao.save(entity);

			return this.entityToBoundary(entity);

		}
		else
			throw new AccessDeniedException(user.getRole() + " can't create items! (Only MANAGER)");

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
				throw new ItemNotFoundException("item with id " + itemId + "__" + itemSpace + " not available in the database");
			}
			return this.entityToBoundary(updated);
		}
		else
			throw new AccessDeniedException(user.getRole() + " can't update item! (Only MANAGER)");
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
		if(userRole.equals("MANAGER")||userRole.equals("PLAYER")) {
			Optional<ItemEntity> op = this.itemDao.findById(itemId + "__" + itemSpace); //check how to get specific item. 
			if (op.isPresent()) {
				ItemEntity entity = op.get();
				boolean active=entity.getActive();
				if(active==true||(active==false&&userRole.equals("MANAGER")))
					return this.entityToBoundary(entity);
				else
					throw new AccessDeniedException(userRole + " can't get items that not activate! (Only MANAGER)");

			} else {
				throw new ItemNotFoundException("item with id " + itemId + "__" + itemSpace + " not available in the database");
			}
		}
		else
			throw new AccessDeniedException(userRole + " can't get specific items! (Only PLAYER or MANAGER)");
	}

	public ItemEntity boundaryToEntity(ItemBoundary boundary) {
		ItemEntity entity = new ItemEntity();

		if (boundary.getItemId() != null) {
			entity.setId(boundary.getItemId().getId() + "__" + this.space);
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
		entity.setItemAttributes(this.utils.marshal(boundary.getItemAttributes()));
		entity.setName(boundary.getName());
		entity.setType(boundary.getType());
		

		return entity;
	}

	public ItemBoundary entityToBoundary(ItemEntity entity) {
		ItemBoundary boundary = new ItemBoundary();
		String tmp[] = entity.getId().split("__");
		boundary.setItemId(new ItemId(tmp[1],tmp[0]));
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setLocation(new Location(entity.getLat(),entity.getLng()));
		boundary.setCreatedBy(new CreatedBy(new UserId(entity.getUserSpace(),entity.getEmail())));
		boundary.setItemAttributes(this.utils.unmarshal(entity.getItemAttributes(), Map.class));
		return boundary;
	}

	@Override
	@Transactional
	public void deleteAllItems(String adminSpace, String adminEmail) {
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN"))
			this.itemDao.deleteAll(); //DELETE BY SPACE ??
		else
			throw new AccessDeniedException(user.getRole() + " can't delete all items! (Only ADMIN)");
	}

	@Override
	public List<ItemBoundary> getAllItems(String userSpace, String userEmail, int size, int page) {
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		String userRole=user.getRole();
		Page<ItemEntity> allEntitiesPage = this.itemDao.findAll(PageRequest.of(page, size, Direction.ASC, "name", "id"));
		List<ItemEntity> allItemEntities = allEntitiesPage.getContent();
		List<ItemBoundary> rv = new ArrayList<>();
		

		for (ItemEntity entity : allItemEntities) {
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
			throw new AccessDeniedException(userRole + " can't get all items!");
	}

	@Override
	@Transactional
	public void deleteSpecificItem(String userSpace, String userEmail, ItemId itemid) {
		String id=itemid.getId()+"__"+itemid.getSpace();
		UserBoundary user= usersServiceImplementation.login(userSpace, userEmail);
		if(user.getRole().equals("ADMIN"))
			this.itemDao.deleteById(id); //DELETE BY SPACE ??
		else
			throw new AccessDeniedException(user.getRole() + " can't delete all items! (Only ADMIN)");
		
	}

	@Override
	public List<ItemBoundary> getAllByActiveAndName(String userSpace, String userEmail, String name, int size, int page) {
//		UserBoundary user = usersServiceImplementation.login(userSpace, userEmail);
		
//		List<ItemEntity> entities = this.itemDao.findAllByNameLike(name, PageRequest.of(page, size, Direction.ASC, "name", "id"));
//		
//		List<ItemBoundary> rv = new ArrayList<>();
//		for(ItemEntity entity: entities) {
//			ItemBoundary boundary = this.entityToBoundary(entity);
//			rv.add(boundary);
//		}
//		
//		
//		return rv;
		return null;
	}

}