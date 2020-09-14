package org.eventnative.client;

import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.eventnative.model.EventNativeResponse;
import org.eventnative.model.WebEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EventNativeSyncClient extends AbstractClient {

    private final Logger logger = LoggerFactory.getLogger(EventNativeSyncClient.class);

    public EventNativeSyncClient(String backendBaseUrl, String s2sToken) {
        super(backendBaseUrl, s2sToken);
    }

    public EventNativeSyncClient(String backendBaseUrl, String s2sToken, OkHttpClient client) {
        super(backendBaseUrl, s2sToken, client);
    }

    public EventNativeResponse sendEvent(JsonObject event) throws IOException {
        if (event == null) {
            throw new IllegalArgumentException("event should be a valid JSON object");
        }
        logger.debug("Sending event {}", event);
        RequestBody body = RequestBody.create(event.toString(), MediaType.get("application/json; charset=utf-8"));
        return sendRequest(body);
    }

    public EventNativeResponse sendWebEvent(WebEvent event) throws IOException {
        return sendEvent(event.toJson());
    }
}
