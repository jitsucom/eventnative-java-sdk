package org.eventnative.model;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class WebEvent extends AnyData {
    private final String apiKey;
    private final String src;
    private final String eventType;
    private final EventContext eventnCtx;

    private WebEvent(String apiKey, String src, String eventType, EventContext eventnCtx, Map<String, Object> additionalField) {
        this.apiKey = apiKey;
        this.src = src;
        this.eventType = eventType;
        this.eventnCtx = eventnCtx;
        this.additionalFields = additionalField;
    }

    public JsonObject toJson() {
        return JsonConverter.toJsonObject(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String apiKey;
        private String src;
        private String eventType;
        private EventContext eventnCtx;
        private final Map<String, Object> additionalFields = new HashMap<>();

        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder setSrc(String src) {
            this.src = src;
            return this;
        }

        public Builder setEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder setEventnCtx(EventContext eventnCtx) {
            this.eventnCtx = eventnCtx;
            return this;
        }

        public Builder setProperty(String fieldName, Object value) {
            this.additionalFields.put(fieldName, value);
            return this;
        }

        public WebEvent build() {
            return new WebEvent(apiKey, src, eventType, eventnCtx, additionalFields);
        }
    }
}
