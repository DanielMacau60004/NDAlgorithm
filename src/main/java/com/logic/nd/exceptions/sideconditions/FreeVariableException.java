package com.logic.nd.exceptions.sideconditions;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;

import java.util.List;

public class FreeVariableException extends NDRuleException {

    private final List<IASTND> freeHypotheses;
    private final ASTVariable variable;
    private final ASTVariable from;

    public FreeVariableException(List<IASTND> freeHypotheses, ASTVariable variable, ASTVariable from) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.freeHypotheses = freeHypotheses;
        this.variable = variable;
        this.from = from;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Something is wrong!";
            case MEDIUM -> {
                for(IASTND h : freeHypotheses)
                    h.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION,"Something is wrong!"));

                yield "Missing side condition!";
            }
            case HIGH,SOLUTION -> {
                for(IASTND h : freeHypotheses)
                    h.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION, "Open hypothesis!"+
                            (from != null && variable != null ? "\nVariables: " +from + " ≠ " + variable : "")+
                            (variable != null ? "\nVariable " + variable.getName() + " appears free!" : "\nFree variable!")));

                yield error;
            }
        };
    }
}
