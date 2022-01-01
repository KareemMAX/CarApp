package controller;
import model.DiscountManager;
import view.AdminInterface;
import view.DriverInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * controller.Admin account class
 *
 * @author Khaled Waleed
 */
public class Admin extends Account {
    private List<PublicHolidays> publicHolidays;
    private ArrayList<String> discountDestination;

    /**
     * Creates a new driver account with the parameters as the account details
     *
     * @param username The username associated with this account
     * @param password The password associated with this account
     */
    public Admin(String username, String password) {
        super(username, password);
        setUserInterface(new AdminInterface());
    }

    /**
     * Indicates the ability of the controller.Admin class to sign in
     *
     * @return boolean value indicator that is always true for admins
     */
    public boolean ableToSignIn() {
        return true;
    }

    public void addNewHoliday(Date date, String name) {
        PublicHolidays holiday = new PublicHolidays(date, name);
        publicHolidays.add(holiday);
        holiday.addNewHoliday();

    }
    public void addDiscountDestination(String destination){
        discountDestination.add(destination);
        DiscountManager.getInstance().addDiscountDestination(destination);
    }
}
