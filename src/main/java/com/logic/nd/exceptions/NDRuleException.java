package com.logic.nd.exceptions;

import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;

import java.util.List;

public abstract class NDRuleException extends FeedbackException {

    protected EFeedbackPosition position;

    public NDRuleException(FeedbackType type) {
        super(type);
        this.position = EFeedbackPosition.RULE;
    }

    public NDRuleException(FeedbackType type, EFeedbackPosition position) {
        super(type);
        this.position = position;
    }

    public List<IASTND> getPreviews(FeedbackLevel level) {
        return null;
    }

    public EFeedbackPosition getPosition() {
        return position;
    }
}
