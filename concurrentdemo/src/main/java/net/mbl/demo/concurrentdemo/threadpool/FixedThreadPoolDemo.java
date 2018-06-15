package net.mbl.demo.concurrentdemo.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 * 因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
 * 定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()。可参考PreloadDataCache。
 */
public class FixedThreadPoolDemo {
  public static void main(String[] args) {

    ThreadPoolExecutor fixedThreadPool = new ThreadPoolExecutor(3, 3,
        100000L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>());
    for (int i = 0; i < 10; i++) {
      final int index = i;
      fixedThreadPool.submit(new Runnable() {

        @Override
        public void run() {
          try {
            System.out.println(index);
            Thread.sleep(4000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });
    }
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    fixedThreadPool.setCorePoolSize(1);
    fixedThreadPool.setMaximumPoolSize(1);
    try {
      Thread.sleep(10000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    fixedThreadPool.setCorePoolSize(4);
    fixedThreadPool.setMaximumPoolSize(4);
    try {
      Thread.sleep(4000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (int i = 10; i < 20; i++) {
      final int index = i;
      fixedThreadPool.submit(new Runnable() {

        @Override
        public void run() {
          try {
            System.out.println(index);
            Thread.sleep(4000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      });
    }

  }
}
