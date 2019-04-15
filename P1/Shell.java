// DJ Wadhwa
// 04/14/2019

public class Shell extends Thread {
   StringBuffer input = new StringBuffer(""); //create String buffer for input from user
   public void run( ) { // "main" method for Thread OS
     int line = 1;  //initiate shell line number
     while (true) //run until break is called
     {
       SysLib.cout("shell[" +line+ "]%"); //first line of shell = "shell[1]:"
       SysLib.cin(input); //enter input in SysLib read function
       String in = input.toString(); // conver input to string using StringBuffer's toString() func
        if (in.equals("exit")) // if the input is exit then exit and break loop
        {
         SysLib.exit();
         break;
       }
       if (in.length() < 1) // if the input is empty then skip line
       {
         line++;
         continue;
       }
        String [] semiDelimiter = in.split(";"); //look for ";" in input
        String [] ampDelimiter= in.split("&"); // look for "&" in input

        if (semiDelimiter.length > 1) //if there is an arguement using ";"
        {
          if (ampDelimiter.length > 1 ) //check if there is also "&" in input
          {
            for (int i = 0; i< semiDelimiter.length; i++ )
            //traverse thru arguments with ; as delimeter
            {
              int liveProcesses = 0; //live Processes counter
              String [] ampDelimiter2 = semiDelimiter[i].split("&"); //split &
              for (int j = 0; j < ampDelimiter2.length; j++) // go through ampDelimiter2
              {
                String [] args = SysLib.stringToArgs(ampDelimiter2[j]); //
                if (SysLib.exec(args) < 0)
                {
                  liveProcesses--; //decrese live processes if execution failed
                }
                liveProcesses++; //increment live processes
              }
              while (liveProcesses > 0)
              {
                SysLib.join();
                liveProcesses--;
              }
            }
          }
          else // if only ";" in input
          {
            for(int i = 0; i< semiDelimiter.length; i++)
            {
              String [] args = SysLib.stringToArgs(semiDelimiter[i]);
              if (SysLib.exec(args) < 0)
              {
                input.delete(0, input.length()); // if single arguement empty input buffer
                line++; //increment line number
                continue; //recall the while loop
              }
              SysLib.join();
            }
          }
        }
        else if(ampDelimiter.length > 1) // if only "&" in input
        {
          int liveProcesses = 0; //count number of live process
          for (int i = 0; i < ampDelimiter.length; i++)
          {
            String [] args = SysLib.stringToArgs (ampDelimiter[i]);
            if (SysLib.exec(args) < 0)
            {
              liveProcesses--;
              //if could not execute arguement reduce number of live Processes
            }
              liveProcesses++; //if execute successful, increase number of live Processes
          }
          while (liveProcesses > 0) //if liveProcesses still exist join them
              {
                SysLib.join(); //end child process
                liveProcesses--;
              }
        }
        else
        {
          String [] args = SysLib.stringToArgs(in);
          if (SysLib.exec(args) < 0)
          {
            input.delete(0, input.length()); // if single arguement empty input buffer
            line++; //increment line number
            continue; //recall the while loop
          }
          SysLib.join(); //join with parent process
        }
       input.delete(0, input.length()); //empty input buffer
       line++; //increment line number
       }
       SysLib.sync(); //ensure blocks are synched
     }

     }
