package gov.sandia.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;









import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class PhoneServlet
 */

public class PhoneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhoneServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String inputTel = request.getParameter("inputTel");
		String carrier = request.getParameter("carrier");

		  // Read RDS Connection Information from the Environment
		  Properties prop = new Properties();
		  String propFileName = "sets.properties";

		  InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
		  if (inputStream != null) {
			prop.load(inputStream);
		  } else { 
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		  }
		  final String password = prop.getProperty("empassword");
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465"); 
	 
			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("dariusforrester555@gmail.com", "apushapeng3");
					}
				});
			try {
	 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("sandiaemergencytext@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("sandiaemergencytext@gmail.com"));
				
				String action = "ENROLLED INTO";
				if (request.getParameter("unenroll") != null){
	                   action = "UNENROLLED FROM";
				} 
				message.setSubject("Request to be " + action);
				String text = "User " + inputTel + " has requested to be " + action + " the emergency texting system using carrier: " + carrier;
				message.setText(text);
				Transport.send(message);
			    RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			    request.setAttribute("success", new Boolean(true));
				view.forward(request, response);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			} 
		} 
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
