package net.mbl.grpcfull.common.time;

import java.time.Duration;

/**
 * An interface for a utility which provides a sleep method.
 */
public interface Sleeper {

    /**
     * Sleeps for the given duration.
     *
     * @param duration the duration to sleep for
     * @throws InterruptedException if the sleep is interrupted
     */
    void sleep(Duration duration) throws InterruptedException;
}
