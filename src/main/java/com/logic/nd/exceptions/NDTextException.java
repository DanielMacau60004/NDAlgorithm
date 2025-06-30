package com.logic.nd.exceptions;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;

public class NDTextException extends NDRuleException {

    private final String message;

    public NDTextException(EFeedbackPosition position, String message) {
        super(FeedbackType.SEMANTIC_ERROR, position);
        this.message = message;
    }

    @Override
    protected String produceFeedback(FeedbackLevel level) {
        return message;
    }
}
