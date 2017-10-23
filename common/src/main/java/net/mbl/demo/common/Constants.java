package net.mbl.demo.common;

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

  public static final long SECOND = 1000;
  public static final long MINUTE = SECOND * 60L;
  public static final long HOUR = MINUTE * 60L;
  public static final long DAY = HOUR * 24L;

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  public static final String LS_FORMAT_PERMISSION = "%-15s";
  public static final String LS_FORMAT_FILE_SIZE = "%-10s";
  public static final String LS_FORMAT_CREATE_TIME = "%-25s";
  public static final String LS_FORMAT_FILE_TYPE = "%-15s";
  public static final String LS_FORMAT_USER_NAME = "%-15s";
  public static final String LS_FORMAT_GROUP_NAME = "%-15s";
  public static final String LS_FORMAT_FILE_PATH = "%-5s";
  public static final String LS_FORMAT = LS_FORMAT_PERMISSION + LS_FORMAT_USER_NAME
      + LS_FORMAT_GROUP_NAME + LS_FORMAT_FILE_SIZE + LS_FORMAT_CREATE_TIME + LS_FORMAT_FILE_TYPE
      + LS_FORMAT_FILE_PATH + "%n";
  public static final String LS_FORMAT_NO_ACL = LS_FORMAT_FILE_SIZE + LS_FORMAT_CREATE_TIME
      + LS_FORMAT_FILE_TYPE + LS_FORMAT_FILE_PATH + "%n";

  public static final long SECOND_NANO = 1000000000L;
  public static final int SECOND_MS = 1000;
  public static final int MINUTE_MS = SECOND_MS * 60;
  public static final int HOUR_MS = MINUTE_MS * 60;
  public static final int DAY_MS = HOUR_MS * 24;

  public static final int BYTES_IN_INTEGER = 4;

  public static final long UNKNOWN_SIZE = -1;

  public static final int MAX_PORT = 65535;

  public static final String REST_API_PREFIX = "/api/v1";

  public static final String MASTER_COLUMN_FILE_PREFIX = "COL_";

  public static final String SWIFT_AUTH_KEYSTONE = "keystone";
  public static final String SWIFT_AUTH_KEYSTONE_V3 = "keystonev3";
  public static final String SWIFT_AUTH_SWIFTAUTH = "swiftauth";

  public static final String MESOS_LOCAL_INSTALL = "LOCAL";

  /**
   * Maximum number of seconds to wait for thrift servers to stop on shutdown. Tests use a value of
   * 0 instead of this value so that they can run faster.
   */
  public static final int THRIFT_STOP_TIMEOUT_SECONDS = 60;

  // Time-to-live
  public static final long NO_TTL = -1;

  // Security
  public static final int DEFAULT_FILE_SYSTEM_UMASK = 0022;
  public static final short DEFAULT_FILE_SYSTEM_MODE = (short) 0777;
  public static final short FILE_DIR_PERMISSION_DIFF = (short) 0111;
  public static final short INVALID_MODE = -1;

  // Specific tier write
  public static final int FIRST_TIER = 0;
  public static final int SECOND_TIER = 1;
  public static final int LAST_TIER = -1;

  private Constants() {} // prevent instantiation
}
