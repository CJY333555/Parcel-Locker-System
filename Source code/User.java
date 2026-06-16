public abstract class User {
    protected String userID;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;

    public User(String userID, String name, String email, String phone, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Common getters
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }

    // Common setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }

    // Abstract method for user type
    public abstract String getUserType();

    // Common validation method
    public boolean validateCredentials(String inputID, String inputPassword) {
        return this.userID.equalsIgnoreCase(inputID) && this.password.equals(inputPassword);
    }

    public String toString() {
        return getUserType() + ": " + name + " (" + userID + ")";
    }
}
