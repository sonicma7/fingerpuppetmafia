/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;

/**
 *
 * @author wackss
 */
public class UserLocation extends Position {

    public UserLocation() {
        super();
    }

    public UserLocation(double lat, double lon) {
        super(lat, lon);
    }

    public int getClosestStop(Route route) {
        double minDist = 9999;
        int closestStopIndex = -1;
        Stop tempStop = null;

        for (int i=0; i<route.getStopList().size(); i++) {
            Position temp = (Position)(route.getStopList().elementAt(i));
            if (temp.isStop()) {
                tempStop = (Stop)temp;
                double tempDist = this.getDistanceTo(tempStop);
                if (tempDist <= minDist) {
                    minDist = tempDist;
                    closestStopIndex = i;
                }
            }
        }

        return closestStopIndex;
    }

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
