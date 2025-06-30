package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTAnd;
import com.logic.exps.asts.unary.ASTParenthesis;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class IConjException extends NDRuleException {

    private final ASTIConj rule;
    private ASTAnd and;

    public IConjException(ASTIConj rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    public IConjException(ASTIConj rule, ASTAnd and) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.and = and;
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";

        IASTExp left = rule.getHyp1().getConclusion();
        IASTExp right = rule.getHyp2().getConclusion();

        if(!ExpUtils.isLiteral(left)) left = new ASTParenthesis(left);
        if(!ExpUtils.isLiteral(right)) right = new ASTParenthesis(right);

        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if (and == null)
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                else {
                    if (!and.getLeft().equals(rule.getHyp1().getConclusion()) ||
                            !and.getRight().equals(rule.getHyp2().getConclusion()))
                        rule.appendErrors(
                                new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                }

                yield error;
            }
            case HIGH -> {
                if (and == null)
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));
                else {
                    if (!and.getLeft().equals(rule.getHyp1().getConclusion()) ||
                            !and.getRight().equals(rule.getHyp2().getConclusion()))
                        rule.appendErrors(
                                new NDTextException(EFeedbackPosition.CONCLUSION, "This must be "+
                                        new ASTAnd(left, right)+"!"));
                }
                yield error;
            }
            case SOLUTION -> {
                if (and == null)
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a conjunction!"));

                else {
                    if (!and.getLeft().equals(rule.getHyp1().getConclusion()) ||
                            !and.getRight().equals(rule.getHyp2().getConclusion()))
                        rule.appendErrors(
                                new NDTextException(EFeedbackPosition.CONCLUSION, "This must be "+
                                        new ASTAnd(left, right)+"!"));
                }
                if(and != null) error += "\nPossible solution:";
                yield error;
            }
        };
    }


    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (and != null && level.equals(FeedbackLevel.SOLUTION)) {

            IASTExp left = rule.getHyp1().getConclusion();
            IASTExp right = rule.getHyp2().getConclusion();

            if(!ExpUtils.isLiteral(left)) left = new ASTParenthesis(left);
            if(!ExpUtils.isLiteral(right)) right = new ASTParenthesis(right);

            return List.of(
                    new ASTIConj(rule.getHyp1(),rule.getHyp2(),
                            new ASTAnd(left, right)));
        }
        return null;
    }
}
