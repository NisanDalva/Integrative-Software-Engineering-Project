package twins.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import twins.CreatedBy;
import twins.ItemId;
import twins.Location;

public class ItemBoundary {
	
	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private CreatedBy createdBy;
	private Location location;
	private Map<String, Object> itemAttributes;
	
	
	public ItemBoundary() {
		this.active = true;
		this.itemAttributes = new HashMap<>();
		this.createdTimestamp = new Date();
		this.location = null;
	}


	public ItemBoundary(ItemId itemId, String type, String name, CreatedBy createdBy) {
		this();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
		this.createdBy = createdBy;
	}


	public ItemId getItemId() {
		return itemId;
	}


	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}


	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public Map<String, Object> getItemAttributes() {
		return itemAttributes;
	}


	public void setItemAttributes(Map<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}


	public CreatedBy getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}


	@Override
	public String toString() {
		return "ProductBoundary [itemId=" + itemId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", itemAttributes=" + itemAttributes + "]";
	}

}
