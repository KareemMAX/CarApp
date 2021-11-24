import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mohamed Ashraf
 * Provides interface for Customer Dashboard
 * */
public class CustomerInterface implements UserInterface {

    private Scanner scan = new Scanner(System.in);

    /**
     * Provides terminal interface for Customer Dashboard
     * */
    @Override
    public void userDashboard() {
        System.out.println("1. Request ride");
        System.out.println("2. Rate most frequent driver");
        System.out.println("3. Show offers");
        System.out.println("4. Logout");
        int choice = Integer.parseInt(scan.next());
        switch (choice){
            case 1 -> {
                System.out.print("Source : ");
                String src = scan.next();
                System.out.print("Destination : ");
                String dest = scan.next();
                if (RideManager.getInstance().makeRequest(src, dest, (Customer) AuthenticationManager.getInstance().getCurrentAccount())){
                    System.out.println("Drivers has been notified with your request");
                    System.out.println("Pending offers ...");
                }
                else {
                    System.out.println("Unexpected error occurred");
                }
            }
            case 2 -> {
                //TODO Query past rides
                Driver lastDriver = RideManager.getInstance().getLastRideDriver((Customer) AuthenticationManager.getInstance().getCurrentAccount());
                System.out.print("Rating (Decimal value from 0 to 5) -> ");
                float rateValue = scan.nextFloat();
                lastDriver.rate((Customer) AuthenticationManager.getInstance().getCurrentAccount(), rateValue);
            }
            case 3 -> {
                //TODO get current offers
            }
            case 4 -> {
                AuthenticationManager.getInstance().logout();
            }
        }
    }
}
