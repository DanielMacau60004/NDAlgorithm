package com.logic.nd.algorithm.proofs;

import com.logic.api.IFormula;

import java.util.Set;

public interface IProofGraph {
    GoalNode getMainGoal();

    GoalNode getTargetGoal();

    Set<IFormula> getPremises();

    ProofGraphSettings getSettings();

    ProofEdge getEdge(GoalNode node);

    void build();

    boolean isSolvable();
}
