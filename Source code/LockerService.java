import java.util.List;
import java.util.Iterator;

public class LockerService {
    private List<Locker> lockers;
    private List<Hub> hubs;
    private LockerRepository lockerRepository;

    public LockerService(List<Locker> lockers, List<Hub> hubs, LockerRepository lockerRepository) {
        this.lockers = lockers;
        this.hubs = hubs;
        this.lockerRepository = lockerRepository;
    }

    public void checkLockers() {
        System.out.println("\n=== Locker Status ===");
        if (lockers.isEmpty()) {
            System.out.println("No lockers found.");
            return;
        }

        for (Locker locker : lockers) {
            System.out.println("Hub: " + locker.getHubID()
                             + " | Locker: " + locker.getLockerID()
                             + " | Compartment: " + locker.getCompartmentID()
                             + " | Size: " + locker.getSize()
                             + " | Status: " + locker.getStatus());
        }
        System.out.println("======================\n");
    }

    public void addLocker(String hubID, String lockerID, String compartmentID, String size) {
        // Validate hub ID exists
        if (!isHubExists(hubID)) {
            System.out.println("Error: Hub ID '" + hubID + "' does not exist. Cannot add locker.");
            return;
        }

        // Auto-generate LockerID if 'auto'
        if (lockerID.equalsIgnoreCase("auto")) {
            lockerID = generateNextLockerID();
        }

        // Auto-generate CompartmentID if 'auto'
        if (compartmentID.equalsIgnoreCase("auto")) {
            compartmentID = generateNextCompartmentID();
        }

        // Standardize size case (capitalize first letter)
        String standardizedSize = size.substring(0, 1).toUpperCase() + size.substring(1).toLowerCase();

        // Create new locker with default status = "Available"
        Locker newLocker = new Locker(hubID, lockerID, compartmentID, standardizedSize, "Available");
        lockers.add(newLocker);

        // Save to file
        lockerRepository.saveAll(lockers);

        System.out.println("Locker added successfully: " 
            + hubID + " | " + lockerID + " | " + compartmentID + " | " + standardizedSize);
    }

    public void deleteLocker(String hubID, String lockerID) {
        // Validate hub ID exists
        if (!isHubExists(hubID)) {
            System.out.println("Error: Hub ID '" + hubID + "' does not exist. Cannot delete locker.");
            return;
        }

        boolean removed = false;

        Iterator<Locker> it = lockers.iterator();
        while (it.hasNext()) {
            Locker locker = it.next();
            if (locker.getHubID().equalsIgnoreCase(hubID) && locker.getLockerID().equalsIgnoreCase(lockerID)) {
                it.remove();   // safely remove from list
                removed = true;
                break;
            }
        }

        if (removed) {
            lockerRepository.saveAll(lockers);
            System.out.println("Locker " + lockerID + " from Hub " + hubID + " deleted successfully.");
        } else {
            System.out.println("Locker not found.");
        }
    }

    public Locker findLocker(String hubID, String lockerID, String compartmentID) {
        for (Locker locker : lockers) {
            if (locker.getHubID().equalsIgnoreCase(hubID) 
                && locker.getLockerID().equalsIgnoreCase(lockerID)
                && locker.getCompartmentID().equalsIgnoreCase(compartmentID)) {
                return locker;
            }
        }
        return null;
    }

    public List<Locker> getAvailableLockers(String hubID, String size) {
        return lockers.stream()
            .filter(locker -> locker.getHubID().equalsIgnoreCase(hubID)
                && locker.getSize().equalsIgnoreCase(size)
                && locker.getStatus().equalsIgnoreCase("Available"))
            .collect(java.util.stream.Collectors.toList());
    }

    public void updateLockerStatus(String hubID, String lockerID, String compartmentID, String status) {
        Locker locker = findLocker(hubID, lockerID, compartmentID);
        if (locker != null) {
            locker.setStatus(status);
            lockerRepository.saveAll(lockers);
        }
    }

    public void updateLockerStatusByLockerAndCompartment(String lockerID, String compartmentID, String status) {
        for (Locker locker : lockers) {
            if (locker.getLockerID().equals(lockerID) && locker.getCompartmentID().equals(compartmentID)) {
                locker.setStatus(status);
                lockerRepository.saveAll(lockers);
                break;
            }
        }
    }

    private String generateNextLockerID() {
        int max = 0;
        for (Locker locker : lockers) {
            try {
                int id = Integer.parseInt(locker.getLockerID().replaceAll("[^0-9]", ""));
                if (id > max) max = id;
            } catch (NumberFormatException e) {
                // ignore if lockerID is not numeric
            }
        }
        return "L" + String.format("%03d", max + 1);
    }

    private String generateNextCompartmentID() {
        int max = 0;
        for (Locker locker : lockers) {
            try {
                int id = Integer.parseInt(locker.getCompartmentID().replaceAll("[^0-9]", ""));
                if (id > max) max = id;
            } catch (NumberFormatException e) {
                // ignore if compartmentID is not numeric
            }
        }
        return "C" + String.format("%03d", max + 1);
    }

    private boolean isHubExists(String hubID) {
        for (Hub hub : hubs) {
            if (hub.getHubID().equalsIgnoreCase(hubID)) {
                return true;
            }
        }
        return false;
    }
}
