
public class Stock {
	// create Stock attribute
	private int id;
	private String name;
	private String amount;
	private String pic; //Person in charge
	
	// create constructor to initialize the object attribute value
	public Stock(int id, String name, String amount, String pic)
	{
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.pic = pic;
	}
	
	// method return stock ID number
	public int getID()
	{
		return this.id;
	}
	
	// method return stock name
	public String getName()
	{
		return this.name;
	}
	
	// method return stock amount
	public String getAmount()
	{
		return this.amount;
	}
	
	// method return stock person in charge
	public String getPIC()
	{
		return this.pic;
	}
	
	// method set stock name
	public void setName(String name)
	{
		this.name = name;
	}
	
	// method set stock amount
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	
	// method set stock person in charge
	public void setPIC(String pic)
	{
		this.pic = pic;
	}
	
	// method to return array that store stock attribute data
	public Object[] data()
	{
		Object[]data = {id, name, amount, pic};
		return data;
	}
	
	// method to set the string represent Stock object
	public String toString()
	{
		return this.id + " - " + this.name + " - " + this.amount + 
				" - " + this.pic;
	}
}
