package twins.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String> {
	// pattern %xyz% return all items that contains this pattern
	public List<ItemEntity> findAllByActiveAndItemAttributesLike(@Param("active") boolean active,
			@Param("pattern") String pattern, Pageable pageable);
	
	public void deleteAllByTypeAndId(@Param("type") String type,
			 Pageable pageable);
	
}
