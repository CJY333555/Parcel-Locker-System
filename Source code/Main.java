import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories
        AdminRepository adminRepository = new AdminRepository("Admin.txt");
        ClientRepository clientRepository = new ClientRepository("Client.txt");
        HubRepository hubRepository = new HubRepository("Hub.txt");
        LockerRepository lockerRepository = new LockerRepository("Locker.txt");
        ParcelRepository parcelRepository = new ParcelRepository("Parcel.txt");
        ReservationRepository reservationRepository = new ReservationRepository("Reservation.txt");

        // Load data
        List<Admin> admins = adminRepository.loadAll();
        List<Client> clients = clientRepository.loadAll();
        List<Hub> hubs = hubRepository.loadAll();
        List<Locker> lockers = lockerRepository.loadAll();
        List<Parcel> parcels = parcelRepository.loadAll();
        List<Reservation> reservations = reservationRepository.loadAll();

        // Initialize services
        AuthenticationService authService = new AuthenticationService(admins, clients);
        LockerService lockerService = new LockerService(lockers, hubs, lockerRepository);
        ReservationService reservationService = new ReservationService(reservations, parcels, hubs, 
                                                                       reservationRepository, parcelRepository, lockerService);
        
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Contactless Parcel Locker System ---");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Client");
            System.out.println("3. Register as Client");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter Admin ID: ");
                String id = sc.nextLine();
                System.out.print("Enter Password: ");
                String pass = sc.nextLine();

                Admin admin = authService.authenticateAdmin(id, pass);
                if (admin != null) {
                    System.out.println("Welcome Admin " + admin.getName());
                        while (true) {
                            System.out.println("\n--- Admin Menu ---");
                            System.out.println("1. View Reservations");
                            System.out.println("2. Cancel Reservation");
                            System.out.println("3. Check Locker");
                            System.out.println("4. Add Locker");
                            System.out.println("5. Delete Locker");
                            System.out.println("6. Logout");
                            System.out.print("Choose: ");
                            String adChoice = sc.nextLine();

                            if (adChoice.equals("1")) {
                                reservationService.viewAllReservations();
                            } else if (adChoice.equals("2")) {
                                System.out.print("Enter Reservation ID to cancel: ");
                                reservationService.cancelReservation(sc.nextLine());
                            } else if (adChoice.equals("3")) {
                                lockerService.checkLockers();
                            } else if (adChoice.equals("4")) {
                                System.out.print("Enter Hub ID: ");
                                String hub = sc.nextLine().toUpperCase();
                                System.out.print("Enter Locker ID (or 'auto'): ");
                                String lid = sc.nextLine().toUpperCase();
                                System.out.print("Enter Compartment ID (or 'auto'): ");
                                String cid = sc.nextLine().toUpperCase();
                                System.out.print("Enter Size (Small/Medium/Large): ");
                                String size = sc.nextLine();
                                lockerService.addLocker(hub, lid, cid, size);
                            } else if (adChoice.equals("5")) {
                            	System.out.print("Enter Hub ID: ");
                            	String hub = sc.nextLine().toUpperCase();
                            	System.out.print("Enter Locker ID: ");
                            	String lid = sc.nextLine();
                            	lockerService.deleteLocker(hub, lid);
                            	
                            } else if (adChoice.equals("6")) break;
                            else System.out.println("Invalid choice.");
                        }
                } else {
                    System.out.println("Invalid Admin login.");
                }

            } else if (choice.equals("2")) {
                System.out.print("Enter User ID: ");
                String id = sc.nextLine();
                System.out.print("Enter Password: ");
                String pass = sc.nextLine();

                Client client = authService.authenticateClient(id, pass);
                if (client != null) {
                    System.out.println("Welcome " + client.getName());
                        while (true) {
                            System.out.println("\n--- Client Menu ---");
                            System.out.println("1. View Your Reservation");
                            System.out.println("2. Make Reservation");
                            System.out.println("3. DropOff Parcel");
                            System.out.println("4. Pickup Parcel");
                            System.out.println("5. Logout");
                            System.out.print("Choose: ");
                            String clChoice = sc.nextLine();

                            if (clChoice.equals("1")) reservationService.viewUserReservations(client.getUserID());
                            else if (clChoice.equals("2")) reservationService.makeReservation(client.getUserID());
                            else if (clChoice.equals("3")) {
                                System.out.print("Enter Reservation ID: ");
                                reservationService.dropOffParcel(sc.nextLine());
                            }
                            else if (clChoice.equals("4")) {
                                System.out.print("Enter Reservation ID: ");
                                reservationService.pickupParcel(sc.nextLine(), client.getUserID());
                            } else if (clChoice.equals("5")) break;
                            else System.out.println("Invalid choice.");
                        }
                } else {
                    System.out.println("Invalid Client login.");
                }
                
                
            } else if(choice.equals("3")) {
                System.out.print("Enter Username: ");
                String r_name = sc.nextLine();

                while (true) {
                    System.out.print("Enter Email: ");
                    String r_email = sc.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String r_phone = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String r_pass = sc.nextLine();

                    if (r_pass.length() != 4) {
                        System.out.print("Password are not 4 numbers or characters\n");
                        continue;
                    }

                    // Check if email or phone already exists
                    if (authService.isEmailExists(r_email) || authService.isPhoneExists(r_phone)) {
                        System.out.println("A personal info exists, please try again!");
                        // loop continues to re-enter email/phone/password
                        break;
                    }

                    String userID = authService.generateNextUserID();

                    // add new client
                    Client newClient = new Client(userID, r_name, r_email, r_phone, r_pass);
                    clients.add(newClient);

                    // save in file
                    clientRepository.saveAll(clients);

                    System.out.println("Register Successfully! Your UserID is " + userID);
                    break;
                }
                

            } else if (choice.equals("4")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }
}
