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