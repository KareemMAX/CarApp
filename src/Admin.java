public class Admin extends Account {
    public Admin(String username, String password) {
        super(username, password);
    }

    /**
     * Indicates the ability of the Admin class to sign in
     * @return      boolean value indicator that is always true for admins
     */
    public boolean ableToSignIn() {
        return true;
    }

}
