package net.mbl.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mbl on 13/10/2017.
 */
public class NioChannelDemo {
  public static void init(String filePath, String str) {

    FileWriter writer;
    try {
      writer = new FileWriter(filePath);
      writer.write(str);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFile() {
    String fromFilePath = "/tmp/from.txt";
    init(fromFilePath, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    try {
      RandomAccessFile fFrom = new RandomAccessFile(fromFilePath, "rw");
      FileChannel channelFrom = fFrom.getChannel();
      // 以下是另一种获取channel的方法
//      FileInputStream fin = new FileInputStream(fromFilePath);
//      FileChannel channelFrom = fin.getChannel();

      // 两种获取channel的方式
      RandomAccessFile fTo = new RandomAccessFile("/tmp/to.txt", "rw");
      FileChannel channelTo = fTo.getChannel();
      // force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上
      channelTo.force(true);
      // 在堆上申请48字节的缓冲区，实际就是个48字节的byte
      ByteBuffer buf = ByteBuffer.allocate(48);
      buf.clear();
      System.out.println("channelFrom.position = " + channelFrom.position() + ", size = " + channelFrom.size());
      // truncate方法截断第0个字节到hannelFrom.size()-2个字节，去掉后边的2个字节
      channelFrom.truncate(channelFrom.size()-2);
      System.out.println("after truncate 2. channelFrom.position = " + channelFrom.position() + ", size = " + channelFrom.size());
      channelFrom.position(10);
      System.out.println("after position 10. channelFrom.position = " + channelFrom.position() + ", size = " + channelFrom.size());
      // 从from通道读48字节到buf中，剩下的内容就不读了，因为缓冲区就48个字节
      int ret = channelFrom.read(buf);
      System.out.println("after read. channelFrom.position = " + channelFrom.position() + ", size = " + channelFrom.size());
      System.out.println("after read, ret = " + ret);
      // 将读到的内容的第三个字节修改为0，写入到目标通道。而不影响from.txt的内容
      buf.putChar(3, '0');
      // buf已经读到了内容，把limit设置为当前位置，把position设置为0，为了将该buf写入到另一个通道
      buf.flip();
      //此处的写是异步非阻塞写，返回不确定写完，但通过buf.hasRemaining()可以判断是否写完，没有写完就继续写
      while(buf.hasRemaining()) {
        System.out.println("after write, ret = " + channelTo.write(buf));
      }

      // 最后由于从fromChannel的第10个位置读取了48个字节，并且修改了第3个位置的字符为0.最后的to.txt内容为
      // ABC0FGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv

      System.out.println("after write. channelFrom.position = " + channelFrom.position() + ", size = " + channelFrom.size());
      channelFrom.close();
      channelTo.close();
      fFrom.close();
      fTo.close();
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    copyFile();
    System.out.println("done!");
  }
}
