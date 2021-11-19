public class Offer
{
    private Request request;
    private float offerPrice;
    private Driver driver;

    private final RideManager rideManager = RideManager.getInstance();

    public void accept() {
        rideManager.setOfferAccepted(this, true);
    }

    public void reject() {
        rideManager.setOfferAccepted(this, false);
    }
}
