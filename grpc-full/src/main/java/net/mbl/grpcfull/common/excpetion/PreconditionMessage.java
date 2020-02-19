package net.mbl.grpcfull.common.excpetion;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Precondition messages.
 * <p>
 * Note: To minimize merge conflicts, please sort alphabetically in this section.
 */
@ThreadSafe
public enum PreconditionMessage {
    CHANNEL_NULL_WHEN_CONNECTED(
            "The client channel should never be null when the client is connected"),
    EMPTY("");

    private final String mMessage;

    PreconditionMessage(String message) {
        mMessage = message;
    }

    @Override
    public String toString() {
        return mMessage;
    }
}
