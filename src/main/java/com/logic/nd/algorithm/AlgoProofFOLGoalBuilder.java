package com.logic.nd.algorithm;

import com.logic.api.IFOLFormula;
import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.algorithm.proofs.BitGraphHandler;
import com.logic.nd.algorithm.proofs.GoalNode;

import java.util.HashSet;
import java.util.Set;

public class AlgoProofFOLGoalBuilder {

    protected final IFOLFormula goal;
    protected final Set<IFOLFormula> hypotheses;
    protected final Set<ASTVariable> notFreeVars;
    protected final Set<AASTTerm> terms;
    protected int height;

    public AlgoProofFOLGoalBuilder(IFOLFormula goal) {
        this.goal = goal;
        this.height = 0;
        this.hypotheses = new HashSet<>();
        this.notFreeVars = new HashSet<>();
        this.terms = new HashSet<>();

        goal.iterateTerms().forEachRemaining(terms::add);
    }

    public AlgoProofFOLGoalBuilder addHypothesis(IFOLFormula hypothesis) {
        hypothesis.iterateTerms().forEachRemaining(terms::add);
        this.hypotheses.add(hypothesis);
        return this;
    }

    public AlgoProofFOLGoalBuilder addHypotheses(Set<IFOLFormula> hypotheses) {
        hypotheses.forEach(hypothesis -> hypothesis.iterateTerms().forEachRemaining(terms::add));
        this.hypotheses.addAll(hypotheses);
        return this;
    }

    public AlgoProofFOLGoalBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public AlgoProofFOLGoalBuilder setNoFreeVariables(Set<ASTVariable> notFreeVars) {
        this.notFreeVars.addAll(notFreeVars);
        return this;
    }

    public AlgoProofFOLGoalBuilder addNoFreeVariable(ASTVariable notFreeVar) {
        this.notFreeVars.add(notFreeVar);
        return this;
    }

    public AlgoProofFOLGoalBuilder addTerm(AASTTerm term) {
        this.terms.add(term);
        return this;
    }

    public AlgoProofFOLGoalBuilder addTerms(Set<AASTTerm> terms) {
        this.terms.addAll(terms);
        return this;
    }

    public GoalNode build(Set<IFOLFormula> premises, BitGraphHandler handler) {
        Set<IFOLFormula> notFree = new HashSet<>();
        Set<IFOLFormula> formulas = new HashSet<>(premises);
        formulas.addAll(hypotheses);

        for (IFOLFormula formula : formulas) {
            for (ASTVariable var : notFreeVars)
                if (formula.appearsFreeVariable(var)) notFree.add(formula);
        }

        return new GoalNode(handler.getIndex(goal), handler.toBitSet(new HashSet<>(hypotheses)),
                height, handler.toBitSet(new HashSet<>(notFree)), handler);
    }

}
