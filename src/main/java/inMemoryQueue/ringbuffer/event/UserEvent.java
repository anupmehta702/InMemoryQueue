package inMemoryQueue.ringbuffer.event;

public class UserEvent {
    String messageId;
    String httpCode;

    public UserEvent(String messageId, String httpCode) {
        this.messageId = messageId;
        this.httpCode = httpCode;
    }

    public String getHttpCode() {
        return httpCode;
    }
}
