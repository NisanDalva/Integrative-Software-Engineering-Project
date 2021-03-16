package DanielHays.Team;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductBoundary {
	
	// will create an abstract class???
	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	// created by and userId
	private Location location;
	private Map<String, Object> itemAttributes;
	
	
	public ProductBoundary() {
		this.active = true;
		this.itemAttributes = new HashMap<>();
		this.createdTimestamp = new Date();
		this.location = null;
	}


	public ProductBoundary(ItemId itemId, String type, String name /* userId */) {
		this();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
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


	@Override
	public String toString() {
		return "ProductBoundary [itemId=" + itemId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", location=" + location + ", itemAttributes="
				+ itemAttributes + "]";
	}
	
	
}
