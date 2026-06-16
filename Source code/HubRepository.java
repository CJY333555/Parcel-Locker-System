import java.io.*;
import java.util.*;

public class HubRepository implements IRepository<Hub> {
    private final String filePath;

    public HubRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Hub> loadAll() {
        List<Hub> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 2) {
                    result.add(new Hub(p[0], p[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Hubs file not found.");
        }
        return result;
    }

    public void saveAll(List<Hub> hubs) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Hub h : hubs) {
                pw.println(h.getHubID() + "|" + h.getLocation());
            }
        } catch (IOException e) {
            System.out.println("Error saving hubs.");
        }
    }
}


