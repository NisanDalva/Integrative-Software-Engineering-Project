package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import twins.boundaries.ItemBoundary;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
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
		UserId userId=operation.getInvokedBy().getUserId();
		UserBoundary user= usersServiceImplementation.login(userId.getSpace(), userId.getEmail());
		ItemId itemId=operation.getItem().getItemId();
		ItemBoundary item=itemsServiceImplementation.getSpecificItem(userId.getSpace(), userId.getEmail(), itemId.getSpace(),itemId.getId());
		if(user.getRole()!="PLAYER")
			throw new AccessDeniedException("Only Player can invoke oprerations!");
		if(item.getActive()==false)
			throw new AccessDeniedException("Item need to be active to invoke oprerations!");
		if(operation.getType() == null)
			throw new InvalidOperationException("Operation type can't be null");

		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new InvalidOperationException("InvokedBy or userId is null!");

		if(operation.getItem() == null || operation.getItem().getItemId() == null
				|| operation.getItem().getItemId().getId() == null)
			throw new InvalidOperationException("The item or its attributes is null!");

		operation.getInvokedBy().getUserId().setSpace(space);
		operation.setCreatedTimestamp(new Date());

		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId(UUID.randomUUID().toString());

		entity = this.operationDao.save(entity);
		return this.entityToBoundary(entity);
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
