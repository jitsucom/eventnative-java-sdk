package org.eventnative.client;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;

public class C2SAsyncClient extends AsyncEventClient {

    public static final String C2S_EVENT_URL = "/api/v1/event?token=";

    public C2SAsyncClient(String baseUrl, String token, int queueCapacity) {
        super(baseUrl, C2S_EVENT_URL, token, queueCapacity);
    }

    public C2SAsyncClient(String baseUrl, String token, OkHttpClient client, int queueCapacity) {
        super(baseUrl, C2S_EVENT_URL, token, client, queueCapacity);
    }

    public boolean sendEvent(JsonObject eventContext, JsonObject properties) {
        properties.add("eventn_ctx", eventContext);
        return addEvent(properties);
    }
}
