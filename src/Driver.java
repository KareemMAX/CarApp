import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * A driver account class
 * @author Khaled Waleed
 */
public class Driver extends Account {
    private String email = "";
    private String phoneNumber = "";
    private String nationalID = "";
    private String license = "";
    private boolean verified = false;
    private List<String> favouriteAreas;
    private List<Rate> rates;

    /**
     * Creates a new driver account with the parameters as the account details
     * @param userName      The User name associated with this account
     * @param password      The Password associated with this account
     * @param phoneNumber   The Phone number associated with this account
     * @param licence       The License number associated with this account
     * @param nationalID    The National ID associated with this account
     * @param email         The E-Mail address associated with this account
     */
    public Driver(String userName, String password, String phoneNumber, String licence, String nationalID, String email) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
        this.userInterface = new DriverInterface();
    }

    /**
     * Creates a new driver account with the parameters as the account details
     * An overload of the Driver class constructor to allow not providing an email address
     * @param userName      The Username associated with this account
     * @param password      The Password associated with this account
     * @param phoneNumber   The Phone number associated with this account
     * @param licence       The License number associated with this account
     * @param nationalID    The National ID associated with this account
     */
    public Driver(String userName, String password, String phoneNumber, String licence, String nationalID) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
    }
    /**
     * Get the E-mail address associated with this customer
     * @return E-mail address as a String
     */

    public String getEmail() {
        return email;
    }

    /**
     * Get the Phone number associated with this customer
     * @return phone number as a String
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the National ID associated with this customer
     * @return National ID as a String
     */
    public String getNationalID() {
        return nationalID;
    }

    /**
     * Get the License number associated with this customer
     * @return License number as a String
     */
    public String getLicense() {
        return license;
    }

    /**
     * Get the verification status of that account
     * @return boolean value equals true if verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * Get the driver's favourite areas as a list
     * @return List of strings (Areas)
     */
    public List<String> getFavouriteAreas() {
        return favouriteAreas;
    }

    /**
     * Get the driver's past ratings
     * @return List of Rates
     */
    public List<Rate> getRates() {
        return rates;
    }

    /**
     * Sets the rates Array of that driver
     */
    public void setRates (ArrayList<Rate> newRates) {this.rates = newRates;}

    /**
     * Indicates the ability to sign in of that driver account
     * @return boolean value indicator
     */
    public boolean ableToSignIn() {
        return !isSuspended() && verified;
    }

    /**
     * sets the verified boolean
     * @param b intended value to set
     */
    public void setVerified(boolean b) {
        this.verified = b;
        updateInDB();
    }
    //TODO needs implementation
    public void notify(Request request) {

    }

    /**
     * Add a new entry to the driver's rating history
     * @param customer      The customer who created that rating
     * @param value         The numerical value of the rating
     */
    public void rate(Customer customer, float value) {
        rates.add(new Rate(customer, value));
    }

    /**
     * initializes rates array
     */
    public void initRatesFromDB()
    {
        Database db = Database.getInstance();
        ArrayList<Rate> rates = new ArrayList<Rate>();
        ResultSet myRates = db.query("SELECT * FROM rate WHERE driver_id = '"+getUserName()+"'");
        try
        {
            while (myRates.next())
            {
                Rate rate = new Rate((Customer) AccountManager.getInstance().getAccount(myRates.getString("user_ID")),
                        myRates.getFloat("rate_value"));
                rates.add(rate);
            }
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("SQL ERROR");
        }
    }

    /**
     * initializes favourite areas array
     */
    public void initFavouriteAreasFromDB()
    {
        Database db = Database.getInstance();
        ArrayList<String> areas = new ArrayList<String>();
        ResultSet areasTable = db.query("SELECT favourite_place FROM favourite_places WHERE driver_id = '"+ getUserName()+"'");

        try
        {
            while (areasTable.next())
                areas.add(areasTable.getString("favourite_place"));
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("SQL ERROR");
        }
    }

    /**
     * Get the average of all ratings submitted on this driver
     * @return
     */
    public float getAverageRate() {
        if (rates == null || rates.size() == 0) return 0;
        int sum = 0;
        for (Rate rate : rates) {
            sum += rate.getRateValue();
        }
        return (float) sum / (float) rates.size();
    }

    @Override
    void setSuspended(boolean b) {
        super.setSuspended(b);
        this.updateInDB();
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
     * @param newArea The new area to be added
     */
    public void addFavouriteArea(String newArea) {
        favouriteAreas.add(newArea);
    }

    /**
     * Delete an entry from the areas made favourite by this driver
     * @param area The area to be deleted
     */
    public void deleteFavouriteArea(String area) {
        favouriteAreas.remove(area);
    }

    /**
     * sets the array of the favourite areas
     * @param areas
     */
    public void setFavouriteAreas(List<String> areas) {this.favouriteAreas = areas;}

    /**
     * updates the data concerning this account in the data base
     */
    public void updateInDB()
    {
        Database db = Database.getInstance();
        String query = "UPDATE driver\n"+
                 "SET username= '"+getUserName()+"', password= '"+password+"', email= '"+email+"', phonenumber= '"+phoneNumber+"'"
                +", national_id= '"+nationalID+"', license= '"+license+"'";
        if (isSuspended())
             query+= ", suspended= 'true'";
        else query+= ", suspended= 'false'";

        if (verified)
             query+= ", verified= 'true'";
        else query+= ", verified= 'false'";

        db.update(query);
    }
}
