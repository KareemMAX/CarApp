import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class that manages authentication in the application
 *
 * @author Khaled Waleed
 */
public class AuthenticationManager {
    private Account currentAccount;
    private static AuthenticationManager singletonInstance;


    /**
     * Applying the Singleton Design pattern by only allowing getting the instance
     *
     * @return the one and only Authentication Manager instance
     */
    public static AuthenticationManager getInstance() {
        if (singletonInstance == null) singletonInstance = new AuthenticationManager();
        return singletonInstance;
    }

    /**
     * gets the logged in account
     *
     * @return The current user account
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * revert the login state
     */
    public void logout() {
        currentAccount = null;
    }

    /**
     * Register a new account in the database
     *
     * @param acc the new account
     * @return boolean value as an indication of success or failure
     */
    public boolean register(Account acc) {
        Map<String, String> cred = new HashMap<>();
        cred.put("userName", acc.getUserName());
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
        ResultSet resultSet = db.query("SELECT username from customer WHERE username= '" + cred.get("userName") + "'" +
                "UNION SELECT username from driver WHERE username= '" + cred.get("userName") + "'");
        int counter = 0;
        try {
            while (resultSet.next())
                counter++;
        } catch (java.sql.SQLException e) {
            System.out.println("SQL error!");
            return false;
        }
        if (counter != 0) return false;

        if (acc instanceof Customer)
            return db.update("INSERT into customer VALUES ( '" +
                    cred.get("userName") + "' ,'" + cred.get("password") + "' ,'" + cred.get("email") + "' ,'" + cred.get("phoneNumber") + "' ," +
                    " 'false')");
        else if (acc instanceof Driver)
            return db.update("Insert into driver (username,password,email,phonenumber,national_id, license," +
                    "suspended,verified) values ( '" +
                    cred.get("userName") + "' ,'" + cred.get("password") + "' ,'" + cred.get("email") + "' ,'" + cred.get("phoneNumber") + "' ,'" +
                    cred.get("nationalId") + "' , '" + cred.get("licence") + "', 'false', 'false');");
        else return false;
    }

    /**
     * Logs in a user
     *
     * @param username The username to be logged in
     * @param password The password to be logged in
     * @return {@code true} if the user is logged in successfully, {@code false} otherwise
     */
    public boolean login(String username, String password) {
        Database db = Database.getInstance();
        ResultSet resultSet = db.query("SELECT username FROM customer WHERE username= '" + username + "' AND password = '" + password + "'");
        int size = 0;
        try {
            while (resultSet.next()) {
                size++;
            }
        } catch (java.sql.SQLException e) {
        }
        try {
            if (size != 0) {
                Account temp = AccountManager.getInstance().getAccount(username);
                if (temp.ableToSignIn()){
                    currentAccount = temp;
                    ((Customer) currentAccount).initPastRidesFromDB();
                }

                return temp.ableToSignIn();
            }
            resultSet = db.query("SELECT username FROM driver WHERE username= '" + username + "' AND password = " + " '" + password + "'");
            size = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    size++;
                }
            }
            if (resultSet != null && size != 0) {
                Account temp = AccountManager.getInstance().getAccount(username);
                if (temp.ableToSignIn()) {
                    currentAccount = temp;
                    //init favourite areas
                    ((Driver) currentAccount).initFavouriteAreasFromDB();

                    //init rates
                    ((Driver) currentAccount).initRatesFromDB();
                }
                return temp.ableToSignIn();
            }
            resultSet = db.query("SELECT username FROM admin WHERE username= '" + username + "' AND password = " + " '" + password + "'");
            size = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    size++;
                }
            }
            if (resultSet != null && size != 0) {
                Account temp = AccountManager.getInstance().getAccount(username);
                if (temp.ableToSignIn()){
                    currentAccount = temp;
                }

                return temp.ableToSignIn();
            } else return false;
        } catch (java.sql.SQLException e) {
            return false;
        }
    }
}
