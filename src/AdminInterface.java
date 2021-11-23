import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides interface for Admin Dashboard
 * */
public class AdminInterface implements UserInterface {
    private Scanner scan = new Scanner(System.in);
    /**
     * Provides terminal interface for Admin Dashboard
     * */
    @Override
    public void userDashboard() {
        System.out.println("1. List all driver");
        System.out.println("2. List all Customers");
        int choice = scan.nextInt();
        switch (choice){
            case 1 -> {
                ArrayList<Driver> drivers = AccountManager.getInstance().getAllAccounts();
                int counter = 1;
                for (Driver driver : drivers){
                    System.out.print(counter);
                    System.out.println(". " + driver.toString());
                    counter++;
                }
                System.out.println("-> ");
                int input = scan.nextInt();
                drivers.get(input - 1).setSuspended(!drivers.get(input - 1).suspended);
            }
            case 2 -> {
                ArrayList<Customer> customers = AccountManager.getInstance().getAllAccounts();
                int counter = 1;
                for (Customer customer : customers){
                    System.out.print(counter);
                    System.out.println(". " + customer.toString());
                    counter++;
                }
                System.out.println("-> ");
                int input = scan.nextInt();
                customers.get(input - 1).setSuspended(!customers.get(input - 1).suspended);
            }
        }
    }
}
