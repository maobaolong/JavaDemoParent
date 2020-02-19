package net.mbl.grpcfull.common.time;

import java.time.Duration;

/**
 * A sleeping utility which delegates to Thread.sleep().
 */
public final class ThreadSleeper implements Sleeper {
    public static final ThreadSleeper INSTANCE = new ThreadSleeper();

    private ThreadSleeper() {
    } // Use ThreadSleeper.INSTANCE instead.

    @Override
    public void sleep(Duration duration) throws InterruptedException {
        Thread.sleep(duration.toMillis());
    }
}
