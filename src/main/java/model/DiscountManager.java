package model;

import controller.*;

import controller.PublicHolidays;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiscountManager {
    private final Database db = Database.getInstance();

    private static DiscountManager singletonInstance;

    private DiscountManager() {}

    public static DiscountManager getInstance() {
        if (singletonInstance == null) singletonInstance = new DiscountManager();
        return singletonInstance;
    }

    public void addNewPublicHolidayDB(PublicHolidays holiday) {
        Database.getInstance().update("INSERT into publicHolidays VALUES ( '" +
                holiday.getPublicHolidayDate() + "', '" + holiday.getName() + "')");

    }
    public boolean searchForDestination(String destination) {
        ResultSet resultSet = db.query("SELECT * FROM discountDestination WHERE destination='" + destination + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteDiscountDestination(String destination) {
        if (searchForDestination(destination)) {
            Database.getInstance().update("DELETE FROM discountDestination WHERE destination='" + destination + "';");
            return true;
        }
        return false;
    }

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

    public void addDiscountDestination(String destination) {
        Database.getInstance().update("INSERT INTO discountDestination values ('" + destination + "')");
    }

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

    public boolean isDiscountDestination(String destination) {
        ArrayList<String> destinations = DiscountManager.getInstance().getAllDiscountDestination();
        for (int i = 0; i < destinations.size(); i++) {
            if (destinations.get(i).equals(destination)) {
                return true;
            }
        }
        return false;
    }

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

    public void addDiscount(String id, float discount) {
        db.update("Insert Into discount (discountID,value) values" + "('" + id + "','" + discount + "');");
    }

    public float getDiscount(Offer offer) throws SQLException {
        ResultSet resultSet = db.query("SELECT [value] from discount WHERE discountID ='" + offer.getId() + "'");

        if (resultSet.next()) {
            return resultSet.getFloat("value");
        }
        return 0;
    }

}
