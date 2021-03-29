package twins.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import twins.CreatedBy;
import twins.ItemId;
import twins.Location;
@Entity
@Table(name="ITEMS")
public class ItemEntity {
	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String, Object> itemAttributes;
	
	
	public ItemEntity() {
		this.active = true;
		this.itemAttributes = new HashMap<>();
		this.createdTimestamp = new Date();
		this.location = null;
	}


	public ItemEntity(ItemId itemId, String type, String name, CreatedBy createdBy) {
		this();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
		this.createdBy = createdBy;
	}
	
	@Transient
	public ItemId getItemId() {
		return itemId;
	}
	
	@Transient
	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public Boolean getActive() {
		return active;
	}
	
	@Transient
	public void setActive(Boolean active) {
		this.active = active;
	}

	@Transient
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	@Transient
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Transient
	public Location getLocation() {
		return location;
	}

	@Transient
	public void setLocation(Location location) {
		this.location = location;
	}

	@Transient
	public Map<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	@Transient
	public void setItemAttributes(Map<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}

	@Transient
	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	@Transient
	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}
	
}
