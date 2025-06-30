package com.logic.exps.exceptions;

import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;

public class AmbiguousException extends ExpException {

    private final IASTExp before;
    private final IASTExp after;

    public AmbiguousException(IASTExp before, IASTExp after) {
        super(FeedbackType.SYNTAX_ERROR);

        this.before = before;
        this.after = after;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid expression!";
            case MEDIUM -> "Ambiguous expression!";
            case HIGH -> "Ambiguous expression, consider adding parenthesis!";
            case SOLUTION -> "Ambiguous expression, consider adding parentheses around " + before +"!";
        };
    }
}
