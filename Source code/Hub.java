public class Hub {
    private String hubID;
    private String location;

    // Constructor
    public Hub(String hubID, String location) {
        this.hubID = hubID;
        this.location = location;
    }

    // Getters
    public String getHubID() {
        return hubID;
    }

    public String getLocation() {
        return location;
    }

    // Setters (optional, if you want to allow editing)
    public void setLocation(String location) {
        this.location = location;
    }

    // Convert to file format
    public String toFileString() {
        return hubID + "|" + location;
    }

    public String toString() {
        return hubID + " - " + location;
    }
}
