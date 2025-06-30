package com.logic.nd.exceptions.sideconditions;

import com.logic.api.IFOLFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.exps.interpreters.FOLReplaceExps;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.unary.ASTEUni;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.nd.exceptions.NDRuleException;
import com.logic.nd.exceptions.NDTextException;
import com.logic.others.AlphabetSequenceIterator;

import java.util.List;

public class EUniNotFreeVariableException extends NDRuleException {

    private final ASTEUni rule;
    private final AASTTerm term;
    private final IASTExp from;
    private final IFOLFormula to;


    public EUniNotFreeVariableException(ASTEUni rule, AASTTerm term, IASTExp from, IFOLFormula to) {
        super(FeedbackType.SEMANTIC_ERROR);

        this.rule = rule;
        this.term = term;
        this.from = from;
        this.to = to;
    }

    protected String produceFeedback(FeedbackLevel level) {
        String error = "Error in this rule!";
        return switch (level) {
            case NONE -> "";
            case LOW -> "Something is wrong!";
            case MEDIUM -> "Missing side condition!";
            case HIGH -> {
                 rule.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION,
                         "Term " + term + " is not free to " + from + " in " + to + "!"));

                yield error;
            }
            case SOLUTION -> {
                rule.appendErrors(new NDTextException(EFeedbackPosition.CONCLUSION,
                        "Term " + term + " is not free to " + from + " in " + to + "!"));

                yield error + "\nPossible solution: ";
            }
        };
    }

    @Override
    public List<IASTND> getPreviews(FeedbackLevel level) {
        if (level.equals(FeedbackLevel.SOLUTION)) {
            ASTVariable var;
            IASTExp formula = null;
            AlphabetSequenceIterator it = new AlphabetSequenceIterator(9);
            while (it.hasNext()) {
                var = new ASTVariable(it.next());
                if (!to.isABoundedVariable(var)) {
                    formula = FOLReplaceExps.replace(to.getAST(), from, var);
                    break;
                }
            }

            return List.of(new ASTEUni(rule.getHyp(), formula));

        }
        return null;
    }
}

