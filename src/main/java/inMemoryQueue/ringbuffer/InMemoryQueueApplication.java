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
        Thread ct2 = new Thread(c2);
        cb.addConsumerDependency(c2, c1);
        ct2.start();
        Thread ct1 = new Thread(c1);
        Thread.sleep(3000);
        ct1.start();

        Thread.sleep(200);
        ringBuffer.printRingBuffer();


        createProducerThread("10uvw", pb).start();
        createProducerThread("11xyz", pb).start();
        new Thread(new Producer(new QueueEvent("12error",
                new UserEvent("12error", "500")), pb)).start();

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


/* Output
Failed while adding event for currentIndex -->0 , Retrying !
Failed while adding event for currentIndex -->0 , Retrying !
Event QueueEvent{messageId='2ghi'} successfully added by Producer -->132 at index -->0
Event QueueEvent{messageId='3jkl'} successfully added by Producer -->2ea at index -->1
Event QueueEvent{messageId='4mno'} successfully added by Producer -->76c at index -->2
Event QueueEvent{messageId='6stu'} successfully added by Producer -->547 at index -->3
Producer is fast for index -->4 and cannot override ring buffer for index -4 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -4 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -4 that all consumers have not read yet

--- Printing entire ring buffer ---
QueueEvent{messageId='2ghi'} added at position -->0
QueueEvent{messageId='3jkl'} added at position -->1
QueueEvent{messageId='4mno'} added at position -->2
QueueEvent{messageId='6stu'} added at position -->3

Waiting for dependent consumer to consume the events !
Source consumer index -->0 dependent consumer index -->0
Event QueueEvent{messageId='1def'} successfully added by Producer -->54d at index -->4
Event QueueEvent{messageId='0abc'} successfully added by Producer -->51f at index -->5
Waiting for dependent consumer to consume the events !
Source consumer index -->0 dependent consumer index -->0
Producer is fast for index -->4 and cannot override ring buffer for index -0 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -0 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -0 that all consumers have not read yet
Consumer - Consumer1 processed - QueueEvent{messageId='1def'} @ index - 0
Consumer - Consumer1 processed - QueueEvent{messageId='0abc'} @ index - 1
Consumer - Consumer1 processed - QueueEvent{messageId='4mno'} @ index - 2
Consumer - Consumer2 processed - QueueEvent{messageId='1def'} @ index - 0
Consumer - Consumer1 processed - QueueEvent{messageId='6stu'} @ index - 3
Producer is fast for index -->4 and cannot override ring buffer for index -1 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -1 that all consumers have not read yet
Producer is fast for index -->4 and cannot override ring buffer for index -1 that all consumers have not read yet
Consumer - Consumer2 processed - QueueEvent{messageId='0abc'} @ index - 1
Consumer - Consumer1 processed - QueueEvent{messageId='1def'} @ index - 4
Consumer - Consumer2 processed - QueueEvent{messageId='4mno'} @ index - 2
Consumer - Consumer1 processed - QueueEvent{messageId='0abc'} @ index - 5
Consumer is fast and there are no new events to be produced
Consumer - Consumer2 processed - QueueEvent{messageId='6stu'} @ index - 3
Consumer - Consumer2 processed - QueueEvent{messageId='1def'} @ index - 4
Failed while adding event for currentIndex -->4 , Retrying !
Failed while adding event for currentIndex -->4 , Retrying !
Failed while adding event for currentIndex -->4 , Retrying !
Consumer - Consumer1 encountered empty event for index -->6
Consumer is fast and there are no new events to be produced
Consumer - Consumer2 processed - QueueEvent{messageId='0abc'} @ index - 5
Consumer is fast and there are no new events to be produced
Event QueueEvent{messageId='5pqr'} successfully added by Producer -->2b1 at index -->6
Event QueueEvent{messageId='7uvw'} successfully added by Producer -->dfe at index -->8
Event QueueEvent{messageId='8xyz'} successfully added by Producer -->36b at index -->7
Consumer - Consumer1 encountered empty event for index -->6
Consumer - Consumer2 encountered empty event for index -->6
Waiting for dependent consumer to consume the events !
Source consumer index -->6 dependent consumer index -->6
Consumer - Consumer1 processed - QueueEvent{messageId='5pqr'} @ index - 6
Consumer - Consumer1 processed - QueueEvent{messageId='8xyz'} @ index - 7
Consumer - Consumer1 processed - QueueEvent{messageId='7uvw'} @ index - 8
Consumer is fast and there are no new events to be produced
Consumer - Consumer2 processed - QueueEvent{messageId='5pqr'} @ index - 6
Consumer - Consumer2 processed - QueueEvent{messageId='8xyz'} @ index - 7
Consumer - Consumer2 processed - QueueEvent{messageId='7uvw'} @ index - 8
Consumer is fast and there are no new events to be produced
Consumer - Consumer1 encountered empty event for index -->9
Consumer is fast and there are no new events to be produced
Consumer - Consumer2 encountered empty event for index -->9
 */