package model;

import controller.Account;
import controller.Admin;
import controller.Customer;
import controller.Driver;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class that controls and provides the available accounts in the database
 *
 * @author Khaled Waleed
 */
public class AccountManager {
    private static AccountManager singletonInstance;
    private final Database db = Database.getInstance();
    private final CustomerModel customerModel = CustomerModel.getInstance();
    private final DriverModel driverModel = DriverModel.getInstance();
    private final AdminModel adminModel = AdminModel.getInstance();

    private AccountManager() {}

    /**
     * Gets a singleton instance
     *
     * @return A {@link AccountManager} instance
     */
    public static AccountManager getInstance() {
        if (singletonInstance == null) singletonInstance = new AccountManager();
        return singletonInstance;
    }

    /**
     * Gets all the accounts available of a specific class
     *
     * @param type The account type name as a string, either {@code "customer"}, or {@code "driver"}.
     * @param <T>  The type of the class to cast to
     * @return A list of objects representing the account class
     */
    public <T extends Account> List<T> getAllAccounts(String type) {
        if (type.equalsIgnoreCase("customer"))  //get all customers
        {
            return (ArrayList<T>) customerModel.getAllAccounts();
        } else if (type.equalsIgnoreCase("driver")) {
            return (ArrayList<T>) driverModel.getAllAccounts();
        }
        else if (type.equalsIgnoreCase("admin")) {
            return (ArrayList<T>) adminModel.getAllAccounts();
        }
        return null;
    }

    /**
     * Gets a single account object from a username
     *
     * @param username The username of the desired account
     * @return The account object of the specific username
     */
    public Account getAccount(String username) {
        Customer customer = customerModel.getAccount(username);
        if (customer != null)
            return customer;

        Driver driver = driverModel.getAccount(username);
        if (driver != null)
            return driver;

        return adminModel.getAccount(username);
    }

    /**
     * Get a list of suspended accounts
     *
     * @return a list of accounts
     */
    public ArrayList<Account> getSuspendedAccounts() {

        ArrayList<Account> result = new ArrayList<Account>();
        ResultSet table;
        table = db.query("SELECT * FROM [customer] INNER JOIN [account] ON customer.username = account.username WHERE suspended = 'true'");
        try {
            while (table.next()) {
                Customer current = new Customer(table.getString("username"),
                        table.getString("password"),
                        table.getString("phone_number"),
                        table.getString("email"),
                        table.getDate("birthday")
                        );
                current.setSuspended(table.getBoolean("suspended"));
                result.add(current);
            }

            table = db.query("SELECT * FROM [driver] INNER JOIN [account] ON driver.username = account.username WHERE suspended = 'true'");
            while (table.next()) {
                Driver current = new Driver(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phoneNumber"),
                        table.getString("national_id"),
                        table.getString("license"));
                current.setSuspended(table.getBoolean("suspended"));
                current.setVerified(table.getBoolean("verified"));
                result.add(current);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("SQL ERROR");
        }

        return result;
    }

    /**
     * Updates account in the database
     *
     * @param account controller.Account to be updated
     */
    public void updateAccount(Account account) {
        if(account instanceof Customer customer) {
            customerModel.updateAccount(customer);
        }
        else if (account instanceof Driver driver) {
            driverModel.updateAccount(driver);
        }
        else if (account instanceof Admin admin) {
            adminModel.updateAccount(admin);
        }
    }
    public void addFavouriteArea(Driver driver,String area){
        db.update("INSERT INTO [favouriteAreas] values('"+area+"','"+driver.getUserName()+"')");
    }
}
