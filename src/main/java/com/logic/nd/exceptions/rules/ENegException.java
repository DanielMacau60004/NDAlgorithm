package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class ENegException extends NDRuleException {

    private final ASTENeg rule;
    private final IASTExp exp;

    public ENegException(ASTENeg rule, IASTExp exp) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
        this.exp = exp;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if (!rule.getConclusion().equals(ExpUtils.BOT))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));

                if (exp != null)
                    rule.getHyp2().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH -> {
                if (!rule.getConclusion().equals(ExpUtils.BOT))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));

                if (exp != null)
                    rule.getHyp2().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + ExpUtils.negate(exp) + "!"));
                yield error;
            }
            case SOLUTION -> {
                if (!rule.getConclusion().equals(ExpUtils.BOT))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));

                if (exp != null)
                    rule.getHyp2().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + ExpUtils.negate(exp) + "!"));
                if(exp != null) error += "\nPossible solution:";
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (exp != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTENeg(
                            new ASTHypothesis(exp, null),
                            new ASTHypothesis(ExpUtils.negate(exp), null),
                            ExpUtils.BOT));
        }
        return null;
    }

}
