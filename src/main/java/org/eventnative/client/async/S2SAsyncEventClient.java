package org.eventnative.client.async;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class S2SAsyncEventClient extends AsyncEventClient {
    private static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";
    private final Logger logger = LoggerFactory.getLogger(S2SAsyncEventClient.class);

    public S2SAsyncEventClient(String baseUrl, String token, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, queueCapacity);
    }

    public S2SAsyncEventClient(String baseUrl, String token, OkHttpClient client, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, client, queueCapacity);
    }

    public boolean sendEvent(JsonObject event) {
        if (event == null) {
            throw new IllegalArgumentException("event should be a valid JSON object");
        }
        logger.debug("Adding event [{}] to the queue", event);
        return addEvent(event);
    }
}
