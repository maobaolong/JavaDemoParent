package net.mbl.demo.util;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Common utilities
 */
@ThreadSafe
public final class CommonUtils {

  /**
   * Creates new instance of a class by calling a constructor that receives ctorClassArgs arguments.
   *
   * @param <T> type of the object
   * @param cls the class to create
   * @param ctorClassArgs parameters type list of the constructor to initiate, if null default
   *        constructor will be called
   * @param ctorArgs the arguments to pass the constructor
   * @return new class object or null if not successful
   * @throws InstantiationException if the instantiation fails
   * @throws IllegalAccessException if the constructor cannot be accessed
   * @throws NoSuchMethodException if the constructor does not exist
   * @throws SecurityException if security violation has occurred
   * @throws InvocationTargetException if the constructor invocation results in an exception
   */
  public static <T> T createNewClassInstance(Class<T> cls, Class<?>[] ctorClassArgs,
      Object[] ctorArgs) throws InstantiationException, IllegalAccessException,
      NoSuchMethodException, SecurityException, InvocationTargetException {
    if (ctorClassArgs == null) {
      return cls.newInstance();
    }
    Constructor<T> ctor = cls.getConstructor(ctorClassArgs);
    return ctor.newInstance(ctorArgs);
  }

  private CommonUtils() {} // prevent instantiation
}
