package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.binary.ASTConditional;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTIImp;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class IImpException extends NDRuleException {

    private final ASTIImp rule;
    private ASTConditional imp;

    public IImpException(ASTIImp rule) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.rule = rule;
    }

    public IImpException(ASTIImp rule, ASTConditional imp) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.imp = imp;
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if(imp == null) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(imp.getRight());
                    if (right != rule.getHyp().getConclusion()) rule.getHyp().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                }
                yield error;
            }
            case HIGH -> {
                if(imp == null) rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be an implication!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(imp.getRight());
                    if (right != rule.getHyp().getConclusion()) rule.getHyp().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + right + "!"));
                }
                yield error;
            }
            case SOLUTION -> {
                if(imp == null)
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be an implication!"));
                else {
                    IASTExp right = ExpUtils.removeParenthesis(imp.getRight());
                    if (right != rule.getHyp().getConclusion()) rule.getHyp().appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + right + "!"));
                }
                if(imp != null) error += "\nPossible solution:";
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (imp != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTIImp(new ASTHypothesis(ExpUtils.removeParenthesis(imp.getRight()),null),
                            imp, rule.getM()));
        }
        return null;
    }
}
