public class Offer {
    private final Request request;
    private final float offerPrice;
    private final Driver driver;

    private final RideManager rideManager = RideManager.getInstance();

    public Offer(Request request, float offerPrice, Driver driver) {
        this.request = request;
        this.offerPrice = offerPrice;
        this.driver = driver;
    }

    public Request getRequest() {
        return request;
    }

    public float getOfferPrice() {
        return offerPrice;
    }

    public Driver getDriver() {
        return driver;
    }

    public void accept() {
        rideManager.setOfferAccepted(this, true);
    }

    public void reject() {
        rideManager.setOfferAccepted(this, false);
    }
}
