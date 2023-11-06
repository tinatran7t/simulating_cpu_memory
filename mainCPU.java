/*    Date         Author                    Project 1         Description                         */
/* ------------------------------------------------------------------------------------------------*/
/*    09/18/2022   Tina Tran(txt200023)      CPU & Main        Exploring Multiple Processes and IPC*/

import java.util.*;
import java.io.*;

public class mainCPU{

  //method for running the CPU
  public static class CPU
  {
  
    //initiate global variables
    int PC, SP, AC, X, Y, IR;
    int inputTime, timerInt;
    boolean kernel = false;
    PrintWriter memWrite;
    Scanner memRead;
  
    //CPU to take in the argument values given be the user
    public CPU(Scanner mRead, PrintWriter mWrite, int iTime) {
      //set kernel to false due to being in user mode
      kernel = false;
      
      //setters for memory being read, writtrn, and the input time given by the user
      memWrite = mWrite;
      memRead = mRead;
      inputTime = iTime;
      
      //set registers and timer to 0
      PC = 0;
      IR = 0;
      AC = 0;
      X  = 0;
      Y  = 0;
      timerInt = 0;
      
      //set stack pointer to 1000
      SP = 1000;
    }
    
    //method to represent kernel
    public void kernel(){
      //temp used as place holder for current stack pointer value
      int temp = SP;
      
      //set kernel to true
      kernel = true;
      //set stack pointer to 2000
      SP = 2000;
      
      //decrement SP and send address to memory
      sendToMem(--SP, temp);
      sendToMem(--SP, PC);
      sendToMem(--SP, IR);
      sendToMem(--SP, AC);
      sendToMem(--SP, X);
      sendToMem(--SP, Y);
    }
  
    //retrieve the address from memory
    private int getFromMem(int addr){
      //if the address is 1000+
      if(addr > 999 ){
        //if currently not in kernel mode 
        if(kernel == false){
          //print error message for memory violation
          System.err.println("Memory violation: Accessing system address 1000 in user mode.");
          System.exit(-1);
        }
      }
      
      //read the address and flush
      memWrite.println("r" + addr);
      memWrite.flush();
      
      //return integer to next line
      return Integer.parseInt(memRead.nextLine());
    }
  
    //send the data to the address and flush
    private void sendToMem(int addr, int data){
      memWrite.printf("w%d,%d\n", addr, data);
      memWrite.flush();
    }
  
    public void start(){
      //set runnning processor to true
      boolean inProcess = true;
      
      while(inProcess){
        //set instruction register to incremet program counter
        IR = getFromMem(PC++);
        
        //set processor to run the instructions
        inProcess = runInstructions();
        timerInt++; //increment the timer interrupt
        
        // timeout instructios will be interrupted at 1000
        if(timerInt >= inputTime){
          // if not kernel set timer interrupt to 0
          if(kernel == false) {
            timerInt -= inputTime;
            //switch over to kernel
            kernel();
            //set program counter to 1000
            PC = 1000;
          }
        }
      }
    }

