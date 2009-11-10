/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */
public class Stop extends Location {

    private String name;
    
    public Stop() {
        super();
        isStop = true;
    }

    public Stop(double lat, double lon) {
        super(lat, lon);
        isStop = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
