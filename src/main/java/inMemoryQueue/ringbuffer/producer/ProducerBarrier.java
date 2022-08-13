package inMemoryQueue.ringbuffer.producer;

import inMemoryQueue.ringbuffer.Consumer.ConsumerBarrier;
import inMemoryQueue.ringbuffer.event.QueueEvent;
import inMemoryQueue.ringbuffer.queue.RingBuffer;

public class ProducerBarrier {
    RingBuffer ringBuffer;
    ConsumerBarrier cb;


    public ProducerBarrier(RingBuffer ringBuffer, ConsumerBarrier consumerBarrier) {
        this.ringBuffer = ringBuffer;
        this.cb = consumerBarrier;
    }


    private int getValidIndexToWriteOn() throws InterruptedException {
        int currentIndex = ringBuffer.getCurrIndex();
        while(!cb.checkIfProducerCanOverrideTheIndex(currentIndex)){
            System.out.println("Making producer wait on index ->"+currentIndex+" as consumers are slow");
            Thread.sleep(3000);
        }
        return currentIndex;
    }

    public int addEvent(QueueEvent event) throws InterruptedException {
        int currentIndex = getValidIndexToWriteOn();
        while (!ringBuffer.addEvent(event, currentIndex)) {
            System.out.println("Failed while adding event for currentIndex -->" + currentIndex + " , Retrying !");
            Thread.sleep(3000);
            currentIndex = getValidIndexToWriteOn();

        }

        return currentIndex;
    }

}
