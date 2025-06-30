package com.logic.nd.exceptions;

import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.others.Env;

import java.util.*;
import java.util.stream.Collectors;

public class CloseMarkException extends NDRuleException {

    private final String mark;
    private final IASTExp exp;
    private final Env<String, IASTExp> env;

    public CloseMarkException(String mark, IASTExp exp, Env<String, IASTExp> env) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.mark = mark;
        this.exp = exp;
        this.env = env;
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid mark!";
            case MEDIUM -> "This rule cannot close mark " + mark + "!";
            case HIGH -> {
                String error = "This rule cannot close mark " + mark + "!";
                if (exp != null) error += "\nOnly marks with " + exp + "!";
                yield error;
            }
            case SOLUTION -> {
                String error = "This rule cannot close mark " + mark + "!";
                if (exp != null) error += "\nOnly marks with " + exp + "!";

                Set<String> possibleMarks = env.getMatchingChild(exp);
                if (possibleMarks != null && !possibleMarks.isEmpty())
                    error += "\nConsider:";

                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        Set<String> envMap = env.getMatchingChild(exp);

        if (level.equals(FeedbackLevel.SOLUTION)) {
            Set<String> possibleMarks = envMap.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!possibleMarks.isEmpty())
                return List.of(new ASTHypothesis(exp, possibleMarks.stream().findFirst().get()));
        }

        return null;
    }
}
