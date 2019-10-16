package dev.mason.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import dev.mason.entities.User;
import dev.mason.utils.ConnectionUtil;

public class UserDAOjdbc implements UserDAO{

	private static Connection conn = ConnectionUtil.getConn();
	private static String uTable = "ReimbursementAppDB.user";
	
	private static UserDAO udao;
	private UserDAOjdbc() {	};
	public static UserDAO getDAO() {
		if(udao==null) {
			udao = new UserDAOjdbc();
		}
		return udao;
	}
	
	
	@Override
	public User createUser(User user) {
		// project doesn't need to create users
		return null;
	}

	@Override
	public User getUserById(int u_id) {
		try{
			String sql = "SELECT * FROM "+uTable+" WHERE u_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u_id);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs!=null&&rs.next()) {
				User user = new User(
								rs.getInt("u_id"), 
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getInt("manager")
							);							
				return user;
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public User getUserByUsername(String username) {
		try{
			String sql = "SELECT * FROM "+uTable+" WHERE username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs!=null&&rs.next()) {
				User user = new User(
								rs.getInt("u_id"), 
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getInt("manager")
							);							
				return user;
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Set<User> getAllUsers() {
		try{
			String sql = "SELECT * FROM "+uTable;
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			Set<User> users = new HashSet<User>();
			
			if(rs!=null&&rs.next()) {
				User user = new User(
								rs.getInt("u_id"), 
								rs.getString("username"), 
								rs.getString("password"), 
								rs.getInt("manager")
							);							
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateUser(User user) {
		// project doesn't have to update users
		return false;
	}

	@Override
	public boolean deleteUser(User user) {
		// project does not need to delete users
		return false;
	}

}
