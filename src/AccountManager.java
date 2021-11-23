import java.util.ArrayList;

public class AccountManager {
    private static AccountManager singletonInstance;

    public static AccountManager getInstance(){
        if (singletonInstance == null) singletonInstance = new AccountManager();
        return singletonInstance;
    }

    public <T extends Account> ArrayList<T> getAllAccounts(){
        //TODO
        return new ArrayList();
    }

    public <T extends Account> T getAccount(String username) {
        // TODO
        return null;
    }

    public <T extends Account> ArrayList<T> getSuspendedAccounts(){
        //TODO
        return new ArrayList();
    }

    public <T extends Account> void updateAccount(T acc){
        //TODO Query DB with new values
    }
}
