package com.xlip.pegasusflightchecker.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyData implements Serializable {
    private static final long serialVersionUID = 5052609288549103588L;
    private List<SearchObject> searchObjects = new ArrayList<>();



    public List<SearchObject> getSearchObjects() {
        return searchObjects;
    }

    public void setSearchObjects(List<SearchObject> searchObjects) {
        this.searchObjects = searchObjects;
    }
}
