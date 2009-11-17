// this class describes a shuttle, which has a position on the map
public class Shuttle {
    private Position position;
    private String description;

    //default constructor, starts with coordinates of 0,0
    public Shuttle() {
        position = new Position();
        position.setLongitude(0.0);
        position.setLatitude(0.0);
        description = " A Bus";
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

    public Position getCoordinates() {
        return position;
    }

    public String getDescription() {
        return description;
    }

}
