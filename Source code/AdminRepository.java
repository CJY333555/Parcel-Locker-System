import java.io.*;
import java.util.*;

public class AdminRepository implements IRepository<Admin> {
    private final String filePath;

    public AdminRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Admin> loadAll() {
        List<Admin> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 5) {
                    result.add(new Admin(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Admins file not found.");
        }
        return result;
    }

    public void saveAll(List<Admin> admins) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Admin a : admins) {
                pw.println(a.getAdminID() + "|" + a.getName() + "|" + a.getEmail() + "|" + a.getPhone() + "|" + a.getPassword());
            }
        } catch (IOException e) {
            System.out.println("Error saving admins.");
        }
    }
}


