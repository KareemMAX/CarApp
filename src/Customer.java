import java.util.List;

public class Customer extends Account {
    private String email = "";
    private String phoneNumber = "";
    private List<Offer> pastRides;

    /**
     * Creates a new customer account with the parameters as the account details
     * @param userName      The User name associated with this account
     * @param password      The password associated with this account
     * @param email         The E-mail address associated with this account
     * @param phoneNumber   The phone number associated with this account
     */
    public Customer(String userName, String password, String email, String phoneNumber) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    //TODO needs Implementation
    public void notify(Offer offer) {

    }

    /**
     * Indicates the ability to sign in of that customer account
     * @return boolean value indicator
     */
    boolean ableToSignIn() {
        return !suspended;
    }
}
