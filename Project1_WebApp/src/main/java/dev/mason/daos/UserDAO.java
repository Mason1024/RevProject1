package dev.mason.daos;

import java.util.Set;

import dev.mason.entities.User;

public interface UserDAO {

	User createUser(User user);
	
	User getUserById(int u_id);
	User getUserByUsername(String username);
	Set<User> getAllUsers();
	
	boolean updateUser(User user);
	
	boolean deleteUser(User user);
	
}
