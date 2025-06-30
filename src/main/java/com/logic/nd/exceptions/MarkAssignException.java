package com.logic.nd.exceptions;

import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MarkAssignException extends NDRuleException {

    private final String mark;
    private final IASTExp exp;

    public MarkAssignException(String mark, IASTExp exp) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.mark = mark;
        this.exp = exp;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid mark!";
            case MEDIUM -> "Mark " + mark + " already assigned!";
            case HIGH-> "Mark " + mark + " assigned to " + exp + "!";
            case SOLUTION-> "Mark " + mark + " assigned to " + exp + "!\nConsider assigning a different mark!";
        };
    }
}
