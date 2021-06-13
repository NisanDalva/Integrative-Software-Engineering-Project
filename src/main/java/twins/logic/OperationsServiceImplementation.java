package twins.logic;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import twins.CreatedBy;
import twins.InvokedBy;
import twins.Item;
import twins.ItemId;
import twins.OperationId;
import twins.UserId;
import twins.Exceptions.AccessDeniedException;
import twins.Exceptions.InvalidOperationException;
import twins.Exceptions.UnsupportedOperationException;
import twins.boundaries.ItemBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.data.ItemDao;
import twins.data.ItemEntity;
import twins.data.OperationDao;
import twins.data.OperationEntity;

@Service
public class OperationsServiceImplementation implements AdvancedOperationsService {
	private OperationDao operationDao;
	private String space;
	private UsersServiceImplementation usersServiceImplementation;
	private ItemsServiceImplementation itemsServiceImplementation;
	private Utils utils;
	private JmsTemplate jmsTemplate;
	private ObjectMapper jackson;
	private ItemDao itemDao;

	@Autowired
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Autowired
	public void setItemsServiceImplementation(ItemsServiceImplementation itemsServiceImplementation) {
		this.itemsServiceImplementation = itemsServiceImplementation;
	}
	
	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}
	@Autowired
	public OperationsServiceImplementation(OperationDao operationDao) {
		super();
		this.operationDao = operationDao;
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
	public Object invokeOperation(OperationBoundary operation) {
		if(operation == null)
			throw new InvalidOperationException("Operation can't be null");
		
		OperationEntity entity = this.boundaryToEntity(operation);
		
		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("InvokedBy or userId is null!");
		
		UserId userId=operation.getInvokedBy().getUserId();
		UserBoundary user= usersServiceImplementation.login(userId.getSpace(), userId.getEmail());
		
		if(!user.getRole().equals("PLAYER"))
			throw new RuntimeException("Only Player can invoke oprerations!");

		if(operation.getType() == null) 
			throw new RuntimeException("Operation type can't be null");
		
		operation.getInvokedBy().getUserId().setSpace(space);
		operation.setCreatedTimestamp(new Date());
		
		
		switch (operation.getType()) {
		case "rank restaurant":
			if(operation.getItem() == null || operation.getItem().getItemId() == null
			|| operation.getItem().getItemId().getId() == null)
				throw new RuntimeException("The item or its attributes is null!");

			ItemId itemId=operation.getItem().getItemId();
			ItemBoundary item=itemsServiceImplementation.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(),itemId.getId());

			rankRestaurant(user.getUserid().getSpace(), user.getUserid().getEmail(), item.getItemId().getSpace(),
					item.getItemId().getId(), operation.getOperationAttributes());
			break;
			
		case "search restaurant":
			return searchRestaurantByName(operation);
			
			
		case "view restaurants":
			return viewRestaurants(operation);
			
			
		case "View menu":
			return viewMenu(operation);
			
			
		case "place order":
			return placeOrder(operation, entity, user);
			
		default:
			throw new UnsupportedOperationException("operation " + operation.getType() + " is unsupported");
		}

//		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());

		entity = this.operationDao.save(entity);
		return this.entityToBoundary(entity);
	}
	
	private ItemBoundary[] viewMenu(OperationBoundary operation) {
		UserId userid= operation.getInvokedBy().getUserId();
		ItemId item=operation.getItem().getItemId();
		ItemBoundary restaurant= this.itemsServiceImplementation.getSpecificItem(userid.getSpace(), userid.getEmail(),item.getSpace(), item.getId());
//		if(restaurant.getItemAttributes().containsValue(operation.getOperationAttributes().get("menuId"))) {
//			ItemBoundary menu= this.itemsServiceImplementation.getSpecificItem(userid.getSpace(), userid.getEmail(),item.getSpace(),""+ operation.getOperationAttributes().get("menuId"));
//			return menu;
//		}
//		else
//			throw new UnsupportedOperationException("Menu cant found in this restaurant!");
			

		List<String> menuIds = (List<String>) restaurant.getItemAttributes().get("menus");
		List<ItemEntity> menus = new ArrayList<>();
		List<ItemBoundary> rv = new ArrayList<>();
		
		for (String s: menuIds) {
			Optional<ItemEntity> op = this.itemDao.findById(s + "__" + this.space);
			
			if (op.isPresent())
				menus.add(op.get());
			else
				throw new RuntimeException("menu with id " + s + " is not available in the database");
		}
		
		
		for(ItemEntity e: menus) {
			ItemBoundary it = this.itemsServiceImplementation.entityToBoundary(e);
			rv.add(it);
		}
		
		
		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());
		this.operationDao.save(entity);
		
		
		return rv.toArray(new ItemBoundary[0]);
		
	}

	public ItemBoundary[] searchRestaurantByName(OperationBoundary operation) {
		int page = (int) operation.getOperationAttributes().get("page");
		int size = (int) operation.getOperationAttributes().get("size");
		
		String name = "%" + (String) operation.getOperationAttributes().get("name") + "%";
		
		List<ItemEntity> entities = this.itemDao.findAllByActiveAndNameLike(true, name, PageRequest.of(page, size, Direction.ASC, "name", "id"));
		List<ItemBoundary> rv = new ArrayList<>();

		for(ItemEntity e: entities) {
			ItemBoundary it = this.itemsServiceImplementation.entityToBoundary(e);
			rv.add(it);
		}
		
		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());
		this.operationDao.save(entity);
		
		return rv.toArray(new ItemBoundary[0]);
	}
	
	public ItemBoundary[] viewRestaurants(OperationBoundary operation) {
		
		int page = (int) operation.getOperationAttributes().get("page");
		int size = (int) operation.getOperationAttributes().get("size");

		List<ItemEntity> entities = this.itemDao.findAllByTypeAndActive("restaurant", true, PageRequest.of(page, size, Direction.ASC, "name", "id"));
		List<ItemBoundary> rv = new ArrayList<>();

		for(ItemEntity e: entities) {
			ItemBoundary it = this.itemsServiceImplementation.entityToBoundary(e);
			rv.add(it);
		}
		
		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());
		this.operationDao.save(entity);
		
		return rv.toArray(new ItemBoundary[0]);
	}

	public void rankRestaurant(String userSpace, String userEmail, String itemSpace, String itemId, Map<String, Object> operationAttributes) {
		ItemBoundary boundary = this.itemsServiceImplementation.getSpecificItem(userSpace, userEmail, itemSpace, itemId);
		
		Map<String, Object> newAttributes = boundary.getItemAttributes();
		
		int newRank = (int) operationAttributes.get("rank");
		int newNumOfRankers = (int) newAttributes.get("number of rankers") + 1;

		newAttributes.put("number of rankers", newNumOfRankers);
		
		if (newNumOfRankers == 1)
			newAttributes.put("rank", newRank);
		else {
			int oldAvargeRank = (int)newAttributes.get("rank");
			newAttributes.put("rank", getAvg(oldAvargeRank, newRank, newNumOfRankers));
		}
		
		boundary.setItemAttributes(newAttributes);
		
		ItemEntity entity = this.itemsServiceImplementation.boundaryToEntity(boundary);

		entity.setId(entity.getId());
		this.itemDao.save(entity);
		
	}
	
	private int getAvg(int prev_avg, int x, int n)
    {
        return (prev_avg * (n-1) + x) / n;
    }
	
	private ItemBoundary placeOrder(OperationBoundary operation, OperationEntity entity, UserBoundary user) {
		Map<String, Object> attributes = operation.getOperationAttributes();
		
		// make sure the restaurant is available in the database
		ItemId itemId = operation.getItem().getItemId();		
		this.itemsServiceImplementation.getSpecificItem(user.getUserid().getSpace(), user.getUserid().getEmail(), itemId.getSpace(),itemId.getId());
		
		
		// make sure all the products id is available, and put them on a list
		@SuppressWarnings("unchecked")
		List<String> productsIds = (List<String>) attributes.get("productIds");
		List<ItemEntity> products = new ArrayList<>();
		
		for (String s: productsIds) {
			Optional<ItemEntity> op = this.itemDao.findById(s + "__" + this.space);
			
			if (op.isPresent())
				products.add(op.get());
			else
				throw new RuntimeException("product with id " + s + " is not available in the database");
		}
		
		// calculate the total price of products
		int totalOrderPrice = 0;
		for(ItemEntity e: products) {
			int price = (int) this.itemsServiceImplementation.entityToBoundary(e).getItemAttributes().get("price");
			totalOrderPrice += price;
		}
		
		// create a boundary of item with type 'order'
		ItemBoundary orderBoundary = new ItemBoundary();
		
		orderBoundary.setCreatedTimestamp(new Date());
		
		CreatedBy createdBy = new CreatedBy();
		createdBy.setUserId(user.getUserid());
		orderBoundary.setCreatedBy(createdBy);
		
		ItemId newItemId = new ItemId();
		newItemId.setSpace(this.space);
		newItemId.setId(UUID.randomUUID().toString());
		orderBoundary.setItemId(newItemId);
		
		orderBoundary.setName("order_" + user.getUsername() + "_" + operation.getItem().getItemId().getId());
		
		orderBoundary.setType("order");
		
		orderBoundary.setItemAttributes(attributes);
		orderBoundary.getItemAttributes().put("total price", totalOrderPrice);
		
		// update the operation entity and store it on 'OPERATIONS' table
		operation.getOperationAttributes().put("total price", totalOrderPrice);
		entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());
		entity = this.operationDao.save(entity);
		
		// store the order as an entity in 'ITEMS' table
		ItemEntity itemEntity = this.itemsServiceImplementation.boundaryToEntity(orderBoundary);
		this.itemDao.save(itemEntity);
		
		// return the result to the user
		return this.itemsServiceImplementation.entityToBoundary(itemEntity);
		
	}

	private OperationBoundary updateAttributes(OperationBoundary operation, String keyType,Object attributesToAdd) {
		Map<String, Object> newAttributes = operation.getOperationAttributes();
		newAttributes.put(keyType, attributesToAdd);
		operation.setOperationAttributes(newAttributes);
		return operation;
	}

	private List<ItemBoundary> getAllResturants(String userSpace, String userEmail) {
		List<ItemBoundary> itemsList= this.itemsServiceImplementation.getAllItems(userSpace, userEmail);
		itemsList.removeIf(n -> n.getType().equals("restaurant"));
		return itemsList;
		
	}

