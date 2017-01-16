package net.mbl.demo.exception;

/**
 * The exception thrown when an unexpected error occurs within the system.
 */
public final class UnexpectedMyException extends MyException {
  private static final long serialVersionUID = -1029072354884843903L;

  /**
   * @param e an exception to wrap
   */
  public UnexpectedMyException(Exception e) {
    super(e);
  }
}
