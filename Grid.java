
//VisiCalc
//Nikhil Venkat
//AP Computer Science

import java.io.*;
import java.util.*;
//Helper Class
public class Grid {
	//Make the new spreadsheet
	static Cell[][] spreadsheet = new Cell[10][7];
	public static void print(){
		System.out.print("       |");
		//print first line of sheet
		for(int i = 65; i <72; i++) {
			System.out.print("   "+((char)i)+"   |");
		}
		System.out.println("");
		for(int i = 0; i<64; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		for(int r = 0; r<10; r++) {  //row loop
			System.out.printf("  %2d   |", (r+1));  //print row number
			for(int c = 0; c<7; c++) { //column loop
				if(spreadsheet[r][c] instanceof FormulaCell) {
					//if cell at [r][c] is a formula cell, re-evaluate the formula before printing.
					spreadsheet[r][c].setValue(FormulaCell.evaluate(spreadsheet[r][c].getCommand()));
				}
				if(spreadsheet[r][c] == null) {
					System.out.print("       ");
				//Print the value stored in the cell
				} else if((spreadsheet[r][c].getValue()).length() < 5) {
					System.out.printf("%4s   ", spreadsheet[r][c].getValue());
				} else if((spreadsheet[r][c].getValue()).length() < 8) {
					System.out.print(spreadsheet[r][c].getValue());
					for(int i = (spreadsheet[r][c].getValue()).length(); i < 7; i++) {
						System.out.print(" ");
					}
				} else {
					//if the length of the value is over 7 characters, only print the first 7 characters.
					System.out.print((spreadsheet[r][c].getValue()).substring(0,7));
				}
				System.out.print("|");
			}
			System.out.print("\n");
			for(int i = 0; i<64; i++) {
				System.out.print("-");
			}
			System.out.print("\n");
		}

	}
	//saveGrid method; uses printStream to write all commands received to given destination
    public static void saveGrid(String file, ArrayList<String> inputs) throws FileNotFoundException {			
		PrintStream ps = new PrintStream(file);
		for(int i = 0; i < inputs.size(); i++) {
			String temp = inputs.get(i);
			if(!(temp.equals("load " + file) || temp.equals("save " + file))) {
				ps.printf("%s\n", temp);
			}
		}
    }
    //loadFile method
    public static void loadFile(String file, ArrayList<String> inputs) throws FileNotFoundException, UnsupportedEncodingException {
    	File f = new File(file);
    	if(f.exists()){
    		Scanner fileReader = new Scanner(f);
    		while(fileReader.hasNext()) {
    			String temp = fileReader.nextLine();
    			if(!(temp.equals("load " + file) || temp.equals("save " + file))) {
    				inputs.add(temp);
    				System.out.println("Processing command \"" + temp + "\" recieved from file " + file);
    				VisiCalc.processCommand(temp, inputs);
    			}
    		}
    	} else {
    		//If file does not exist, notify the user
    		System.out.println("File does not exist, or the file name is misspelled.");
    	}
    }
    //clear method sets all cells in grid to null
    public static void clear() {
    	for(int r = 0; r < 10; r++) {
    		for(int c = 0; c<7; c++) {
    			spreadsheet[r][c] = null;
    		}
    	}
    }

	public static void sort(String formula, boolean descending) {
		String[]split = formula.split(" ");
		//Set up variable for beginning and ending rows and columns for rectangular ranges
		int row1 = Math.min(Cell.getRowAddress(split[0].substring(1)), Cell.getRowAddress(split[2].substring(1)));
		int row2 = Math.max(Cell.getRowAddress(split[0].substring(1)), Cell.getRowAddress(split[2].substring(1)));
		int column1 = Math.min(Cell.getColumnAddress(split[0].substring(0,1)), Cell.getColumnAddress(split[2].substring(0,1)));
		int column2 = Math.max(Cell.getColumnAddress(split[0].substring(0,1)), Cell.getColumnAddress(split[2].substring(0,1)));
		System.out.println("row1: " + row1);
		System.out.println("row2: " + row2);
		System.out.println("column1: " + column1);
		System.out.println("column2: " + column2);
		Cell [] sorted = new Cell [(row2-row1+1)*(column2-column1+1)];
		ArrayList<Cell> sortNotNull = new ArrayList<Cell>();
		//nested for loop for rectangular range
		for(int r = row1; r<= row2; r++) {
			for(int c = column1; c <= column2; c++) {
				if(spreadsheet[r][c] != null) {
					sortNotNull.add(spreadsheet[r][c]);
				}
			}
		}
		Cell[] notNullArray = new Cell[sortNotNull.size()];
		for(int i = 0; i < sortNotNull.size(); i++) {
			notNullArray[i] = sortNotNull.get(i);
		}
		
		Arrays.sort(notNullArray);
		System.out.println(Arrays.toString(notNullArray));
		for(int i = 0; i < notNullArray.length; i++) {
			sorted[sorted.length-notNullArray.length + i] = notNullArray[i];
		}
		if(descending) {
			System.out.println("Descending");
			int end = sorted.length-1;
			System.out.println(sorted.toString());
			for (int start = 0; start < end; start++){
		        Cell temp = sorted[start];
		        sorted[start] = sorted[end];
		        sorted[end] = temp;
		        end--;
		    }
			System.out.println("Reversed to decend.");
			System.out.println(sorted.toString());
		}

		for(int r = row1; r<= row2; r++) {
			for(int c = column1; c <= column2; c++) {
				spreadsheet[r][c] = sorted[r*(column2-column1+1)+c-row1];
			}
		}
	}	
}
