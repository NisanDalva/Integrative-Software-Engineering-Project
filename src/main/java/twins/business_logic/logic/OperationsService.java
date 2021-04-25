package twins.business_logic.logic;

import java.util.List;

import twins.business_logic.boundaries.OperationBoundary;

public interface OperationsService {
	public Object invokeOperation(OperationBoundary operation);
	public OperationBoundary invokeAsynchronousOperation(OperationBoundary operation);
	public List<OperationBoundary> getAllOperations(String adminSpace,String adminEmail);
	public void deleteAllOperations(String adminSpace,String adminEmail);
}