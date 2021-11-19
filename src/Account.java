public abstract class Account {
    String userName = "";
    String password = "";
    boolean suspended = false;

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    abstract boolean ableToSignIn();

    void setSuspended(boolean b) {
        this.suspended = b;
    }

    public String getUserName() {
        return userName;
    }
}
