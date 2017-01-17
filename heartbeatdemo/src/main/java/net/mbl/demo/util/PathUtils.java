package net.mbl.demo.util;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 */
@ThreadSafe
public final class PathUtils {
  public static final String SEPARATOR = "/";

  /**
   * Checks and normalizes the given path.
   *
   * @param path The path to clean up
   * @return a normalized version of the path, with single separators between path components and
   *         dot components resolved
   */
  public static String cleanPath(String path) {
    validatePath(path);
    return FilenameUtils.separatorsToUnix(FilenameUtils.normalizeNoEndSeparator(path));
  }

  /**
   * Joins each element in paths in order, separated by
   * <p>
   * For example,
   *
   * <pre>
   * {@code
   * concatPath("/myroot/", "dir", 1L, "filename").equals("/myroot/dir/1/filename");
   * concatPath("myroot/", "/dir/", "filename").equals("myroot/dir/filename");
   * concatPath("/", "dir", "filename").equals("/dir/filename");
   * }
   * </pre>
   *
   * Note that empty element in base or paths is ignored.
   *
   * @param base base path
   * @param paths paths to concatenate
   * @return joined path
   * @throws IllegalArgumentException if base or paths is null
   */
  public static String concatPath(Object base, Object... paths) throws IllegalArgumentException {
    Preconditions.checkArgument(base != null, "Failed to concatPath: base is null");
    Preconditions.checkArgument(paths != null, "Failed to concatPath: a null set of paths");
    List<String> trimmedPathList = new ArrayList<>();
    String trimmedBase =
        CharMatcher.is(PathUtils.SEPARATOR.charAt(0)).trimTrailingFrom(base.toString().trim());
    trimmedPathList.add(trimmedBase);
    for (Object path : paths) {
      if (path == null) {
        continue;
      }
      String trimmedPath =
          CharMatcher.is(PathUtils.SEPARATOR.charAt(0)).trimFrom(path.toString().trim());
      if (!trimmedPath.isEmpty()) {
        trimmedPathList.add(trimmedPath);
      }
    }
    if (trimmedPathList.size() == 1 && trimmedBase.isEmpty()) {
      // base must be "[/]+"
      return PathUtils.SEPARATOR;
    }
    return Joiner.on(PathUtils.SEPARATOR).join(trimmedPathList);

  }

  /**
   * Gets the parent of the file at a path.
   *
   * @param path The path
   * @return the parent path of the file; this is "/" if the given path is the root
   */
  public static String getParent(String path)  {
    String cleanedPath = cleanPath(path);
    String name = FilenameUtils.getName(cleanedPath);
    String parent = cleanedPath.substring(0, cleanedPath.length() - name.length() - 1);
    if (parent.isEmpty()) {
      // The parent is the root path
      return PathUtils.SEPARATOR;
    }
    return parent;
  }

  /**
   * Gets the path components of the given path. The first component will be an empty string.
   *
   * "/a/b/c" => {"", "a", "b", "c"}
   * "/" => {""}
   *
   * @param path The path to split
   * @return the path split into components
   */
  public static String[] getPathComponents(String path)  {
    path = cleanPath(path);
    if (isRoot(path)) {
      return new String[]{""};
    }
    return path.split(PathUtils.SEPARATOR);
  }

  /**
   * Removes the prefix from the path, yielding a relative path from the second path to the first.
   *
   * If the paths are the same, this method returns the empty string.
   *
   * @param path the full path
   * @param prefix the prefix to remove
   * @return the path with the prefix removed
   */
  public static String subtractPaths(String path, String prefix)  {
    String cleanedPath = cleanPath(path);
    String cleanedPrefix = cleanPath(prefix);
    if (cleanedPath.equals(cleanedPrefix)) {
      return "";
    }
    if (!hasPrefix(cleanedPath, cleanedPrefix)) {
      throw new RuntimeException(
          String.format("Cannot subtract %s from %s because it is not a prefix", prefix, path));
    }
    // The only clean path which ends in '/' is the root.
    int prefixLen = cleanedPrefix.length();
    int charsToDrop = PathUtils.isRoot(cleanedPrefix) ? prefixLen : prefixLen + 1;
    return cleanedPath.substring(charsToDrop, cleanedPath.length());
  }

  /**
   * Checks whether the given path contains the given prefix. The comparison happens at a component
   * granularity; for example, {@code hasPrefix(/dir/file, /dir)} should evaluate to true, while
   * {@code hasPrefix(/dir/file, /d)} should evaluate to false.
   *
   * @param path a path
   * @param prefix a prefix
   * @return whether the given path has the given prefix
   */
  public static boolean hasPrefix(String path, String prefix)  {
    String[] pathComponents = getPathComponents(path);
    String[] prefixComponents = getPathComponents(prefix);
    if (pathComponents.length < prefixComponents.length) {
      return false;
    }
    for (int i = 0; i < prefixComponents.length; i++) {
      if (!pathComponents[i].equals(prefixComponents[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the given path is the root.
   *
   * @param path The path to check
   * @return true if the path is the root
   */
  public static boolean isRoot(String path)  {
    return PathUtils.SEPARATOR.equals(cleanPath(path));
  }

  /**
   * Checks if the given path is properly formed.
   *
   * @param path The path to check
   */
  public static void validatePath(String path)  {
    boolean invalid = (path == null || path.isEmpty() || path.contains(" "));
    if (!OSUtils.isWindows()) {
      invalid = (invalid || !path.startsWith(PathUtils.SEPARATOR));
    }


  }

  /**
   * Creates a unique path based off the caller.
   *
   * @return unique path based off the caller
   */
  public static String uniqPath() {
    StackTraceElement caller = new Throwable().getStackTrace()[1];
    long time = System.nanoTime();
    return "/" + caller.getClassName() + "/" + caller.getMethodName() + "/" + time;
  }

  /**
   * Adds a trailing separator if it does not exist in path.
   *
   * @param path the file name
   * @param separator trailing separator to add
   * @return updated path with trailing separator
   */
  public static String normalizePath(String path, String separator) {
    return path.endsWith(separator) ? path : path + separator;
  }

  private PathUtils() {} // prevent instantiation
}
