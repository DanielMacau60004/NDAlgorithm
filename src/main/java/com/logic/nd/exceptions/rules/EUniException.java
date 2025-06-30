package com.logic.nd.exceptions.rules;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.unary.ASTEUni;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

public class EUniException extends NDRuleException {

    private ASTEUni rule;

    public EUniException(ASTEUni rule) {
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
            case HIGH,SOLUTION -> {
                rule.getHyp().appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a universal!"));
                yield error;
            }
        };
    }

}
