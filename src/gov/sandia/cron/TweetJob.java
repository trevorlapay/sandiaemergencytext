package gov.sandia.cron;
 
import gov.sandia.mail.SendMail;
import gov.sandia.model.Emergency;
import gov.sandia.model.User;
import gov.sandia.model.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.jobs.ee.mail.SendMailJob;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.sun.mail.smtp.SMTPTransport;
 
public class TweetJob implements Job
{
	public void execute(JobExecutionContext context)
	throws JobExecutionException {
		// The factory instance is re-useable and thread safe.
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("80xgCoqMRdl5RGF4biCKJVSQO")
		  .setOAuthConsumerSecret("Z0z6uWQVxm6TvaHUcDobvPfZuhSHeH2JLdSHp2neesKt0Gmi8O")
		  .setOAuthAccessToken("3122075449-TKdZIEm978qpCkE2sk9mXAvVMqKFCoIG8C755Hw")
		  .setOAuthAccessTokenSecret("rGrRVxCt4fG2veMHCmP3pl7GtQ5TFvfgCJRWH5wHL7CdH");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		String alert = "";
        try { 
        	
			for (Status status : twitter.getUserTimeline("sandiaemergency")) {
			    if (status.getText().toUpperCase().contains("EMERGENCY") 
			    		&& Emergency.isNewEmergency(status)){
			    	//We hit on a new emergency! Therefore, we can stop looking and send texts. Even if there are more emergencies, we will 
			    	//hit on them again later if they are not already in the DB - system assumption that emergencies > 1 per minute 
			    	//are rare and need to be sent through the pipeline LIFO (possible clarifications of EXTREME situations may be more important than
			    	//initial tweet
						ArrayList<gov.sandia.model.User> users = Users.getAllUsers();
						for (User user : users){
							SendMail.sendMail(status.getText(), user.getNumber() + "@" + user.getProvider());
						    Emergency.insertLastEmergency(status.getText(), status.getId() + "");
						}
						
			    	    break;
			    } else {
			    	System.out.println("Everything's Ok Alarm");
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		}
//		 // Read RDS Connection Information from the Environment
//		  Properties prop = new Properties();
//		  String propFileName = "sets.properties";
//
//		  InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
//
//		  if (inputStream != null) {
//			try {
//				prop.load(inputStream);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//			e.printStackTrace();
//			}
//		  } else { 
//			try {
//				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		  }
//		  final String password = prop.getProperty("empassword");
//		  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
//
//	        // Get a Properties object
//	        Properties props = System.getProperties();
//	        props.setProperty("mail.smtps.host", "smtp.gmail.com");
//	        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
//	        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//	        props.setProperty("mail.smtp.port", "465");
//	        props.setProperty("mail.smtp.socketFactory.port", "465");
//	        props.setProperty("mail.smtps.auth", "true");
//
//	        /*
//	        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
//	        to true (the default), causes the transport to wait for the response to the QUIT command.
//
//	        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
//	                http://forum.java.sun.com/thread.jspa?threadID=5205249
//	                smtpsend.java - demo program from javamail
//	        */
//	        props.put("mail.smtps.quitwait", "false");
//
//	        Session session = Session.getInstance(props, null);
//	        try{
//	        // -- Create a new message --
//	        final MimeMessage msg = new MimeMessage(session);
//            
//	        // -- Set the FROM and TO fields --
//	        msg.setFrom(new InternetAddress("sandiaemergencytext" + "@gmail.com"));
//	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sandiaemergemcytext@gmail.com", false));
//
//
//	        msg.setSubject("Hi");
//	        msg.setText("woah", "utf-8");
//	        msg.setSentDate(new Date());
//
//	        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
//
//	        t.connect("smtp.gmail.com", "sandiaemergencytext@gmail.com", password);
//	        t.sendMessage(msg, msg.getAllRecipients());      
//	        t.close();
//            } catch (Exception e){
//            	System.out.println(e.getMessage());
//            }
//			} 
		} 
	}
}
	
 

 
