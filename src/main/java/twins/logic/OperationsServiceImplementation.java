package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.InvokedBy;
import twins.Item;
import twins.ItemId;
import twins.OperationId;
import twins.UserId;
import twins.boundaries.OperationBoundary;
import twins.boundaries.UserBoundary;
import twins.data.OperationDao;
import twins.data.OperationEntity;

@Service
public class OperationsServiceImplementation implements AdvancedOperationsService {
	private OperationDao operationDao;
	private ObjectMapper jackson;
	private AtomicLong atomicLong; // TODO DO NOT RELY ON ATOMIC LONG IN PRODUCTION!!!!
	private String space;
	private UsersServiceImplementation usersServiceImplementation;


	@Autowired
	public void setUsersServiceImplementation(UsersServiceImplementation usersServiceImplementation) {
		this.usersServiceImplementation = usersServiceImplementation;
	}
	@Autowired
	public OperationsServiceImplementation(OperationDao operationDao) {
		super();
		this.operationDao = operationDao;
		this.jackson = new ObjectMapper();
		this.atomicLong = new AtomicLong(1L);
	}

	@Value("${spring.application.name}")
	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	public Object invokeOperation(OperationBoundary operation) {
		if(operation == null)
			throw new RuntimeException("null operation");

		if(operation.getType() == null)
			throw new RuntimeException("null type");

		if(operation.getInvokedBy() == null || operation.getInvokedBy().getUserId() == null)
			throw new RuntimeException("null invoked by or null user id");

		if(operation.getItem() == null || operation.getItem().getItemId() == null
				|| operation.getItem().getItemId().getId() == null)
			throw new RuntimeException("null item or its attributes");

		operation.getInvokedBy().getUserId().setSpace(space);
		operation.setCreatedTimestamp(new Date());

		OperationEntity entity = this.boundaryToEntity(operation);
		entity.setId("" + this.atomicLong.getAndIncrement());

		entity = this.operationDao.save(entity);
		return this.entityToBoundary(entity);
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		return (OperationBoundary) invokeOperation(operation);
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
		throw new RuntimeException("deprecated operation - use the new API getAllItems(userSpace, userEmail, size, page)");
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
		entity.setOperationAttributes(this.marshal(boundary.getOperationAttributes()));
		return entity;
	}


	private OperationBoundary entityToBoundary(OperationEntity entity) {
		OperationBoundary boundary = new OperationBoundary();
		boundary.setType(entity.getType());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInvokedBy(new InvokedBy(new UserId(entity.getUserSpace(), entity.getEmail())));
		boundary.setOperationId(new OperationId(this.space, entity.getId().split("__")[0]));
		boundary.setItem(new Item(new ItemId(entity.getItemSpace(), entity.getItemId())));
		boundary.setOperationAttributes(this.unmarshal(entity.getOperationAttributes(), Map.class));

		return boundary;
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN"))
			this.operationDao.deleteAll();
		else
			throw new RuntimeException("Only admin delete all operations! ");
			
	}

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
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail, int size, int page) {
		UserBoundary user= usersServiceImplementation.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN")) {
			Iterable<OperationEntity> allOperationsEntities = this.operationDao.findAll(PageRequest.of(page, size, Direction.ASC, "type", "id"));
			List<OperationBoundary> operationsBoundaryList = new ArrayList<>();

			for (OperationEntity entity : allOperationsEntities) {
				OperationBoundary boundary = entityToBoundary(entity);
				operationsBoundaryList.add(boundary);
			}
			return operationsBoundaryList;
		}
		else
			throw new RuntimeException("Only admin can get all operations!!");

	}
}
