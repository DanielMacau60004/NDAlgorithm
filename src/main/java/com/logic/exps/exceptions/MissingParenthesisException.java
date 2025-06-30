package com.logic.exps.exceptions;

import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.parser.Token;

public class MissingParenthesisException extends ExpException {

    private final Token token;
    private final IASTExp before;

    public MissingParenthesisException(IASTExp before, Token token) {
        super(FeedbackType.SYNTAX_ERROR);

        this.before = before;
        this.token = token;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid expression!";
            case MEDIUM -> "Missing a parenthesis!";
            case HIGH -> "You forgot to close the parentheses!";
            case SOLUTION -> "Consider closing the parenthesis at column " + token.endColumn +
                    ", after \"" + token.image + "\"!";
        };
    }
}
