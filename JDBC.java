import java.sql.*;

public class JDBC {
	private final String userName = "root"; //declare and assign the MySQL account name
	private final String password = "root"; //declare and assign the MySQL account password
	private final String serverName = "localhost"; //declare and assign the server name
	private final int portNumber = 3306; // declare and assign the MySQL server port
	private final String dbName = "jdbc"; //declare and assign the MySQL database name
	private final String tableName = "java_stock"; //declare and assign the table name
	
	private Connection con; // Create Connection object to store the connection to the database URL
	private ResultSet rs; // Create ResultSet object to store a table of data representing database result set
	private Statement stmt; // Create a statement object to represent the interface of the SQL statement
	
	// create constructor
	public JDBC()
	{
		Connect(); // connect the SQL server
	}
	
	
	// method to check the database of the SQL server exist or not
	public void CheckDatabase() throws SQLException, ClassNotFoundException
	{
		// create variable to store the database name
		String databaseName;

		// establish connection with the SQL server with the URL
		con = DriverManager.getConnection("jdbc:mysql://"+ this.serverName + 
							":" + this.portNumber, this.userName, this.password);
		
		// get the DatabaseMetaData object with respect to SQL server connection
		// then, get the database catalog from the DatabaseMetaData object return into ResultSet object
		rs = con.getMetaData().getCatalogs();
		
		// as a loop to iterate the contents of the result set 
		while (rs.next())
		{
			databaseName = rs.getString(1); // assign the database name into the variable
			
			// break the loop when the databaseName is equal to target database name
			if (databaseName.equals(this.dbName))
			{
				System.out.println(this.dbName + " database exists"); // print the database exist message
				break;
			}
		}
		// use if statement to judge whether result set is end of the iterator or not
		if (!rs.next())
		{
			System.out.println( this.dbName + " not exists."); // print the database not exist message
			System.out.println("creating " + this.dbName + " database..."); // print create database message
			
			stmt = con.createStatement(); // create a Statement object for sending SQL statement to database
			stmt.execute("CREATE DATABASE " + this.dbName); // execute the SQL command which create the target database in SQL server
		}
	}
	
	// method to check the table of the SQL databse exist or not
	public void CheckTable() throws SQLException, ClassNotFoundException
	{
		// get the DatabaseMetaData object with respect to SQL server connection
		// then, retrieving the table in the database and return into result set object
		rs = con.getMetaData().getTables(null, null, this.tableName, null);
		
		// as a loop to iterate the contents of the result set 
		while (rs.next())
		{
			// break the loop and return when the result set table name is same as target
			if (this.tableName.equals(rs.getString("TABLE_NAME")))
			{
				System.out.println(this.tableName + " exists"); // print the table exist message
				return;
			}
		}
		
		// use if statement to judge whether result set is end of the iterator or not
		if (!rs.next()) 
		{
			System.out.println(this.tableName+ " table not exists in " + this.dbName); // print the table not exist message
			System.out.println("creating " + this.tableName + " database..."); // print create the table message
			
			stmt = con.createStatement(); // create a Statement object for sending SQL statement to database
			
			// execute the SQL command which create the table in SQL database
			stmt.execute("CREATE TABLE " + this.tableName + " ( " +
						 "ID INTEGER NOT NULL, " +
						 "NAME varchar(45) NOT NULL, " +
						 "AMOUNT varchar(45) NOT NULL, " +
						 "PIC varchar(45) NOT NULL, " +
						 "PRIMARY KEY (ID))");
		}
	}
	
	// Connect the SQL server database and table
	public void Connect()
	{
		try {
			// register and dynamically load the jdbc driver class
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// check the target database exist in SQL server or not
			CheckDatabase();
			
			// establish connection with the SQL target database with the URL
			con = DriverManager.getConnection("jdbc:mysql://"+ this.serverName + 
								":" + this.portNumber + "/" + this.dbName, this.userName, this.password);
			
			// check the target table exist in SQL database or not
			CheckTable();
			System.out.println("Database connect succefully!"); // print connect success message

		}
		// catch the error when register the driver class is not found in the try block
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace(); // print the error detail when exception occur
		}
		// catch the SQL error in the try block
		catch (SQLException ex)
		{
			ex.printStackTrace(); // print the error detail when exception occur
		}
	}
	
	// method to return Connection object that connected to the database URL
	public Connection getCon()
	{
		return this.con;
	}
	
	// method to return target table name
	public String getTable()
	{
		return this.tableName;
	}
	
	// method to close the connection to SQL server database
	public void disconnect()
	{
		try
		{
			con.close();
			System.out.println("Database disconnect succefully!"); // print the disconnect success message
		}
		// catch the SQL error in the try block
		catch (SQLException ex)
		{
			ex.printStackTrace(); // print the error detail when exception occur
		}
	}
}
