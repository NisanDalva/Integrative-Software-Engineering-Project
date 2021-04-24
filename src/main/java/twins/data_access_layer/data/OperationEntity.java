package twins.data_access_layer.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="OPERATIONS")
public class OperationEntity {

	private String id;
	private String operationSpace;
	private String type;
	private String itemSpace;
	private String itemId;
	private Date createdTimestamp;
	private String userSpace;
	private String email;
	private String operationAttributes;

	public OperationEntity() {
		this.createdTimestamp = new Date();
		this.operationAttributes = null;
	}

	public OperationEntity(String id, String operationSpace, String type, String itemSpace, String itemId,
			Date createdTimestamp, String userSpace, String email, String operationAttributes) {
		this();
		this.id = id;
		this.operationSpace = operationSpace;
		this.type = type;
		this.itemSpace = itemSpace;
		this.itemId = itemId;
		this.createdTimestamp = createdTimestamp;
		this.userSpace = userSpace;
		this.email = email;
		this.operationAttributes = operationAttributes;
	}
	
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperationSpace() {
		return operationSpace;
	}

	public void setOperationSpace(String operationSpace) {
		this.operationSpace = operationSpace;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemSpace() {
		return itemSpace;
	}

	public void setItemSpace(String itemSpace) {
		this.itemSpace = itemSpace;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATION_CREATION")
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
	
	@Lob
	public String getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(String operationAttributes) {
		this.operationAttributes = operationAttributes;
	}
	
}
