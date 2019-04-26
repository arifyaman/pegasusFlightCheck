package com.xlip.pegasusflightchecker.storage;

import java.io.Serializable;
import java.util.Calendar;

public class SearchObject implements Serializable {

    private static final long serialVersionUID = 179611265292244338L;
    private long id;
    private int depPort;
    private int arPort;
    private Calendar date;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDepPort() {
        return depPort;
    }

    public void setDepPort(int depPort) {
        this.depPort = depPort;
    }

    public int getArPort() {
        return arPort;
    }

    public void setArPort(int arPort) {
        this.arPort = arPort;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
