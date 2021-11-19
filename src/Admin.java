public class Admin extends Account {
    public Admin(String username, String password) {
        super(username, password);
    }

    public boolean ableToSignIn() {
        return true;
    }

}
