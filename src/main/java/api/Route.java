package api;


import controller.*;
import model.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Route {

    @PostMapping("login")
    public Account login(@RequestBody creds user){
        if (AuthenticationManager.getInstance().login(user.getUsername(), user.getPassword()))
            return AuthenticationManager.getInstance().getCurrentAccount();
        else return null;
    }

    @PutMapping("customer/register")
    public boolean register(@RequestBody Customer customer){
        return AuthenticationManager.getInstance().register(customer);
    }

    @PutMapping("driver/register")
    public boolean register(@RequestBody Driver driver){
        return AuthenticationManager.getInstance().register(driver);
    }

    @GetMapping("admin/logs")
    public List<Event> getLogs(){
        return EventManager.getInstance().getAllEvents();
    }

    @GetMapping("admin/holiday/view")
    public List<PublicHolidays> getHolidays(){
        return DiscountManager.getInstance().getAllPublicHolidays();
    }

    @PutMapping("admin/holiday/add")
    public void addHoliday(@RequestBody PublicHolidays holiday){
        DiscountManager.getInstance().addNewPublicHolidayDB(holiday);
    }

    @GetMapping("admin/discount/view")
    public List<String> getDiscountDestination(){
        return DiscountManager.getInstance().getAllDiscountDestination();
    }

    @PutMapping("admin/discount/add")
    public void addDiscountDestination(@RequestBody String destination){
        DiscountManager.getInstance().addDiscountDestination(destination);
    }
    @GetMapping("admin/alldrivers")
    public List<Driver> getALlDriver(){
        return AccountManager.getInstance().getAllAccounts("driver");
    }
    @GetMapping("admin/allcustomer")
    public List<Customer> getALlCustomer(){
        return AccountManager.getInstance().getAllAccounts("customer");
    }
    @PostMapping("admin/verify/{userName}")
    public void setVerify(@PathVariable String userName,@RequestParam boolean verified){
       Driver driver=(Driver) AccountManager.getInstance().getAccount(userName);
       driver.setVerified(verified);
    }
    @PostMapping("admin/suspend/{userName}")
    public void setSuspend(@PathVariable String userName,@RequestParam boolean suspended){
        Account account= AccountManager.getInstance().getAccount(userName);
        account.setSuspended(suspended);
    }

}
