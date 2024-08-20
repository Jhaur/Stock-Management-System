import java.sql.*;
import java.util.*;

import javax.swing.table.DefaultTableModel;


//create a ADTs class to store Stock object
public class StockList
{	
	private int ID; // to store latest stock ID
	private Connection con; // to store the connection to the database URL
	private String tableName; // to store target table name in database
	private PreparedStatement pst; // Create Prepared Object to store precompiled SQL statement
	private ResultSet rs; // Create ResultSet object to store a table of data representing database result set
	
	// create a LinkedList collection variable
	private LinkedList<Stock> list;
	
	// create constructor
	public StockList(Connection con, String table)
	{
		list = new LinkedList<>(); // create new LinkedList
		ID = 0; // set the current latest stock ID as 0
		tableName = table; // assign the parameter value to table name
		this.con = con; // assign the Connection object from parameter to "con" attribute
		this.loadData(); // load data from the database
	}
	
	// method to add Stock instance
	public void add (String name, String amount, String pic)
	{
		ID += 1; // increase id number when add the new instance
		// create a stock instance and add into the list
		this.list.add(new Stock(ID, name, amount, pic)); 
	}
	
	// return list size
	public int size()
	{
		return list.size();
	}
	
	// method to display custom content into the table
	public DefaultTableModel table(DefaultTableModel tableModel)
	{
		// clear the table content
		tableModel.setRowCount(0);
		// add the table content into the table
		for (int i = 0; i < list.size(); i++)
		{
			tableModel.addRow(list.get(i).data());
		}
		return tableModel; // return the tableModel to UI module
	}
	
	// method to search the Stock Instance by ID number
	public int search(String id)
	{
		int ID = Integer.parseInt(id); // convert string to integer
		// retrieve the id number
		for (int i=0; i< list.size(); i++)
		{
			if (list.get(i).getID() == ID)
				return i; // return the index target Object in linked list
		}
		return -1; // return -1 as ID not found message
	}
	
	// retrieve target ID Stock object and return attribute value in array format
	public Object[] getInfo(String id)
	{
		Object[] data = {"", "", "", ""}; // create an array list
		int index = search(id); // search the object base on id number
		
		// Get the Stock Object data when id number is exist
		if (index != -1) 
		{
			data = list.get(index).data();
		}
		return data; // return array data
	}
	
	// update Stock Object information like name, amount and person in charge
	public void update(String id, String name, String amount, String pic)
	{
		int index = search(id); // get the target Stock index number in list
		list.get(index).setName(name);
		list.get(index).setAmount(amount);
		list.get(index).setPIC(pic);
	}
	
	// remove the Stock Object base on ID number
	public void remove(String id)
	{
		int index = search(id); // get index from search method
		list.remove(index); // remove the target Stock object
	}
	
	// method to set the string represent StockList object
	public String toString()
	{
		String content ="";
		for (int i=0; i< list.size(); i++)
			content += (list.get(i).toString() + '\n');
		return content;
	}
	
	// method to load all data from the sql server database into the list
	public void loadData()
	{
		try {
            String sql = "select * from " + tableName; // SQL command to select all data from the table
            // invoke Connection Object to sent the sql command to database to compile and return it
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(); // execute SQL query in the object and return the result set
            
            // declare variable to store the Stock attribute value
            int id = 0;
            String name, amount, pic;
 
            // as a loop to iterate the contents of the result set 
            while (rs.next()) 
            {
            	// store the result set data into the variable
                id = rs.getInt("ID");
                name = rs.getString("Name");
                amount = rs.getString("Amount");
                pic = rs.getString("PIC");
                list.add(new Stock(id, name, amount, pic)); // create a Stock object and add into the list
            }
            this.ID = id; // assign the latest id number
        }
		catch(SQLException ex)
		{
			ex.printStackTrace(); // print the error detail when exception occur
		}
	}
	
	// method to store all data from list into the sql server database
	public void saveData()
	{
		try
		{
			pst.execute("Truncate table " + tableName); /// execute SQL command to clear table data in database
			// declare variable to store the Stock attribute value
			int id;
			String name, amount, pic;
			
			// insert the list data into the table in database
			for (int i = 0; i < list.size(); i++)
			{
				// store the result set data into the variable
				id = list.get(i).getID();
				name = list.get(i).getName();
				amount = list.get(i).getAmount();
				pic = list.get(i).getPIC();
				// execute SQL command which insert data into table in database
				pst = con.prepareStatement("insert into " + this.tableName +
											"(ID, Name, Amount, PIC)values(?,?,?,?)");
				pst.setInt(1,  id);
				pst.setString(2, name);
				pst.setString(3, amount);
				pst.setString(4, pic);
				pst.executeUpdate(); // execute the SQL statement in PreparedStatement object
			}
		}
		// catch the SQL error in the try block
		catch(SQLException ex)
		{
			ex.printStackTrace(); // print the error detail when exception occur
		}
	}
}