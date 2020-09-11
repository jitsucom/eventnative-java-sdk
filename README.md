# Eventnative Java SDK
This project contains the implementation of Java clients to send events to [Eventnative backend](https://github.com/ksensehq/eventnative).

We have two types of clients: synchronous and asynchronous events processing.

Before we start with an overview of clients, here is a difference between S2S and C2S events.

S2S may be any JSON object, without any schema.

C2S is a JSON object with a mandatory _event_ctx_ node, which is a JSON object.

For example:
```json
{
    "event_type": "3rdparty", 
    "eventn_ctx": {
      "event_id": "dq4s1q6rrg",
      "user": {
        "anonymous_id": "afdklmflv"
      },
      "user_agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36",
      "utc_time": "2020-08-03T14:35:58.638Z",
      "local_tz_offset": 240,
      "referer": "",
      "url": "http://localhost:63342/tracker-demo/demo.html?_ijt=u5jsi606103iugi7mjphngrc7i",
      "page_title": "kSenseTrackingDemo"
    },
    "eventn_data": {
    "customer_group": "test_group"
    }
}
```

So, our C2S clients accept following parameters:
```java
sendEvent(JsonObject eventContext, JsonObject properties)
```

In the example above, _eventContext_ parameter has the value:
```json
{
  "event_id": "dq4s1q6rrg",
  "user": {
  "anonymous_id": "afdklmflv"
  },
  "user_agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36",
  "utc_time": "2020-08-03T14:35:58.638Z",
  "local_tz_offset": 240,
  "referer": "",
  "url": "http://localhost:63342/tracker-demo/demo.html?_ijt=u5jsi606103iugi7mjphngrc7i",
  "page_title": "kSenseTrackingDemo"
}
```

And the second, _properties_:
```json
{
  "event_type": "3rdparty", 
  "eventn_data": {
    "customer_group": "test_group"
  }
}
```

C2S clients merge the first argument to the second one setting as _eventn_ctx_ field. 

## Asynchronous clients
This group of clients stores events to queue and sends them to the backend server using a dedicated thread.
The queue size is configurable. When the queue is full, it stops accepting new events. If an event is not added to the queue, you'll get _false_ value.

We have an implementation of two async clients:
- [C2SAsyncClient](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/async/C2SAsyncClient.java)
- [S2SAsyncEventClient](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/async/S2SAsyncEventClient.java)

Example of S2SAsyncEventClient:
```java
try (S2SAsyncEventClient eventsClient = new S2SAsyncEventClient(EVENTNATIVE_BASE_URL, S2S_TOKEN, 10)) {
    JsonObject event = JsonParser.parseString("{\"event_type\": \"s2s_async_test\"}").getAsJsonObject();
    final boolean addedToQueue = eventsClient.sendEvent(event);
    if (!addedToQueue) {
    // process the situation when queue is full
    }
}
```

Asynchronous clients do not guarantee that all the messages of the queue will be delivered. 
If you stop the application, events at the queue that were not processed get lost. To avoid this problem, 
you should use synchronous and implement your own persistence of the events.

To check which events are still in queue, use 

## Synchronous clients
These clients send event and return server response.

We have an implementation of two sync clients:
- [C2SSyncEventClient](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/sync/C2SSyncEventClient.java)
- [S2SSyncEventClient](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/sync/S2SSyncEventClient.java)

Example of sync event sending:
```java
String s2sEvent = "{\"event_type\": \"test_event\" }";
JsonObject json = JsonParser.parseString(s2sEvent).getAsJsonObject();
final S2SSyncEventClient client = new S2SSyncEventClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
final EventNativeResponse eventNativeResponse = client.sendEvent(json);
if (!eventNativeResponse.isSuccessful()) {
    System.out.println("status: " + eventNativeResponse.getStatus());
    System.out.println("response body: " + eventNativeResponse.getBody());
}
```

To see examples of how to use clients, please, check [Demo class](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/io/ksense/eventnative/demo/Demo.java).

## SDK logs
We use log4j logs. To get more information about events processing, especially for async clients, enable 'debug' mode.