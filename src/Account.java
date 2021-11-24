/**
 * An account class holding account data
 *
 * @author Khaled Waleed
 */
public abstract class Account {
    protected String password = "";
    protected UserInterface userInterface;
    private String userName = "";
    private boolean suspended = false;

    /**
     * Creates An account with the specified username and password
     *
     * @param userName User Name
     * @param password Password
     */
    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Indicates the ability to sign in of that account
     *
     * @return boolean value indicator
     */
    abstract boolean ableToSignIn();

    /**
     * gets the user suspension state
     *
     * @return The current suspension state
     */
    boolean isSuspended() {
        return suspended;
    }

    /**
     * Sets the suspension state for that account
     *
     * @param b the desired suspension state
     */
    void setSuspended(boolean b) {
        this.suspended = b;
    }

    /**
     * gets the user name of that account
     *
     * @return The user name as a string
     */
    public String getUserName() {
        return userName;
    }
}
