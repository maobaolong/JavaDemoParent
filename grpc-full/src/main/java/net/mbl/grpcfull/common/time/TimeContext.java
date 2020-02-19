package net.mbl.grpcfull.common.time;

import java.time.Clock;

/**
 * Context for managing time.
 */
public final class TimeContext {
    public static final TimeContext SYSTEM =
            new TimeContext(Clock.systemUTC(), ThreadSleeper.INSTANCE);

    private final Clock mClock;
    private final Sleeper mSleeper;

    /**
     * @param clock   the clock for this context
     * @param sleeper the sleeper for this context
     */
    public TimeContext(Clock clock, Sleeper sleeper) {
        mClock = clock;
        mSleeper = sleeper;
    }

    /**
     * @return the clock for this context
     */
    public Clock getClock() {
        return mClock;
    }

    /**
     * @return the sleeper for thix context
     */
    public Sleeper getSleeper() {
        return mSleeper;
    }
}
