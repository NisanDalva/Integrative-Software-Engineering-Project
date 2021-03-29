package twins.data;

import org.springframework.data.repository.CrudRepository;

public interface ItemDao extends CrudRepository<ItemEntity, String> {

}
