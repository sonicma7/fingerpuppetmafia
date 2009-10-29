/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mark
 */

import java.util.Random;
import javax.microedition.lcdui.TextBox;

public class SimulateData implements Runnable {
    Random generator = new Random();
    TextBox smb1;
    StringBuffer sb = null;
    double passengercount, peaktime, offpeaktime;

    public void SimulateData() {

    }
    SimulateData(TextBox t1){
        smb1 = t1;
        smb1.setString("Simulating data...");

    }

    public void run(){
        while(true){
            smb1.setString("Total Passengers: " + Double.toString(passengercount)
                    + '\n' + "Peak Passenger Time: " + Double.toString(peaktime)
                    + '\n' + "Off Peak Passenger Time: " + Double.toString(offpeaktime));

            try{
                Thread.sleep(1000);
            }catch (Exception e){};
        }
    }








}
