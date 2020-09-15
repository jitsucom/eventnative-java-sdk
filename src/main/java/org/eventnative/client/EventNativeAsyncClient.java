package org.eventnative.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.eventnative.model.EventNativeResponse;
import org.eventnative.model.WebEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class EventNativeAsyncClient extends AbstractClient implements AutoCloseable {
    private final BlockingQueue<JsonObject> eventsQueue;
    private final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder().setNameFormat("message-consumer").setDaemon(true).build()
    );
    private final Logger logger = LoggerFactory.getLogger(EventNativeAsyncClient.class);

    public EventNativeAsyncClient(String backendBaseUrl, String s2sToken, int queueCapacity) {
        super(backendBaseUrl, s2sToken);
        this.eventsQueue = new LinkedBlockingQueue<>(queueCapacity);
        runMessagesConsumer();
    }

    public EventNativeAsyncClient(String backendBaseUrl, String s2sToken, OkHttpClient client, int queueCapacity) {
        super(backendBaseUrl, s2sToken, client);
        this.eventsQueue = new LinkedBlockingQueue<>(queueCapacity);
        runMessagesConsumer();
    }

    /**
     * Puts event to the queue. Separate thread sends events to server.
     *
     * @param event any JSON object
     * @return true if the event was added to the queue, false if the queue if full
     * @throws IllegalArgumentException if event is null
     */
    public boolean sendEvent(JsonObject event) {
        if (event == null) {
            throw new IllegalArgumentException("event should be a valid JSON object");
        }
        logger.debug("Adding event [{}] to the queue", event);
        return addEvent(event);
    }

    public boolean sendWebEvent(WebEvent event) {
        return sendEvent(event.toJson());
    }

    private boolean addEvent(JsonObject event) {
        return eventsQueue.offer(event);
    }

    public Collection<JsonObject> getUnprocessedEvents() {
        return new ArrayList<>(eventsQueue);
    }

    private void runMessagesConsumer() {
        executorService.execute(() -> {
            while (true) {
                try {
                    JsonObject event = eventsQueue.take();
                    RequestBody body = RequestBody.create(event.toString(), MediaType.get("application/json; charset=utf-8"));
                    logger.debug("Sending {} event", event);
                    final EventNativeResponse eventNativeResponse = sendRequest(body);
                    logger.debug("Eventnative server response: {}", eventNativeResponse);
                } catch (Throwable t) {
                    logger.error("Exception happened while running message consumer", t);
                }
            }
        });
    }

    @Override
    public void close() {
        logger.info("Shutting down executor");
        executorService.shutdownNow();
    }
}
