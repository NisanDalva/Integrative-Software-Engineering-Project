package twins.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.UserId;
import twins.Exceptions.AccessDeniedException;
import twins.Exceptions.InvalidUserDetailsException;
import twins.Exceptions.UserNotFoundException;
import twins.boundaries.UserBoundary;
import twins.data.UserDao;
import twins.data.UserEntity;
import twins.data.UserRole;

@Service
public class UsersServiceImplementation implements AdvancedUserService {
	private UserDao userDao;
	private String springApplictionName;
	
	@Autowired
	public UsersServiceImplementation(UserDao userDao) {
		super();
		this.userDao = userDao;
	}
	
	@Value("${spring.application.name:DanielHay_space}")
	public void setValue(String springApplictionName) {
		this.springApplictionName = springApplictionName;
	}
	
	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary user) {
		UserEntity entity = this.convertFromBoundary(user);
		
		entity = this.userDao.save(entity);
		return this.convertToBoundary(entity);
	}

	private UserBoundary convertToBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		boundary.setRole(entity.getRole());
		
		String tmp[] = entity.getEmail().split("__");
		UserId userid=new UserId(tmp[1], tmp[0]);
		boundary.setUserid(userid);
		return boundary;
	}

	private UserEntity convertFromBoundary(UserBoundary user) {
		UserEntity entity = new UserEntity();	

		
		if (UserRole.valueOf(user.getRole())==null ) /// check later what valueOf returns!!!!
			throw new InvalidUserDetailsException(user.getRole() + " is invalid role");
		
//		System.err.println(user.getUserid().getEmail());
//		System.err.println(user.getUserid());
		
		if(!isValidEmailAddress(user.getUserid().getEmail()))
			throw new InvalidUserDetailsException(user.getUserid().getEmail() + " is invalid email");
		if(user.getUsername()==null)
			throw new InvalidUserDetailsException("User name can't be null!");
		if(user.getAvatar() == null&&user.getAvatar() == "")
			throw new InvalidUserDetailsException("Avatar can't be null or empty");
			
		entity.setRole(user.getRole());
		entity.setEmail(user.getUserid().getEmail()+"__"+this.springApplictionName);
		entity.setUsername(user.getUsername());
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
		Optional<UserEntity> op = this.userDao.findById(userEmail+"__"+userSpace); 
		if (op.isPresent()) {
			UserEntity existing = op.get();
			UserBoundary user= convertToBoundary(existing);
			return user;}
		else {
			throw new RuntimeException("The user email " + userEmail + " with space " + userSpace + " not found in the database"); 
		}
	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		String email=userEmail+"__"+userSpace;
		Optional<UserEntity> op = this.userDao
				.findById(email); 
			
			if (op.isPresent()) {
				UserEntity existing = op.get();
				
				UserEntity updatedEntity = this.convertFromBoundary(update);
					
				updatedEntity.setEmail(existing.getEmail());
				
				this.userDao.save(updatedEntity);
				UserBoundary user= convertToBoundary(updatedEntity);
				return user;
			} else {
				throw new UserNotFoundException("The user email " + userEmail + " with space " + userSpace + " not found in the database"); 
			}
			
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail) {
//			Iterable<UserEntity> allUsersEntities = this.userDao.findAll();
//			List<UserBoundary> usersBoundaryList = new ArrayList<>();
//			for (UserEntity entity : allUsersEntities) {
//				UserBoundary boundary = convertToBoundary(entity);
//				usersBoundaryList.add(boundary);
//			}
//			return usersBoundaryList;
		throw new RuntimeException("deprecated operation - use the new API getAllUsers(userSpace, userEmail, size, page)");
		}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		UserBoundary user= this.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN")) 
			this.userDao.deleteAll();
		else
			throw new AccessDeniedException(user.getRole() + " can't delete all users! (Only ADMIN)");
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int size, int page) {
		UserBoundary user= this.login(adminSpace, adminEmail);
		if(user.getRole().equals("ADMIN")) {
		Page<UserEntity> pageOfEntity = this.userDao.findAll(PageRequest.of(page, size, Direction.ASC, "username", "email"));
		List<UserEntity> allUsersEntities = pageOfEntity.getContent();
		List<UserBoundary> usersBoundaryList = new ArrayList<>();
		for (UserEntity entity : allUsersEntities) {
			UserBoundary boundary = convertToBoundary(entity);
			usersBoundaryList.add(boundary);
		}
		return usersBoundaryList;
		}
		else
			throw new AccessDeniedException(user.getRole() + " can't get all users! (Only ADMIN)");
	}

}
