package CarApp.webService;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class CustomerControler {
    @Autowired
    private AccountManager accountManager=AccountManager.getInstance();

}
