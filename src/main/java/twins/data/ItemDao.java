package twins.data;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String> {

}
