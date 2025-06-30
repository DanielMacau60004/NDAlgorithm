package com.logic.nd.algorithm;

import com.logic.api.IPLFormula;
import com.logic.nd.algorithm.proofs.BitGraphHandler;
import com.logic.nd.algorithm.proofs.GoalNode;

import java.util.HashSet;
import java.util.Set;

public class AlgoProofPLGoalBuilder {

    protected final IPLFormula goal;
    protected final Set<IPLFormula> hypotheses;
    protected int height;

    public AlgoProofPLGoalBuilder(IPLFormula goal) {
        this.goal = goal;
        this.height = 0;
        this.hypotheses = new HashSet<>();
    }

    public AlgoProofPLGoalBuilder addHypothesis(IPLFormula hypothesis) {
        this.hypotheses.add(hypothesis);
        return this;
    }

    public AlgoProofPLGoalBuilder addHypotheses(Set<IPLFormula> hypotheses) {
        this.hypotheses.addAll(hypotheses);
        return this;
    }

    public AlgoProofPLGoalBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public GoalNode build(BitGraphHandler handler) {
        return new GoalNode(handler.getIndex(goal), handler.toBitSet(new HashSet<>(hypotheses)),
                height, null, handler);
    }

}
