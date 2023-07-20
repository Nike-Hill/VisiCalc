//VisiCalc
//Nikhil Venkat
//AP Computer Science

//Helper Class
import java.util.*;
public class FormulaCell extends Cell{
	String formula;
	//constructor
	public FormulaCell(String formula) {
		super(evaluate(formula), formula);
		//System.out.println("Called evaluate");
	}
	//method used to evaluate formulas
	public static String evaluate(String formula){
		//if command is SUM then call the SumOrAverage method with the boolean indicating that the command is AVG set to false
		 if(formula.contains(" ") && formula.substring(0, formula.indexOf(" ")).equals("SUM")) {
			return SumOrAverage(formula.substring(formula.indexOf(" ")+1), false);
		//if command is AVG then call the SumOrAverage method with the boolean indicating that the command is AVG set to true
		} else if(formula.contains(" ") && formula.substring(0, formula.indexOf(" ")).equals("AVG")) {
			return SumOrAverage(formula.substring(formula.indexOf(" ")+1), true);
		}  else if(formula.length()>1 && 
				formula.length()<4 && 
				formula.charAt(0) > 64 && 
				formula.charAt(0) < 72 && 
				formula.charAt(1) > 48 && 
				formula.charAt(1) < 58 &&
				((formula.length()==3&&formula.charAt(2) == '0') ||formula.length()==2)) {
        	//input checking for complex commands
        		return ((Grid.spreadsheet[Cell.getRowAddress(formula.substring(1))][Cell.getColumnAddress(formula.substring(0,1))]).getValue());
		
        }else {
			//System.out.println("Evaluating " + formula);
			//Store all tokens from formula in an array to make solving easier
			String [] split = formula.split(" ");
			for(int i = 0; i < split.length; i++) {
				//if token is a reference to a cell, replace cell reference with it's int value so it can be solved as part of the formula.
				if(split[i].length()>1 &&
				split[i].length()<4 && 
				split[i].charAt(0) > 64 && 
				split[i].charAt(0) < 72 && 
				split[i].charAt(1) > 48 && 
				split[i].charAt(1) < 58) {
					if((split[i].length()==3&&split[i].charAt(1)=='1'&&split[i].charAt(2)=='0') || split[i].length() == 2){
						//System.out.println("split[i]: " + split[i]);
						//System.out.println("Row Address: " + getRowAddress(split[i].substring(1)));
						//System.out.println("Column Address: " + getColumnAddress(split[0].substring(0,1)));
						if(Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))] instanceof TextCell ||
						   Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))] instanceof DateCell ||
						   Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))] == null) {
							//If the token refers to a TextCell or a DateCell, or refers to a cell that holds the value null, replace it with 0.
							split[i] = "0";
						} else if(Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))] instanceof FormulaCell) {
							//System.out.println("Going to evaluate sub-formula " + split[i] + " from formula " + formula);
							split[i]= evaluate(Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))].getCommand());
							//If the token refers to a Formula Cell, replace it with the solution to the formula.
						} else {
							//System.out.println("split[i]: " + split[i]);
							split[i] = Grid.spreadsheet[getRowAddress(split[i].substring(1))][getColumnAddress(split[i].substring(0,1))].getValue();
							//If the token refers to a cell that is not a formula cell, date cell, or Text cell, then replace it with the int value it holds.
						}
					}
					
				}
			}
			String solution = "";
			for(int i = 0; i < split.length; i++) {
				solution += split[i];
				if(i != split.length-1) {
					solution += " ";
				}
			}


			
			
			//initialize variables used to check for parentheses
			int paras = 0;
	        String left = "";
	        String op = "";
	        String right = "";
	        int[] numOfOpenParas = new int[solution.length()];
			while(solution.contains("(") || solution.contains(")")) {
				for(int i = 0; i < solution.length(); i++){
					if(solution.charAt(i) == '('){
						paras++;
					} else if(solution.charAt(i) == ')'){
						paras--;
					}
					numOfOpenParas[i] = paras;
	                if(numOfOpenParas[i] == 0 && i > solution.indexOf("(") && solution.contains("(")){
	                	//If a pair of parentheses exists within the formula, evaluate the part of the formula between them before the rest of it.
	                    left = solution.substring(0, solution.indexOf("("));
	                    op = solution.substring(solution.indexOf("(") + 2, i-1);
	                    right = solution.substring(i+1);
	                    op = evaluate(op);
	                    solution = left + op + right;
	                    
	                }
				}
			}
			
			//initialize variables used to simplify the formula.
			String prev = "";
			String next = "";
			String temp = "";
			String last = "";
			String solved = "";
			
			//check for multiplication and division
			while(solution.contains(" * ") || solution.contains(" / ") || solution.contains(" + ") || solution.contains(" - ")) {
				//System.out.println("* or / or + or -");
				while(solution.contains("*") || solution.contains("/")) {
	                //System.out.println("* or /");
	                solved = "";
	                String[] split1 = solution.split(" ");
	                for(int i = 0; i<split1.length; i++) {
	                	//System.out.println("Traversing split array, looking for * and /, length is " + split1.length + ", current index is " + i);
	                    if(split1[i].equals("*")){
	                        //MULTIPLY
	                    	//System.out.println("formula: " + solution);
	                    	prev = split1[i-1];
	                    	next = split1[i+1];
	                        solved = "" + (int) Math.floor(Double.parseDouble(prev) * Double.parseDouble(next));
	                        temp = solution;
	                        if(temp.indexOf("*")+2+next.length()<temp.length()) {
	                        	last = temp.substring(temp.indexOf("*")+2+next.length());
	                        } else {
	                        	last = "";
	                        }
	                        solution = temp.substring(0, (temp.indexOf("*") -1 - prev.length())) + solved  +  last;
	                        split1 = solution.split(" ");
	                        i = 0;
	                    } else if(split1[i].equals("/")){
	                        //DIVIDE
	                    	prev = split1[i-1];
	                    	next = split1[i+1];
	                        solved = "" + (int) Math.floor(Double.parseDouble(prev) / Double.parseDouble(next));
	                        temp = solution;
	                        if(temp.indexOf("/")+2+next.length()<temp.length()) {
	                        	last = temp.substring(temp.indexOf("/")+2+next.length());
	                        } else {
	                        	last = "";
	                        }
	                        solution = temp.substring(0, (temp.indexOf("/") -1 - prev.length())) + solved  +  last;
	                        split1 = solution.split(" ");
	                        i = 0;
	                    }
	                }
	                prev = "";
	                next = "";
	        		temp = "";
	        		last = "";
				}
				
				//check for addition and subtraction
				while(solution.contains(" + ") || solution.contains(" - ")) {
	                //System.out.println("+ or -");
	                solved = "";
	                String[] split1 = solution.split(" ");
	                for(int i = 0; i<split1.length; i++) {
	                    if(split1[i].equals("+")){
	                        //ADD
	                    	prev = split1[i-1];
	                    	next = split1[i+1];
	                    	//System.out.println("previous: " + prev + ", current: " + split[i] + ", next: " + next + ", formula: " + solution);
	                    	solved = "" + (int) Math.floor(Double.parseDouble(prev) + Double.parseDouble(next));
	                        temp = solution;
	                        if(temp.indexOf("+")+2+next.length()<temp.length()) {
	                        	last = temp.substring(temp.indexOf("+")+2+next.length());
	                        } else {
	                        	last = "";
	                        }
	                        //System.out.println("Formula: " + temp);
	                        //System.out.println("previous: " + prev + ", current: " + split[i] + ", next: " + next);
	                        solution = temp.substring(0, (temp.indexOf("+") -1 - prev.length())) + solved  +  last;
	                        split1 = solution.split(" ");
	                        i = 0;
	                    } else if(split1[i].equals("-")){
	                        //SUBTRACT
	                    	prev = split1[i-1];
	                    	next = split1[i+1];
	                    	solved = "" + (int) Math.floor(Double.parseDouble(prev) - Double.parseDouble(next));
	                        temp = solution;
	                        if(temp.indexOf("-")+2+next.length()<temp.length()) {
	                        	last = temp.substring(temp.indexOf("-")+2+next.length());
	                        } else {
	                        	last = "";
	                        }
	                        solution = temp.substring(0, (temp.indexOf("- ") -1 - prev.length())) + solved + last;
	                        split1 = solution.split(" ");
	                        i = 0;
	                    }
	                }
	                prev = "";
	                next = "";
	        		temp = "";
	        		last = "";
				}
			}
			//return solution to formula.
			return solution;
		}
	}
	//method for sum and average functions
	public static String SumOrAverage(String formula, Boolean Average) {
		String[]split = formula.split(" ");
		//Set up variable for beginning and ending rows and columns for rectangular ranges
		int row1 = Math.min(Cell.getRowAddress(split[0].substring(1)), Cell.getRowAddress(split[2].substring(1)));
		int row2 = Math.max(Cell.getRowAddress(split[0].substring(1)), Cell.getRowAddress(split[2].substring(1)));
		int column1 = Math.min(Cell.getColumnAddress(split[0].substring(0,1)), Cell.getColumnAddress(split[2].substring(0,1)));
		int column2 = Math.max(Cell.getColumnAddress(split[0].substring(0,1)), Cell.getColumnAddress(split[2].substring(0,1)));
		double sum = 0;
		int count = 1;
		//nested for loop for rectangular range
		for(int r = row1; r<= row2; r++) {
			for(int c = column1; c <= column2; c++) {
				if(Grid.spreadsheet[r][c] instanceof TextCell ||
					Grid.spreadsheet[r][c] instanceof DateCell ||
					Grid.spreadsheet[r][c] == null){
					//If [r][c] refers to a text cell, date cell, or nothing; then add nothing to sum.
				} else if(Grid.spreadsheet[r][c] instanceof FormulaCell) {
						sum += Double.parseDouble(evaluate(Grid.spreadsheet[r][c].getCommand()));
						//If [r][c] refers to a formula cell; add the value that the formula evaluates to to the sum.
				} else {
						sum += Double.parseDouble(Grid.spreadsheet[r][c].getValue());
						//If [r][c] refers to a cell that is not a formula cell, date cell, or Text cell, add it's int value to sum.
				}
			}
		}
		
		if(Average) {
			//if command is AVG (and not SUM) then divide the sum by the number of cells in the rectangular range
			count = (row2-row1+1)*(column2-column1+1);
		}
		if((sum/count) % 1 == 0.0) {   
			return "" + (int)(sum/count);
		}
		return "" + (sum / count);
	}
	
	public int compareTo(Cell c) {
		if(c==null) {
			return 1;
		} else if(c instanceof FormulaCell) {
			return Integer.parseInt(evaluate(this.getCommand())) - Integer.parseInt(evaluate(c.getCommand()));
		}
		return -1;
	}
}
