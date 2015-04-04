package sandia.gov.util;

import gov.sandia.model.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SETSDbConnection {
	  public static Connection getConnection() throws IOException{
			 Properties prop = new Properties();
			  String propFileName = "sets.properties";

			  InputStream inputStream = Users.class.getClassLoader().getResourceAsStream(propFileName);

			  if (inputStream != null) {
				prop.load(inputStream);
			  } else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			  }
			 
			  String dbName = prop.getProperty("dbname");
			  String userName = prop.getProperty("dbuser");
			  String password = prop.getProperty("dbpassword");
			  String hostname = prop.getProperty("dbhostname");
			  String port = prop.getProperty("dbport");
			  String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
					    port + "/" + dbName + "?user=" + userName + "&password=" + password;
					  
			  // Load the JDBC Driver
			  try { 
			    System.out.println("Loading driver...");
			    Class.forName("com.mysql.jdbc.Driver");
			    System.out.println("Driver loaded!");
			  } catch (ClassNotFoundException e) {
			    throw new RuntimeException("Cannot find the driver in the classpath!", e);
			  }

			  Connection conn = null;
			  Statement setupStatement = null;
			  Statement readStatement = null;
			  ResultSet resultSet = null;
			  String results = "";
			  int numresults = 0;
			  String statement = null;


			  try {
			    conn = DriverManager.getConnection(jdbcUrl);
			    return conn;
			    //readStatement = conn.createStatement();

			    //resultSet = readStatement.executeQuery("SELECT PROVIDER_NAME FROM Providers;");

			    //resultSet.first();
//			    results = resultSet.getString("PROVIDER_NAME");
//			    resultSet.next();
//			    results += ", " + resultSet.getString("PROVIDER_NAME");
//			    
//			    resultSet.close();
//			    readStatement.close();
//			    conn.close();
		        //return resultSet;
			  } catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			  } finally {
			      //System.out.println("Closing the connection.");
			      //if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
			  }
			  return null;
		}
	    public static void closeConnection(Connection conn){
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
}
