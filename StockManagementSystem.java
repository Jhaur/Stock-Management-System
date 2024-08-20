import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;



public class StockManagementSystem {
	// create UI attribute
	private JFrame frame;
	private JTable table;
	// create Database attribute
	private JTextField textFieldName;
	private JTextField textFieldAmount;
	private JTextField textFieldPIC;
	private JTextField textFieldItemID;
	
	// create the JDBC class attribute
	private JDBC database;
	// create the StockList class attribute
	private StockList stockList;

	// main class to run the system
	public static void main(String[] args) {
		//launch the application in other thread 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StockManagementSystem window = new StockManagementSystem();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// create the constructor
	public StockManagementSystem() 
	{
		database = new JDBC(); // create JDBC class object
		stockList = new StockList(database.getCon(), database.getTable()); // create StockList object
		initialize(); // initialize the contents of the frame
	}

	// initialize the contents of the frame
	private void initialize() 
	{
		// create and set up frame	of UI
		frame = new JFrame();
		frame.setBounds(100, 100, 1033, 600);
		frame.getContentPane().setLayout(null); // set the frame into absolute positioning
		frame.setTitle("Stock Management System");
		
		// set closing event method to save data and disconnect into database before closing
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stockList.saveData();
				database.disconnect();
				System.exit(0);
			}
		});
		
		//create "Stock Management List" label and add into frame
		JLabel lblTitle = new JLabel("Stock Management List");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 29));
		lblTitle.setBounds(347, 32, 352, 66);
		frame.getContentPane().add(lblTitle);
		
		// create and set up a scroll panel and add into frame
		JPanel panelRegistration = new JPanel();
		panelRegistration.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRegistration.setBounds(43, 108, 447, 223);
		frame.getContentPane().add(panelRegistration);
		panelRegistration.setLayout(null);
		
		//create "Item" label and add into panelRegistration
		JLabel lblName = new JLabel("Item: ");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblName.setBounds(25, 29, 60, 46);
		panelRegistration.add(lblName);
		
		//create "Amount" label and add into panelRegistration
		JLabel lblAmount = new JLabel("Amount: ");
		lblAmount.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblAmount.setBounds(25, 81, 90, 46);
		panelRegistration.add(lblAmount);
		
		//create "PIC" label and add into panelRegistration
		JLabel lblPIC = new JLabel("PIC:");
		lblPIC.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblPIC.setBounds(25, 142, 43, 34);
		panelRegistration.add(lblPIC);
		
		//create text field for item name and add into panelRegistration
		textFieldName = new JTextField();
		textFieldName.setBounds(128, 41, 260, 30);
		panelRegistration.add(textFieldName);
		textFieldName.setColumns(10);
		
		//create text field for amount and add into panelRegistration
		textFieldAmount = new JTextField();
		textFieldAmount.setColumns(10);
		textFieldAmount.setBounds(128, 93, 260, 30);
		panelRegistration.add(textFieldAmount);
		
		//create text field for pic and add into panelRegistration
		textFieldPIC = new JTextField();
		textFieldPIC.setColumns(10);
		textFieldPIC.setBounds(128, 148, 260, 30);
		panelRegistration.add(textFieldPIC);
		
		// create and set up a scroll panel and add into frame
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(510, 108, 499, 336);
		frame.getContentPane().add(scrollPane);
		
		// create custom table and view in the scroll pane
		String col[] = {"ID", "Name","Amount", "PIC"};
		DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        table = new JTable(tableModel);
		scrollPane.setViewportView(table);
		stockList.table(tableModel);
		
		// create and set up save button
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// declare variable to store the value
				String bname, amount, pic;
				// assign variable value get from text field
				bname = textFieldName.getText();
				amount = textFieldAmount.getText();
				pic = textFieldPIC.getText();
				// create a Exception Handling to handle the error
				try {
					stockList.add(bname, amount, pic); // add new stock into the list
					stockList.table(tableModel); // display the table
					
					// set the text field into empty string
					textFieldName.setText("");
					textFieldAmount.setText("");
					textFieldPIC.setText("");
					textFieldName.requestFocus();
				}
				// catch the error in the try block
				catch (Exception error) {
					error.printStackTrace(); // print the error detail when exception occur
				}
			}
		});
		// set up the save button
		btnSave.setBounds(53, 359, 114, 66);
		frame.getContentPane().add(btnSave); // add save button into the frame content pane
		
		// create the exit button
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stockList.saveData(); // save the data into database
				database.disconnect(); // disconnect the SQL server
				System.exit(0); // exit the program
			}
		});
		// set up the exit button
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnExit.setBounds(215, 359, 114, 66);
		frame.getContentPane().add(btnExit); // add exit button into the frame content pane
		
		// create the clear button
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set the text field into empty string
				textFieldName.setText("");
				textFieldAmount.setText("");
				textFieldPIC.setText("");
				textFieldItemID.setText("");
				textFieldName.requestFocus(); // set to focus states
			}
		});
		// set up the clear button
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnClear.setBounds(375, 359, 114, 66);
		// add button to the frame content pane
		frame.getContentPane().add(btnClear);
		
		// create a panel for searching item
		JPanel panelSearch = new JPanel();
		// set up the panel
		panelSearch.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSearch.setBounds(43, 446, 446, 80);
		frame.getContentPane().add(panelSearch); // add panel into the frame content pane
		panelSearch.setLayout(null);
		
		// create and set up "Item ID" label
		JLabel lblItemID = new JLabel("Item ID: ");
		lblItemID.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblItemID.setBounds(20, 35, 101, 28);
		panelSearch.add(lblItemID); // add label into the panelSearch
		
		// create a text field for panelSearch
		textFieldItemID = new JTextField();
		textFieldItemID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					// get the ID number from the text field
					String id = textFieldItemID.getText();
					
					// use if-else statement check the id exist or not
					if(stockList.search(id)!= -1)
					{
						// get the Stock object information and store into variable
						Object[] data = stockList.getInfo(id);
						String name = (String) data[1];
						String amount = (String) data[2];
						String pic = (String) data[3];
						
						// display the stock information on the text field
						textFieldName.setText(name);
						textFieldAmount.setText(amount);
						textFieldPIC.setText(pic);
					}
					else {
						// pop out the dialog to inform the error
						JOptionPane.showMessageDialog(null,  "ID number not exist!");
					}
				}
				// catch the error in the try block to handle the empty string error
				catch (NumberFormatException error) {
					;
				}
				// catch the error in the try block
				catch (Exception error) {
					error.printStackTrace(); // print the error detail when exception occur
				}
			}
		});
		// set up the text field for item id
		textFieldItemID.setColumns(10);
		textFieldItemID.setBounds(122, 33, 260, 30);
		panelSearch.add(textFieldItemID); // add into panelSearch
		
		// create the update button
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// declare variable and store the value which get from text field
				String name, amount, pic, id;
				name = textFieldName.getText();
				amount = textFieldAmount.getText();
				pic = textFieldPIC.getText();
				id = textFieldItemID.getText();
				try
				{
					// pass the value of variable and update the stock information
					stockList.update(id, name, amount, pic);
					stockList.table(tableModel); // reload the table
					
					// pop out dialog to inform the record is updated
					JOptionPane.showMessageDialog(null, "Record Update!!!!!!");
					
					// set the text field into empty string
					textFieldName.setText("");
					textFieldAmount.setText("");
					textFieldPIC.setText("");
					textFieldItemID.setText("");
					textFieldName.requestFocus(); // set to focus state			
				}
				// catch the error in the try block
				catch (Exception error) {
					error.printStackTrace(); // print the error detail when exception occur
				}
			}
		});
		// set up the update button
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnUpdate.setBounds(613, 449, 114, 66);
		frame.getContentPane().add(btnUpdate); // add update button into the frame content pane
		
		// create delete button
		JButton btnDelete = new JButton("Delete");
		// add action listener to the delete button
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// create variable and store value get from textFieldItemID
				String id;
				id = textFieldItemID.getText();
				try {
					// remove the stock base on id number
					stockList.remove(id);
					stockList.table(tableModel); // reload table
					// pop out dialog to inform record deleted
					JOptionPane.showMessageDialog(null,  "Record Delete!!!!!");
					
					// set the text field into empty string
					textFieldName.setText("");
					textFieldAmount.setText("");
					textFieldPIC.setText("");
					textFieldItemID.setText("");
					textFieldName.requestFocus();
				}
				// catch the error in the try block
				catch (Exception error) {
					error.printStackTrace(); // print the error detail when exception occur
				}
			}
		});
		// set up the delete button
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnDelete.setBounds(789, 449, 114, 66);
		frame.getContentPane().add(btnDelete); // add delete button into the frame content pane
	}
}
