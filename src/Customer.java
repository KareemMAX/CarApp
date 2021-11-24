import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Account {
    private String email = "";
    private String phoneNumber = "";
    private List<Offer> pastRides;

    /**
     * Creates a new customer account with the parameters as the account details
     * @param userName      The User name associated with this account
     * @param password      The password associated with this account
     * @param phoneNumber   The phone number associated with this account
     * @param email         The E-mail address associated with this account
     */
    public Customer(String userName, String password, String phoneNumber, String email) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userInterface = new CustomerInterface();
    }

    /**
     * Creates a new customer account with the parameters as the account details
     * An overload of the Customer class constructor to allow not providing an email address
     * @param userName      The User name associated with this customer
     * @param password      The password associated with this customer
     * @param phoneNumber   The phone number associated with this customer
     */
    public Customer(String userName, String password, String phoneNumber)
    {
        super(userName, password);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the E-mail address associated with this customer
     * @return E-mail address as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the phone number associated with this customer
     * @return Phone Number as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the past rides of this customer (Ride History) as a list of accepted offers
     * @return A list of accepted offers
     */
    public List<Offer> getPastRides() {
        return pastRides;
    }
    public void setPastRides(List<Offer> rides) {this.pastRides = rides;}

    //TODO needs Implementation
    public void notify(Offer offer) {

    }

    /**
     * Indicates the ability to sign in of that customer account
     * @return boolean value indicator
     */
    boolean ableToSignIn() {
        return !isSuspended();
    }

    /**
     * updates the data concerning this account in the data base
     */
    public void updateInDB()
    {
        Database db = Database.getInstance();
        if (isSuspended())
            db.update("UPDATE customer\n"+
                    "SET username= '"+getUserName()+"', password= '"+password+"', email= '"+email+"', phone_number= '"+phoneNumber+"'"
                    + ", suspended= 'false'" );

        else
            db.update("UPDATE customer\n"+
                    "SET username= '"+getUserName()+"', password= '"+password+"', email= '"+email+"', phone_number= '"+phoneNumber+"'"
                    + ", suspended= 'true'" );
    }

    @Override
    void setSuspended(boolean b) {
        super.setSuspended(b);
        this.updateInDB();
    }

    /**
     * initializes past rides array
     */
    public void initPastRidesFromDB()
    {
        Database db = Database.getInstance();
        ArrayList<Offer> rides= new ArrayList<Offer>();
        ResultSet ridesTable = db.query("SELECT * FROM offer INNER JOIN request ON offer.ride_id = request.request_id\n" +
                "WHERE offer.accepted = 'true' AND request.user_id = '"+getUserName()+"'");
        ResultSet resultSet = db.query("SELECT username FROM customer WHERE username= '" + getUserName() + "' AND password = '" + password + "'");
        try
        {
            while (ridesTable.next())
            {
                Request request = new Request(ridesTable.getInt("request_id"),ridesTable.getString("source"),
                        ridesTable.getString("destination"), this);
                Offer offer     = new Offer(request,resultSet.getFloat("price"),
                        (Driver)AccountManager.getInstance().getAccount(ridesTable.getString("driver_id")));
                rides.add(offer);
            }
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("SQL ERROR");
        }
    }

}
