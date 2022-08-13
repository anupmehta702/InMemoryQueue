package inMemoryQueue.ringbuffer.queue;

import inMemoryQueue.ringbuffer.event.QueueEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class RingBuffer {

    int totalSize;
    QueueEvent[] ringBuffer;
    AtomicInteger currIndex = new AtomicInteger();

    public RingBuffer(int totalSize) {
        this.totalSize = totalSize;
        ringBuffer = new QueueEvent[totalSize];
    }

    public int getCurrIndex() {
        return currIndex.get();
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean addEvent(QueueEvent event, int inputIndex) {
        int toUpdateIndex = inputIndex + 1;
        if (currIndex.compareAndSet(inputIndex, toUpdateIndex)) {
            ringBuffer[inputIndex % totalSize] = event;

            return true;
        }
        return false;
    }

    public QueueEvent getEvent(int index) {
        int ringBufferIndex = index % totalSize;
        return ringBuffer[ringBufferIndex];
    }

    public void printRingBuffer() {
        System.out.println("--- Printing entire ring buffer ---");
        for (int i = 0; i < totalSize; i++) {
            System.out.println(ringBuffer[i] + " added at position -->" + i);
        }
        System.out.println();
    }


}
