package twins.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.OperationBoundary;
import twins.data.OperationDao;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllOperations(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
