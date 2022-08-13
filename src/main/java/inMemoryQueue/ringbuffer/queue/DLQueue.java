package inMemoryQueue.ringbuffer.queue;

import inMemoryQueue.ringbuffer.event.QueueEvent;

import java.util.ArrayList;
import java.util.List;

public class DLQueue {
    List<QueueEvent> deadLetters = new ArrayList<QueueEvent>();
    private static final DLQueue instance = new DLQueue();
    private DLQueue(){
    }
    public static DLQueue getInstance(){
        return instance;
    }

    public void addEvent(QueueEvent event){
        deadLetters.add(event);
    }

    public void printEvents(){
        for(QueueEvent event : deadLetters){
            System.out.println(event);
        }
    }
}
