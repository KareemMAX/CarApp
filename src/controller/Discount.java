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
        this.offer=offer;
        this.discount = discount;
    }

    public void reject() {
        offer.reject();
    }

    public void accept() {
        offer.accept();
    }

    @Override
    public Request getRequest() {
        return offer.getRequest();
    }

    @Override
    public Driver getDriver() {
        return offer.getDriver();
    }

    @Override
    public String getId() {
        return offer.getId();
    }

    @Override
    public float getOfferPrice() {
        return offer.getOfferPrice() - (offer.getOfferPrice() * (discount / 100));
    }

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
                ", accepted=" + offer.isAccepted()+
                ", discount applied=" + discount +" %"+

                '}';
    }
}
