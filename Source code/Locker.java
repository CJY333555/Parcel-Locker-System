public class Locker {
    private String hubID;
    private String lockerID;
    private String compartmentID;
    private String size;
    private String status;

    public Locker(String hubID, String lockerID, String compartmentID, String size, String status) {
        this.hubID = hubID;
        this.lockerID = lockerID;
        this.compartmentID = compartmentID;
        this.size = size;
        this.status = status;
    }

    public String getHubID() { return hubID; }
    public String getLockerID() { return lockerID; }
    public String getCompartmentID() { return compartmentID; }
    public String getSize() { return size; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
