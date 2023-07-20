//VisiCalc
//Nikhil Venkat
//AP Computer Science

//Helper Class
public class TextCell extends Cell{
	//constructor
	public TextCell(String text, String command){
		super(text, command);
	}
	
	public int compareTo(Cell c) {
		if(c==null ||
			c  instanceof DateCell ||
			c instanceof FormulaCell) {
			return 1;
		} else if(c instanceof TextCell) {
			int length1 = this.getValue().length();  
			int length2 = c.getValue().length();  
			int length = Math.min(length1, length2);  
			   
			int i = 0;  
			while (i < length) {  
				char ch1 = this.getValue().charAt(i);  
				char ch2 = c.getValue().charAt(i);  
			    if (ch1 != ch2) {  
			        return ch1 - ch2;  
			    }  
			    i++;  
			}  
			return length1 - length2;  			  
		}
		return -1;
	}
}