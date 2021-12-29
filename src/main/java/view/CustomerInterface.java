package view;

import controller.*;
import model.AuthenticationManager;
import model.RideManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mohamed Ashraf
 * Provides interface for Customer Dashboard
 */
public class CustomerInterface implements UserInterface {

    private final Scanner scan = new Scanner(System.in);

    /**
     * Provides terminal interface for Customer Dashboard
     */
    @Override
    public void userDashboard() {
        System.out.println("1. Request ride");
        System.out.println("2. Rate most frequent driver");
        System.out.println("3. Show offers");
        System.out.println("4. Logout");
        int choice = Integer.parseInt(scan.next());
        switch (choice) {
            case 1 -> {
                System.out.print("Source : ");
                String src = scan.next();
                System.out.print("Destination : ");
                String dest = scan.next();
                System.out.println("Number of passengers : ");
                int numberOfPassengers = scan.nextInt();
                if (RideManager.getInstance().makeRequest(src, dest, (Customer) AuthenticationManager.getInstance().getCurrentAccount(), numberOfPassengers)) {
                    System.out.println("Drivers has been notified with your request");
                    System.out.println("Pending offers ...");
                } else {
                    System.out.println("Unexpected error occurred");
                }
            }
            case 2 -> {
                Driver lastDriver = RideManager.getInstance().getLastRideDriver((Customer) AuthenticationManager.getInstance().getCurrentAccount());
                if (lastDriver == null) return;
                System.out.print("Rating (Decimal value from 0 to 5) -> ");
                float rateValue = scan.nextFloat();
                lastDriver.rate((Customer) AuthenticationManager.getInstance().getCurrentAccount(), rateValue);
            }
            case 3 -> {
                // Filtering for current rides first
                ArrayList<Request> requests = (ArrayList<Request>) RideManager.getInstance().getRequests();
                int i = 0;
                while (requests.size() > 0 && i < requests.size()) {
                    if (requests.get(i).getAllOffers().get(i).isAccepted()) {
                        requests.remove(i);
                    } else i++;
                }
                int counter = 1;
                for (Request request : requests) {

                    System.out.print(counter);
                    System.out.println(". " + request.toString());
                    counter++;
                }
                System.out.println("Enter -1 to exit");
                System.out.print("-> ");
                int index = scan.nextInt();
                if (index == -1) return;
                Request currentRide = requests.get(index - 1);
                counter = 1;
                for (Offer offer : currentRide.getAllOffers()) {
                    System.out.print(counter);
                    System.out.println(". " + offer.toString());
                    counter++;
                }
                System.out.println("Enter -1 to exit");
                System.out.print("-> ");
                index = scan.nextInt();
                System.out.println("1. Accept Offer");
                System.out.println("2. Reject Offer");
                Offer currentOffer = currentRide.getAllOffers().get(index - 1);
                index = scan.nextInt();
                if (index == 1) currentOffer.accept();
                else if (index == 2) currentOffer.reject();
            }
            case 4 -> {
                AuthenticationManager.getInstance().logout();
            }
        }
    }
}
