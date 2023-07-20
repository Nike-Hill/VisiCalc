//VisiCalc
//Nikhil Venkat
//AP Computer Science

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class VisiCalc {
	//main method
	public static void main(String[]args) throws FileNotFoundException, UnsupportedEncodingException{
		Scanner scan = new Scanner(System.in);
		boolean quit = false;
		String in = "";
		//track inputs (I'm going to make a getCommand method later that this will be useful for)
		ArrayList<String> inputs = new ArrayList<String>();
		System.out.println("Welcome to VisiCalc!");
		//User input loop
		while(!quit){
			//get input
		    System.out.print("Enter: ");
		    in = scan.nextLine();
		    inputs.add(in);
		    //process input
		    quit = processCommand(in, inputs);
		}
	}
	
	public static boolean processCommand(String command, ArrayList<String> inputs) throws FileNotFoundException, UnsupportedEncodingException{
		boolean quit = false;
		//check input for commands
        if(command.equalsIgnoreCase("quit")){
            quit = true;
            System.out.println("Thank you for using VisiCalc.");
        } else if(command.equalsIgnoreCase("help")){
        	System.out.println("VisiCalc is a spreadsheet program!");
        } else if(command.equalsIgnoreCase("print")){
        	//print grid
        	Grid.print();
        } else if(command.length()>1&&command.length()<4) {
        	//input checking for complex commands
        	if(command.charAt(0) > 64 && command.charAt(0) < 72 && command.charAt(1) > 48 && command.charAt(1) < 58){
        		if((command.length()==3&&command.charAt(2) == '0') ||command.length()==2) {
        			System.out.println((Grid.spreadsheet[Cell.getRowAddress(command.substring(1))][Cell.getColumnAddress(command.substring(0,1))]).getValue());
        		}
        	}
        } else if(command.contains(" = ") || command.substring(0,4).equalsIgnoreCase("SORT")) {
        	//System.out.println("Splitting " + command);
        	String[] split = command.split(" ");
        	if(split.length == 4 && 
        	   split[0].substring(0,4).equalsIgnoreCase("sort") &&
        	   split[2].equals("-")) {
        		String sort = split[1] + " " + split[2] + " " + split[3];
        		
        		
        		
        		if(split[0].equalsIgnoreCase("SORTA")) {
        			Grid.sort(sort, false);
        		} else if(split[0].equalsIgnoreCase("SORTD")) {
        			Grid.sort(sort, true);
        		}
        	}
        	//System.out.println("Split[2] = " + split[2]);
        	if(split[1].equals("=")){
        		if(split[2].equals("\"")) {
        			//Create new Text Cell
        			Grid.spreadsheet[Cell.getRowAddress(split[0].substring(1))][Cell.getColumnAddress(split[0].substring(0,1))]= new TextCell(command.substring(command.indexOf("\"")+2, command.lastIndexOf("\"")-1), command);
        		}  else if(split[2].equals("(")){
    				//Create new Formula Cell
    				//System.out.println("Creating new formula cell...");
        			//System.out.println("Creating new formula cell");
    				Grid.spreadsheet[Cell.getRowAddress(split[0].substring(1))][Cell.getColumnAddress(split[0].substring(0,1))] = new FormulaCell(command.substring(command.indexOf("(")+2, command.lastIndexOf(")")-1));
    			} else if(split.length ==  3){
        			if(split[2].contains("/")) {
        				String[] dateSplit = split[2].split("/");
        				int month = Integer.parseInt(dateSplit[0]);
        				int day = Integer.parseInt(dateSplit[1]);
        				int year = Integer.parseInt(dateSplit[2]);
        				//Create new Date Cell
        				Grid.spreadsheet[Cell.getRowAddress(split[0].substring(1))][Cell.getColumnAddress(split[0].substring(0,1))] = new DateCell(month,day,year, command);
        			}else {
        				//Create new Cell
        				Grid.spreadsheet[Cell.getRowAddress(split[0].substring(1))][Cell.getColumnAddress(split[0].substring(0,1))] = new Cell(split[2], command);
        			}
        		}
        	} 
        //check for save and load commands
        } else if(command.contains(".txt")){
        	String[] split = command.split(" ");
        	if(split[0].equals("save")) {
        		//Save the grid
        		Grid.saveGrid(split[1], inputs);
        	} else if(split[0].equals("load")) {
        		//Load commands from file
        		Grid.loadFile(split[1], inputs);
        	}
        //check for clear command
        } else if(command.contains("clear")){
        	String[]split = command.split(" ");
        	if(split[0].equalsIgnoreCase("clear")){
        		if(split.length > 1) {
        			Grid.spreadsheet[Cell.getRowAddress(split[1].substring(1))][Cell.getColumnAddress(split[1].substring(0,1))] = null;
        		} else {
        			Grid.clear();
        		}
        	}
        } else{
    		System.out.println(command);
    	}
        //return quit so the program will end if the user prompted it to.
		return quit;
	}
}
