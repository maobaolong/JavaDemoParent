/**
 * 工厂方法                  corePoolSize maximumPoolSize    keepAliveTime  workQueue
 * newCachedThreadPool      0            Integer.MAX_VALUE  60s            SynchronousQueue
 * newFixedThreadPool       nThreads     nThreads           0              LinkedBlockingQueue
 * newSingleThreadExecutor  1            1                  0              LinkedBlockingQueue
 * newScheduledThreadPool   corePoolSize Integer.MAX_VALUE  0              DelayedWorkQueue
 */
package net.mbl.demo.concurrentdemo.threadpool;

/**
 * execute()方法实际上是Executor中声明的方法，在ThreadPoolExecutor进行了具体的实现，这个方法是ThreadPoolExecutor的核心方法，
 * 通过这个方法可以向线程池提交一个任务，交由线程池去执行。
 * submit()方法是在ExecutorService中声明的方法，在AbstractExecutorService就已经有了具体的实现，
 * 在ThreadPoolExecutor中并没有对其进行重写，这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，
 * 它能够返回任务执行的结果，去看submit()方法的实现，会发现它实际上还是调用的execute()方法，
 * 只不过它利用了Future来获取任务执行结果（Future相关内容将在下一篇讲述）。
 *
 */

/**
 * 线程池的状态
 * volatile int runState;
 * static final int RUNNING    = 0;
 * static final int SHUTDOWN   = 1;
 * static final int STOP       = 2;
 * static final int TERMINATED = 3;
 * */