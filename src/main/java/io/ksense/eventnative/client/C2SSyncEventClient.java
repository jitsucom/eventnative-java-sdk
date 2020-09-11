package io.ksense.eventnative.client;

import com.google.gson.JsonObject;
import io.ksense.eventnative.model.EventNativeResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * This client is used to send client events (with 'eventn_ctx' node) synchronously.
 */
public class C2SSyncEventClient extends SyncEventClient {
    public static final String C2S_EVENT_URL = "/api/v1/event?token=";

    /**
     * Use this constructor if you want to create client with default connection parameters
     * (see defailt values at {@link OkHttpClient.Builder})
     * @param baseUrl URL of the eventnative backend
     * @param C2SToken client token
     */
    public C2SSyncEventClient(String baseUrl, String C2SToken) {
        super(baseUrl, C2SToken);
    }

    /**
     * You should use this constructor if you want to use a preconfigured client
     * (with custom timeouts, connection pool, e.t.c.)
     * @param baseUrl URL of the eventnative backend
     * @param C2SToken client token
     * @param client preconfigured {@link OkHttpClient}
     */
    public C2SSyncEventClient(String baseUrl, String C2SToken, OkHttpClient client) {
        super(baseUrl, C2SToken, client);
    }


    /**
     * Send client events to server
     * @param eventContext JSON object with event context content
     * @param properties JSON object with all the properties except for event context
     * @return {@link EventNativeResponse} with http response status and body
     * @throws IOException in case of network problems or timeout
     */
    public EventNativeResponse sendEvent(JsonObject eventContext, JsonObject properties) throws IOException {
        properties.add("eventn_ctx", eventContext);
        var body = RequestBody.create(properties.toString(), MediaType.get("application/json; charset=utf-8"));
        final String postEventUrl = baseUrl + C2S_EVENT_URL + token;
        return sendRequest(postEventUrl, body);
    }
}
