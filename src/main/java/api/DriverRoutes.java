package api;

import controller.Driver;
import controller.Offer;
import controller.Rate;
import controller.Request;
import model.AuthenticationManager;
import model.RideManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DriverRoutes {
    private final AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
    private final RideManager rideManager = RideManager.getInstance();

    @PutMapping("driver/register")
    public boolean register(@RequestBody Driver driver){
        return authenticationManager.register(driver);
    }

    @GetMapping("driver/favouritearea")
    public List<String> getFavouriteAreas() {
        return ((Driver) authenticationManager.getCurrentAccount()).getFavouriteAreas();
    }

    @PutMapping("driver/favouritearea/{area}")
    public void addFavouriteArea(@PathVariable String area) {
        ((Driver) authenticationManager.getCurrentAccount()).addFavouriteArea(area);
    }

    @DeleteMapping("driver/favouritearea/{area}")
    public void deleteFavouriteArea(@PathVariable String area) {
        ((Driver) authenticationManager.getCurrentAccount()).deleteFavouriteArea(area);
    }

    @GetMapping("driver/requests")
    public List<Request> getRequests() {
        return rideManager.getRequests();
    }

    @PostMapping("driver/offer/{requestID}")
    public void makeOffer(@PathVariable String requestID, @RequestParam float price) {
        Request request = rideManager.getRequestById(requestID);
        request.makerOffer(
                (Driver) authenticationManager.getCurrentAccount(),
                price
        );
    }

    @GetMapping("driver/ratings")
    public List<Rate> getAllRates() {
        return ((Driver) authenticationManager.getCurrentAccount()).getRates();
    }

    @GetMapping("driver/activerides")
    public List<Offer> getActiveRides() {
        return ((Driver) authenticationManager.getCurrentAccount()).getActiveOffers();
    }

    @GetMapping("driver/profile")
    public Driver getDriverProfile() {
        return ((Driver) authenticationManager.getCurrentAccount());
    }

    @PostMapping("driver/pickup/{offerId}")
    public void pickUp(@PathVariable String offerId) {
        ((Driver) authenticationManager.getCurrentAccount()).pickUpCustomer(rideManager.getOfferById(offerId));
    }

    @PostMapping("driver/drop/{offerId}")
    public void drop(@PathVariable String offerId) {
        ((Driver) authenticationManager.getCurrentAccount()).dropCustomer(rideManager.getOfferById(offerId));
    }
}
