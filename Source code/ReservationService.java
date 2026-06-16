import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ReservationService {
    private List<Reservation> reservations;
    private List<Parcel> parcels;
    private List<Hub> hubs;
    private ReservationRepository reservationRepository;
    private ParcelRepository parcelRepository;
    private LockerService lockerService;

    public ReservationService(List<Reservation> reservations, List<Parcel> parcels, List<Hub> hubs,
                             ReservationRepository reservationRepository, ParcelRepository parcelRepository,
                             LockerService lockerService) {
        this.reservations = reservations;
        this.parcels = parcels;
        this.hubs = hubs;
        this.reservationRepository = reservationRepository;
        this.parcelRepository = parcelRepository;
        this.lockerService = lockerService;
    }

    public void viewAllReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation reservation : reservations) {
            System.out.println(reservation.getReservationID() + " | " + reservation.getUserID() + " | " + reservation.getLockerID() +
                               " | " + reservation.getCompartmentID() + " | " + reservation.getStatus());
        }
    }

    public void viewUserReservations(String userID) {
        if (userID == null || userID.isEmpty()) {
            System.out.println("Invalid user.");
            return;
        }

        System.out.println("\n=== Your Reservations ===");
        boolean hasReservations = false;

        for (Reservation reservation : reservations) {
            if (reservation.getUserID().equalsIgnoreCase(userID)) {
                hasReservations = true;
                System.out.println("--------------------------------------");
                System.out.println("Reservation ID : " + reservation.getReservationID());
                System.out.println("Locker ID      : " + reservation.getLockerID());
                System.out.println("Compartment ID : " + reservation.getCompartmentID());
                System.out.println("Start Time     : " + reservation.getStartTime());
                System.out.println("End Time       : " + (reservation.getEndTime().equals(" ") ? "N/A" : reservation.getEndTime()));
                System.out.println("Status         : " + reservation.getStatus());
                System.out.println("PIN            : " + reservation.getPin());
                System.out.println("Picked Up By   : " + (reservation.getPickedUpBy().equals(" ") ? "N/A" : reservation.getPickedUpBy()));
            }
        }

        if (!hasReservations) {
            System.out.println("You have no reservations.");
        }
        System.out.println("--------------------------------------\n");
    }

    public void makeReservation(String userID) {
        Scanner sc = new Scanner(System.in);

        if (userID == null || userID.isEmpty()) {
            System.out.println("Invalid user.");
            return;
        }

        // Step 1: Select Hub
        String hubID = selectHub(sc);
        if (hubID == null) return;

        // Step 2: Select Locker Size
        String sizeFilter = selectLockerSize(sc);
        if (sizeFilter == null) return;

        // Step 3: Choose Locker and Compartment
        Locker chosenLocker = selectLockerAndCompartment(sc, hubID, sizeFilter);
        if (chosenLocker == null) return;

        // Step 4: Enter Parcel ID
        System.out.print("Enter Parcel ID: ");
        String parcelID = sc.nextLine().toUpperCase();

        // Step 5: Create Reservation
        createReservation(userID, parcelID, chosenLocker);
    }

    public void dropOffParcel(String reservationID) {
        Scanner sc = new Scanner(System.in);
        Reservation target = findValidReservation(reservationID, sc, "Booked", "PickedUp");
        
        if (target == null) return;

        // Check PIN
        if (!validatePIN(target, sc)) return;

        // Update status and locker availability
        target.setStatus("DroppedOff");
        target.setEndTime(" "); // No end time yet since it's dropped off

        lockerService.updateLockerStatusByLockerAndCompartment(target.getLockerID(), target.getCompartmentID(), "In Use");

        // Save changes
        reservationRepository.saveAll(reservations);
        System.out.println("Parcel dropped off. Locker marked as in use.");
    }

    public void pickupParcel(String reservationID, String userID) {
        Scanner sc = new Scanner(System.in);
        Reservation target = findValidReservation(reservationID, sc, "Booked", "DroppedOff");
        
        if (target == null) return;

        // Validate PIN
        if (!validatePIN(target, sc)) return;

        // Step 3: Calculate payment
        PaymentService paymentService = new PaymentService(reservations);
        double payment = paymentService.calculatePayment(target.getReservationID());
        System.out.printf("Total payment required: RM %.2f%n", payment);

        System.out.print("Do you want to pay now? (yes/no): ");
        String choice = sc.nextLine();

        if (!choice.equalsIgnoreCase("yes")) {
            System.out.println("Pickup cancelled until payment is made.");
            return;
        }

        System.out.println("Payment successful!");

        // Step 4: Mark as picked up and free locker
        target.setStatus("PickedUp");
        target.setEndTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        target.setPickedUpBy(userID);

        lockerService.updateLockerStatusByLockerAndCompartment(target.getLockerID(), target.getCompartmentID(), "Available");

        reservationRepository.saveAll(reservations);
        System.out.println("Parcel picked up. Locker now available.");
    }

    public void cancelReservation(String reservationID) {
        Scanner sc = new Scanner(System.in);
        Reservation target = findValidReservation(reservationID, sc, "Booked", "DroppedOff");
        
        if (target == null) return;

        // Cancel reservation and free locker
        target.setStatus("Cancelled");
        target.setEndTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        lockerService.updateLockerStatusByLockerAndCompartment(target.getLockerID(), target.getCompartmentID(), "Available");

        reservationRepository.saveAll(reservations);
        System.out.println("Reservation cancelled. Locker now available.");
    }

    private String selectHub(Scanner sc) {
        while (true) {
            System.out.println("\nAvailable Hubs:");
            for (Hub hub : hubs) {
                System.out.println(hub.getHubID() + " : " + hub.getLocation());
            }
            System.out.print("Enter Hub ID (or type 'exit' to cancel): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) return null;

            boolean hubExists = hubs.stream().anyMatch(h -> h.getHubID().equalsIgnoreCase(input));
            if (hubExists) {
                return input;
            } else {
                System.out.println("Invalid Hub ID. Please try again.");
            }
        }
    }

    private String selectLockerSize(Scanner sc) {
        while (true) {
            System.out.print("Enter Locker Size (Small/Medium/Large) or type 'exit': ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) return null;

            if (input.equalsIgnoreCase("Small") || input.equalsIgnoreCase("Medium") || input.equalsIgnoreCase("Large")) {
                // Standardize case (capitalize first letter)
                return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            } else {
                System.out.println("Invalid size. Please enter Small, Medium, or Large.");
            }
        }
    }

    private Locker selectLockerAndCompartment(Scanner sc, String hubID, String sizeFilter) {
        while (true) {
            System.out.println("\nAvailable Lockers (filtered):");
            List<Locker> availableLockers = lockerService.getAvailableLockers(hubID, sizeFilter);
            
            if (availableLockers.isEmpty()) {
                System.out.println("No available lockers for this selection.");
                return null;
            }

            for (Locker locker : availableLockers) {
                System.out.println(locker.getHubID() + " | " + locker.getLockerID() + " | " + locker.getCompartmentID() + " | " + locker.getSize());
            }

            System.out.print("Enter Locker ID (or type 'exit'): ");
            String lockerID = sc.nextLine();
            if (lockerID.equalsIgnoreCase("exit")) return null;

            System.out.print("Enter Compartment ID (or type 'exit'): ");
            String compID = sc.nextLine();
            if (compID.equalsIgnoreCase("exit")) return null;

            Locker chosenLocker = lockerService.findLocker(hubID, lockerID, compID);

            if (chosenLocker == null) {
                System.out.println("Locker or Compartment not found. Please try again.");
            } else if (!chosenLocker.getStatus().equalsIgnoreCase("Available")) {
                System.out.println("Selected locker compartment is not available.");
                chosenLocker = null; // reset
            } else {
                return chosenLocker; // found correct locker
            }
        }
    }

    private void createReservation(String userID, String parcelID, Locker chosenLocker) {
        String reservationID = getNextReservationID();
        String pin = String.format("%06d", new Random().nextInt(999999));
        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        parcels.add(new Parcel(parcelID, reservationID, chosenLocker.getLockerID(), chosenLocker.getCompartmentID(), userID));
        reservations.add(new Reservation(reservationID, userID, chosenLocker.getLockerID(), chosenLocker.getCompartmentID(), startTime, "Booked", " ", pin, " "));
        chosenLocker.setStatus("In Use");

        parcelRepository.saveAll(parcels);
        reservationRepository.saveAll(reservations);
        lockerService.updateLockerStatusByLockerAndCompartment(chosenLocker.getLockerID(), chosenLocker.getCompartmentID(), "Booked");

        System.out.println("Reservation created! Your PIN is: " + pin);
    }

    private Reservation findValidReservation(String reservationID, Scanner sc, String... validStatuses) {
        Reservation target = null;
        
        while (true) {
            for (Reservation reservation : reservations) {
                if (reservation.getReservationID().equalsIgnoreCase(reservationID)) {
                    for (String status : validStatuses) {
                        if (reservation.getStatus().equalsIgnoreCase(status)) {
                            target = reservation;
                            break;
                        }
                    }
                    if (target != null) break;
                }
            }

            if (target == null) {
                System.out.println("Reservation not found or not valid for this operation.");
                System.out.print("Enter Reservation ID again (or type 'exit'): ");
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("exit")) return null;
                reservationID = input;
            } else {
                break;
            }
        }
        return target;
    }

    private boolean validatePIN(Reservation target, Scanner sc) {
        while (true) {
            System.out.print("Enter PIN (or type 'exit'): ");
            String inputPin = sc.nextLine();
            if (inputPin.equalsIgnoreCase("exit")) return false;

            if (!target.getPin().equals(inputPin)) {
                System.out.println("Incorrect PIN! Try again.");
            } else {
                return true;
            }
        }
    }

    private String getNextReservationID() {
        int max = 0;
        for (Reservation reservation : reservations) {
            int id = Integer.parseInt(reservation.getReservationID().replaceAll("[^0-9]", ""));
            if (id > max) max = id;
        }
        return "R" + String.format("%03d", max + 1);
    }
}
