package controller;

/**
 * class for make discount to an offer {@link Offer} .
 *
 * @author Andrew Naseif
 */
public class Discount extends Offer {
    private final float discount;
    private Offer offer;

    /**
     * Discount parameterized constructor
     *
     * @param offer    The original {@link Offer} to make discount for it
     * @param discount The the value of the discount
     */
    public Discount(Offer offer, float discount) {
        super(
                offer.getId(),
                offer.getRequest(),
                offer.getOfferPrice(),
                offer.getDriver()
        );
        this.offer = offer;
        this.discount = discount;
    }

    /**
     * rejects the offer with this discount{@link Offer} object and clear it from memory
     */
    public void reject() {
        offer.reject();
    }

    /**
     * Accepts the offer with this discount while rejecting any other existing offers to the same {@link Request} object
     */
    public void accept() {
        offer.accept();
    }

    /**
     * function to get the request of the discount object
     *
     * @return The request {@link Request}
     */
    @Override
    public Request getRequest() {
        return offer.getRequest();
    }

    /**
     * function to get the driver who make the offer of this discount
     *
     * @return The driver {@link Driver}
     */
    @Override
    public Driver getDriver() {
        return offer.getDriver();
    }

    /**
     * The Discount's database ID
     *
     * @return The discount's database ID
     */
    @Override
    public String getId() {
        return offer.getId();
    }

    /**
     * The offer proposed price after apply discount
     *
     * @return The offer proposed price
     */
    @Override
    public float getOfferPrice() {
        return offer.getOfferPrice() - (offer.getOfferPrice() * (discount / 100));
    }

    /**
     * The price paid after applying discount
     *
     * @return The offer price after discount paid to the driver
     */
    @Override
    public float getPaidPrice() {
        return offer.getPaidPrice();
    }

    @Override
    public String toString() {
        return "Offer{" +
                "request=" + offer.getRequest() +
                ", offerPrice=" + getOfferPrice() +
                ", driver=" + offer.getDriver().getUserName() +
                ", accepted=" + offer.isAccepted() +
                ", discount applied=" + discount + " %" +

                '}';
    }
}
