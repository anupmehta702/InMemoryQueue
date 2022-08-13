package inMemoryQueue.ringbuffer.producer;

import inMemoryQueue.ringbuffer.event.QueueEvent;

import java.util.UUID;

public class Producer implements Runnable {
    String producerName = UUID.randomUUID().toString().substring(0, 3);
    QueueEvent event;
    ProducerBarrier producerBarrier;

    public Producer(QueueEvent event, ProducerBarrier producerBarrier) {
        this.event = event;
        this.producerBarrier = producerBarrier;
    }

    public void run() {
        try {
            int currentIndex = producerBarrier.addEvent(event);
            System.out.println("Event " + event + " successfully added by Producer -->" + producerName + " at index -->" + currentIndex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
