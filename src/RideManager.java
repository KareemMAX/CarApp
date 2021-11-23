import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class RideManager {
    private static RideManager singletonInstance;

    public static RideManager getInstance() {
        if (singletonInstance == null) singletonInstance = new RideManager();
        return singletonInstance;
    }


    private final Database db =Database.getInstance();
    private final AccountManager accountManager = AccountManager.getInstance();

    public List<Offer> listOffers(Request request) {
        ResultSet table= db.query("SELECT * FROM offer\n" +
                "right JOIN request ON offer.offer_id=request.request_id WHERE request.request_id=" + request.getId());

        ArrayList<Offer> result = new ArrayList<>();

        try
        {
            while (table.next()) {
                Offer offer = new Offer(
                        request,
                        table.getFloat("price"),
                        (Driver) accountManager.getAccount(table.getString("driver_id"))
                );

                result.add(offer);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    public void makeOffer(Driver driver, Request request, float price) {
        Offer offer = new Offer(request,price, driver);
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO offer (driver_id, accepted, ride_id, price)\n");
        sqlQuery.append("VALUES (");
        sqlQuery.append("'").append(driver.getUserName()).append("',");
        sqlQuery.append(0).append(",");
        sqlQuery.append(request.getId()).append(",");
        sqlQuery.append(price).append(");");
        db.update(sqlQuery.toString());
        request.getUser().notify(offer);
    }
    public void setOfferAccepted(Offer offer, boolean accepted) {
        if(accepted){
            db.update("UPDATE offer \n" +
                    "SET \n" +
                    "    accepted = 1\n" +
                    "WHERE\n" +
                    " ride_id ="+offer.getRequest().getId()+"AnD driver_id="+offer.getDriver().getUserName());
            db.update("DELETE FROM offer WHERE accepted=0;\n");
        }else {
            db.update("DELETE FROM offer WHERE ride_id ="+offer.getRequest().getId()+"AnD driver_id="+offer.getDriver().getUserName());
        }
    }

    public boolean makeRequest(String source, String Destination, Customer account){
        //TODO Create request and return true/false accordingly
        return true;
    }

    public List<Request> getRequests(){
        //TODO returns list of all requests
        return new ArrayList<Request>();
    }

    public Driver getLastRideDriver(Customer account){
        //TODO Return last ride's driver for user provided
        return new Driver("", "", "", "", "");
    }
}
