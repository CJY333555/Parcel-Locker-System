public class Admin extends User {
    public Admin(String adminID, String name, String email, String phone, String password) {
        super(adminID, name, email, phone, password);
    }

    public String getAdminID() { return userID; }
    
    public String getUserType() {
        return "Admin";
    }
}
