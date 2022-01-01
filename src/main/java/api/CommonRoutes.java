package api;


import api.data.RestCredentials;
import controller.*;
import model.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommonRoutes {
    private final AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

    @PostMapping("login")
    public Account login(@RequestBody RestCredentials user){
        if (authenticationManager.login(user.getUsername(), user.getPassword()))
            return authenticationManager.getCurrentAccount();
        else return null;
    }

    @PostMapping("logout")
    public void logout() {
        authenticationManager.logout();
    }
}
