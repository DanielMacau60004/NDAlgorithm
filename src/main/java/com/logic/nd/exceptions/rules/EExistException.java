package com.logic.nd.exceptions.rules;

import com.logic.exps.asts.binary.ASTExistential;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class EExistException extends NDRuleException {

    private final ASTEExist rule;
    private ASTExistential exist;

    public EExistException(ASTEExist rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    public EExistException(ASTEExist rule, ASTExistential exist) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
        this.exist = exist;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                if (exist == null) rule.getHyp1().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));

                if (!rule.getConclusion().equals(rule.getHyp2().getConclusion()))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH -> {
                if (exist == null) rule.getHyp1().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be an existential!"));

                if (!rule.getConclusion().equals(rule.getHyp2().getConclusion()))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + rule.getHyp2().getConclusion() + "!"));
                yield error;
            }
            case SOLUTION -> {
                if (exist == null) rule.getHyp1().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be an existential!"));

                if (!rule.getConclusion().equals(rule.getHyp2().getConclusion()))
                    rule.appendErrors(
                            new NDTextException(EFeedbackPosition.CONCLUSION, "This must be " + rule.getHyp2().getConclusion() + "!"));
                if(exist != null) error += "\nPossible solution:";
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (exist != null && level.equals(FeedbackLevel.SOLUTION)) {
            return List.of(
                    new ASTEExist(new ASTHypothesis(rule.getHyp1().getConclusion(), null),
                            new ASTHypothesis(rule.getConclusion(), null),
                            rule.getConclusion(), rule.getM()));
        }
        return null;
    }
}