//	private ItemBoundary searchRestaurant(String userSpace, String userEmail, String itemSpace, String itemId) {
//		ItemBoundary boundary = this.itemsServiceImplementation.getSpecificItem(userSpace, userEmail, itemSpace, itemId);
//		return boundary;
//	}
	
	private List<ItemBoundary> searchRestaurantByName(String userSpace, String userEmail, String name, int size, int page) {
//		List<ItemBoundary> boundaries = this.itemsServiceImplementation.getAllByActiveAndName(userSpace, userEmail, name, size, page);
//		
//		return boundaries;
		
//		int page = (int) operation.getOperationAttributes().get("page");
//		int size = (int) operation.getOperationAttributes().get("size");
//		
//		String name = "%" + (String) operation.getOperationAttributes().get("name") + "%";
//		
//		List<ItemBoundary> boundaries = this.searchRestaurantByName(user.getUserid().getSpace(), user.getUserid().getEmail(), name, size, page);
//		
//		
//		entity.setId(UUID.randomUUID().toString());
//		entity = this.operationDao.save(entity);
//		
//		return boundaries.toArray(new ItemBoundary[0]);
		return null;
	}
	




	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		return (OperationBoundary) invokeOperation(operation);
	}
	
	
	
	public OperationBoundary sendAndForget(OperationBoundary input) {
		try {
			OperationId id=input.getOperationId();
			id.setId(UUID.randomUUID().toString());
			input.setOperationId(id);
			
			String json = this.jackson
				.writeValueAsString(input);
			
			// send json to MOM
			this.jmsTemplate.send(
					"Operations", // destination name 
					session->session.createTextMessage(json)); // lambda that creates a text message


			return input;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		//		Iterable<OperationEntity> allOperationsEntities = this.operationDao.findAll();
		//		List<OperationBoundary> operationsBoundaryList = new ArrayList<>();
		//		
		//		for (OperationEntity entity : allOperationsEntities) {
		//			OperationBoundary boundary = entityToBoundary(entity);
		//			operationsBoundaryList.add(boundary);
		//		}
		//		return operationsBoundaryList;
		throw new RuntimeException("deprecated operation - use the new API getAllOperations(userSpace, userEmail, size, page)");
	}

	private OperationEntity boundaryToEntity(OperationBoundary boundary) {
		OperationEntity entity = new OperationEntity();

		if (boundary.getItem()!= null) {
			entity.setItemId(boundary.getItem().getItemId().getId());
			entity.setItemSpace(boundary.getItem().getItemId().getSpace());
		}

		if (boundary.getInvokedBy() != null) {
			if (boundary.getInvokedBy().getUserId() != null) {
				entity.setEmail(boundary.getInvokedBy().getUserId().getEmail());
				entity.setUserSpace(boundary.getInvokedBy().getUserId().getSpace());
			}
		}

		if (boundary.getOperationId() != null) {
			entity.setId(boundary.getOperationId().getId() + "__" + this.space);
		}

		entity.setType(boundary.getType());
		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());		
		entity.setOperationAttributes(this.utils.marshal(boundary.getOperationAttributes()));
		return entity;
	}


	private OperationBoundary entityToBoundary(OperationEntity entity) {
		OperationBoundary boundary = new OperationBoundary();
		boundary.setType(entity.getType());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInvokedBy(new InvokedBy(new UserId(entity.getUserSpace(), entity.getEmail())));
		boundary.setOperationId(new OperationId(this.space, entity.getId().split("__")[0]));
		boundary.setItem(new Item(new ItemId(entity.getItemSpace(), entity.getItemId())));
		boundary.setOperationAttributes(this.utils.unmarshal(entity.getOperationAttributes(), Map.class));

		return boundary;
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN"))
			this.operationDao.deleteAll();
		else
			throw new AccessDeniedException(user.getRole() + " can't delete all operations! (Only ADMIN)");
			
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int size, int page) {
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN")) {
			Page<OperationEntity> operationsEntitiesPage = this.operationDao.findAll(PageRequest.of(page, size, Direction.ASC, "type", "id"));
			List<OperationEntity> allOperationsEntities = operationsEntitiesPage.getContent();
			List<OperationBoundary> operationsBoundaryList = new ArrayList<>();

			for (OperationEntity entity : allOperationsEntities) {
				OperationBoundary boundary = entityToBoundary(entity);
				operationsBoundaryList.add(boundary);
			}
			return operationsBoundaryList;
		}
		else
			throw new AccessDeniedException(user.getRole() + " can't get all operations! (Only ADMIN)");
	}
}
