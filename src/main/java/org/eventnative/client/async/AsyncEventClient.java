package org.eventnative.client.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import org.eventnative.model.EventNativeResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AsyncEventClient implements AutoCloseable {
    protected final OkHttpClient client;
    protected final String postEventUrl;

    private final Logger logger = LoggerFactory.getLogger(AsyncEventClient.class);

    private final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder().setNameFormat("message-consumer").setDaemon(true).build()
    );
    private final BlockingQueue<JsonObject> eventsQueue;

    public AsyncEventClient(String baseUrl, String eventApiUrl, String token, int queueCapacity) {
        this.client = new OkHttpClient();
        this.postEventUrl = buildPostEventUrl(normalizeBaseURL(baseUrl), eventApiUrl, token);
        this.eventsQueue  = new LinkedBlockingQueue<>(queueCapacity);
        runMessagesConsumer();
    }

    public AsyncEventClient(String baseUrl, String eventApiUrl, String token, OkHttpClient client, int queueCapacity) {
        this.client = client;
        this.postEventUrl = buildPostEventUrl(normalizeBaseURL(baseUrl), eventApiUrl, token);
        this.eventsQueue  = new LinkedBlockingQueue<>(queueCapacity);
        runMessagesConsumer();
    }

    private String buildPostEventUrl(String baseUrl, String eventApiUrl, String token) {
        return baseUrl + eventApiUrl + token;
    }

    private void runMessagesConsumer() {
        executorService.execute(() -> {
            while (true) {
                try {
                    JsonObject event = eventsQueue.take();
                    RequestBody body = RequestBody.create(event.toString(), MediaType.get("application/json; charset=utf-8"));
                    logger.debug("Sending {} event to [{}]", event, postEventUrl);
                    final EventNativeResponse eventNativeResponse = sendRequest(postEventUrl, body);
                    logger.debug("Eventnative server response: {}", eventNativeResponse);
                } catch (Throwable t) {
                    logger.error("Exception happened while running message consumer", t);
                }
            }
        });
    }

    protected boolean addEvent(JsonObject event) {
        return eventsQueue.offer(event);
    }

    public Collection<JsonObject> unprocessedEvents() {
        return new ArrayList<>(eventsQueue);
    }

    private String normalizeBaseURL(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

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

    @Override
    public void close() {
        logger.info("Shutting down executor");
        executorService.shutdownNow();
    }
}
