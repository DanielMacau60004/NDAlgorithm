package com.logic.nd.algorithm.proofs;

import com.logic.api.IFormula;

public class ProofTransitionEdge {

    private final GoalNode node;
    private final IFormula produces;

    public ProofTransitionEdge(GoalNode transition, IFormula produces) {
        this.node = transition;
        this.produces = produces;
    }

    public IFormula getProduces() {
        return produces;
    }

    public GoalNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
