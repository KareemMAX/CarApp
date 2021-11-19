import java.util.List;

public class Request {
    private final RideManager rideManager = RideManager.getInstance();
    private String source;
    private String destination;
    private Customer user;

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Customer getUser() {
        return user;
    }

    public List<Offer> getAllOffers() {
        return rideManager.listOffers(this);
    }

    public void makerOffer(Driver driver, float price) {
        rideManager.makeOffer(driver, this, price);
    }
}
