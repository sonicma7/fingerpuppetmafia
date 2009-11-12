/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;
import javax.microedition.lcdui.TextBox;
import javax.microedition.location.*;

/*LocPosition sets up locations for a person in terms of longitude and latitude
 */
public class LocPosition implements Runnable, LocationListener {
    Location locPos;
    StringBuffer sb = null;

    double latitude, longitude=0;
    boolean coord_valid;
    LocationProvider provider= null;
    TextBox ltb1;
    Coordinates coordinates = null;

/*Default constructor for LocPosition, sets up Location Position listener,
 * buffer along with criteria
 */
    public void LocPosition() {
    }
    
    LocPosition(TextBox t1) {
        ltb1 = t1;
        sb = new StringBuffer("");
        ltb1.setString("In Loc Position");
        try {
            Criteria crit = new Criteria();
            crit.setCostAllowed(true); //default value
            crit.setPreferredPowerConsumption(Criteria.NO_REQUIREMENT);
            provider = LocationProvider.getInstance(crit);
        }
        catch (LocationException ex) {
            ex.printStackTrace();
        }
        provider.setLocationListener(this,-1,-1,-1);
    }
    
/* Sets up thread to continuously output and update current gps locations
 * works every 50ms
 */
    public void run() {
        while( true ) {
                String ss1 = new String(sb);
                ltb1.setString("Lat: " + Double.toString(latitude) + '\n' + "Long:" 
                        + Double.toString(longitude)+ '\n' + "Time:" + ss1);
                 
                try {
                    Thread.sleep(50);
                }
                catch(Exception e){};

        }
    }
    /*locationUpdated takes in a provider and a location, gets new locations and
     * updates the current time
     */
    public void locationUpdated(LocationProvider provider, Location locPos) {    
        coordinates = locPos.getQualifiedCoordinates();
        sb.delete(0,sb.length());
        sb.append(new Date(locPos.getTimestamp()).toString());
        latitude = coordinates.getLatitude();
        longitude = coordinates.getLongitude();
    }

    public void providerStateChanged(LocationProvider provider2, int arg1) {
    }
    /*getLatitude returns the current latitude
     */
    public double getLatitude(){
        return latitude;
    }

    /*getLongitude returns the current longitude
     */
    public double getLongitude(){
        return longitude;
    }
}


