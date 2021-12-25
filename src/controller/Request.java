package controller;

import model.RideManager;

import java.util.List;

/**
 * A request from the {@link Customer} to have a ride from a specific source to a specific destination.
 *
 * @author Kareem Morsy
 */
public class Request {
    private final RideManager rideManager = RideManager.getInstance();
    private final String id;
    private final String source;
    private final String destination;
    private final Customer user;

    /**
     * controller.Request default constructor
     *
     * @param source      The source area of the request
     * @param destination The destination area of the request
     * @param user        The requested {@link Customer}
     */
    public Request(String id, String source, String destination, Customer user) {
        this.source = source;
        this.destination = destination;
        this.user = user;
        this.id = id;
    }

    /**
     * The database ID
     *
     * @return controller.Request ID
     */
    public String getId() {
        return id;
    }

    /**
     * The source area of the request
     *
     * @return The source area of the request
     */
    public String getSource() {
        return source;
    }

    /**
     * The destination area of the request
     *
     * @return The destination area of the request
     */
    public String getDestination() {
        return destination;
    }

    /**
     * The requested {@link Customer}
     *
     * @return The requested {@link Customer}
     */
    public Customer getUser() {
        return user;
    }

    /**
     * Gets all the offers offered to this request
     *
     * @return A list of {@link Offer} that contains a price and a {@link Driver}
     */
    public List<Offer> getAllOffers() {
        return rideManager.listOffers(this);
    }

    /**
     * Purpose an {@link Offer} to this request
     *
     * @param driver The {@link Driver} offering the offer
     * @param price  The price purposed
     */
    public void makerOffer(Driver driver, float price) {
        rideManager.makeOffer(driver, this, price);
    }

    /**
     * @return String version of request object
     */
    @Override
    public String toString() {
        return "Source : " + source + " ,Destination : " + destination + " ,Username : " + user.getUserName();
    }
}
