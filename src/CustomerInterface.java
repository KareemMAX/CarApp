import java.util.Scanner;

/**
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
        System.out.println("3. Logout");
        int choice = scan.nextInt();
        switch (choice){
            case 1 -> {
                System.out.print("Source : ");
                String src = scan.nextLine();
                System.out.print("Destination : ");
                String dest = scan.nextLine();
                if (RideManager.getInstance().makeRequest(src, dest, (Customer) AuthenticationManager.getInstance().getcurrentAccount())){
                    System.out.println("Drivers has been notified with your request");
                    System.out.println("Pending offers ...");
                }
                else {
                    System.out.println("Unexpected error occurred");
                }
            }
            case 2 -> {
                //TODO Query past rides
                Driver lastDriver = RideManager.getInstance().getLastRideDriver((Customer) AuthenticationManager.getInstance().getcurrentAccount());
                System.out.print("Rating (Decimal value from 0 to 5) -> ");
                float rateValue = scan.nextFloat();
                lastDriver.rate((Customer) AuthenticationManager.getInstance().getcurrentAccount(), rateValue);
            }
            case 3 -> {
                AuthenticationManager.getInstance().logout();
            }
        }
    }
}
