package api;

import api.data.RestPublicHoliday;
import controller.*;
import model.AccountManager;
import model.DiscountManager;
import model.EventManager;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class AdminRoutes {
    @GetMapping("admin/logs")
    public List<Event> getLogs(){
        return EventManager.getInstance().getAllEvents();
    }

    @GetMapping("admin/holiday/view")
    public List<PublicHolidays> getHolidays(){
        return DiscountManager.getInstance().getAllPublicHolidays();
    }

    @PutMapping("admin/holiday/add")
    public void addHoliday(@RequestBody RestPublicHoliday holiday){
        PublicHolidays publicHoliday = new PublicHolidays(Date.valueOf(holiday.getDate()), holiday.getName());
        DiscountManager.getInstance().addNewPublicHolidayDB(publicHoliday);
    }

    @GetMapping("admin/discount/")
    public List<String> getDiscountDestination(){
        return DiscountManager.getInstance().getAllDiscountDestination();
    }

    @PutMapping("admin/discount/{destination}")
    public void addDiscountDestination(@PathVariable String destination){
        DiscountManager.getInstance().addDiscountDestination(destination);
    }

    @DeleteMapping("admin/discount/{destination}")
    public boolean deleteDiscountDestination(@PathVariable String destination){
        return DiscountManager.getInstance().deleteDiscountDestination(destination);
    }

    @GetMapping("admin/drivers")
    public List<Driver> getALlDriver(){
        return AccountManager.getInstance().getAllAccounts("driver");
    }
    @GetMapping("admin/customers")
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
