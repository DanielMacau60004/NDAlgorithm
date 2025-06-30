package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTOr;
import com.logic.exps.asts.unary.ASTParenthesis;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTIRDis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class IRDisException extends NDRuleException {

    private final ASTIRDis rule;
    private ASTOr or;

    public IRDisException(ASTIRDis rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    public IRDisException(ASTIRDis rule, ASTOr or) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.or = or;
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {

        IASTExp hyp = rule.getHyp().getConclusion();
        if(!ExpUtils.isLiteral(hyp)) hyp = new ASTParenthesis(hyp);

        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if (or == null) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                else if (or.getLeft() != rule.getHyp().getConclusion()) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH -> {
                if (or == null) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));
                else if (or.getLeft() != rule.getHyp().getConclusion()) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " +
                                new ASTOr(hyp,or.getRight())+ "!"));
                yield error;
            }
            case SOLUTION -> {
                if (or == null)
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));
                else if (or.getLeft() != rule.getHyp().getConclusion()) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " +
                                new ASTOr(hyp,or.getRight())+ "!"));
                if (or != null) error += "\nPossible solution:";
                yield error;
            }

        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (or != null && level.equals(FeedbackLevel.SOLUTION)) {
            IASTExp hyp = rule.getHyp().getConclusion();
            if(!ExpUtils.isLiteral(hyp)) hyp = new ASTParenthesis(hyp);

            return List.of(
                    new ASTIRDis(new ASTHypothesis(rule.getHyp().getConclusion(), null),
                            new ASTOr(hyp, or.getRight())));
        }
        return null;
    }
}
