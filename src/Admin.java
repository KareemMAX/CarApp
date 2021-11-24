/**
 * Admin account class
 * @author Khaled Waleed
 */
public class Admin extends Account {

    /**
     * Creates a new driver account with the parameters as the account details
     * @param username      The username associated with this account
     * @param password      The password associated with this account
     */
    public Admin(String username, String password) {
        super(username, password);
        this.userInterface = new AdminInterface();
    }

    /**
     * Indicates the ability of the Admin class to sign in
     * @return      boolean value indicator that is always true for admins
     */
    public boolean ableToSignIn() {
        return true;
    }

}
