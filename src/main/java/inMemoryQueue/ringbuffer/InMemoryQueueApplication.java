package inMemoryQueue.ringbuffer;

import inMemoryQueue.ringbuffer.Consumer.Consumer;
import inMemoryQueue.ringbuffer.Consumer.ConsumerBarrier;
import inMemoryQueue.ringbuffer.event.QueueEvent;
import inMemoryQueue.ringbuffer.event.UserEvent;
import inMemoryQueue.ringbuffer.producer.Producer;
import inMemoryQueue.ringbuffer.producer.ProducerBarrier;
import inMemoryQueue.ringbuffer.queue.RingBuffer;

public class InMemoryQueueApplication {

    public static void main(String[] args) throws InterruptedException {
        RingBuffer ringBuffer = new RingBuffer(4);
        ConsumerBarrier cb = new ConsumerBarrier(ringBuffer);
        ProducerBarrier pb = new ProducerBarrier(ringBuffer, cb);

        createProducerThread("0abc", pb).start();
        createProducerThread("1def", pb).start();
        createProducerThread("2ghi", pb).start();
        createProducerThread("3jkl", pb).start();
        createProducerThread("4mno", pb).start();
        createProducerThread("5pqr", pb).start();
        createProducerThread("6stu", pb).start();
        createProducerThread("7uvw", pb).start();
        createProducerThread("8xyz", pb).start();


        Consumer c1 = new Consumer("Consumer1", cb);
        cb.addConsumer(c1);

        Consumer c2 = new Consumer("Consumer2", cb);
        cb.addConsumer(c2);

        cb.addConsumerDependency(c2, c1);

        Thread ct2 = new Thread(c2);
        ct2.start();

        Thread ct1 = new Thread(c1);
        Thread.sleep(3000);
        ct1.start();

        createProducerThread("10uvw", pb).start();
        createProducerThread("11xyz", pb).start();

        //deliberately created the below thread to test retry mechanism and DLQ
        //new Thread(new Producer(new QueueEvent("12error", new UserEvent("12error", "500")), pb)).start();

        Thread.sleep(4000);
        ringBuffer.printRingBuffer();


    }

    public static Thread createProducerThread(String messageId, ProducerBarrier pb) {
        String firstChar = messageId.charAt(0) + "";
        String httpCode = "200";
        if (Integer.parseInt(firstChar) % 2 == 0) {
            httpCode = "404";
        }
        return new Thread(new Producer(new QueueEvent(messageId, new UserEvent(messageId, httpCode)), pb));
    }
}


