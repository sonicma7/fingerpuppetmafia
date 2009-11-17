
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */

/*
 * The position class is a basic, low level class that represents a position on the map.
 * Note that some positions can also be "Stops" so the isStop boolean keeps
 * track of that when iterating through Positions looking only for Stops.
 */
public class Position {

    protected double latitude;
    protected double longitude;

    protected String name;

    protected boolean isStop;

    //default constructor sets the coordinates to 0
    public Position() {
        this.latitude = this.longitude = 0;
        isStop = false;
    }

    //constructor for a specific latitude & longitude
    public Position(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
        isStop = false;
    }

    public Position(double lat, double lon, boolean stop) {
        this.latitude = lat;
        this.longitude = lon;
        isStop = stop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //standard getters & setters
    public boolean isStop() {
        return isStop;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //this function simply returns the distance from this point to another point
    public double getDistanceTo(Position other) {
        double xdist = (other.getLatitude() - this.latitude);
        double ydist = (other.getLongitude() - this.longitude);

        return Math.sqrt(xdist*xdist + ydist*ydist);
    }
}
