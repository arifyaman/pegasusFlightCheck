package com.xlip.pegasusflightchecker.model;

import java.util.ArrayList;

public class AvailabilityResponse {
    private ArrayList<Flight> flights = new ArrayList<>();


    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
}
