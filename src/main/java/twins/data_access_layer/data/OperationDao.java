package twins.data_access_layer.data;

import org.springframework.data.repository.CrudRepository;

public interface OperationDao extends CrudRepository<OperationEntity, String> {

}
