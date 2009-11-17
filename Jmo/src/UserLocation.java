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
 * UserLocation, a subclass of Position, defines the Position of the user
 * (the coordinates that the phone GPS returns)
 */
public class UserLocation extends Position {

    //we just use the Position constructors
    public UserLocation() {
        super();
    }

    public UserLocation(double lat, double lon) {
        super(lat, lon);
    }

    /*
     * returns the index of the stop closest to the user
     * simply iterates through the stops in the route and keeps track of the
     * closest one
     */
    public int getClosestStop(Route route) {
        double minDist = 9999;
        int closestStopIndex = -1;
        Position tempStop = null;

        for (int i=0; i<route.getStopList().size(); i++) {
            tempStop = (Position)(route.getStopList().elementAt(i));
            if (tempStop.isStop()) {
                double tempDist = this.getDistanceTo(tempStop);
                if (tempDist <= minDist) {
                    minDist = tempDist;
                    closestStopIndex = i;
                }
            }
        }

        return closestStopIndex;
    }

    /*
     * returns the index of the shuttle closest to the user
     * simply iterates through the shuttles and keeps track of the
     * closest one
     */
    public Shuttle getClosestShuttle(Vector shuttles) {
        Shuttle closestShuttle = null;
        double minDist = 9999;

        for (int i=0; i<shuttles.size(); i++) {
            Shuttle tempShuttle = (Shuttle)(shuttles.elementAt(i));
            double tempDist = this.getDistanceTo(tempShuttle.getCoordinates());
            if (tempDist < minDist) {
                minDist = tempDist;
                closestShuttle = tempShuttle;
            }
        }

        return closestShuttle;
    }

    /*
     * Returns the approximate amount of time until a shuttle reaches a stop
     * on a route.
     *
     * the ETA is calculated using the average time for each stop, along with
     * the isBetweenPositions function of the Route class
     *
     * returns -1 if shuttle is not found on the route
     */
    public double getETA(Shuttle shuttle, int stopNum, Route route) {
        double eta = 0;
        boolean done = false;
        int currentStop = stopNum;
        int lastStop = route.getStopList().size()-1;
        Position test, p1, p2;
        while (!done) {
            test = shuttle.getCoordinates();
            p1 = (Position)(route.getStopList().elementAt(currentStop));
            if (currentStop > 0)
                p2 = (Position)(route.getStopList().elementAt(currentStop-1));
            else
                p2 = (Position)(route.getStopList().elementAt(lastStop));

            double percent = route.isBetweenPositions(test, p1, p2, .5);
            if (percent >= 0) {
                eta += percent * ((Double)(route.getTimeList().elementAt(currentStop))).doubleValue();
                done = true;
            }
            else {
                eta += ((Double)(route.getTimeList().elementAt(currentStop))).doubleValue();
            }

            if (currentStop > 0)
                currentStop--;
            else
                currentStop = lastStop;

            if ((currentStop == stopNum)&&(done == false)) { //we've returned to the original stop without finding the shuttle
                eta = -1;
                done = true;
            }
        }
        return eta;
    }
}
