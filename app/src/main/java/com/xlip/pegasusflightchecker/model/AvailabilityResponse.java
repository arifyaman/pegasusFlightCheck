package com.xlip.pegasusflightchecker.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AvailabilityResponse implements Serializable {

    private static final long serialVersionUID = 6778342545120679311L;

    private ArrayList<Flight> flights = new ArrayList<>();


    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
}
