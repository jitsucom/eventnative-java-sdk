package org.eventnative.model;

import java.util.HashMap;
import java.util.Map;

public class UserProperties extends AnyData {
    private final String anonymousId;

    public UserProperties(String anonymousId, Map<String, Object> additionalFields) {
        this.anonymousId = anonymousId;
        this.additionalFields = additionalFields;
    }

//    @Override
//    public JsonObject toJson() {
//        final JsonObject jsonObject = JsonConverter.toJsonObject(additionalFields);
//        jsonObject.addProperty("anonymous_id", anonymousId);
//        return jsonObject;
//    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String anonymousId;
        private final Map<String, Object> additionalFields = new HashMap<>();

        public Builder setAnonymousId(String anonymousId) {
            this.anonymousId = anonymousId;
            return this;
        }

        public Builder setField(String fieldName, Object value) {
            additionalFields.put(fieldName, value);
            return this;
        }

        public UserProperties build() {
            return new UserProperties(anonymousId, additionalFields);
        }
    }
}
