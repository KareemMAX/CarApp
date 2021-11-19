
import java.util.ArrayList;

public class AccountManager {
    private static AccountManager singletonInstance;

    public static AccountManager getInstance(){
        if (singletonInstance == null) singletonInstance = new AccountManager();
        return singletonInstance;
    }

    public ArrayList<Account> getAllAccounts(){
        //TODO
        return new ArrayList();
    }

    public ArrayList<Account> getSuspendedAccounts(){
        //TODO
        return new ArrayList();
    }

    public void updateAccount(Account acc){
        //TODO Query DB with new values
    }
}
