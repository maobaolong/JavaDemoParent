package net.mbl.demo.master;



import net.mbl.demo.Constants;
import net.mbl.demo.web.MasterWebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.net.InetSocketAddress;

/**
 * Entry point for the master.
 */
@ThreadSafe
public final class MyWebServer {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /**
   * Starts the master.
   *
   * @param args command line arguments, should be empty
   */
  public static void main(String[] args) {

    MasterWebServer mWebServer = new MasterWebServer("MasterWebServer",new InetSocketAddress("localhost",19999));

    // start web ui
    mWebServer.start();

  }

  private MyWebServer() {} // prevent instantiation
}
