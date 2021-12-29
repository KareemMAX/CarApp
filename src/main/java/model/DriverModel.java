package model;

import controller.Customer;
import controller.Driver;
import controller.Rate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverModel extends AccountModel<Driver> {
    private static DriverModel singletonInstance;
    private final Database db = Database.getInstance();
    private final CustomerModel customerModel = CustomerModel.getInstance();

    private DriverModel() {
    }

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
                Driver current = createDriverFromResultSet(table);

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
            if (!table.next()) return null;

            return createDriverFromResultSet(table);
        } catch (SQLException ignored) {
        }
        return null;
    }

    private Driver createDriverFromResultSet(ResultSet table) throws SQLException {
        Driver driver = new Driver(
                table.getString("username"),
                table.getString("password"),
                table.getString("email"),
                table.getString("phoneNumber"),
                table.getString("national_id"),
                table.getString("license"),
                table.getBoolean("suspended"),
                table.getBoolean("verified")
        );
        initFavouriteAreasFromDB(driver);
        initRatesFromDB(driver);
        initActiveOffersFromDB(driver);
        return driver;
    }

    @Override
    void updateAccount(Driver account) {
        String query = "UPDATE driver\n" +
                "SET email= '" + account.getEmail() + "', phoneNumber= '" + account.getPhoneNumber() + "'"
                + ", national_id= '" + account.getNationalID() + "', license= '" + account.getNationalID() +
                "', balance= " + account.getBalance();

        if (account.isSuspended())
            query += ", suspended= 'true'";
        else
            query += ", suspended= 'false'";

        if (account.isVerified())
            query += ", verified= 'true'";
        else
            query += ", verified= 'false'";

        query += " WHERE username= '" + account.getUserName() + "'";

        db.update(query);
    }

    /**
     * Adds a favourite area for the driver
     *
     * @param driver The driver
     * @param area   The area to be added
     */
    public void addFavouriteArea(Driver driver, String area) {
        db.update("INSERT INTO [favouriteAreas] values('" + area + "','" + driver.getUserName() + "')");
    }

    /**
     * initializes rates array
     * @param driver The driver to be initialized
     */
    private void initRatesFromDB(Driver driver) {
        ArrayList<Rate> rates = new ArrayList<Rate>();
        ResultSet myRates = db.query("SELECT * FROM rate WHERE driverUsername = '" + driver.getUserName() + "'");
        try {
            while (myRates.next()) {
                Rate rate = new Rate(
                        customerModel.getAccount(myRates.getString("customerUsername")),
                        myRates.getFloat("rating")
                );
                rates.add(rate);
            }
            driver.setRates(rates);
        } catch (java.sql.SQLException e) {
            System.out.println("SQL ERROR");
        }
    }

    /**
     * initializes favourite areas array
     * @param driver The driver to be initialized
     */
    private void initFavouriteAreasFromDB(Driver driver) {
        ArrayList<String> areas = new ArrayList<String>();
        ResultSet areasTable = db.query("SELECT area FROM favouriteAreas WHERE driverUsername = '" + driver.getUserName() + "'");

        try {
            while (areasTable.next())
                areas.add(areasTable.getString("area"));
            driver.setFavouriteAreas(areas);
        } catch (java.sql.SQLException e) {
            System.out.println("SQL ERROR");
        }
    }

    /**
     * Initializes the active offers from the database
     * @param driver The driver to be initialized
     */
    private void initActiveOffersFromDB(Driver driver) {
        ResultSet activeOffersTable = db.query("SELECT offerID FROM activeOffers WHERE username = '" + driver.getUserName() + "'");

        try {
            while (activeOffersTable.next())
                driver.getActiveOffers().add(
                        RideManager.getInstance().getOfferById(
                                activeOffersTable.getString("offerID"),
                                driver
                        )
                );
        } catch (java.sql.SQLException e) {
            System.out.println("SQL ERROR");
        }
    }

}
