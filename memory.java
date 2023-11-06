/*    Date         Author                    Project 1         Description                         */
/* ------------------------------------------------------------------------------------------------*/
/*    09/18/2022   Tina Tran(txt200023)      Memory Class      Exploring Multiple Processes and IPC*/

import java.util.*;
import java.io.*;

public class memory{

  //array for memory with 2000 integer entries
  public static int[] mem = new int[2000]; 

  //method used for writing the data into the address
  public static void writing(int addr, int data){
    mem[addr] = data;
  }
  
  //method used for reading the address from memory and returning the value
  public static int reading(int addr){
    return mem[addr];
  }
  
  //method used to load addresses into memory depending if they are integers or start with a decimal
  public static void initMem(String fileInput) throws FileNotFoundException{
    //use scanner to read lines from the textfile/input
    Scanner scanLine = new Scanner(new File(fileInput));
    //value to load the address
    int loadAddr = 0;
    
    do{
      String s1 = scanLine.nextLine().trim();
      int sLen = s1.length(); //get length of the string
      
      //check if there is there is a string in the line if not then continue to the next line
      if(sLen == 0)
        continue;
      //check if line starts with a decimal and load the address into memory
      else if(s1.charAt(0) == '.'){
        loadAddr = Integer.parseInt(s1.substring(1).split(" ")[0]);
        continue;
      }
      //check if the initial character is not a number between 0 to 9
      else if(s1.charAt(0) < '0' || s1.charAt(0) > '9')
        continue;
      //proceed to load the integer into memory
      else{
        String[] splitSpace = s1.split(" "); //split white spaces
        mem[loadAddr++] = Integer.parseInt(splitSpace[0]);
      }
      
    }while(scanLine.hasNextLine());
    //close the scanner
    scanLine.close();
  }
  
  //Main method to retrieve the argument provided by the user and scan the values
  public static void main(String[] args){ 
    //string variable to hold file arguments
    String fileInput = args[0];
    //use scanner to read lines from the textfile/input
    Scanner scanLine = new Scanner(System.in); 
    
    //try memory initialization and catch to handle file not found exception
    try{ 
      initMem(fileInput);
    }catch (FileNotFoundException ex) {
      System.err.println("Error with input file."); //print to standard error
      System.exit(1); //exit exception
    }
    
    do{ 
      //initializing address and data for reading/writing, readInt is used to print out the read integer
      int addr, data, readInt;
      //create string input for the next line being read and string array to hold values
      String s1 = scanLine.nextLine();
      String[] val = s1.substring(1).split(",");
      //create character for each command being received
      char cmd = s1.charAt(0);
      
      //if given command is 'r' for reading set the address
      if (cmd == 'w'){
        addr = Integer.parseInt(val[0]);
        data = Integer.parseInt(val[1]);
        //write the data to the address
        writing(addr, data);
      }
      //if given command is 'w' for writing set the address and data
      else if (cmd == 'r'){
        addr = Integer.parseInt(s1.substring(1));
        readInt = reading(addr);
        //print the integer
        System.out.println(readInt);
      }
      // 'q' command is to quit out of the program
      else if (cmd == 'q'){
        System.exit(0); //exit success
      }
      // send error for incorrect commmand
      else
        System.out.println("Error: Incorrect command.");
    
    }while(scanLine.hasNextLine()); //while loop while scanning through the entire text file
    //close the scanner
    scanLine.close();
  }

}