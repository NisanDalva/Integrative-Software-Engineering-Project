package twins.data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import twins.UserId;
@Entity
@Table(name="USERS")
public class UserEntity {
	private UserId userid;
	private String role;
	private String username;
	private String avatar;


	public UserEntity() {
	}


	public UserEntity(UserId userid, String role, String username, String avatar) {
		super();
		this.userid = userid;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	@Transient
	public UserId getUserid() {
		return userid;
	}
	
	@Transient
	public void setUserid(UserId userid) {
		this.userid = userid;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	@Id
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
