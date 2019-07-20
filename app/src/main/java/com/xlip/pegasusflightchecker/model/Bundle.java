package com.xlip.pegasusflightchecker.model;

import java.io.Serializable;

public class Bundle implements Serializable {


    private static final long serialVersionUID = -1426827960459473559L;

    private String bundleId;
    private String bundleType;
    private ShownFare shownFare;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleType() {
        return bundleType;
    }

    public void setBundleType(String bundleType) {
        this.bundleType = bundleType;
    }

    public ShownFare getShownFare() {
        return shownFare;
    }

    public void setShownFare(ShownFare shownFare) {
        this.shownFare = shownFare;
    }
}
