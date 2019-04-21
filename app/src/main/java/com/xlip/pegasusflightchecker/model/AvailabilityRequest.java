package com.xlip.pegasusflightchecker.model;

public class AvailabilityRequest {
    private String ffRedemption;

    private String adultCount;

    private FlightSearchList[] flightSearchList;

    private String operationCode;

    private String dateOption;

    private String totalPoints;

    private String preventAdara;

    private String childCount;

    private String currency;

    private String personnelFlightSearch;

    private String affiliate;

    private String infantCount;

    public String getFfRedemption ()
    {
        return ffRedemption;
    }

    public void setFfRedemption (String ffRedemption)
    {
        this.ffRedemption = ffRedemption;
    }

    public String getAdultCount ()
    {
        return adultCount;
    }

    public void setAdultCount (String adultCount)
    {
        this.adultCount = adultCount;
    }

    public FlightSearchList[] getFlightSearchList ()
    {
        return flightSearchList;
    }

    public void setFlightSearchList (FlightSearchList[] flightSearchList)
    {
        this.flightSearchList = flightSearchList;
    }

    public String getOperationCode ()
    {
        return operationCode;
    }

    public void setOperationCode (String operationCode)
    {
        this.operationCode = operationCode;
    }

    public String getDateOption ()
    {
        return dateOption;
    }

    public void setDateOption (String dateOption)
    {
        this.dateOption = dateOption;
    }

    public String getTotalPoints ()
    {
        return totalPoints;
    }

    public void setTotalPoints (String totalPoints)
    {
        this.totalPoints = totalPoints;
    }

    public String getPreventAdara ()
    {
        return preventAdara;
    }

    public void setPreventAdara (String preventAdara)
    {
        this.preventAdara = preventAdara;
    }

    public String getChildCount ()
    {
        return childCount;
    }

    public void setChildCount (String childCount)
    {
        this.childCount = childCount;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getPersonnelFlightSearch ()
    {
        return personnelFlightSearch;
    }

    public void setPersonnelFlightSearch (String personnelFlightSearch)
    {
        this.personnelFlightSearch = personnelFlightSearch;
    }

    public String getAffiliate ()
    {
        return affiliate;
    }

    public void setAffiliate (String affiliate)
    {
        this.affiliate = affiliate;
    }

    public String getInfantCount ()
    {
        return infantCount;
    }

    public void setInfantCount (String infantCount)
    {
        this.infantCount = infantCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ffRedemption = "+ffRedemption+", adultCount = "+adultCount+", flightSearchList = "+flightSearchList+", operationCode = "+operationCode+", dateOption = "+dateOption+", totalPoints = "+totalPoints+", preventAdara = "+preventAdara+", childCount = "+childCount+", currency = "+currency+", personnelFlightSearch = "+personnelFlightSearch+", affiliate = "+affiliate+", infantCount = "+infantCount+"]";
    }

}
