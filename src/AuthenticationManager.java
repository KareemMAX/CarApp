import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private Account currentAccount;
    private static AuthenticationManager singletonInstance;


    public static AuthenticationManager getInstance() {
        if (singletonInstance == null) singletonInstance = new AuthenticationManager();
        return singletonInstance;
    }

    public Account getcurrentAccount() {
        return currentAccount;
    }

    public void logout(){
        currentAccount = null;
    }

    public boolean register(Account acc) {
        Map<String, String> cred = new HashMap<>();
        cred.put("userName", acc.userName);
        cred.put("password", acc.password);
        if (acc instanceof Customer) {
            cred.put("email", ((Customer) acc).getEmail());
            cred.put("phoneNumber", ((Customer) acc).getPhoneNumber());
        } else if (acc instanceof Driver) {
            cred.put("email", ((Driver) acc).getEmail());
            cred.put("phoneNumber", ((Driver) acc).getPhoneNumber());
            cred.put("nationalId", ((Driver) acc).getNationalID());
            cred.put("license", ((Driver) acc).getLicense());
            cred.put("verified", ((Driver) acc).isVerified() ? "true" : "false");
            cred.put("avgRating", String.valueOf(((Driver) acc).getAverageRate()));
        }
        for (String key : cred.keySet()) {
            //TODO Create Query Object & Send to DB
        }
        return true;
    }

    public boolean login(String username, String password) {
        //TODO Query DB & validate creds
        return true;
    }
}
