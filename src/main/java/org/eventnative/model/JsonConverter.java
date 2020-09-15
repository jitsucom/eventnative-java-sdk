package org.eventnative.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonConverter {
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static JsonObject toJsonObject(Object value) {
        return gson.toJsonTree(value).getAsJsonObject();
    }
}
