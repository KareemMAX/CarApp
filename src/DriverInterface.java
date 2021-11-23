import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides interface for Driver Dashboard
 * */
public class DriverInterface implements UserInterface{
    private Scanner scan = new Scanner(System.in);
    /**
     * Provides terminal interface for Driver Dashboard
     * */
    @Override
    public void userDashboard() {
        Driver currentAccount = (Driver) AuthenticationManager.getInstance().getCurrentAccount();
        System.out.println("1. Add Favourite Area");
        System.out.println("2. Show Requests");
        System.out.println("3. Show Ratings");
        System.out.println("4. Logout");
        int choice = scan.nextInt();
        switch (choice){
            case 1 -> {
                System.out.print("Area : ");
                String area = scan.nextLine();
                currentAccount.addFavouriteArea(area);
            }
            case 2 -> {
                ArrayList<Request> requests = (ArrayList<Request>) RideManager.getInstance().getRequests();
                int counter = 1;
                for (Request request: requests){
                    System.out.print(counter);
                    System.out.println(". " + request.toString());
                }
                System.out.println("-> ");
                int rideChoice = scan.nextInt();
                Request selectedRide = null;
                try{
                    selectedRide = requests.get(rideChoice - 1);
                }
                catch (ArrayIndexOutOfBoundsException e){
                    return;
                }
                System.out.println("Offer Value (In Decimal) : ");
                float offerValue = scan.nextFloat();
                selectedRide.makerOffer(currentAccount, offerValue);
            }
            case 3 -> {
                ArrayList <Rate> rates = (ArrayList<Rate>) currentAccount.getRates();
                int counter = 1;
                for (Rate rate : rates){
                    System.out.print(counter);
                    System.out.println(". " + rate.toString());
                    System.out.print("Average Rating : ");
                    System.out.println(currentAccount.getAverageRate());
                }
            }
            case 4 -> {
                AuthenticationManager.getInstance().logout();
            }
        }
        //TODO
    }
}
