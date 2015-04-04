package gov.sandia.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import sandia.gov.util.SETSDbConnection;
import twitter4j.Status;

/**
 * This DB access object class stores the last texted emergency. If an emergency text appears in the cron poll
 * and occurs after the date (or does not match the text), send a text to the members in the Users table.
 * 
 * @author Administrator
 *
 */
public class Emergency {
  public Emergency(Date lastEmergencyDate, String lastEmergencyText) {
		super();
		this.lastEmergencyDate = lastEmergencyDate;
		this.lastEmergencyText = lastEmergencyText;
	}
  private java.util.Date lastEmergencyDate;  
  private String lastEmergencyText;
  private String lastEmergencyId;
  
  public static Emergency getLastEmergency() throws Exception{
		 Emergency emerg = new Emergency(null, null);
		 Connection conn = SETSDbConnection.getConnection();
		 Statement readStatement = conn.createStatement();
		 ResultSet resultSet = readStatement.executeQuery("SELECT TOP 1 FROM EMERGENCIES ORDER BY LAST_EMERGENCY_DATE DESC;");
		 while (resultSet.next()){
			 emerg.setLastEmergencyDate(resultSet.getDate("LAST_EMERGENCY_DATE"));
			 emerg.setLastEmergencyText(resultSet.getString("LAST_EMERGENCY"));	 
			 emerg.setLastEmergencyId(resultSet.getString("LAST_EMERGENCY_ID"));	 
			 }
		 SETSDbConnection.closeConnection(conn);
		 return emerg;	 
		 
	 }
  public static ArrayList<Emergency>  getLast100() throws Exception{
		 ArrayList<Emergency> emergList = new ArrayList();
		 Connection conn = SETSDbConnection.getConnection();
		 Statement readStatement = conn.createStatement();
		 ResultSet resultSet = readStatement.executeQuery("SELECT * FROM EMERGENCIES ORDER BY LAST_EMERGENCY_DATE DESC LIMIT 100;");
		 while (resultSet.next()){
			 Emergency emerg = new Emergency(null, null);
			 emerg.setLastEmergencyDate(resultSet.getDate("LAST_EMERGENCY_DATE"));
			 emerg.setLastEmergencyText(resultSet.getString("LAST_EMERGENCY"));	 
			 emerg.setLastEmergencyId(resultSet.getString("LAST_EMERGENCY_ID"));	
			 emergList.add(emerg);
			 }
		 SETSDbConnection.closeConnection(conn);
		 return emergList;	 
		 
	 }

  public static void insertLastEmergency(String text, String id) throws Exception{
		 Emergency emerg = new Emergency(null, null);
		 Connection conn = SETSDbConnection.getConnection();
		 Statement readStatement = conn.createStatement();
         if (notInDB(id))
        	 readStatement.executeUpdate("INSERT INTO EMERGENCIES (LAST_EMERGENCY, LAST_EMERGENCY_DATE, LAST_EMERGENCY_ID) VALUES (' " + text +" ', NOW(), "+id+")");
		 SETSDbConnection.closeConnection(conn);
 
	 }
  
  public java.util.Date getLastEmergencyDate() {
		return lastEmergencyDate;
	}
	public void setLastEmergencyDate(java.util.Date lastEmergencyDate) {
		this.lastEmergencyDate = lastEmergencyDate;
	}
	public String getLastEmergencyText() {
		return lastEmergencyText;
	}
	public void setLastEmergencyText(String lastEmergencyText) {
		this.lastEmergencyText = lastEmergencyText;
	}
	public static boolean isNewEmergency(Status status) throws Exception {
		if (notInDB(status.getId() + ""))return true;
		return false;
		
	}
	public static boolean notInDB(String ID) throws Exception{
		boolean newStatusId = true;
		for (Emergency emergency : getLast100()){
			if(ID.equals(emergency.getLastEmergencyId())){
				newStatusId = false;
				break;
			}
		}
		return newStatusId;
	}
	public String getLastEmergencyId() {
		return lastEmergencyId;
	}
	public void setLastEmergencyId(String lastEmergencyId) {
		this.lastEmergencyId = lastEmergencyId;
	}

}
