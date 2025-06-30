package com.logic.nd.exceptions;

import com.logic.api.INDProof;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.others.Utils;

import java.util.List;

public class NDException extends FeedbackException {

    private final INDProof proof;
    private final NDRuleException mainException;
    private final List<NDRuleException> exceptions;

    public NDException(INDProof proof, FeedbackType type, List<NDRuleException> exceptions,
                       NDRuleException mainException) {
        super(type);

        this.proof = proof;
        this.exceptions = exceptions;
        this.mainException = mainException;
    }

    public NDException(INDProof proof, FeedbackType type, List<NDRuleException> exceptions) {
        super(type);

        this.proof = proof;
        this.exceptions = exceptions;
        this.mainException = null;
    }

    public NDRuleException getMainException() {
        return mainException;
    }

    public List<NDRuleException> getExceptions() {
        return exceptions;
    }

    public INDProof getProof() {
        return proof;
    }

    @Override
    protected String produceFeedback(FeedbackLevel level) {
        StringBuilder feedback = new StringBuilder();
        for(NDRuleException e : exceptions)
            feedback.append(e.getFeedback(level)).append("\n");

        return feedback.toString();
    }
}
