 //VisiCalc
//Nikhil Venkat
//AP Computer Science

//Helper Class
public class Cell implements Comparable<Cell>{
	int num;
	int month;
	int day;
	int year;
	private String value;
	String command;
	//constructor
	public Cell(String value, String command){
		this.setValue(value);
		this.command = command;
		//System.out.println("The command " + command + " created a cell with the value " + value);
	}
	//returns row address 
	public static int getRowAddress(String address){
		//System.out.println("getting row address from the string " + address);
		return Integer.parseInt(address)-1;
	}
	//returns column address
	public static int getColumnAddress(String address){
		//System.out.println("Int value of " + address.charAt(0) + ": " +(int)address.charAt(0));
		//System.out.println("Address String from getColumnAddress: " + address);
		//System.out.println("getting column address from the string " + address);		
		return ((int)(address.toUpperCase().charAt(0)))-65;
	}
	//Getter for the command that created cell
	public String getCommand() {
		return this.command;
	}
	//Getter for value stored in cell
	public String getValue() {
		if(this.value.equals(null)) {
			return "0";
		}
		return this.value;
	}
	//Setter for the command that created cell
	public void setValue(String value) {
		this.value = value;
	}

	public int compareTo(Cell c) {
		if(c==null ||
			c instanceof TextCell ||
			c  instanceof DateCell ||
			c instanceof FormulaCell) {
			return 1;
		}
		return Integer.parseInt(this.getValue()) - Integer.parseInt(c.getValue());
	}
	
	public String toString() {
		return this.value;
	}
	

}
