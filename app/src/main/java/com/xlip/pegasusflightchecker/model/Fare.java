package com.xlip.pegasusflightchecker.model;

import java.io.Serializable;

public class Fare implements Serializable {
    private static final long serialVersionUID = -8991082208602145002L;

    private String tariffFareId;
    private String segmentId;
    private String reservationClass;
    private Bundle[] bundleList;

    public String getTariffFareId() {
        return tariffFareId;
    }

    public void setTariffFareId(String tariffFareId) {
        this.tariffFareId = tariffFareId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getReservationClass() {
        return reservationClass;
    }

    public void setReservationClass(String reservationClass) {
        this.reservationClass = reservationClass;
    }

    public Bundle[] getBundleList() {
        return bundleList;
    }

    public void setBundleList(Bundle[] bundleList) {
        this.bundleList = bundleList;
    }
}
