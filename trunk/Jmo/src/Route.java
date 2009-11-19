/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;

/**
 *
 * @author wackss
 */

/*
 * The Route class describes a shuttle route as a list of Positions, some of
 * which are Stops.
 * timeList is a list of times that represent average times between stops.
 * The two lists are parallel, meaning that one element in the position list has
 * a corresponding element in the time list (the average time to get to that
 * stop from the previous stop).
 */
public class Route {

    private Vector posiList;
    private Vector timeList;

    //default constructor - initialize vectors
    public Route() {
        posiList = new Vector();
        timeList = new Vector();
    }

    //standard getters and setters
    public Vector getStopList() {
        return posiList;
    }

    public void setStopList(Vector posiList) {
        this.posiList = posiList;
    }

    public Vector getTimeList() {
        return timeList;
    }

    public void setTimeList(Vector timeList) {
        this.timeList = timeList;
    }

    //add a position to the end of the route (along with it's average time)
    public void addPosition(Position st, double time) {
        posiList.addElement(st);
        timeList.addElement(new Double(time));
    }

    //this adds the position/time to the middle of the route at a specific index
    public void addPositionAtIndex(Position st, double time, int index) {
        posiList.insertElementAt(st, index);
        timeList.insertElementAt(new Double(time), index);
    }

    //remove a position in the route
    public void removePosition(int index) {
        posiList.removeElementAt(index);
        timeList.removeElementAt(index);
    }

    //set the time for a route position
    public void setStopTime(int index, double time) {
        timeList.setElementAt(new Double(time), index);
    }

    /*
     * this function determines whether or not the test position is between
     * position loc1 and loc2, allowing the test position to be off of the line
     * segment by a small error.
     *
     * test - the position to be tested between the other two positions
     * loc1 - the position defining the first point
     * loc2 - the position defining the second point
     * err - the allowable distance from the line segment that test can be
     *
     * returns: the percentage along the line segment that test is.
     * For example if it's 3/4 of the way from loc1 to loc2 the function will
     * return 0.75.
     *
     * the function returns -1 if test is not between loc1 and loc2
     */
    public double isBetweenPositions(Position test, Position loc1, Position loc2, double err) {
        //get the vector along the direction of the route
        double vectorLongX = loc2.getLatitude() - loc1.getLatitude();
        double vectorLongY = loc2.getLongitude() - loc1.getLongitude();

        //get the vector from location 1 to the test point
        double vectorPointX = test.getLatitude() - loc1.getLatitude();
        double vectorPointY = test.getLongitude() - loc1.getLongitude();

        double pointDotLong = vectorPointX*vectorLongX + vectorPointY*vectorLongY;
        double longDotLong = vectorLongX*vectorLongX + vectorLongY*vectorLongY;

        //now just need to make sure it's close enough
        double t = pointDotLong / longDotLong;
        if (t < 0.0)
            t = 0.0;
        if (t > 1.0)
            t = 1.0;

        double closestX = loc1.getLatitude() + t*vectorLongX;
        double closestY = loc1.getLongitude() + t*vectorLongY;

        double dx = test.getLatitude() - closestX;
        double dy = test.getLongitude() - closestY;

        //d.x /=2;
        double dist_sqr = dx*dx + dy*dy;
        //return dist_sqr <= (circle_radius * circle_radius);

        if ((0 <= pointDotLong) && (pointDotLong <= longDotLong) && (dist_sqr <= err*err))
            return (pointDotLong/longDotLong);
        else
            return -1;
    }

}
