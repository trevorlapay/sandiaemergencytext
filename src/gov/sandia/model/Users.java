package gov.sandia.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import sandia.gov.util.SETSDbConnection;



public class Users {

	 public static ArrayList<User> getAllUsers() throws Exception{
		 ArrayList<User> users= new ArrayList();
		 Connection conn = SETSDbConnection.getConnection();
		 Statement readStatement = conn.createStatement();
		 ResultSet resultSet = readStatement.executeQuery("SELECT * FROM USERS;");
		 while (resultSet.next()){
			 users.add(new User(resultSet.getString("DIG"), resultSet.getString("PROVIDER_NAME")));
		 }
		 SETSDbConnection.closeConnection(conn);
		 return users;	 
		 
	 }


     
}
