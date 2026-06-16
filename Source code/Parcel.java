public class Parcel {
    private String parcelID;
    private String reservationID;
    private String lockerID;
    private String compartmentID;
    private String userID;

    public Parcel(String parcelID, String reservationID, String lockerID, String compartmentID, String userID) {
        this.parcelID = parcelID;
        this.reservationID = reservationID;
        this.lockerID = lockerID;
        this.compartmentID = compartmentID;
        this.userID = userID;
    }

    public String getParcelID() { return parcelID; }
    public String getReservationID() { return reservationID; }
    public String getLockerID() { return lockerID; }
    public String getCompartmentID() { return compartmentID; }
    public String getUserID() { return userID; }
}
