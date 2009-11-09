/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */
public class Location {

    private double latitude;
    private double longitude;

    public Location() {
        this.latitude = this.longitude = 0;
    }

    public Location(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
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

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public double getDistanceTo(Location other) {
        double xdist = (other.getLatitude() - this.latitude);
        double ydist = (other.getLongitude() - this.longitude);

        return Math.sqrt(xdist*xdist + ydist*ydist);
    }
}
