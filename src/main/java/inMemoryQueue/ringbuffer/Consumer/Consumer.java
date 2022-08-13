package inMemoryQueue.ringbuffer.Consumer;

import inMemoryQueue.ringbuffer.exceptions.ErrorProcessingEventException;
import inMemoryQueue.ringbuffer.exceptions.InvalidHttpCodeException;
import inMemoryQueue.ringbuffer.event.QueueEvent;
import inMemoryQueue.ringbuffer.queue.DLQueue;

public class Consumer implements Runnable {
    String consumerName = "";
    ConsumerBarrier consumerBarrier;
    int index = 0;
    int retry = 2;

    public Consumer(String name, ConsumerBarrier barrier) {
        this.consumerName = name;
        this.consumerBarrier = barrier;
    }


    public void run() {

        while (true) {
            try {
                QueueEvent event = consumerBarrier.getEvent(consumerName, index);
                if (event != null) {
                    eventProcessing(event, retry);
                    Thread.sleep(1000);
                    System.out.println("Consumer - " + consumerName + " processed - " + event + " @ index - " + index);
                    index++;
                } else {
                    System.out.println("Consumer - " + consumerName + " encountered empty event for index -->" + index);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void eventProcessing(QueueEvent event, int retry) throws ErrorProcessingEventException {
        while (true) {
            try {
                Thread.sleep(1000);
                String httpCode = event.getEvent().getHttpCode();
                if (httpCode.equalsIgnoreCase("500")) {
                    System.out.println("Message processing failed for messageId ->" + event.getMessageId()
                            + "No of retries left -" + retry);
                    throw new InvalidHttpCodeException("In valid exception");
                } else {
                    return;
                }
            } catch (InvalidHttpCodeException e) {
                if (retry == 0) {
                    DLQueue.getInstance().addEvent(event);
                    throw new ErrorProcessingEventException("Retries exhausted , moving to DLQ");
                }
                retry--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
