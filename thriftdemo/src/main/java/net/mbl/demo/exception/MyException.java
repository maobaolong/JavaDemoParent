package net.mbl.demo.exception;


import net.mbl.demo.thrift.MyTException;

import javax.annotation.concurrent.ThreadSafe;

/**
 * General {@link MyException} used throughout the system. It must be able serialize itself to
 * the RPC framework and convert back without losing any necessary information.
 */
@ThreadSafe
public class MyException extends Exception {
  private static final long serialVersionUID = 2243833925609642384L;

  /**
   * Constructs a {@link MyException} with an exception type from a {@link MyTException}.
   *
   * @param te the type of the exception
   */
  protected MyException(MyTException te) {
    super(te.getMessage());
  }

  /**
   * Constructs an {@link MyException} with the given cause.
   *
   * @param cause the cause
   */
  protected MyException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an {@link MyException} with the given message.
   *
   * @param message the message
   */
  protected MyException(String message) {
    super(message);
  }

  /**
   * Constructs an {@link MyException} with the given message and cause.
   *
   * @param message the message
   * @param cause the cause
   */
  protected MyException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a {@link MyTException} from a {@link MyException}.
   *
   * @return a {@link MyTException} of the type of this exception
   */
  public MyTException toThrift() {
    return new MyTException(MyExceptionType.getMyExceptionType(getClass()),
        getMessage(), getClass().getName());
  }

  /**
   * Converts an exception from Thrift representation to native representation.
   *
   * @param e the Thrift exception
   * @return the native exception
   */
  public static MyException fromThrift(MyTException e) {
    try {
      Class<? extends MyException> throwClass;
      if (e.isSetClassName()) {
        // server version 1.1.0 or newer
        throwClass = (Class<? extends MyException>) Class.forName(e.getClassName());
      } else {
        // server version 1.0.x
        throwClass = MyExceptionType.getMyExceptionClass(e.getType());
      }
      if (throwClass == null) {
        throwClass = MyException.class;
      }
      return throwClass.getConstructor(String.class).newInstance(e.getMessage());
    } catch (ReflectiveOperationException reflectException) {
      String errorMessage = "Could not instantiate " + e.getType() + " with a String-only "
          + "constructor: " + reflectException.getMessage();
      throw new IllegalStateException(errorMessage, reflectException);
    }
  }

  /**
   * Holds the different types of myExceptions .
   *
   * @deprecated since version 1.1 and will be removed in version 2.0
   */
  @ThreadSafe
  @Deprecated
  private enum MyExceptionType {
    CONNECTION_FAILED(ConnectionFailedException.class);

    private final Class<? extends MyException> mExceptionClass;

    /**
     * Constructs a {@link MyExceptionType} with its corresponding {@link MyException}.
     */
    MyExceptionType(Class<? extends MyException> exceptionClass) {
      mExceptionClass = exceptionClass;
    }

    /**
     * Produces an my exception whose type matches the given name.
     *
     * @param text the type name
     * @return the my exception
     */
    static Class<? extends MyException> getMyExceptionClass(String text) {
      if (text != null) {
        for (MyExceptionType t : MyExceptionType.values()) {
          if (text.equalsIgnoreCase(t.name())) {
            return t.mExceptionClass;
          }
        }
      }
      return null;
    }

    /**
     * Produces the type name for the type that matches the given my exception.
     *
     * @param e the my exception
     * @return the type name
     */
    static String getMyExceptionType(Class<? extends MyException> e) {
      if (e != null) {
        for (MyExceptionType t : MyExceptionType.values()) {
          if (t.mExceptionClass.equals(e)) {
            return t.name();
          }
        }
      }
      return null;
    }
  }
}
