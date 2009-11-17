/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wackss
 */

//Stop is a subclass of Position, and has a name to describe the specific stop
public class Stop extends Position {

    private String name;
    
    public Stop() {
        super();
        isStop = true;
    }

    public Stop(double lat, double lon) {
        super(lat, lon);
        isStop = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
