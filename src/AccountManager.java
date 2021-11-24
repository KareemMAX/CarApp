import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Manager class that controls and provides the available accounts in the database
 *
 * @author Khaled Waleed
 */
public class AccountManager {
    private static AccountManager singletonInstance;
    private final Database db = Database.getInstance();

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
    public <T extends Account> ArrayList<T> getAllAccounts(String type) {

        if (type.equalsIgnoreCase("customer"))  //get all customers
        {
            ArrayList<Customer> result = new ArrayList<Customer>();
            ResultSet table;
            table = db.query("SELECT * FROM customer");

            try {
                while (table.next()) {
                    Customer current = new Customer(table.getString("username"),
                            table.getString("password"),
                            table.getString("email"),
                            table.getString("phone_number"));
                    current.setSuspended(table.getBoolean("suspended"));
                    result.add(current);
                }
            } catch (java.sql.SQLException e) {
                System.out.println("No Customers");
            }
            return (ArrayList<T>) result;
        } else {
            ArrayList<Driver> result = new ArrayList<Driver>();
            ResultSet table;
            table = db.query("SELECT * FROM driver");

            try {
                while (table.next()) {
                    Driver current = new Driver(table.getString("username"),
                            table.getString("password"),
                            table.getString("email"),
                            table.getString("phonenumber"),
                            table.getString("national_id"),
                            table.getString("license"));
                    current.setSuspended(table.getBoolean("suspended"));
                    current.setVerified(table.getBoolean("verified"));
                    result.add(current);
                }
            } catch (java.sql.SQLException e) {
                System.out.println("No Drivers");
            }
            return (ArrayList<T>) result;
        }
    }

    /**
     * Gets a single account object from a username
     *
     * @param username The username of the desired account
     * @return The account object of the specific username
     */
    public Account getAccount(String username) {
        try {
            Account target;
            ResultSet table = db.query("SELECT * FROM driver WHERE username = '" + username + "'");
            int size = 0;
            while (table.next()) {
                size++;
            }
            if (size != 0) {
                table = db.query("SELECT * FROM driver WHERE username = '" + username + "'");
                table.next();
                target = new Driver(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phonenumber"),
                        table.getString("national_id"),
                        table.getString("license"));
                target.setSuspended(table.getBoolean("suspended"));
                ((Driver) target).setVerified(table.getBoolean("verified"));
            } else {
                table = db.query("SELECT * FROM customer WHERE username = '" + username + "'");
                size = 0;
                while (table.next()) {
                    size++;
                }
                if (size != 0) {
                    table = db.query("SELECT * FROM customer WHERE username = '" + username + "'");
                    table.next();
                    target = new Customer(table.getString("username"),
                            table.getString("password"),
                            table.getString("email"),
                            table.getString("phone_number"));
                    target.setSuspended(table.getBoolean("suspended"));
                } else {
                    table = db.query("SELECT * FROM admin WHERE username = '" + username + "'");
                    size = 0;
                    while (table.next()) {
                        size++;
                    }
                    if (size != 0) {
                        table = db.query("SELECT * FROM admin WHERE username = '" + username + "'");
                        table.next();
                        target = new Admin(table.getString("username"),
                                table.getString("password"));
                        target.setSuspended(table.getBoolean("suspended"));
                    } else
                        throw new java.sql.SQLException();
                }
            }

            if (target != null) {
                return target;
            } else throw new java.sql.SQLException();

        } catch (java.sql.SQLException exception) {
            System.out.println("User not found");
            return null;
        }

    }

    /**
     * Get a list of suspended accounts
     *
     * @return a list of accounts
     */
    public ArrayList<Account> getSuspendedAccounts() {

        ArrayList<Account> result = new ArrayList<Account>();
        ResultSet table;
        table = db.query("SELECT * FROM customer WHERE suspended = 'true'");
        try {
            while (table.next()) {
                Customer current = new Customer(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phone_number"));
                current.setSuspended(table.getBoolean("suspended"));
                result.add(current);
            }

            table = db.query("SELECT * FROM driver WHERE suspended = 'true'");
            while (table.next()) {
                Driver current = new Driver(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phone"),
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
     * @param account Account to be updated
     */
    public void updateAccount(Account account) {
        if(account instanceof Customer customer) {
            if (customer.isSuspended())
                db.update("UPDATE customer\n"+
                        "SET username= '"+customer.getUserName()+"', password= '"+customer.password+"', email= '"+customer.getEmail()+"', phone_number= '"+customer.getPhoneNumber()+"'"
                        + ", suspended= 'false'" );

            else
                db.update("UPDATE customer\n"+
                        "SET username= '"+customer.getUserName()+"', password= '"+customer.password+"', email= '"+customer.getEmail()+"', phone_number= '"+customer.getPhoneNumber()+"'"
                        + ", suspended= 'true'" );
        }
        else if (account instanceof Driver driver) {
            String query = "UPDATE driver\n"+
                    "SET username= '"+ driver.getUserName()+"', password= '"+driver.password+"', email= '"+ driver.getEmail()+"', phonenumber= '"+driver.getPhoneNumber()+"'"
                    +", national_id= '"+driver.getNationalID()+"', license= '"+driver.getNationalID()+"'";
            if (driver.isSuspended())
                query+= ", suspended= 'true'";
            else query+= ", suspended= 'false'";

            if (driver.isVerified())
                query+= ", verified= 'true'";
            else query+= ", verified= 'false'";

            db.update(query);
        }
    }
}
