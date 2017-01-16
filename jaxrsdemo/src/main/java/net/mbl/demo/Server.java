package net.mbl.demo;

/**
 * Interface representing an Server.
 */
public interface Server {

  /**
   * Starts the server.
   *
   * @throws Exception if the server fails to start
   */
  void start() throws Exception;

  /**
   * Stops the server. Here, anything created or started in {@link #start()} should be
   * cleaned up and shutdown.
   *
   * @throws Exception if the server fails to stop
   */
  void stop() throws Exception;
}
