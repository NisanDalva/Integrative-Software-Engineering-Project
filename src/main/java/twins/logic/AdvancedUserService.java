package twins.logic;

import java.util.List;

import twins.boundaries.UserBoundary;

public interface AdvancedUserService extends UsersService {
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int size, int page);
}
