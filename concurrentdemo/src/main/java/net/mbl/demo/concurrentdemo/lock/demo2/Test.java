package net.mbl.demo.concurrentdemo.lock.demo2;

/**
 * ReentrantLock 的lock机制有2种，忽略中断锁和响应中断锁，这给我们带来了很大的灵活性。比如：如果A、B 2个线程去竞争锁，A线程得到了锁，B线程等待，但是A线程这个时候实在有太多事情要处理，就是 一直不返回，B线程可能就会等不及了，想中断自己，不再等待这个锁了，转而处理其他事情。这个时候ReentrantLock就提供了2种机制，第一，B线程中断自己（或者别的线程中断它），但是ReentrantLock 不去响应，继续让B线程等待，你再怎么中断，我全当耳边风（synchronized原语就是如此）；第二，B线程中断自己（或者别的线程中断它），ReentrantLock 处理了这个中断，并且不再等待这个锁的到来，完全放弃
 */
public class Test {
  //是用ReentrantLock，还是用synchronized
  public static boolean useSynchronized = false;

  public static void main(String[] args) {
    IBuffer buff = null;
    if (useSynchronized) {
      buff = new Buffer();
    } else {
      buff = new BufferInterruptibly();
    }
    final Writer writer = new Writer(buff);
    final Reader reader = new Reader(buff);
    writer.start();
    reader.start();
    new Thread(new Runnable() {
      public void run() {
        long start = System.currentTimeMillis();
        for (; ; ) {
          // 等5秒钟去中断读
          if (System.currentTimeMillis() - start > 5000) {
            System.out.println("不等了，尝试中断");
            reader.interrupt();
            break;
          }

        }

      }
    }).start();
  }
}

class Writer extends Thread {
  private IBuffer buff;

  public Writer(IBuffer buff) {
    this.buff = buff;
  }

  @Override
  public void run() {
    buff.write();
  }
}

class Reader extends Thread {
  private IBuffer buff;

  public Reader(IBuffer buff) {
    this.buff = buff;
  }

  @Override
  public void run() {
    try {
      buff.read();
    } catch (InterruptedException e) {
      System.out.println("我不读了");
    }
    System.out.println("读结束");
  }
}