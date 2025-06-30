package com.logic.nd.exceptions.rules;

import com.logic.exps.asts.binary.ASTOr;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class EDisException extends NDRuleException {

    private final ASTEDis rule;
    private ASTOr or;

    public EDisException(ASTEDis rule) {
        this(rule, null);
    }

    public EDisException(ASTEDis rule, ASTOr or) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.or = or;
        this.rule = rule;
    }

    private void appendSubErrors(String dis, String hyps) {
        if (or == null)
            rule.getHyp1().appendErrors(
                    new NDTextException(EFeedbackPosition.CONCLUSION, dis));
        if (!rule.getConclusion().equals(rule.getHyp2().getConclusion()))
            rule.getHyp2().appendErrors(
                    new NDTextException(EFeedbackPosition.CONCLUSION, hyps));
        if (!rule.getConclusion().equals(rule.getHyp3().getConclusion()))
            rule.getHyp3().appendErrors(
                    new NDTextException(EFeedbackPosition.CONCLUSION, hyps));

    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM ->{
                appendSubErrors("Something is wrong!", "Something is wrong!");
                yield error;
            }
            case HIGH -> {
                appendSubErrors("This must be a disjunction!", "This must be " + rule.getConclusion() + "!");
                yield error;
            }
            case SOLUTION -> {
                appendSubErrors("This must be a disjunction!", "This must be " + rule.getConclusion() + "!");
                if(or != null) error += "\nPossible solution:";
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (or != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTEDis(new ASTHypothesis(rule.getHyp1().getConclusion(), null),
                            new ASTHypothesis(rule.getConclusion(), null),
                            new ASTHypothesis(rule.getConclusion(), null),
                            rule.getConclusion(), rule.getM(), rule.getN()));
        }
        return null;
    }
}
