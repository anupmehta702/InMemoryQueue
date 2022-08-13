# InMemoryQueue

I have implemented in memory queue that supports multiple concurrent producers and consumers.
I have used Atomic locking and ring buffer that nulifies contention of resource and hence makes code run much faster as no synchronization is required( lock house keeping).
Atomic locking works on principle of "Better to ask for forgiveness rather than permission".

Basic components of the the Queue -
1) RingBuffer - unbounded ring buffer of a specific given size which is configurable.
2) ProducerBarrier - Used to calculate the index on which the event should be stored using atomic locking thus avoiding synchronzation at head pointer in traditional queue 
3) Producer - A thread that generates events and with help of producerBarrier stores the event in ringbuffer
4) Consumer - A thread that consumes the events from ring buffer. It maintains it's own pointer/index to ringbuffer, thus avoiding traditional synchoronization at tail variable in queue
5) ConsumerBarrier - Used to maintain dependency between consumers and also track the pointers/index of all consumers to help producerBarrier to figure out if a position can be overridden or not.
6) DLQueue - Class which stores events whose processing failed .
7) InMemoryQueueApplication - Main class which has a sample test created that includes multiple producers and multiple consumers trying to write and read concurrently.

TO DO - 
1) Write test cases.
2) Code optimization for Retry mechanism
3) Include json code to serialize - deserialize UserEvent
4) TTL implementation.
