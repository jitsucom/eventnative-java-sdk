package org.eventnative.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AnyData {

    protected Map<String, Object> additionalFields = new HashMap<>();

//    public abstract JsonObject toJson();

    public void setProperty(String fieldName, Object value) {
        additionalFields.put(fieldName, value);
    }
}
