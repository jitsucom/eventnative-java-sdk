package org.eventnative.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.BasicConfigurator;
import org.eventnative.client.EventNativeAsyncClient;
import org.eventnative.client.EventNativeSyncClient;
import org.eventnative.model.EventContext;
import org.eventnative.model.EventNativeResponse;
import org.eventnative.model.UserProperties;
import org.eventnative.model.WebEvent;

import java.io.IOException;

/**
 * Fill the constants with your Eventnative URL and token values to test clients
 */
public class Demo {

    public static final String EVENTNATIVE_BASE_URL = "https://track-us-dev.ksense.io";
    public static final String S2S_TOKEN = "a409ecb1-ef95-4841-9fa7-80c75b1ba931";

    public static void main(String[] args) throws IOException, InterruptedException {
        // demo logs are written to console
        BasicConfigurator.configure();
        // uncomment the demo you want to run
        syncAnyEventDemo();
//        syncWebEventDemo();
//        asyncEventDemo();
    }

    public static void syncAnyEventDemo() throws IOException {
        final EventNativeSyncClient client = new EventNativeSyncClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
        final JsonObject event = JsonParser.parseString("{\"event_type\": \"test_sync\"}").getAsJsonObject();
        final EventNativeResponse eventNativeResponse = client.sendEvent(event);
        System.out.printf("Is successful: [%s], status: [%s], body: [%s]%n",
                eventNativeResponse.isSuccessful(), eventNativeResponse.getStatus(), eventNativeResponse.getBody());
    }

    public static void syncWebEventDemo() throws IOException {
        final EventNativeSyncClient client = new EventNativeSyncClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
        final EventContext context = EventContext.builder()
                .setEventId("test123")
                .setField("custom_context_filed", "set")
                .setLocalTzOffset(4)
                .setUtcTime("2020-09-14 01:00:59.132")
                .setPageTitle("test.io")
                .setReferer("any-site")
                .setUrl("https://test.io")
                .setUserAgent("unknown")
                .setUser(UserProperties.builder().setAnonymousId("21321").build())
                .build();
        WebEvent event = WebEvent.builder()
                .setApiKey(S2S_TOKEN)
                .setEventType("test_sync_web")
                .setSrc("test")
                .setProperty("test_custom_field", 2)
                .setEventnCtx(context)
                .build();
        final EventNativeResponse eventNativeResponse = client.sendWebEvent(event);
        System.out.printf("Is successful: [%s], status: [%s], body: [%s]%n",
                eventNativeResponse.isSuccessful(), eventNativeResponse.getStatus(), eventNativeResponse.getBody());
    }

    public static void asyncEventDemo() {
        try (EventNativeAsyncClient client = new EventNativeAsyncClient(EVENTNATIVE_BASE_URL, S2S_TOKEN, 10)) {
            final JsonObject event = JsonParser.parseString("{\"event_type\": \"test_sync\"}").getAsJsonObject();
            boolean added = client.sendEvent(event);
            System.out.println("Added event to queue: " + added);
        }
    }
}
