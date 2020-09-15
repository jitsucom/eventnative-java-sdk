package org.eventnative.model;

import java.util.HashMap;
import java.util.Map;

public class EventContext extends AnyData {
    private final String eventId;
    private final UserProperties user;
    private final String userAgent;
    private final String utcTime;
    private final int localTzOffset;
    private final String referer;
    private final String url;
    private final String pageTitle;

    private EventContext(String eventId, UserProperties user, String userAgent, String utcTime, int localTzOffset,
                         String referer, String url, String pageTitle, Map<String, Object> additionalFields) {
        this.eventId = eventId;
        this.user = user;
        this.userAgent = userAgent;
        this.utcTime = utcTime;
        this.localTzOffset = localTzOffset;
        this.referer = referer;
        this.url = url;
        this.pageTitle = pageTitle;
        this.additionalFields = additionalFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String eventId;
        private UserProperties user;
        private String userAgent;
        private String utcTime;
        private int localTzOffset;
        private String referer;
        private String url;
        private String pageTitle;
        private final Map<String, Object> additionalFields = new HashMap<>();

        public Builder setEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder setUser(UserProperties user) {
            this.user = user;
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder setUtcTime(String utcTime) {
            this.utcTime = utcTime;
            return this;
        }

        public Builder setLocalTzOffset(int timezoneOffset) {
            this.localTzOffset = timezoneOffset;
            return this;
        }

        public Builder setReferer(String referer) {
            this.referer = referer;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
            return this;
        }

        public Builder setField(String fieldName, Object value) {
            additionalFields.put(fieldName, value);
            return this;
        }

        public EventContext build() {
            return new EventContext(eventId, user, userAgent, utcTime, localTzOffset, referer, url, pageTitle, additionalFields);
        }
    }
}
