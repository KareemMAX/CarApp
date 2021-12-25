package model;

import controller.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
                        table.getInt("offerID"),
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
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO offer (driver_id,accepted,ride_id,price)\n");
        sqlQuery.append("VALUES (");
        sqlQuery.append("'").append(driver.getUserName()).append("',");
        sqlQuery.append(0).append(",");
        sqlQuery.append(request.getId()).append(",");
        sqlQuery.append(price).append(");");
        db.update(sqlQuery.toString());

        String query = "SELECT offerID FROM offer WHERE requestID=" + request.getId() + " and driverUsername='"
                + driver.getUserName() + "'and price=" + price;
        ResultSet resultSet = db.query(query);

        try {
            resultSet.next();
            Offer offer = new Offer(resultSet.getInt("offerID"), request, price, driver);
            EventManager.getInstance().receiveEvent("price added", new Date(),offer);
            request.getUser().notify(offer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Makes a query to the database to insert new offer in database.
     *
     * @param offer    used to which offer is checked
     * @param accepted {@code true} all offers will be deleted excepted this offer.
     *                 {@code false} only this offer will be deleted
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

                offer.getDriver().pickUpCustomer(offer);
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
    public boolean makeRequest(String source, String destination, Customer account, int numberOfPassengers) {
        if (source == null || destination == null || account.getUserName() == null) {
            return false;
        }
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO request (source,destination,user_id,numberOfPassengers)\n");
        sqlQuery.append("VALUES (");
        sqlQuery.append("'").append(source).append("',");
        sqlQuery.append("'").append(destination).append("',");
        sqlQuery.append("'").append(account.getUserName()).append("',");
        sqlQuery.append(numberOfPassengers).append(");");
        db.update(sqlQuery.toString());

        String query = "SELECT request_id FROM request WHERE source='" + source + "' and destination='" + destination + "'and user_id='" + account.getUserName() + "'";
        ResultSet resultSet = db.query(query);
        try {
            resultSet.next();

            if (subscribers.containsKey(source)) {
                for (Driver driver :
                        subscribers.get(source)) {
                    if (driver.isAvailable())
                        driver.notify(new Request(resultSet.getInt("request_id"), source, destination, account, numberOfPassengers));
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
                        (Customer) dbA.getAccount(table.getString("user_id")),
                        table.getInt("numberOfPassengers")
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

    /**
     * Gets offer from the database by its ID
     * @param id ID of the offer
     * @return The offer object
     */
    public Offer getOfferById(int id) {
        AccountManager dbA = AccountManager.getInstance();
        ResultSet table = db.query("SELECT request.requestID, request.[source], request.destination, " +
                "request.customerUsername, request.numberOfPassengers, offer.price, offer.accepted, offer.driverUsername" +
                " FROM offer LEFT jOIN request on request.requestID=offer.requestID WHERE offer.offerID = " + id + ";");
        Offer result = null;
        try {
            while (table.next()) {
                Request request = new Request(
                        table.getInt("request.requestID"),
                        table.getString("request.[source]"),
                        table.getString("request.destination"),
                        (Customer) dbA.getAccount(table.getString("request.customerUsername")),
                        table.getInt("request.numberOfPassengers")
                );
                result = new Offer(
                        id,
                        request,
                        table.getFloat("offer.price"),
                        (Driver) dbA.getAccount(table.getString("offer.driverUsername")),
                        table.getBoolean("offer.accepted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void pickUpCustomer(Offer offer) {
        db.update("Insert Into activeOffers (username,offerID) values"+"('"+offer.getDriver().getUserName()+"','"+offer.getId()+"');");
    }

    public void dropCustomer(Offer offer) {
        db.update("DELETE FROM activeOffers WHERE offerID =" + offer.getId()+";");
    }

}