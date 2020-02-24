package net.mbl.grpcfull.common.retry;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Policy for determining whether retries should be performed, and potentially waiting for some time
 * before the next retry attempt. The way that this interface works is that the logic
 * for delayed retries (retries that sleep) can delay the caller of {@link #attempt()}.
 */
@NotThreadSafe
public interface RetryPolicy {

    /**
     * How many retries have been performed. If no retries have been performed, 0 is returned.
     *
     * @return number of retries performed
     */
    int getAttemptCount();

    /**
     * Waits until it is time to perform the next retry, then returns. Returns false if no further
     * retries should be performed. The first call to this method should never delay the caller, this
     * allow users of the policy to use it in the context of a while-loop.
     *
     * @return whether another retry should be performed
     */
    boolean attempt();
}
