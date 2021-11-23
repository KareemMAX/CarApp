import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthenticationManager {
    private Account currentAccount;
    private static AuthenticationManager singletonInstance;


    public static AuthenticationManager getInstance() {
        if (singletonInstance == null) singletonInstance = new AuthenticationManager();
        return singletonInstance;
    }

    public Account getCurrentAccount() {
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
        //check if exists
        Database db = Database.getInstance();
        ResultSet resultSet = db.query("SELECT username from customer WHERE username= '"+cred.get("userName")+"'" +
                                         "UNION SELECT username from driver WHERE username= '"+cred.get("userName")+"'");
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
        if (counter != 0) return false;

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

    public boolean login(String username, String password) {
        Database db = Database.getInstance();
        try
        {
            ResultSet resultSet = db.query("SELECT username FROM customer WHERE username= '"+username+"' AND password = "+" '"+password+"'");
            int size=0;
            if (resultSet != null)
            {
                resultSet.last();    // moves cursor to the last row
                size = resultSet.getRow(); // get row id
            }
            if (resultSet!=null && size !=0)
            {
                currentAccount = AccountManager.getInstance().getAccount(username);
                //init past rides //TODO
                ArrayList<Offer> rides= new ArrayList<Offer>();
                //ResultSet ridesTable = db.query()
                return true;
            }
            resultSet = db.query("SELECT username FROM customer WHERE username= '"+username+"' AND password = "+" '"+password+"'");
            size = 0;
            if (resultSet != null)
            {
                resultSet.last();    // moves cursor to the last row
                size = resultSet.getRow(); // get row id
            }
            if (resultSet!=null && size !=0)
            {
                currentAccount = AccountManager.getInstance().getAccount(username);
                //init favourite areas
                ArrayList<String> areas = new ArrayList<String>();
                ResultSet areasTable = db.query("SELECT favourite_place FROM favourite_places WHERE driver_id = '"+ currentAccount.getUserName()+"'");

                while (areasTable.next())
                    areas.add(areasTable.getString("favourite_place"));
                ((Driver)currentAccount).setFavouriteAreas(areas);

                //init rates //TODO


                return true;
            }
            else return false;
        }
        catch (java.sql.SQLException e)
        {
            return false;
        }
    }
}
