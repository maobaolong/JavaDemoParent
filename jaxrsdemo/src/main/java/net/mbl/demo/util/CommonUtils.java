package net.mbl.demo.util;


import com.google.common.base.Function;
import org.slf4j.Logger;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Common utilities shared by all components .
 */
@ThreadSafe
public final class CommonUtils {
  /**
   * Sleeps for the given number of milliseconds.
   *
   * @param timeMs sleep duration in milliseconds
   */
  public static void sleepMs(long timeMs) {
    sleepMs(null, timeMs);
  }

  /**
   * Sleeps for the given number of milliseconds, reporting interruptions using the given logger.
   *
   * Unlike Thread.sleep(), this method responds to interrupts by setting the thread interrupt
   * status. This means that callers must check the interrupt status if they need to handle
   * interrupts.
   *
   * @param logger logger for reporting interruptions; no reporting is done if the logger is null
   * @param timeMs sleep duration in milliseconds
   */
  public static void sleepMs(Logger logger, long timeMs) {
    try {
      Thread.sleep(timeMs);
    } catch (InterruptedException e) {
      if (logger != null) {
        logger.warn(e.getMessage(), e);
      }
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Waits indefinitely for a condition to be satisfied.
   *
   * @param description a description of what causes condition to evaluation to true
   * @param condition the condition to wait on
   */
  public static void waitFor(String description, Function<Void, Boolean> condition) {
    while (!condition.apply(null)) {
      CommonUtils.sleepMs(20);
    }
  }

  private CommonUtils() {} // prevent instantiation
}
