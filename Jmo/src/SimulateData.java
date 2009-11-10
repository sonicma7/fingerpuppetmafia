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

public class SimulateData implements Runnable{
    Random generator = new Random();
    TextBox smb1;
    StringBuffer sb = null;
    double passengercount, totalpassengers;
    double[] shuttle, peaktime, offpeaktime;
    int found1 = 0, found2 = 0;

    public void SimulateData() {
    }
    
    SimulateData(TextBox t1){
        smb1 = t1;
        sb = new StringBuffer("");
        smb1.setString("Simulating data...");
    }

    public void run(){
        shuttle = new double[6];
        peaktime = new double[6];
        offpeaktime = new double[6];
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
        if (totalpassengers % 2 != 0){
            totalpassengers += 0.5;
        }
        sb.delete(0,sb.length());
        while(true){
            sb.append("Total Passengers: " + Double.toString(totalpassengers)
                    + '\n' + "Peak Passenger Time: ");
            for (int i=0;i<6;i++){
                if (peaktime[i] == 1){
                    sb.append("12:" + Integer.toString(i) + "0 ");
                    found1 = 1;
                }
            }
            if (found1 == 0){
                sb.append("None");
            }
            sb.append('\n' + "Off Peak Passenger Time: ");
            for (int i=0;i<6;i++){
                if (offpeaktime[i] == 1){
                    sb.append("12:" + Integer.toString(i) + "0 ");
                    found2 = 1;
                }
            }
            if (found2 == 0){
                sb.append("None");
            }
            String printer = new String(sb);
            smb1.setString(printer);
            try{
                Thread.sleep(50);
            }catch (Exception e){};
        }
    }
}