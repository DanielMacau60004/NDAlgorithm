package com.logic.nd.exceptions;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.parser.TokenMgrError;

public class NDLexicalException extends NDRuleException {

    protected TokenMgrError exception;

    public NDLexicalException(TokenMgrError exception) {
        super(FeedbackType.LEXICAL_ERROR);

        this.exception = exception;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid proof!";
            case MEDIUM, HIGH, SOLUTION -> "Lexical error!";
        };
    }
}
