package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTAbsurdity;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class AbsurdityException extends NDRuleException {

    private final ASTAbsurdity rule;

    public AbsurdityException(ASTAbsurdity rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH -> {
                rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));
                yield error;
            }
            case SOLUTION -> {
                rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));
                yield error + "\nPossible solution:";
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTAbsurdity(new ASTHypothesis(ExpUtils.BOT, null),
                            rule.getConclusion(), rule.getM()));
        }
        return null;
    }

}
