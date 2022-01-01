package controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.AccountManager;
import model.Database;
import model.EventManager;
import model.RideManager;
import view.DriverInterface;

import java.sql.ResultSet;
import java.util.*;

/**
 * A driver account class
 *
 * @author Khaled Waleed
 */
public class Driver extends Account {
    private String email = "";
    private String phoneNumber = "";
    private String nationalID = "";
    private String license = "";
    private boolean verified = false;
    private List<String> favouriteAreas = new ArrayList<>();
    private List<Rate> rates;
    private final List<Offer> activeOffers = new ArrayList<>();
    private float balance = 0;

    private final RideManager rideManager = RideManager.getInstance();
    private final AccountManager accountManager = AccountManager.getInstance();

    /**
     * Creates a new driver account with the parameters as the account details
     *
     * @param userName    The Username associated with this account
     * @param password    The Password associated with this account
     * @param phoneNumber The Phone number associated with this account
     * @param licence     The License number associated with this account
     * @param nationalID  The National ID associated with this account
     * @param email       The E-Mail address associated with this account
     */
    public Driver(String userName, String password, String email, String phoneNumber, String nationalID, String licence) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
        setUserInterface(new DriverInterface());
        this.rates = new ArrayList<>();
    }

    public Driver(String userName, String password, String email, String phoneNumber, String nationalID, String license, boolean suspended, boolean verified) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationalID = nationalID;
        this.license = license;
        this.verified = verified;
        super.setSuspended(suspended);
        setUserInterface(new DriverInterface());
    }

    /**
     * Creates a new driver account with the parameters as the account details
     * An overload of the controller.Driver class constructor to allow not providing an email address
     *
     * @param userName    The Username associated with this account
     * @param password    The Password associated with this account
     * @param phoneNumber The Phone number associated with this account
     * @param licence     The License number associated with this account
     * @param nationalID  The National ID associated with this account
     */
    public Driver(String userName, String password, String phoneNumber, String licence, String nationalID) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
        this.rates = new ArrayList<>();
        setUserInterface(new DriverInterface());
    }

    public Driver() {

    }

    /**
     * Get the E-mail address associated with this customer
     *
     * @return E-mail address as a String
     */

    public String getEmail() {
        return email;
    }

    /**
     * Get the Phone number associated with this customer
     *
     * @return phone number as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the National ID associated with this customer
     *
     * @return National ID as a String
     */
    public String getNationalID() {
        return nationalID;
    }

    /**
     * Get the License number associated with this customer
     *
     * @return License number as a String
     */
    public String getLicense() {
        return license;
    }

    /**
     * Get the verification status of that account
     *
     * @return boolean value equals true if verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Get the driver's favourite areas as a list
     *
     * @return List of strings (Areas)
     */
    public List<String> getFavouriteAreas() {
        return favouriteAreas;
    }

    /**
     * Get the driver's past ratings
     *
     * @return List of Rates
     */
    public List<Rate> getRates() {
        return rates;
    }

    /**
     * Sets the rates Array of that driver
     */
    public void setRates(ArrayList<Rate> newRates) {
        this.rates = newRates;
    }

    /**
     * Indicates the ability to sign in of that driver account
     *
     * @return boolean value indicator
     */
    public boolean ableToSignIn() {
        return !isSuspended() && verified;
    }

    /**
     * sets the verified boolean
     *
     * @param b intended value to set
     */
    public void setVerified(boolean b) {
        this.verified = b;
        accountManager.updateAccount(this);
    }

    /**
     * Notification function for the customer to notify
     *
     * @param request The new supplied request from a customer
     */
    public void notify(Request request) {
        //TODO needs Implementation
    }

    /**
     * Add a new entry to the driver's rating history
     *
     * @param customer The customer who created that rating
     * @param value    The numerical value of the rating
     */
    public void rate(Customer customer, float value) {
        Rate rate = new Rate(customer, value);
        rates.add(rate);
        rideManager.rate(this, rate);
    }

    /**
     * Get the average of all ratings submitted on this driver
     *
     * @return Average Rating
     */
    public float getAverageRate() {
        if (rates == null || rates.size() == 0) return 0;
        float sum = 0;
        for (Rate rate : rates) {
            sum += rate.getRateValue();
        }
        return sum / (float) rates.size();
    }

    @Override
    public void setSuspended(boolean b) {
        super.setSuspended(b);
        accountManager.updateAccount(this);
    }

    @JsonIgnore
    public List<Offer> getActiveOffers() {
        return activeOffers;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "userName='" + getUserName() + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nationalID='" + nationalID + '\'' +
                ", license='" + license + '\'' +
                ", verified=" + verified +
                ", favouriteAreas=" + favouriteAreas +
                ", rates=" + rates +
                "} " + super.toString();
    }

    /**
     * Add new entry to the areas made favourite by this driver
     *
     * @param newArea The new area to be added
     */
    public void addFavouriteArea(String newArea) {
        accountManager.addFavouriteArea(this, newArea);
        favouriteAreas.add(newArea);

    }

    /**
     * Delete an entry from the areas made favourite by this driver
     *
     * @param area The area to be deleted
     */
    public void deleteFavouriteArea(String area) {
        favouriteAreas.remove(area);
    }

    /**
     * sets the array of the favourite areas
     *
     * @param areas List of areas
     */
    public void setFavouriteAreas(List<String> areas) {
        this.favouriteAreas = areas;
    }

    /**
     * Gets the driver's current balance
     *
     * @return The current balance
     */
    public float getBalance() {
        return balance;
    }

    /**
     * Checks if the driver available to handle a new request
     *
     * @return A boolean
     */
    public boolean isAvailable() {
        if (activeOffers.isEmpty()) {
            return true;
        }

        int minPassengers = activeOffers.get(0).getRequest().getNumberOfPassengers();
        for (Offer offer :
                activeOffers) {
            minPassengers = Math.min(minPassengers, offer.getRequest().getNumberOfPassengers());
        }

        return activeOffers.size() < minPassengers;
    }

    /**
     * Called when the customer accepts the driver's offer
     * @param offer The offer to be added
     */
    public void acceptCustomer(Offer offer) {
        activeOffers.add(offer);
    }

    /**
     * Adds the offer to the current active offers of the driver
     *
     * @param offer The offer to be active
     */
    public void pickUpCustomer(Offer offer) {

        EventManager eventManager = EventManager.getInstance();
        Event event = new Event("Pick up",new Date(),offer);
        eventManager.receiveEvent(event);
        rideManager.pickUpCustomer(offer);
    }

    /**
     * Removes the offer from the current active offers
     *
     * @param offer The offer to be removed
     */
    public void dropCustomer(Offer offer) {
        EventManager eventManager = EventManager.getInstance();
        Event event = new Event("Drop",new Date(),offer);
        eventManager.receiveEvent(event);
        activeOffers.remove(offer);
        rideManager.dropCustomer(offer);
        balance += offer.getPaidPrice();

        accountManager.updateAccount(this);
    }
}
