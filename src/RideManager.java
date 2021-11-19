import java.util.ArrayList;
import java.util.List;

public class RideManager {
    public static RideManager getInstance() {
        return new RideManager();
    }

    public List<Offer> listOffers(Request request) {
        return new ArrayList<>();
    }

    public void makeOffer(Driver driver, Request request, float price) {

    }

    public void setOfferAccepted(Offer offer, boolean accepted) {

    }
}
