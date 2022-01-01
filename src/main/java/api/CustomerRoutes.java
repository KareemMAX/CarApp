package api;

import api.data.RestRequest;
import controller.Customer;
import controller.Driver;
import controller.Offer;
import controller.Request;
import model.AccountManager;
import model.AuthenticationManager;
import model.RideManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerRoutes {
    private final AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
    private final RideManager rideManager = RideManager.getInstance();

    @PutMapping("customer/register")
    public boolean register(@RequestBody Customer customer){
        return authenticationManager.register(customer);
    }

    @PutMapping("customer/request")
    public void requestRide(@RequestBody RestRequest request) {
        rideManager.makeRequest(
                request.getSource(),
                request.getDestination(),
                (Customer) authenticationManager.getCurrentAccount(),
                request.getNumberOfPassengers()
        );
    }

    @PutMapping("customer/rate/{driverUsername}")
    public void rateDriver(@PathVariable String driverUsername, @RequestParam float rate) {
        Driver driver = (Driver) AccountManager.getInstance().getAccount(driverUsername);
        driver.rate(
                (Customer) authenticationManager.getCurrentAccount(),
                rate
        );
    }

    @GetMapping("customer/offers")
    public List<Offer> getAllOffers() {
        List<Offer> result = new ArrayList<>();
        for (Request r :
                rideManager.getRequests()) {
            if (authenticationManager.getCurrentAccount().getUserName().equals(r.getUser().getUserName()))
                result.addAll(r.getAllOffers());
        }

        return result;
    }

    @PostMapping("customer/offers/{offerId}/accept")
    public void acceptOffer(@PathVariable String offerId) {
        rideManager.getOfferById(offerId).accept();
    }

    @PostMapping("customer/offers/{offerId}/reject")
    public void rejectOffer(@PathVariable String offerId) {
        rideManager.getOfferById(offerId).reject();
    }

    @GetMapping("customer/pastrides")
    public List<Offer> getPastRides() {
        return ((Customer) authenticationManager.getCurrentAccount()).getPastRides();
    }
}
