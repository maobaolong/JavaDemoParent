package net.mbl.demo.concurrentdemo.util.blockingqueue;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <b>一个线程往里边放，另外一个线程从里边取的一个 BlockingQueue。<b/>
 * <p>
 * 一个线程将会持续生产新对象并将其插入到队列之中，直到队列达到它所能容纳的临界点。也就是说，它是有限的。如果该阻塞队列到达了其临界点，
 * 负责生产的线程将会在往里边插入新对象时发生阻塞。它会一直处于阻塞之中，直到负责消费的线程从队列中拿走一个对象。
 * 负责消费的线程将会一直从该阻塞队列中拿出对象。如果消费线程尝试去从一个空的队列中提取对象的话，这个消费线程将会处于阻塞之中，
 * 直到一个生产线程把一个对象丢进队列。
 */
public class BlockingQueueDemo {
  public static void main(String[] args) throws Exception {
    BlockingQueue queue = new ArrayBlockingQueue(1024);

    Producer producer = new Producer(queue);
    Consumer consumer = new Consumer(queue);

    new Thread(producer).start();
    new Thread(consumer).start();

    Thread.sleep(4000);
  }
}
