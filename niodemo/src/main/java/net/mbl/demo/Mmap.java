package net.mbl.demo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mbl on 10/10/2017.
 */
public class Mmap {
  public static void main(String[] args) throws IOException {
    String FILE_NAME = "/tmp/hello.txt";

    RandomAccessFile f = new RandomAccessFile(FILE_NAME, "rw");
    FileChannel fc = f.getChannel();
    MappedByteBuffer b = fc.map( FileChannel.MapMode.READ_WRITE, 0, 4096 );
    System.out.println(b.isLoaded());
    System.out.println(b.capacity());

    b.put(0, (byte) 4);
    b.put(0, (byte) 5);
    b.put(0, (byte) 6);
    System.exit(-1);
    b.put(0, (byte) 7);
    b.put(0, (byte) 8);

//    CharBuffer charBuf = b.asCharBuffer();
//
//    char[] string = "Hello client\0".toCharArray();
//    charBuf.put( string );
//
//    System.out.println( "Waiting for client." );
//    while( charBuf.get( 0 ) != '\0' );
    System.out.println( "Finished waiting." );
  }
}
