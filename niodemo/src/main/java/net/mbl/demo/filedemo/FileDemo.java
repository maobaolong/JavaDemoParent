package net.mbl.demo.filedemo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileDemo {

  public static final int SIZE = 1024;
  public static final String PATH = "/tmp/hadoopclass.txt";

  public static void writefile() {
    try {
      FileChannel fc = new FileOutputStream(PATH, true).getChannel();
      fc.write(ByteBuffer.wrap("Hello World java NIO ".getBytes()));
      fc.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void readfile() {
    try {
      FileChannel fc = new FileInputStream(PATH).getChannel();
      ByteBuffer buffer = ByteBuffer.allocate(SIZE);
      fc.read(buffer);
      //重值ByteBuffer中的数组
      buffer.flip();
      while (buffer.hasRemaining()) {
        System.out.println((char) buffer.get());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    writefile();
    readfile();
  }
}
