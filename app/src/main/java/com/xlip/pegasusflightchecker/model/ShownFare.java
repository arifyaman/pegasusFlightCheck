package com.xlip.pegasusflightchecker.model;

import java.io.Serializable;

public class ShownFare implements Serializable {


    private static final long serialVersionUID = 2511187093302891781L;


    private String currency;
    private String amount;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
