package inMemoryQueue.ringbuffer.Consumer;

import inMemoryQueue.ringbuffer.event.QueueEvent;
import inMemoryQueue.ringbuffer.queue.RingBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConsumerBarrier {
    Map<String, Consumer> consumersMap;
    RingBuffer ringBuffer;
    Map<Consumer, Consumer> dependencyConsumersMap = new HashMap<Consumer, Consumer>();

    public ConsumerBarrier(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
        consumersMap = new HashMap<String, Consumer>();
    }

    //to have all consumers recorded at one place for future reference
    public void addConsumer(Consumer consumer) {
        consumersMap.put(consumer.consumerName, consumer);
    }

    //to add dependencies between consumer threads
    public void addConsumerDependency(Consumer sourceConsumerName, Consumer dependentOnConsumerName) {
        dependencyConsumersMap.put(sourceConsumerName, dependentOnConsumerName);
    }


    public QueueEvent getEvent(String consumerName, int index) throws InterruptedException {
        while (index == ringBuffer.getCurrIndex()) {
            System.out.println("Consumer " + consumerName + " and producer index are at same index -->" + index + " Want Producer to move ahead !");
            Thread.sleep(2000);
            //consumer is trying to read the same index on which producer is writing or would write
            //so we should wait until new producer writes to this index or else we might read the same previous data
            //before we write on it
        }
        checkForDependentConsumerPosition(consumerName);
        return ringBuffer.getEvent(index);
    }


    private void checkForDependentConsumerPosition(String consumerName) throws InterruptedException {
        Consumer sourceConsumer = consumersMap.get(consumerName);
        if (dependencyConsumersMap.containsKey(sourceConsumer)) {
            Consumer dependentOnConsumer = dependencyConsumersMap.get(sourceConsumer);
            while (dependentOnConsumer.index < consumersMap.get(consumerName).index) {
                System.out.println("Dependency --> Source consumer index -->" + sourceConsumer.index + " waiting for dependent consumer index -->" + dependentOnConsumer.index);
                Thread.sleep(3000);
            }
        }
    }


    public boolean checkIfProducerCanOverrideTheIndex(int producerIndexToWriteOn) {
        if(producerIndexToWriteOn < ringBuffer.getTotalSize()){
            return true; // Producer can fill the intial size of the queue as they are empty and no oveeriding required
        }
        else {
            int originalIndex = producerIndexToWriteOn - ringBuffer.getTotalSize();
            if(consumersMap.size() == 0){
                return false; //NO consumer present yet so cant override
            }
            Set<Map.Entry<String, Consumer>> entries = consumersMap.entrySet();
            for (Map.Entry<String, Consumer> currentEntry : entries) {
                if (originalIndex >= currentEntry.getValue().index) {
                    System.out.println("Trying to write on index -" + originalIndex + " " +
                            "which is currently read/unread by " + currentEntry.getKey());
                    return false;
                }
            }
        }
        return true;
    }

    public int getSlowestConsumerIndex() {
        int slowestIndex = Integer.MAX_VALUE;
        Set<Map.Entry<String, Consumer>> entries = consumersMap.entrySet();
        for (Map.Entry<String, Consumer> currentEntry : entries) {
            if (currentEntry.getValue().index <= slowestIndex) {
                slowestIndex = currentEntry.getValue().index;
            }
        }
        //always subtract 1 from slowestIndex as consumer pointers are always at next available position to read
        // return slowestIndex == Integer.MAX_VALUE || slowestIndex == 0 ? 0 : slowestIndex - 1;
        return slowestIndex == Integer.MAX_VALUE || slowestIndex == 0 ? 0 : slowestIndex;
    }
}
