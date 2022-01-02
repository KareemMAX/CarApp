package model;

import controller.*;

import controller.PublicHolidays;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * model.DiscountManager Singleton class
 *
 * Provides a single connection to the current database for other components to use.
 *
 * @author Andrew Naseif
 * @version 1.00 2022/01/01
 */
public class DiscountManager {
    private final Database db = Database.getInstance();

    private static DiscountManager singletonInstance;

    private DiscountManager() {}
    /**
     * Gets the singleton instance of the class
     *
     * @return The active instance of the class
     */
    public static DiscountManager getInstance() {
        if (singletonInstance == null) singletonInstance = new DiscountManager();
        return singletonInstance;
    }
    /**
     * Makes a query to the database to insert new Public Holidays in database.
     *
     * @param holiday  The SQL store  it in data base.
     *
     */
    public void addNewPublicHolidayDB(PublicHolidays holiday) {
        Database.getInstance().update("INSERT into publicHolidays VALUES ( '" +
                holiday.getPublicHolidayDate() + "', '" + holiday.getName() + "')");

    }
    /**
     * Makes a query to the database to search for discount destination in database.
     *
     * @param destination  The SQL will use it to check if it in data base or not.
     * @return boolean value  as an indication of success or failure
     */
    public boolean searchForDestination(String destination) {
        ResultSet resultSet = db.query("SELECT * FROM discountDestination WHERE destination='" + destination + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }
    /**
     * Makes a query to the database to delete discount destination from database.
     *
     * @param destination  The SQL will use it to delete it if it in data base.
     * @return boolean value  as an indication of success or failure
     */
    public boolean deleteDiscountDestination(String destination) {
        if (searchForDestination(destination)) {
            Database.getInstance().update("DELETE FROM discountDestination WHERE destination='" + destination + "';");
            return true;
        }
        return false;
    }
    /**
     * Makes a query to the database to get a list of Public Holidays.
     *
     * @return {@link ArrayList} list of public holidays.
     */
    public ArrayList<PublicHolidays> getAllPublicHolidays() {
        ArrayList<PublicHolidays> allHolidays = new ArrayList<PublicHolidays>();
        ResultSet resultSet = Database.getInstance().query("SELECT * FROM publicHolidays");
        try {
            while (resultSet.next()) {

                allHolidays.add(new PublicHolidays(
                        resultSet.getDate("date"),
                        resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allHolidays;
    }
    /**
     * Makes a query to the database to get a list contains the date of Public Holidays.
     *
     * @return {@link ArrayList} list of holidays date.
     */
    public ArrayList<Date> getAllPublicHolidaysDate() {
        ArrayList<Date> allHolidays = new ArrayList<Date>();
        ResultSet resultSet = Database.getInstance().query("SELECT date FROM publicHolidays");
        try {
            while (resultSet.next()) {

                allHolidays.add(resultSet.getDate("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allHolidays;
    }
    /**
     * Makes a query to the database to insert new discount destination in data base.
     *
     * @param  destination it will be saved in data base.
     */
    public void addDiscountDestination(String destination) {
        Database.getInstance().update("INSERT INTO discountDestination values ('" + destination + "')");
    }
    /**
     * Makes a query to the database to get a list of Discount Destinations.
     *
     * @return {@link ArrayList} list of Discount Destinations.
     */
    public ArrayList<String> getAllDiscountDestination() {
        ArrayList<String> destinations = new ArrayList<String>();
        ResultSet resultSet = Database.getInstance().query("SELECT * FROM discountDestination");
        try {
            while (resultSet.next()) {
                destinations.add(
                        resultSet.getString("destination"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinations;
    }
    /**
     * function to check today is public holiday or not.
     *
     * @return a boolean value as an indication of today is holiday or not.
     */
    public boolean isPublicHoliday() {
        int counter;
        java.util.Date currentDate = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateNow = dateFormat.format(currentDate).toString().split("-");
        ArrayList<Date> holidays = DiscountManager.getInstance().getAllPublicHolidaysDate();
        for (int i = 0; i < holidays.size(); i++) {
            counter = 0;
            String[] holiday = holidays.get(i).toString().split("-");
            for (int j = 1; j < 3; j++) {
                if (!(holiday[j].equals(dateNow[j]))) {
                    return false;
                } else
                    counter++;
            }
            if (counter == 2) {
                return true;
            }
        }
        return false;
    }
    /**
     * function to check if the destination of an request is one of discount-destinations or not.
     * @param destination it will used to knowmif it public holiday or not
     * @return a boolean value as an indication of destination is one of discount-destinations or not.
     */
    public boolean isDiscountDestination(String destination) {
        ArrayList<String> destinations = DiscountManager.getInstance().getAllDiscountDestination();
        for (int i = 0; i < destinations.size(); i++) {
            if (destinations.get(i).equals(destination)) {
                return true;
            }
        }
        return false;
    }
    /**
     * function to calculate the discount for a request.
     * @param request {@link Request} it will be used to calculate the discount
     * @return discount the value of discount.
     */
    public float calculateDiscount(Request request) {
        int discount = 0;
        if (request.getNumberOfPassengers() == 2)
            discount += 5;
        if (request.getUser().getPastRides().isEmpty())
            discount += 10;
        if (request.getUser().isBirthDay())
            discount += 10;
        if (DiscountManager.getInstance().isPublicHoliday())
            discount += 5;
        if (DiscountManager.getInstance().isDiscountDestination(request.getDestination()))
            discount += 10;
        return discount;
    }
    /**
     * Makes a query to the database to insert  a new Discount in data base.
     * @param id it will be used to know for each offer this discount.
     * @param discount the value of the discount
     *
     */
    public void addDiscount(String id, float discount) {
        db.update("Insert Into discount (discountID,value) values" + "('" + id + "','" + discount + "');");
    }

    /**
     * Makes a query to the database to get  the Discount of an offer from data base.
     * @param offer it will be used to search for the  discount value for this offer.
     * @return value of the Discount.
     *
     */
    public float getDiscount(Offer offer) throws SQLException {
        ResultSet resultSet = db.query("SELECT [value] from discount WHERE discountID ='" + offer.getId() + "'");

        if (resultSet.next()) {
            return resultSet.getFloat("value");
        }
        return 0;
    }

}
