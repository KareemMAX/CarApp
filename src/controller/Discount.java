package controller;

public class Discount extends Offer {
    private final float discount;
    private Offer offer;

    public Discount(Offer offer, float discount) {
        super(
            offer.getId(),
            offer.getRequest(),
            offer.getOfferPrice(),
            offer.getDriver()
        );
        this.discount = discount;
    }

    public void reject() {
        offer.reject();
    }

    public void accept() {
        offer.accept();
    }

    @Override
    public float getOfferPrice() {
        return offer.getOfferPrice() - (offer.getOfferPrice() / (discount / 100));
    }


}
