package CarApp.webService;

import model.RideManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerControler {
    @Autowired
    private final RideManager rideManager=RideManager.getInstance();

}
