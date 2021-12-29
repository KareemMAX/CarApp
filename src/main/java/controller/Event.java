package controller;

import java.util.Date;

public class Event
{
    private String eventName;
    private Date eventDate;
    private Offer offer;

    public Event(String eventName, Date eventDate, Offer offer)
    {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.offer = offer;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public Date getEventDate()
    {
        return eventDate;
    }

    public void setEventDate(Date eventDate)
    {
        this.eventDate = eventDate;
    }

    public Offer getOffer()
    {
        return offer;
    }

    public void setOffer(Offer offer)
    {
        this.offer = offer;
    }
}
