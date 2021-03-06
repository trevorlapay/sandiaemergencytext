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
<script src="jquery-2.1.3.min.js"></script>
<script src="jquery.mask.js"></script>
<script type="text/javascript">
$( document ).ready(function() {
	 $('#inputTel').mask('(000) 000-0000');
	});


</script>
</head>
 <body>
    <form name="mainForm" action="PhoneServlet">
    <div class="container">
      <div class="header clearfix">
        <nav>
          <ul class="nav nav-pills pull-right">
            <li role="presentation" class="active"><a href="#">Home</a></li>
            <li role="presentation"><a href="about.jsp">About</a></li>
            <li role="presentation"><a href="contact.jsp">Contact</a></li>
          </ul>
        </nav>
        <h3 class="text-muted">CS 591</h3>
      </div>

      <div class="jumbotron">
        <h1>Sandia Emergency Texting System</h1>
        <p class="lead">Emergency at Sandia? We'll text you. </p>
        <% Boolean success = (Boolean) request.getAttribute("success");
        if (success != null &&  success) {%>
        <span class="label label-success">Thank you for submitting your request. You will be notified via text when your request has been processed.</span>
        <%} %>
        <p>Select your carrier:
        <select class="form-control" name="carrier">
		    <option value="txt.att.net">AT&T</option>
		    <option value="vtext.com">Verizon</option>
		    <option value="tmomail.net">T-Mobile</option>
		    <option value="vmobl.com">Virgin Mobile</option>
		    <option value="qwestmp.com">Qwest</option>
		    <option value="messaging.nextel.com">Nextel</option>
		    <option value="email.uscc.net">U.S. Cellular</option>
		    <option value="myboostmobile.com">Boost Mobile</option>
		    <option value="mymetropcs.com">Metro PCS</option>
		</select>
        </p>

        <p>Enter a text-capable mobile number: <input type=tel name="inputTel"  size="14" id="inputTel" class="form-control" placeholder="Phone" required="" >
        <br clear="all">
        <input type="submit" name="register" class="btn btn-lg btn-success" role="button" value="Request to sign up today">&nbsp;<input type="submit" name="unenroll" class="btn btn-lg btn-danger" role="button" value="Request to unenroll this number"></a></p>
      </div>

 

      <footer class="footer">
        <p>&copy; Trevor La Pay 2015</p>
      </footer>

    </div> <!-- /container -->
    </form>

  </body>
</html>
