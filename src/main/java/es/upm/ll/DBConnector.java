package es.upm.ll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {
	
	private Connection conn;
	/*
	 String server = "localhost";
			String user = "root";
			String pwd = "";
			String dbName = "livinglabdefinitions";
	 */
	

	public DBConnector(String server, String dbName, String user, String password)
			throws SQLException {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String url = "jdbc:mysql://" + server + ":3306/" + dbName;
		conn = DriverManager.getConnection(url, user, password);
	}
	
	public static void test() throws SQLException{
		String server = "localhost";
		String user = "root";
		String pwd = "";
		String dbName = "livinglabdefinitions";
         
		System.out.println("Try to connect with the database.....");
		DBConnector dataBase = new DBConnector(server, dbName, user, pwd);
	}


}
