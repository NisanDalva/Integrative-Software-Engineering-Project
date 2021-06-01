package twins.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ItemDao extends PagingAndSortingRepository<ItemEntity, String> {
	// pattern %xyz% return all items that contains xyz
	public List<ItemEntity> findAllByActiveAndItemAttributesLike(@Param("active") boolean active,
			@Param("pattern") String pattern, Pageable pageable);
	
	public List<ItemEntity> deleteAllByTypeAndId(@Param("type") String type,
			@Param("id") String id, Pageable pageable);
	
	public List<ItemEntity> findAllByActiveAndNameLike(@Param("active") boolean active, @Param("name") String name, Pageable pageable);
	
	
	public List<ItemEntity> findAllByTypeAndActive(@Param("type") String type,
			@Param("active") boolean active, Pageable pageable);
}
