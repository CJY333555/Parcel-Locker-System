public class Reservation {
    private String reservationID;
    private String userID;
    private String lockerID;
    private String compartmentID;
    private String startTime;
    private String status;
    private String endTime;
    private String pin;
    private String pickedUpBy;

    public Reservation(String reservationID, String userID, String lockerID, String compartmentID,
                       String startTime, String status, String endTime, String pin, String pickedUpBy) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.lockerID = lockerID;
        this.compartmentID = compartmentID;
        this.startTime = startTime;
        this.status = status;
        this.endTime = endTime;
        this.pin = pin;
        this.pickedUpBy = pickedUpBy;
    }

    public String getReservationID() 
    { 
        return reservationID; 
    }
    public String getUserID() 
    { 
        return userID; 
    }
    public String getLockerID() 
    { 
        return lockerID; 
    }
    public String getCompartmentID() 
    { 
        return compartmentID; 
    }
    public String getStartTime() 
    { 
        return startTime; 
    }
    public String getStatus() 
    { 
        return status; 
    }
    public String getEndTime() 
    { 
        return endTime; 
    }
    public String getPin() 
    { 
        return pin; 
    }

    public void setStatus(String status) 
    { 
        this.status = status; 
    }
    public void setEndTime(String endTime)
    { 
        this.endTime = endTime; 
    }
    public String getPickedUpBy() {
        return pickedUpBy;
    }

    public void setPickedUpBy(String pickedUpBy) {
        this.pickedUpBy = pickedUpBy;
    }
}
