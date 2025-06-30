package com.logic.nd.exceptions.rules;

import com.logic.exps.ExpUtils;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.unary.ASTIExist;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

public class IExistException extends NDRuleException {

    private ASTIExist rule;

    public IExistException(ASTIExist rule) {
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
                        new NDTextException(EFeedbackPosition.CONCLUSION, "This must be an existential!"));
                yield error;
            }
        };
    }
}
