import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentService {
    private List<Reservation> reservations;

    public PaymentService(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public double calculatePayment(String reservationID) {
        Reservation target = findReservationById(reservationID);
        
        if (target == null) {
            System.out.println("Reservation not found.");
            return 0.0;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime start = LocalDateTime.parse(target.getStartTime().trim(), formatter);
            LocalDateTime now = LocalDateTime.now();

            long days = java.time.Duration.between(start, now).toDays() + 1; // inclusive
            double total = 4.00; // RM 4.00 for first 7 days
            
            if (days < 7) {
                total = 4.00; 
            } else if (days > 7) {
                total += (days - 7) * 0.50;
            }
            return total;

        } catch (Exception e) {
            System.out.println("Error parsing dates for payment calculation.");
            return 0.00;
        }
    }

    private Reservation findReservationById(String reservationID) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationID().equalsIgnoreCase(reservationID)) {
                return reservation;
            }
        }
        return null;
    }
}
