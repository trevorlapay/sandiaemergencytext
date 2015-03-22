<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.amazonaws.*" %>
<%@ page import="com.amazonaws.auth.*" %>
<%@ page import="com.amazonaws.services.ec2.*" %>
<%@ page import="com.amazonaws.services.ec2.model.*" %>
<%@ page import="com.amazonaws.services.s3.*" %>
<%@ page import="com.amazonaws.services.s3.model.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.*" %>
<%@ page import="com.amazonaws.services.dynamodbv2.model.*" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileNotFoundException" %>

<%@ page import="java.sql.*" %>
<%
  // Read RDS Connection Information from the Environment
  Properties prop = new Properties();
  String propFileName = "sets.properties";

  InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

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
	    System.out.println(jdbcUrl);
    conn = DriverManager.getConnection(jdbcUrl);
    
    readStatement = conn.createStatement();

    resultSet = readStatement.executeQuery("SELECT PROVIDER_NAME FROM Providers;");

    resultSet.first();
    results = resultSet.getString("PROVIDER_NAME");
    resultSet.next();
    results += ", " + resultSet.getString("PROVIDER_NAME");
    
    resultSet.close();
    readStatement.close();
    conn.close();

  } catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
  } finally {
       System.out.println("Closing the connection.");
      if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
  }
%>
<%! // Share the client objects across threads to
    // avoid creating new clients for each web request
    private AmazonEC2         ec2;
    private AmazonS3           s3;
    private AmazonDynamoDB dynamo;
 %>

<%
    /*
     * AWS Elastic Beanstalk checks your application's health by periodically
     * sending an HTTP HEAD request to a resource in your application. By
     * default, this is the root or default resource in your application,
     * but can be configured for each environment.
     *
     * Here, we report success as long as the app server is up, but skip
     * generating the whole page since this is a HEAD request only. You
     * can employ more sophisticated health checks in your application.
     */
    if (request.getMethod().equals("HEAD")) return;
%>

<%
    if (ec2 == null) {
        AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
        ec2    = new AmazonEC2Client(credentialsProvider);
        s3     = new AmazonS3Client(credentialsProvider);
        dynamo = new AmazonDynamoDBClient(credentialsProvider);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>Emergency? Get texted.</title>
    <link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
    <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
 <body>
    <form name="mainForm" action="PhoneServlet">
    <div class="container">
      <div class="header clearfix">
        <nav>
          <ul class="nav nav-pills pull-right">
            <li role="presentation" class="active"><a href="#">Home</a></li>
            <li role="presentation"><a href="#">About</a></li>
            <li role="presentation"><a href="#">Contact</a></li>
          </ul>
        </nav>
        <h3 class="text-muted">CS 531</h3>
      </div>

      <div class="jumbotron">
        <h1>Sandia Emergency Texting System</h1>
        <p class="lead">Emergency at Sandia? We'll text you. <p>Established connection to RDS. Read first two rows: <%= results %></p></p>
        
        <p><input type=tel name="inputTel"  id="inputTel" class="form-control" placeholder="Email address" required="" autofocus=""><input type="submit" class="btn btn-lg btn-success" role="button" value="Request to sign up today"></a></p>
      </div>

 

      <footer class="footer">
        <p>&copy; Trevor La Pay 2015</p>
      </footer>

    </div> <!-- /container -->
    </form>

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>
<!--  <body>
    <div id="content" class="container">
        <div class="section grid grid5 s3">
            <h2>Amazon S3 Buckets:</h2>
            <ul>
            <% for (Bucket bucket : s3.listBuckets()) { %>
               <li> <%= bucket.getName() %> </li>
            <% } %>
            </ul>
        </div>

        <div class="section grid grid5 sdb">
            <h2>Amazon DynamoDB Tables:</h2>
            <ul>
            <% for (String tableName : dynamo.listTables().getTableNames()) { %>
               <li> <%= tableName %></li>
            <% } %>
            </ul>
        </div>

        <div class="section grid grid5 gridlast ec2">
            <h2>Amazon EC2 Instances:</h2>
            <ul>
            <% for (Reservation reservation : ec2.describeInstances().getReservations()) { %>
                <% for (Instance instance : reservation.getInstances()) { %>
                   <li> <%= instance.getInstanceId() %></li>
                <% } %>
            <% } %>
            </ul>
        </div>
    </div>
</body>
</html>-->