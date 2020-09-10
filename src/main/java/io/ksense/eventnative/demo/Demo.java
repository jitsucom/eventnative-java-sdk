package io.ksense.eventnative.demo;

import com.google.gson.*;
import io.ksense.eventnative.model.EventNativeResponse;
import io.ksense.eventnative.client.C2SEventClient;
import io.ksense.eventnative.client.S2SEventClient;

import java.io.IOException;

/**
 * Fill the constants with your Eventnative URL and token values to test clients
 */
public class Demo {

    public static final String EVENTNATIVE_BASE_URL = "<PUT_YOUR_EVENTNATIVE_URL>";
    public static final String C2S_TOKEN = "<YOUR_CLIENT_TOKEN>";
    public static final String S2S_TOKEN = "<S2S_AUTH>";

    public static void main(String[] args) throws IOException {
        demoS2S();
        demoC2S();
    }

    private static void demoS2S() throws IOException {
        var s2sEvent = """
                        {
                            "event_type": "test_event"
                        }
                """;
        JsonObject json = JsonParser.parseString(s2sEvent).getAsJsonObject();
        final S2SEventClient client = new S2SEventClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
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
        final C2SEventClient client = new C2SEventClient(EVENTNATIVE_BASE_URL, C2S_TOKEN);

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
}
