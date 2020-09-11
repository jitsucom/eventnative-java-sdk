package org.eventnative.model;

public class EventNativeResponse {
    private final int status;
    private final String body;

    public EventNativeResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public boolean isSuccessful() {
        return this.status == 200;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", body='" + body + '\'' +
                '}';
    }
}
