package inMemoryQueue.ringbuffer.event;

public class QueueEvent {
    String messageId;
    Long timestamp;
    UserEvent event;


    public QueueEvent(String messageId) {
        this.messageId = messageId;
        this.timestamp = System.currentTimeMillis();
        this.event = new UserEvent(messageId,"200");
    }


    public QueueEvent(String messageId,UserEvent userEvent) {
        this.messageId = messageId;
        this.timestamp = System.currentTimeMillis();
        this.event = userEvent;
    }

    public String getMessageId() {
        return messageId;
    }

    public UserEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "QueueEvent{" +
                "messageId='" + messageId + '\'' +
                '}';
    }
}
