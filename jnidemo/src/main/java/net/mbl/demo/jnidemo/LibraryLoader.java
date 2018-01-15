package net.mbl.demo.jnidemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mbl on 15/01/2018.
 */
public class LibraryLoader {

  /**
   * Loads a native shared library. It tries the standard System.loadLibrary
   * method first and if it fails, it looks for the library in the current
   * class path. It will handle libraries packed within jar files, too.
   *
   * @param name name of the library to load
   * @throws IOException if the library cannot be extracted from a jar file
   * into a temporary file
   */
  public static void loadLibrary(String name) throws IOException {
    try {
      System.loadLibrary(name);
    } catch (UnsatisfiedLinkError e) {
      String filename = System.mapLibraryName(name);
      InputStream in = LibraryLoader.class.getClassLoader().getResourceAsStream(filename);
      int pos = filename.lastIndexOf('.');
      File file = File.createTempFile(filename.substring(0, pos), filename.substring(pos));
      file.deleteOnExit();
      try {
        byte[] buf = new byte[4096];
        OutputStream out = new FileOutputStream(file);
        try {
          while (in.available() > 0) {
            int len = in.read(buf);
            if (len >= 0) {
              out.write(buf, 0, len);
            }
          }
        } finally {
          out.close();
        }
      } finally {
        in.close();
      }
      System.load(file.getAbsolutePath());
    }
  }
}