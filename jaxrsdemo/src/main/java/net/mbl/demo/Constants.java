package net.mbl.demo;

import javax.annotation.concurrent.ThreadSafe;

/**
 * System wide constants.
 */
@ThreadSafe
public final class Constants {
  public static final int KB = 1024;
  public static final int MB = KB * 1024;
  public static final int GB = MB * 1024;
  public static final long TB = GB * 1024L;
  public static final long PB = TB * 1024L;


  public static final long SECOND_NANO = 1000000000L;
  public static final int SECOND_MS = 1000;
  public static final int MINUTE_MS = SECOND_MS * 60;
  public static final int HOUR_MS = MINUTE_MS * 60;
  public static final int DAY_MS = HOUR_MS * 24;


  public static final int MAX_PORT = 65535;

  public static final String REST_API_PREFIX = "/api/v1";

  public static final String LOGGER_TYPE = "logger.type.jaxrsdemo";

  // prevent instantiation
  private Constants() {}
}
