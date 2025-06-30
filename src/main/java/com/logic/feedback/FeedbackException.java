package com.logic.feedback;

import com.logic.others.Utils;


public abstract class FeedbackException extends RuntimeException {

    protected abstract String produceFeedback(FeedbackLevel level);

    protected FeedbackType type;

    public FeedbackException(FeedbackType type) {
        super("Error found!");
        this.type = type;
    }

    @Override
    public String getMessage() {
        return getFeedback(FeedbackLevel.SOLUTION);
    }

    public String getFeedback(FeedbackLevel level) {
        return Utils.getToken(produceFeedback(level));
    }

    public FeedbackType getType() {
        return type;
    }

}
