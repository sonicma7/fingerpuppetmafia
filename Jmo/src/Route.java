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

    private Vector posiList;
    private Vector timeList;

    public Route() {
        posiList = new Vector();
        timeList = new Vector();
    }

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

    public void addPosition(Position st, double time) {
        posiList.addElement(st);
        timeList.addElement(new Double(time));
    }

    public void addPositionAtIndex(Position st, double time, int index) {
        posiList.insertElementAt(st, index);
        timeList.insertElementAt(new Double(time), index);
    }

    public void removePosition(int index) {
        posiList.removeElementAt(index);
        timeList.removeElementAt(index);
    }
    
    public void setStopTime(int index, double time) {
        timeList.setElementAt(new Double(time), index);
    }

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
