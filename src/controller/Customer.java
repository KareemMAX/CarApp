package controller;

import model.AccountManager;
import model.Database;
import view.CustomerInterface;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * A customer account class
 *
 * @author Khaled Waleed
 */
public class Customer extends Account {
    private String email = "";
    private String phoneNumber = "";
    private List<Offer> pastRides;
    private Date birthday;

    /**
     * Creates a new customer account with the parameters as the account details
     *
     * @param userName    The Username associated with this account
     * @param password    The password associated with this account
     * @param phoneNumber The phone number associated with this account
     * @param email       The E-mail address associated with this account
     * @param birthday    The birthday date associated with this account
     */
    public Customer(String userName, String password, String phoneNumber, String email, Date birthday) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userInterface = new CustomerInterface();
        this.birthday = birthday;
    }

    public Customer(String userName, String password, String email, String phoneNumber, Date birthday, boolean suspended) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        super.setSuspended(suspended);
    }

    /**
     * Creates a new customer account with the parameters as the account details
     * An overload of the controller.Customer class constructor to allow not providing an email address
     *
     * @param userName    The Username associated with this customer
     * @param password    The password associated with this customer
     * @param phoneNumber The phone number associated with this customer
     * @param birthday    The birthday date associated with this account
     */
    public Customer(String userName, String password, String phoneNumber, Date birthday) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
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
     * Get the phone number associated with this customer
     *
     * @return Phone Number as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the birthday associated with this customer
     *
     * @return Birthday as a SQL.Date object
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Get the past rides of this customer (Ride History) as a list of accepted offers
     *
     * @return A list of accepted offers
     */
    public List<Offer> getPastRides() {
        initPastRidesFromDB();
        return pastRides;
    }

    /**
     * Set the value for past rides
     *
     * @param rides List of rides to be used
     */
    public void setPastRides(List<Offer> rides) {
        this.pastRides = rides;
    }

    /**
     * Notification function for the driver to notify
     *
     * @param offer The new supplied offer from the driver
     */
    public void notify(Offer offer) {
        //TODO needs Implementation
    }

    /**
     * Indicates the ability to sign in of that customer account
     *
     * @return boolean value indicator
     */
    public boolean ableToSignIn() {
        return !isSuspended();
    }

    /**
     * Sets the suspension state for the current customer
     *
     * @param b new state of suspension
     */
    @Override
    public void setSuspended(boolean b) {
        super.setSuspended(b);
        AccountManager.getInstance().updateAccount(this);
    }


    @Override
    public String toString() {
        return "Customer{" +
                "User name='"+this.getUserName()+"'"+
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isSuspended='" + isSuspended() + '\'' +
                ", birthday='" + birthday.toString() + '\'' +
                '}';
    }

    /**
     * initializes past rides array
     */
    public void initPastRidesFromDB() {
        Database db = Database.getInstance();
        ArrayList<Offer> rides = new ArrayList<Offer>();
        ResultSet ridesTable = db.query("SELECT * FROM [offer] INNER JOIN request ON [offer].[requestID] = [request].[requestID]\n" +
                "WHERE [offer].[accepted] = 'true' AND [request].[customerUsername] = '" + getUserName() + "'");
        try {
            while (ridesTable.next()) {
                Request request = new Request(ridesTable.getString("requestID"), ridesTable.getString("source"),
                        ridesTable.getString("destination"), this);
                Offer offer = new Offer(request, ridesTable.getFloat("price"),
                        (Driver) AccountManager.getInstance().getAccount(ridesTable.getString("driverUsername")));
                rides.add(offer);
            }
            pastRides=rides;
        } catch (java.sql.SQLException e) {
            System.out.println("SQL ERROR");
        }
    }

}
