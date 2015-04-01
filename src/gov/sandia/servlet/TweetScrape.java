package gov.sandia.servlet;

import gov.sandia.mail.SendMail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;












import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Servlet implementation class TweetScrape
 */
public class TweetScrape extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TweetScrape() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			    if (status.getText().toUpperCase().contains("EMERGENCY")){
			    	System.out.println(status.getText());
			    	SendMail.sendMail(status.getText());
			    	alert += status.getText() +  "  ";
			    	
			    } else {
			    	System.out.println("Everything's Ok Alarm");
			    }
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (alert == null || alert.isEmpty()) alert="Everything's Ok Alarm";
        RequestDispatcher view = request.getRequestDispatcher("tweetScrapeResult.jsp");
	    request.setAttribute("tweet", alert);
	    System.out.println(alert);
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
