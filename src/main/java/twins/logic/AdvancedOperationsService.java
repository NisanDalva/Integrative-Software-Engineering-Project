package twins.logic;

import java.util.List;

import twins.boundaries.OperationBoundary;

public interface AdvancedOperationsService extends OperationsService{
	public List<OperationBoundary> getAllOperations(String adminSpace,String adminEmail, int size, int page);
	public OperationBoundary sendAndForget(OperationBoundary input);
}
