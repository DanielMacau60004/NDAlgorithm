package com.logic.nd.algorithm;

import com.logic.api.IPLFormula;

import java.util.HashSet;
import java.util.Set;

public class AlgoProofPLMainGoalBuilder extends AlgoProofPLGoalBuilder {

    public final Set<IPLFormula> premises;

    public AlgoProofPLMainGoalBuilder(IPLFormula state) {
        super(state);

        this.premises = new HashSet<>();
    }

    public AlgoProofPLMainGoalBuilder addPremise(IPLFormula premise) {
        this.premises.add(premise);
        return this;
    }

    public AlgoProofPLMainGoalBuilder addPremises(Set<IPLFormula> premises) {
        this.premises.addAll(premises);
        return this;
    }

    public AlgoProofPLMainGoalBuilder addHypothesis(IPLFormula hypothesis) {
        this.hypotheses.add(hypothesis);
        return this;
    }

    public AlgoProofPLMainGoalBuilder addHypotheses(Set<IPLFormula> hypotheses) {
        this.hypotheses.addAll(hypotheses);
        return this;
    }

    public AlgoProofPLMainGoalBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

}
