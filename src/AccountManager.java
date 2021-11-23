import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountManager {
    private static AccountManager singletonInstance;
    private final Database db =Database.getInstance();

    public static AccountManager getInstance(){
        if (singletonInstance == null) singletonInstance = new AccountManager();
        return singletonInstance;
    }

    public <T extends Account>  ArrayList<T> getAllAccounts(String type){

        if (type.equalsIgnoreCase("customer"))  //get all customers
        {
            ArrayList<Customer> result = new ArrayList<Customer>();
            ResultSet table;
            table = db.query("SELECT * FROM customer");

            try
            {
                while (table.next())
                {
                    Customer current = new Customer(table.getString("username"),
                                                    table.getString("password"),
                                                    table.getString("email"),
                                                    table.getString("phone_number"));
                    current.setSuspended(table.getBoolean("suspended"));
                    result.add(current);
                }
            }
            catch (java.sql.SQLException e)
            {
                System.out.println("No Customers");
            }
            return (ArrayList<T>) result;
        }
        else
        {
            ArrayList<Driver> result = new ArrayList<Driver>();
            ResultSet table;
            table = db.query("SELECT * FROM driver");

            try
            {
                while (table.next())
                {
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
            }
            catch (java.sql.SQLException e)
            {
                System.out.println("No Drivers");
            }
            return (ArrayList<T>) result;
        }
    }

    public Account getAccount(String username) {
        try
        {
            Account target;
            ResultSet table = db.query("SELECT * FROM driver WHERE username = '"+username+"'");
            int size=0;
            if (table != null)
            {
                table.last();    // moves cursor to the last row
                size = table.getRow(); // get row id
            }
            if (size>0)
            {
                table.next();
                target = new Driver(table.getString("username"),
                                    table.getString("password"),
                                    table.getString("email"),
                                    table.getString("phone"),
                                    table.getString("national_id"),
                                    table.getString("license"));
                target.setSuspended(table.getBoolean("suspended"));
                ((Driver) target).setVerified(table.getBoolean("verified"));
            }
            else
            {
                table = db.query("SELECT * FROM customer WHERE username = '"+username+"'");
                if (table != null)
                {
                    table.last();    // moves cursor to the last row
                    size = table.getRow(); // get row id
                }
                if (size > 0)
                {
                    table.next();
                    target = new Customer(table.getString("username"),
                            table.getString("password"),
                            table.getString("email"),
                            table.getString("phone_number"));
                    target.setSuspended(table.getBoolean("suspended"));
                }
                else
                {
                  throw new java.sql.SQLException();
                }
            }

            if (target!= null)
                return target;
            else throw new java.sql.SQLException();

        }
        catch (java.sql.SQLException exception)
        {
            System.out.println("User not found");
            return null;
        }

    }

    public ArrayList<Account> getSuspendedAccounts(){

        ArrayList<Account> result = new ArrayList<Account>();
        ResultSet table;
        table = db.query("SELECT * FROM customer WHERE suspended = 'true'");
        try
        {
            while (table.next())
            {
                Customer current = new Customer(table.getString("username"),
                                                table.getString("password"),
                                                table.getString("email"),
                                                table.getString("phone_number"));
                current.setSuspended(table.getBoolean("suspended"));
                result.add(current);
            }

            table = db.query("SELECT * FROM driver WHERE suspended = 'true'");
            while (table.next())
            {
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
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("SQL ERROR");
        }

        return result;
    }

    public <T extends Account> void updateAccount(T acc){
        //TODO
    }
}
