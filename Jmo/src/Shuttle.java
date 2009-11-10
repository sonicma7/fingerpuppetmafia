// Placeholder
public class Shuttle {
    private double coordinates[] = new double[2];
    private String description;

    public Shuttle() {
        coordinates[0] = -1;
        coordinates[1] = -1;

        description = "Bus";
    }

    public void setCoordinates(double longitude, double latitude){
        coordinates[0] = longitude;
        coordinates[1] = latitude;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

}
