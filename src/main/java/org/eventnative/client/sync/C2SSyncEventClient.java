package org.eventnative.client.sync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.eventnative.model.EventNativeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This client is used to send client events (with 'eventn_ctx' node) synchronously.
 */
public class C2SSyncEventClient extends SyncEventClient {
    private static final String C2S_EVENT_URL = "/api/v1/event?token=";
    private final Logger logger = LoggerFactory.getLogger(C2SSyncEventClient.class);

    /**
     * Use this constructor if you want to create client with default connection parameters
     * (see defailt values at {@link OkHttpClient.Builder})
     *
     * @param baseUrl  URL of the eventnative backend
     * @param C2SToken client token
     */
    public C2SSyncEventClient(String baseUrl, String C2SToken) {
        super(baseUrl, C2SToken);
    }

    /**
     * You should use this constructor if you want to use a preconfigured client
     * (with custom timeouts, connection pool, e.t.c.)
     *
     * @param baseUrl  URL of the eventnative backend
     * @param C2SToken client token
     * @param client   preconfigured {@link OkHttpClient}
     */
    public C2SSyncEventClient(String baseUrl, String C2SToken, OkHttpClient client) {
        super(baseUrl, C2SToken, client);
    }


    /**
     * Send client events to server
     *
     * @param eventContext JSON object with event context content
     * @param properties   JSON object with all the properties except for event context
     * @return {@link EventNativeResponse} with http response status and body
     * @throws IOException in case of network problems or timeout
     * @throws IllegalArgumentException if eventContext is null
     */
    public EventNativeResponse sendEvent(JsonObject eventContext, JsonObject properties) throws IOException {
        logger.debug("event context: [{}], other properties: [{}]", eventContext, properties);
        if (eventContext == null) {
            throw new IllegalArgumentException("eventContext must be a valid JSON object");
        }
        properties = properties != null ? properties : JsonParser.parseString("{}").getAsJsonObject();
        properties.add("eventn_ctx", eventContext);
        RequestBody body = RequestBody.create(properties.toString(), MediaType.get("application/json; charset=utf-8"));
        final String postEventUrl = baseUrl + C2S_EVENT_URL + token;
        return sendRequest(postEventUrl, body);
    }
}
