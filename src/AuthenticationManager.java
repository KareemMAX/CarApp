import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
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
        //check if exists
        Database db = Database.getInstance();
        ArrayList<Account> accountsWithUsername = new ArrayList<Account>();
        ResultSet resultSet = db.query("SELECT username from customer where username= '"+cred.get("userName")+"'");
        int counter=0;
        try
        {
            while (resultSet.next())
                counter++;
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("SQL error!");
            return false;
        }
        if (counter == 0) return false;

        if (acc instanceof Customer)
            return db.update("Insert into customer (username,password,email,phone_number,suspended) values ( " +
                    cred.get("userName")+" ,"+cred.get("password")+" ,"+cred.get("email")+" ,"+ cred.get("phoneNumber")+" ,"+
                    "false");
        else if (acc instanceof Driver)
            return db.update("Insert into customer (username,password,email,phone_number,nationalId, license," +
                    "suspended,verified) values ( " +
                    cred.get("userName")+" ,"+cred.get("password")+" ,"+cred.get("email")+" ,"+ cred.get("phoneNumber")+" ,"+
                    cred.get("nationalId")+" , "+ cred.get("licence")+", false, false");
        else return false;
    }

    public boolean login(Account acc) {
        //TODO Query DB & validate creds
        return true;
    }
}
