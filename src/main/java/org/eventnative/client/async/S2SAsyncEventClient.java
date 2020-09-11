package org.eventnative.client.async;

import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;

public class S2SAsyncEventClient extends AsyncEventClient {
    public static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";

    public S2SAsyncEventClient(String baseUrl, String token, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, queueCapacity);
    }

    public S2SAsyncEventClient(String baseUrl, String token, OkHttpClient client, int queueCapacity) {
        super(baseUrl, S2S_EVENT_URL, token, client, queueCapacity);
    }

    public boolean sendEvent(JsonObject event) {
        return addEvent(event);
    }
}
