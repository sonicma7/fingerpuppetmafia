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

}
