package com.logic.nd.algorithm.proofs.strategies;

import com.logic.nd.algorithm.proofs.ProofEdge;
import com.logic.nd.algorithm.proofs.GoalNode;

import java.util.Map;

public interface ITrimStrategy {

    Map<GoalNode, ProofEdge> trim(IBuildStrategy buildStrategy);
}
