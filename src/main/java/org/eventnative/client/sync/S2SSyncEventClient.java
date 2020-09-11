package org.eventnative.client.sync;

import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.eventnative.model.EventNativeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This client is used to send s2s events synchronously.
 */
public class S2SSyncEventClient extends SyncEventClient {
    private static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";
    private final Logger logger = LoggerFactory.getLogger(S2SSyncEventClient.class);

    /**
     * Use this constructor if you want to create client with default connection parameters
     * (see defailt values at {@link OkHttpClient.Builder})
     * @param baseUrl URL of the eventnative backend
     * @param S2SToken s2s token (configured as s2s_auth at eventnative configuration file)
     */
    public S2SSyncEventClient(String baseUrl, String S2SToken) {
        super(baseUrl, S2SToken);
    }

    /**
     * You should use this constructor if you want to use a preconfigured client
     * (with custom timeouts, connection pool, e.t.c.)
     * @param baseUrl URL of the eventnative backend
     * @param S2SToken s2s token (configured as s2s_auth at eventnative configuration file)
     * @param client preconfigured {@link OkHttpClient}
     */
    public S2SSyncEventClient(String baseUrl, String S2SToken, OkHttpClient client) {
        super(baseUrl, S2SToken, client);
    }

    /**
     *
     * @param event any JSON object
     * @return {@link EventNativeResponse} with http response status and body
     * @throws IOException in case of network problems or timeout
     */
    public EventNativeResponse sendEvent(JsonObject event) throws IOException {
        if (event == null) {
            throw new IllegalArgumentException("event must be a valid JSON object");
        }
        logger.debug("Sending event [{}]", event);
        RequestBody body = RequestBody.create(event.toString(), MediaType.get("application/json; charset=utf-8"));
        return sendRequest(baseUrl + S2S_EVENT_URL + token, body);
    }
}
