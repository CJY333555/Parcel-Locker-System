import java.io.*;
import java.util.*;

public class ReservationRepository implements IRepository<Reservation> {
    private final String filePath;

    public ReservationRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Reservation> loadAll() {
        List<Reservation> reservations = new ArrayList<>();
        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\|");
                if (p.length >= 9) {
                    reservations.add(new Reservation(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Reservations file not found.");
        }
        return reservations;
    }

    public void saveAll(List<Reservation> reservations) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (Reservation r : reservations) {
                pw.println(r.getReservationID() + "|" + r.getUserID() + "|" + r.getLockerID() + "|" + r.getCompartmentID()
                        + "|" + r.getStartTime() + "|" + r.getStatus() + "|" + r.getEndTime() + "|" + r.getPin()+ "|" + r.getPickedUpBy());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations.");
        }
    }
}


