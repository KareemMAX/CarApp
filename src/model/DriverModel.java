package model;

import controller.Customer;
import controller.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverModel  extends AccountModel<Driver>{
    private static DriverModel singletonInstance;
    private final Database db = Database.getInstance();

    private DriverModel() {}

    /**
     * Gets a singleton instance
     *
     * @return A {@link CustomerModel} instance
     */
    public static DriverModel getInstance() {
        if (singletonInstance == null) singletonInstance = new DriverModel();
        return singletonInstance;
    }

    @Override
    List<Driver> getAllAccounts() {
        ArrayList<Driver> result = new ArrayList<Driver>();
        ResultSet table;
        table = db.query("SELECT * FROM [driver] INNER JOIN [account] ON [driver].[username] = [account].[username]");

        try {
            while (table.next()) {
                Driver current = new Driver(table.getString("username"),
                        table.getString("password"),
                        table.getString("email"),
                        table.getString("phoneNumber"),
                        table.getString("national_id"),
                        table.getString("license"),
                        table.getBoolean("suspended"),
                        table.getBoolean("verified")
                );
                result.add(current);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("No Drivers");
        }

        return result;
    }

    @Override
    Driver getAccount(String username) {
        ResultSet table = db.query("SELECT * FROM [driver] INNER JOIN [account] ON driver.username = account.username WHERE account.username = '" + username + "'");
        try {
            if(!table.next()) return null;

            return new Driver(
                    table.getString("username"),
                    table.getString("password"),
                    table.getString("email"),
                    table.getString("phoneNumber"),
                    table.getString("national_id"),
                    table.getString("license"),
                    table.getBoolean("suspended"),
                    table.getBoolean("verified")
            );
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    void updateAccount(Driver account) {
        String query = "UPDATE driver\n"+
                "SET email= '" + account.getEmail()+"', phoneNumber= '"+account.getPhoneNumber()+"'"
                +", national_id= '"+account.getNationalID()+"', license= '"+account.getNationalID()+"'";
        if (account.isSuspended())
            query+= ", suspended= 'true'";
        else query+= ", suspended= 'false'";

        if (account.isVerified())
            query+= ", verified= 'true'";
        else query+= ", verified= 'false'";
        query+=" WHERE username= '"+account.getUserName()+"'";

        db.update(query);
    }

    /**
     * Adds a favourite area for the driver
     * @param driver The driver
     * @param area The area to be added
     */
    public void addFavouriteArea(Driver driver, String area) {
        db.update("INSERT INTO [favouriteAreas] values('"+area+"','"+driver.getUserName()+"')");
    }
}
