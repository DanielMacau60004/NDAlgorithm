package com.logic.exps.exceptions;

import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.parser.TokenMgrError;

public class ExpLexicalException extends ExpException {

    protected TokenMgrError exception;

    public ExpLexicalException(TokenMgrError exception) {
        super(FeedbackType.LEXICAL_ERROR);

        this.exception = exception;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid expression!";
            case MEDIUM, HIGH, SOLUTION -> "Lexical error!";
        };
    }
}
