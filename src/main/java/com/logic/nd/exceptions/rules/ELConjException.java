package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTAnd;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTELConj;
import com.logic.nd.asts.unary.ASTERConj;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class ELConjException extends NDRuleException {

    private final ASTELConj rule;
    private ASTAnd and;

    public ELConjException(ASTELConj rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    public ELConjException(ASTELConj rule, ASTAnd and) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.and = and;
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if (and == null) rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(and.getRight());
                    if (right != rule.getConclusion()) rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                }
                yield error;
            }
            case HIGH -> {
                if (and == null) rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(and.getRight());
                    if (right != rule.getConclusion()) rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + right + "!"));
                }
                yield error;
            }
            case SOLUTION -> {
                if (and == null)
                    rule.getHyp().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(and.getRight());
                    if (right != rule.getConclusion()) rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + right + "!"));
                }
                if (and != null) error += "\nPossible solution:";
                yield error;
            }

        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (and != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTERConj(new ASTHypothesis(and, null),
                            ExpUtils.removeParenthesis(and.getRight())));
        }
        return null;
    }
}
