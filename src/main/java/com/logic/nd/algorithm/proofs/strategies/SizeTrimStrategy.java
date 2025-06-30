package com.logic.nd.algorithm.proofs.strategies;

import com.logic.nd.algorithm.proofs.ProofEdge;
import com.logic.nd.algorithm.proofs.GoalNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class SizeTrimStrategy implements ITrimStrategy {

    //Can be slower than HeightTrimStrategy as is some cases require to pass thought the same node and edge multiple times
    //The worst case is O(n^2)
    @Override
    public Map<GoalNode, ProofEdge> trim(IBuildStrategy buildStrategy) {
        Queue<GoalNode> explore = buildStrategy.getClosedNodes();
        Map<GoalNode, Set<ProofEdge>> graph = buildStrategy.getGraph();
        Map<GoalNode, Set<GoalNode>> inverted = buildStrategy.getInvertedGraph();

        Map<GoalNode, ProofEdge> tree = new HashMap<>();
        Map<GoalNode, Integer> explored = new HashMap<>();

        //Reset heights
        graph.keySet().forEach(node -> node.setHeight(node.isClosed() ? 1 : -1));

        while (!explore.isEmpty()) {
            GoalNode state = explore.poll();
            tree.putIfAbsent(state, null);
            state.setClosed();

            if (explored.get(state) != null && explored.get(state) <= state.getHeight()) continue;
            explored.put(state, state.getHeight());

            if (inverted.get(state) != null) {
                for (GoalNode to : inverted.get(state)) {
                    Set<ProofEdge> edges = graph.get(to);
                    if (edges != null) {
                        edges.removeIf(e -> {
                            if (!e.isClosed()) return false;

                            //Ignore node because there is a smaller path to that node
                            if (to.getHeight() > 0 && to.getHeight() <= e.height() + 1) return true;

                            to.setHeight(e.height() + 1);

                            explore.add(to);
                            tree.put(to, e);

                            return true;
                        });
                    }

                }
            }
        }

        return tree;
    }
}
