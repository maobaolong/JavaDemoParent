package net.mbl.grpcfull.common;

/**
 * Interface representing a process.
 */
public interface Process {

    /**
     * Starts the process. This call blocks until the process is stopped via {@link #stop()}.
     * The {@link #waitForReady(int)} method can be used to make sure that the process is ready to
     * serve requests.
     */
    void start() throws Exception;

    /**
     * Stops the process, blocking until the action is completed.
     */
    void stop() throws Exception;

    /**
     * Waits until the process is ready to serve requests.
     *
     * @param timeoutMs how long to wait in milliseconds
     * @return whether the process became ready before the specified timeout
     */
    boolean waitForReady(int timeoutMs);
}
