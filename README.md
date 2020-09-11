#Eventnative Java SDK
This project contains implementation of Java clients to send events to [Eventnative backend](https://github.com/ksensehq/eventnative).

We have two types of clients: for synchronous and asynchronous events processing.

##Asynchronous clients
This group of clients stores events to queue and sends them to backend server using dedicated thread.
Queue size is configurable. When the queue is full, it stops accepting new events. You should handle this case 
checking return value of event sending method.

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

##Synchronous clients
These clients send event and return server response.

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

##SDK logs
We use log4j logs. If you want to see log of every event sent by async client, enable 'debug' mode.
