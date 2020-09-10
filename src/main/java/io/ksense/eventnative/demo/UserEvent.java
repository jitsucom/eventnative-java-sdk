package io.ksense.eventnative.demo;

import com.google.gson.JsonObject;

/**
 * This is a class that is only used for demo purposes as an example of serialization Java class to JsonObject
 */
public class UserEvent {
    private final String eventId;
    private final JsonObject user;

    public UserEvent(String eventId, JsonObject user) {
        this.eventId = eventId;
        this.user = user;
    }
}
