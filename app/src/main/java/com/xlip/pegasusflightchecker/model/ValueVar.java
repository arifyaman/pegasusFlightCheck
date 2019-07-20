package com.xlip.pegasusflightchecker.model;

import java.io.Serializable;

public class ValueVar implements Serializable {


    private static final long serialVersionUID = 6912965019139279736L;

    private String bundleId;
    private String bundleType;

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
}
