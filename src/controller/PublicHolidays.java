package controller;

import model.DiscountManager;

import java.util.Date;

public class PublicHolidays {
    private Date publicHolidayDate;
    private String name;

    public PublicHolidays(Date publicHolidayDate, String name) {
        this.publicHolidayDate = publicHolidayDate;
        this.name = name;
    }

    public void setPublicHolidayDate(Date publicHolidayDate) {
        this.publicHolidayDate = publicHolidayDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublicHolidayDate() {
        return publicHolidayDate;
    }

    public void addNewHoliday() {
        DiscountManager.getInstance().addNewPublicHolidayDB(this);
    }

    public String getName() {
        return name;
    }


}
