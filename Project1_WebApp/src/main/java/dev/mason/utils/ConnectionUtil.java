package dev.mason.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	private static Connection conn = null;
	
	public static Connection getConn() {
		if(conn==null)
			initConn();
		return conn;
	}
	
	private static void initConn() {
		if(conn==null) {
			try{
				Class.forName("org.mariadb.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mariadb://kweldb.c4zod4lc7ws5.us-east-2.rds.amazonaws.com:3306/ReimbursementAppDB?user=admin&password=password");				
			} catch (SQLException e) {
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
