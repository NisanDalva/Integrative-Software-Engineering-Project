package twins.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ITEMS")
public class ItemEntity {
	//itemId
	private String itemSpace;
	private String id;
	
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	//createdBy
	private String userSpace;
	private String email;
	//Location
	private Double lat;
	private Double lng;
	
	private String itemAttributes;
	
	
	public ItemEntity() {
		this.active = true;
		this.itemAttributes = null;
		this.createdTimestamp = new Date();
		this.lat = null;
		this.lng = null;
	}
	
	public ItemEntity(String itemSpace, String id, String type, String name, Boolean active, Date createdTimestamp,
			String userSpace, String email, Double lat, Double lng, String itemAttributes) {
		this();
		this.itemSpace = itemSpace;
		this.id = id;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.userSpace = userSpace;
		this.email = email;
		this.lat = lat;
		this.lng = lng;
		this.itemAttributes = itemAttributes;
	}

	public String getItemSpace() {
		return itemSpace;
	}

	public void setItemSpace(String itemSpace) {
		this.itemSpace = itemSpace;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MESSAGE_CREATION")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(String userSpace) {
		this.userSpace = userSpace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Lob
	public String getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
	
}