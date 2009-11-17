import java.util.*;

// this class describes a shuttle, which has a position on the map
public class Shuttle {
    private Position position;
    private String description;
    private double availability;
    private boolean full;

    //default constructor, starts with coordinates of 0,0
    public Shuttle() {
        Random generator = new Random();
        position = new Position();
        position.setLongitude(0.0);
        position.setLatitude(0.0);
        description = " A Bus";
        this.setAvailability(generator.nextInt(5), generator);
    }

    //constructor for specific coordinates
    public void setCoordinates(double longitude, double latitude){
        position.setLongitude(longitude);
        position.setLatitude(latitude);
    }

    //standard getters & setters
    public void setCoordiantes(Position p){
        position = p;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public void setAvailability(double time, Random gen) {
        availability = gen.nextInt(25)+1;
        if (time == 0 || time == 4){
            availability = availability * 1.5;
        }
        if (time == 2){
            availability = availability * 0.5;
        }
        if (time == 5){
            availability = availability * 2;
        }
        if (availability >= 25){
            full = true;
        }
        else{
            full = false;
        }
    }

    public Position getCoordinates() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public boolean getAvailability() {
        return full;
    }

}
