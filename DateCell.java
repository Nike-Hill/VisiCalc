//VisiCalc
//Nikhil Venkat
//AP Computer Science

//Helper Class

public class DateCell extends Cell{
	int month;
	int day;
	int year;
	//constructor
	public DateCell(int month, int day, int year, String command) {
		super("0", command);
		//print date
		this.setValue(month + "/" + day + "/" + year);
		this.month = month;
		this.day = day;
		this.year = year;
		this.command = command;
	}	
	
	public int compareTo(Cell c) {
		if(c==null ||
			c instanceof FormulaCell) {
			return 1;
		} else if(c instanceof DateCell) {
			System.out.println("Comparing two date cells");
			System.out.println(this.month + "/" + this.day + "/" + this.year);
			System.out.println(c.month + "/" + c.day + "/" + c.year);
			if (this.year != c.year) {
				return this.year - c.year;
			} else if (this.month != c.month) {
				return this.month - c.month;
			} else if (this.day != c.day) {
				System.out.println(this.day - c.day);
				return this.day - c.day;
			}
			return 0;
		}
		return -1;
	}
}
