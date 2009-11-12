/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mark
 */

import java.util.*;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;

/* The Simulate Data class is a thread that simulates data that could be
 * extrapolated from constantly gathered information if the program was
 * kept on a server or internet site.  Since the program is not hosted on
 * a web site, the data is simulated at runtime. */
public class SimulateData implements Runnable{
    //Random number generator
    Random generator = new Random();
    TextBox smb1;
    StringBuffer sb = null;
    double passengercount, totalpassengers;
    double[] shuttle, peaktime, offpeaktime;
    int found1 = 0, found2 = 0;

    //Default constructor
    public void SimulateData() {
    }

    //Constructor that has a TextBox argument for use later in the class
    SimulateData(TextBox t1){
        smb1 = t1;
        sb = new StringBuffer("");
        smb1.setString("Simulating data...");
    }

    /* At startup the class executes the run function which simulates data that
     * would be gathered throughout the day.  Each time received a modifier
     * based on when the shuttle would normally have people on it.  The
     * function then outputs to the screen the total number of passengers, peak
     * shuttle time, and offpeak shuttle time. */
    public void run(){
        shuttle = new double[6];
        peaktime = new double[6];
        offpeaktime = new double[6];

        //Add a random number to array with modifier based on time
        for(int i=0; i<6; i++){
            passengercount = generator.nextInt(25) + 1;
            if (i == 0 || i == 4){
                passengercount = passengercount * 1.5;
            }
            if (i == 2){
                passengercount = passengercount * 0.5;
            }
            if (i == 5){
                passengercount = passengercount * 2;
            }
            if (passengercount > 25){
                passengercount = 25;
            }
            shuttle[i] = passengercount;
        }

        //If passengers are large mark time as peak or if small mark as offpeak
        for (int i=0; i<6; i++){
            if (shuttle[i] > 20){
                peaktime[i] = 1;
            }
            else{
                peaktime[i] = 0;
            }
            if (shuttle[i] < 5){
                offpeaktime[i] = 1;
            }
            else{
                offpeaktime[i] = 0;
            }
            totalpassengers += shuttle[i];
        }

        //Modifier may cause a fraction, can't have fraction of a person
        if (totalpassengers % 2 != 0){
            totalpassengers += 0.5;
        }
        sb.delete(0,sb.length());
        
        //Output display to screen
        while(true){

            //Add string to buffer in chunks to make output look pretty onscreen
            sb.append("Total Passengers: " + Double.toString(totalpassengers)
                    + '\n' + "Peak Passenger Time: ");
            for (int i=0;i<6;i++){
                if (peaktime[i] == 1){
                    sb.append("12:" + Integer.toString(i) + "0 ");
                    found1 = 1;
                }
            }
            //If no peak times, add None to string buffer
            if (found1 == 0){
                sb.append("None");
            }
            //Add off peak times
            sb.append('\n' + "Off Peak Passenger Time: ");
            for (int i=0;i<6;i++){
                if (offpeaktime[i] == 1){
                    sb.append("12:" + Integer.toString(i) + "0 ");
                    found2 = 1;
                }
            }
            //If no offpeak times, add None ot string buffer
            if (found2 == 0){
                sb.append("None");
            }

            //Make new string and set TextBox to it.
            String printer = new String(sb);
            smb1.setString(printer);

            //Sleep thread to save on memory.
            try{
                Thread.sleep(50);
            }catch (Exception e){};
        }
    }
}