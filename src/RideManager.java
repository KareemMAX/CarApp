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
}
