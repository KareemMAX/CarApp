package model;
/**
 *  Manager class that Receives Events and stores them for the purpose of maintaining a log
 *
 *  @author Khaled Waleed
 */

import controller.*;

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

        //Extract date as a string. must be in this format: 2020-12-20 12:41:00
        char x='-'; //date separator //can be '/'
        char z=':'; //time separator
        calender.setTime(date);
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int min = calender.get(Calendar.MINUTE);
        int sec = calender.get(Calendar.SECOND);
        String sDate = String.valueOf(year)+x+month+x+day+' ';
        sDate+= hour+z+min+z+sec;

        //Get offer ID
        int offerID=0;
        ResultSet table = db.query( "SELECT offerID " +
                "FROM offer " +
                "WHERE driverUsername = '"+ offer.getDriver().getUserName() +"' "+
                "AND requestID = '"+offer.getRequest().getId()+"'"+
                "AND accepted = 1");
        try
        {
            table.next();   //Advance one step
            offerID =table.getInt(0);
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("Offer was not found in the Database");
        }

        //Build the query
        db.update("INSERT into event VALUES ( '"+name+"' ,'"+sDate+"' ,"+offerID);

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

    /**
     * Display All Events in the database
     * @return
     */
    public ArrayList<Event> getAllEvents()
    {
        ArrayList<Event> resultEvents = new ArrayList<>();
        Database db = Database.getInstance();
        ResultSet table;
        String query =  "SELECT * FROM event " +
                        "LEFT JOIN offer " +
                        "ON event.offerID = offer.offerID"+
                        "LEFT JOIN request " +
                        "ON offer.requestID = request.requestID";
        table = db.query(query);
        resultEvents = extractEventsFromResultSet(table);
        return resultEvents;
    }
    public ArrayList<Event> getEventsForRide(Request request)
    {
        ArrayList<Event> resultEvents = new ArrayList<>();
        Database db = Database.getInstance();
        ResultSet table;
        String query =  "SELECT * FROM event " +
                        "LEFT JOIN offer " +
                        "ON event.offerID = offer.offerID " +
                        "LEFT JOIN request " +
                        "ON offer.requestID = request.requestID " +
                        "WHERE requestID ='" + request.getId()+"'";
        table = db.query(query);
        resultEvents = extractEventsFromResultSet(table);
        return resultEvents;
    }

    /**
     * Extracts events as objects form the database
     * @param table initialized with a query joining the tables (event,offer,request)
     * @return
     */
    private ArrayList<Event> extractEventsFromResultSet(ResultSet table)
    {
        ArrayList<Event> resultEvents = new ArrayList<>();
        try
        {
            while (table.next())
            {
                //Extract internal objects
                Customer customer =
                        (Customer) AccountManager.getInstance().getAccount(table.getString("customerUsername"));
                Request request =
                        new Request(table.getInt("RequestID"),table.getString("source"),
                                table.getString("destination"),customer,table.getInt("numberOfPassengers"));
                Driver driver =
                        (Driver) AccountManager.getInstance().getAccount(table.getString("driverUsername"));

                Offer offer =
                        new Offer(table.getInt("offerID"),request,table.getInt("price"),driver);

                Date date = table.getDate("time");
                String name = table.getString("name");

                //Plug into the event
                Event event = new Event(name,date,offer);
                resultEvents.add(event);
            }
        }
        catch (java.sql.SQLException e)
        {
            System.out.println("ERROR in event retrieval!");
        }
        return resultEvents;
    }
}