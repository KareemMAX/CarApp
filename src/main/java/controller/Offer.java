package controller;

import model.EventManager;
import model.RideManager;

import java.util.Date;

/**
 * A full offer provided from the driver to a user {@link Request}.
 *
 * @author Kareem Morsy
 */
public class Offer {
    private final String id;
    private final Request request;
    private final float offerPrice;
    private final Driver driver;
    private boolean accepted;

    private final RideManager rideManager = RideManager.getInstance();

    /**
     * Offer default constructor
     * @param request The original {@link Request} that the offer is for
     * @param offerPrice The offer proposed price
     * @param driver The {@link Driver} object offering
     */
    public Offer(String id, Request request, float offerPrice, Driver driver) {
        this.id = id;
        this.request = request;
        this.offerPrice = offerPrice;
        this.driver = driver;
        accepted = false;
    }

    /**
     * controller.Offer constructor with accepted parameter
     * @param request The original {@link Request} that the offer is for
     * @param offerPrice The offer proposed price
     * @param driver The {@link Driver} object offering
     * @param accepted Is the ride offer accepted or not
     */
    public Offer(String id, Request request, float offerPrice, Driver driver, boolean accepted) {
        this(id, request, offerPrice, driver);
        this.accepted = accepted;
    }

    /**
     * The original {@link Request} that the offer is for
     * @return The original {@link Request} that the offer is for
     */
    public Request getRequest() {
        return request;
    }

    /**
     * The offer proposed price
     * @return The offer proposed price
     */
    public float getOfferPrice() {
        return offerPrice;
    }

    /**
     * The offer price paid to the driver
     * @return The offer price paid to the driver
     */
    public float getPaidPrice() {
        return offerPrice;
    }

    /**
     * The {@link Driver} object offering
     * @return The {@link Driver} object offering
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * The offer's database ID
     * @return The offer's database ID
     */
    public String getId() {
        return id;
    }

    /**
     * Accepts the offer while rejecting any other existing offers to the same {@link Request} object
     */
    public void accept() {
        accepted = true;
        rideManager.setOfferAccepted(this, true);
        //Send event
        Event event = new Event("offer acceptance",new Date(),this);
        EventManager.getInstance().receiveEvent(event);
    }

    /**
     * @return     true if the offer is accepted (IE completed/in-progress Ride) else returns false
     * */
    public boolean isAccepted(){
        return accepted;
    }

    /**
     * Rejects the offer and clear it from memory/storage
     */
    public void reject() {
        accepted = false;
        rideManager.setOfferAccepted(this, false);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "request=" + request +
                ", offerPrice=" + offerPrice +
                ", driver=" + driver.getUserName() +
                ", accepted=" + accepted +
                '}';
    }
}
