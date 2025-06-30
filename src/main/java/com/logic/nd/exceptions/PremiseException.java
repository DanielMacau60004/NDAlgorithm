package com.logic.nd.exceptions;

import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.others.Env;

import java.util.*;
import java.util.stream.Collectors;

public class PremiseException extends NDRuleException {

    private final IASTExp premise;
    private final Set<IASTExp> premises;
    private final Env<String, IASTExp> env;
    private final boolean hasMark;

    public PremiseException(IASTExp premise, boolean hasMark, Set<IASTExp> premises, Env<String, IASTExp> env) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.hasMark = hasMark;
        this.premise = premise;
        this.premises = premises;

        this.env = env;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Not a premise.\n" + (!hasMark ? "A mark must be assigned!" : "That mark cannot be closed by any rule!");

        return switch (level) {
            case NONE -> "";
            case LOW -> "Not a premise!";
            case MEDIUM -> error;
            case HIGH -> {
                List<?> list = env.mapParent().entrySet().stream()
                        .filter(k -> k.getKey() != null).toList();
                if (!list.isEmpty())
                    error += "\nAvailable marks:";
                yield error;
            }
            case SOLUTION -> {
                Set<String> possibleMarks = env.getMatchingParent(premise);
                if (possibleMarks != null && !possibleMarks.isEmpty())
                    error += "\nConsider:";
                else {
                    List<?> list = env.mapParent().entrySet().stream()
                            .filter(k -> k.getKey() != null).toList();
                    if (!list.isEmpty())
                        error += "\nAvailable marks:";
                }
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        Set<String> envMap = env.getMatchingParent(premise);

        if (level.equals(FeedbackLevel.SOLUTION)) {
            Set<String> possibleMarks = envMap.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            if (!possibleMarks.isEmpty())
                return List.of(new ASTHypothesis(premise, possibleMarks.stream().findFirst().get()));
        }

        if (level.equals(FeedbackLevel.HIGH) || level.equals(FeedbackLevel.SOLUTION)) {
            List<IASTND> marks = new ArrayList<>();
            Map<String, IASTExp> available = env.mapParent();

            if (!available.isEmpty()) {
                available.entrySet().stream()
                        .filter(k -> k.getKey() != null)
                        .forEach(k -> marks.add(new ASTHypothesis(k.getValue(), k.getKey())));
                return marks;
            }
        }
        return null;
    }
}
