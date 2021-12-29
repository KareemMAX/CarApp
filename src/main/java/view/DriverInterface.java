package view;

import controller.Driver;
import controller.Offer;
import controller.Rate;
import controller.Request;
import model.AuthenticationManager;
import model.RideManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Mohamed Ashraf
 * Provides interface for controller.Driver Dashboard
 */
public class DriverInterface implements UserInterface {
    private final Scanner scan = new Scanner(System.in);

    /**
     * Provides terminal interface for Driver Dashboard
     */
    @Override
    public void userDashboard() {
        Driver currentAccount = (Driver) AuthenticationManager.getInstance().getCurrentAccount();
        System.out.println("1. Add Favourite Area");
        System.out.println("2. Show Requests");
        System.out.println("3. Show Ratings");
        System.out.println("4. Show active rides");
        System.out.println("5. Show balance");
        System.out.println("6. Logout");
        int choice = scan.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.print("Area : ");
                String area = scan.next();
                currentAccount.addFavouriteArea(area);
            }
            case 2 -> {
                ArrayList<Request> requests = (ArrayList<Request>) RideManager.getInstance().getRequests();
                int counter = 1;
                for (Request request : requests) {
                    System.out.print(counter);
                    System.out.println(". " + request.toString());
                    counter++;
                }
                System.out.print("-> ");
                int rideChoice = scan.nextInt();
                Request selectedRide = null;
                try {
                    selectedRide = requests.get(rideChoice - 1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    return;
                }
                System.out.print("Offer Value (In Decimal) : ");
                float offerValue = scan.nextFloat();
                selectedRide.makerOffer(currentAccount, offerValue);
            }
            case 3 -> {
                ArrayList<Rate> rates = (ArrayList<Rate>) currentAccount.getRates();
                int counter = 1;
                for (Rate rate : rates) {
                    System.out.print(counter);
                    System.out.println(". " + rate.toString());
                    System.out.print("Average Rating : ");
                    System.out.println(currentAccount.getAverageRate());
                }
            }
            case 4 -> {
                List<Offer> activeOffers = currentAccount.getActiveOffers();
                int counter = 1;
                for (Offer offer :
                        activeOffers) {
                    System.out.print(counter);
                    System.out.println(". " + offer.toString());
                    counter++;
                }
                System.out.println("Choose a ride to drop the customer:");
                System.out.println("Enter -1 to exit");
                System.out.print("-> ");
                int index = scan.nextInt();
                if (index == -1)
                    return;

                Offer chosenOffer = activeOffers.get(index - 1);
                currentAccount.dropCustomer(chosenOffer);
            }
            case 5 -> {
                System.out.println("Balance: " + currentAccount.getBalance());
            }
            case 6 -> {
                AuthenticationManager.getInstance().logout();
            }
        }

    }
}
