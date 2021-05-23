package twins.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface OperationDao extends PagingAndSortingRepository<OperationEntity, String> {

}
