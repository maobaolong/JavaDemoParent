package net.mbl.grpcfull.common.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * Utilities for performing retries.
 */
public final class RetryUtils {
    private static final Logger LOG = LoggerFactory.getLogger(RetryUtils.class);

    private RetryUtils() {
    } // prevent instantiation

    /**
     * Retries the given method until it doesn't throw an IO exception or the retry policy expires. If
     * the retry policy expires, the last exception generated will be rethrown.
     *
     * @param action a description of the action that fits the phrase "Failed to ${action}"
     * @param f      the function to retry
     * @param policy the retry policy to use
     */
    public static void retry(String action, RunnableThrowsIOException f, RetryPolicy policy)
            throws IOException {
        IOException e = null;
        while (policy.attempt()) {
            try {
                f.run();
                return;
            } catch (IOException ioe) {
                e = ioe;
                LOG.warn("Failed to {} (attempt {}): {}", action, policy.getAttemptCount(), e.toString());
            }
        }
        throw e;
    }

    /**
     * Gives a ClientRetry based on the given parameters.
     *
     * @param maxRetryDuration the maximum total duration to retry for
     * @param baseSleepMs initial sleep time in milliseconds
     * @param maxSleepMs max sleep time in milliseconds
     * @return the default client retry
     */
    public static RetryPolicy defaultClientRetry(Duration maxRetryDuration, Duration baseSleepMs,
            Duration maxSleepMs) {
        return ExponentialTimeBoundedRetry.builder()
                .withMaxDuration(maxRetryDuration)
                .withInitialSleep(baseSleepMs)
                .withMaxSleep(maxSleepMs)
                .build();
    }

    /**
     * Interface for methods which return nothing and may throw IOException.
     */
    @FunctionalInterface
    public interface RunnableThrowsIOException {
        /**
         * Runs the runnable.
         */
        void run() throws IOException;
    }
}
