package twins.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
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
import twins.data.UserEntity;

@Service
public class OperationsServiceImplementation implements OperationsService {
	private OperationDao operationDao;
	private ObjectMapper jackson;
	private AtomicLong atomicLong; // TODO DO NOT RELY ON ATOMIC LONG IN PRODUCTION!!!!
	
	@Autowired
	public OperationsServiceImplementation(OperationDao operationDao) {
		super();
		this.operationDao = operationDao;
		this.jackson = new ObjectMapper();
		this.atomicLong = new AtomicLong(1L);
	}
	
	@Override
	public Object invokeOperation(OperationBoundary operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OperationBoundary> getAllOperations(String adminSpace, String adminEmail) {
		Iterable<OperationEntity> allOperationsEntities = this.operationDao.findAll();
		List<OperationBoundary> operationsBoundaryList = new ArrayList<>();
		
		for (OperationEntity entity : allOperationsEntities) {
			OperationBoundary boundary = convertToBoundary(entity);
			operationsBoundaryList.add(boundary);
		}
		return operationsBoundaryList;
	}

	private OperationBoundary convertToBoundary(OperationEntity entity) {
		OperationBoundary boundary = new OperationBoundary();
		boundary.setType(entity.getType());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setInvokedBy(new InvokedBy(new UserId(entity.getUserSpace(), entity.getEmail())));
		boundary.setOperationId(new OperationId(entity.getOperationSpace(), entity.getId()));
		boundary.setItem(new Item(new ItemId(entity.getItemSpace(), entity.getItemId())));
		boundary.setOperationAttributes(this.unmarshal(entity.getOperationAttributes(), Map.class));
		
		return boundary;
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		this.operationDao.deleteAll();		
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
}
