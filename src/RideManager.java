import java.util.ArrayList;
import java.util.List;

public class RideManager {
    private static RideManager singletonInstance;

    public static RideManager getInstance() {
        if (singletonInstance == null) singletonInstance = new RideManager();
        return singletonInstance;
    }

    public List<Offer> listOffers(Request request) {
        return new ArrayList<>();
    }

    public void makeOffer(Driver driver, Request request, float price) {

    }

    public void setOfferAccepted(Offer offer, boolean accepted) {
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
