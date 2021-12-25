package model;
/**
 *  Manager class that Receives Events and stores them for the purpose of maintaining a log
 *
 *  @author Khaled Waleed
 */

import controller.Event;
import controller.Offer;
import controller.Request;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EventManager
{
    private Calendar calender;



    /**
     * Event Manager Constructor
     */
    private EventManager()
    {
        Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+2"));
    }

    /**
     * Receive an event and stores it in the Database
     * @param event
     */
    public void receiveEvent(Event event)
    {
        //Extract data from event
        String name = event.getEventName();
        Date date   = event.getEventDate();
        Offer offer = event.getOffer();

        //init database
        Database db = Database.getInstance();

        //Extract date as a string
        calender.setTime(date);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        String sDate = "'"+year+"'-'"+month+"'-'"+day+"'";

        //Get offer ID
        int offerID=0;
        ResultSet table = db.query( "SELECT offer_id " +
                                                "FROM offer " +
                                                "WHERE driver_id = '"+ offer.getDriver().getUserName() +"' "+
                                                "AND ride_id = '"+offer.getRequest().getId()+"'"+
                                                "AND accepted = 1");
        try
        {
            table.next();   //Advance one step
            offerID =table.getInt(0);
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("Offer not Found in the Database");
        }

        //Build the query
        db.update("INSERT into Events VALUES ( '"+name+"' ,'"+date+"' ,"+offerID);

    }

    /**
     * Overload of the receiveEvent(Event) function
     *
     * @param name of the event
     * @param date of the event
     * @param offer as the source of the event
     */
    public void receiveEvent (String name, Date date, Offer offer)
    {
        receiveEvent(new Event(name, date, offer));
    }
    public ArrayList<Event> getAllEvents()
    {

        return null;
    }
    public ArrayList<Event> getEventsForRide(Request request)
    {


        return null;
    }
}
