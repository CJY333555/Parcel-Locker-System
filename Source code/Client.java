public class Client extends User {
    public Client(String userID, String name, String email, String phone, String password) {
        super(userID, name, email, phone, password);
    }
    
    public String getUserType() {
        return "Client";
    }
}
