package com.logic.exps.exceptions;

import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;

public class FunctionArityException extends ExpException {

    private final String name;
    private final int expectedArity;
    private final int foundArity;

    public FunctionArityException(String name, int expectedArity, int foundArity) {
        super(FeedbackType.SYNTAX_ERROR);

        this.name = name;
        this.expectedArity = expectedArity;
        this.foundArity = foundArity;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid expression!";
            case MEDIUM -> "Invalid function arity!";
            case HIGH -> "Invalid function arity in function \""+name+"\"!";
            case SOLUTION -> "Invalid function arity in function \"" + name +
                    "\" found " + foundArity + " but expected " + expectedArity + "!"
            ;
        };
    }
}
