import java.io.*;
import java.util.*;

public class ClientRepository implements IRepository<Client> {
    private final String filePath;

    public ClientRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Client> loadAll() {
        List<Client> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 5) {
                    result.add(new Client(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Clients file not found.");
        }
        return result;
    }

    public void saveAll(List<Client> clients) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Client c : clients) {
                pw.println(c.getUserID() + "|" + c.getName() + "|" + c.getEmail() + "|" + c.getPhone() + "|" + c.getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving clients.");
        }
    }
}


