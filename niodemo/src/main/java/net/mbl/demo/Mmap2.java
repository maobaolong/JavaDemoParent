package net.mbl.demo;

import sun.nio.ch.FileChannelImpl;

import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mbl on 10/10/2017.
 */
public class Mmap2 {
  public static void main(String[] args) throws Exception {
    String FILE_NAME = "/tmp/hello.txt";
    RandomAccessFile f = new RandomAccessFile(FILE_NAME, "rw");
    FileChannel fc = f.getChannel();
    MappedByteBuffer b = fc.map(FileChannel.MapMode.READ_ONLY, 0, 4096);
    System.out.println(b.isLoaded());
    System.out.println(b.capacity());
    Method m = FileChannelImpl.class.getDeclaredMethod("unmap",
        MappedByteBuffer.class);
    m.setAccessible(true);
    m.invoke(FileChannelImpl.class, b);
    System.out.println(b.isLoaded());
    System.out.println(b.capacity());
    while (true) {
      Thread.sleep(1000);
      System.out.println(b.get(0));
    }
  }
}
