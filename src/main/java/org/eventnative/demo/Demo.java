package org.eventnative.demo;

import com.google.gson.*;
import org.eventnative.client.C2SAsyncClient;
import org.eventnative.client.S2SAsyncEventClient;
import org.eventnative.model.EventNativeResponse;
import org.eventnative.client.C2SSyncEventClient;
import org.eventnative.client.S2SSyncEventClient;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fill the constants with your Eventnative URL and token values to test clients
 */
public class Demo {

    public static final String EVENTNATIVE_BASE_URL = "<PUT_YOUR_EVENTNATIVE_URL>";
    public static final String C2S_TOKEN = "<YOUR_CLIENT_TOKEN>";
    public static final String S2S_TOKEN = "<S2S_AUTH>";

    public static void main(String[] args) throws IOException, InterruptedException {
        // uncomment the demo you want to run

        demoS2S();
//        demoC2S();
//        demoAsyncS2S();
//        demoAsyncC2S();
    }

    private static void demoS2S() throws IOException {
        var s2sEvent = """
                        {
                            "event_type": "test_event"
                        }
                """;
        JsonObject json = JsonParser.parseString(s2sEvent).getAsJsonObject();
        final S2SSyncEventClient client = new S2SSyncEventClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
        final EventNativeResponse eventNativeResponse = client.sendEvent(json);
        System.out.println(eventNativeResponse);
        System.out.println(eventNativeResponse.isSuccessful());
    }

    private static void demoC2S() throws IOException {
        var event = """
                        {
                            "event_type": "test_event"
                        }
                """;
        var json = JsonParser.parseString(event).getAsJsonObject();
        final C2SSyncEventClient client = new C2SSyncEventClient(EVENTNATIVE_BASE_URL, C2S_TOKEN);

        final JsonObject userJson = JsonParser.parseString("""
                {"anonymous_id": "randomValue"}""").getAsJsonObject();

        final UserEvent userEvent = new UserEvent("231", userJson);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        final JsonObject jsonElement = gson.toJsonTree(userEvent).getAsJsonObject();
        final EventNativeResponse eventNativeResponse = client.sendEvent(jsonElement, json);
        System.out.println(eventNativeResponse);
        System.out.println(eventNativeResponse.isSuccessful());
    }

    /**
     * sends s2s events till queue is full and new events are not allowed anymore.
     * If queue is full, ends execution and closes client
     */
    private static void demoAsyncS2S() throws InterruptedException {
        try (S2SAsyncEventClient eventsClient = new S2SAsyncEventClient(EVENTNATIVE_BASE_URL, S2S_TOKEN, 10)) {
            while (true) {
                JsonObject event = JsonParser.parseString("{\"event_type\": \"s2s_async_test\"}").getAsJsonObject();
                final boolean sent = eventsClient.sendEvent(event);
                if (!sent) {
                    break;
                }
                Thread.sleep(100);
            }
        }
    }

    /**
     * sends c2s events till queue is full and new events are not allowed anymore.
     * If queue is full, ends execution and closes client
     */
    private static void demoAsyncC2S() throws InterruptedException {
        try (C2SAsyncClient eventsClient = new C2SAsyncClient(EVENTNATIVE_BASE_URL, C2S_TOKEN, 10)) {
            while (true) {
                JsonObject eventProperties = JsonParser.parseString("{\"event_type\": \"c2c_async_test\"}").getAsJsonObject();
                JsonObject eventContext = JsonParser.parseString("{\"event_id\": \"" + ThreadLocalRandom.current().nextLong() + "\"}").getAsJsonObject();
                final boolean sent = eventsClient.sendEvent(eventContext, eventProperties);
                if (!sent) {
                    break;
                }
                Thread.sleep(100);
            }
        }
    }
}
