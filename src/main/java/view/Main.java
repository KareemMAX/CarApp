package view;

import controller.Customer;
import model.AuthenticationManager;

import controller.Driver;

import java.sql.Date;
import java.util.Scanner;

/**
 * Entry Class
 *
 * @version 1.00 2021/11/22
 * Course: Advanced Software Engineering 2021/2022 - Assignment 1 - Sprint 1
 * @Author Mohamed Ashraf
 */
public class Main {
    private static final Scanner scan = new Scanner(System.in);

    /**
     * Entry Function
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("--- Car App ---");
            System.out.println("---------------");
            if (AuthenticationManager.getInstance().getCurrentAccount() == null) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                int choice = scan.nextInt();
                switch (choice) {
                    case 1 -> {
                        loginInterface();
                    }
                    case 2 -> {
                        registerInterface();
                    }
                    case 3 -> {
                        scan.close();
                        return;
                    }
                }
            } else {
                AuthenticationManager.getInstance().getCurrentAccount().userInterface.userDashboard();
            }
        }
    }

    /**
     * Provides a terminal interface for login behaviour
     */
    public static void loginInterface() {
        System.out.print("Username : ");
        String user = scan.next();
        System.out.print("Password : ");
        String password = scan.next();
        if (AuthenticationManager.getInstance().login(user, password)) {
            System.out.println("Welcome " + AuthenticationManager.getInstance().getCurrentAccount().getUserName());
        } else {
            System.out.println("Invalid username and/or password");
        }
    }

    /**
     * Provides a terminal interface for register behaviour
     */
    public static void registerInterface() {
        System.out.println("Would you like to register as a ");
        System.out.println("1. Customer");
        System.out.println("2. Driver");
        System.out.println("3. Return");
        int choice = scan.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.print("Username : ");
                String user = scan.next();
                System.out.print("Password : ");
                String password = scan.next();
                System.out.print("Phone Number : ");
                String phoneNumber = scan.next();
                System.out.print("Would you like to add an email ? (Y/N) -> ");
                String email = null;
                if (scan.next().equalsIgnoreCase("y")) {
                    System.out.print("Email : ");
                    email = scan.next();
                }
                System.out.print("Enter your birthdate in this format YYYY-MM-DD : ");
                Date birthday = Date.valueOf(scan.next());
                if (email == null) {
                    if (AuthenticationManager.getInstance().register(new Customer(user, password, phoneNumber, birthday)))
                        System.out.println("Registration Complete!");
                    else System.out.println("Something went wrong");
                } else if (AuthenticationManager.getInstance().register(new Customer(user, password, phoneNumber, email, birthday))) {
                    System.out.println("Registration Complete!");
                } else System.out.println("Something went wrong");

            }
            case 2 -> {
                System.out.print("Username : ");
                String user = scan.next();
                System.out.print("Password : ");
                String password = scan.next();
                System.out.print("Phone Number : ");
                String phoneNumber = scan.next();
                System.out.print("Would you like to add an email ? (Y/N) -> ");
                String email = null;
                if (scan.next().equalsIgnoreCase("y")) {
                    System.out.print("Email : ");
                    email = scan.next();
                }
                System.out.print("Licence : ");
                String licence = scan.next();
                System.out.print("nationalId : ");
                String nationalId = scan.next();
                if (email == null) {
                    if (AuthenticationManager.getInstance().register(new Driver(user, password, phoneNumber, licence, nationalId)))
                        System.out.println("Registration Complete!");
                    else System.out.println("Something went wrong");

                } else if (AuthenticationManager.getInstance().register(new Driver(user, password, phoneNumber, licence, nationalId, email))) {
                    System.out.println("Registration Complete!");
                } else System.out.println("Something went wrong");
            }
            case 3 -> {
            }
        }
    }
}
