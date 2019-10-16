package dev.mason.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {

	private static Connection conn = null;
	
	public static Connection getConn() {
		if(conn==null)
			initConn();
		return conn;
	}
	
	private static void initConn() {
		if(conn==null) {
			try(FileInputStream in = new FileInputStream("src/main/resources/connection.properties")) {
				Properties props = new Properties();
				props.load(in);
				Class.forName("org.mariadb.jdbc.Driver");
				conn = DriverManager.getConnection(props.getProperty("conn"));				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
