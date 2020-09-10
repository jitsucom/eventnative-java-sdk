package io.ksense.eventnative.client;

import com.google.gson.JsonObject;
import io.ksense.eventnative.model.EventNativeResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * This client is used to send s2s events synchronously.
 */
public class S2SEventClient extends AbstractEventClient {
    public static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";

    /**
     * Use this constructor if you want to create client with default connection parameters
     * (see defailt values at {@link OkHttpClient.Builder})
     * @param baseUrl URL of the eventnative backend
     * @param S2SToken s2s token (configured as s2s_auth at eventnative configuration file)
     */
    public S2SEventClient(String baseUrl, String S2SToken) {
        super(baseUrl, S2SToken);
    }

    /**
     * You should use this constructor if you want to use a preconfigured client
     * (with custom timeouts, connection pool, e.t.c.)
     * @param baseUrl URL of the eventnative backend
     * @param S2SToken s2s token (configured as s2s_auth at eventnative configuration file)
     * @param client preconfigured {@link OkHttpClient}
     */
    public S2SEventClient(String baseUrl, String S2SToken, OkHttpClient client) {
        super(baseUrl, S2SToken, client);
    }

    /**
     *
     * @param jsonObject - any json object
     * @return {@link EventNativeResponse} with http response status and body
     * @throws IOException in case of network problems or timeout
     */
    public EventNativeResponse sendEvent(JsonObject jsonObject) throws IOException {
        var body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));
        return sendRequest(baseUrl + S2S_EVENT_URL + token, body);
    }
}
