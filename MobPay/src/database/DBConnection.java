package database;
import java.sql.*;

public class DBConnection {
 
	public String USERNAME;
	public String PASSWORD;  
	public static Connection con;
	public DBConnection()
	{
	
		USERNAME = "root";
		PASSWORD = "test";
		try 
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql:///mobilepay",USERNAME, PASSWORD);

	      if(!con.isClosed())
	        System.out.println("Successfully connected to " +
	          "MySQL server using TCP/IP...");

	    } catch(Exception e) {
	      System.err.println("Exception: " + e.getMessage());
	    } 
	}
	
	public static Connection getConnection()
	{
		return con;
	}
	
}
