package com.xlip.pegasusflightchecker.model;

public class FlightSearchList
{
    private String arrivalPort;

    private String departurePort;

    private String departureDate;

    public String getArrivalPort ()
    {
        return arrivalPort;
    }

    public void setArrivalPort (String arrivalPort)
    {
        this.arrivalPort = arrivalPort;
    }

    public String getDeparturePort ()
    {
        return departurePort;
    }

    public void setDeparturePort (String departurePort)
    {
        this.departurePort = departurePort;
    }

    public String getDepartureDate ()
    {
        return departureDate;
    }

    public void setDepartureDate (String departureDate)
    {
        this.departureDate = departureDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [arrivalPort = "+arrivalPort+", departurePort = "+departurePort+", departureDate = "+departureDate+"]";
    }
}