    private boolean runInstructions(){
      //instruction register
      switch(IR){
        //Load the value into the AC
        case 1: 
          IR = getFromMem(PC++);
          AC = IR;
          break;
        //Load the value at the address into the AC
        case 2:
          IR = getFromMem(PC++);
          AC = getFromMem(IR);
          break;
        //Load the value from the address found in the given address into the AC
        case 3: 
          IR = getFromMem(PC++);
          AC = getFromMem(getFromMem(IR));
          break;
        //Load the value at (address+X) into the AC
        case 4: 
          IR = getFromMem(PC++);
          AC = getFromMem(IR + X);
          break;
        //Load the value at (address+Y) into the AC
        case 5: 
          IR = getFromMem(PC++);
          AC = getFromMem(IR + Y);
          break;
        //Load from (Sp+X) into the AC
        case 6: 
          AC = getFromMem(SP+X);
          break;
        //Store the value in the AC into the address
        case 7: 
          IR = getFromMem(PC++);
          sendToMem(IR, AC);
          break;
        //Gets a random int from 1 to 100 into the AC
        case 8: 
          AC = (int) (Math.random()*100+1);
          break;
        //Put port
        case 9: 
          IR = getFromMem(PC++);
          //If port=1, writes AC as an int to the screen
          if(IR == 1){ System.out.print(AC); }
          //If port=2, writes AC as a char to the screen
          else if(IR == 2){ System.out.print((char)AC); }
          break;
        //Add the value in X to the AC
        case 10: 
          AC += X; 
          break;
        //Add the value in Y to the AC
        case 11:
          AC += Y; 
          break;
        //Subtract the value in X from the AC
        case 12: 
          AC -= X; 
          break;
        //Subtract the value in Y from the AC
        case 13: 
          AC -= Y; 
          break;
        //Copy the value in the AC to X
        case 14: 
          X = AC; 
          break;
        //Copy the value in X to the AC
        case 15: 
          AC = X; 
          break;
        //Copy the value in the AC to Y
        case 16: 
          Y = AC; 
          break;
        //Copy the value in Y to the AC
        case 17: 
          AC = Y; 
          break;
        //Copy the value in AC to the SP
        case 18: 
          SP = AC; 
          break;
        //Copy the value in SP to the AC 
        case 19: 
          AC = SP; 
          break;
        //Jump to the address
        case 20: 
          IR = getFromMem(PC++);
          PC = IR;
          break;
        //Jump to the address only if the value in the AC is zero
        case 21: 
          IR = getFromMem(PC++);
          if(AC == 0){ PC = IR; }
          break;
        //Jump to the address only if the value in the AC is not zero
        case 22: 
          IR = getFromMem(PC++);
          if(AC != 0){ PC = IR; }
          break;
        //Push return address onto stack, jump to the address
        case 23: 
          IR = getFromMem(PC++);
          sendToMem(--SP, PC);
          PC = IR;
          break;
        //Pop return address from the stack, jump to the address
        case 24: 
          PC = getFromMem(SP++);
          break;
        //Increment the value in X
        case 25: 
          X++; 
          break;
        //Decrement the value in X
        case 26: 
          X--; 
          break;
        //Push AC onto stack
        case 27: 
          sendToMem(--SP, AC);
          break;
        //Pop from stack into AC
        case 28: 
          AC = getFromMem(SP++);
          break;
        //Perform system call
        case 29: 
          if(kernel == false){
            kernel();
            //set program counter to 1500
            PC = 1500;
          }
          break;
        //Return from system call
        case 30: 
          //increment stack pointer for each register
          Y  = getFromMem(SP++);
          X  = getFromMem(SP++);
          AC = getFromMem(SP++);
          IR = getFromMem(SP++);
          PC = getFromMem(SP++);
          SP = getFromMem(SP++);
          kernel = false;
          break;
        //End execution
        case 50: 
          memWrite.println("q");
          memWrite.flush();
          System.exit(0);
          return false;
        //default if none of the instructions are given
        default: 
          System.err.println("ERROR: Invalid instruction.");
          memWrite.println("q");
          memWrite.flush();
          System.exit(0);
          return false;
        }
      return true;
    }
    
  }

  //Main method used to call the run the programs using the implemented arguments
  public static void main(String[] args)throws Exception {
    
    //set up user input time 
    int inputTime = Integer.parseInt(args[1]);
    
    //set up runtime exec and initiating memory call with user input 
    Process pMem = Runtime.getRuntime().exec("java memory "+ args[0]);
      
      //scanner for piping the input 
      Scanner memRead = new Scanner(pMem.getInputStream());
      //printer for piping the output
      PrintWriter memWrite = new PrintWriter(pMem.getOutputStream());
      
      //call the cpu and parameters for reading and writing to memory along with the user input time
      CPU cpuProcessor = new CPU(memRead, memWrite, inputTime);
      
      //start the cpu
      cpuProcessor.start();
      
  }

}