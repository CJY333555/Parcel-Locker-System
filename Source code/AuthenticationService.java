import java.util.List;

public class AuthenticationService {
    private List<Admin> admins;
    private List<Client> clients;

    public AuthenticationService(List<Admin> admins, List<Client> clients) {
        this.admins = admins;
        this.clients = clients;
    }

    public Admin authenticateAdmin(String adminID, String password) {
        for (Admin admin : admins) {
            if (admin.validateCredentials(adminID, password)) {
                return admin;
            }
        }
        return null;
    }

    public Client authenticateClient(String userID, String password) {
        for (Client client : clients) {
            if (client.validateCredentials(userID, password)) {
                return client;
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        return clients.stream().anyMatch(client -> client.getEmail().equals(email));
    }

    public boolean isPhoneExists(String phone) {
        return clients.stream().anyMatch(client -> client.getPhone().equals(phone));
    }

    public String generateNextUserID() {
        int max = 0;
        for (Client client : clients) {
            int id = Integer.parseInt(client.getUserID().replaceAll("[^0-9]", ""));
            if (id > max) max = id;
        }
        return "U" + String.format("%03d", max + 1);
    }
}
