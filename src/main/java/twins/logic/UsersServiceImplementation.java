package twins.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twins.UserBoundary;
import twins.data.UserDao;

@Service
public class UsersServiceImplementation implements UsersService {
	private UserDao userDao;
	
	@Autowired
	public UsersServiceImplementation(UserDao userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary login(String userSpace, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
