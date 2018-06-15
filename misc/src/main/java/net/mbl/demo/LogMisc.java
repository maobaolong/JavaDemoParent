package net.mbl.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mbl on 09/05/2018.
 */
public class LogMisc {
  private static final Logger LOGGER = LoggerFactory.getLogger("LogMisc");
  public static final Log LOG = LogFactory.getLog(LogMisc.class);
  public static void main(String[] args) {
    LOG.info("s", new Throwable());
  }
}
