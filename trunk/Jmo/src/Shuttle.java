// Placeholder
public class Shuttle {
    private Position position;
    private String description;

    public Shuttle() {
        position = new Position();
        position.setLongitude(0.0);
        position.setLatitude(0.0);
        description = " A Bus";
    }

    public void setCoordinates(double longitude, double latitude){
        position.setLongitude(longitude);
        position.setLatitude(latitude);
    }

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
