package view;

import controller.Customer;
import controller.Driver;
import model.AccountManager;
import model.AuthenticationManager;
import view.UserInterface;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Mohamed Ashraf
 * Provides interface for controller.Admin Dashboard
 */
public class AdminInterface implements UserInterface {
    private final Scanner scan = new Scanner(System.in);

    /**
     * Provides terminal interface for controller.Admin Dashboard
     */
    @Override
    public void userDashboard() {
        System.out.println("1. List all driver");
        System.out.println("2. List all Customers");
        System.out.println("3. logout");
        int choice = scan.nextInt();
        switch (choice) {
            case 1 -> {
                ArrayList<Driver> drivers = AccountManager.getInstance().getAllAccounts("controller.Driver");
                int counter = 1;
                for (Driver driver : drivers) {
                    System.out.print(counter);
                    System.out.println(". " + driver.toString());
                    counter++;
                }
                System.out.println("Enter -1 to return");
                System.out.print("-> ");
                int index = scan.nextInt();
                if (index == -1) return;
                System.out.println("1. Verify");
                System.out.println("2. Toggle suspension state");
                System.out.println("3. Return");
                int input = scan.nextInt();
                switch (input) {
                    case 1 -> {
                        drivers.get(index - 1).setVerified(true);
                    }
                    case 2 -> {
                        drivers.get(index - 1).setSuspended(!drivers.get(index - 1).isSuspended());
                    }
                }
            }
            case 2 -> {
                ArrayList<Customer> customers = AccountManager.getInstance().getAllAccounts("Customer");
                int counter = 1;
                for (Customer customer : customers) {
                    System.out.print(counter);
                    System.out.println(". " + customer.toString());
                    counter++;
                }
                System.out.println("Enter -1 to return");
                System.out.print("-> ");
                int index = scan.nextInt();
                if (index == -1) return;
                System.out.println("1. Toggle suspension state");
                System.out.println("2. Return");
                int input = scan.nextInt();
                if (input == 1) customers.get(index - 1).setSuspended(!customers.get(index - 1).isSuspended());
            }
            case 3 -> {
                AuthenticationManager.getInstance().logout();
            }
        }
    }
}
