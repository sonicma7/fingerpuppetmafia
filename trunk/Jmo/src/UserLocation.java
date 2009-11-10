/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */
public class UserLocation extends Position {

    public Stop getClosestStop(Route route) {
        double minDist = 9999;
        Stop closestStop = null;
        int closestStopIndex;

        for (int i=0; i<route.getStopList().size(); i++) {
            Stop tempStop = (Stop)(route.getStopList().elementAt(i));
            double tempDist = this.getDistanceTo(tempStop);
            if (tempDist <= minDist) {
                minDist = tempDist;
                closestStop = tempStop;
                closestStopIndex = i;
            }
        }

        if (minDist == 9999)
            return null;
        else
            return closestStop;
    }
}
