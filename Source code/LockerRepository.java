import java.io.*;
import java.util.*;

public class LockerRepository implements IRepository<Locker> {
    private final String filePath;

    public LockerRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Locker> loadAll() {
        List<Locker> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 5) {
                    result.add(new Locker(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Lockers file not found.");
        }
        return result;
    }

    public void saveAll(List<Locker> lockers) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Locker l : lockers) {
                pw.println(l.getHubID() + "|" + l.getLockerID() + "|" + l.getCompartmentID() + "|" + l.getSize() + "|" + l.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving lockers.");
        }
    }
}


