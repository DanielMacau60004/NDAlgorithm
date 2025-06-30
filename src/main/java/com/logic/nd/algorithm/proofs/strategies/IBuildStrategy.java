package com.logic.nd.algorithm.proofs.strategies;

import com.logic.nd.algorithm.proofs.*;
import com.logic.nd.algorithm.transition.ITransitionGraph;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public interface IBuildStrategy {

    void build(GoalNode initialNode, ITransitionGraph transitionGraph, ProofGraphSettings settings);

    Map<GoalNode, Set<ProofEdge>> getGraph();

    Map<GoalNode, Set<GoalNode>> getInvertedGraph();

    Queue<GoalNode> getClosedNodes();

}
