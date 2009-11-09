/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;

/**
 *
 * @author wackss
 */
public class Route {

    private Vector locaList;
    private Vector timeList;

    public Route() {
        locaList = new Vector();
        timeList = new Vector();
    }

    public Vector getStopList() {
        return locaList;
    }

    public void setStopList(Vector locaList) {
        this.locaList = locaList;
    }

    public Vector getTimeList() {
        return timeList;
    }

    public void setTimeList(Vector timeList) {
        this.timeList = timeList;
    }

    public void addStop(Stop st, int time) {
        locaList.addElement(st);
        timeList.addElement(new Integer(time));
    }

    public void addStopAtIndex(Stop st, int time, int index) {
        locaList.insertElementAt(st, index);
        timeList.insertElementAt(new Integer(time), index);
    }

    public void removeStop(int index) {
        locaList.removeElementAt(index);
        timeList.removeElementAt(index);
    }
    
    public void setStopTime(int index, int time) {
        timeList.setElementAt(new Integer(time), index);
    }

    public boolean isBetweenLocations(Location test, Location loc1, Location loc2, double err) {
        //get the vector along the direction of the route
        double vectorLongX = loc2.getLatitude() - loc1.getLatitude();
        double vectorLongY = loc2.getLongitude() - loc1.getLongitude();

        //get the vector perpendicular (this vector will define the width of the road)
        double vectorShortX = vectorLongY * -1;
        double vectorShortY = vectorLongX;

        //make vectorShort's magnitude equal to err*2
        double mag = Math.sqrt(vectorShortX*vectorShortX + vectorShortY*vectorShortY);
        vectorShortX = vectorShortX / mag * err * 2;
        vectorShortY = vectorShortY / mag * err * 2;

        //calculate the origin of our rectangle/coord system
        double originX = loc1.getLatitude();
        double originY = loc1.getLongitude();
        originX = originX - vectorShortX/2;
        originY = originY - vectorShortY/2;

        //get the vector from the origin to the test point
        double vectorPointX = test.getLatitude() - originX;
        double vectorPointY = test.getLongitude() - originY;

        //now do the test
        //vectorPoint must be in the same direction as both vectorShort & vectorLong
        double pointDotShort = vectorPointX*vectorShortX + vectorPointY*vectorShortY;
        double pointDotLong = vectorPointX*vectorLongX + vectorPointY*vectorLongY;
        double shortDotShort = vectorShortX*vectorShortX + vectorShortY*vectorShortY;
        double longDotLong = vectorLongX*vectorLongX + vectorLongY*vectorLongY;

        if ((0 <= pointDotShort) && (pointDotShort <= shortDotShort) &&
                (0 <= pointDotLong) && (pointDotLong <= longDotLong))
            return true;
        else
            return false;
    }

}
