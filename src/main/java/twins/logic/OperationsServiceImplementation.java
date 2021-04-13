package twins.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Autowired
	public OperationsServiceImplementation(OperationDao operationDao) {
		super();
		this.operationDao = operationDao;
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
		//boundary.setInvokedBy(entity.getInvokedBy());
		OperationId operationid=new OperationId(entity.getSpace(),entity.getId());
		boundary.setOperationId(operationid);
		return boundary;
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		//OperationBoundary checkAdmin=login(adminSpace,adminEmail);
		//if(checkAdmin.getRole()=="ADMIN")
			this.operationDao.deleteAll();	
	//	else {
		//	throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		//}
		
	}

}
