package net.mbl.demo.concurrentdemo.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个定长线程池，支持定时及周期性任务执行。
 * 本例包含两个示例，1表示延迟3秒执行。2表示延迟5秒后每3秒执行一次。ScheduledExecutorService比Timer更安全，功能更强大
 */
public class ScheduledThreadPoolDemo {
  public static void main(String[] args) throws InterruptedException {
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    scheduledThreadPool.schedule(new Runnable() {
      @Override
      public void run() {
        System.out.println("delay 3 seconds");
      }
    }, 3, TimeUnit.SECONDS);

    //确保上边的示例执行完成
    Thread.sleep(5000);
    scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        System.out.println("delay 1 seconds, and excute every 3 seconds");
      }
    }, 5, 3, TimeUnit.SECONDS);
  }
}
