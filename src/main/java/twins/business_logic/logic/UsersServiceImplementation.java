package twins.business_logic.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import twins.UserId;
import twins.business_logic.boundaries.UserBoundary;
import twins.data_access_layer.data.UserDao;
import twins.data_access_layer.data.UserEntity;
import twins.data_access_layer.data.UserRole;

@Service
public class UsersServiceImplementation implements UsersService {
	private UserDao userDao;
	
	@Autowired
	public UsersServiceImplementation(UserDao userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		UserEntity entity = this.convertFromBoundary(user);
		//entity.setMessageTimestamp(new Date());
		//entity.setId("" + this.atomicLong.getAndIncrement());
		//entity.setHelper(value);
		
		
		// store entity to database using INSERT query
		entity = this.userDao.save(entity);
		return this.convertToBoundary(entity);
	}

	private UserBoundary convertToBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getRole());
		UserId userid=new UserId(entity.getSpace(),entity.getEmail());
		boundary.setUserid(userid);
		return boundary;
	}

	private UserEntity convertFromBoundary(UserBoundary user) {
		UserEntity entity = new UserEntity();	
		entity.setSpace(user.getUserid().getSpace());
		
		if (UserRole.valueOf(user.getRole())!=null ) {/// check later what valueOf returns!!!!
			entity.setRole(user.getRole());
		}
		if(isValidEmailAddress(user.getUserid().getEmail()))
			entity.setEmail(user.getUserid().getEmail());
		if(user.getUsername()!=null)
			entity.setUsername(user.getUsername());
		if(user.getAvatar()!=null&&user.getAvatar()!="")
			entity.setAvatar(user.getAvatar());
			
			
		return entity;	
	}
	
	private boolean isValidEmailAddress(String email) {
	    String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
	    java.util.regex.Matcher m = p.matcher(email);
	    return m.matches();
	}
	

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSpace, String userEmail) {
		Optional<UserEntity> op = this.userDao.findById(userEmail); //check, we need two id!!!!
		if (op.isPresent()) {
			UserEntity existing = op.get();
			UserBoundary user= convertToBoundary(existing);
			return user;}
		else {
			throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		}
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		Optional<UserEntity> op = this.userDao
				.findById(userEmail); //check, we need two id!!!!
			
			if (op.isPresent()) {
				UserEntity existing = op.get();
				
				UserEntity updatedEntity = this.convertFromBoundary(update);
				
				updatedEntity.setSpace(existing.getSpace());
		
				updatedEntity.setEmail(existing.getEmail());
				
				this.userDao.save(updatedEntity);
				UserBoundary user= convertToBoundary(updatedEntity);
				return user;}
			else {
				throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
			}
			
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) { //check what to do with the parameters !!!!!
		UserBoundary checkAdmin=login(adminSpace,adminEmail);
		if(checkAdmin.getRole()=="ADMIN") {
			Iterable<UserEntity> allUsersEntities = this.userDao.findAll();
			List<UserBoundary> usersBoundaryList = new ArrayList<>();
			for (UserEntity entity : allUsersEntities) {
				UserBoundary boundary = convertToBoundary(entity);
				usersBoundaryList.add(boundary);
			}
			return usersBoundaryList;
		}
		else {
			throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		}
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllUsers(String adminSpace, String adminEmail) {//check what to do with the parameters !!!!!
		UserBoundary checkAdmin=login(adminSpace,adminEmail);
		if(checkAdmin.getRole()=="ADMIN")
			this.userDao.deleteAll();	
		else {
			throw new RuntimeException(); // TODO: return status = 404 instead of status = 500 
		}
	}

}
