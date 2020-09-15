# Eventnative Java SDK
This project contains the implementation of Java clients to send events to [Eventnative backend](https://github.com/ksensehq/eventnative).

## Dependency configuration
To add a dependency on eventnative-java-sdk, use one of the following tools:

**Gradle**
```groovy
compile "org.eventnative:eventnative:0.1"
```

**Maven**
```xml
<dependency>
  <groupId>org.eventnative</groupId>
  <artifactId>eventnative</artifactId>
  <version>0.1</version>
</dependency>
```

Or use [other dependency management configuration](https://search.maven.org/artifact/org.eventnative/eventnative/0.1/jar). 

## Clients
We have two types of clients: synchronous and asynchronous events processing.

Clients allow sending web events or any event. 
Web event has a defined structure 
(see [WebEvent](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/model/WebEvent.java)).
Other events supposed to be any valid JSON object.

### Asynchronous clients
This group of clients stores events to queue and sends them to the backend server using a dedicated thread.
The queue size is configurable. When the queue is full, it stops accepting new events. If an event 
may not be added to the queue, you'll get _false_ value.

We have one implementation of [async client](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/EventNativeAsyncClient.java).

Example of async client usage:
```java
try (EventNativeAsyncClient client = new EventNativeAsyncClient(EVENTNATIVE_BASE_URL, S2S_TOKEN, 10)) {
    JsonObject event = JsonParser.parseString("{\"event_type\": \"s2s_async_test\"}").getAsJsonObject();
    final boolean addedToQueue = eventsClient.sendEvent(event);
    if (!addedToQueue) {
    // process the situation when queue is full
    }
}
```

The asynchronous client does not guarantee that all the messages of the queue will be delivered. 
If you stop the application, events at the queue that were not processed get lost. To avoid this problem, 
you should use a synchronous client and implement your own persistence of the events.

To check which events are still in the queue, use org.eventnative.client.EventNativeAsyncClient.getUnprocessedEvents 

### Synchronous clients
These clients send an event and return server response. We have an implementation of one [sync client](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/org/eventnative/client/EventNativeSyncClient.java).

Example of sync event sending:
```java
String event = "{\"event_type\": \"test_event\" }";
JsonObject json = JsonParser.parseString(event).getAsJsonObject();
final EventNativeSyncClient client = new EventNativeSyncClient(EVENTNATIVE_BASE_URL, S2S_TOKEN);
final EventNativeResponse eventNativeResponse = client.sendEvent(event);
if (!eventNativeResponse.isSuccessful()) {
    System.out.println("status: " + eventNativeResponse.getStatus());
    System.out.println("response body: " + eventNativeResponse.getBody());
}
```

To see more examples of how to use clients, please, check [Demo class](https://github.com/ksensehq/eventnative-java-sdk/blob/master/src/main/java/io/ksense/eventnative/demo/Demo.java).

## SDK logs
We use log4j logs. To get more information about events processing, especially for async clients, enable 'debug' mode.