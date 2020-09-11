package org.eventnative.client.async;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This client adds any events to the queue that is processed by a dedicated thread which sends events to backend.
 */
public class S2SAsyncEventClient extends AsyncEventClient {
    private static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";
    private final Logger logger = LoggerFactory.getLogger(S2SAsyncEventClient.class);

    public S2SAsyncEventClient(String baseUrl, String token, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, queueCapacity);
    }

    public S2SAsyncEventClient(String baseUrl, String token, OkHttpClient client, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, client, queueCapacity);
    }

    /**
     * Puts event to the queue. Separate thread sends events to server.
     *
     * @param event any JSON object
     * @return true if the event was added to the queue, false if the queue if full
     * @throws IllegalArgumentException if event is null
     */
    public boolean sendEvent(JsonObject event) {
        if (event == null) {
            throw new IllegalArgumentException("event should be a valid JSON object");
        }
        logger.debug("Adding event [{}] to the queue", event);
        return addEvent(event);
    }
}
