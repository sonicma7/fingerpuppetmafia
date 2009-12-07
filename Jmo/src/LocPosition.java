/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;
import javax.microedition.lcdui.TextBox;
import javax.microedition.location.*;

/* LocPosition sets up locations for a person in terms of longitude and latitude
 */
public class LocPosition implements Runnable, LocationListener {
    int file = 0;
    Location locPos;
    StringBuffer sb = null;

    double latitude, longitude=0;
    boolean coord_valid;
    LocationProvider provider= null;
    TextBox ltb1;
    Coordinates coordinates = null;
    Data thisdata = new Data();

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
    
    /* Sets up thread to continuously output and update current gps locations,
     * along with the closest stop and ETA of next shuttle.  Works every 50ms
     * after the initial wait to load in the shuttles
     */
    public void run() {
        //figure out which route we are talking about
        int rte;
        if (ltb1.getTitle() == "East")
            rte = 0;
        else
            rte = 1;
        
        // Reset the kml file
        file++;
        if (file > 5) {
            file = 1;
        }
        
        Parser par = new Parser();

        //run parser
        par.go("1");
        try{
        Thread.sleep(5000);
        }
        catch(Exception e){
            System.out.println("Exception: " + e);
         }
        //set data up with shuttles
        thisdata.setShuttles(par.getShuttles());

        if(thisdata.getShuttles() != null){
            //get the user location
            UserLocation user = new UserLocation(this.getLatitude(),this.getLongitude());
            System.out.println("User:" + this.getLatitude() + "," + this.getLongitude());
            //get the correct route
            Route thisroute = ((Route)(thisdata.getRoutes()).elementAt(rte));
            //get closest stop, position of that stop and then the closest shuttle
            int stop = user.getClosestStop(thisroute);
            Position pos = (Position)(thisroute.getStopList().elementAt(stop));
            Shuttle thisshuttle = user.getClosestShuttle(thisdata.getShuttles());

            //append the string with the correct information about student and relative objects
            while( true ) {
                    String ss1 = new String(sb);
                    ltb1.setString("Lat: " + Double.toString(latitude) + '\n' + "Long:"
                            + Double.toString(longitude)+ '\n' + "Time:" + ss1 + '\n'
                            + "Closest Stop:" + pos.getName() + '\n'
                            + "Closest Shuttle ETA:" + (int)user.getETA(thisshuttle, stop, thisroute) + " Seconds");

                    try {
                        Thread.sleep(50);
                    }
                    catch(Exception e){};
            }
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


