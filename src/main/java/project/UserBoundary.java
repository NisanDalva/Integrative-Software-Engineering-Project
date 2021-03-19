package project;

public class UserBoundary {
	String role;
	String username;
	String avatar;
	UserId userid;
	
	
	public UserBoundary() {
		
		// TODO Auto-generated constructor stub
	}
	
	




	public UserBoundary(String role, String username, String avatar, String space,String email) {
		super();
		this.role = role;
		this.username = username;
		this.avatar = avatar;
		this.userid = new UserId(space,email);
	}


	public UserId getUserid() {
		return userid;
	}
	public void setUserid(UserId userid) {
		this.userid = userid;
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
