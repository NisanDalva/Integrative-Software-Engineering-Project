package twins.data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import twins.UserId;
@Entity
@Table(name="USERS")
public class UserEntity {
	//private String space;
	private String email;//email__space
	private String role;
	private String username;
	private String avatar;


	public UserEntity() {
	}


	public UserEntity(UserId userid, String role, String username, String avatar) {
		super();
		
		this.email=userid.getEmail();
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}
	@Id
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
