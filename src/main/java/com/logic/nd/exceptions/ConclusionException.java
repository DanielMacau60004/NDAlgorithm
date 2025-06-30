package com.logic.nd.exceptions;

import com.logic.api.IFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;

import java.util.Set;
import java.util.stream.Collectors;

public class ConclusionException extends NDRuleException {

    private final Set<IFormula> premises;
    private final IFormula conclusion;

    public ConclusionException(Set<IFormula> premises, IFormula conclusion) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.premises = premises;
        this.conclusion = conclusion;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW, MEDIUM -> "This tree doesn't solve the problem!";
            case HIGH, SOLUTION -> "This tree doesn't solve the problem!\n" +
                    "You proved:\n" +
                    (premises != null && !premises.isEmpty()
                            ? "{"+premises.stream().map(Object::toString).collect(Collectors.joining(", "))+"} "
                            : "") +
                    "⊢ " + conclusion.toString();
        };
    }
}
