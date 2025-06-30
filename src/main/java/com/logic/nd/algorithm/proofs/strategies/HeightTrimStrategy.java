package com.logic.nd.algorithm.proofs.strategies;

import com.logic.nd.algorithm.proofs.ProofEdge;
import com.logic.nd.algorithm.proofs.GoalNode;

import java.util.*;

public class HeightTrimStrategy implements ITrimStrategy {

    //The final answer may differ, it always returns solutions with the lowest height possible
    //It is faster time complexity O(E+N)
    @Override
    public Map<GoalNode, ProofEdge> trim(IBuildStrategy buildStrategy) {
        Queue<GoalNode> explore = buildStrategy.getClosedNodes();
        Map<GoalNode, Set<ProofEdge>> graph = buildStrategy.getGraph();
        Map<GoalNode, Set<GoalNode>> inverted = buildStrategy.getInvertedGraph();

        Map<GoalNode, ProofEdge> tree = new HashMap<>();
        Set<GoalNode> explored = new HashSet<>();

        while (!explore.isEmpty()) {
            GoalNode state = explore.poll();
            tree.putIfAbsent(state, null);
            state.setClosed();

            if (explored.contains(state)) continue;
            explored.add(state);

            if (inverted.get(state) != null) {
                for (GoalNode to : inverted.get(state)) {
                    Set<ProofEdge> edges = graph.get(to);
                    if (edges != null) {

                        Optional<ProofEdge> e = edges.stream().filter(ProofEdge::isClosed).findFirst();
                        if (e.isPresent()) {
                            explore.add(to);

                            if (!tree.containsKey(to))
                                tree.put(to, e.get());
                        }
                    }
                }
            }
        }

        return tree;
    }
}
