package org.eventnative.client;

public class EventClientUtils {

    /**
     * @param url Eventnative server URL with schema
     * @return URL without '/' at the end it source URL containes one
     */
    public static String normalizeBaseUrl(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
