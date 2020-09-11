package org.eventnative.client.sync;

import org.eventnative.client.EventClientUtils;
import org.eventnative.model.EventNativeResponse;
import okhttp3.*;

import java.io.IOException;

/**
 * This class keeps common data for different types of event clients - token, configured http client, base URL
 */
public abstract class SyncEventClient {
    protected final OkHttpClient client;
    protected final String baseUrl;
    protected final String token;

    public SyncEventClient(String baseUrl, String token) {
        this.baseUrl = EventClientUtils.normalizeBaseUrl(baseUrl);
        this.client = new OkHttpClient();
        this.token = token;
    }

    public SyncEventClient(String baseUrl, String token, OkHttpClient client) {
        this.baseUrl = EventClientUtils.normalizeBaseUrl(baseUrl);
        this.client = client;
        this.token = token;
    }

    /**
     * Sends post requests
     * @param postEventUrl path to send event to
     * @param body event body
     * @return in case of network problems or timeout
     * @throws IOException in case of network problems or timeout
     */
    protected EventNativeResponse sendRequest(String postEventUrl, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(postEventUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            final ResponseBody responseBody = response.body();
            return new EventNativeResponse(response.code(), responseBody == null ? "" : responseBody.string());
        }
    }
}
