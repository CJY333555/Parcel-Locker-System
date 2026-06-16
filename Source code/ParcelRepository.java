import java.io.*;
import java.util.*;

public class ParcelRepository implements IRepository<Parcel> {
    private final String filePath;

    public ParcelRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Parcel> loadAll() {
        List<Parcel> result = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 5) {
                    result.add(new Parcel(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Parcels file not found.");
        }
        return result;
    }

    public void saveAll(List<Parcel> parcels) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Parcel p : parcels) {
                pw.println(p.getParcelID() + "|" + p.getReservationID() + "|" + p.getLockerID() + "|" + p.getCompartmentID() + "|" + p.getUserID());
            }
        } catch (IOException e) {
            System.out.println("Error saving parcels.");
        }
    }
}


