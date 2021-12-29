package model;

import controller.Account;
import controller.Customer;
import controller.Driver;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager class that manages authentication in the application
 *
 * @author Khaled Waleed
 */
public class AuthenticationManager {
    private Account currentAccount;
    private static AuthenticationManager singletonInstance;

    private AuthenticationManager() {}

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
        cred.put("username", acc.getUserName());
        cred.put("password", acc.password);
        if (acc instanceof Customer) {
            cred.put("email", ((Customer) acc).getEmail());
            cred.put("phoneNumber", ((Customer) acc).getPhoneNumber());
            cred.put("birthday", ((Customer) acc).getBirthday().toString());
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
        ResultSet resultSet = db.query("SELECT username from [dbo].[account] WHERE username= '" + cred.get("username") + "'");
        int counter = 0;
        try {
            while (resultSet.next())
                counter++;
        } catch (java.sql.SQLException e) {
            System.out.println("SQL error!");
            return false;
        }
        if (counter != 0) return false;
        boolean insertToAccount = db.update("INSERT into account VALUES ( '" +
                cred.get("username") + "', '" + cred.get("password") + "')");

        if (acc instanceof Customer)
            return insertToAccount && db.update("INSERT into [dbo].[customer] VALUES ( '" +
                    cred.get("username") + "' ,'" + cred.get("email") + "' ,'" + cred.get("phoneNumber") + "' ," +
                    " 'false' , '" + cred.get("birthday") + "')");
        else if (acc instanceof Driver)
            return db.update("Insert into [dbo].[driver] (username,email,phonenumber,national_id, license," +
                    "suspended,verified) values ( '" +
                    cred.get("username") + "' ,'" + cred.get("email") + "' ,'" + cred.get("phoneNumber") + "' ,'" +
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
        ResultSet resultSet = db.query("SELECT username FROM [dbo].[account] WHERE username= '" + username + "' AND password = '" + password + "'");
        int size = 0;
        try {
            while (resultSet.next()) {
                size++;
            }
        } catch (java.sql.SQLException e) {
        }
        if (size != 0) {
            Account temp = AccountManager.getInstance().getAccount(username);
            if (temp.ableToSignIn()) {
                currentAccount = temp;
                if (temp instanceof Customer) ((Customer) currentAccount).initPastRidesFromDB();
            }
            return temp.ableToSignIn();
        }
        return false;
    }
}
