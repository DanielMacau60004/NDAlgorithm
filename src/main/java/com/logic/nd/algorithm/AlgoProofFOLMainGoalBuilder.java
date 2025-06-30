package com.logic.nd.algorithm;

import com.logic.api.IFOLFormula;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTVariable;

import java.util.HashSet;
import java.util.Set;

public class AlgoProofFOLMainGoalBuilder extends AlgoProofFOLGoalBuilder {

    public final Set<IFOLFormula> premises;

    public AlgoProofFOLMainGoalBuilder(IFOLFormula state) {
        super(state);

        state.iterateTerms().forEachRemaining(terms::add);
        premises = new HashSet<>();
    }

    public AlgoProofFOLMainGoalBuilder addPremise(IFOLFormula premise) {
        premise.iterateTerms().forEachRemaining(terms::add);
        this.premises.add(premise);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addPremises(Set<IFOLFormula> premises) {
        premises.forEach(premise -> premise.iterateTerms().forEachRemaining(terms::add));
        this.premises.addAll(premises);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addHypothesis(IFOLFormula hypothesis) {
        hypothesis.iterateTerms().forEachRemaining(terms::add);
        this.hypotheses.add(hypothesis);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addHypotheses(Set<IFOLFormula> hypotheses) {
        hypotheses.forEach(hypothesis -> hypothesis.iterateTerms().forEachRemaining(terms::add));
        this.hypotheses.addAll(hypotheses);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public AlgoProofFOLMainGoalBuilder setNoFreeVariables(Set<ASTVariable> notFreeVars) {
        this.notFreeVars.addAll(notFreeVars);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addNoFreeVariable(ASTVariable notFreeVar) {
        this.notFreeVars.add(notFreeVar);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addTerm(AASTTerm term) {
        this.terms.add(term);
        return this;
    }

    public AlgoProofFOLMainGoalBuilder addTerms(Set<AASTTerm> terms) {
        this.terms.addAll(terms);
        return this;
    }

}
