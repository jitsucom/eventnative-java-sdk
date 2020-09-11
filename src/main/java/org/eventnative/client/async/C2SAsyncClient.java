package org.eventnative.client.async;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2SAsyncClient extends AsyncEventClient {

    private static final String C2S_EVENT_URL = "/api/v1/event?token=";
    private final Logger logger = LoggerFactory.getLogger(C2SAsyncClient.class);

    public C2SAsyncClient(String baseUrl, String token, int queueCapacity) {
        super(baseUrl, C2S_EVENT_URL, token, queueCapacity);
    }

    public C2SAsyncClient(String baseUrl, String token, OkHttpClient client, int queueCapacity) {
        super(baseUrl, C2S_EVENT_URL, token, client, queueCapacity);
    }

    public boolean sendEvent(JsonObject eventContext, JsonObject properties) {
        logger.debug("event context: [{}], other properties: [{}]", eventContext, properties);
        if (eventContext == null) {
            throw new IllegalArgumentException("eventContext must be a valid JSON object");
        }
        properties = properties != null ? properties : JsonParser.parseString("{}").getAsJsonObject();
        properties.add("eventn_ctx", eventContext);
        return addEvent(properties);
    }
}
