package net.mbl.demo.util;

import org.apache.commons.lang3.SystemUtils;

import javax.annotation.concurrent.ThreadSafe;

/**
 * OS related utility functions.
 */
@ThreadSafe
public final class OSUtils {
  /** The OS name. */
  public static final String OS_NAME = System.getProperty("os.name");
  /** The processor bit. */
  public static final String PROCESSOR_BIT = System.getProperty("os.arch");
  /** The java vendor name used in this platform. */
  public static final String JAVA_VENDOR_NAME = System.getProperty("java.vendor");
  /** Indicates the current java vendor is IBM java or not. */
  public static final boolean IBM_JAVA = JAVA_VENDOR_NAME.contains("IBM");

  /**
   * @return true if current processor is 64 bit
   */
  public static boolean is64Bit() {
    return OSUtils.PROCESSOR_BIT.contains("64");
  }

  /**
   * @return true if current OS is Windows
   */
  public static boolean isWindows() {
    return SystemUtils.IS_OS_WINDOWS;
  }

  /**
   * @return true if current OS is AIX
   */
  public static boolean isAIX() {
    return OSUtils.OS_NAME.equals("AIX");
  }

  private OSUtils() {} // prevent instantiation
}
