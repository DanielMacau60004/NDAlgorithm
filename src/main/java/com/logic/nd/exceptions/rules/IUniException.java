package com.logic.nd.exceptions.rules;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.unary.ASTIUni;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

public class IUniException extends NDRuleException {

    private ASTIUni rule;

    public IUniException(ASTIUni rule) {
        super(FeedbackType.SEMANTIC_ERROR);
        this.rule = rule;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "Something is wrong!"));
                yield error;
            }
            case HIGH,SOLUTION -> {
                rule.appendErrors(
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be a universal!"));
                yield error;
            }
        };
    }
}
