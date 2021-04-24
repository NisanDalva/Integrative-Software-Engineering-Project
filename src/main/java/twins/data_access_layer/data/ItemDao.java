package twins.data_access_layer.data;

import org.springframework.data.repository.CrudRepository;

public interface ItemDao extends CrudRepository<ItemEntity, String> {

}
