package com.logic.nd.algorithm.proofs;

import com.logic.nd.algorithm.AlgoProofPLMainGoalBuilder;
import com.logic.nd.algorithm.AlgoProofPLGoalBuilder;
import com.logic.nd.algorithm.transition.ITransitionGraph;

import java.util.*;

public class ProofGraphPL extends ProofGraph {

    public ProofGraphPL(AlgoProofPLMainGoalBuilder problem, AlgoProofPLGoalBuilder state,
                        ITransitionGraph transitionGraph, ProofGraphSettings settings) {
        super(new HashSet<>(problem.premises), transitionGraph, settings);

        this.mainGoal = problem.build(handler);
        targetGoal = state.build(handler);
    }

}
