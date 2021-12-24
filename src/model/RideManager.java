package model;

import controller.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * model.RideManager Singleton class
 * <p>
 * Provides a single connection to the current database for other components to use.
 *
 * @author Andrew Naseif
 * @version 1.00 2021/11/20
 */
public class RideManager {
    private static RideManager singletonInstance;

    /**
     * Gets the singleton instance of the class
     *
     * @return The active instance of the class
     */
    public static RideManager getInstance() {
        if (singletonInstance == null) singletonInstance = new RideManager();
        return singletonInstance;
    }


    private final Database db = Database.getInstance();
    private final AccountManager accountManager = AccountManager.getInstance();
    private final HashMap<String, List<Driver>> subscribers = new HashMap<>();

    /**
     * Makes a query to the database to get al list of offers for a given request.
     * if any error has happened during execution a stacktrace is printed.
     *
     * @param request The SQL will use it to make query.
     * @return {@link ArrayList} if the execution happened successfully.
     */
    public List<Offer> listOffers(Request request) {
        ResultSet table = db.query("SELECT * FROM offer\n" +
                "right JOIN request ON offer.ride_id=request.request_id WHERE ride_id=" + request.getId());
        ArrayList<Offer> result = new ArrayList<>();
        try {
            while (table.next()) {
                Offer offer = new Offer(
                        request,
                        table.getFloat("price"),
                        (Driver) accountManager.getAccount(table.getString("driver_id"))
                );
                result.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Makes a query to the database to insert new offer in database.
     *
     * @param driver  The SQL will use it to know who make this offer.
     * @param request The SQL will use it to make offer for this request.
     * @param price   it is the price of ride.
     */
    public void makeOffer(Driver driver, Request request, float price) {
        Offer offer = new Offer(request, price, driver);
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO offer (driver_id,accepted,ride_id,price)\n");
        sqlQuery.append("VALUES (");
        sqlQuery.append("'").append(driver.getUserName()).append("',");
        sqlQuery.append(0).append(",");
        sqlQuery.append(request.getId()).append(",");
        sqlQuery.append(price).append(");");
        db.update(sqlQuery.toString());
        request.getUser().notify(offer);
    }

    /**
     * Makes a query to the database to insert new offer in database.
     *
     * @param offer    used to which offer is checked
     * @param accepted {@code true} all offers will be deleted excepted this offer.
     *                 {@code  false}only this offer will be deleted
     */
    public void setOfferAccepted(Offer offer, boolean accepted) {
        try {
            if (accepted) {
                db.update("UPDATE offer \n" +
                        "SET \n" +
                        "    accepted = 1\n" +
                        "WHERE\n" +
                        " ride_id =" + offer.getRequest().getId() + "AnD driver_id= '" + offer.getDriver().getUserName()+"'");
                db.update("DELETE FROM offer WHERE accepted=0 and  ride_id =" + offer.getRequest().getId()+";");
            } else {
                db.update("DELETE FROM offer WHERE ride_id =" + offer.getRequest().getId() + "AnD driver_id= '" + offer.getDriver().getUserName()+"'");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a query to the database to insert new request in database.
     *
     * @param source      The SQL will use it to know where he is.
     * @param destination The SQL will use it to make know where he is going to.
     * @param account     the user he makes the request.
     */
    public boolean makeRequest(String source, String destination, Customer account) {
        if (source == null || destination == null || account.getUserName() == null) {
            return false;
        }
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO request (source,destination,user_id)\n");
        sqlQuery.append("VALUES (");
        sqlQuery.append("'").append(source).append("',");
        sqlQuery.append("'").append(destination).append("',");
        sqlQuery.append("'").append(account.getUserName()).append("');");
        db.update(sqlQuery.toString());

        String query = "SELECT request_id FROM request WHERE source='" + source + "' and destination='" + destination + "'and user_id='" + account.getUserName() + "'";
        ResultSet resultSet = db.query(query);
        try {
            resultSet.next();

            if (subscribers.containsKey(source)) {
                for (Driver driver :
                        subscribers.get(source)) {
                    driver.notify(new Request(resultSet.getInt("request_id"), source, destination, account));
                }
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Makes a query to the database to get al list of requests.
     * if any error has happened during execution a stacktrace is printed.
     *
     * @return {@link ArrayList} if the execution happened successfully.
     */
    public List<Request> getRequests() {
        AccountManager dbA = AccountManager.getInstance();
        ResultSet table = db.query("SELECT distinct request_id, [source], destination, [user_id] FROM request LEFT jOIN  offer \n" +
                "on request.request_id=offer.ride_id WHERE accepted !='true' or accepted is NULL ;");
        ArrayList<Request> result = new ArrayList<>();
        try {
            while (table.next()) {
                Request request = new Request(
                        table.getInt("request_id"),
                        table.getString("source"),
                        table.getString("destination"),
                        (Customer) dbA.getAccount(table.getString("user_id"))
                );
                result.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Make a driver subscribe to any new requests in the system
     * @param driver A driver to be notified
     */
    public void subscribeForRequests(Driver driver) {
        for (String area :
                driver.getFavouriteAreas()) {
            if (!subscribers.containsKey(area))
                subscribers.put(area, new ArrayList<>());

            subscribers.get(area).add(driver);
        }
    }


    /**
     * @return Last ride driver for a certain customer
     */
    public Driver getLastRideDriver(Customer account) {
        return account.getPastRides().get(account.getPastRides().size() - 1).getDriver();
    }
    public void rate(Driver driver, Rate rate){
        db.update("Insert Into rate values"+"('"+rate.getUser().getUserName()+"','"+driver.getUserName()+"',"+rate.getRateValue()+");");
    }

}