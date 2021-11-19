import java.util.List;

public class Request
{
    private String source;
    private String destination;
    private Customer user;

    private final RideManager rideManager = RideManager.getInstance();

    public List<Offer> getAllOffers() {
        return rideManager.listOffers(this);
    }

    public void makerOffer(Driver driver, float price) {
        rideManager.makeOffer(driver, this, price);
    }
}
