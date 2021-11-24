import java.util.ArrayList;
import java.util.List;

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
     * @param userName      The User name associated with this account
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
        return !suspended && verified;
    }

    /**
     * sets the verified boolean
     * @param b intended value to set
     */
    public void setVerified(boolean b) {
        this.verified = b;
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
     * Get the average of all ratings submitted on this driver
     * @return
     */
    public float getAverageRate() {
        int sum = 0;
        for (Rate rate : rates) {
            sum += rate.getRateValue();
        }
        return (float) sum / (float) rates.size();
    }

    @Override
    public String toString() {
        return "Driver{" +
                "userName='" + userName + '\'' +
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
    public void setFavouriteAreas(List<String> areas) {this.favouriteAreas = areas;}
}
