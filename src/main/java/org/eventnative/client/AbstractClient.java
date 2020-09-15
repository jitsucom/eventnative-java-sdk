package org.eventnative.client;

import okhttp3.*;
import org.eventnative.model.EventNativeResponse;

import java.io.IOException;

public abstract class AbstractClient {
    private final String postUrl;
    private final OkHttpClient client;
    private static final String S2S_EVENT_URL = "/api/v1/s2s/event?token=";

    protected AbstractClient(String backendBaseUrl, String s2sToken) {
        this.postUrl = normalizeBaseUrl(backendBaseUrl) + S2S_EVENT_URL + s2sToken;
        this.client = new OkHttpClient();
    }

    protected AbstractClient(String backendBaseUrl, String s2sToken, OkHttpClient client) {
        this.postUrl = normalizeBaseUrl(backendBaseUrl) + S2S_EVENT_URL + s2sToken;
        this.client = client;
    }

    /**
     * @param url Eventnative server URL with schema
     * @return URL without '/' at the end it source URL containes one
     */
    public static String normalizeBaseUrl(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * Sends post requests
     *
     * @param body event body
     * @return status and http response body
     * @throws IOException in case of network problems or timeout
     */
    protected EventNativeResponse sendRequest(RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            final ResponseBody responseBody = response.body();
            return new EventNativeResponse(response.code(), responseBody == null ? "" : responseBody.string());
        }
    }
}
