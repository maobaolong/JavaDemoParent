
package net.mbl.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.core.Response;

/**
 * Utilities for handling REST calls.
 */
public final class RestUtils {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /**
   * Calls the given {@link RestUtils.RestCallable} and handles any exceptions thrown.
   *
   * @param <T> the return type of the callable
   * @param callable the callable to call
   * @return the response object
   */
  public static <T> Response call(RestUtils.RestCallable<T> callable) {

    try {
      return createResponse(callable.call());
    } catch (Exception e) {
      LOG.error("Unexpected error invoking rest endpoint", e);
      return createErrorResponse(e.getMessage());
    }
  }

  /**
   * An interface representing a callable.
   *
   * @param <T> the return type of the callable
   */
  public interface RestCallable<T> {
    /**
     * The REST endpoint implementation.
     *
     * @return the return value from the callable
     * @throws Exception if an exception occurs
     */
    T call() throws Exception;
  }

  /**
   * Creates a response using the given object.
   *
   * @param object the object to respond with
   * @return the response
   */
  private static Response createResponse(Object object) {
    if (object instanceof Void) {
      return Response.ok().build();
    }
    if (object instanceof String) {
      // Need to explicitly encode the string as JSON because Jackson will not do it automatically.
      ObjectMapper mapper = new ObjectMapper();
      try {
        return Response.ok(mapper.writeValueAsString(object)).build();
      } catch (JsonProcessingException e) {
        return createErrorResponse(e.getMessage());
      }
    }
    return Response.ok(object).build();
  }

  /**
   * Creates an error response using the given message.
   *
   * @param message the message to respond with
   * @return the response
   */
  private static Response createErrorResponse(String message) {
    return Response.serverError().entity(message).build();
  }

  private RestUtils() {} // prevent instantiation
}
