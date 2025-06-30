package com.logic.nd.exceptions.sideconditions;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.ASTLiteral;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.ASTIExist;
import com.logic.nd.asts.unary.ASTIUni;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;
import com.logic.others.Levenshtein;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IUniInvalidMappingException extends NDRuleException {

    private final ASTIUni rule;
    private final ASTVariable variable;
    private final IASTExp from;
    private final IASTExp to;
    private final Set<IASTExp> outcomes;

    public IUniInvalidMappingException(ASTIUni rule, ASTVariable variable, IASTExp from, IASTExp to, Set<IASTExp> outcomes) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.rule = rule;
        this.variable = variable;
        this.from = from;
        this.to = to;
        this.outcomes = outcomes;

    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";

        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid rule application!";
            case MEDIUM -> {
                rule.getHyp().appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION, "Invalid mapping!"));
                yield error;
            }
            case HIGH -> {
                rule.getHyp().appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION,
                        "No mapping of " + variable + " in " + from + " that can produce " + to + "!"));
                yield error;
            }
            case SOLUTION -> {
                rule.getHyp().appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION,
                        "No mapping of " + variable + " in " + from + " that can produce " + to + "!"));

                String exp = Levenshtein.findMostSimilar(outcomes.stream().map(Object::toString).collect(Collectors.toSet()), to.toString());
                if(exp != null) yield error + "\nDid you mean:";
                yield error;
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (level.equals(FeedbackLevel.SOLUTION)) {
            String exp = Levenshtein.findMostSimilar(outcomes.stream().map(Object::toString).collect(Collectors.toSet()), to.toString());

            if(exp != null)
                return List.of(new ASTIUni(new ASTHypothesis(new ASTLiteral(exp), null), rule.getConclusion()));
        }

        return null;
    }
}
