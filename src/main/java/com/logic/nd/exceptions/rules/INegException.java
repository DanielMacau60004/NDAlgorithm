package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTAbsurdity;
import com.logic.nd.asts.unary.ASTINeg;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class INegException extends NDRuleException {

    private ASTINeg rule;
    private ASTNot neg;

    public INegException(ASTINeg rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    public INegException(ASTINeg rule, ASTNot neg) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.rule = rule;
        this.neg = neg;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if(!rule.getHyp().getConclusion().equals(ExpUtils.BOT)) rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                if(neg == null)
                    rule.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH -> {
                if(!rule.getHyp().getConclusion().equals(ExpUtils.BOT)) rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));
                if(neg == null)
                    rule.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a negation!"));
                yield error;
            }
            case SOLUTION -> {
                if(!rule.getHyp().getConclusion().equals(ExpUtils.BOT)) rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a " + ExpUtils.BOT + "!"));
                if(neg == null)
                    rule.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a negation!"));

                if(neg != null) yield error + "\nPossible solution:";
                yield error ;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (neg != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTINeg(new ASTHypothesis(ExpUtils.BOT, null),
                            neg, rule.getM()));
        }
        return null;
    }
}
