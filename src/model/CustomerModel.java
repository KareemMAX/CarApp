package model;

import controller.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel extends AccountModel<Customer> {
    private static CustomerModel singletonInstance;
    private final Database db = Database.getInstance();

    private CustomerModel() {}

    /**
     * Gets a singleton instance
     *
     * @return A {@link CustomerModel} instance
     */
    public static CustomerModel getInstance() {
        if (singletonInstance == null) singletonInstance = new CustomerModel();
        return singletonInstance;
    }

    @Override
    List<Customer> getAllAccounts() {
        ArrayList<Customer> result = new ArrayList<Customer>();
        ResultSet table;
        table = db.query("SELECT * FROM [customer] INNER JOIN [account] ON [customer].[username] = [account].[username]");

        try {
            while (table.next()) {
                Customer current = new Customer(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phoneNumber"),
                        table.getDate("birthday"),
                        table.getBoolean("suspended")
                );
                result.add(current);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("No Customers");
        }
        return result;
    }

    @Override
    Customer getAccount(String username) {
        ResultSet table = db.query("SELECT * FROM [customer] INNER JOIN [account] ON [customer].[username] = [account].[username] WHERE [customer].[username] = '" + username + "'");
        try {
            if(!table.next()) return null;

            return new Customer(
                    table.getString("username"),
                    table.getString("password"),
                    table.getString("phoneNumber"),
                    table.getString("email"),
                    table.getDate("birthday"),
                    table.getBoolean("suspended")
            );
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    void updateAccount(Customer account) {
        if (account.isSuspended())
            db.update("UPDATE customer\n"+
                    "SET email= '"  +account.getEmail() + "', phoneNumber= '"+account.getPhoneNumber()+"'"
                    + ", suspended= 'true' WHERE username= '"+account.getUserName()+"'");

        else
            db.update("UPDATE customer\n"+
                    "SET email= '"  +account.getEmail() + "', phoneNumber= '"+account.getPhoneNumber()+"'"
                    + ", suspended= 'false' WHERE username= '"+account.getUserName()+"'");
    }
}
